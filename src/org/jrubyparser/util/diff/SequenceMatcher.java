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

import org.jrubyparser.ast.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * The SequenceMatcher class is used to produce a list of matching nodes.
 * It does most of the work in the diffing process.
 * @see NodeDiff
 */
public class SequenceMatcher
{
    protected IsJunk isJunk;
    protected Node newNode;
    protected Node oldNode;
    protected List<Change> diffNodes;

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
     * <code>SequenceMatcher</code> compares two nodes for matching nodes.
     *<p>
     * <code>isJunk</code> is an object which implements the {@link IsJunk}
     * interface and the <code>#checkJunk()</code> method.
     * <code>checkJunk</code> is a method which takes a Node and determines
     * whether or not it should be compared against the other node.
     *<p>
     * We pass in two nodes. Later, we can use the <code>#setSequence</code>,
     * <code>#setNewNode</code> and <code>#setOldNode</code> methods to change
     * out one or both of the nodes and create a new set of matches.
     *
     * @param isJunk A callback used to let users choose nodes not to be
     * checked in diff.
     * @param newNode The current version of the node.
     * @param oldNode The node in the old version.
     *
     * @see IsJunk
     */
    public SequenceMatcher(Node newNode, Node oldNode, IsJunk isJunk) {
        this.isJunk = isJunk;
        this.diffNodes  = new ArrayList<Change>();
        setSequences(newNode, oldNode);

    }

    public final void setSequences(Node newNode, Node oldNode) {
        setNewNode(newNode);
        setOldNode(oldNode);
    }

    public void setNewNode(Node newNode) {
        this.newNode = newNode;
        
        diffNodes.clear();
    }

    public void setOldNode(Node oldNode) {
        this.oldNode = oldNode;
        
        diffNodes.clear();
    }

    public Node getNewNode() {
        return this.newNode;
    }

    public Node getOldNode() {
        return this.oldNode;
    }

    /**
     * Decides what to do with nodes being diffed.
     *
     * @param childNew The current version of the node being diffed.
     * @param childOld The original version of the node being diffed.
     */
    protected void sortNodesIntoDiff(Node childNew, Node childOld) {
        if (childNew.isSame(childOld)) {
            if (childNew.isLeaf()) {
                if (childOld.isLeaf()) return;

                deletedNode(childOld);
            } else if (childOld.isLeaf()) {
                insertedNode(childNew);
            } else {
                findChanges(childNew, childOld);
            }
        } else {
            handleMismatchedNodes(childNew, childOld);
        }
    }


