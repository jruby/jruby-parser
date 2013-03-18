/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.util;

import org.jrubyparser.ast.AliasNode;
import org.jrubyparser.ast.DSymbolNode;
import org.jrubyparser.ast.ILocalVariable;
import org.jrubyparser.ast.INameMatchable;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.ZSuperNode;

/**
 *
 * @author enebo
 */
public class VariableHelper {
    public static boolean isParameterUsed(Node node, String name, boolean isMethod) {
        for (Node child: node.childNodes()) {
            if (child instanceof INameMatchable && child instanceof ILocalVariable && 
                    ((INameMatchable) child).isNameMatch(name)) {
                return true;
            } else if (child instanceof ZSuperNode) {
                return true;
            } else if (child instanceof AliasNode) {
                boolean match = ((AliasNode) child).oldNameMatches(name);
                if (match) return match;

                // alias :new_thing :"old#{my_parameter}"
                if (((AliasNode) child).getOldName() instanceof DSymbolNode &&
                        isParameterUsed(((AliasNode) child).getOldName(), name, isMethod)) {
                    return true;
                }
                    
                // alias :"new#{my_parameter}" :thing
                if (((AliasNode) child).getNewName() instanceof DSymbolNode && 
                        isParameterUsed(((AliasNode) child).getNewName(), name, isMethod)) {
                    return true;
                }
            }

            // For all non-special nodes recurse and look to see if subtree contains the name
            if (isParameterUsed(child, name, isMethod)) return true;
        }
            
        return false;
    }    
}
