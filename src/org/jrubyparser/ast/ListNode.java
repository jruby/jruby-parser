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

import java.util.ArrayList;
import java.util.List;
import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * All Nodes which have a list representation inherit this.  This is also used
 * as generic container for additional information that is not directly evaluated.
 * In particular, f_arg production rule uses this to capture arg information for
 * the editor projects who want position info saved.
 */
public class ListNode extends Node {
    private List<Node> list;

    /**
     * Create a new ListNode.
     *
     * @param position type of listnode
     * @param firstNode first element of the list
     */
    public ListNode(SourcePosition position, Node firstNode) {
        this(position);

        list = new ArrayList<Node>();
        list.add(adopt(firstNode));
    }

    public ListNode(SourcePosition position) {
        super(position);

        list = new ArrayList<Node>(0);
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

        ListNode other = (ListNode) node;
        if (size() != other.size()) return false;

        for (int i = 0; i <= size() - 1; i++) {
            if (!get(i).isSame(other.get(i))) return false;
        }

        return true;
    }


    public NodeType getNodeType() {
        return NodeType.LISTNODE;
    }

    public ListNode add(Node node) {
        list.add(adopt(node));

        if (node == null) return this;

        if (getPosition() == null) {
            setPosition(node.getPosition());
        } else {
            setPosition(getPosition().union(node.getPosition()));
        }

        return this;
    }

    public ListNode prepend(Node node) {
        // Ruby Grammar productions return plenty of nulls.
        if (node == null) return this;

        list.add(0, adopt(node));

        setPosition(getPosition().union(node.getPosition()));
        return this;
    }

    public int size() {
        return list.size();
    }

    /**
     * Add all elements in other list to this list node.
     *
     * @param other list which has elements
     * @return this instance for method chaining
     */
    public ListNode addAll(ListNode other) {
        if (other != null && other.size() > 0) {
            for (Node e: other.list) {
                adopt(e);
            }
            list.addAll(other.list);
            setPosition(getPosition().union(getLastNodePosition()));
        }
        return this;
    }

    /**
     * @return the position of the last node in this list that has a valid position
     * (i.e. is not a NilImplicitNode).
     */
    private SourcePosition getLastNodePosition() {
        // sometimes the last node is a NilImplicitNode which has no valid position
        for (int i = list.size() - 1; i >= 0; i--) {
            Node last = list.get(i);
            if (last != null) return last.getPosition();
        }
        return null;
    }

    /**
     * Add other element to this list
     *
     * @param other list which has elements
     * @return this instance for method chaining
     */
    public ListNode addAll(Node other) {
        return add(other);
    }

    public Node getLast() {
    	return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitListNode(this);
    }

    public Node get(int idx) {
        return list.get(idx);
    }
}
