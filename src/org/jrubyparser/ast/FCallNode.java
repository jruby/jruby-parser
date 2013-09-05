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
 * Represents a method call with self as an implicit receiver.
 */
public class FCallNode extends Node implements INameNode, IArgumentNode, BlockAcceptingNode {
    private Node argsNode;
    protected Node iterNode;
    protected String name;
    private boolean hasParens = false;

    public FCallNode(SourcePosition position, String name, Node argsNode) {
        this(position, name, argsNode, null);
    }
    
    public FCallNode(SourcePosition position, String name, Node argsNode, Node iterNode) {
        super(position);

        //        if (argsNode == null) argsNode = new ListNode(position);
        
        this.argsNode = adopt(argsNode);
        this.iterNode = adopt(iterNode);
        this.name = name;
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node node) {
        return super.isSame(node) && isNameMatch(((FCallNode) node).getName());
    }


    public NodeType getNodeType() {
        return NodeType.FCALLNODE;
    }
    
    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitFCallNode(this);
    }

    public boolean hasParens() {
        return hasParens;
    }
    
    public void setHasParens(boolean hasParens) {
        this.hasParens = hasParens;
    }
    
    /**
     * Get the node that represents a block or a block variable.
     */
    @Deprecated
    public Node getIterNode() {
        return getIter();
    }
    
    public Node getIter() {
        return iterNode;
    }
    
    public Node setIterNode(Node iterNode) {
        this.iterNode = adopt(iterNode);
        
        return this;
    }
    
    public void setIter(Node iter) {
        this.iterNode = adopt(iter);
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
    
    /**
     * Set the argsNode.
     * 
     * @param argsNode set the arguments for this node.
     */
    @Deprecated
    public Node setArgsNode(Node argsNode) {
        setArgs(argsNode);
        
        return argsNode;
    }
    
    public void setArgs(Node argsNode) {
        this.argsNode = adopt(argsNode);
    }
    
    public String getLexicalName() {
        return getName();
    }
    
    /**
     * Gets the name.
     * @return Returns a String
     */
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public boolean isNameMatch(String name) {
        String thisName = getName();
        
        return thisName != null && thisName.equals(name);
    }

    public SourcePosition getNamePosition() {
        return getPosition().fromBeginning(getName().length());
    }
    
    public SourcePosition getLexicalNamePosition() {
        return getNamePosition();
    }    
}
