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

import org.jrubyparser.SourcePosition;

/**
 * Base class of any node which can be assigned to.
 */
public abstract class AssignableNode extends Node {
    private Node valueNode;
    
    public AssignableNode(SourcePosition position) {
        super(position);
    }
    
    public AssignableNode(SourcePosition position, Node valueNode) {
        super(position);
        
        this.valueNode = adopt(valueNode);
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

        AssignableNode other = (AssignableNode) node;
        if (getValue() == null && other.getValue() == null) return true;
        if (getValue() == null || other.getValue() == null) return false;
        
        return getValue().isSame(other.getValue());
    }


    /**
     * Gets the valueNode.
     * @return Returns a Node
     */
    public Node getValue() {
        return valueNode;
    }
    
    @Deprecated
    public Node getValueNode() {
        return getValue();
    }
    
    /**
     * Sets the valueNode.
     * @param valueNode The valueNode to set
     */
    public void setValue(Node valueNode) {
        this.valueNode = adopt(valueNode);
    }
    
    @Deprecated
    public void setValueNode(Node valueNode) {
        setValue(valueNode);
    }
    
    public SourcePosition getLeftHandSidePosition() {
        if (!(this instanceof INameNode)) throw new UnsupportedOperationException("getLeftHandSidePosition() needs impl");

        SourcePosition position = getPosition();

        return new SourcePosition(position.getFile(), position.getStartLine(), position.getEndLine(),
                position.getStartOffset(), position.getStartOffset() + ((INameNode) this).getName().length());
        
    }
}
