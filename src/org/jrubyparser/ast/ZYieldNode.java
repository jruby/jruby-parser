package org.jrubyparser.ast;

import org.jrubyparser.SourcePosition;

/**
 * A Yield node with no parens
 */
public class ZYieldNode extends YieldNode {
    public ZYieldNode(SourcePosition position) {
        super(position, null, true);
    }
}
