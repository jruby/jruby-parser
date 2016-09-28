package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Node to hold string value of a methods name (for either Defn or Defs).
 */
public class MethodNameNode extends NamedNode {
    public MethodNameNode(SourcePosition position, String name) {
        super(position, name);
    }

    public NodeType getNodeType() {
        return NodeType.METHODNAMENODE;
    }

    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitMethodNameNode(this);
    }
}
