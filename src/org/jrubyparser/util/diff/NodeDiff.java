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

import java.util.ArrayList;
import java.util.List;

/**
 * The NodeDiff class takes two Node objects, and via the SequenceMatcher class,
 * determines the differences between the two objects and creates a diff, which
 * is a List containing the Nodes which are different between the two.
 */
public class NodeDiff
{
    protected ArrayList<Change> diff = new ArrayList<Change>();
    protected ArrayList<DeepDiff> deepdiff = new ArrayList<DeepDiff>();
    protected SequenceMatcher SequenceMatch;
    Node newNode;
    Node oldNode;
    IsJunk isJunk;

    public NodeDiff(Node newNode, Node oldNode) {
        this(newNode, oldNode, null);
    }

    public NodeDiff(Node newNode, Node oldNode, IsJunk isJunk) {
        this.newNode = newNode;
        this.oldNode = oldNode;
        this.isJunk = isJunk;

    }

    public ArrayList<Change> getDiff() {
        if (diff.isEmpty()) {
        SequenceMatcher sm = new SequenceMatcher(newNode, oldNode, isJunk);
        diff = createDiff(sm);
        }
        return diff;
    }

    public ArrayList<DeepDiff> getDeepDiff() {
        if (deepdiff.isEmpty()) {
        SequenceMatcher sm = new SequenceMatcher(newNode, oldNode, isJunk);
        deepdiff = createDeepDiff(createDiff(sm));
        }
        return deepdiff;
    }

    public ArrayList<Change> createDiff(SequenceMatcher sequenceMatch) {
        ArrayList<Change> roughDiff = sequenceMatch.getDiffNodes();
        return roughDiff;
    }

    public ArrayList<DeepDiff> createDeepDiff(ArrayList<Change> roughDiff) {
        ArrayList<DeepDiff> complexDiff = new ArrayList<DeepDiff>();
        for (Change change: roughDiff) {
           ArrayList<Change> subdiff = getSubdiff(change);
           complexDiff.add(new DeepDiff(change, subdiff));
        }
        return complexDiff;
    }

     public ArrayList<Change> getSubdiff(Change change) {

        ArrayList<Change> subdiff = new ArrayList<Change>();

        Node oldNode = change.getOldNode();
        Node newNode = change.getNewNode();

        if ((oldNode == null || newNode == null) || !(oldNode instanceof ILocalScope && !(oldNode instanceof RootNode))) {
            return null;
        } else {
            SequenceMatcher smForSubDiff = new SequenceMatcher(newNode, oldNode);
            subdiff = smForSubDiff.getDiffNodes();
            return subdiff;
        }
     }

}
