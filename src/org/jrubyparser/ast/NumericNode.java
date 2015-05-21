package org.jrubyparser.ast;


import org.jrubyparser.SourcePosition;

/**
 * Any node representing a numeric value.
 */
public abstract class NumericNode extends Node implements ILiteralNode {
    public NumericNode(SourcePosition position) {
        super(position);
    }
}
