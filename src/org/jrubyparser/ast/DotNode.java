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
 * Represents a range literal.
 */
public class DotNode extends Node {
    private Node beginNode;
    private Node endNode;
    private boolean exclusive;
    private boolean isLiteral;

    public DotNode(SourcePosition position, Node beginNode, Node endNode, boolean exclusive, 
            boolean isLiteral) {
        super(position);
        
        assert beginNode != null : "beginNode is not null";
        assert endNode != null : "endNode is not null";
        
        this.beginNode = adopt(beginNode);
        this.endNode = adopt(endNode);
        this.exclusive = exclusive;
        this.isLiteral = isLiteral;
    }

    public NodeType getNodeType() {
        return NodeType.DOTNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitDotNode(this);
    }

    /**
     * Gets the beginNode.
     * @return Returns a Node
     */
    public Node getBegin() {
        return beginNode;
    }

    @Deprecated
    public Node getBeginNode() {
        return getBegin();
    }
    
    /**
     * Gets the endNode.
     * @return Returns a Node
     */
    public Node getEnd() {
        return endNode;
    }
    
    @Deprecated
    public Node getEndNode() {
        return getEnd();
    }

    /**
     * Gets the exclusive.
     * @return Returns a boolean
     */
    public boolean isExclusive() {
        return exclusive;
    }
    
    /**
     * Is this a literal node.  MRI has a literal node type and we currently don't.
     * We provide this attribute so we can detect that this should be a literal to
     * match MRI semantics of literal DOT nodes.
     * 
     * @return true is literal
     */
    public boolean isLiteral() {
        return isLiteral;
    }
    
    public List<Node> childNodes() {
        return Node.createList(beginNode, endNode);
    }
    
}
