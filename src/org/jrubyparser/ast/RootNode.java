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
import org.jrubyparser.util.ILocalVariableVisitor;

/**
 * Represents the top of the AST.  This is a node not present in MRI.  It was created to
 * hold the top-most static scope in an easy to grab way and it also exists to hold BEGIN
 * and END nodes.  These can then be interpreted/compiled in the same places as the rest
 * of the code.
 *
 */
// TODO: Store BEGIN and END information into this node
public class RootNode extends Node implements ILocalScope {
    private StaticScope staticScope;
    private Node bodyNode;

    public RootNode(SourcePosition position, StaticScope scope, Node bodyNode) {
        super(position);

        this.staticScope = scope;
        this.bodyNode = adopt(bodyNode);
    }

    public NodeType getNodeType() {
        return NodeType.ROOTNODE;
    }

    /**
     * The static scoping relationships that should get set first thing before interpretation
     * of the code represented by this AST.  Actually, we use getScope first since that also
     * can contain a live dynamic scope.  We rely on this method only for interpreting a root
     * node from a serialized format.
     *
     * @return the top static scope for the AST
     */
    public StaticScope getStaticScope() {
        return staticScope;
    }

    /**
     * First real AST node to be interpreted
     *
     * @return real top AST node
     */
    public Node getBody() {
        return bodyNode;
    }

    @Deprecated
    public Node getBodyNode() {
        return getBody();
    }

    public void setBody(Node body) {
        this.bodyNode = adopt(body);
    }

    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitRootNode(this);
    }

    public List<ILocalVariable> getVariableReferencesNamed(String name) {
        return ILocalVariableVisitor.findOccurrencesIn(this, name);
    }
}
