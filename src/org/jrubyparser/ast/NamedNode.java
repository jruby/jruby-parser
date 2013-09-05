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


    /**
     * Checks node for 'sameness' for diffing.
     *
     * @param node to be compared to
     * @return Returns a boolean
     */
    @Override
    public boolean isSame(Node node) {
        return super.isSame(node) && isNameMatch(((NamedNode) node).getName());
    }


    public String getLexicalName() {
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
     * @param testName
     */
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
