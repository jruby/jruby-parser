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

/**
 *
 */
public class MultipleAsgn19Node extends AssignableNode {
    private ListNode pre;
    private Node rest;
    private ListNode post;

    public MultipleAsgn19Node(SourcePosition position, ListNode pre, Node rest, ListNode post) {
        super(position);
        this.pre = pre;
        this.rest = rest;
        this.post = post;
        
        assert pre != null || rest != null : "pre or rest must exist in a multipleasgn19node";
    }

    public NodeType getNodeType() {
        return NodeType.MULTIPLEASGN19NODE;
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

    public int getPreCount() {
        return pre == null ? 0 : pre.size();
    }

    public int getPostCount() {
        return post == null ? 0 : post.size();
    }

    public ListNode getPost() {
        return post;
    }

    public List<Node> childNodes() {
        return Node.createList(pre, rest, getValue());
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
