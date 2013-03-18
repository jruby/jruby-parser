package org.jrubyparser.ast;

import java.util.List;
import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Similiar to BlockArg, but with idiosyncracies that 1.8.7 allows:
 *
 * proc { |a,&b| }
 * proc { |a,&FOO| }
 * proc { |a,b.c| }
 * proc { |a,b[0]| }
 *
 */
public class BlockArg18Node extends Node {
    private Node normalBlockArgs;
    private Node blockArgAssignee;

    public BlockArg18Node(SourcePosition position, Node blockArgAssignee,
            Node normalBlockArgs) {
        super(position);

        assert blockArgAssignee != null : "Must be a value to assign too";

        this.blockArgAssignee = adopt(blockArgAssignee);
        this.normalBlockArgs = adopt(normalBlockArgs);
    }

    public Node getArgs() {
        return normalBlockArgs;
    }

    public Node getBlockArg() {
        return blockArgAssignee;
    }

    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitBlockArg18Node(this);
    }

    @Override
    public List<Node> childNodes() {
        return createList(normalBlockArgs, blockArgAssignee);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.BLOCKARG18NODE;
    }
}
