/*
 * Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
 */
package org.jrubyparser.parser;

import org.jrubyparser.ISourcePositionHolder;
import org.jrubyparser.ast.MethodNameNode;
import org.jrubyparser.ast.Node;

/**
 * Carries the partially-built information for a method definition head
 * (Ruby 3.0 {@code defn_head}/{@code defs_head}) so that both the normal
 * {@code def name args body end} form and the endless {@code def name = expr}
 * form can share a single grammar prefix and push their local scope once.
 */
public class DefHolder {
    /** The {@code def} keyword token (used as the node start position). */
    public final ISourcePositionHolder keyword;
    public final MethodNameNode nameNode;
    /** Receiver for singleton method definitions ({@code def obj.name}); null for plain defs. */
    public final Node receiver;

    public DefHolder(ISourcePositionHolder keyword, MethodNameNode nameNode) {
        this(keyword, nameNode, null);
    }

    public DefHolder(ISourcePositionHolder keyword, MethodNameNode nameNode, Node receiver) {
        this.keyword = keyword;
        this.nameNode = nameNode;
        this.receiver = receiver;
    }
}
