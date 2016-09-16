/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jrubyparser.ast;

/**
 * Marker interface for return, break, next, redo, retry
 */
public interface NonLocalControlFlowNode {
    public Node getValueNode();
    public boolean hasValue();
}
