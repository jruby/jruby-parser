/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.util;

import java.util.ArrayList;
import java.util.List;
import org.jrubyparser.ast.ArgumentNode;
import org.jrubyparser.ast.ClassNode;
import org.jrubyparser.ast.DAsgnNode;
import org.jrubyparser.ast.DVarNode;
import org.jrubyparser.ast.DefnNode;
import org.jrubyparser.ast.DefsNode;
import org.jrubyparser.ast.ILocalVariable;
import org.jrubyparser.ast.IScope;
import org.jrubyparser.ast.LocalAsgnNode;
import org.jrubyparser.ast.LocalVarNode;
import org.jrubyparser.ast.ModuleNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.RootNode;
import org.jrubyparser.ast.SClassNode;

/**
 * Find all ILocalVariables for a particular scope.  The implementation will walk all children
 * until it runs into a nested scope which cannot possibly be in the current scope (e.g. a def
 * in a def).  IterNode is the lone IScope which can capture external ILocalVariables so we 
 * let the visitor enter into them.  Restricting this walking is meant as an optimization since
 * our logic will only allow variables of the same scope to be added to the list.
 * 
 */
public class ILocalVariableVisitor extends NoopVisitor {
    private List<ILocalVariable> list;
    private String name;
    private IScope scope;
    
    public static List<ILocalVariable> findOccurrencesIn(IScope scope, String name) {
        ILocalVariableVisitor visitor = new ILocalVariableVisitor(scope, name);
        
        visitor.run();
        
        return visitor.getVariableList();
    }
    
    public ILocalVariableVisitor(IScope scope, String name) {
        list = new ArrayList<ILocalVariable>();
        this.scope = scope;
        this.name = name;
    }
    
    public void run() {
        for (Node child: ((Node) scope).childNodes()) {
            child.accept(this);
        }
    }
    
    public List<ILocalVariable> getVariableList() {
        return list;
    }
    
    /*
     * nested blocks in blocks can have variables masked with same name so we make sure scope is correct.
     */
    private void addVariableIfInScopeAndRightName(ILocalVariable variable) {
        if (variable.getDefinedScope() == scope && name.equals(variable.getName())) list.add(variable);
    }

    @Override
    public Object visitArgumentNode(ArgumentNode iVisited) {
        addVariableIfInScopeAndRightName(iVisited);
        return null;
    }

    @Override
    public Object visitClassNode(ClassNode iVisited) {
        return null;
    }

    @Override
    public Object visitDAsgnNode(DAsgnNode iVisited) {
        addVariableIfInScopeAndRightName(iVisited);
        return null;
    }

    @Override
    public Object visitDVarNode(DVarNode iVisited) {
        addVariableIfInScopeAndRightName(iVisited);
        return null;
    }

    @Override
    public Object visitDefnNode(DefnNode iVisited) {
        return null;
    }

    @Override
    public Object visitDefsNode(DefsNode iVisited) {
        return null;
    }

    @Override
    public Object visitLocalAsgnNode(LocalAsgnNode iVisited) {
        addVariableIfInScopeAndRightName(iVisited);
        return null;
    }

    @Override
    public Object visitLocalVarNode(LocalVarNode iVisited) {
        addVariableIfInScopeAndRightName(iVisited);
        return null;
    }

    @Override
    public Object visitModuleNode(ModuleNode iVisited) {
        return null;
    }

    @Override
    public Object visitRootNode(RootNode iVisited) {
        return null; // should not happen
    }

    @Override
    public Object visitSClassNode(SClassNode iVisited) {
        return null;
    }
}
