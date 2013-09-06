/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 *
 * @author enebo
 */
public class KeywordArgNode extends Node {
    private AssignableNode assignable;
    
    public KeywordArgNode(SourcePosition position, AssignableNode assignable) {
        super(position);
        this.assignable = assignable;
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node node) {
        return super.isSame(node) && getAssignable().isSame(((KeywordArgNode) node).getAssignable());
    }


    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitKeywordArgNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.KEYWORDARGNODE;
    }

    public AssignableNode getAssignable() {
        return assignable;
    }
    
}
