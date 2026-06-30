/*
 * Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
 */
package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Represents a find pattern in Ruby 3.0+ pattern matching, e.g.
 * <code>[*pre, x, y, *post]</code> or <code>Const(*, mid, *)</code>.
 * A find pattern always has a leading and trailing rest argument with one
 * or more middle patterns in between.
 */
public class FindPatternNode extends Node {
    private Node constant;
    private Node preRestArg;
    private ListNode args;
    private Node postRestArg;

    public FindPatternNode(SourcePosition position, Node preRestArg, ListNode args, Node postRestArg) {
        super(position);

        this.preRestArg = adopt(preRestArg);
        this.args = (ListNode) adopt(args);
        this.postRestArg = adopt(postRestArg);
    }

    @Override
    public boolean isSame(Node node) {
        if (!super.isSame(node)) return false;

        FindPatternNode other = (FindPatternNode) node;

        if (!isSameOrNull(getConstant(), other.getConstant())) return false;
        if (!isSameOrNull(getPreRestArg(), other.getPreRestArg())) return false;
        if (!isSameOrNull(getArgs(), other.getArgs())) return false;
        return isSameOrNull(getPostRestArg(), other.getPostRestArg());
    }

    private boolean isSameOrNull(Node a, Node b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.isSame(b);
    }

    public NodeType getNodeType() {
        return NodeType.FINDPATTERNNODE;
    }

    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitFindPatternNode(this);
    }

    public Node getConstant() {
        return constant;
    }

    public void setConstant(Node constant) {
        this.constant = adopt(constant);
    }

    public Node getPreRestArg() {
        return preRestArg;
    }

    public void setPreRestArg(Node preRestArg) {
        this.preRestArg = adopt(preRestArg);
    }

    public ListNode getArgs() {
        return args;
    }

    public void setArgs(ListNode args) {
        this.args = (ListNode) adopt(args);
    }

    public Node getPostRestArg() {
        return postRestArg;
    }

    public void setPostRestArg(Node postRestArg) {
        this.postRestArg = adopt(postRestArg);
    }
}
