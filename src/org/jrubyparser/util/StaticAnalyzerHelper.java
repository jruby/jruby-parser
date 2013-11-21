/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.util;

import java.util.ArrayList;
import java.util.List;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.ast.ArgsCatNode;
import org.jrubyparser.ast.ArgsPushNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.MultipleAsgnNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.NodeType;
import org.jrubyparser.ast.ToAryNode;

/**
 * Separate logic from AST for calculating static relationships in Ruby.
 */
public class StaticAnalyzerHelper {
    private static ListNode EMPTY = new ListNode(new SourcePosition());
    private static short PRE = 0;
    private static short SPLAT = 1;
    private static short POST = 2;

    // FIXME: This feels really icky..
    public static List<NodePair> calculateStaticAssignments(MultipleAsgnNode masgn, Node values) {
        List<NodePair> assignments = new ArrayList<NodePair>();
        Node[] rhs = flattenRHSValues(values);
        ListNode rhsPre = (ListNode) rhs[PRE];
        int rhsPreSize = rhsPre.size();
        int handled = 0;

        if (masgn.getPreCount() > 0) {
            for (int i = 0; i < masgn.getPreCount(); i++) {
                assignments.add(new NodePair(masgn.getPre().get(i), i < rhsPreSize ? rhsPre.get(i) : null));
                handled++;
            }
        }

        if (masgn.getRest() != null) {
            ListNode rhsPost = (ListNode) rhs[POST];
            int rhsPostSize = rhsPost.size();
            
            if (rhs[SPLAT] != null) {
                if (rhsPostSize == masgn.getPostCount()) {
                    if (handled == rhsPreSize) assignments.add(new NodePair(masgn.getRest(), rhs[SPLAT]));
                    
                    for (int i = 0; i < rhsPostSize; i++) {
                        assignments.add(new NodePair(masgn.getPost().get(i), rhsPost.get(i)));
                    }
                } else if (rhsPostSize <= masgn.getPostCount()) {
                    assignments.add(new NodePair(masgn.getRest(), null));
                    
                    int pivot = masgn.getPostCount() - rhsPostSize;
                    for (int i = 0; i < masgn.getPostCount(); i++) {
                        assignments.add(new NodePair(masgn.getPost().get(i), i >= pivot ? rhsPost.get(i-pivot) : null));        
                    }
                } else {
                    assignments.add(new NodePair(masgn.getRest(), null));
                    
                    int pivot = rhsPostSize - masgn.getPostCount();
                    for (int i = 0; i < masgn.getPostCount(); i++) {
                        assignments.add(new NodePair(masgn.getPost().get(i), rhsPost.get(i+pivot)));
                    }
                }
            } else if (handled <= rhsPreSize) { // leftover right hand elements (!*rhs, !rhspost)
                int leftOver = rhsPreSize - handled;

                // We only care when we discover rest arg on lhs since even those this is syntactically broken 
                // (extra args and no rest or post) we don't care.  We just figure out what is reasonable.
                if (masgn.getRest() != null) { // *lhs, ?lhspost
                    int postSize = masgn.getPostCount();
                        
                    if (leftOver > postSize) {
                        ListNode list = new ListNode(rhsPre.get(handled).getPosition());
                            
                        for (int i = 0; i < leftOver - postSize; i++) {
                            list.add(rhsPre.get(handled + i));
                        }
                        handled += leftOver - postSize;
                            
                        assignments.add(new NodePair(masgn.getRest(), list));
                            
                        for (int i = 0; i < postSize; i++) {
                            assignments.add(new NodePair(masgn.getPost().get(i), rhsPre.get(handled + i)));
                        }
                    } else if (leftOver == postSize) {
                        // I cannot find case where handled is zero but I have seen it occur in netbeans so I am doing what seems appropriate for that case
                        SourcePosition pos = handled == 0 ? 
                                (postSize == 0 ? masgn.getPosition().makeEmptyPositionAfterThis() :
                                    rhsPre.get(0).getPosition().makeEmptyPositionBeforeThis()) : 
                                rhsPre.get(handled-1).getPosition();
                                
                        assignments.add(new NodePair(masgn.getRest(), new ListNode(pos)));
                        for (int i = 0; i < postSize; i++) {
                            assignments.add(new NodePair(masgn.getPost().get(i), rhsPre.get(handled + i)));
                        }
                    }
                }
            } else { // no left over elements and but left over lhs ones (!*rhs, !rhspost)
                assignments.add(new NodePair(masgn.getRest(), null));
                
                int postSize = masgn.getPostCount();                            
                for (int i = 0; i < postSize; i++) {
                    assignments.add(new NodePair(masgn.getPost().get(i), null));
                }                
            }
        }
        
        return assignments;
    }

    /**
     * There are four possible conditions:
     * 1. rhs: [b, c] - ListNode
     * 2. rhs: [*b, c] - ArgsPush(b, c)
     * 3. rhs: [c, *b] - ArgsCat(c, b)
     * 4. rhs: [c, *b, d] - ArgsPush(ArgsCat(c, b), d)
     * 
     * For cases 2 and 3 we can additionally return the rhs splat as a single value if
     * the argscat or argspush will end up completely being assigned into a rest of the lhs.
     * These four cases can have any number of pre or post nodes (these examples only use one
     * for simplicity).  This method really just flattens the tree into pre, rest, and post.
     * Now in actuality these three values will not neccesarily fit into the lhs in the same
     * fashion.
     */
    public static Node[] flattenRHSValues(Node rhs) {
        // FIXME: Identify what this is?
        if (rhs == null) return new Node[0];
        
        // 1.8-only logic
        if (rhs.getNodeType() == NodeType.TOARYNODE) rhs = ((ToAryNode) rhs).getValue();
        
        Node[] values = new Node[3];
        
        if (rhs instanceof ArgsPushNode) {
            ArgsPushNode push = (ArgsPushNode) rhs;
            
            if (push.getFirstNode() instanceof ArgsCatNode) {
                ArgsCatNode cat = (ArgsCatNode) push.getFirstNode();
                
                values[0] = asList(cat.getFirst()); // pre
                values[1] = cat.getSecond(); // splat
                values[2] = asList(push.getSecondNode()); // post
            } else {
                values[1] = push.getFirstNode(); // splat
                values[2] = asList(push.getSecondNode()); // post;
            }
        } else if (rhs instanceof ArgsCatNode) {
            ArgsCatNode cat = (ArgsCatNode) rhs;
            
            values[0] = asList(cat.getFirst());
            values[1] = cat.getSecond();
        } else { // LISTNODE
            values[0] = asList(rhs);
        }
        
        if (values[0] == null) values[0] = EMPTY;
        if (values[2] == null) values[2] = EMPTY;

        return values;
    }
        
    private static ListNode asList(Node node) {
        // FIXME: This needs an actual position
        if (node == null) return new ListNode(null);
        
        return !(node instanceof ListNode) ? new ListNode(node.getPosition(), node) : (ListNode) node;
        
    }
}
