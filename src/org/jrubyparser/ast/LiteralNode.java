package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.lexer.Token;

/**
 * This is not a node in the classic sense in that it has no defined or
 * interpret method which can be called.  It just stores the position of
 * the literal and the name/value of the literal.  We made it a node so that
 * the parser needs to work less hard in its productions.  dynamic literals
 * are nodes and by having literals also be nodes means they have a common
 * subtype which is not Object.
 */
public class LiteralNode extends Node {
    private String name;

    public LiteralNode(Token token) {
        super(token.getPosition());

        this.name = (String) token.getValue();
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node node) {
        return super.isSame(node) && getName().equals(((LiteralNode) node).getName());
    }


    public String getName() {
        return name;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitLiteralNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.LITERALNODE;
    }

}