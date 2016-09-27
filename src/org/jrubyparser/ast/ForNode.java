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

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.StaticScope;

/**
 * A 'for' statement.  This is implemented using iter and that is how MRI does things,
 * but 'for's do not have their own stack, so doing this way is mildly painful.
 *
 * @see IterNode
 */
public class ForNode extends IterNode {

    private Node iterNode;

    public ForNode(SourcePosition position, Node varNode, Node bodyNode, Node iterNode, StaticScope scope) {
        // For nodes do not have their own scope so we pass null to indicate this.
        // 'For's are implemented as blocks in evaluation, but they have no scope so we
        // just deal with this lack of scope throughout its lifespan.  We should probably
        // change the way this works to get rid of multiple null checks.
        super(position, varNode, scope, bodyNode);

        assert iterNode != null : "iterNode is not null";

        this.iterNode = adopt(iterNode);
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

        ForNode other = (ForNode) node;

        if (getVar() == null && other.getVar() == null) return getIter().isSame(other.getIter()) && getBody().isSame(other.getBody());
        if (getVar() == null || other.getVar() == null) return false;

        return getVar().isSame(other.getVar()) && getIter().isSame(other.getIter()) && getBody().isSame(other.getBody());
    }


    @Override
    public NodeType getNodeType() {
        return NodeType.FORNODE;
    }

    public Node getIter() {
        return iterNode;
    }

    @Deprecated
    public Node getIterNode() {
        return getIter();
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    @Override
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitForNode(this);
    }
}
