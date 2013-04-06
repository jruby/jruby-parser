/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.ast;

/**
 * Simple marker interface to indicate this node type is a type of local variable (block or local).
 */
public interface ILocalVariable extends INameNode {
    /**
     * Which Variable Scope does this variable belong to?  Note that RootNode is a special ILocalScope
     * for the implicit scope created at top-level.
     */
    public Node getDefinedScope();
}
