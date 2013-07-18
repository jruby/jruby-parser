/*
 ***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2013 The JRuby team
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jrubyparser.ast;

import java.util.ArrayList;
import java.util.List;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.ISourcePositionHolder;
import org.jrubyparser.SourcePosition;

/**
 * Base class for all Nodes in the AST
 */
public abstract class Node implements ISourcePositionHolder {    
    private SourcePosition position;
    
    private Node parent = null;

    private List<Node> children = new ArrayList<Node>();    

    public Node(SourcePosition position) {
        assert position != null;
        this.position = position;
    }

    /**
     * Location of this node within the source
     */
    public SourcePosition getPosition() {
        return position;
    }
    
    // Parentage methods
    
    public Node adopt(Node child) {
        if (child != null) {
            child.setParent(this);
            children.add(child);
        }
        
        return child;
    }
    
    public Node adopt(Node child, int index) {
        if (child != null) {
            child.setParent(this);
            children.add(index, child);
        }
        
        return child;
    }
    
    /**
     * Adopt the node in it's proper location amongst the children of this node.
     * Used internally by insertNode.  It is possible subclasses will know enough to use it
     * so it is marked protected. 
     */
    protected Node adoptUsingNodesPosition(Node node) {
        int i = 0;
        boolean added = false;
        for (Node child: childNodes()) {
            int direction = child.comparePositionWith(node);
                
            if (direction < 0) { // Immediately before current child
                adopt(node, i);
                added = true;
                break;
            } else if (direction == 0) { // inside child
                child.insertNode(node);
                added = true;
                break;
            }
                
            i++;
        }
        
        if (!added) adopt(node);  // must be after last child
        
        return node;
    }
    
    public Node getParent() {
        return parent;
    }
    
    public Node getGrandParent() {
        Node p = getParent();
        return p != null ? p.getParent() : null;
    }
    
    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    /**
     * Is this node the same or a descendent of the supplied testParent node?
     */
    public boolean isDescendentOf(Node testParent) {
        for (Node current = this; current != null; current = current.getParent()) {
            if (current == testParent) return true;
        }
        
        return false;
    }

    public void setPosition(SourcePosition position) {
        this.position = position;
    }
    
    public abstract Object accept(NodeVisitor visitor);
    public List<Node> childNodes() {
        return children;
    }

    protected static List<Node> createList(Node... nodes) {
        ArrayList<Node> list = new ArrayList<Node>();
        
        for (Node node: nodes) {
            if (node != null) list.add(node);
        }
        
        return list;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(60);

        builder.append("(").append(getNodeName());

        if (this instanceof INameNode) {
            builder.append(":").append(((INameNode) this).getName());
        }
        
        for (Node child: childNodes()) {
            builder.append(", ").append(child);
        }
        builder.append(")");

        return builder.toString();
    }

    protected String getNodeName() {
        String name = getClass().getName();
        int i = name.lastIndexOf('.');
        String nodeType = name.substring(i + 1);
        return nodeType;
    }
    
    public SourcePosition getPositionIncludingComments() {
        List<CommentNode> comments = getPreviousComments();
        
        if (comments.isEmpty()) return getPosition();
        
        return comments.get(0).getPosition().union(getPosition());
    }

    /**
     * Is the current node something that is syntactically visible in the AST.  IDE consumers
     * should ignore these elements.
     */
    public boolean isInvisible() {
        return this instanceof InvisibleNode;
    }
    
    /**
     * Is this AST node considered a leaf node?
     */
    public boolean isLeaf() {
        return childNodes().isEmpty();
    }

    /**
     * @return the nodeId
     */
    public abstract NodeType getNodeType();

    /**
     * Put entire list of nodes into their proper positions based on the SourcePosition specified
     * by each node in the list.  This list must be in sorted order to work.
     * 
     * @param nodes 
     */
    public void insertAll(List<? extends Node> nodes) {
        if (nodes == null || nodes.isEmpty()) return;

        for (Node current: nodes) {
            insertNode(current);
        }
    }
    
    public void insertNode(Node node) {
        int direction = comparePositionWith(node);
        
        if (direction < 0) {
            if (getParent() == null) { // first-line comment
                adoptUsingNodesPosition(node);
            } else {
                insertBefore(node);
            }
        } else if (direction > 0) {
            insertAfter(node);
        } else {
            adoptUsingNodesPosition(node);
        }
    }
    
    public void insertBefore(Node newNode) {
        getParent().adopt(newNode, getParent().childNodes().indexOf(this));
    }
    
    public void insertAfter(Node newNode) {
        getParent().adopt(newNode, getParent().childNodes().indexOf(this) + 1);
    }
    
    /**
     * Is the testNode before, inside, or after this node?
     * 
     * @return -1 if before, 0 is inside, or 1 if after
     */
    public int comparePositionWith(Node testNode) {
        if (testNode.getPosition().getStartOffset() < getPosition().getStartOffset()) return -1;
        if (testNode.getPosition().getEndOffset() > getPosition().getEndOffset()) return 1;
        
        return 0;
    }
    
