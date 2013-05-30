package org.jrubyparser.ast;

import org.jrubyparser.SourcePosition;

/**
 * Nodes with string names are very common.
 */
public abstract class NamedNode extends Node implements INameNode {
    private String name;

    public NamedNode(SourcePosition position, String name) {
        super(position);
        
        assert name != null : "Name node with no null name";
        
        this.name = name;
    }
    
    public String getDecoratedName() {
        return getName();
    }
    
    /**
     * Gets the name.
     * @return Returns a String
     */    
    public String getName() {
        return name;
    }

    /**
     * Sets the name (for refactoring support)
     * @param name is the new name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Does the supplied name match this one?
     * @param name
     */
    public boolean isNameMatch(String testName) {
        return name.equals(testName);
    }
    
    public SourcePosition getNamePosition() {
        return getPosition();
    }
    
    public SourcePosition getDecoratedNamePosition() {
        return getPosition();
    }    
}
