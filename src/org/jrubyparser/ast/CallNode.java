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

import java.util.List;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * A method or operator call.
 */
public class CallNode extends Node implements INameNode, IArgumentNode, BlockAcceptingNode {
    private Node receiverNode;
    private Node argsNode;
    protected Node iterNode;
    protected String name;

    public CallNode(SourcePosition position, Node receiverNode, String name, Node argsNode) {
        this(position, receiverNode, name, argsNode, null);
    }
    
    public CallNode(SourcePosition position, Node receiverNode, String name, Node argsNode, 
            Node iterNode) {
        super(position);
        
        assert receiverNode != null : "receiverNode is not null";
        
        this.receiverNode = receiverNode;
	setArgsNode(argsNode);
        this.iterNode = iterNode;
        this.name = name;
    }

    public NodeType getNodeType() {
        return NodeType.CALLNODE;
    }
    
    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitCallNode(this);
    }
    
    public Node getIterNode() {
        return iterNode;
    }
    
    public Node setIterNode(Node iterNode) {
        this.iterNode = iterNode;
        // refresh call adapter, since it matters if this is iter-based or not
        
        return this;
    }

    /**
     * Gets the argsNode representing the method's arguments' value for this call.
     * @return argsNode
     */
    public Node getArgsNode() {
        return argsNode;
    }
    
    /**
     * Set the argsNode.
     * 
     * @param argsNode set the arguments for this node.
     */
    public Node setArgsNode(Node argsNode) {
	if (argsNode == null) {
	    argsNode = new ListNode(getReceiverNode().getPosition());
	}
        this.argsNode = argsNode;
        
        return argsNode;
    }

    /**
     * Gets the name.
	 * name is the name of the method called
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the receiverNode.
	 * receiverNode is the object on which the method is being called
     * @return receiverNode
     */
    public Node getReceiverNode() {
        return receiverNode;
    }

    public List<Node> childNodes() {
        return Node.createList(receiverNode, argsNode, iterNode);
    }
}
