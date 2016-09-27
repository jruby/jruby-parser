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
 * Declaration (and assignment) of a Constant.
 */
// FIXME: ConstDecl could be two seperate classes (or done differently since constNode and name
// never exist at the same time.
public class ConstDeclNode extends AssignableNode implements INameNode {
    private String name;
    private INameNode constNode;

    // TODO: Split this into two sub-classes so that name and constNode can be specified seperately.
    public ConstDeclNode(SourcePosition position, String name, INameNode constNode, Node valueNode) {
        super(position, valueNode);

        this.name = name;
        this.constNode = (INameNode) adopt((Node) constNode);
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param other to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node other) {
        return super.isSame(other) && isNameMatch(((ConstDeclNode) other).getName());
    }


    public NodeType getNodeType() {
        return NodeType.CONSTDECLNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitConstDeclNode(this);
    }

    public String getLexicalName() {
        return getName();
    }

    /**
     * Gets the name (this is the rightmost element of lhs (in Foo::BAR it is BAR).
	 * name is the constant Name, it normally starts with a Capital
     * @return name
     */
    public String getName() {
    	return (name == null ? constNode.getName() : name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNameMatch(String name) {
        String thisName = getName();

        return thisName != null && thisName.equals(name);
    }

    /**
     * Get the path the name is associated with or null (in Foo::BAR it is Foo).
     * @return pathNode
     */
    public Node getConstNode() {
        return (Node) constNode;
    }

    public SourcePosition getNamePosition() {
        return getPosition().fromBeginning(getName().length());
    }

    public SourcePosition getLexicalNamePosition() {
        return getNamePosition();
    }
}
