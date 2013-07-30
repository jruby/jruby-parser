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

import java.util.ArrayList;


/**
 * The SequenceMatcher class is used to produce a list of matching nodes.
 * @see NodeDiff
 */
public class SequenceMatcher
{
    private IsJunk isJunk;
    private Node newNode;
    private Node oldNode;
    private boolean hasIsJunk = true;
    protected ArrayList<Node> matchingNodes;

    /**
     * Create a SequenceMatcher object without a function for sorting out junk.
     *
     * @param newNode
     * @param oldNode
     */
    public SequenceMatcher(Node newNode, Node oldNode) {
        this(newNode, oldNode, null);
    }

    /**
     * isJunk is an object which implements the {@link IsJunk} interface and the
     * #hasIsJunk() method. hasIsJunk is a method which takes a Node and determines
     * whether or not it should be compared against the other node.
     *
     * We pass in two nodes. Later, we can use the #setSequence, #setSequenceOne and
     * #setSequenceTwo methods to change out one or both of the nodes and create a new set of
     * matches.
     *
     * @param isJunk
     * @param newNode
     * @param oldNode
     */
    public SequenceMatcher(Node newNode, Node oldNode, IsJunk isJunk) {
        if (isJunk == null) {
            hasIsJunk = false;
        } else {
            this.isJunk = isJunk;
            isJunk.checkJunk(newNode);
        }

        setSequences(newNode, oldNode);

    }

    public void setSequences(Node newNode, Node oldNode) {
        setSequenceOne(newNode);
        setSequenceTwo(oldNode);
    }

    public void setSequenceOne(Node newNode) {
        this.newNode = newNode;
    }

    public void setSequenceTwo(Node oldNode) {
        if (this.oldNode == oldNode) {
            return;
        } else {
            this.oldNode = oldNode;
            this.matchingNodes = null;

        }
    }

    public Node getSequenceOne() {
        return this.newNode;
    }

    public Node getSequenceTwo() {
        return this.oldNode;
    }
}

