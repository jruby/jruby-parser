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
 * A Range in a boolean expression (named after a FlipFlop component in electronic?).
 */
public class FlipNode extends Node {
    private Node beginNode;
    private Node endNode;
    private boolean exclusive;
    // A scoped location of this variable (high 16 bits is how many scopes down and low 16 bits
    // is what index in the right scope to set the value.
    private int location;
    
    public FlipNode(SourcePosition position, Node beginNode, Node endNode, boolean exclusive, int location) {
        super(position);
        
        assert beginNode != null : "beginNode is not null";
        assert endNode != null : "endNode is not null";
        
        this.beginNode = adopt(beginNode);
        this.endNode = adopt(endNode);
        this.exclusive = exclusive;
        this.location = location;
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    public boolean isSame(Node node) {
        if (super.isSame(node)) {
            FlipNode mnode = (FlipNode) node;
            if (getBegin().isSame(mnode.getBegin())) {
                if (getEnd().isSame(mnode.getEnd())) {
                    return true;
                }
            }
        }
        return false;
    }


    public NodeType getNodeType() {
        return NodeType.FLIPNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitFlipNode(this);
    }

    /**
     * Gets the beginNode.
	 * beginNode will set the FlipFlop the first time it is true
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
	 * endNode will reset the FlipFlop when it is true while the FlipFlop is set.
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
	 * if the range is a 2 dot range it is false if it is a three dot it is true
     * @return Returns a boolean
     */
    public boolean isExclusive() {
        return exclusive;
    }

    /**
     * How many scopes should we burrow down to until we need to set the block variable value.
     * 
     * @return 0 for current scope, 1 for one down, ...
     */
    public int getDepth() {
        return location >> 16;
    }
    
    /**
     * Gets the index within the scope construct that actually holds the eval'd value
     * of this local variable
     * 
     * @return Returns an int offset into storage structure
     */
    public int getIndex() {
        return location & 0xffff;
    }
}
