package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 *
 */
public class KeywordRestArgNode extends ArgumentNode {
    public KeywordRestArgNode(SourcePosition position, String name, int index) {
        super(position, name, index);
    }
    
    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitKeywordRestArgNode(this);
    }
    
    @Override
    public NodeType getNodeType() {
        return NodeType.KEYWORDRESTARGNODE;
    }
}
