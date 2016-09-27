package org.jrubyparser.parser;



/**
 * Gives instances of static scopes based on compile mode.
 */
public class StaticScopeFactory {
    private final StaticScope dummyScope;

    public StaticScopeFactory() {
        dummyScope = new StaticScope(StaticScope.Type.LOCAL, null);
    }

    public static StaticScope newBlockScope(StaticScope parent, String file) {
        return new StaticScope(StaticScope.Type.BLOCK, parent, file);
    }

    public StaticScope newBlockScope(StaticScope parent) {
        return new StaticScope(StaticScope.Type.BLOCK, parent);
    }

    public StaticScope newBlockScope(StaticScope parent, String[] names) {
        return new StaticScope(StaticScope.Type.BLOCK, parent, names);
    }

    public StaticScope newEvalScope(StaticScope parent) {
        return new StaticScope(StaticScope.Type.EVAL, parent);
    }

    public StaticScope newEvalScope(StaticScope parent, String[] names) {
        return new StaticScope(StaticScope.Type.EVAL, parent, names);
    }

    public static StaticScope newLocalScope(StaticScope parent, String file) {
        return new StaticScope(StaticScope.Type.LOCAL, parent, file);
    }

    public static StaticScope newLocalScope(StaticScope parent) {
        return new StaticScope(StaticScope.Type.LOCAL, parent);
    }

    public StaticScope newLocalScope(StaticScope parent, String[] names) {
        return new StaticScope(StaticScope.Type.LOCAL, parent, names);
    }

    // We only call these from inside IR impl (IR is all or nothing)
    public static StaticScope newIRBlockScope(StaticScope parent) {
        return new StaticScope(StaticScope.Type.BLOCK, parent);
    }

    public static StaticScope newStaticScope(StaticScope parent, StaticScope.Type type, String[] names) {
        if(names == null) {
            return new StaticScope(type, parent);
        } else {
            return new StaticScope(type, parent, names);
        }
    }

    public StaticScope getDummyScope() {
        return dummyScope;
    }
}
