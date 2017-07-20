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

import org.jrubyparser.ast.ILocalScope;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.RootNode;
import org.jrubyparser.rewriter.ReWriteVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The <code>NodeDiff</code> class takes two <code>Node</code> objects, and
 * via the <code>SequenceMatcher</code> class, determines the differences
 * between the two objects and creates a diff, which is a List of
 * <code>Change</code> objects containing a pair of Nodes, the original and
 * its replacement (or a single Node, representing an insertion
 * or deletion, depending on whether its in the new or old AST) as well as
 * other relevant information about the difference.
 * <p>
 *
 * Additionally, if given the String each Node was parsed from,
 * <code>NodeDiff</code> can create a <code>DeepDiff</code>. The
 * <code>DeepDiff</code> will contain all the information of the original
 * diff, but it will also create subdiffs of certain types of Nodes such as
 * methods and classes. By using a Levenshtein distance algorithm, which would
 * be too imprecise when comparing the entire diff, but works within the
 * smaller context, the subdiff is able to re-match changes that were
 * previously disconnected and recognized as a separate deletion and
 * insertion. Careful use of the subdiff information can be used to
 * dramatically improve diffing results.
 *<p>
 *
 * Finally, by passing in a callback, <code>isJunk</code> , which implements
 * the <code>#IsJunk</code> interface, and specifically, the <code>#checkJunk()
 * </code> method, one can instruct <code>NodeDiff</code> to ignore particular
 * Nodes, or types of Nodes.
 * <p>
 *
 * Example:
 * <pre>
 * {@code
 *
 * String codeStringA = "def foo(bar)\n bar\n end\n foo('astring')";
 * String codeStringB = "'a'\ndef bloo(bar)\n puts bar\n end\n foo('astring')";
 *
 * //The implementation of parseContents is left to the reader.
 * Node nodeA = parseContents(codeStringA);
 * Node nodeB = parseContents(codeStringB);
 *
 * NodeDiff nd = new NodeDiff(nodeA, codeStringA, nodeB, codeStringB, new
 * IsJunk() {
 *   public boolean checkJunk(Node node) {
 *       if (!(node instanceof ILocalScope && !(node instanceof RootNode))) {
 *           return true;
 *       }
 *         return false;
 *     }
 * });
 *
 * ArrayList<DeepDiff> diff = nd.getDeepDiff();
 *
 * System.out.println(diff);
 * // Output:
 * //[
 * // Change:
 * // New Node: (DefnNode:foo, (MethodNameNode:foo), (ArgsNode, (ListNode, (
 * // ArgumentNode:bar))), (NewlineNode, (LocalVarNode:bar))) Complexity: 7
 * // Position: <code>:[0,2]:[0,22]
 * //,
 * // Change:
 * // Old Node: (DefnNode:bloo, (MethodNameNode:bloo), (ArgsNode, (ListNode, (
 * // ArgumentNode:bar))), (NewlineNode, (FCallNode:puts, (ArrayNode, (
 * // LocalVarNode:bar))))) Complexity: 9 Position: <code>:[1,3]:[4,32]
 * //]
 * }
 * </pre>
 *
 * @see #getDeepDiff()
 * @see #getSubdiff(Change)
 * @see #NodeDiff(org.jrubyparser.ast.Node, org.jrubyparser.ast.Node, IsJunk)
 * @see #NodeDiff(org.jrubyparser.ast.Node, String, org.jrubyparser.ast.Node,
 * String, IsJunk)
 * @see SequenceMatcher
 * @see Change
 * @see DeepDiff
 * @see IsJunk
 * @see IsJunk#checkJunk(org.jrubyparser.ast.Node)
 */
public class NodeDiff
{
    protected List<Change> diff = new ArrayList<Change>();
    protected List<DeepDiff> deepdiff = new ArrayList<DeepDiff>();
    protected SequenceMatcher SequenceMatch;
    protected String oldDocument;
    protected String newDocument;
    protected Node newNode;
    protected Node oldNode;
    protected IsJunk isJunk;

    /**
     * Create a <code>NodeDiff</code> object without passing in the Strings
     * that the nodes were parsed from. You can create a normal diff from
     * this. However, to create a DeepDiff (with subdiffs) it will be
     * necessary to set both oldDocument and newDocument.
     *
     * @param newNode The current version of the node being diffed.
     * @param oldNode The original version of the node being diffed.
     */
    public NodeDiff(Node newNode, Node oldNode) {
        this(newNode, oldNode, null);
    }

