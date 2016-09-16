/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jrubyparser.ast;

import org.jrubyparser.ast.visitor.NodeVisitor;
import org.jrubyparser.lexer.yacc.ISourcePosition;

import java.util.List;

/**
 *
 * @author enebo
 */
public class RationalNode extends NumericNode implements SideEffectFree {
    private final long numerator;
    private final long denominator;

    public RationalNode(ISourcePosition position, long numerator, long denominator) {
        super(position);

        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitRationalNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RATIONALNODE;
    }

    public long getNumerator() {
        return numerator;
    }

    public long getDenominator() {
        return denominator;
    }
}
