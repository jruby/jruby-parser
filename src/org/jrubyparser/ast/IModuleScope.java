package org.jrubyparser.ast;

import java.util.List;

/**
 * Marker interface to indicate a Module/Class/SClass (all extend module in Ruby semantics).
 */
public interface IModuleScope {
    /**
     * Methods defined on the module.
     *
     * @return A List containing all the MethodDefNode's defined on a module.
     */
    public List<MethodDefNode> getMethodDefs();

}
