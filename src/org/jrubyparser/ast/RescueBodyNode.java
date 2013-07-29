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
 * Represents the contents of a rescue to be evaluated
 */
public class RescueBodyNode extends Node {
    private Node exceptionNodes;
    private Node bodyNode;
    private RescueBodyNode optRescueNode;

    public RescueBodyNode(SourcePosition position, Node exceptionNodes, Node bodyNode, RescueBodyNode optRescueNode) {
        super(position);
        
        this.exceptionNodes = adopt(exceptionNodes);
        this.bodyNode = adopt(bodyNode);
        this.optRescueNode = (RescueBodyNode) adopt(optRescueNode);
    }

    public NodeType getNodeType() {
        return NodeType.RESCUEBODYNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitRescueBodyNode(this);
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
     * Get the next rescue node (if any).
     */
    public RescueBodyNode getOptRescue() {
        return optRescueNode;
    }

    @Deprecated
    public RescueBodyNode getOptRescueNode() {
        return getOptRescue();
    }
    
    public void setOptRescue(RescueBodyNode optRescue) {
        this.optRescueNode = (RescueBodyNode) adopt(optRescue);
    }
    
    /**
     * Gets the exceptionNodes.
     * @return Returns a Node
     */
    public Node getExceptions() {
        return exceptionNodes;
    }
    
    @Deprecated
    public Node getExceptionNodes() {
        return getExceptions();
    }
    
    public void setExceptions(Node exceptions) {
        this.exceptionNodes = adopt(exceptions);
    }
}