    /**
     * Create a NodeDiff object without passing in the Strings that the nodes
     * were parsed from. You can create a normal diff from this. However,
     * before creating a DeepDiff (with subdiffs) it will be necessary to set
     * both oldDocument and newDocument.
     *
     * <p>
     * Passing in isJunk allows you to customize the diffs by choosing to
     * ignore specific Nodes or types of Nodes. isJunk is an object which
     * implements the {@link IsJunk} interface and the #checkJunk() method.
     * checkJunk is a method which takes a Node and determines whether or not
     * it should be compared in the diff or skipped. If isJunk is non-null,
     * isJunk#checkJunk() will be called at each pass through
     * {@link SequenceMatcher#findChanges(org.jrubyparser.ast.Node,
     * org.jrubyparser.ast.Node)}.
     * {@link org.jrubyparser.ast.NewlineNode} and {@link org.jrubyparser.ast.BlockNode}
     * nodetypes are ignored automatically.
     *
     * @param newNode The current version of the node being diffed.
     * @param oldNode The original version of the node being diffed.
     * @param isJunk A callback used to let users choose nodes not to be checked in diff.
     */
    public NodeDiff(Node newNode, Node oldNode, IsJunk isJunk) {
        this.newNode = newNode;
        this.oldNode = oldNode;
        this.isJunk = isJunk;
    }


    /**
     * Create a NodeDiff object by passing in both the Nodes to be diffed as
     * well as the Strings they were parsed from. The object constructed can
     * perform both a diff of its nodes as well as a DeepDiff (subdiff of
     * particular nodes).
     *
     * @param newNode The current version of the node being diffed.
     * @param newDocument The String that newNode was parsed from.
     * @param oldNode The original version of the node being diffed.
     * @param oldDocument The String that oldNode was parsed from.
     */
    public NodeDiff(Node newNode, String newDocument, Node oldNode, String oldDocument) {
        this(newNode, newDocument, oldNode, oldDocument, null);
    }

    /**
     * Create a NodeDiff object by passing in both the Nodes to be diffed as
     * well as the Strings they were parsed from. The object constructed can
     * perform both a diff of its nodes as well as a DeepDiff (subdiff of
     * particular nodes).
     *
     * <p>
     * Passing in isJunk allows you to customize the diffs by choosing to
     * ignore specific Nodes or types of Nodes. <code>isJunk</code> is an
     * object which implements the {@link IsJunk} interface and the
     * <code>#checkJunk()</code> method. checkJunk is a method which takes a
     * Node and determines whether or not it should be compared against the
     * other node. If isJunk is non-null, <code>isJunk#checkJunk()</code> will
     * be called at each pass through
     * {@link SequenceMatcher#findChanges(org.jrubyparser.ast.Node, org.jrubyparser.ast.Node)}.
     * {@link org.jrubyparser.ast.NewlineNode} and {@link org.jrubyparser.ast.BlockNode}
     * nodetypes are ignored automatically.
     *
     * @param newNode The current version of the node (AST) being diffed.
     * @param newDocument The String that newNode was parsed from.
     * @param oldNode The original version of the node (AST) being diffed.
     * @param oldDocument The String that oldNode was parsed from.
     * @param isJunk A callback used to let users choose nodes not to be checked in diff.
     */
    public NodeDiff(Node newNode, String newDocument, Node oldNode, String oldDocument, IsJunk isJunk) {
        this.newNode = newNode;
        this.newDocument = newDocument;
        this.oldNode = oldNode;
        this.oldDocument = oldDocument;
        this.isJunk = isJunk;

    }

    public void setOldDocument(String oldDocument) {
        this.oldDocument = oldDocument;
    }

    public void setNewDocument(String newDocument) {
        this.newDocument = newDocument;
    }

    /**
     * Fetch or create a diff of the Nodes newNode and oldNode. This is
     * done via {@link SequenceMatcher}.
     *
     * @return Returns an ArrayList of {@link Change} objects representing the
     * diff.
     */
    public List<Change> getDiff() {
        if (diff.isEmpty()) diff = createDiff(new SequenceMatcher(newNode, oldNode, isJunk));

        return diff;
    }

