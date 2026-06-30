/*
 * Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
 */
package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Represents an array pattern in Ruby 2.7+ pattern matching, e.g.
 * <code>[a, b, *rest]</code> or <code>Const(a, b)</code>.
 */
public class ArrayPatternNode extends Node {
    private Node constant;
    private ListNode preArgs;
    private boolean hasRestArg;
    private Node restArg;
    private ListNode postArgs;

    public ArrayPatternNode(SourcePosition position, ListNode preArgs, boolean hasRestArg,
            Node restArg, ListNode postArgs) {
        super(position);

        this.preArgs = (ListNode) adopt(preArgs);
        this.hasRestArg = hasRestArg;
        this.restArg = adopt(restArg);
        this.postArgs = (ListNode) adopt(postArgs);
    }

    @Override
    public boolean isSame(Node node) {
        if (!super.isSame(node)) return false;

        ArrayPatternNode other = (ArrayPatternNode) node;

        if (hasRestArg != other.hasRestArg) return false;
        if (!isSameOrNull(getConstant(), other.getConstant())) return false;
        if (!isSameOrNull(getPreArgs(), other.getPreArgs())) return false;
        if (!isSameOrNull(getRestArg(), other.getRestArg())) return false;
        return isSameOrNull(getPostArgs(), other.getPostArgs());
    }

    private boolean isSameOrNull(Node a, Node b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.isSame(b);
    }

    public NodeType getNodeType() {
        return NodeType.ARRAYPATTERNNODE;
    }

    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitArrayPatternNode(this);
    }

    public Node getConstant() {
        return constant;
    }

    public void setConstant(Node constant) {
        this.constant = adopt(constant);
    }

    public ListNode getPreArgs() {
        return preArgs;
    }

    public void setPreArgs(ListNode preArgs) {
        this.preArgs = (ListNode) adopt(preArgs);
    }

    public boolean hasRestArg() {
        return hasRestArg;
    }

    public void setHasRestArg(boolean hasRestArg) {
        this.hasRestArg = hasRestArg;
    }

    public Node getRestArg() {
        return restArg;
    }

    public void setRestArg(Node restArg) {
        this.restArg = adopt(restArg);
    }

    public ListNode getPostArgs() {
        return postArgs;
    }

    public void setPostArgs(ListNode postArgs) {
        this.postArgs = (ListNode) adopt(postArgs);
    }
}
