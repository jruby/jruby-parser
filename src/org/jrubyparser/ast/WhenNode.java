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
 * Represents a when condition
 */
public class WhenNode extends Node {
    protected Node expressionNodes;
    protected Node bodyNode;
    private Node nextCase;

    public WhenNode(SourcePosition position, Node expressionNodes, Node bodyNode, Node nextCase) {
        super(position);

        this.expressionNodes = adopt(expressionNodes);
        this.bodyNode = adopt(bodyNode);
        this.nextCase = adopt(nextCase);
    }

    @Override
    public boolean isSame(Node other) {
        if (super.isSame(other)) {
            WhenNode whenNode = (WhenNode) other;
            if (getBody() != null && whenNode.getBody() != null) {
                if (!getBody().isSame(whenNode.getBody())) {
                    return false;
                }
            }

            if ((getBody() == null && whenNode.getBody() != null) || (getBody() != null && whenNode.getBody() == null)) {
                return false;
            }

            if (getExpression() != null && whenNode.getExpression() != null) {
                if (!getExpression().isSame(whenNode.getExpression())) {
                    return false;
                }
            }

            if ((getExpression() == null && whenNode.getExpression() != null) || (getExpression() != null && whenNode.getExpression() == null)) {
                return false;
            }

             if (getNextCase() != null && whenNode.getNextCase() != null) {
                if (!getNextCase().isSame(whenNode.getNextCase())) {
                    return false;
                }
            }

            if ((getNextCase() == null && whenNode.getNextCase() != null) || (getNextCase() != null && whenNode.getNextCase() == null)) {
                return false;
            }

            return true;
        }
        return false;
    }

    public NodeType getNodeType() {
        return NodeType.WHENNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitWhenNode(this);
    }

    /**
     * Gets the bodyNode.
     * @return Returns a INode
     */
    public Node getBody() {
        return bodyNode;
    }
    
    @Deprecated
    public Node getBodyNode() {
        return getBody();
    }
    
    public void setBody(Node body) {
        this.bodyNode = adopt(body);
    }
    
    /**
     * Gets the next case node (if any).
     */
    public Node getNextCase() {
        return nextCase;
    }

    /**
     * Get the expressionNode(s).
     */
    public Node getExpression() {
        return expressionNodes;
    }
    
    @Deprecated
    public Node getExpressionNode() {
        return getExpression();
    }
    
    public void setExpression(Node expression) {
        this. expressionNodes = adopt(expression);
    }
}
