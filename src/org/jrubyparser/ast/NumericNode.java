package org.jrubyparser.ast;

import org.jrubyparser.ast.types.ILiteralNode;
import org.jrubyparser.lexer.yacc.ISourcePosition;

/**
 * Any node representing a numeric value.
 */
public abstract class NumericNode extends Node implements ILiteralNode {
    public NumericNode(ISourcePosition position) {
        super(position, false);
    }
}