    /**
     * Here we deal with the case where two nodes are mismatched.
     * This can be due to the insertion or deletion of a node in the
     * new source. We make an initial attempt to match them back up
     * here.
     *
     * @param childNew The current version of the node being diffed.
     * @param childOld The original version of the node being diffed.
     */
    protected void handleMismatchedNodes(Node childNew, Node childOld) {
        List<Node> oldSiblings = childOld.getParent().childNodes();
        boolean nmatch = false;
        boolean omatch = false;

        for (Node sibling : childNew.getParent().childNodes()) {
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

    /**
     * A Node was inserted into the new AST.
     * <p>
     * It is possible that it was just mismatched, but we will sort that out
     * later.
     *
     * @param node recieves a Node.
     */
    protected void insertedNode(Node node) {
        diffNodes.add(new Change(node, calcComplexity(node), null, 0));
    }

    /**
     * A Node in the old AST is no longer present in the new one.
     * <p>
     * We assume it was deleted, but we will check for mismatches later.
     *
     * @param node Receives a Node.
     */
    protected void deletedNode(Node node) {
        diffNodes.add(new Change(null, 0, node, calcComplexity(node)));
    }

    /**
     * The node in the new AST has been changed.
     *
     * @param newNode The Node from the new AST which was modified.
     * @param oldNode The original version of the Node, from the old AST.
     */
    protected void modifiedNode(Node newNode, Node oldNode) {
        diffNodes.add(new Change(newNode, calcComplexity(newNode), oldNode, calcComplexity(oldNode)));
    }

    /**
     * This method does the diffing by iterating through the nodes and
     * calling itself recursively.
     *
     * @param newNode The node in the new version.
     * @param oldNode The node in the old version.
     */
    protected void findChanges(Node newNode, Node oldNode) {
        if (oldNode == null || newNode == null) return;

        Iterator<Node> oldChildren = oldNode.childNodes().iterator();

        loopForNewNode:
        for (Node childNew : newNode.childNodes()) {
            childNew = stripOutNewlines(childNew);
            if (checkForJunk(childNew)) continue loopForNewNode;

            if (oldChildren.hasNext()) {
                Node childOld = stripOutNewlines(oldChildren.next());

                // Because we aren't in a loop with the oldChildren, we can't just continue
                // the next turn of the loop to skip junk after we check for it.
                while (checkForJunk(childOld)) {
                    if (!oldChildren.hasNext()) continue loopForNewNode;

                    childOld = stripOutNewlines(oldChildren.next());
                }

                if (childNew.getNodeType() == NodeType.BLOCKNODE || childOld.getNodeType() == NodeType.BLOCKNODE) {
                    handleBlockNodes(childNew, childOld);
                    continue;
                }

                // We have both nodes, and they are not junk, or of type BLOCKNODE.
                sortNodesIntoDiff(childNew, childOld);
            } else {

                // We have a node (from newChildren) left dangling, so we'll insert it
                // into the diff, then check to see if it matches anything later.
                insertedNode(childNew);
            }

        }
    }

    /**
     * BlockNodes are problematic because they are often mismatched in
     * source code where nodes have been moved around. However, they
     * can't just be skipped like the NewLineNode, because they often
     * have more than one child. This is how we deal with them.
     *
     * @param childNew The current version of the node.
     * @param childOld The original version of the node.
     */
    protected void handleBlockNodes(Node childNew, Node childOld) {
        if (childNew.getNodeType() == NodeType.BLOCKNODE) {
            if (childOld.getNodeType() == NodeType.BLOCKNODE) {
                findChanges(childNew, childOld);
            } else {
                findChanges(childNew, childOld.getParent());
            }
        } else if (childOld.getNodeType() == NodeType.BLOCKNODE) {
            findChanges(childNew.getParent(), childOld);
        }
    }


    /**
     * If an isJunk object provided, check the passed in node
     * to see if it should be skipped.
     *
     * @param node A Node to be checked against provided callback.
     * @return Returns a boolean indicating whether or not Node
     *         should be skipped.
     */
    protected boolean checkForJunk(Node node) {
        // No matter what isJunk.checkJunk is, we have to deal with BlockNodes specially
        return node.getNodeType() != NodeType.BLOCKNODE && isJunk != null ? isJunk.checkJunk(node) : false;
    }

    /**
     * Newlines throw off the diffing algorithm's so we skip those.
     *
     * @param node node is a Node that needs checked to see if it is a Newline.
     * @return returns a Node.
     */
    protected Node stripOutNewlines(Node node) {
        return node.getNodeType() == NodeType.NEWLINENODE ? ((NewlineNode) node).getNextNode() : node;
    }

    /**
     * If a node has been moved, our initial diff will contain two changes:
     * one insertion and one deletion. Here we match those up, then send their
     * children back in to check for changes internal to the moved node. This
     * is how we can handle a method which has been both moved and modified,
     * something impossible for most text-based diff tools.
     */
    protected void checkDiffForMoves() {
        List<Change> diffClone = new ArrayList<Change>(diffNodes);

        oldTest:
            for (Change change : diffClone) {
                if (change.getOldNode() == null) continue oldTest;
                Node oNode = change.getOldNode();

                Iterator<Change> newn = diffClone.iterator();
                newTest:
                    while (newn.hasNext()) {
                        Change newChange = newn.next();
                        if (newChange.getNewNode() == null) continue newTest;
                        Node nNode = newChange.getNewNode();

                        // We want to make sure that we aren't just finding an already matched up change.

                        if (diffNodes.indexOf(change) != diffNodes.indexOf(newChange)) {
                            if (oNode.isSame(nNode)) {
                                if (diffNodes.contains(change)) {
                                    diffNodes.set(diffNodes.indexOf(change), new Change(nNode, calcComplexity(nNode), oNode, calcComplexity(oNode)));
                                    diffNodes.remove(newChange);
                                    findChanges(nNode, oNode);
                                    checkDiffForMoves();
                                }

                            }

                            // Since methods are such an essential unit in ruby code, we take extra care to match these back up
                            // This should match cases where the name has remained the same, but the node has moved, or its
                            // internal structure was altered, or both.

                            if (oNode.getNodeType() == NodeType.DEFNNODE && nNode.getNodeType() == NodeType.DEFNNODE) {
                                if (((MethodDefNode) oNode).isNameMatch(((MethodDefNode) nNode).getName())) {
                                    if (diffNodes.contains(change)) {
                                        diffNodes.set(diffNodes.indexOf(change), new Change(nNode, calcComplexity(nNode), oNode, calcComplexity(oNode)));
                                        diffNodes.remove(newChange);
                                        findChanges(nNode, oNode);
                                        checkDiffForMoves();
                                    }
                                }
                            }
                        }
                    }
            }
    }

    /**
     * If we already have a diff, return that, otherwise
     * create one.
     *
     * @return Returns an ArrayList of Change objects, the diff.
     */
    public List<Change> getDiffNodes() {
        if (diffNodes.isEmpty()) {
            diffNodes  = new ArrayList<Change>();
            findChanges(getNewNode(), getOldNode());
            checkDiffForMoves();
        }
        return diffNodes;
    }

    /**
     * Returns an integer representing how many levels of nesting of children
     * the node has.
     *
     * @param node to calculate complexity on
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

