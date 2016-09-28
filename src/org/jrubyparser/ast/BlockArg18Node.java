package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * Similiar to BlockArg, but with idiosyncracies that 1.8.7 allows:
 *
 * <pre>
 * proc { |a,&amp;b| }
 * proc { |a,&amp;FOO| }
 * proc { |a,b.c| }
 * proc { |a,b[0]| }
 * </pre>
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


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node node) {
        if (!super.isSame(node)) return false;

        BlockArg18Node other = (BlockArg18Node) node;

        if (getArgs() == null && other.getArgs() == null) return getBlockArg().isSame(other.getBlockArg());
        if (getArgs() == null || other.getArgs() == null) return false;

        return getArgs().isSame(other.getArgs()) && getBlockArg().isSame(other.getBlockArg());
    }



    public Node getArgs() {
        return normalBlockArgs;
    }

    public Node getBlockArg() {
        return blockArgAssignee;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitBlockArg18Node(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.BLOCKARG18NODE;
    }
}
