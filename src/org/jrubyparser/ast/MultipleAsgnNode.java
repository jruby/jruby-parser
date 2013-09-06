/***** BEGIN LICENSE BLOCK *****
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
 * Copyright (C) 2009 Thomas E. Enebo <tom.enebo@gmail.com>
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

import java.util.List;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.util.NodePair;
import org.jrubyparser.util.StaticAnalyzerHelper;

/**
 *
 */
public class MultipleAsgnNode extends AssignableNode {
    private ListNode pre;
    private Node rest;
    private ListNode post;

    // 1.8-only constructor
    public MultipleAsgnNode(SourcePosition position, ListNode pre, Node rest) {
        this(position, pre, rest, null);
    }
    
    // 1.9+ constructor
    public MultipleAsgnNode(SourcePosition position, ListNode pre, Node rest, ListNode post) {
        super(position);
        this.pre = (ListNode) adopt(pre);
        this.rest = adopt(rest);
        this.post = (ListNode) adopt(post);
        
        assert pre != null || rest != null : "pre or rest must exist in a multipleasgn19node";
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node node) {
        if (!super.isSame(node)) return false;

        MultipleAsgnNode other = (MultipleAsgnNode) node;
        
        // Make sure counts are correct since a nodes childNodes will exclude adds of null.
        // Weird scenario of no rest arg method where 2pre == 1pre, 1post without this.
        if (getPreCount() != other.getPreCount() || getPostCount() != other.getPostCount()) return false;
        if (getRest() == null && other.getRest() == null) {
            List<Node> kids = childNodes();
            List<Node> otherKids = childNodes();
                
            for (int i = 0; i < kids.size(); i++) {
                if (!kids.get(i).isSame(otherKids.get(i))) return false;
            }
            return true;
        }
        if (getRest() == null || other.getRest() == null) return false;
        if (!getRest().isSame(other.getRest())) return false;

        List<Node> kids = childNodes();
        List<Node> otherKids = childNodes();
                
        for (int i = 0; i < kids.size(); i++) {
            if (!kids.get(i).isSame(otherKids.get(i))) return false;
        }
        
        return true;
    }


    public NodeType getNodeType() {
        return NodeType.MULTIPLEASGNNODE;
    }

    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitMultipleAsgnNode(this);
    }
    
    public Node getRest() {
        return rest;
    }

    public ListNode getPre() {
        return pre;
    }

    /**
     * Get total count of all pre arguments ({pre},*rest,post).
     */
    public int getPreCount() {
        return pre == null ? 0 : pre.size();
    }

    /**
     * Get total count of all post arguments (pre,*rest,{post}).
     */
    public int getPostCount() {
        return post == null ? 0 : post.size();
    }

    public ListNode getPost() {
        return post;
    }
    
    public int getRequiredCount() {
        return getPreCount() + getPostCount();
    }
    
    /**
     * In cases where some or all the the LHS to RHS assignments are known then return a list of
     * known mappings.  This will only work on a subset of simple static resolvable mappings.  
     * Something like:
     * <pre>
     *   a, *b, c = 1,2,3,4
     * </pre>
     * will work since all values can be computed simply ([a=1,b=[2,3],c=4]).  For the case of b
     * an ArrayNode will get constructed with proper range offsets as a computed node.  This 
     * example used FixnumNodes but the node type can be anything which can be statically resolved.
     * 
     * However:
     * <pre>
     *   a, *b, c = random_call
     *   a, *b, c = *[1,2,3,4]
     * </pre>
     * will not work since we cannot know what random_call will return.  The splat case could be 
     * made to work since it is statically resolvable, but at this point we do not want to 
     * replicate too much of Ruby's semantics.  So statically resolvable but compound static
     * resolution is not currently in scope for this method (although it might be added later).
     * 
     * Note, this will return a partial list when part of the list is statically resolvable and
     * the unresolvable portions will have a null value to indicate they cannot be resolved:
     * <pre>
     *   a, *b, c = 1, *foo
     * </pre>
     * In this case we will get back [a=1,b=null,c=null].  Partial results are still useful for
     * inference in an IDE.
     *
     * @return a list of nodepairs for known bindings and null for values not bound.
     */
    public List<NodePair> calculateStaticAssignments() {
        return StaticAnalyzerHelper.calculateStaticAssignments(this, getValue());
    }
    
    @Override
    public SourcePosition getLeftHandSidePosition() {
        SourcePosition leftPosition = null;
        
        if (getPreCount() > 0) leftPosition = getPre().getPosition();
        if (leftPosition == null && getRest() != null) leftPosition = getRest().getPosition();
        // left position guaranteed non-nill based on constructor contract.
        if (getPostCount() > 0) return leftPosition.union(getPost().getPosition());
        if (getRest() != null && leftPosition != getRest().getPosition()) return leftPosition.union(getRest().getPosition());

        return leftPosition;
    } 
}
