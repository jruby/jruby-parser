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
 * An assignment to a local variable.
 */
public class LocalAsgnNode extends AssignableNode implements ILocalVariable {
    // The name of the variable
    private String name;

    // A scoped location of this variable (high 16 bits is how many scopes down and low 16 bits
    // is what index in the right scope to set the value.
    private int location;

    public LocalAsgnNode(SourcePosition position, String name, int location, Node valueNode) {
        super(position, valueNode);
        this.name = name;
        this.location = location;
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node node) {
        return super.isSame(node) && isNameMatch(((LocalAsgnNode) node).getName());
    }


    public NodeType getNodeType() {
        return NodeType.LOCALASGNNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitLocalAsgnNode(this);
    }

    public String getLexicalName() {
        return getName();
    }

    /**
     * Name of the local assignment.
     **/
    public String getName() {
        return name;
    }

    /**
     * Change the name of this local assignment (for refactoring)
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public boolean isNameMatch(String name) {
        String thisName = getName();

        return thisName != null && thisName.equals(name);
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

    public IScope getDefinedScope() {
        IScope scope = getClosestIScope();

        for (int i = 0; i < getDepth(); i++) {
            scope = ((Node) scope).getClosestIScope();
        }

        return scope;
    }

    public List<ILocalVariable> getOccurrences() {
        return getDefinedScope().getVariableReferencesNamed(getName());
    }

    /**
     * Return parameter if this local assignment is referring to a parameter since that clearly
     * is the declaration.
     *
     * In the case that it is not a parameter then we pick itself since current assignment
     * is what most programmers will expect to be a declaration.  Note: This is largely an
     * arbitrary choice.  In LocalVar node we always pick the closest LocalAsgn to the beginning
     * of the scope whereas here we pick itself.  Asking programmers what they thought was the
     * declaration is why this choice was made.  Also NetBeans also came to the same decision.
     */
    public ILocalVariable getDeclaration() {
        List<ILocalVariable> list = getOccurrences();
        if (list.size() > 0) {
            ILocalVariable variable = list.get(0);

            if (variable instanceof IParameter) return variable;
            if (variable instanceof LocalAsgnNode) return variable; // optarg (e.g. a=1) in 1.8
        }

        return this;
    }

    public SourcePosition getNamePosition() {
        return getPosition().fromBeginning(getName().length());
    }

    public SourcePosition getLexicalNamePosition() {
        return getNamePosition();
    }
}