    /**
     * Fetch or create a deep diff  of the Nodes newNode and oldNode. The
     * ArrayList of {@link DeepDiff} objects returned will contain both
     * primary diff information ({@link Change} objects) as well as
     * subdiffs of some of those changes.
     *
     * <p>
     * Because it uses the Strings which the nodes were parsed from for
     * matching purposes, if these have not been set (either at object
     * construction or via #setOldDocument and #setNewDocument) it will throw a
     * NullPointerException.
     *
     * @return Returns an ArrayList of {@link DeepDiff} objects representing
     * the diff and subdiff.
     *
     * @throws NullPointerException if it is null
     *
     * @see #getSubdiff(Change)
     * @see Change
     * @see DeepDiff
     */
    public List<DeepDiff> getDeepDiff() {
        if (deepdiff.isEmpty()) {
            // FIXME: This should be a more useful error
            // oldDocument and newDocument must be set to create a DeepDiff.
            // They are necessary for the #distance() method that #sortSubdiff() uses.
            if (oldDocument == null || newDocument == null) throw new NullPointerException();
                
            deepdiff = createDeepDiff(createDiff(new SequenceMatcher(newNode, oldNode, isJunk)));
        }
        
        return deepdiff;
    }

    protected List<Change> createDiff(SequenceMatcher sequenceMatch) {
        return sequenceMatch.getDiffNodes();
    }

    protected List<DeepDiff> createDeepDiff(List<Change> roughDiff) {
        List<DeepDiff> complexDiff = new ArrayList<DeepDiff>();

        for (Change change: roughDiff) {
           complexDiff.add(new DeepDiff(change, getSubdiff(change)));
        }
        
        return complexDiff;
    }

    /**
     * Sorts through a diff, checking for specific, important types of Nodes
     * like classes, methods, etc and performs subdiffs on those. It calls
     * {@link #sortSubdiff(java.util.List)} for matching nodes from the
     * original and current version, which uses a Levenshtein distance
     * measurement for this purpose. The subdiff, since it is comparing a much
     * smaller set of potential matches, can be more optimistic than the
     * matching which occurs for an ordinary diff. Careful usage of the
     * subdiff information can dramatically improve the diff results.
     *
     * @param change The Change object being subdiffed.
     * @return Returns an ArrayList of Change objects. Essentially a diff.
     */
     protected List<Change> getSubdiff(Change change) {
        Node oNode = change.getOldNode();
        Node nNode = change.getNewNode();

        // Besides checking nulls, we save a lot of time here by only subdiffing a subset of possible nodes

        if ((oNode == null || nNode == null) || !(oNode instanceof ILocalScope && !(oNode instanceof RootNode))) return null;

        return sortSubdiff(new SequenceMatcher(nNode, oNode).getDiffNodes());
     }

    /**
     * We sort through subdiffs, trying to match up insertions with deletions.
     * While the diff is weighted to avoid false positives, given that the
     * subdiff has a much smaller number of nodes to be compared against, we
     * can be a bit more liberal, though the possibility of false positives
     * does exist, it is far less critical if one does occur.
     *
     * @param subdiff An ArrayList which is a diff of the nodes in a Change
     * object.
     * @return Returns an ArrayList this is the subdiff object, after sorting.
     */
    protected List<Change> sortSubdiff(List<Change> subdiff) {
        List<Change> diffClone = new ArrayList<Change>(subdiff);
        
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
                if (subdiff.indexOf(change) != subdiff.indexOf(newChange) ) {
                    String nstring = ReWriteVisitor.createCodeFromNode(nNode, newDocument);
                    String ostring = ReWriteVisitor.createCodeFromNode(oNode, oldDocument);

                    int lfd = distance(nstring, ostring);
                    double ratio = ((double) lfd) / (Math.max(nstring.length(), ostring.length()));

                    // This ratio may need to be tweaked.
                    if (ratio < 0.2 && subdiff.contains(change)) {
                        SequenceMatcher sm = new SequenceMatcher(null, null, null);

                        subdiff.set(subdiff.indexOf(change), 
                                new Change(nNode, sm.calcComplexity(nNode), oNode, sm.calcComplexity(oNode)));
                        subdiff.remove(newChange);
                    }
                }
            }
        }
        
        return subdiff;
    }

    /**
     * Takes two strings and measures the Levenshtein distance between them.
     *
     * @param s1 First string to be considered. This is code from the new, or
     * current node (AST).
     * @param s2 Second string to be considered. This is code from the old, or
     * original node (AST).
     * @return Returns an int.
     */
    private static int distance(String s1, String s2){
        int edits[][]=new int[s1.length()+1][s2.length()+1];
        
        for(int i=0; i <= s1.length(); i++) {
            edits[i][0] = i;
        }
        
        for(int j=1; j <= s2.length(); j++) {
            edits[0][j] = j;
        }
        
        for(int i=1; i <= s1.length(); i++) {
            for(int j=1; j <= s2.length(); j++) {
                int u = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                edits[i][j] = Math.min(edits[i-1][j]+1,
                        Math.min(edits[i][j-1]+1, edits[i-1][j-1]+u));
            }
        }
        
        return edits[s1.length()][s2.length()];
    }

}
