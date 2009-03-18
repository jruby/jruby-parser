/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jrubyparser.ast;

import org.jrubyparser.SourcePosition;

/**
 * Represents a constant path which ends in a method (e.g. Foo::bar).  Note: methods with
 * explicit parameters (e.g. Foo::bar()) will be a CallNode.
 */
public class Colon2MethodNode extends Colon2Node {
    public Colon2MethodNode(SourcePosition position, Node leftNode, String name) {
        super(position, leftNode, name);

        assert leftNode != null: "class fooBar is not valid";
    }
}
