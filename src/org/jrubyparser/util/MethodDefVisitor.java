package org.jrubyparser.util;

import org.jrubyparser.ast.*;

import java.util.ArrayList;
import java.util.List;


public class MethodDefVisitor extends NoopVisitor {
    private IModuleScope scope;
    private List<MethodDefNode> list;

    public static List<MethodDefNode> findMethodsIn(IModuleScope scope) {
        MethodDefVisitor visitor = new MethodDefVisitor(scope);

        visitor.run();

        return visitor.getMethodList();
    }

    public MethodDefVisitor(IModuleScope scope) {
        list = new ArrayList<MethodDefNode>();
        this.scope = scope;
    }

    public void run() {
        for (Node child: ((Node) scope).childNodes()) {
            child.accept(this);
        }
    }

    public List<MethodDefNode> getMethodList() {
        return list;
    }

    /*
     * nested blocks in blocks can have variables masked with same name so we make sure scope is correct.
     */
    private void addMethodIfInModule(MethodDefNode method) {
        if (method.getClosestModule() == scope ) list.add(method);
    }


    @Override
    public Object visitDefnNode(DefnNode iVisited) {
        addMethodIfInModule((MethodDefNode) iVisited);
        return null;
    }

    @Override
    public Object visitDefsNode(DefsNode iVisited) {
        addMethodIfInModule((MethodDefNode) iVisited);
        return null;
    }



}
