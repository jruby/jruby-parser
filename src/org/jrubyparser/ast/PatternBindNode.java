/*
 * Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
 */
package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Represents a pattern binding in Ruby 2.7+ pattern matching: <code>pattern =&gt; var</code>.
 */
public class PatternBindNode extends Node {
    private Node pattern;
    private Node target;

    public PatternBindNode(SourcePosition position, Node pattern, Node target) {
        super(position);

        this.pattern = adopt(pattern);
        this.target = adopt(target);
    }

    @Override
    public boolean isSame(Node node) {
        if (!super.isSame(node)) return false;

        PatternBindNode other = (PatternBindNode) node;

        if (!isSameOrNull(getPattern(), other.getPattern())) return false;
        return isSameOrNull(getTarget(), other.getTarget());
    }

    private boolean isSameOrNull(Node a, Node b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.isSame(b);
    }

    public NodeType getNodeType() {
        return NodeType.PATTERNBINDNODE;
    }

    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitPatternBindNode(this);
    }

    public Node getPattern() {
        return pattern;
    }

    public void setPattern(Node pattern) {
        this.pattern = adopt(pattern);
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = adopt(target);
    }
}
