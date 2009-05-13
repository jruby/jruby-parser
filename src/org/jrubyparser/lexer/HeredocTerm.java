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
 * Copyright (C) 2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2004-2007 Thomas E Enebo <enebo@acm.org>
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
package org.jrubyparser.lexer;

import org.jrubyparser.SourcePosition;
import org.jrubyparser.ast.StrNode;
import org.jrubyparser.lexer.SyntaxException.PID;
import org.jrubyparser.parser.Tokens;
import org.jrubyparser.util.CStringBuilder;

/**
 * A lexing unit for scanning a heredoc element.
 * Example:
 * <pre>
 * foo(<<EOS, bar)
 * This is heredoc country!
 * EOF
 * 
 * Where:
 * EOS = marker
 * ',bar)\n' = lastLine
 * </pre>
 *  
 */
public class HeredocTerm extends StrTerm {
    // Marker delimiting heredoc boundary
    private final String marker;

    // Expand variables, Indentation of final marker
    private final int flags;

    // Portion of line right after beginning marker.  In preserve spaces mode this is not used
    // since it parses in order (so it has no need to put lastLine back).
    private final String lastLine;
    
    public HeredocTerm(String marker, int func, String lastLine) {
        this.marker = marker;
        this.flags = func;
        this.lastLine = lastLine;
    }
    
    public int parseString(Lexer lexer, LexerSource src) throws java.io.IOException {
        boolean indent = (flags & Lexer.STR_FUNC_INDENT) != 0;

        if (src.peek(Lexer.EOF)) syntaxError(src);

        if (lexer.getPreserveSpaces()) {
            boolean done = src.matchMarker(marker, indent, true);
            if (done) {
                lexer.yaccValue = new StrNode(lexer.getPosition(), "");
                lexer.setStrTerm(new StringTerm(-1, '\0', '\0'));
                src.setIsANewLine(true);
                return Tokens.tSTRING_END;
            }
        }

        // Found end marker for this heredoc
        if (src.lastWasBeginOfLine() && src.matchMarker(marker, indent, true)) {
            SourcePosition position = lexer.getPosition();
            
            unreadLastLine(src); // push back last line to lex stuff after initial heredoc marker
            
            lexer.yaccValue = new Token(marker, position);
            return Tokens.tSTRING_END;
        }

        CStringBuilder str = new CStringBuilder();
        SourcePosition position;
        
        if ((flags & Lexer.STR_FUNC_EXPAND) == 0) {
            do {
                str.append(src.readLineBytes());
                str.append('\n');
                if (src.peek(Lexer.EOF)) syntaxError(src);
                position = lexer.getPosition();
            } while (!src.matchMarker(marker, indent, true));
        } else {
            int c = src.read();
            if (c == '#') {
                switch (c = src.read()) {
                case '$':
                case '@':
                    if (processingEmbedded == LOOKING_FOR_EMBEDDED) {
                        processingEmbedded = EMBEDDED_DVAR;
                    }
                    src.unread(c);
                    lexer.setValue(new Token("#" + c, lexer.getPosition()));
                    return Tokens.tSTRING_DVAR;
                case '{':
                    if (processingEmbedded == LOOKING_FOR_EMBEDDED) {
                        processingEmbedded = EMBEDDED_DEXPR;
                    }
                    lexer.setValue(new Token("#" + c, lexer.getPosition()));
                    return Tokens.tSTRING_DBEG;
                }
                str.append('#');
            }

            src.unread(c);

            // MRI has extra pointer which makes our code look a little bit more strange 
            // in comparison.
            do {
                StringTerm stringTerm = new StringTerm(flags, '\0', '\n');
                stringTerm.processingEmbedded = processingEmbedded;
                if ((c = stringTerm.parseStringIntoBuffer(lexer, src, str)) == Lexer.EOF) {
                     syntaxError(src);
                }

                // Completed expansion token
                if (processingEmbedded == EMBEDDED_DVAR || processingEmbedded == EMBEDDED_DEXPR) {
                    processingEmbedded = LOOKING_FOR_EMBEDDED;
                }

                if (c != '\n') {
                    lexer.yaccValue = new StrNode(lexer.getPosition(), str.toString());
                    return Tokens.tSTRING_CONTENT;
                }
                str.append(src.read());
                
                if (src.peek(Lexer.EOF)) syntaxError(src);
                position = lexer.getPosition();
            } while (!src.matchMarker(marker, indent, true));
        }

        // DVARs last only for a single string token so shut if off here.
        if (processingEmbedded == EMBEDDED_DVAR) processingEmbedded = LOOKING_FOR_EMBEDDED;

        unreadLastLine(src); // push back last line to lex stuff after initial heredoc marker

        // When handling heredocs in syntax highlighting mode, process the end marker separately
        if (lastLine == null) {
            src.unreadMany(marker+"\n"); // \r?
        } else {
            lexer.setStrTerm(new StringTerm(-1, '\0', '\0'));
        }

        lexer.yaccValue = new StrNode(position, str.toString());
        return Tokens.tSTRING_CONTENT;
    }
    
