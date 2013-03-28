/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.util;

import org.jrubyparser.ast.AliasNode;
import org.jrubyparser.ast.DAsgnNode;
import org.jrubyparser.ast.DSymbolNode;
import org.jrubyparser.ast.ILocalScope;
import org.jrubyparser.ast.ILocalVariable;
import org.jrubyparser.ast.INameMatchable;
import org.jrubyparser.ast.LocalAsgnNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.ZSuperNode;

/**
 *
 * @author enebo
 */
public class VariableHelper {
    public static boolean isParameterUsed(Node node, String name, boolean isMethod) {
        if (node == null) return false; // Empty methods have null bodies (other nodes may also pass in null)
        
        for (Node child: node.childNodes()) {
            if (child instanceof INameMatchable && child instanceof ILocalVariable && 
                    ((INameMatchable) child).isNameMatch(name)) {
                // Assignment of a variable by itself does not indicate it is used
                if (!(child instanceof DAsgnNode) && !(child instanceof LocalAsgnNode)) return true; 
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

    /**
     * Can we find a parameter named by name in the arguments node (presumably from an iter or methoddef)?
     */
    public static Node getParameterName(Node argsNode, String name) {
        return getParameterNameInner(argsNode, name);
    }

    private static Node getParameterNameInner(Node node, String name) {
        for (Node child: node.childNodes()) {
            if (child instanceof ILocalScope) return null; // Any nested scope in an ARGS context will stop the search
            if (child instanceof ILocalVariable && ((INameMatchable) child).isNameMatch(name)) return child;

            Node result = getParameterName(child, name);
            if (result != null) return result;
        }
            
        return null;
    }
    
}
