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

/**
 * A Case statement.  Represents a complete case statement, including the body with its
 * when statements.
 */
public class CaseNode extends Node {
	/**
	 * the case expression.
	 **/
    private Node caseNode;
	/**
	 * A list of all choices including else
	 */
    private ListNode cases;
    private Node elseNode = null;

    public CaseNode(SourcePosition position, Node caseNode, ListNode cases) {
        super(position);

        assert cases != null : "caseBody is not null";
        // TODO: Rewriter and compiler assume case when empty expression.  In MRI this is just
        // a when.
//        assert caseNode != null : "caseNode is not null";

        this.caseNode = adopt(caseNode);
        this.cases = (ListNode) adopt(cases);
    }

    public void setElseNode(Node elseNode) {
        this.elseNode = elseNode;
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

        CaseNode other = (CaseNode) node;

        if (getCase() == null && other.getCase() == null) return true;
        if (getCase() == null || other.getCase() == null) return false;

        return getCase().isSame(other.getCase());
    }


    public NodeType getNodeType() {
        return NodeType.CASENODE;
    }

 	/**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitCaseNode(this);
    }

    /**
     * Gets the caseNode.
	 * caseNode is the case expression
     * @return caseNode
     */
    public Node getCase() {
        return caseNode;
    }

    @Deprecated
    public Node getCaseNode() {
        return getCase();
    }

    public ListNode getCases() {
        return cases;
    }

    public Node getElse() {
        return elseNode;
    }

    @Deprecated
    public Node getElseNode() {
        return getElse();
    }

    /**
     * Gets the first whenNode.
	 * the body of the case statement, the first of a list of WhenNodes
     * @return whenNode
     */
    public Node getFirstWhen() {
        return cases;
    }

    @Deprecated
    public Node getFirstWhenNode() {
        return getFirstWhen();
    }
}
