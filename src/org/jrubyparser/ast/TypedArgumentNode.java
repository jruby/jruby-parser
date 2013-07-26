/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jrubyparser.ast;

import org.jrubyparser.SourcePosition;

/**
 *
 * @author enebo
 */
public class TypedArgumentNode extends ArgumentNode {
    private Node typeNode;
    
    public TypedArgumentNode(SourcePosition position, String identifier, Node typeNode) {
        super(position, identifier);
        
        this.typeNode = adopt(typeNode);
    }
    
    public Node getTypeNode() {
        return typeNode;
    }
}
