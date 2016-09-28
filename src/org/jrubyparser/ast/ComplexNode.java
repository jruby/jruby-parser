package org.jrubyparser.ast;

import java.util.List;
import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Created by enebo on 5/21/15.
 */
public class ComplexNode extends NumericNode implements ILiteralNode {
    private NumericNode y;

    public ComplexNode(SourcePosition position, NumericNode y) {
        super(position);

        this.y = (NumericNode) adopt(y);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitComplexNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return createList(y);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.COMPLEXNODE;
    }

    public NumericNode getNumber() {
        return y;
    }

    public void setNumber(NumericNode y) {
        this.y = y;
    }
}
