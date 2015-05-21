package org.jrubyparser.ast;

import java.util.List;
import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Created by enebo on 5/21/15.
 */
public class RationalNode extends NumericNode {
    private final long numerator;
    private final long denominator;

    public RationalNode(SourcePosition position, long numerator, long denominator) {
        super(position);

        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitRationalNode(this);
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