    private void syntaxError(LexerSource src) {
        throw new SyntaxException(PID.STRING_MARKER_MISSING, src.getPosition(), "can't find string \"" + marker
                + "\" anywhere before EOF", marker);
    }

    /**
     * Report whether this string should be substituting things like \n into newlines.
     * E.g. are we dealing with a "" string or a '' string (or their alternate representations)
     */
    public boolean isSubstituting() {
        return (flags & Lexer.STR_FUNC_EXPAND) != 0;
    }

    /**
     * Record any mutable state from this StrTerm such that it can
     * be set back to this exact state through a call to {@link setMutableState}
     * later on. Necessary for incremental lexing where we may restart
     * lexing parts of a string (since they can be split up due to
     * Ruby embedding like "Evaluated by Ruby: #{foo}".
     */
    public Object getMutableState() {
        return new MutableTermState(processingEmbedded);
    }

    /**
     * Apply the given state object (earlier returned by {@link getMutableState})
     * to this StringTerm to revert state to the earlier snapshot.
     */
    public void setMutableState(Object o) {
        MutableTermState state = (MutableTermState) o;

        if (state != null) processingEmbedded = state.processingEmbedded;
    }

    public void splitEmbeddedTokens() {
        if (processingEmbedded == IGNORE_EMBEDDED) processingEmbedded = LOOKING_FOR_EMBEDDED;
    }

    private void unreadLastLine(LexerSource src) {
        if (lastLine != null) src.unreadMany(lastLine);
    }

    private class MutableTermState {
        private MutableTermState(int embeddedCode) {
            this.processingEmbedded = embeddedCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null && getClass() != obj.getClass()) return false;

            final MutableTermState other = (MutableTermState) obj;

            return this.processingEmbedded == other.processingEmbedded;
        }

        @Override
        public int hashCode() {
            return 83 * 7 + this.processingEmbedded;
        }

        @Override
        public String toString() {
            return "HeredocTermState[" + processingEmbedded + "]";
        }

        private int processingEmbedded;
    }

    // Equals - primarily for unit testing (incremental lexing tests
    // where we do full-file-lexing and compare state to incremental lexing)
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;

        final HeredocTerm other = (HeredocTerm) obj;

        return (marker == other.marker || (marker != null && marker.equals(other.marker)) &&
               this.flags == other.flags &&
               (lastLine == other.lastLine || (lastLine != null && lastLine.equals(other.lastLine))));
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 83 * hash + (this.marker != null ? this.marker.hashCode() : 0);
        hash = 83 * hash + this.flags;
        hash = 83 * hash + (this.lastLine != null ? this.lastLine.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "HeredocTerm[" + flags + "," + marker + "," + lastLine + "," + processingEmbedded + "]";
    }
}
