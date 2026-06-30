/*
 * Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
 */
package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Represents an `in' clause (pattern matching) within a case/in expression.
 * Mirrors WhenNode but for Ruby 2.7+ pattern matching.
 */
public class InNode extends Node {
    private Node expressionNodes;
    private Node bodyNode;
    private Node nextCase;

    public InNode(SourcePosition position, Node expressionNodes, Node bodyNode, Node nextCase) {
        super(position);

        this.expressionNodes = adopt(expressionNodes);
        this.bodyNode = adopt(bodyNode);
        this.nextCase = adopt(nextCase);
    }

    @Override
    public boolean isSame(Node node) {
        if (!super.isSame(node)) return false;

        InNode other = (InNode) node;

        if (!isSameOrNull(getExpression(), other.getExpression())) return false;
        if (!isSameOrNull(getBody(), other.getBody())) return false;
        return isSameOrNull(getNextCase(), other.getNextCase());
    }

    private boolean isSameOrNull(Node a, Node b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.isSame(b);
    }

    public NodeType getNodeType() {
        return NodeType.INNODE;
    }

    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitInNode(this);
    }

    public Node getBody() {
        return bodyNode;
    }

    public void setBody(Node body) {
        this.bodyNode = adopt(body);
    }

    public Node getNextCase() {
        return nextCase;
    }

    public void setNextCase(Node nextCase) {
        this.nextCase = adopt(nextCase);
    }

    public Node getExpression() {
        return expressionNodes;
    }

    public void setExpression(Node expression) {
        this.expressionNodes = adopt(expression);
    }
}
