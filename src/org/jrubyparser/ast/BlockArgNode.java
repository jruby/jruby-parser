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
 *	An explicit block argument (&amp;my_block) in a declaration (at call sites see BlockPassNode).
 */
public class BlockArgNode extends NamedNode implements IParameter {
    private int count;

    public BlockArgNode(SourcePosition position, int count, String name) {
        super(position, name);
        this.count = count;
    }

    public BlockArgNode(SourcePosition position, ArgumentNode argNode) {
        this(position, argNode.getIndex(), argNode.getName());
    }
    
    public NodeType getNodeType() {
        return NodeType.BLOCKARGNODE;
    }
    
    @Override
    public String getDecoratedName() {
        return "&" + getName();
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitBlockArgNode(this);
    }

    /**
     * Gets the count.
     * @return Returns a int
     */
    public int getCount() {
        return count;
    }
    
    // 1.9+
    @Override
    public boolean isBlockParameter() {
        IterNode iter = getInnermostIter();
        
        return iter != null && isDescendentOf(iter.getVar());
    }    

    public IScope getDefinedScope() {
        return getClosestIScope(); // argument list elements always belong to closest scope
    }
    
    public List<ILocalVariable> getOccurrences() {
        return getDefinedScope().getVariableReferencesNamed(getName());
    }    

    public ILocalVariable getDeclaration() {
        return this;
    }

    @Override
    public SourcePosition getNamePosition() {
        return getPosition().fromEnd(getName().length());
    }
    
    @Override
    public SourcePosition getDecoratedNamePosition() {
        return getPosition();
    }    
}
