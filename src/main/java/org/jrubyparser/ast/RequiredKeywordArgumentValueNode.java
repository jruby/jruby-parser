package org.jrubyparser.ast;

import org.jrubyparser.ast.visitor.NodeVisitor;
import org.jrubyparser.lexer.yacc.InvalidSourcePosition;

import java.util.List;

/**
 * Marker to indicate that rather than assigning nil (where in multiple
 * places we have nulls getting implicitly converted to nils) we should
 * raise an error.
 *
 * MRI passes a -1 as a special value so we are doing something similar
 * but more explicit.
 */
public class RequiredKeywordArgumentValueNode extends Node implements InvisibleNode {
    public RequiredKeywordArgumentValueNode() {
        super(InvalidSourcePosition.INSTANCE, false);
    }

    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitRequiredKeywordArgumentValueNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.REQUIRED_KEYWORD_ARGUMENT_VALUE;
    }
}
