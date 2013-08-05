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
import java.util.Iterator;
import java.util.List;


/**
 * The SequenceMatcher class is used to produce a list of matching nodes.
 * @see NodeDiff
 */
public class SequenceMatcher
{
    protected IsJunk isJunk;
    protected Node newNode;
    protected Node oldNode;
    protected boolean hasIsJunk = true;
    protected ArrayList<Change> diffNodes;

    /**
     * Create a SequenceMatcher object without a function for sorting out junk.
     *
     * @param newNode The current version of the node.
     * @param oldNode The original version of the node being checked.
     */
    public SequenceMatcher(Node newNode, Node oldNode) {
        this(newNode, oldNode, null);
    }

    /**
     * SequenceMatcher compares two nodes for matching nodes.
     *
     * isJunk is an object which implements the {@link IsJunk} interface and the
     * #hasIsJunk() method. hasIsJunk is a method which takes a Node and determines
     * whether or not it should be compared against the other node.
     *
     * We pass in two nodes. Later, we can use the #setSequence, #setSequenceOne and
     * #setSequenceTwo methods to change out one or both of the nodes and create a new set of
     * matches.
     *
     * @param isJunk A callback used to let users choose nodes not to be checked in diff.
     * @param newNode The current version of the node.
     * @param oldNode The node in the old version.
     */
    public SequenceMatcher(Node newNode, Node oldNode, IsJunk isJunk) {
        if (isJunk == null) {
            hasIsJunk = false;
        } else {
            this.isJunk = isJunk;
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
            this.diffNodes  = new ArrayList<>();
        }
    }

    public Node getSequenceOne() {
        return this.newNode;
    }

    public Node getSequenceTwo() {
        return this.oldNode;
    }

    /**
     * Checks whether two nodes qualify as 'equal' according or should be added
     * to diff. This would be a good place to override if you need to manipulate
     * how diffing is done.
     *
     * @param newNode The current version of the node.
     * @param oldNode The original version of the node being checked.
     * @return This returns a boolean which says whether the nodes are equal or not.
     */
    public boolean isSameNode(Node newNode, Node oldNode) {
        return newNode.getNodeType() == oldNode.getNodeType() && newNode.isSame(oldNode);
    }

    /**
     * Decides what to do with nodes being diffed.
     *
     * @param childNew The current version of the node being diffed.
     * @param childOld The original version of the node being diffed.
     */
    public void sortNodesIntoDiff(Node childNew, Node childOld) {

        if (isSameNode(childNew, childOld)) {
            if (childNew.isLeaf()) {
                if (childOld.isLeaf()) {
                    return;
                } else {
                    this.diffNodes.add(new Change(null, 0, childOld, childOld.getComplexity()));
                }
            } else if (childOld.isLeaf()) {
                this.diffNodes.add(new Change(childNew, childNew.getComplexity(), null, 0));
            } else {
                findChanges(childNew, childOld);
            }
        } else {
            this.diffNodes.add(new Change(childNew, childNew.getComplexity(), childOld, childOld.getComplexity()));
        }

    }

    /**
     * This method does the diffing by iterating through the nodes and
     * calling itself recursively.
     *
     * @param newNode The node in the new version.
     * @param oldNode The node in the old version.
     */
    public void findChanges(Node newNode, Node oldNode) {

        Iterator<Node> oldChildren = oldNode.childNodes().iterator();
        List<Node> newChildren = newNode.childNodes();

        for (Node childNew : newChildren) {
            if (oldChildren.hasNext()) {
                Node childOld = oldChildren.next();

                if (hasIsJunk) {
                    if (!isJunk.checkJunk(childNew) && !isJunk.checkJunk(childOld)) {
                        sortNodesIntoDiff(childNew, childOld);
                    } else {
                        findChanges(childNew, childOld);
                    }

                    } else {
                    sortNodesIntoDiff(childNew, childOld);
                }
                }

        }
    }

    public void checkDiffForMoves() {
        Iterator<Change> newn = diffNodes.iterator();

        for (Change change : diffNodes) {
            Node oldNode = change.getOldNode();
            while (newn.hasNext()) {
                Change newChange = newn.next();
                Node newNode = newChange.getNewNode();
                if (oldNode.isSame(newNode)) {
                    change.setNewNode(newNode);
                    change.setNewCost(newNode.getComplexity());
                    diffNodes.remove(newChange);
                }
            }
        }
    }

    public ArrayList<Change> getDiffNodes() {
        return diffNodes;
    }

}

