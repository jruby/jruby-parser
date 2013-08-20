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

import org.jrubyparser.ast.NewlineNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.NodeType;

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
     * We pass in two nodes. Later, we can use the #setSequence, #setNewNode and
     * #setOldNode methods to change out one or both of the nodes and create a new set of
     * matches.
     *
     * @param isJunk A callback used to let users choose nodes not to be checked in diff.
     * @param newNode The current version of the node.
     * @param oldNode The node in the old version.
     */
    public SequenceMatcher(Node newNode, Node oldNode, IsJunk isJunk) {
        this.isJunk = isJunk;

        setSequences(newNode, oldNode);
        this.diffNodes  = new ArrayList<>();

    }

    public void setSequences(Node newNode, Node oldNode) {
        setNewNode(newNode);
        setOldNode(oldNode);
    }

    public void setNewNode(Node newNode) {
        this.newNode = newNode;
    }

    public void setOldNode(Node oldNode) {
            this.oldNode = oldNode;
    }

    public Node getNewNode() {
        return this.newNode;
    }

    public Node getOldNode() {
        return this.oldNode;
    }

    /**
     * Checks whether two nodes qualify as 'equal' according to the #isSame() method
     * or should be added to diff. This would be a good place to override if you
     * need to manipulate how diffing is done.
     *
     * @param newNode The current version of the node.
     * @param oldNode The original version of the node being checked.
     * @return This returns a boolean which says whether the nodes are equal or not.
     */
    public boolean isSameNode(Node newNode, Node oldNode) {
        return newNode.getNodeType() == oldNode.getNodeType() && newNode.isSame(oldNode);
    }

    /**
     * We use this to determine if two nodes are similar enough in internal
     * structure to be considered modified versions of the same node
     *
     * @param newNode The node in the new version.
     * @param oldNode The original version of the node being checked.
     * @return Returns an int which measures the editing distance between two nodes
     */
    public int findDistance(Node newNode, Node oldNode) {
        int distance = 1;

        Node biggest = calcComplexity(newNode) >= calcComplexity(oldNode) ? newNode : oldNode;
        Node smallest = biggest == newNode ? oldNode : newNode;

        Iterator<Node> sck = smallest.childNodes().iterator();

        for (Node bchild : biggest.childNodes()) {
            if (sck.hasNext()) {
                Node schild = sck.next();
                if (!isSameNode(schild, bchild)) {
                    distance = distance + 1 * (findDistance(schild, bchild));
                }
            } else {
                distance = distance + 1 * (calcComplexity(bchild));
            }
        }

        return distance;
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
                    deletedNode(childOld);
                }
            } else if (childOld.isLeaf()) {
                insertedNode(childNew);
            } else {
                findChanges(childNew, childOld);
            }
        } else {
            if (childNew.getNodeType() == childOld.getNodeType() && findDistance(childNew, childOld) <= 3) {
                modifiedNode(childNew, childOld);
            } else {
                handleMismatchedNodes(childNew, childOld);
            }

        }

    }

    /**
     * Here we deal with the case where two nodes are mismatched.
     * This can be due to the insertion or deletion of a node in the
     * new source. We make an attempt to match them back up here.
     *
     * @param childNew The current version of the node being diffed.
     * @param childOld The original version of the node being diffed.
     */
    public void handleMismatchedNodes(Node childNew, Node childOld) {

        List<Node> newSiblings = childNew.getParent().childNodes();
        List<Node> oldSiblings = childOld.getParent().childNodes();
        boolean nmatch = false;
        boolean omatch = false;

        for (Node sibling : newSiblings) {
            if (sibling.isSame(childOld)) {
                modifiedNode(sibling, childOld);
                findChanges(sibling, childOld);
                omatch = true;
                break;
            }
        }

        for (Node sibling : oldSiblings) {
            if (sibling.isSame(childNew)) {
                modifiedNode(childNew, sibling);
                findChanges(childNew, sibling);
                nmatch = true;
                break;
            }
        }

        if (nmatch == false || omatch == false) {
            insertedNode(childNew);
            deletedNode(childOld);
        }

    }

    public void insertedNode(Node node) {
        diffNodes.add(new Change(node, calcComplexity(node), null, 0));
    }

    public void deletedNode(Node node) {
        diffNodes.add(new Change(null, 0, node, calcComplexity(node)));
    }

    public void modifiedNode(Node newNode, Node oldNode) {
        diffNodes.add(new Change(newNode, calcComplexity(newNode), oldNode, calcComplexity(oldNode)));
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
            childNew = stripOutNewlines(childNew);

            if (oldChildren.hasNext()) {
                Node childOld = oldChildren.next();
                childOld = stripOutNewlines(childOld);

                if (!(isJunk == null)) {

                        // We got an isJunk object from the user and have to check each node against that.
                        if (!isJunk.checkJunk(childNew) && !isJunk.checkJunk(childOld)) {
                            sortNodesIntoDiff(childNew, childOld);
                        } else {
                            findChanges(childNew, childOld);
                        }

                    } else {
                        // We don't have to check for Junk so we can start sorting right away.
                        sortNodesIntoDiff(childNew, childOld);
                    }

            } else {
                // We have a node left dangling, so we'll insert it into the diff, then check to see if it matches
                // anything later.
                insertedNode(childNew);
            }

        }
    }

    /**
     * Newlines throw off the diffing algorithm's so we skip those.
     *
     * @param node node is a Node that needs checked to see if it is a Newline.
     * @return returns a Node.
     */
    public Node stripOutNewlines(Node node) {
        if (node.getNodeType() == NodeType.NEWLINENODE) {
            return ((NewlineNode) node).getNextNode();
        } else {
            return node;
        }
    }

    /**
     * If a node has been moved, our initial diff will contain two changes:
     * one insertion and one deletion. Here we match those up, then send their children
     * back in to check for changes internal to the moved node. This is how we can handle
     * a method which has been both moved and modified, something impossible for most text-based
     * diffs.
     */
    public void checkDiffForMoves() {
        ArrayList<Change> diffClone = (ArrayList) diffNodes.clone();

        oldTest:
            for (Change change : diffClone) {
                if (change.getOldNode() == null) continue oldTest;
                Node oldNode = change.getOldNode();

                Iterator<Change> newn = diffClone.iterator();
                newTest:
                    while (newn.hasNext()) {
                        Change newChange = newn.next();
                        if (newChange.getNewNode() == null) continue newTest;
                        Node newNode = newChange.getNewNode();

                        // We want to make sure that we aren't just finding an already matched up change.

                        if (diffNodes.indexOf(change) != diffNodes.indexOf(newChange) && oldNode.isSame(newNode)) {
                            diffNodes.set(diffNodes.indexOf(change), new Change(newNode, calcComplexity(newNode), oldNode, calcComplexity(oldNode)));
                            diffNodes.remove(newChange);
                            findChanges(newNode, oldNode);
                            checkDiffForMoves();
                        }
                    }
            }
    }

    public ArrayList<Change> getDiffNodes() {
        if (diffNodes.isEmpty()) {
            diffNodes  = new ArrayList<>();
            findChanges(getNewNode(), getOldNode());
            checkDiffForMoves();
        }
        return diffNodes;
    }

    /**
     * Returns an integer representing how many levels of nesting of children the
     * node has.
     *
     * @return Returns an int
     */
    public int calcComplexity(Node node) {
        if (node.isLeaf()) return 1;

        int complexity = 1;

        for (Node child : node.childNodes()) {
            complexity = complexity + calcComplexity(child);
        }

        return complexity;
    }

}

