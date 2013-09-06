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
 * an 'if' statement.
 */
public class IfNode extends Node {
    private Node condition;
    private Node thenBody;
    private Node elseBody;

    public IfNode(SourcePosition position, Node condition, Node thenBody, Node elseBody) {
        super(position);
        
        assert condition != null : "condition is not null";
        
        this.condition = adopt(condition);
        this.thenBody = adopt(thenBody);
        this.elseBody = adopt(elseBody);
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
            
        IfNode other = (IfNode) node;
        
        if (getThenBody() == null && other.getThenBody() == null) {
            if (getElseBody() == null && other.getElseBody() == null) return getCondition().isSame(other.getCondition());
            if (getElseBody() == null || other.getElseBody() == null) return false;
        } 
        if (getThenBody() == null || other.getThenBody() == null) return false;
        if (getElseBody() == null && other.getElseBody() == null) {
            return getThenBody().isSame(other.getThenBody()) && getCondition().isSame(other.getCondition());
        }
        if (getElseBody() == null || other.getElseBody() == null) return false;

        return getThenBody().isSame(other.getThenBody()) && 
                getElseBody().isSame(other.getElseBody()) && getCondition().isSame(other.getCondition());
    }

    public NodeType getNodeType() {
        return NodeType.IFNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitIfNode(this);
    }

    /**
     * Gets the condition.
     * @return Returns a Node
     */
    public Node getCondition() {
        return condition;
    }

    /**
     * Gets the elseBody.
     * @return Returns a Node
     */
    public Node getElseBody() {
        return elseBody;
    }

    /**
     * Gets the thenBody.
     * @return Returns a Node
     */
    public Node getThenBody() {
        return thenBody;
    }
}
