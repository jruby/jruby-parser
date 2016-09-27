/***** BEGIN LICENSE BLOCK *****
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
 *
 * @author enebo
 */
public class OptArgNode extends Node implements IParameter {
    private Node value;

    public OptArgNode(SourcePosition position, Node value) {
        super(position);
        this.value = adopt(value);
    }

    @Override
    public boolean isSame(Node node) {
        if (super.isSame(node)) return false;

        OptArgNode other = (OptArgNode) node;

        return isNameMatch(other.getName()) && getValue().isSame(other.getValue());
    }

    public NodeType getNodeType() {
        return NodeType.OPTARGNODE;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitOptArgNode(this);
    }

    public String getLexicalName() {
        return getName();
    }

    public String getName() {
        if (value instanceof INameNode) return ((INameNode) value).getName();

        return null;
    }

    public void setName(String newName) {
        if (value instanceof INameNode) ((INameNode) value).setName(newName);
    }

    public boolean isNameMatch(String name) {
        String thisName = getName();

        return thisName != null && thisName.equals(name);
    }

    public SourcePosition getNamePosition() {
        if (value instanceof INameNode) return ((INameNode) value).getNamePosition();

        return getPosition();
    }

    public SourcePosition getLexicalNamePosition() {
        return getNamePosition();
    }

    public ILocalVariable getDeclaration() {
        return this;
    }

    public List<ILocalVariable> getOccurrences() {
        return getDefinedScope().getVariableReferencesNamed(getName());
    }

    public IScope getDefinedScope() {
        return getClosestIScope();
    }
}
