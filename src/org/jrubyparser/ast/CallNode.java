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
 * A method or operator call.
 */
public class CallNode extends Node implements INameNode, IArgumentNode, BlockAcceptingNode {
    private Node receiverNode;
    private Node argsNode;
    protected Node iterNode;
    protected String name;
    protected String lexicalName;
    private boolean hasParens = false;

    public CallNode(SourcePosition position, Node receiverNode, String name, Node argsNode) {
        this(position, receiverNode, name, argsNode, null);
    }
    
    public CallNode(SourcePosition position, Node receiverNode, String name, Node argsNode, 
            Node iterNode) {
        super(position);
        
        assert receiverNode != null : "receiverNode is not null";
        
        this.receiverNode = adopt(receiverNode);
        setArgs(argsNode);
        this.iterNode = adopt(iterNode);
        this.name = name;
        lexicalName = name;
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node node) {
        return super.isSame(node) && isNameMatch(((CallNode) node).getName());
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
    
    @Deprecated
    public Node getIterNode() {
        return getIter();
    }
    
    public Node getIter() {
        return iterNode;
    }
    
    public Node setIterNode(Node iterNode) {
        setIter(iterNode);
        
        return this;
    }
    
    public void setIter(Node iter) {
        this.iterNode = adopt(iter);
    }

    /**
     * Gets the argsNode representing the method's arguments' value for this call.
     * @return argsNode
     */
    @Deprecated
    public Node getArgsNode() {
        return getArgs();
    }
    
    public Node getArgs() {
        return argsNode;
    }
    
    /**
     * Set the argsNode.
     * 
     * @param argsNode set the arguments for this node.
     */
    @Deprecated
    public Node setArgsNode(Node argsNode) {
        setArgs(argsNode);
        
        return getArgs();
    }
    
    public void setArgs(Node argsNode) {
        if (argsNode == null) {
	    argsNode = new ListNode(getReceiver().getPosition());
        }
        this.argsNode = adopt(argsNode);
    }

    public boolean hasParens() {
        return hasParens;
    }

    public void setHasParens(boolean hasParens) {
        this.hasParens = hasParens;
    }
    
    public String getLexicalName() {
        return lexicalName;
    }

    /**
     * Gets the name.
	 * name is the name of the method called
     * @return name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLexicalName(String lexcicalName) {
        this.lexicalName = lexicalName;
    }

    public boolean isNameMatch(String name) {
        String thisName = getName();
        
        return thisName != null && thisName.equals(name);
    }
    
    /**
     * Gets the receiverNode.
	 * receiverNode is the object on which the method is being called
     * @return receiverNode
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

    public SourcePosition getNamePosition() {
        SourcePosition pos = receiverNode.getPosition();
        
        return new SourcePosition(pos.getFile(), pos.getStartLine(), pos.getEndLine(),
                pos.getEndOffset(), pos.getEndOffset() + getName().length());
    }
    
    public SourcePosition getLexicalNamePosition() {
        return getNamePosition();
    }
}
