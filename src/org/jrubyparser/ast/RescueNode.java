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
 * Represents a rescue statement
 */
public class RescueNode extends Node {
    private Node bodyNode;
    private RescueBodyNode rescueNode;
    private Node elseNode;
    
    public RescueNode(SourcePosition position, Node bodyNode, RescueBodyNode rescueNode, Node elseNode) {
        super(position);
        this.bodyNode = adopt(bodyNode);
        this.rescueNode = (RescueBodyNode) adopt(rescueNode);
        this.elseNode = adopt(elseNode);
    }

    @Override
    public boolean isSame(Node node) {
        if (!super.isSame(node)) return false;
            RescueNode other = (RescueNode) node;

            if (getBody() == null && other.getBody() == null) {
                if (getRescue() == null && other.getRescue() == null) {
                    if (getElse() == null && other.getElse() == null) return true;
                    if (getElse() == null || other.getElse() == null) return false;
                    
                    return getElse().isSame(other.getElse());
                } else if (getRescue() == null || other.getRescue() == null) {
                    return false;
                } else if (getElse() == null && other.getElse() == null) {
                    return getRescue().isSame(other.getRescue());
                } else if (getElse() == null || other.getElse() == null) {
                    return false;
                } else {
                    return getElse().isSame(other.getElse()) && getRescue().isSame(other.getRescue());
                }
            } else if (getBody() == null || other.getBody() == null) {
                return false;
            } else if (getRescue() == null && other.getRescue() == null) {
                if (getElse() == null && other.getElse() == null) {
                    return getRescue().isSame(other.getRescue());
                } else if (getElse() == null || other.getElse() == null) {
                    return false;
                } else {
                    return getElse().isSame(other.getElse()) && getBody().isSame(other.getBody());
                }
            } else if (getRescue() == null || other.getRescue() == null) {
                return false;
            } else if (getElse() == null && other.getElse() == null) {
                return getRescue().isSame(other.getRescue()) && getBody().isSame(other.getBody());
            } else if (getElse() == null || other.getElse() == null) {
                return false;
            }

            return getElse().isSame(other.getElse()) && getRescue().isSame(other.getRescue()) && getBody().isSame(other.getBody());
    }

    public NodeType getNodeType() {
        return NodeType.RESCUENODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitRescueNode(this);
    }

    /**
     * Gets the bodyNode.
     * @return Returns a Node
     */
    public Node getBody() {
        return bodyNode;
    }
    
    @Deprecated
    public Node getBodyException() {
        return getBody();
    }
    
    public void setBody(Node body) {
        this.bodyNode = adopt(body);
    }

    /**
     * Gets the elseNode.
     * @return Returns a Node
     */
    public Node getElse() {
        return elseNode;
    }
    
    @Deprecated
    public Node getElseNode() {
        return getElse();
    }
    
    public void setElse(Node elseNode) {
        this.elseNode = adopt(elseNode);
    }

    /**
     * Gets the first rescueNode.
     * @return Returns a Node
     */
    public RescueBodyNode getRescue() {
        return rescueNode;
    }
    
    @Deprecated
    public RescueBodyNode getRescueNode() {
        return getRescue();
    }
    
    public void setRescue(RescueBodyNode rescue) {
        this.rescueNode = (RescueBodyNode) adopt(rescue);
    }
}
