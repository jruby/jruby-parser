/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.ast;

import java.util.List;

/**
 * Simple marker interface to indicate this node type is a type of local variable (block or local).
 */
public interface ILocalVariable extends INameNode {
    /**
     * Which Variable Scope does this variable belong to?  Note that RootNode is a special ILocalScope
     * for the implicit scope created at top-level.
     *
     * @return the defined scope
     */
    public IScope getDefinedScope();
    
    /**
     * Retrieve the node which is responsible for declaring this one.  This can be a variable
     * or a parameter.
     * 
     * @return the declaration variable
     */
    public ILocalVariable getDeclaration();
    
    /**
     * Find all occurrences of this variable including itself.
     *
     * @return the list of all occurrences.
     */
    public List<ILocalVariable> getOccurrences();
}
