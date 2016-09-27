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

// FIXME: Make into same ListNode<arg1, ..., argn> format as calls
/**
 * Represents a yield statement.
 */
public class YieldNode extends Node implements IArgumentNode {
    private Node argsNode;
    private boolean expandedArguments;
    private boolean hasParens = false;

    /**
     * Construct a new YieldNode.
     *
     * @param position position of the node in the source
     * @param argsNode the arguments to the yield
     * @param expandedArguments whether the arguments should be treated as directly-passed args
     *                          as in yield 1, 2, 3 (expandArguments = true) versus
     *                          yield [1, 2, 3] (expandArguments = false).
     */
    public YieldNode(SourcePosition position, Node argsNode, boolean expandedArguments) {
        super(position);

        // block.yield depends on null to represent empty and nil to represent nil - [nil] vs []
        //assert argsNode != null : "argsNode is not null";

        this.argsNode = adopt(argsNode);
        this.expandedArguments = expandedArguments;
    }

    @Override
    public boolean isSame(Node other) {
        if (super.isSame(other)) {
            YieldNode ynode = (YieldNode) other;
            if (getArgs() != null && ynode.getArgs() != null) {
                if (getArgs().isSame(ynode.getArgs()) && getExpandArguments() == ynode.getExpandArguments()){
                    return true;
                }
            }

            if ((getArgs() == null && ynode.getArgs() != null) || (getArgs() != null && ynode.getArgs() == null)) {
                return false;
            }

        }
        return false;
    }

    public NodeType getNodeType() {
        return NodeType.YIELDNODE;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitYieldNode(this);
    }

    /**
     * Gets the argsNode.
     * @return Returns a Node
     */
    @Deprecated
    public Node getArgsNode() {
        return getArgs();
    }

    public Node getArgs() {
        return argsNode;
    }

    public void setArgs(Node args) {
        this.argsNode = adopt(args);
    }

    public boolean getExpandArguments() {
        return expandedArguments;
    }

    public boolean hasParens() {
        return hasParens;
    }

    public void setHasParens(boolean hasParens) {
        this.hasParens = hasParens;
    }
}
