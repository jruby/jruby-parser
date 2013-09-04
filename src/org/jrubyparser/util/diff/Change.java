/*
 ***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2013 The JRuby team
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/

package org.jrubyparser.util.diff;

import org.jrubyparser.ast.Node;

public class Change {

    private Node oldNode;
    private Node newNode;
    private int oldCost;
    private int newCost;

    /**
     * Each changed node in the diff is represented by a Change object.
     * It should hold enough information to locate the change, and determine
     * whether the change was an insertion, deletion or modification.
     *
     * @param newNode the new version of the node
     * @param newCost the complexity/depth of the node that was changed
     * @param oldNode the old version of the node
     * @param oldCost the complexity/depth of the original node
     */
    public Change(Node newNode, int newCost, Node oldNode, int oldCost) {
        setNewNode(newNode);
        setOldNode(oldNode);
        setNewCost(newCost);
        setOldCost(oldCost);
    }

    public void setNewNode(Node newNode) {
        this.newNode = newNode;
    }

    public void setOldNode(Node oldNode) {
        this.oldNode = oldNode;
    }

    public void setNewCost(int newCost) {
        this.newCost = newCost;
    }

    public void setOldCost(int oldCost) {
        this.oldCost = oldCost;
    }

    public Node getOldNode() {
        return oldNode;
    }

    public Node getNewNode() {
        return newNode;
    }

    public int getOldCost() {
        return oldCost;
    }

    public int getNewCost() {
        return newCost;
    }

    public int getTotalCost() {
        return newCost + oldCost;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(60);
        builder.append("\nChange: ");

        if (getOldNode() != null) {
            builder.append("\nOld Node: ").append(getOldNode()).append(" Complexity: ").append(getOldCost()).append(" Position: ").append(getOldNode().getPosition()).append("\n");
        }
        if (getNewNode() != null) {
            builder.append("\nNew Node: ").append(getNewNode()).append(" Complexity: ").append(getNewCost()).append(" Position: ").append(getNewNode().getPosition()).append("\n");
        }




        return builder.toString();
    }
}
