package org.jrubyparser.util;

import org.jrubyparser.ast.Node;

/**
 * Simple struct for holding a pair of nodes
 */
public class NodePair {
    private final Node first;
    private final Node second;
    
    public NodePair(Node first, Node second) {
        this.first = first;
        this.second = second;
    }
    
    public Node getFirst() {
        return first;
    }
    
    public Node getSecond() {
        return second;
    }
    
    @Override
    public String toString() {
        return "NodePair: {" + getFirst() + ", " + getSecond() + "}";
    }
}
