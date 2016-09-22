package org.jrubyparser.ast;

import org.jrubyparser.lexer.yacc.ISourcePosition;

/**
 * f rescue nil
 */
public class RescueModNode extends RescueNode {
    public RescueModNode(ISourcePosition position, Node bodyNode, RescueBodyNode rescueNode) {
        super(position, bodyNode, rescueNode, null /* else */);
    }
}
