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
 * Represents the argument declarations of a method.  The fields:
 * foo(p1, ..., pn, o1 = v1, ..., on = v2, *r, q1, ..., qn)
 *
 * p1...pn = pre arguments
 * o1...on = optional arguments
 * r       = rest argument
 * q1...qn = post arguments (only in 1.9)
 */
public class ArgsNode extends Node {
    private final ListNode pre;
    private final int preCount;
    private final ListNode optArgs;
    protected final ArgumentNode restArgNode;
    protected final int restArg;
    private final BlockArgNode blockArgNode;
    private final int requiredArgsCount;
    protected final boolean hasOptArgs;
    protected final boolean hasMasgnArgs;
    protected int maxArgsCount;

    // Only in ruby 1.9 methods
    private final ListNode post;
    private final int postCount;
    private final int postIndex;
    /**
     * 
     * @param optionalArguments  Node describing the optional arguments
     * 				This Block will contain assignments to locals (LAsgnNode)
     * @param restArguments  index of the rest argument in the local table
     * 				(the array argument prefixed by a * which collects 
     * 				all additional params)
     * 				or -1 if there is none.
     * @param argsCount number of regular arguments
     * @param restArgNode The rest argument (*args).
     * @param blockArgNode An optional block argument (&amp;arg).
     **/
    public ArgsNode(SourcePosition position, ListNode pre, ListNode optionalArguments,
            RestArgNode rest, ListNode post, BlockArgNode blockArgNode) {
        super(position);

        this.pre = pre;
        this.preCount = pre == null ? 0 : pre.size();
        this.post = post;
        this.postCount = post == null ? 0 : post.size();
        this.postIndex = rest == null ? 0 : rest.getIndex() + 1;
        this.optArgs = optionalArguments;
        this.restArg = rest == null ? -1 : rest.getIndex();
        this.restArgNode = rest;
        this.blockArgNode = blockArgNode;
        this.requiredArgsCount = preCount + postCount;
        this.hasOptArgs = getOptArgs() != null;
        this.hasMasgnArgs = hasMasgnArgs();
        this.maxArgsCount = getRestArg() >= 0 ? -1 : getRequiredArgsCount() + getOptionalArgsCount();
    }

    public NodeType getNodeType() {
        return NodeType.ARGSNODE;
    }

    protected boolean hasMasgnArgs() {
        if (preCount > 0) for (Node node : pre.childNodes()) {
            if (node instanceof AssignableNode) return true;
        }
        if (postCount > 0) for (Node node : post.childNodes()) {
            if (node instanceof AssignableNode) return true;
        }
        return false;
    }
    
    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Object accept(NodeVisitor iVisitor) {
        return iVisitor.visitArgsNode(this);
    }

    /**
     * Gets the required arguments at the beginning of the argument definition
     */
    public ListNode getPre() {
        return pre;
    }

    @Deprecated
    public ListNode getArgs() {
        return pre;
    }
    
    public int getRequiredArgsCount() {
        return requiredArgsCount;
    }
    
    public int getOptionalArgsCount() {
        return optArgs == null ? 0 : optArgs.size();
    }

    public ListNode getPost() {
        return post;
    }

    public int getMaxArgumentsCount() {
        return maxArgsCount;
    }

    /**
     * Gets the optArgs.
     * @return Returns a ListNode
     */
    public ListNode getOptArgs() {
        return optArgs;
    }

    /**
     * Gets the restArg.
     * @return Returns a int
     */
    public int getRestArg() {
        return restArg;
    }

    /**
     * Gets the restArgNode.
     * @return Returns an ArgumentNode
     */
    public ArgumentNode getRestArgNode() {
        return restArgNode;
    }

    @Deprecated
    public BlockArgNode getBlockArgNode() {
        return blockArgNode;
    }

    /**
     * Gets the explicit block argument of the parameter list (&block).
     * 
     * @return Returns a BlockArgNode
     */
    public BlockArgNode getBlock() {
        return blockArgNode;
    }

    public int getPostCount() {
        return postCount;
    }

    public int getPostIndex() {
        return postIndex;
    }

    public int getPreCount() {
        return preCount;
    }

    public List<Node> childNodes() {
        if (post != null) return Node.createList(pre, optArgs, restArgNode, post, blockArgNode);

        return Node.createList(pre, optArgs, restArgNode, blockArgNode);
    }
}
