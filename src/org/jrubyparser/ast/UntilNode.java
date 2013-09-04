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
 * Represents an until statement.
 */
public class UntilNode extends Node {
    private Node conditionNode;
    private Node bodyNode;
    private boolean evaluateAtStart;
    
    public boolean containsNonlocalFlow = false;

    public UntilNode(SourcePosition position, Node conditionNode, Node bodyNode) {
        this(position, conditionNode, bodyNode, true);
    }

    public NodeType getNodeType() {
        return NodeType.UNTILNODE;
    }

    public UntilNode(SourcePosition position, Node conditionNode, Node bodyNode, boolean evaluateAtStart) {
        super(position);
        
        assert conditionNode != null : "conditionNode is not null";
        assert bodyNode != null : "bodyNode is not null";
        
        this.conditionNode = adopt(conditionNode);
        this.bodyNode = adopt(bodyNode);
        this.evaluateAtStart = evaluateAtStart;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitUntilNode(this);
    }

     @Override
    public boolean isSame(Node other) {
        if (super.isSame(other)) {
            UntilNode untilNode = (UntilNode) other;
            if (getBody().isSame(untilNode.getBody()) && getCondition().isSame(untilNode.getCondition())) {
                if (evaluateAtStart() == untilNode.evaluateAtStart()) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Gets the bodyNode.
     * @return Returns a Node
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
     * Gets the conditionNode.
     * @return Returns a Node
     */
    public Node getCondition() {
        return conditionNode;
    }
    
    @Deprecated
    public Node getConditionNode() {
        return getCondition();
    }
    
    public void setConditionNode(Node condition) {
        this.conditionNode = adopt(condition);
    }

    /**
     * Determine whether this is while or do while
     * @return true if you are a while, false if do while
     */
    public boolean evaluateAtStart() {
        return evaluateAtStart;
    }
    
}
