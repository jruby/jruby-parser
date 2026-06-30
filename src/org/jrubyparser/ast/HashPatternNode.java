/*
 * Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
 */
package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Represents a hash pattern in Ruby 2.7+ pattern matching, e.g.
 * <code>{a:, b: 1, **rest}</code> or <code>Const(a:)</code>.
 */
public class HashPatternNode extends Node {
    private Node constant;
    private HashNode elements;
    private Node restArg;
    private boolean noRest;

    public HashPatternNode(SourcePosition position, HashNode elements, Node restArg, boolean noRest) {
        super(position);

        this.elements = (HashNode) adopt(elements);
        this.restArg = adopt(restArg);
        this.noRest = noRest;
    }

    @Override
    public boolean isSame(Node node) {
        if (!super.isSame(node)) return false;

        HashPatternNode other = (HashPatternNode) node;

        if (noRest != other.noRest) return false;
        if (!isSameOrNull(getConstant(), other.getConstant())) return false;
        if (!isSameOrNull(getElements(), other.getElements())) return false;
        return isSameOrNull(getRestArg(), other.getRestArg());
    }

    private boolean isSameOrNull(Node a, Node b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.isSame(b);
    }

    public NodeType getNodeType() {
        return NodeType.HASHPATTERNNODE;
    }

    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitHashPatternNode(this);
    }

    public Node getConstant() {
        return constant;
    }

    public void setConstant(Node constant) {
        this.constant = adopt(constant);
    }

    public HashNode getElements() {
        return elements;
    }

    public void setElements(HashNode elements) {
        this.elements = (HashNode) adopt(elements);
    }

    public Node getRestArg() {
        return restArg;
    }

    public void setRestArg(Node restArg) {
        this.restArg = adopt(restArg);
    }

    public boolean isNoRest() {
        return noRest;
    }

    public void setNoRest(boolean noRest) {
        this.noRest = noRest;
    }
}
