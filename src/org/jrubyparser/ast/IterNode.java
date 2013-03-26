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
import org.jrubyparser.StaticScope;
import org.jrubyparser.util.VariableHelper;

/**
 * Represents a block.  
 */
public class IterNode extends Node implements IParameterScope {
    private Node varNode;
    private Node bodyNode;
    
    // What static scoping relationship exists when it comes into being.
    private StaticScope scope;
    
    public IterNode(SourcePosition position, Node varNode, StaticScope scope, Node bodyNode) {
        super(position);
        this.varNode = adopt(varNode);
        this.scope = scope;
        this.bodyNode = adopt(bodyNode);
    }

    public IterNode(SourcePosition position, ArgsNode args, Node body, StaticScope scope) {
        super(position);

        this.varNode = adopt(args);
        this.bodyNode = adopt(body);
        this.scope = scope;
    }

    public NodeType getNodeType() {
        return NodeType.ITERNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitIterNode(this);
    }
    
    public StaticScope getScope() {
        return scope;
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

    /**
     * Gets the varNode.
     * @return Returns a Node
     */
    public Node getVar() {
        return varNode;
    }
    
    @Deprecated
    public Node getVarNode() {
        return getVar();
    }
    
    public List<Node> childNodes() {
        return Node.createList(varNode, bodyNode);
    }
    
    /**
     * Given a name (presumably retrieve via getNormativeSignatureNameList()) is this parmeter used
     * in this method definition?
     * 
     * @param name
     * @return if used or not.
     */
    public boolean isParameterUsed(String name) {
        // FIXME: Do I need to worry about used vars in parameter initialization?
        return VariableHelper.isParameterUsed(getBody(), name, false);
    }

    public Node getParameterNamed(String name) {
        return VariableHelper.getParameterName(getVar(), name);
    }
}
