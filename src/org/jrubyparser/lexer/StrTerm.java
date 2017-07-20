/***** BEGIN LICENSE BLOCK *****
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

public abstract class StrTerm {
    public abstract int parseString(Lexer lexer, LexerSource src) throws java.io.IOException;

    /** Tell this string term to return separate tokens for embedded ruby code (#$foo, #@foo, #{foo}) */
    public abstract void splitEmbeddedTokens();

    /**
     * Report whether this string should be substituting things like \n into newlines (double
     * quoting rules).
     * E.g. are we dealing with a "" string or a '' string (or their alternate representations)
     * @return true if substitution is happening
     */
    public abstract boolean isSubstituting();

    // When StringTerm processes a string with an embedded code fragment (or variable),
    // such as #{thiscode()}, it splits the string up at the beginning of the boundary
    // and returns Tokens.tSTRING_DBEG or Tokens.tSTRING_DVAR. However, it doesn't
    // split the string up where the embedded code ends, it just processes to the end.
    // For my lexing purposes that's not good enough; I want to know where the embedded
    // fragment ends (so I can lex that String as real Ruby code rather than just
    // a String literal).
    // However,
    /** Default; ignore embedded fragments */
    final static int IGNORE_EMBEDDED = 0;
    /** Flag set in embeddedCode when we are processing an embedded code expression: #{foo} */
    final static int LOOKING_FOR_EMBEDDED = 1;
    /** Flag set in embeddedCode when we are processing an embedded code expression: #{foo} */
    final static int EMBEDDED_DEXPR = 2;
    /** Flag set in embeddedCode when we are processing an embedded variable: #@foo */
    final static int EMBEDDED_DVAR = 3;
    /** Flag set while we're processing embedded Ruby expressions. It will be 0 when we are not,
     * or otherwise set to the the relevant embedded type (EMBEDDED_DVAR or EMBEDDED_DEXPR) */
    protected int processingEmbedded;
    /**
     * Record any mutable state from this StrTerm such that it can
     * be set back to this exact state through a call to {@link #setMutableState}
     * later on. Necessary for incremental lexing where we may restart
     * lexing parts of a string (since they can be split up due to
     * Ruby embedding like "Evaluated by Ruby: #{foo}".
     * @return mutable state
     */
    public abstract Object getMutableState();
    /** Support for incremental lexing: set current state of the term. See {@link #getMutableState} 
     * @param o the object to set
     */
    public abstract void setMutableState(Object o);
}
