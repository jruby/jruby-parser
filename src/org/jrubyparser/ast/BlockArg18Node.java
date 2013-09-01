package org.jrubyparser.ast;

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


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    public boolean isSame(Node node) {
        if (super.isSame(node)) {
            BlockArg18Node blockArg18Node = (BlockArg18Node) node;

            if (getArgs() != null && blockArg18Node.getArgs() != null) {

                if (getArgs().isSame(blockArg18Node.getArgs()) && getBlockArg().isSame(blockArg18Node.getBlockArg())) {
                    return true;
                }

            }

            if (getArgs() == null && blockArg18Node.getArgs() == null) {
                if (getBlockArg().isSame(blockArg18Node.getBlockArg())) {
                    return true;
                }

            }

        }
        return false;
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
    public NodeType getNodeType() {
        return NodeType.BLOCKARG18NODE;
    }
}
