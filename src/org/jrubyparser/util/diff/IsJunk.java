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

/**
 * Implementations of the interface IsJunk and the
 * <code>#checkJunk()</code> method can be passed to {@link NodeDiff}
 * and {@link SequenceMatcher} as callbacks which will then be used
 * to select Nodes to be skipped over during the diffing process.
 * <p>
 *
 * More specifically, if an object implementing <code>IsJunk</code> was
 * passed in as the last parameter to the constructor of
 * <code>SequenceMatcher</code>, then during an early phase of each iteration
 * of the diff, <code>SequenceMatcher#findChanges</code> will call
 * <code>SequenceMatcher#checkForJunk</code> on each Node which, in turn,
 * calls <code>#checkJunk()</code> for both Nodes. If
 * <code>#checkJunk()</code> returns true, that node
 * will be skipped.
 * <p>
 *
 * Nodes of type <code>NewLineNode</code> and <code>BlockNode</code> are
 * automatically skipped over by <code>SequenceMatcher</code>, so it is
 * unnecessary to check for either of these.
 * <p>
 *
 * Example:
 * <pre>
 * {@code
 *
 * // Skipping all nodes which are methods
 * public class MyJunkChecker implements IsJunk {
 *     public boolean checkJunk(Node node) {
 *         if (node instanceof MethodDefNode) {
 *             return true;
 *         }
 *         return false;
 *     }
 * }
 * }
 * </pre>
 *
 * @see NodeDiff
 * @see SequenceMatcher
 * @see SequenceMatcher#findChanges(org.jrubyparser.ast.Node, org.jrubyparser.ast.Node)
 * @see SequenceMatcher#checkForJunk(org.jrubyparser.ast.Node)
 *
 */
public interface IsJunk
{
    /**
     * Any node passed to an implementation of <code>#checkJunk()</code>
     * which returns true will not be diffed. Any Node for which
     * <code>#checkJunk()</code> returns true will skip that node and all
     * of that node's children. For example, if checkJunk returns true
     * on all instances of <code>MethodDefNode</code>, the diff will skip
     * all methods entirely, contents included.
     * <p>
     *
     * Nodes of type <code>NewLineNode</code> and <code>BlockNode</code>
     * are automatically skipped, so it is not necessary to check for them
     * here.
     *
     * @param node The Node being checked. If it returns true, this node will
     *             not be diffed.
     *
     * @return  Returns true or false. Any node for which it returns true will
     *          not be diffed.
     */
    public boolean checkJunk(Node node);
}
