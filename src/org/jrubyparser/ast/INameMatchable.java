/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.ast;

/**
 * A node of this type can determine whether it matches a supplied String name
 */
public interface INameMatchable {
    public boolean isNameMatch(String name);
}
