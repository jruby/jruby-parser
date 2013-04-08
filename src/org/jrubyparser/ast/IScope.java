package org.jrubyparser.ast;

import java.util.List;

/**
 * Represents a variable scope (block or local).
 */
public interface IScope {
    public List<ILocalVariable> getVariableReferencesNamed(String name);
}
