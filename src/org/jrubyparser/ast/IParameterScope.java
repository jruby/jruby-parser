/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.ast;

/**
 *
 */
public interface IParameterScope {
    public boolean isParameterUsed(String name);
    public Node getParameterNamed(String name);
}
