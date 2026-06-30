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

/*
 * Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
 */
package org.jrubyparser.parser;

import org.jrubyparser.SourcePosition;
import org.jrubyparser.lexer.Token;
import org.jrubyparser.ast.ArrayNode;
import org.jrubyparser.ast.ArrayPatternNode;
import org.jrubyparser.ast.AssignableNode;
import org.jrubyparser.ast.CaseNode;
import org.jrubyparser.ast.HashNode;
import org.jrubyparser.ast.HashPatternNode;
import org.jrubyparser.ast.InNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.PatternBindNode;

/**
 * Ruby 2.7 parser support. Extends Ruby 2.6 support with 2.7 grammar behavior.
 */
public class ParserSupport27 extends ParserSupport26 {

    // ---- Ruby 2.7 pattern matching helpers ----

    public CaseNode newCaseInNode(SourcePosition position, Node expression, Node firstInNode) {
        ArrayNode cases = new ArrayNode(firstInNode != null ? firstInNode.getPosition() : position);
        CaseNode caseNode = new CaseNode(position, expression, cases);

        for (Node current = firstInNode; current != null;) {
            if (current instanceof InNode) {
                cases.add(current);
                current = ((InNode) current).getNextCase();
            } else {
                caseNode.setElseNode(current);
                break;
            }
        }

        return caseNode;
    }

    public InNode newInNode(SourcePosition position, Node expression, Node body, Node nextCase) {
        return new InNode(position, expression, body, nextCase);
    }

    public AssignableNode assignablePatternVariable(SourcePosition position, String name) {
        return currentScope.assign(position, name, null);
    }

    public Node patternVarRef(SourcePosition position, String name) {
        return currentScope.declare(position, name);
    }

    public ArrayPatternNode newArrayPatternTail(SourcePosition position, ListNode preArgs, boolean hasRest,
            Token restName, ListNode postArgs) {
        Node restArg = null;
        if (hasRest && restName != null) {
            restArg = assignablePatternVariable(restName.getPosition(), (String) restName.getValue());
        }
        return new ArrayPatternNode(position, preArgs, hasRest, restArg, postArgs);
    }

    public Node newArrayPattern(SourcePosition position, Node constant, Node preArg, ArrayPatternNode tail) {
        tail.setConstant(constant);
        if (preArg != null) {
            ListNode pre = tail.getPreArgs();
            if (pre == null) {
                pre = new ArrayNode(preArg.getPosition(), preArg);
            } else {
                ArrayNode newPre = new ArrayNode(preArg.getPosition(), preArg);
                for (Node n : pre.childNodes()) newPre.add(n);
                pre = newPre;
            }
            tail.setPreArgs(pre);
        }
        if (position != null) tail.setPosition(position);
        return tail;
    }

    public HashPatternNode newHashPatternTail(SourcePosition position, ListNode pairs, Token restName, boolean noRest) {
        HashNode hash = new HashNode(position, pairs == null ? new ArrayNode(position) : pairs);
        Node restArg = null;
        if (restName != null) {
            restArg = assignablePatternVariable(restName.getPosition(), (String) restName.getValue());
        }
        return new HashPatternNode(position, hash, restArg, noRest);
    }

    public Node newHashPattern(SourcePosition position, Node constant, HashPatternNode tail) {
        tail.setConstant(constant);
        if (position != null) tail.setPosition(position);
        return tail;
    }

    public PatternBindNode newPatternBind(SourcePosition position, Node pattern, Node target) {
        return new PatternBindNode(position, pattern, target);
    }
}
