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

import org.jrubyparser.IRubyWarnings.ID;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.MultipleAsgnNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.StrNode;
import org.jrubyparser.lexer.Token;

/**
 * Ruby 2.4 parser support. Extends ParserSupport19 with:
 * - multiple assignment in conditionals allowed (warning, not error)
 * - squiggly heredoc ({@code <<~}) indentation stripping
 */
public class ParserSupport24 extends ParserSupport19 {

    @Override
    protected boolean checkAssignmentInCondition(Node node) {
        if (node instanceof MultipleAsgnNode) {
            warnings.warn(ID.ASSIGNMENT_IN_CONDITIONAL, node.getPosition(),
                    "Multiple assignment in conditional.");
            return true;
        }
        return super.checkAssignmentInCondition(node);
    }

    public boolean isDedentingHeredoc(Token token) {
        Object value = token == null ? null : token.getValue();
        return value instanceof String && ((String) value).startsWith("<<~");
    }

    public Node dedentHeredoc(Node node) {
        if (node == null) return null;

        HeredocIndentState indentState = new HeredocIndentState();
        collectHeredocIndent(node, indentState);

        if (!indentState.hasIndentedContent() || indentState.getIndent() <= 0) return node;

        applyHeredocDedent(node, new HeredocDedentState(indentState.getIndent()));
        return node;
    }

    private void collectHeredocIndent(Node node, HeredocIndentState state) {
        if (node == null) return;

        if (node instanceof StrNode) {
            collectHeredocIndent(((StrNode) node).getValue(), state);
            return;
        }

        if (node instanceof ListNode) {
            ListNode listNode = (ListNode) node;
            for (int i = 0; i < listNode.size(); i++) {
                collectHeredocIndent(listNode.get(i), state);
            }
            return;
        }

        state.markDynamicContent();
    }

    private void collectHeredocIndent(String value, HeredocIndentState state) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);

            if (state.atLineStart()) {
                if (c == ' ' || c == '\t') {
                    state.incrementCurrentIndent();
                    continue;
                }

                if (c == '\n') {
                    state.resetLine();
                    continue;
                }

                state.markContent();
            }

            if (c == '\n') {
                state.resetLine();
            }
        }
    }

    private void applyHeredocDedent(Node node, HeredocDedentState state) {
        if (node == null) return;

        if (node instanceof StrNode) {
            StrNode strNode = (StrNode) node;
            strNode.setValue(applyHeredocDedent(strNode.getValue(), state));
            return;
        }

        if (node instanceof ListNode) {
            ListNode listNode = (ListNode) node;
            for (int i = 0; i < listNode.size(); i++) {
                applyHeredocDedent(listNode.get(i), state);
            }
            return;
        }

        state.markDynamicContent();
    }

    private String applyHeredocDedent(String value, HeredocDedentState state) {
        StringBuilder builder = new StringBuilder(value.length());

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);

            if (state.atLineStart()) {
                if ((c == ' ' || c == '\t') && state.hasRemainingIndent()) {
                    state.consumeIndent();
                    continue;
                }

                if (c != '\n') state.markContent();
            }

            builder.append(c);

            if (c == '\n') {
                state.resetLine();
            }
        }

        return builder.toString();
    }

    private static final class HeredocIndentState {
        private int indent = Integer.MAX_VALUE;
        private int currentIndent = 0;
        private boolean lineStart = true;
        private boolean foundContent;

        boolean atLineStart() { return lineStart; }

        void incrementCurrentIndent() { currentIndent++; }

        void markContent() {
            indent = Math.min(indent, currentIndent);
            foundContent = true;
            lineStart = false;
        }

        void markDynamicContent() { if (lineStart) markContent(); }

        void resetLine() { currentIndent = 0; lineStart = true; }

        boolean hasIndentedContent() { return foundContent && indent != Integer.MAX_VALUE; }

        int getIndent() { return hasIndentedContent() ? indent : 0; }
    }

    private static final class HeredocDedentState {
        private final int indent;
        private int remaining;
        private boolean lineStart = true;

        HeredocDedentState(int indent) { this.indent = indent; this.remaining = indent; }

        boolean atLineStart() { return lineStart; }

        boolean hasRemainingIndent() { return remaining > 0; }

        void consumeIndent() { remaining--; }

        void markContent() { lineStart = false; remaining = 0; }

        void markDynamicContent() { if (lineStart) markContent(); }

        void resetLine() { lineStart = true; remaining = indent; }
    }
}
