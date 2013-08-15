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
 * Copyright (C) 2013 Chris Seaton <chris@chrisseaton.com>
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
 * An operator call on one of the unary operators '+' or '-' that lexically
 * appears to have the same name as the binary operator, but semantically has a
 * name decorated with an '@' sigil. These two operators are syntactically
 * distinct from use of other operators, so they are made distinct in the AST
 * with this node.
 */
public class UnaryCallNode extends Node implements INameNode {
    private Node receiverNode;
    protected String lexicalName;
    private boolean hasParens = false;

    public UnaryCallNode(SourcePosition position, Node receiverNode, String lexicalName) {
        super(position);
        
        assert receiverNode != null : "receiverNode is not null";
        
        this.receiverNode = adopt(receiverNode);
        this.lexicalName = lexicalName;
    }

    public NodeType getNodeType() {
        return NodeType.UNARYCALLNODE;
    }
    
    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitUnaryCallNode(this);
    }

    public boolean hasParens() {
        return hasParens;
    }

    public void setHasParens(boolean hasParens) {
        this.hasParens = hasParens;
    }
    
    /**
     * Gets the name as it lexically appears in the source code, undecorated by
     * the '@' sigil.
     */
    public String getLexicalName() {
        return lexicalName;
    }

    /**
     * Gets the name as it is semantically, decorated by the '@' sigil.
     */
    public String getName() {
        return lexicalName + "@";
    }

    public void setLexicalName(String lexicalName) {
        this.lexicalName = lexicalName;
    }

    public void setName(String name) {
        assert name.startsWith("@");
        lexicalName = name.substring(1);
    }

    public boolean isLexicalNameMatch(String name) {
        String thisName = getLexicalName();
        
        return thisName != null && thisName.equals(name);
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
