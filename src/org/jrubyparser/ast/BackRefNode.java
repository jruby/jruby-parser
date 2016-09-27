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
 *	Regexp back reference:
 *    - $&amp; last successful match
 *    - $+ highest numbered group matched in last successful match.
 *    - $` what precedes the last successful match
 *    - $' what follows the last successful match
 */
public class BackRefNode extends Node implements IGlobalVariable {
    /**
     * the character which generated the back reference
     */
    private char type;

    public BackRefNode(SourcePosition position, int type) {
        super(position);
        this.type = (char) type;
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param other to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node other) {
        return super.isSame(other) && getLexicalName().equals(((BackRefNode) other).getLexicalName());
    }


    public NodeType getNodeType() {
        return NodeType.BACKREFNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitBackRefNode(this);
    }

    /**
     * Gets the type
     *
     * @return the character which generates the back reference
     */
    public char getType() {
        return type;
    }

    public String getName() {
        return "" + type;
    }

    public String getLexicalName() {
        return "$" + getName();
    }

    public void setName(String newName) {
        type = newName.charAt(0); // FIXME: Some sanity to only allowing one char and a correct one?
    }

    public SourcePosition getNamePosition() {
        return getPosition().fromEnd(getName().length());
    }

    public SourcePosition getLexicalNamePosition() {
        return getPosition();
    }

    public boolean isNameMatch(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
