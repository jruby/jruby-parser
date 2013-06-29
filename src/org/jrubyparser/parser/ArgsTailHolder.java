package org.jrubyparser.parser;

import org.jrubyparser.ISourcePositionHolder;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.ast.BlockArgNode;
import org.jrubyparser.ast.KeywordRestArgNode;
import org.jrubyparser.ast.ListNode;

/**
 * Simple struct to hold values until they can be inserted into the AST.
 */
public class ArgsTailHolder implements ISourcePositionHolder {
    private SourcePosition position;
    private BlockArgNode blockArg;
    private ListNode keywordArgs;
    private KeywordRestArgNode keywordRestArg;
    
    public ArgsTailHolder(SourcePosition position, ListNode keywordArgs,
            KeywordRestArgNode keywordRestArg, BlockArgNode blockArg) {
        this.position = position;
        this.blockArg = blockArg;
        this.keywordArgs = keywordArgs;
        this.keywordRestArg = keywordRestArg;
    }
    
    public SourcePosition getPosition() {
        return position;
    }
    
    public void setPosition(SourcePosition position) {
        this.position = position;
    }    
    
    public BlockArgNode getBlockArg() {
        return blockArg;
    }
    
    public ListNode getKeywordArgs() {
        return keywordArgs;
    }
    
    public KeywordRestArgNode getKeywordRestArgNode() {
        return keywordRestArg;
    }
    
    /**
     * Does this holder support either keyword argument types
     */
    public boolean hasKeywordArgs() {
        return keywordArgs != null || keywordRestArg != null;
    }
}
