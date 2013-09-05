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

/** Represents an operator assignment to an element.
 * 
 * This could be for example:
 * 
 * <pre>
 * a[4] += 5
 * a[3] &&= true
 * </pre>
 */
public class OpElementAsgnNode extends Node {
    private Node receiverNode;
    private Node argsNode;
    private Node valueNode;
    private String name;

    public OpElementAsgnNode(SourcePosition position, Node receiverNode, String operatorName, Node argsNode, Node valueNode) {
        super(position);
        
        assert receiverNode != null : "receiverNode is not null";
        assert valueNode != null : "valueNode is not null";
        
        this.receiverNode = adopt(receiverNode);
        this.argsNode = adopt(argsNode);
        this.valueNode = adopt(valueNode);
        this.name = operatorName;
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
        OpElementAsgnNode other = (OpElementAsgnNode) node;
        
        boolean truth = getReceiver().isSame(other.getReceiver()) && getValue().isSame(other.getValue()) &&
                getOperatorName().equals(other.getOperatorName());

        if (getArgs() == null && other.getArgs() == null) return truth;
        if (getArgs() == null && other.getArgs() == null) return false;
        
        return truth && getArgs().isSame(other.getArgs());

    }


    public NodeType getNodeType() {
        return NodeType.OPELEMENTASGNNODE;
    }
    
    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitOpElementAsgnNode(this);
    }

    /**
     * Gets the argsNode.
     * @return Returns a Node
     */
    @Deprecated
    public Node getArgsNode() {
        return getArgs();
    }

    public Node getArgs() {
        return argsNode;
    }
    
    public void setArgs(Node args) {
        this.argsNode = adopt(args);
    }
    
    /**
     * Gets the operatorName.
     * @return Returns a String
     */
    public String getOperatorName() {
        return name;
    }
    
    public void setOperatorName(String name) {
        this.name = name;
    }

    /**
     * Gets the receiverNode.
     * @return Returns a Node
     */
    @Deprecated
    public Node getReceiverNode() {
        return getReceiver();
    }
    
    public Node getReceiver() {
        return receiverNode;
    }
    
    public void setReceiver(Node receiver) {
        this.receiverNode = adopt(receiver);
    }

    /**
     * Gets the valueNode.
     * @return Returns a Node
     */
    @Deprecated
    public Node getValueNode() {
        return valueNode;
    }
    
    public Node getValue() {
        return valueNode;
    }
    
    public void setValue(Node value) {
        this.valueNode = adopt(value);
    }
}
