/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jrubyparser.ast;

import org.jrubyparser.ast.visitor.NodeVisitor;
import org.jrubyparser.lexer.yacc.ISourcePosition;

import java.util.List;

/**
 *
 * @author enebo
 */
public class ComplexNode extends NumericNode implements SideEffectFree {
    private NumericNode y;

    public ComplexNode(ISourcePosition position, NumericNode y) {
        super(position);

        this.y = y;
    }

    @Override
    public Object accept(NodeVisitor visitor) {
       return visitor.visitComplexNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.COMPLEXNODE;
    }

    public NumericNode getNumber() {
        return y;
    }

    public void setNumber(NumericNode y) {
        this.y = y;
    }
}
