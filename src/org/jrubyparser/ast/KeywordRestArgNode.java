package org.jrubyparser.ast;

import org.jrubyparser.ast.visitor.NodeVisitor;
import org.jrubyparser.lexer.yacc.ISourcePosition;

/**
 *
 */
public class KeywordRestArgNode extends ArgumentNode {
    public KeywordRestArgNode(ISourcePosition position, String name, int index) {
        super(position, name, index);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitKeywordRestArgNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.KEYWORDRESTARGNODE;
    }
}
