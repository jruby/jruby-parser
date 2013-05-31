/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.util;

import java.util.ArrayList;
import java.util.List;
import org.jrubyparser.ast.ClassNode;
import org.jrubyparser.ast.IInstanceVariable;
import org.jrubyparser.ast.InstAsgnNode;
import org.jrubyparser.ast.InstVarNode;
import org.jrubyparser.ast.ModuleNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.SClassNode;

/**
 * To use this visitor pass in the root of the scope you want to search for instance variables.
 * This will not enter into contained classes/modules/singleton-classes so it should not see
 * same-named ivars in a nested class.  If you want those as well it is up to you to realize
 * the ivars in those nested objects refer to the same instance variables and you need to visit
 * them separately.
 */
public class IInstanceVariableVisitor extends NoopVisitor {
    private List<IInstanceVariable> list;
    private String name;
    private Node root;
    
    /**
     * This helper method uses Node instead of IModuleScope so that you can do more localized
     * searching (e.g. all instance variables in a method).
     */
    public static List<IInstanceVariable> findOccurrencesIn(Node root, String name) {
        IInstanceVariableVisitor visitor = new IInstanceVariableVisitor(root, name);
        
        visitor.run();
        
        return visitor.getVariableList();
    }
    
    public IInstanceVariableVisitor(Node root, String name) {
        list = new ArrayList<IInstanceVariable>();
        this.name = name;
    }
    
    public void run() {
        for (Node child: root.childNodes()) {
            child.accept(this);
        }
    }
    
    public List<IInstanceVariable> getVariableList() {
        return list;
    }
    
    /*
     * nested blocks in blocks can have variables masked with same name so we make sure scope is correct.
     */
    private void addVariableIfInScopeAndRightName(IInstanceVariable variable) {
        if (name.equals(variable.getName())) list.add(variable);
    }

    @Override
    public Object visitInstAsgnNode(InstAsgnNode iVisited) {
        addVariableIfInScopeAndRightName(iVisited);
        return null;
    }
    
    @Override
    public Object visitInstVarNode(InstVarNode iVisited) {
        addVariableIfInScopeAndRightName(iVisited);
        return null;
    }

    @Override
    public Object visitClassNode(ClassNode iVisited) {
        return null;
    }

    @Override
    public Object visitModuleNode(ModuleNode iVisited) {
        return null;
    }

    @Override
    public Object visitSClassNode(SClassNode iVisited) {
        return null;
    }
}
