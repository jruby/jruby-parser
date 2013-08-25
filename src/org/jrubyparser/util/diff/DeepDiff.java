package org.jrubyparser.util.diff;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 8/23/13
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * The DeepDiff class attaches the subnode-diff (the result of diffing the children of Nodes
 * which have already been diffed) to the Change object holding the nodes that the subnodes are contained within.
 */
public class DeepDiff extends Change {

    private ArrayList<Change> subdiff;

    public DeepDiff(Change change, ArrayList<Change> subdiff) {
        super(change.getNewNode(), change.getNewCost(), change.getOldNode(), change.getOldCost());
        if (subdiff != null) {
         this.subdiff = subdiff;
        }
    }

    public ArrayList<Change> getSubdiff() {
        return subdiff;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(150);

        if (getSubdiff() != null) {
            builder.append(super.toString()).append("\n").append("Subdiff: [ ").append(getSubdiff()).append(" ]\n");
        } else {
            return super.toString();
        }
        return builder.toString();
    }
}