    /**
     * Look for all comment nodes immediately preceeding this one.  Additional pure-syntax nodes
     * will not break up contiguous comments (e.g. extra whitespace or an errant ';').
     */
    public List<CommentNode> getPreviousComments() {
        List<CommentNode> comments = new ArrayList<CommentNode>();
        Node parent = getParent();
        
        if (parent == null) return comments;
        
        List<Node> siblings = parent.childNodes();

        int thisIndex = siblings.indexOf(this);
        
        if (thisIndex == 0) {
            // # one\ndef foo... and similar are pretty common to see a newline node in the middle
            if (getParent() instanceof NewlineNode) {
                // top of file will start out script with a block
                comments = getParent().getPreviousComments();
                
                if (comments.size() == 0 && getParent().getParent() instanceof BlockNode) {
                    return getParent().getParent().getPreviousComments();
                }
            }

            return comments;
        }
        
        for (int i = thisIndex - 1; i >= 0; i--) {
            Node current = siblings.get(i);
            
            if (!(current instanceof SyntaxNode)) break;
            if (current instanceof CommentNode) comments.add((CommentNode) current);
        }
        
        return comments;
    }
    
    /**
     * Get the comment which happens to appear on the same line as this node immediately after it.
     */
    public CommentNode getInlineComment() {
        List<Node> siblings = getParent().childNodes();

        int thisIndex = siblings.indexOf(this);
        
        if (thisIndex + 1 > siblings.size()) return null;
        
        Node nextNode = siblings.get(thisIndex + 1);
        
        if (nextNode instanceof CommentNode) return (CommentNode) nextNode;
        
        return null;
    }
    
    /**
     * Find the leaf node (which is not invisible) at the specified offset).
     * @param offset in characters into the source unit
     */
    public Node getNodeAt(int offset) {
        // offset < 0 is for method chaining of methods which will return -1 if an index or node is not found (baby optimization)
        if (isInvisible() || offset < 0) return null;
        
        for (Node child : childNodes()) {  // Check children for more specific results
            if (child.isInvisible()) continue;

            Node found = child.getNodeAt(offset);
            if (found != null && !found.getPosition().isEmpty()) return found; // refactoring includes place-holders (empty)...ignore them
        }

        return getPosition().isWithin(offset) ? this : null;
    }

    /**
     * Which method is this node contained in?
     * @return the method or null if one cannot be found
     */
    public MethodDefNode getMethodFor() {
        if (isInvisible()) return null; // FIXME: Invisible nodes do not have reasonable parentage
        
        for (Node p = this; p != null; p = p.getParent()) {
            if (p instanceof MethodDefNode) return (MethodDefNode) p;
        }
        
        return null;
    }
    
    /**
     * Get closest parent Module/Class/SClass for this node
     */
    public IModuleScope getClosestModule() {
        if (isInvisible()) return null; // FIXME: Invisible nodes do not have reasonable parentage
        
        IScope p = getClosestIScope();
        
        while (p != null && !(p instanceof IModuleScope)) {
            p = ((Node) p).getClosestIScope();
        }
        
        return (IModuleScope) p; // null or an IModuleScope
    }
    
    /**
     * Return closest iter node unless this is contained within a non-block scope and then return
     * null instead.
     */
    public IterNode getInnermostIter() {
        if (isInvisible()) return null; // FIXME: Invisible nodes do not have reasonable parentage
        
        for (Node p = this; p != null; p = p.getParent()) {
            if (p instanceof ILocalScope) return null; // foo { def bar; im_here; end }
            if (p instanceof IterNode) return (IterNode) p;
        }
        
        return null;
    }

    public IterNode getOutermostIter() {
        IterNode nextIter = getInnermostIter();
        
        for (IterNode iter = nextIter; iter != null && iter.getParent() != null; iter = nextIter) {
            nextIter = iter.getParent().getInnermostIter();
            if (nextIter == null) return iter;
        }
        
        return null;
    }
    
    /**
     * Get the most immediate ParameterScope.
     * @return 
     */
    public IScope getClosestIScope() {
        if (isInvisible()) return null; // FIXME: Invisible nodes do not have reasonable parentage
        
        for (Node current = this.getParent(); current != null; current = current.getParent()) {
            if (current instanceof IScope) return (IScope) current;
        }

        return null; // Should never happen since Root is a ILocalScope        
    }
    
    // Common predicates...The default on node will typically be false with specialized nodes conditionally being true
    
    /**
     * Is this node specifying a parameter in a block statement?
     */
    public boolean isBlockParameter() {
        IterNode iter = getInnermostIter();
        
        return iter != null && this instanceof ILocalVariable && isDescendentOf(iter.getVar());
    }

    /**
     * Is this node specifying a parameter in a method definition?
     */    
    public boolean isMethodParameter() {
        MethodDefNode def = getMethodFor();
        
        return def != null && this instanceof ILocalVariable && isDescendentOf(def.getArgs());
    }
}
