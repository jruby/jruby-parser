package org.jrubyparser.ast;

import org.jrubyparser.SourcePosition;

/**
 * Ruby keywords like self,true,false,nil.
 */
public abstract class BareKeywordNode extends Node implements INameNode {
    private String name;
    
    public BareKeywordNode(SourcePosition position, String name) {
        super(position);
     
        this.name = name;
    }


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param other to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node other) {
        return super.isSame(other) && isNameMatch(((BareKeywordNode) other).getName());
    }


    public String getLexicalName() {
        return getName();
    }
    
    /**
     * Get name of self node.
     */
    public String getName() {
        return name;
    }
    
    public void setName(String newName) {
        // FIXME: error or noop?
    }
    
    public boolean isNameMatch(String testName) {
        return name.equals(testName);
    }
    
    public SourcePosition getNamePosition() {
        return getPosition();
    }
    
    public SourcePosition getLexicalNamePosition() {
        return getPosition();
    }    
}
