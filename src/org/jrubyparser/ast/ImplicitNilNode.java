package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * This node represent those strange places where we have what it a valid semantic element
 * but syntactically it is not there:  [1, (), 3].  The parens here are syntax and evaluating
 * it will return nil but a nil is not actually there.
 */
public class ImplicitNilNode extends Node {
    public ImplicitNilNode(SourcePosition position) {
        super(position);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitImplicitNilNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.IMPLICITNILNODE;
    }

}
