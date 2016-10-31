package org.jrubyparser.ast;

import java.util.Collections;
import java.util.List;
import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Marker to indicate that rather than assigning nil (where in multiple
 * places we have nulls getting implicitly converted to nils) we should
 * raise an error.
 *
 * MRI passes a -1 as a special value so we are doing something similar
 * but more explicit.
 */
public class RequiredKeywordArgumentValueNode extends Node {
    public RequiredKeywordArgumentValueNode(SourcePosition position) {
        super(position);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitRequiredKeywordArgumentValueNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Collections.emptyList();
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.REQUIREDKEYWORDARGNODE;
    }
}
