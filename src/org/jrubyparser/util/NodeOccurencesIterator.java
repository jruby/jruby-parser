/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.util;

import java.util.Iterator;
import org.jrubyparser.ast.ILocalScope;
import org.jrubyparser.ast.ILocalVariable;
import org.jrubyparser.ast.IParameterScope;
import org.jrubyparser.ast.Node;

/**
 * Starting out simple here and allowing
 */
public class NodeOccurencesIterator implements Iterator {
    public NodeOccurencesIterator(ILocalVariable sourceNode) {
        Node source = (Node) sourceNode;
        String name = sourceNode.getName();
        for (Node current = source.getParent(); current != null; current = current.getParent()) {
            if (!(current instanceof ILocalScope)) continue;
            if (!(current instanceof IParameterScope)) {
                // This is end of line
                return;
            }
            
            // Is it a block or method and it has same-named variable defined in it.
            if (((IParameterScope) current).isParameterUsed(name)) {
                return;
            }
        }        
    }

    public boolean hasNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object next() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
