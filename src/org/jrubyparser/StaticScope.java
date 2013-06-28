/*
 ***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2006-2009 Thomas E Enebo <enebo@acm.org>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jrubyparser;

import java.io.Serializable;

import org.jrubyparser.ast.AssignableNode;
import org.jrubyparser.ast.Node;

/**
 * StaticScope represents lexical scoping of variables and module/class constants.
 * 
 * At a very high level every scopes enclosing scope contains variables in the next outer
 * lexical layer.  The enclosing scopes variables may or may not be reachable depending
 * on the scoping rules for variables (governed by BlockStaticScope and LocalStaticScope).
 * 
 * StaticScope also keeps track of current module/class that is in scope.  previousCRefScope
 * will point to the previous scope of the enclosing module/class (cref).
 * 
 */
public abstract class StaticScope implements Serializable {
    private static final long serialVersionUID = 4843861446986961013L;
    
    // Next immediate scope.  Variable and constant scoping rules make use of this variable
    // in different ways.
    final protected StaticScope enclosingScope;
    
    // Next CRef down the lexical structure
    private StaticScope previousCRefScope = null;
    
    // Our name holder (offsets are assigned as variables are added
    private String[] variableNames;
    
    private boolean[] variableCaptured;
    
    // Whether this scope is used as the "argument scope" for e.g. zsuper
    private boolean isArgumentScope = false;
    
    protected StaticScope(StaticScope enclosingScope, String[] names) {
        assert names != null : "names is not null";
        assert namesAreInterned(names);
        
        this.enclosingScope = enclosingScope;
        this.variableNames = names;
        this.variableCaptured = new boolean[variableNames.length];
    }

    private static boolean namesAreInterned(String[] names) {
        for (String name : names) {
            if (name != name.intern()) return false;
        }
        return true;
    }
    
    /**
     * Add a new variable to this (current) scope unless it is already defined in the
     * current scope.
     *
     * @param name of new variable
     * @return index+depth merged location of scope
     */
    public int addVariableThisScope(String name) {
        int slot = exists(name);

        if (slot >= 0) return slot;

        // This is perhaps innefficient timewise?  Optimal spacewise
        growVariableNames(name);

        // Returns slot of variable
        return variableNames.length - 1;
    }
    
    public int addVariable(String name) {
        int slot = isDefined(name); 

        if (slot >= 0) return slot;
            
        // This is perhaps innefficient timewise?  Optimal spacewise
        growVariableNames(name);
        
        // Returns slot of variable
        return variableNames.length - 1;
    }
    
    public String[] getVariables() {
        return variableNames;
    }
    
    public int getNumberOfVariables() {
        return variableNames.length;
    }
    
    public void setVariables(String[] names) {
        assert names != null : "names is not null";
        
        variableNames = new String[names.length];
        System.arraycopy(names, 0, variableNames, 0, names.length);
        variableCaptured = new boolean[variableNames.length];
    }
    
    /**
     * Next outer most scope in list of scopes.  An enclosing scope may have no direct scoping
     * relationship to its child.  If I am in a localScope and then I enter something which
     * creates another localScope the enclosing scope will be the first scope, but there are
     * no valid scoping relationships between the two.  Methods which walk the enclosing scopes
     * are responsible for enforcing appropriate scoping relationships.
     * 
     * @return the parent scope
     */
    public StaticScope getEnclosingScope() {
        return enclosingScope;
    }
    
    /**
     * Does the variable exist?
     * 
     * @param name of the variable to find
     * @return index of variable or -1 if it does not exist
     */
    public int exists(String name) {
        return findVariableName(name);
    }
    
    private int findVariableName(String name) {
        for (int i = 0; i < variableNames.length; i++) {
            if (name.equals(variableNames[i])) return i;
        }
        return -1;
    }
    
    /**
     * Is this name in the visible to the current scope
     * 
     * @param name to be looked for
     * @return a location where the left-most 16 bits of number of scopes down it is and the
     *   right-most 16 bits represents its index in that scope
     */
    public int isDefined(String name) {
        return isDefined(name, 0);
    }
    
    /**
     * Make a DASgn or LocalAsgn node based on scope logic
     * 
     * @param position
     * @param name
     * @param value
     * @return
     */
    public AssignableNode assign(SourcePosition position, String name, Node value) {
        return assign(position, name, value, this, 0);
    }
    
    /**
     * Get all visible variables that we can see from this scope that have been assigned
     * (e.g. seen so far)
     * 
     * @return a list of all names (sans $~ and $_ which are special names)
     */
    public abstract String[] getAllNamesInScope();
    
    protected abstract int isDefined(String name, int depth);
    protected abstract AssignableNode assign(SourcePosition position, String name, Node value, 
            StaticScope topScope, int depth);
    protected abstract Node declare(SourcePosition position, String name, int depth);
    
    /**
     * Make a DVar or LocalVar node based on scoping logic
     * 
     * @param position the location that in the source that the new node will come from
     * @param name of the variable to be created is named
     * @return a DVarNode or LocalVarNode
     */
    public Node declare(SourcePosition position, String name) {
        return declare(position, name, 0);
    }
    
    public void capture(int index) {
        variableCaptured[index] = true;
    }
    
    public boolean isCaptured(int index) {
        return variableCaptured[index];
    }

    /**
     * Gets the Local Scope relative to the current Scope.  For LocalScopes this will be itself.
     * Blocks will contain the LocalScope it contains.
     * 
     * @return localScope
     */
    public abstract StaticScope getLocalScope();
    
    
    public StaticScope getPreviousCRefScope() {
        return previousCRefScope;
    }
    
    public boolean isArgumentScope() {
        return isArgumentScope;
    }
    
    public void setArgumentScope(boolean isArgumentScope) {
        this.isArgumentScope = isArgumentScope;
    }

    private void growVariableNames(String name) {
        String[] newVariableNames = new String[variableNames.length + 1];
        System.arraycopy(variableNames, 0, newVariableNames, 0, variableNames.length);
        variableNames = newVariableNames;
        variableNames[variableNames.length - 1] = name;
        boolean[] newVariableCaptured = new boolean[variableCaptured.length + 1];
        System.arraycopy(variableCaptured, 0, newVariableCaptured, 0, variableCaptured.length);
        variableCaptured = newVariableCaptured;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("[");
            
        for (int i = 0; i < variableNames.length - 1; i++) {
            buf.append(variableNames[i]).append(", ");
        }
        if (variableNames.length > 0) {
            buf.append(variableNames[variableNames.length - 1]);
        }
        buf.append("]");
            
        return buf.toString();
    }
}
