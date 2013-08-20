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
 * Node that represents an assignment of either an array element or attribute.
 */
public class AttrAssignNode extends Node implements INameNode, IArgumentNode {
    protected Node receiver;
    private String name;
    private Node arg;
    private boolean hasParens;

    public AttrAssignNode(SourcePosition position, Node receiver, String name, Node arg) {
        super(position);

        assert receiver != null : "receiverNode is not null";
        // TODO: At least ParserSupport.attrset passes argsNode as null.  ImplicitNil is wrong magic for 
        // setupArgs since it will IRubyObject[] { nil }.  So we need to figure out a nice fast
        // null pattern for setupArgs.
        // assert argsNode != null : "receiverNode is not null";
        
        this.receiver = adopt(receiver);
        this.name = name;
        this.arg = adopt(arg);
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    public boolean isSame(Node node) {
        if (super.isSame(node)) {
            AttrAssignNode attrAssignNode = (AttrAssignNode) node;

            if (!isNameMatch(attrAssignNode.getName())) {
                return false;
            }

            if (!getReceiver().isSame(attrAssignNode.getReceiver())) {
                return false;
            }

            if (!getArgs().isSame(attrAssignNode.getArgs())) {
               return false;
            }

            return true;
        }
        return false;
    }


    public NodeType getNodeType() {
        return NodeType.ATTRASSIGNNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param visitor the visitor
     **/
    public Object accept(NodeVisitor visitor) {
        return visitor.visitAttrAssignNode(this);
    }

    public String getLexicalName() {
        return getName();
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
        return receiver;
    }
    
    /**
     * Gets the argsNode.
     * argsNode representing the method's arguments' value for this call.
     * @return argsNode
     */
    @Deprecated
    public Node getArgsNode() {
        return getArgs();
    }
    
    public Node getArgs() {
        return arg;
    }
    
    
    /**
     * Set the argsNode
     * 
     * @param argsNode set the arguments for this node.
     */
    @Deprecated
    public Node setArgsNode(Node argsNode) {
        setArgs(argsNode);
        
        return this;
    }
    
    public void setArgs(Node argsNode) {
        this.arg = adopt(argsNode);
    }

    public boolean hasParens() {
        return hasParens;
    }

    public void setHasParens(boolean hasParens) {
        this.hasParens = hasParens;
    }

    public SourcePosition getNamePosition() {
        return getPosition().fromBeginning(getName().length());
    }
    
    public SourcePosition getLexicalNamePosition() {
        return getNamePosition();
    }    
}
