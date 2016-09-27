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

/** Represents an alias statement (<code>alias newName oldName</code>).
 */
public class AliasNode extends Node {
    private Node oldName;
    private Node newName;

    public AliasNode(SourcePosition position, Node newName, Node oldName) {
        super(position);
        this.oldName = adopt(oldName);
        this.newName = adopt(newName);
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

        AliasNode aliasNode = (AliasNode) node;

        return getOldNameString().equals(aliasNode.getOldNameString()) && getNewNameString().equals(aliasNode.getNewNameString());
    }


    public NodeType getNodeType() {
        return NodeType.ALIASNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitAliasNode(this);
    }

    /**
     * Gets the newName.
     * @return the newName as in the alias statement :  <code> alias <b>newName</b> oldName </code>
     */
    public Node getNewName() {
        return newName;
    }

    /**
     * Gets the oldName.
     * @return the oldName as in the alias statement :  <code> alias newName <b>oldName</b></code>
     */
    public Node getOldName() {
        return oldName;
    }

    public boolean oldNameMatches(String name) {
        if (oldName instanceof INameNode) return ((INameNode) oldName).isNameMatch(name);
        if (oldName instanceof StrNode) return ((StrNode) oldName).getValue().equals(name);
        if (oldName instanceof LiteralNode) return ((LiteralNode) oldName).getName().equals(name);

        return false;
    }

    /**
    * Returns the actual string name of the old method from the alias statement,
    * rather than the node representing it.
    */
    public String getOldNameString() {
        if (oldName instanceof INameNode) return ((INameNode) oldName).getName();
        if (oldName instanceof StrNode) return ((StrNode) oldName).getValue();
        if (oldName instanceof LiteralNode) return ((LiteralNode) oldName).getName();

        return "";
    }

    /**
     * Returns the actual string name that the method is being aliased to from the alias statement,
     * rather than the node representing it.
     */
    public String getNewNameString() {
        if (newName instanceof INameNode) return ((INameNode) newName).getName();
        if (newName instanceof StrNode) return ((StrNode) newName).getValue();
        if (newName instanceof LiteralNode) return ((LiteralNode) newName).getName();

        return "";
    }

}
