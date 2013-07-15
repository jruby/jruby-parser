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
 * Copyright (C) 2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2002-2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2004-2009 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Stefan Matthias Aust <sma@3plus4.de>
 * Copyright (C) 2004-2005 David Corbin <dcorbin@users.sourceforge.net>
 * Copyright (C) 2005 Zach Dennis <zdennis@mktec.com>
 * Copyright (C) 2006 Thomas Corbat <tcorbat@hsr.ch>
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

import java.io.IOException;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jrubyparser.ast.BackRefNode;
import org.jrubyparser.ast.BignumNode;
import org.jrubyparser.ast.CommentNode;
import org.jrubyparser.ast.FixnumNode;
import org.jrubyparser.ast.FloatNode;
import org.jrubyparser.ast.NthRefNode;
import org.jrubyparser.ast.StrNode;
import org.jrubyparser.IRubyWarnings;
import org.jrubyparser.IRubyWarnings.ID;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.lexer.SyntaxException.PID;
import org.jrubyparser.parser.ParserSupport;
import org.jrubyparser.parser.Tokens;
import org.jrubyparser.util.CStringBuilder;


/**
 *  This is a port of the MRI lexer to Java it is compatible to Ruby 1.8.x and Ruby 1.9.x
 *  depending on compatibility flag.
 */
public class Lexer {
    private static String END_MARKER = "_END__";
    private static String BEGIN_DOC_MARKER = "begin";
    private static String END_DOC_MARKER = "end";
    private static HashMap<String, Keyword> map;
    
    static {
        map = new HashMap<String, Keyword>();
        
        map.put("end", Keyword.END);
        map.put("else", Keyword.ELSE);
        map.put("case", Keyword.CASE);
        map.put("ensure", Keyword.ENSURE);
        map.put("module", Keyword.MODULE);
        map.put("elsif", Keyword.ELSIF);
        map.put("def", Keyword.DEF);
        map.put("rescue", Keyword.RESCUE);
        map.put("not", Keyword.NOT);
        map.put("then", Keyword.THEN);
        map.put("yield", Keyword.YIELD);
        map.put("for", Keyword.FOR);
        map.put("self", Keyword.SELF);
        map.put("false", Keyword.FALSE);
        map.put("retry", Keyword.RETRY);
        map.put("return", Keyword.RETURN);
        map.put("true", Keyword.TRUE);
        map.put("if", Keyword.IF);
        map.put("defined?", Keyword.DEFINED_P);
        map.put("super", Keyword.SUPER);
        map.put("undef", Keyword.UNDEF);
        map.put("break", Keyword.BREAK);
        map.put("in", Keyword.IN);
        map.put("do", Keyword.DO);
        map.put("nil", Keyword.NIL);
        map.put("until", Keyword.UNTIL);
        map.put("unless", Keyword.UNLESS);
        map.put("or", Keyword.OR);
        map.put("next", Keyword.NEXT);
        map.put("when", Keyword.WHEN);
        map.put("redo", Keyword.REDO);
        map.put("and", Keyword.AND);
        map.put("begin", Keyword.BEGIN);
        map.put("__LINE__", Keyword.__LINE__);
        map.put("class", Keyword.CLASS);
        map.put("__FILE__", Keyword.__FILE__);
        map.put("END", Keyword.LEND);
        map.put("BEGIN", Keyword.LBEGIN);
        map.put("while", Keyword.WHILE);
        map.put("alias", Keyword.ALIAS);
        map.put("__ENCODING__", Keyword.__ENCODING__);
    }

    private int getFloatToken(String number) {
        double d;
        try {
            d = Double.parseDouble(number);
        } catch (NumberFormatException e) {
            warnings.warn(ID.FLOAT_OUT_OF_RANGE, getPosition(), "Float " + number + " out of range.", number);

            d = number.startsWith("-") ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        yaccValue = new FloatNode(getPosition(), d);
        return Tokens.tFLOAT;
    }

    private Object newBignumNode(String value, int radix) {
        return new BignumNode(getPosition(), new BigInteger(value, radix));
    }

    private Object newFixnumNode(String value, int radix) throws NumberFormatException {
        return new FixnumNode(getPosition(), Long.parseLong(value, radix));
    }
    
    public enum Keyword {
        END ("end", Tokens.kEND, Tokens.kEND, LexState.EXPR_END),
        ELSE ("else", Tokens.kELSE, Tokens.kELSE, LexState.EXPR_BEG),
        CASE ("case", Tokens.kCASE, Tokens.kCASE, LexState.EXPR_BEG),
        ENSURE ("ensure", Tokens.kENSURE, Tokens.kENSURE, LexState.EXPR_BEG),
        MODULE ("module", Tokens.kMODULE, Tokens.kMODULE, LexState.EXPR_BEG),
        ELSIF ("elsif", Tokens.kELSIF, Tokens.kELSIF, LexState.EXPR_BEG),
        DEF ("def", Tokens.kDEF, Tokens.kDEF, LexState.EXPR_FNAME),
        RESCUE ("rescue", Tokens.kRESCUE, Tokens.kRESCUE_MOD, LexState.EXPR_MID),
        NOT ("not", Tokens.kNOT, Tokens.kNOT, LexState.EXPR_BEG),
        THEN ("then", Tokens.kTHEN, Tokens.kTHEN, LexState.EXPR_BEG),
        YIELD ("yield", Tokens.kYIELD, Tokens.kYIELD, LexState.EXPR_ARG),
        FOR ("for", Tokens.kFOR, Tokens.kFOR, LexState.EXPR_BEG),
        SELF ("self", Tokens.kSELF, Tokens.kSELF, LexState.EXPR_END),
        FALSE ("false", Tokens.kFALSE, Tokens.kFALSE, LexState.EXPR_END),
        RETRY ("retry", Tokens.kRETRY, Tokens.kRETRY, LexState.EXPR_END),
        RETURN ("return", Tokens.kRETURN, Tokens.kRETURN, LexState.EXPR_MID),
        TRUE ("true", Tokens.kTRUE, Tokens.kTRUE, LexState.EXPR_END),
        IF ("if", Tokens.kIF, Tokens.kIF_MOD, LexState.EXPR_BEG),
        DEFINED_P ("defined?", Tokens.kDEFINED, Tokens.kDEFINED, LexState.EXPR_ARG),
        SUPER ("super", Tokens.kSUPER, Tokens.kSUPER, LexState.EXPR_ARG),
        UNDEF ("undef", Tokens.kUNDEF, Tokens.kUNDEF, LexState.EXPR_FNAME),
        BREAK ("break", Tokens.kBREAK, Tokens.kBREAK, LexState.EXPR_MID),
        IN ("in", Tokens.kIN, Tokens.kIN, LexState.EXPR_BEG),
        DO ("do", Tokens.kDO, Tokens.kDO, LexState.EXPR_BEG),
        NIL ("nil", Tokens.kNIL, Tokens.kNIL, LexState.EXPR_END),
        UNTIL ("until", Tokens.kUNTIL, Tokens.kUNTIL_MOD, LexState.EXPR_BEG),
        UNLESS ("unless", Tokens.kUNLESS, Tokens.kUNLESS_MOD, LexState.EXPR_BEG),
        OR ("or", Tokens.kOR, Tokens.kOR, LexState.EXPR_BEG),
        NEXT ("next", Tokens.kNEXT, Tokens.kNEXT, LexState.EXPR_MID),
        WHEN ("when", Tokens.kWHEN, Tokens.kWHEN, LexState.EXPR_BEG),
        REDO ("redo", Tokens.kREDO, Tokens.kREDO, LexState.EXPR_END),
        AND ("and", Tokens.kAND, Tokens.kAND, LexState.EXPR_BEG),
        BEGIN ("begin", Tokens.kBEGIN, Tokens.kBEGIN, LexState.EXPR_BEG),
        __LINE__ ("__LINE__", Tokens.k__LINE__, Tokens.k__LINE__, LexState.EXPR_END),
        CLASS ("class", Tokens.kCLASS, Tokens.kCLASS, LexState.EXPR_CLASS),
        __FILE__("__FILE__", Tokens.k__FILE__, Tokens.k__FILE__, LexState.EXPR_END),
        LEND ("END", Tokens.klEND, Tokens.klEND, LexState.EXPR_END),
        LBEGIN ("BEGIN", Tokens.klBEGIN, Tokens.klBEGIN, LexState.EXPR_END),
        WHILE ("while", Tokens.kWHILE, Tokens.kWHILE_MOD, LexState.EXPR_BEG),
        ALIAS ("alias", Tokens.kALIAS, Tokens.kALIAS, LexState.EXPR_FNAME),
        __ENCODING__("__ENCODING__", Tokens.k__ENCODING__, Tokens.k__ENCODING__, LexState.EXPR_END);
        
        public final String name;
        public final int id0;
        public final int id1;
        public final LexState state;
        
        Keyword(String name, int id0, int id1, LexState state) {
            this.name = name;
            this.id0 = id0;
            this.id1 = id1;
            this.state = state;
        }
    }
    
    public enum LexState {
        EXPR_BEG, EXPR_END, EXPR_ARG, EXPR_CMDARG, EXPR_ENDARG, EXPR_MID,
        EXPR_FNAME, EXPR_DOT, EXPR_CLASS, EXPR_VALUE, EXPR_ENDFN
    }
    
    public static Keyword getKeyword(String str) {
        return (Keyword) map.get(str);
    }

    // Last token read via yylex().
    private int token;
    
    // Value of last token which had a value associated with it.
    Object yaccValue;

    // Stream of data that yylex() examines.
    private LexerSource src;
    
    // Used for tiny smidgen of grammar in lexer (see setParserSupport())
    private ParserSupport parserSupport = null;

    // What handles warnings
    private IRubyWarnings warnings;

    // Additional context surrounding tokens that both the lexer and
    // grammar use.
    private LexState lex_state;

    // Whether or not the lexer should be "space preserving" - see {set,get}PreserveSpaces
    // the parser should consider whitespace sequences and code comments to be separate
    // tokens to return to the client. Parsers typically do not want to see any
    // whitespace or comment tokens - but an IDE trying to tokenize a chunk of source code
    // does want to identify these separately. The default, false, means the parser mode.
    private boolean preserveSpaces;
    
    private String encoding = null;

    // List of HeredocTerms to be applied when we see a new line.
    // This is done to be able to handle heredocs in input source order (instead of
    // the normal JRuby operation of handling it out of order by stashing the rest of
    // the line on the side while searching for the end of the heredoc, and then pushing
    // the line back on the input before proceeding). Out-of-order handling of tokens
    // is difficult for the IDE to handle, so in syntax highlighting mode we process the
    // output differently. When we see a heredoc token, we return a normal string-begin
    // token, but we also push the heredoc term (without line-state) into the "newline-list"
    // and continue processing normally (with no string strterm in effect).
    // Whenever we get to a new line, we look at the newline list, and if we find something
    // there, we pull it off and set it as the current string term and use it to process
    // the string literal and end token.
    // NOTE:: This list should not be modified but rather duplicated, in order to ensure
    // that incremental lexing (which relies on pulling out these lists at token boundaries)
    // will not interfere with each other.
    public static class HeredocContext {
        private HeredocTerm[] heredocTerms;

        private boolean[] lookingForEnds;

        public HeredocContext(HeredocTerm term) {
            heredocTerms = new HeredocTerm[] {term, term};
            lookingForEnds = new boolean[] {false, true};
        }

        private HeredocContext(HeredocTerm[] terms, boolean[] lookingForEnds) {
            heredocTerms = terms;
            this.lookingForEnds = lookingForEnds;
        }

        private HeredocContext add(HeredocTerm h) {
            // Add 2 entries: one for starting lexing of the string, one for the end token
            HeredocTerm[] copy = new HeredocTerm[heredocTerms.length + 2];
            System.arraycopy(heredocTerms, 0, copy, 0, heredocTerms.length);
            copy[heredocTerms.length] = h;
            copy[heredocTerms.length + 1] = h;

            boolean[] copy2 = new boolean[lookingForEnds.length + 2];
            System.arraycopy(lookingForEnds, 0, copy2, 0, lookingForEnds.length);
            copy2[lookingForEnds.length] = false;
            copy2[lookingForEnds.length + 1] = true;

            return new HeredocContext(copy, copy2);
        }

        private HeredocTerm getTerm() {
            return heredocTerms[0];
        }

        private HeredocContext pop() {
            if (heredocTerms.length > 1) {
                HeredocTerm[] copy = new HeredocTerm[heredocTerms.length - 1];
                System.arraycopy(heredocTerms, 1, copy, 0, copy.length);

                boolean[] copy2 = new boolean[lookingForEnds.length - 1];
                System.arraycopy(lookingForEnds, 1, copy2, 0, copy2.length);

                HeredocContext hc = new HeredocContext(copy, copy2);
                return hc;
            }

            return null;
        }

        public boolean isLookingForEnd() {
            return lookingForEnds[0];
        }

        @Override
        public String toString() {
            CStringBuilder buffer = new CStringBuilder("HeredocContext(count=");
            buffer.append(Integer.toString(heredocTerms.length));
            buffer.append("):");
            for (int i = 0; i < heredocTerms.length; i++) {
                if (i > 0) buffer.append(",");

                buffer.append("end:").append(lookingForEnds[i]);
                buffer.append(",term:").append(heredocTerms[i]);
            }
            
            return buffer.toString();
        }

        @Override
        public int hashCode() {
            return heredocTerms[0].getMutableState().hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof HeredocContext) {
                HeredocContext o = (HeredocContext) other;
                if (o.heredocTerms.length != heredocTerms.length) return false;

                return heredocTerms[0].getMutableState().equals(o.heredocTerms[0].getMutableState());
            }

            return false;
        }
    }
    
    public HeredocContext heredocContext;

    // Tempory buffer to build up a potential token.  Consumer takes responsibility to reset 
    // this before use.
    private CStringBuilder tokenBuffer = new CStringBuilder(60);

    private StackState conditionState = new StackState();
    private StackState cmdArgumentState = new StackState();
    private StrTerm lex_strterm;
    public boolean commandStart;
    
    // Give a name to a value.  Enebo: This should be used more.
    static final int EOF = -1;

    // ruby constants for strings (should this be moved somewhere else?)
    static final int STR_FUNC_ESCAPE=0x01;
    static final int STR_FUNC_EXPAND=0x02;
    static final int STR_FUNC_REGEXP=0x04;
    static final int STR_FUNC_QWORDS=0x08;
    static final int STR_FUNC_SYMBOL=0x10;
    // When the heredoc identifier specifies <<-EOF that indents before ident. are ok (the '-').
    static final int STR_FUNC_INDENT=0x20;

    private static final int str_squote = 0;
    private static final int str_dquote = STR_FUNC_EXPAND;
    private static final int str_xquote = STR_FUNC_EXPAND;
    private static final int str_regexp = STR_FUNC_REGEXP | STR_FUNC_ESCAPE | STR_FUNC_EXPAND;
    private static final int str_ssym   = STR_FUNC_SYMBOL;
    private static final int str_dsym   = STR_FUNC_SYMBOL | STR_FUNC_EXPAND;

    // Are we lexing Ruby 1.8 or 1.9+ syntax
    private boolean isOneEight;
    private boolean isTwoZero;
    // Count of nested parentheses (1.9 only)
    private int parenNest = 0;
    // 1.9 only
    private int leftParenBegin = 0;


    /* In normal JRuby, there is a "spaceSeen" flag which is local to yylex. It is
     * used to interpret input based on whether a space was recently seen.
     * Since I now bail -out- of yylex() when I see space, I need to be able
     * to preserve this flag across yylex() calls. In most cases, "spaceSeen"
     * should be set to false (as it previous was at the beginning of yylex().
     * However, when I've seen a space and have bailed out, I need to set spaceSeen=true
     * on the next call to yylex(). This is what the following flag is all about.
     * It is set to true when we bail out on space (or other states that didn't
     * previous bail out and spaceSeen is true).
     */
    private boolean setSpaceSeen;

    /**
     * Set whether or not the lexer should be "space preserving" - in other words, whether
     * the parser should consider whitespace sequences and code comments to be separate
     * tokens to return to the client. Parsers typically do not want to see any
     * whitespace or comment tokens - but an IDE trying to tokenize a chunk of source code
     * does want to identify these separately. The default, false, means the parser mode.
     *
     * @param preserveSpaces If true, return space and comment sequences as tokens, if false, skip these
     * @see #getPreserveSpaces
     */
    public void setPreserveSpaces(final boolean preserveSpaces) {
        this.preserveSpaces = preserveSpaces;
    }

    /**
     * Return whether or not the lexer should be "space preserving". For a description
     * of what this means, see {@link #setPreserveSpaces}.
     *
     * @return preserveSpaces True iff space and comment sequences will be returned as
     * tokens, and false otherwise.
     *
     * @see #setPreserveSpaces
     */
    public boolean getPreserveSpaces() {
       return preserveSpaces;
    }

    public LexState getLexState() {
        return lex_state;
    }

    public void setLexState(final LexState lex_state) {
        this.lex_state = lex_state;
    }

    public boolean isSetSpaceSeen() {
        return setSpaceSeen;
    }

    public void setSpaceSeen(boolean setSpaceSeen) {
        this.setSpaceSeen = setSpaceSeen;
    }

    public boolean isCommandStart() {
        return commandStart;
    }

    public void setCommandStart(boolean commandStart) {
        this.commandStart = commandStart;
    }

    public LexerSource getSource() {
        return this.src;
    }

    public int incrementParenNest() {
        parenNest++;

        return parenNest;
    }

    public int getLeftParenBegin() {
        return leftParenBegin;
    }

    public void setLeftParenBegin(int value) {
        leftParenBegin = value;
    }

    public Lexer() {
    	this(true);
    }
    
    public Lexer(boolean isOneEight) {
    	reset();
        this.isOneEight = isOneEight;
    }
    
    public void reset() {
    	token = 0;
    	yaccValue = null;
    	src = null;
        setState(LexState.EXPR_BEG); // Parser sets this right away but for pure-Lexing we also set it.
        resetStacks();
        lex_strterm = null;
        commandStart = true;
        if (parserSupport != null) isTwoZero = parserSupport.getConfiguration().getVersion().is2_0();
    }
    
    /**
     * How the parser advances to the next token.
     * 
     * @return true if not at end of file (EOF).
     */
    public boolean advance() throws IOException {
        return (token = yylex()) != EOF;
    }
    
    public int nextToken() throws IOException {
        token = yylex();

        return token == EOF ? 0 : token;
    }    
    
    /**
     * Last token read from the lexer at the end of a call to yylex()
     * 
     * @return last token read
     */
    public int token() {
        return token;
    }

    public CStringBuilder getTokenBuffer() {
        return tokenBuffer;
    }
    
    /**
     * Value of last token (if it is a token which has a value).
     * 
     * @return value of last value-laden token
     */
    public Object value() {
        return yaccValue;
    }
    
    /**
     * Get position information for Token/Node that follows node represented by startPosition 
     * and current lexer location.
     * 
     * @param startPosition previous node/token
     * @param inclusive include previous node into position information of current node
     * @return a new position
     */
    public SourcePosition getPosition(SourcePosition startPosition, boolean inclusive) {
    	return src.getPosition(startPosition, inclusive); 
    }
    
    public SourcePosition getPosition() {
        return src.getPosition(null, false);
    }
    
    public String getCurrentLine() {
        return null;
        // TODO: Add currentLine?
//        return src.getCurrentLine();
    }    
    
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    public String getEncoding() {
        return encoding;
    }

    /**
     * Parse must pass its support object for some check at bottom of
     * yylex().  Ruby does it this way as well (i.e. a little parsing
     * logic in the lexer).
     * 
     * @param parserSupport
     */
    public void setParserSupport(ParserSupport parserSupport) {
        this.parserSupport = parserSupport;
    }

    /**
     * Allow the parser to set the source for its lexer.
     * 
     * @param source where the lexer gets raw data
     */
    public void setSource(LexerSource source) {
        this.src = source;
    }

    public StrTerm getStrTerm() {
        return lex_strterm;
    }
    
    public void setStrTerm(StrTerm strterm) {
        this.lex_strterm = strterm;
    }

    public void resetStacks() {
        conditionState.reset();
        cmdArgumentState.reset();
    }
    
    public void setWarnings(IRubyWarnings warnings) {
        this.warnings = warnings;
    }
    
    private void printState() {
        if (lex_state == null) {
            System.out.println("NULL");
        } else {
            System.out.println(lex_state);
        }
    }

    public void setState(LexState state) {
        this.lex_state = state;
//        printState();        
    }

    public StackState getCmdArgumentState() {
        return cmdArgumentState;
    }

    public boolean isOneEight() {
        return isOneEight;
    }

    public StackState getConditionState() {
        return conditionState;
    }
    
    public void setValue(Object yaccValue) {
        this.yaccValue = yaccValue;
    }

    private boolean isNext_identchar() throws IOException {
        int c = src.read();
        src.unread(c);

        return c != EOF && (Character.isLetterOrDigit(c) || c == '_');
    }

    private boolean isBEG() {
        return lex_state == LexState.EXPR_BEG || lex_state == LexState.EXPR_MID ||
                lex_state == LexState.EXPR_CLASS || (!isOneEight && lex_state == LexState.EXPR_VALUE);
    }

    private boolean isEND() {
        return lex_state == LexState.EXPR_END || lex_state == LexState.EXPR_ENDARG ||
                (!isOneEight && lex_state == LexState.EXPR_ENDFN);
    }
    
    private boolean isARG() {
        return lex_state == LexState.EXPR_ARG || lex_state == LexState.EXPR_CMDARG;
    }
    
   private boolean isSpaceArg(int c, boolean spaceSeen) {
        return isARG() && spaceSeen && !Character.isWhitespace(c);
    }    

    private void determineExpressionState() {
        switch (lex_state) {
        case EXPR_FNAME: case EXPR_DOT:
            setState(LexState.EXPR_ARG);
            break;
        default:
            setState(LexState.EXPR_BEG);
            break;
        }
    }

    private Object getInteger(String value, int radix) {
        try {
            return newFixnumNode(value, radix);
        } catch (NumberFormatException e) {
            return newBignumNode(value, radix);
        }
    }

	/**
	 * @param c the character to test
	 * @return true if character is a hex value (0-9a-f)
	 */
    static boolean isHexChar(int c) {
        return Character.isDigit(c) || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
    }

    /**
	 * @param c the character to test
     * @return true if character is an octal value (0-7)
	 */
    static boolean isOctChar(int c) {
        return '0' <= c && c <= '7';
    }
    
    /**
     * This is a valid character for an identifier?
     *
     * @param c is character to be compared
     * @return whether c is an identifier or not
     *
     * mri: is_identchar
     */
    public boolean isIdentifierChar(int c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
    
    /**
     * What type/kind of quote are we dealing with?
     * 
     * @param c first character the the quote construct
     * @return a token that specifies the quote type
     */
    private int parseQuote(int c) throws IOException {
        int begin, end;
        boolean shortHand;
        
        // Short-hand (e.g. %{,%.,%!,... versus %Q{).
        if (!Character.isLetterOrDigit(c)) {
            begin = c;
            c = 'Q';
            shortHand = true;
        // Long-hand (e.g. %Q{}).
        } else {
            shortHand = false;
            begin = src.read();
            if (Character.isLetterOrDigit(begin) /* no mb || ismbchar(term)*/) {
                throw new SyntaxException(PID.STRING_UNKNOWN_TYPE, getPosition(), 
                        getCurrentLine(), "unknown type of %string");
            }
        }
        if (c == EOF || begin == EOF) {
            throw new SyntaxException(PID.STRING_HITS_EOF, getPosition(), 
                    getCurrentLine(), "unterminated quoted string meets end of file");
        }
        
        // Figure end-char.  '\0' is special to indicate begin=end and that no nesting?
        switch(begin) {
        case '(': end = ')'; break;
        case '[': end = ']'; break;
        case '{': end = '}'; break;
        case '<': end = '>'; break;
        default: 
            end = begin; 
            begin = '\0';
        }

        switch (c) {
        case 'Q':
            lex_strterm = new StringTerm(str_dquote, begin ,end);
            yaccValue = new Token("%"+ (shortHand ? (""+end) : ("" + c + begin)), getPosition());
            return Tokens.tSTRING_BEG;

        case 'q':
            lex_strterm = new StringTerm(str_squote, begin, end);
            yaccValue = new Token("%"+c+begin, getPosition());
            return Tokens.tSTRING_BEG;

        case 'W':
            lex_strterm = new StringTerm(str_dquote | STR_FUNC_QWORDS, begin, end);
            do {c = src.read();} while (Character.isWhitespace(c));
            src.unread(c);
            yaccValue = new Token("%"+c+begin, getPosition());
            return Tokens.tWORDS_BEG;

        case 'w':
            lex_strterm = new StringTerm(/* str_squote | */ STR_FUNC_QWORDS, begin, end);
            do {c = src.read();} while (Character.isWhitespace(c));
            src.unread(c);
            yaccValue = new Token("%"+c+begin, getPosition());
            return Tokens.tQWORDS_BEG;

        case 'x':
            lex_strterm = new StringTerm(str_xquote, begin, end);
            yaccValue = new Token("%"+c+begin, getPosition());
            return Tokens.tXSTRING_BEG;

        case 'r':
            lex_strterm = new StringTerm(str_regexp, begin, end);
            yaccValue = new Token("%"+c+begin, getPosition());
            return Tokens.tREGEXP_BEG;

        case 's':
            lex_strterm = new StringTerm(str_ssym, begin, end);
            setState(LexState.EXPR_FNAME);
            yaccValue = new Token("%"+c+begin, getPosition());
            return Tokens.tSYMBEG;
        case 'I':
            if (isTwoZero) {
                lex_strterm = new StringTerm(str_dquote | STR_FUNC_QWORDS, begin, end);
                do {c = src.read();} while (Character.isWhitespace(c));                
                src.unread(c);
                yaccValue = new Token("%" + c + begin, getPosition());
                return Tokens.tSYMBOLS_BEG;
            }
        case 'i':
            if (isTwoZero) {
                lex_strterm = new StringTerm(/* str_squote | */STR_FUNC_QWORDS, begin, end);
                do {c = src.read();} while (Character.isWhitespace(c));
                src.unread(c);
                yaccValue = new Token("%" + c + begin, getPosition());
                return Tokens.tQSYMBOLS_BEG;
            }

        default:
            throw new SyntaxException(PID.STRING_UNKNOWN_TYPE, getPosition(), getCurrentLine(),
                    "Unknown type of %string. Expected 'Q', 'q', 'w', 'x', 'r' or any non letter character, but found '" + c + "'.");
        }
    }
    
    private int hereDocumentIdentifier() throws IOException {
        int c = src.read(); 
        int term;

        int func = 0;
        if (c == '-') {
            c = src.read();
            func = STR_FUNC_INDENT;
        }
        
        CStringBuilder markerValue;
        if (c == '\'' || c == '"' || c == '`') {
            if (c == '\'') {
                func |= str_squote;
            } else if (c == '"') {
                func |= str_dquote;
            } else {
                func |= str_xquote; 
            }

            markerValue = new CStringBuilder();
            term = c;
            while ((c = src.read()) != EOF && c != term) {
                markerValue.append(c);
            }
            if (c == EOF) {
                throw new SyntaxException(PID.STRING_MARKER_MISSING, getPosition(), 
                        getCurrentLine(), "unterminated here document identifier");
            }	
        } else {
            if (!isIdentifierChar(c)) {
                src.unread(c);
                if ((func & STR_FUNC_INDENT) != 0) {
                    src.unread('-');
                }
                return 0;
            }
            markerValue = new CStringBuilder();
            term = '"';
            func |= str_dquote;
            do {
                markerValue.append(c);
            } while ((c = src.read()) != EOF && isIdentifierChar(c));

            src.unread(c);
        }

        // See issue nb #93990
        // It is very difficult for the IDE (especially with incremental lexing)
        // to handle heredocs with additional input on the line, where the
        // input end up getting processed out of order (JRuby will read the rest
        // of the line, process up to the end token, then stash the rest of the line
        // back on the input and continue (which could process another heredoc)
        // and then just jump over the heredocs since input is processed out of order.
        // Instead, use our own HeredocTerms which behave differently; they don't
        // mess with the output, and will be handled differently from within
        // the lexer in that it gets invited back on the next line (in order)
        if (preserveSpaces) {
            HeredocTerm h = new HeredocTerm(markerValue.toString(), func, null);

            if (term == '`') {
                yaccValue = new Token("`", getPosition());
                return Tokens.tXSTRING_BEG;
            }

            yaccValue = new Token("\"", getPosition());

            if (heredocContext == null) {
                heredocContext = new HeredocContext(h);
            } else {
                heredocContext = heredocContext.add(h);
            }

            return Tokens.tSTRING_BEG;
        }

        String lastLine = src.readLineBytes();
        lastLine = lastLine.concat("\n");
        lex_strterm = new HeredocTerm(markerValue.toString(), func, lastLine);

        if (term == '`') {
            yaccValue = new Token("`", getPosition());
            return Tokens.tXSTRING_BEG;
        }
        
        yaccValue = new Token("\"", getPosition());
        // Hacky: Advance position to eat newline here....
        getPosition();
        return Tokens.tSTRING_BEG;
    }
    
    private void arg_ambiguous() {
        if (warnings.isVerbose()) warnings.warning(ID.AMBIGUOUS_ARGUMENT, getPosition(), "Ambiguous first argument; make sure.");
    }

    
    /* MRI: magic_comment_marker */
    /* This impl is a little sucky.  We basically double scan the same bytelist twice.  Once here
     * and once in parseMagicComment.
     */
    private int magicCommentMarker(String str, int begin) {
        int i = begin;
        int len = str.length();

        while (i < len) {
            switch (str.charAt(i)) {
                case '-':
                    if (i >= 2 && str.charAt(i - 1) == '*' && str.charAt(i - 2) == '-') return i + 1;
                    i += 2;
                    break;
                case '*':
                    if (i + 1 >= len) return -1;

                    if (str.charAt(i + 1) != '-') {
                        i += 4;
                    } else if (str.charAt(i - 1) != '-') {
                        i += 2;
                    } else {
                        return i + 2;
                    }
                    break;
                default:
                    i += 3;
                    break;
            }
        }
        return -1;
    }
    
    private boolean magicCommentSpecialChar(char c) {
        switch (c) {
            case '\'': case '"': case ':': case ';': return true;
        }
        return false;
    }
    
    private static final String magicString = "([^\\s\'\":;]+)\\s*:\\s*(\"(?:\\\\.|[^\"])*\"|[^\"\\s;]+)[\\s;]*";
    private static final Pattern magicRegexp = Pattern.compile(magicString);

    // MRI: parser_magic_comment
    protected boolean parseMagicComment(String magicLine) throws IOException {
        int length = magicLine.length();

        if (length <= 7) return false;
        int beg = magicCommentMarker(magicLine, 0);
        if (beg < 0) return false;
        int end = magicCommentMarker(magicLine, beg);
        if (end < 0) return false;

        // We only use a regex if -*- ... -*- is found.  Not too hot a path?
        int realSize = magicLine.length();
        Matcher matcher = magicRegexp.matcher(magicLine);
        boolean result = matcher.find(beg);

        if (!result) return false;
        
        String name = matcher.group(1);
        if (!name.equalsIgnoreCase("encoding")) return false;

        setEncoding(matcher.group(2));

        return true;
    }

    // TODO: Make hand-rolled version of this
    private static final String encodingString = "[cC][oO][dD][iI][nN][gG]\\s*[=:]\\s*([a-zA-Z0-9\\-_]+)";
    private static final Pattern encodingRegexp = Pattern.compile(encodingString);

    protected void handleFileEncodingComment(String encodingLine) throws IOException {
        Matcher matcher = encodingRegexp.matcher(encodingLine);
        boolean result = matcher.find();

        if (!result) return;

        setEncoding(matcher.group(1));
    }
    
    // FIXME: Consider stashing some boolean rather that running this set of conditionals a million times.
    public boolean collectComments() {
        switch (parserSupport.getConfiguration().getSyntax()) {
            case COMMENTS: case ALL:
                return true;
        }
        
        return false;
    }
    
    /**
     * Read a comment up to end of line.  When found each comment will get stored away into
     * the parser result so that any interested party can use them as they seem fit.  One idea
     * is that IDE authors can do distance based heuristics to associate these comments to the
     * AST node they think they belong to.
     * 
     * @param c last character read from lexer source
     * @return newline or eof value 
     */
    protected int readComment(int c) throws IOException {
        return collectComments() ? readCommentLong(c) : src.skipUntil('\n');
    }
    
    private int readCommentLong(int c) throws IOException {
        SourcePosition startPosition = src.getPosition();
        tokenBuffer.setLength(0);
        tokenBuffer.append(c);

        // FIXME: Consider making a better LexerSource.readLine
        while ((c = src.read()) != '\n') {
            if (c == EOF) break;

            tokenBuffer.append(c);
        }
        src.unread(c);

        // ENEBO: When is parserSupport actually null?
        if (parserSupport != null) {
            // Store away each comment to parser result so IDEs can do whatever they want with them.
            SourcePosition position = startPosition.union(getPosition());

            parserSupport.getResult().addSyntax(new CommentNode(position, tokenBuffer.toString()));
        } else {
            getPosition();
        }
        
        return c;
    }
    
    /*
     * Not normally used, but is left in here since it can be useful in debugging
     * grammar and lexing problems.
     *
     */
    private void printToken(int token) {
        //System.out.print("LOC: " + support.getPosition() + " ~ ");
        
        switch (token) {
            case Tokens.yyErrorCode: System.err.print("yyErrorCode,"); break;
            case Tokens.kCLASS: System.err.print("kClass,"); break;
            case Tokens.kMODULE: System.err.print("kModule,"); break;
            case Tokens.kDEF: System.err.print("kDEF,"); break;
            case Tokens.kUNDEF: System.err.print("kUNDEF,"); break;
            case Tokens.kBEGIN: System.err.print("kBEGIN,"); break;
            case Tokens.kRESCUE: System.err.print("kRESCUE,"); break;
            case Tokens.kENSURE: System.err.print("kENSURE,"); break;
            case Tokens.kEND: System.err.print("kEND,"); break;
            case Tokens.kIF: System.err.print("kIF,"); break;
            case Tokens.kUNLESS: System.err.print("kUNLESS,"); break;
            case Tokens.kTHEN: System.err.print("kTHEN,"); break;
            case Tokens.kELSIF: System.err.print("kELSIF,"); break;
            case Tokens.kELSE: System.err.print("kELSE,"); break;
            case Tokens.kCASE: System.err.print("kCASE,"); break;
            case Tokens.kWHEN: System.err.print("kWHEN,"); break;
            case Tokens.kWHILE: System.err.print("kWHILE,"); break;
            case Tokens.kUNTIL: System.err.print("kUNTIL,"); break;
            case Tokens.kFOR: System.err.print("kFOR,"); break;
            case Tokens.kBREAK: System.err.print("kBREAK,"); break;
            case Tokens.kNEXT: System.err.print("kNEXT,"); break;
            case Tokens.kREDO: System.err.print("kREDO,"); break;
            case Tokens.kRETRY: System.err.print("kRETRY,"); break;
            case Tokens.kIN: System.err.print("kIN,"); break;
            case Tokens.kDO: System.err.print("kDO,"); break;
            case Tokens.kDO_COND: System.err.print("kDO_COND,"); break;
            case Tokens.kDO_BLOCK: System.err.print("kDO_BLOCK,"); break;
            case Tokens.kRETURN: System.err.print("kRETURN,"); break;
            case Tokens.kYIELD: System.err.print("kYIELD,"); break;
            case Tokens.kSUPER: System.err.print("kSUPER,"); break;
            case Tokens.kSELF: System.err.print("kSELF,"); break;
            case Tokens.kNIL: System.err.print("kNIL,"); break;
            case Tokens.kTRUE: System.err.print("kTRUE,"); break;
            case Tokens.kFALSE: System.err.print("kFALSE,"); break;
            case Tokens.kAND: System.err.print("kAND,"); break;
            case Tokens.kOR: System.err.print("kOR,"); break;
            case Tokens.kNOT: System.err.print("kNOT,"); break;
            case Tokens.kIF_MOD: System.err.print("kIF_MOD,"); break;
            case Tokens.kUNLESS_MOD: System.err.print("kUNLESS_MOD,"); break;
            case Tokens.kWHILE_MOD: System.err.print("kWHILE_MOD,"); break;
            case Tokens.kUNTIL_MOD: System.err.print("kUNTIL_MOD,"); break;
            case Tokens.kRESCUE_MOD: System.err.print("kRESCUE_MOD,"); break;
            case Tokens.kALIAS: System.err.print("kALIAS,"); break;
            case Tokens.kDEFINED: System.err.print("kDEFINED,"); break;
            case Tokens.klBEGIN: System.err.print("klBEGIN,"); break;
            case Tokens.klEND: System.err.print("klEND,"); break;
            case Tokens.k__LINE__: System.err.print("k__LINE__,"); break;
            case Tokens.k__FILE__: System.err.print("k__FILE__,"); break;
            case Tokens.k__ENCODING__: System.err.print("k__ENCODING__,"); break;
            case Tokens.kDO_LAMBDA: System.err.print("kDO_LAMBDA,"); break;
            case Tokens.tIDENTIFIER: System.err.print("tIDENTIFIER["+ value() + "],"); break;
            case Tokens.tFID: System.err.print("tFID[" + value() + "],"); break;
            case Tokens.tGVAR: System.err.print("tGVAR[" + value() + "],"); break;
            case Tokens.tIVAR: System.err.print("tIVAR[" + value() +"],"); break;
            case Tokens.tCONSTANT: System.err.print("tCONSTANT["+ value() +"],"); break;
            case Tokens.tCVAR: System.err.print("tCVAR,"); break;
            case Tokens.tINTEGER: System.err.print("tINTEGER,"); break;
            case Tokens.tFLOAT: System.err.print("tFLOAT,"); break;
            case Tokens.tSTRING_CONTENT: System.err.print("tSTRING_CONTENT[" + ((StrNode) value()).getValue().toString() + "],"); break;
            case Tokens.tSTRING_BEG: System.err.print("tSTRING_BEG,"); break;
            case Tokens.tSTRING_END: System.err.print("tSTRING_END,"); break;
            case Tokens.tSTRING_DBEG: System.err.print("STRING_DBEG,"); break;
            case Tokens.tSTRING_DVAR: System.err.print("tSTRING_DVAR,"); break;
            case Tokens.tXSTRING_BEG: System.err.print("tXSTRING_BEG,"); break;
            case Tokens.tREGEXP_BEG: System.err.print("tREGEXP_BEG,"); break;
            case Tokens.tREGEXP_END: System.err.print("tREGEXP_END,"); break;
            case Tokens.tWORDS_BEG: System.err.print("tWORDS_BEG,"); break;
            case Tokens.tQWORDS_BEG: System.err.print("tQWORDS_BEG,"); break;
            case Tokens.tBACK_REF: System.err.print("tBACK_REF,"); break;
            case Tokens.tBACK_REF2: System.err.print("tBACK_REF2,"); break;
            case Tokens.tNTH_REF: System.err.print("tNTH_REF,"); break;
            case Tokens.tUPLUS: System.err.print("tUPLUS"); break;
            case Tokens.tUMINUS: System.err.print("tUMINUS,"); break;
            case Tokens.tPOW: System.err.print("tPOW,"); break;
            case Tokens.tCMP: System.err.print("tCMP,"); break;
            case Tokens.tEQ: System.err.print("tEQ,"); break;
            case Tokens.tEQQ: System.err.print("tEQQ,"); break;
            case Tokens.tNEQ: System.err.print("tNEQ,"); break;
            case Tokens.tGEQ: System.err.print("tGEQ,"); break;
            case Tokens.tLEQ: System.err.print("tLEQ,"); break;
            case Tokens.tANDOP: System.err.print("tANDOP,"); break;
            case Tokens.tOROP: System.err.print("tOROP,"); break;
            case Tokens.tMATCH: System.err.print("tMATCH,"); break;
            case Tokens.tNMATCH: System.err.print("tNMATCH,"); break;
            case Tokens.tDOT: System.err.print("tDOT,"); break;
            case Tokens.tDOT2: System.err.print("tDOT2,"); break;
            case Tokens.tDOT3: System.err.print("tDOT3,"); break;
            case Tokens.tAREF: System.err.print("tAREF,"); break;
            case Tokens.tASET: System.err.print("tASET,"); break;
            case Tokens.tLSHFT: System.err.print("tLSHFT,"); break;
            case Tokens.tRSHFT: System.err.print("tRSHFT,"); break;
            case Tokens.tCOLON2: System.err.print("tCOLON2,"); break;
            case Tokens.tCOLON3: System.err.print("tCOLON3,"); break;
            case Tokens.tOP_ASGN: System.err.print("tOP_ASGN,"); break;
            case Tokens.tASSOC: System.err.print("tASSOC,"); break;
            case Tokens.tLPAREN: System.err.print("tLPAREN,"); break;
            case Tokens.tLPAREN2: System.err.print("tLPAREN2,"); break;
            case Tokens.tLPAREN_ARG: System.err.print("tLPAREN_ARG,"); break;
            case Tokens.tLBRACK: System.err.print("tLBRACK,"); break;
            case Tokens.tRBRACK: System.err.print("tRBRACK,"); break;
            case Tokens.tLBRACE: System.err.print("tLBRACE,"); break;
            case Tokens.tLBRACE_ARG: System.err.print("tLBRACE_ARG,"); break;
            case Tokens.tSTAR: System.err.print("tSTAR,"); break;
            case Tokens.tSTAR2: System.err.print("tSTAR2,"); break;
            case Tokens.tAMPER: System.err.print("tAMPER,"); break;
            case Tokens.tAMPER2: System.err.print("tAMPER2,"); break;
            case Tokens.tSYMBEG: System.err.print("tSYMBEG,"); break;
            case Tokens.tTILDE: System.err.print("tTILDE,"); break;
            case Tokens.tPERCENT: System.err.print("tPERCENT,"); break;
            case Tokens.tDIVIDE: System.err.print("tDIVIDE,"); break;
            case Tokens.tPLUS: System.err.print("tPLUS,"); break;
            case Tokens.tMINUS: System.err.print("tMINUS,"); break;
            case Tokens.tLT: System.err.print("tLT,"); break;
            case Tokens.tGT: System.err.print("tGT,"); break;
            case Tokens.tCARET: System.err.print("tCARET,"); break;
            case Tokens.tBANG: System.err.print("tBANG,"); break;
            case Tokens.tLCURLY: System.err.print("tTLCURLY,"); break;
            case Tokens.tRCURLY: System.err.print("tRCURLY,"); break;
            case Tokens.tPIPE: System.err.print("tTPIPE,"); break;
            case Tokens.tLAMBDA: System.err.print("tLAMBDA,"); break;
            case Tokens.tLAMBEG: System.err.print("tLAMBEG,"); break;
            case Tokens.tRPAREN: System.err.print("tRPAREN,"); break;
            case Tokens.tLABEL: System.err.print("tLABEL("+
                    ((Token) value()).getValue() +":),"); break;
            case '\n': System.err.println("NL"); break;
            case EOF: System.out.println("EOF"); break;
            default: System.err.print("'" + (char)token + "',"); break;
        }
    }

    // DEBUGGING HELP 
    private int yylex2() throws IOException {
        int currentToken = yylex();
        
        printToken(currentToken);
        
        return currentToken;
    }

    /**
     *  Returns the next token. Also sets yyVal is needed.
     *
     *@return    Description of the Returned Value
     */
    private int yylex() throws IOException {
        int c;
        boolean spaceSeen = false;
        boolean commandState;
        
        if (setSpaceSeen) {
            spaceSeen = true;
            setSpaceSeen = false;
        }
        
        // On new lines, possibly resume heredoc processing (see docs for newlineTerms for more)
        if (heredocContext != null) {
            if (heredocContext.isLookingForEnd()) {
                lex_strterm = heredocContext.getTerm();
            } else if (src.isANewLine()) {
                lex_strterm = heredocContext.getTerm();

                heredocContext = heredocContext.pop();
            }
        }

        if (lex_strterm != null) {
            try {
                int tok = lex_strterm.parseString(this, src);
                if (tok == Tokens.tSTRING_END || tok == Tokens.tREGEXP_END) {
                    lex_strterm = null;
                    setState(LexState.EXPR_END);

                    if (heredocContext != null && heredocContext.isLookingForEnd()) {
                        heredocContext = heredocContext.pop();
                    }
                }
                return tok;
            } catch (SyntaxException se) {
                // If we abort in string parsing, throw away the str term
                // such that we don't try again on restart
                lex_strterm = null;
                setState(LexState.EXPR_END);
                throw se;
            }
        }

        commandState = commandStart;
        commandStart = false;

        loop: for(;;) {
            c = src.read();            
            switch(c) {
            case '\004':		/* ^D */
            case '\032':		/* ^Z */
            case EOF:			/* end of script. */
                return EOF;
           
                /* white spaces */
            case ' ': case '\t': case '\f': case '\r':
            case '\13': /* '\v' */
                if (preserveSpaces) {
                    // Collapse all whitespace into one token
                    while (true) {
                        c = src.read();
                        if (c != ' ' && c != '\t' && c != '\f' && c != '\r' && c != '\13') break;
                    }
                    src.unread(c);
                    yaccValue = new Token("whitespace", getPosition());
                    setSpaceSeen = true;

                    return Tokens.tWHITESPACE;
                }
                
                getPosition();
                spaceSeen = true;
                continue;
            case '#':		/* it's a comment */
                if (preserveSpaces) {
                    // Skip to end of the comment
                    while ((c = src.read()) != '\n') {
                        if (c == EOF) break;
                    }

                    yaccValue = new Token("line-comment", getPosition());
                    setSpaceSeen = spaceSeen;
                    // Ensure that commandStart and lex_state is updated
                    // as it otherwise would have if preserveSpaces was false
                    if (!(lex_state == LexState.EXPR_BEG ||
                            lex_state == LexState.EXPR_FNAME ||
                            lex_state == LexState.EXPR_DOT ||
                            lex_state == LexState.EXPR_CLASS)) {
                        commandStart = true;
                        setState(LexState.EXPR_BEG);
                    }

                    return Tokens.tCOMMENT;
                } else {
                    // FIXME: Need to detect magic_comment in 1.9 here for encoding
                    if (readComment(c) == EOF) return EOF;
                }
                    
                /* fall through */
            case '\n':
                if (isOneEight) {             	// Replace a string of newlines with a single one
                    while((c = src.read()) == '\n');
                } else {
                    switch (lex_state) {
                    case EXPR_BEG: case EXPR_FNAME: case EXPR_DOT:
                    case EXPR_CLASS: case EXPR_VALUE:
                        getPosition();
                        continue loop;
                    }

                    boolean done = false;
                    while(!done) {
                        c = src.read();

                        switch (c) {
                        case ' ': case '\t': case '\f': case '\r': case '\13': /* '\v' */
                            spaceSeen = true;
                            continue;
                        case '.': {
                            if ((c = src.read()) != '.') {
                                src.unread(c);
                                src.unread('.');

                                continue loop;
                            }
                        }
                        default:
                        case -1:		// EOF (ENEBO: After default?
                            done = true;
                        }
                    }
                }

                if (c == -1 && !preserveSpaces) return EOF;

                src.unread(c);
                getPosition();

                if (preserveSpaces) {
                    src.setIsANewLine(true);
                    yaccValue = new Token("whitespace", getPosition());
                    // Ensure that commandStart and lex_state is updated
                    // as it otherwise would have if preserveSpaces was false
                    if (!(lex_state == LexState.EXPR_BEG ||
                        lex_state == LexState.EXPR_FNAME ||
                        lex_state == LexState.EXPR_DOT ||
                        lex_state == LexState.EXPR_CLASS)) {
                        commandStart = true;
                        setState(LexState.EXPR_BEG);
                    }
                    return Tokens.tWHITESPACE;
                }

                switch (lex_state) {
                case EXPR_BEG: case EXPR_FNAME: case EXPR_DOT: case EXPR_CLASS:
                    continue loop;
                }

                commandStart = true;
                setState(LexState.EXPR_BEG);
                return '\n';
            case '*':
                return star(spaceSeen);
            case '!':
                return bang();
            case '=':
                // documentation nodes
                if (src.wasBeginOfLine()) {
                    
                    if (src.matchMarker(BEGIN_DOC_MARKER, false, false)) {
                	SourcePosition startPosition = src.getPosition();
                        if (collectComments()) {
                            tokenBuffer.setLength(0);
                            tokenBuffer.append('=');
                            tokenBuffer.append(BEGIN_DOC_MARKER);
                        }
                        c = src.read();
                        
                        if (Character.isWhitespace(c)) {
                            // In case last next was the newline.
                            src.unread(c);
                            for (;;) {
                                c = src.read();
                                if (collectComments()) tokenBuffer.append(c);

                                // If a line is followed by a blank line put
                                // it back.
                                while (c == '\n') {
                                    c = src.read();
                                    if (collectComments()) tokenBuffer.append(c);
                                }
                                if (c == EOF) {
                                    throw new SyntaxException(PID.STRING_HITS_EOF, getPosition(), 
                                            getCurrentLine(), "embedded document meets end of file");
                                }
                                if (c != '=') continue;
                                if (src.wasBeginOfLine() && src.matchMarker(END_DOC_MARKER, false, false)) {
                                    if (collectComments()) tokenBuffer.append(END_DOC_MARKER);
                                    String list = src.readLineBytes();
                                    if (collectComments()) tokenBuffer.append(list);
                                    src.unread('\n');
                                    break;
                                }
                            }

                            if (collectComments()) {
                        	// Store away each comment to parser result so IDEs can do whatever they want with them.
                        	SourcePosition position = startPosition.union(getPosition());
                                parserSupport.getResult().addSyntax(new CommentNode(position, tokenBuffer.toString()));
                            }
                            if (preserveSpaces) {
                        	yaccValue = new Token("here-doc", getPosition());
                                return Tokens.tDOCUMENTATION;
                            }
                            continue;
                        }
                        src.unread(c);
                    }
                }

                determineExpressionState();

                c = src.read();
                if (c == '=') {
                    c = src.read();
                    if (c == '=') {
                        yaccValue = new Token("===", getPosition());
                        return Tokens.tEQQ;
                    }
                    src.unread(c);
                    yaccValue = new Token("==", getPosition());
                    return Tokens.tEQ;
                }
                if (c == '~') {
                    yaccValue = new Token("=~", getPosition());
                    return Tokens.tMATCH;
                } else if (c == '>') {
                    yaccValue = new Token("=>", getPosition());
                    return Tokens.tASSOC;
                }
                src.unread(c);
                yaccValue = new Token("=", getPosition());
                return '=';
                
            case '<':
                return lessThan(spaceSeen);
            case '>':
                return greaterThan();
            case '"':
                return doubleQuote();
            case '`':
                return backtick(commandState);
            case '\'':
                return singleQuote();
            case '?':
                return questionMark();
            case '&':
                return ampersand(spaceSeen);
            case '|':
                return pipe();
            case '+':
                return plus(spaceSeen);
            case '-':
                return minus(spaceSeen);
            case '.':
                return dot();
            case '0' : case '1' : case '2' : case '3' : case '4' :
            case '5' : case '6' : case '7' : case '8' : case '9' :
                return parseNumber(c);
            case ')':
                return rightParen();
            case ']':
                return rightBracket();
            case '}':
                return rightCurly();
            case ':':
                return colon(spaceSeen);
            case '/':
                return slash(spaceSeen);
            case '^':
                return caret();
            case ';':
                commandStart = true;
                if (!isOneEight) {
                    setState(LexState.EXPR_BEG);
                    yaccValue = new Token(";", getPosition());
                    return ';';
                }
            case ',':
                return comma(c);
            case '~':
                return tilde();
            case '(':
                return leftParen(spaceSeen);
            case '[':
                return leftBracket(spaceSeen);
            case '{':
            	return leftCurly();
            case '\\':
                c = src.read();
                if (c == '\n') {
                    spaceSeen = true;
                    continue;
                }
                src.unread(c);
                yaccValue = new Token("\\", getPosition());
                return '\\';
            case '%':
                return percent(spaceSeen);
            case '$':
                return dollar();
            case '@':
                return at();
            case '_':
                if (src.wasBeginOfLine() && src.matchMarker(END_MARKER, false, true)) {
                	if (parserSupport != null) parserSupport.getResult().setEndOffset(src.getOffset());
                    return EOF;
                }
                return identifier(c, commandState);
            default:
                return identifier(c, commandState);
            }
        }
    }

    private int identifierToken(LexState last_state, int result, String value) {

        // FIXME: Parsersupport should always be hooked up.  No need for null check
        if (result == Tokens.tIDENTIFIER && last_state != LexState.EXPR_DOT &&
                parserSupport != null && parserSupport.getCurrentScope().isDefined(value) >= 0) {
            setState(LexState.EXPR_END);
        }

        yaccValue = new Token(value, result, getPosition());
        return result;
    }

    private int getIdentifier(int c) throws IOException {
        do {
            tokenBuffer.append(c);
            /* no special multibyte character handling is needed in Java
             * if (ismbchar(c)) {
                int i, len = mbclen(c)-1;

                for (i = 0; i < len; i++) {
                    c = src.read();
                    tokenBuffer.append(c);
                }
            }*/
            c = src.read();
        } while (isIdentifierChar(c));
        
        return c;
    }
    
    private int ampersand(boolean spaceSeen) throws IOException {
        int c = src.read();
        
        switch (c) {
        case '&':
            setState(LexState.EXPR_BEG);
            if ((c = src.read()) == '=') {
                yaccValue = new Token("&&", getPosition());
                setState(LexState.EXPR_BEG);
                return Tokens.tOP_ASGN;
            }
            src.unread(c);
            yaccValue = new Token("&&", getPosition());
            return Tokens.tANDOP;
        case '=':
            yaccValue = new Token("&", getPosition());
            setState(LexState.EXPR_BEG);
            return Tokens.tOP_ASGN;
        }
        src.unread(c);
        
        //tmpPosition is required because getPosition()'s side effects.
        //if the warning is generated, the getPosition() on line 954 (this line + 18) will create
        //a wrong position if the "inclusive" flag is not set.
        SourcePosition tmpPosition = getPosition();
        if (isSpaceArg(c, spaceSeen)) {
            if (warnings.isVerbose()) warnings.warning(ID.ARGUMENT_AS_PREFIX, tmpPosition, "`&' interpreted as argument prefix", "&");
            c = Tokens.tAMPER;
        } else if (isBEG()) {
            c = Tokens.tAMPER;
        } else {
            c = Tokens.tAMPER2;
        }
        
        determineExpressionState();
        
        yaccValue = new Token("&", tmpPosition);
        return c;
    }
    
    private int at() throws IOException {
        int c = src.read();
        int result;
        tokenBuffer.setLength(0);
        tokenBuffer.append('@');
        if (c == '@') {
            tokenBuffer.append('@');
            c = src.read();
            result = Tokens.tCVAR;
        } else {
            result = Tokens.tIVAR;                    
        }
        
        if (Character.isDigit(c)) {
            if (tokenBuffer.length() == 1) {
                throw new SyntaxException(PID.IVAR_BAD_NAME, getPosition(), 
                        getCurrentLine(), "`@" + c + "' is not allowed as an instance variable name");
            }
            throw new SyntaxException(PID.CVAR_BAD_NAME, getPosition(), 
                    getCurrentLine(), "`@@" + c + "' is not allowed as a class variable name");
        }
        
        if (!isIdentifierChar(c)) {
            src.unread(c);
            yaccValue = new Token("@", getPosition());
            return '@';
        }

        c = getIdentifier(c);
        src.unread(c);

        LexState last_state = lex_state;
        setState(LexState.EXPR_END);

        return identifierToken(last_state, result, tokenBuffer.toString().intern());        
    }
    
    private int backtick(boolean commandState) throws IOException {
        yaccValue = new Token("`", getPosition());

        switch (lex_state) {
        case EXPR_FNAME:
            setState(isOneEight ? LexState.EXPR_END : LexState.EXPR_ENDFN);
            
            return Tokens.tBACK_REF2;
        case EXPR_DOT:
            setState(commandState ? LexState.EXPR_CMDARG : LexState.EXPR_ARG);

            return Tokens.tBACK_REF2;
        default:
            lex_strterm = new StringTerm(str_xquote, '\0', '`');
        
            return Tokens.tXSTRING_BEG;
        }
    }
    
    private int bang() throws IOException {
        int c = src.read();

        if (!isOneEight && (lex_state == LexState.EXPR_FNAME || lex_state == LexState.EXPR_DOT)) {
            setState(LexState.EXPR_ARG);
            if (c == '@') {
                yaccValue = new Token("!",getPosition());
                return Tokens.tBANG;
            }
        } else {
            setState(LexState.EXPR_BEG);
        }
        
        switch (c) {
        case '=':
            yaccValue = new Token("!=",getPosition());
            
            return Tokens.tNEQ;
        case '~':
            yaccValue = new Token("!~",getPosition());
            
            return Tokens.tNMATCH;
        default: // Just a plain bang
            src.unread(c);
            yaccValue = new Token("!",getPosition());
            
            return Tokens.tBANG;
        }
    }
    
    private int caret() throws IOException {
        int c = src.read();
        if (c == '=') {
            setState(LexState.EXPR_BEG);
            yaccValue = new Token("^", getPosition());
            return Tokens.tOP_ASGN;
        }
        
        determineExpressionState();
        
        src.unread(c);
        yaccValue = new Token("^", getPosition());
        return Tokens.tCARET;
    }

    private int colon(boolean spaceSeen) throws IOException {
        int c = src.read();
        
        if (c == ':') {
            if (isBEG() || lex_state == LexState.EXPR_CLASS || (isARG() && spaceSeen)) {
                setState(LexState.EXPR_BEG);
                yaccValue = new Token("::", getPosition());
                return Tokens.tCOLON3;
            }
            setState(LexState.EXPR_DOT);
            yaccValue = new Token(":",getPosition());
            return Tokens.tCOLON2;
        }

        if (isEND() || Character.isWhitespace(c)) {
            src.unread(c);
            setState(LexState.EXPR_BEG);
            yaccValue = new Token(":",getPosition());
            return ':';
        }
        
        switch (c) {
        case '\'':
            lex_strterm = new StringTerm(str_ssym, '\0', c);
            break;
        case '"':
            lex_strterm = new StringTerm(str_dsym, '\0', c);
            break;
        default:
            src.unread(c);
            break;
        }
        
        setState(LexState.EXPR_FNAME);
        yaccValue = new Token(":", getPosition());
        return Tokens.tSYMBEG;
    }

    private int comma(int c) throws IOException {
        setState(LexState.EXPR_BEG);
        yaccValue = new Token(",", getPosition());
        
        return c;
    }
    
    private int doKeyword(LexState state) {
        commandStart = true;

        if (!isOneEight && leftParenBegin > 0 && leftParenBegin == parenNest) {
            leftParenBegin = 0;
            parenNest--;
            return Tokens.kDO_LAMBDA;
        }

        if (conditionState.isInState()) return Tokens.kDO_COND;

        if (state != LexState.EXPR_CMDARG && cmdArgumentState.isInState()) {
            return Tokens.kDO_BLOCK;
        }
        if (state == LexState.EXPR_ENDARG || (!isOneEight && state == LexState.EXPR_BEG)) {
            return Tokens.kDO_BLOCK;
        }
        return Tokens.kDO;
    }
    
    private int dollar() throws IOException {
        LexState last_state = lex_state;
        setState(LexState.EXPR_END);
        int c = src.read();
        
        switch (c) {
        case '_':       /* $_: last read line string */
            c = src.read();
            if (isIdentifierChar(c)) {
                tokenBuffer.setLength(0);
                tokenBuffer.append("$_");
                c = getIdentifier(c);
                src.unread(c);
                last_state = lex_state;
                setState(LexState.EXPR_END);

                return identifierToken(last_state, Tokens.tGVAR, tokenBuffer.toString().intern());
            }
            src.unread(c);
            c = '_';
            
            // fall through
        case '~':       /* $~: match-data */
        case '*':       /* $*: argv */
        case '$':       /* $$: pid */
        case '?':       /* $?: last status */
        case '!':       /* $!: error string */
        case '@':       /* $@: error position */
        case '/':       /* $/: input record separator */
        case '\\':      /* $\: output record separator */
        case ';':       /* $;: field separator */
        case ',':       /* $,: output field separator */
        case '.':       /* $.: last read line number */
        case '=':       /* $=: ignorecase */
        case ':':       /* $:: load path */
        case '<':       /* $<: reading filename */
        case '>':       /* $>: default output handle */
        case '\"':      /* $": already loaded files */
            yaccValue = new Token("$" + (char) c, Tokens.tGVAR, getPosition());
            return Tokens.tGVAR;

        case '-':
            tokenBuffer.setLength(0);
            tokenBuffer.append('$');
            tokenBuffer.append(c);
            c = src.read();
            if (isIdentifierChar(c)) {
                tokenBuffer.append(c);
            } else {
                src.unread(c);
            }
            yaccValue = new Token(tokenBuffer.toString(), Tokens.tGVAR, getPosition());
            /* xxx shouldn't check if valid option variable */
            return Tokens.tGVAR;

        case '&':       /* $&: last match */
        case '`':       /* $`: string before last match */
        case '\'':      /* $': string after last match */
        case '+':       /* $+: string matches last paren. */
            // Explicit reference to these vars as symbols...
            if (last_state == LexState.EXPR_FNAME) {
                yaccValue = new Token("$" + (char) c, Tokens.tGVAR, getPosition());
                return Tokens.tGVAR;
            }
            
            yaccValue = new BackRefNode(getPosition(), c);
            return Tokens.tBACK_REF;

        case '1': case '2': case '3': case '4': case '5': case '6':
        case '7': case '8': case '9':
            tokenBuffer.setLength(0);
            tokenBuffer.append('$');
            do {
                tokenBuffer.append(c);
                c = src.read();
            } while (Character.isDigit(c));
            src.unread(c);
            if (last_state == LexState.EXPR_FNAME) {
                yaccValue = new Token(tokenBuffer.toString(), Tokens.tGVAR, getPosition());
                return Tokens.tGVAR;
            }
            
            yaccValue = new NthRefNode(getPosition(), Integer.parseInt(tokenBuffer.substring(1)));
            return Tokens.tNTH_REF;
        case '0':
            setState(LexState.EXPR_END);

            return identifierToken(last_state, Tokens.tGVAR, ("$" + (char) c).intern());
        default:
            if (!isIdentifierChar(c)) {
                src.unread(c);
                yaccValue = new Token("$", getPosition());
                return '$';
            }
        
            // $blah
            tokenBuffer.setLength(0);
            tokenBuffer.append('$');
            int d = getIdentifier(c);
            src.unread(d);
            last_state = lex_state;
            setState(LexState.EXPR_END);

            return identifierToken(last_state, Tokens.tGVAR, tokenBuffer.toString().intern());
        }
    }
    
    private int dot() throws IOException {
        int c;
        
        setState(LexState.EXPR_BEG);
        if ((c = src.read()) == '.') {
            if ((c = src.read()) == '.') {
                yaccValue = new Token("...", getPosition());
                return Tokens.tDOT3;
            }
            src.unread(c);
            yaccValue = new Token("..", getPosition());
            return Tokens.tDOT2;
        }
        
        src.unread(c);
        if (Character.isDigit(c)) {
            throw new SyntaxException(PID.FLOAT_MISSING_ZERO, getPosition(), 
                    getCurrentLine(), "no .<digit> floating literal anymore; put 0 before dot"); 
        }
        
        setState(LexState.EXPR_DOT);
        yaccValue = new Token(".", getPosition());
        return Tokens.tDOT;
    }
    
    private int doubleQuote() throws IOException {
        lex_strterm = new StringTerm(str_dquote, '\0', '"');
        yaccValue = new Token("\"", getPosition());

        return Tokens.tSTRING_BEG;
    }
    
    private int greaterThan() throws IOException {
        determineExpressionState();

        int c = src.read();

        switch (c) {
        case '=':
            yaccValue = new Token(">=", getPosition());
            
            return Tokens.tGEQ;
        case '>':
            if ((c = src.read()) == '=') {
                setState(LexState.EXPR_BEG);
                yaccValue = new Token(">>", getPosition());
                return Tokens.tOP_ASGN;
            }
            src.unread(c);
            
            yaccValue = new Token(">>", getPosition());
            return Tokens.tRSHFT;
        default:
            src.unread(c);
            yaccValue = new Token(">", getPosition());
            return Tokens.tGT;
        }
    }
    
    private int identifier(int c, boolean commandState) throws IOException {
        if (!isIdentifierChar(c)) {
            String badChar = "\\" + Integer.toOctalString(c & 0xff);
            throw new SyntaxException(PID.CHARACTER_BAD, getPosition(), getCurrentLine(),
                    "Invalid char `" + badChar + "' ('" + (char) c + "') in expression", badChar);
        }
    
        tokenBuffer.setLength(0);
        int first = c;

        // Need to undo newline status after reading too far
        boolean wasNewline = src.wasBeginOfLine();

        c = getIdentifier(c);
        boolean lastBangOrPredicate = false;

        // methods 'foo!' and 'foo?' are possible but if followed by '=' it is relop
        if (c == '!' || c == '?') {
            if (!src.peek('=')) {
                lastBangOrPredicate = true;
                tokenBuffer.append(c);
            } else {
                src.unread(c);
            }
        } else {
            src.unread(c);
        }
        
        src.setIsANewLine(wasNewline);

        int result = 0;

        LexState last_state = lex_state;
        if (lastBangOrPredicate) {
            result = Tokens.tFID;
        } else {
            if (lex_state == LexState.EXPR_FNAME) {
                if ((c = src.read()) == '=') { 
                    int c2 = src.read();

                    if (c2 != '~' && c2 != '>' &&
                            (c2 != '=' || (c2 == '\n' && src.peek('>')))) {
                        result = Tokens.tIDENTIFIER;
                        tokenBuffer.append(c);
                        src.unread(c2);
                    } else { 
                        src.unread(c2);
                        src.unread(c);
                    }
                } else {
                    src.unread(c);
                }
            }
            if (result == 0 && Character.isUpperCase(first)) {
                result = Tokens.tCONSTANT;
            } else {
                result = Tokens.tIDENTIFIER;
            }
        }

        String tempVal = tokenBuffer.toString().intern();

        if (!isOneEight && ((lex_state == LexState.EXPR_BEG && !commandState) ||
                lex_state == LexState.EXPR_ARG || lex_state == LexState.EXPR_CMDARG)) {
            int c2 = src.read();
            if (c2 == ':' && !src.peek(':')) {
                src.unread(c2);
                setState(LexState.EXPR_BEG);
                src.read();
                yaccValue = new Token(tempVal, Tokens.tLABEL, getPosition());
                return Tokens.tLABEL;
            }
            src.unread(c2);
        }

        if (lex_state != LexState.EXPR_DOT) {
            Keyword keyword = getKeyword(tempVal); // Is it is a keyword?
            
            if (keyword != null && (keyword != Keyword.__ENCODING__ || !isOneEight)) {
                LexState state = lex_state; // Save state at time keyword is encountered

                if (!isOneEight && keyword == Keyword.NOT) {
                    setState(LexState.EXPR_ARG);
                } else {
                    setState(keyword.state);
                }
                if (state == LexState.EXPR_FNAME) {
                    yaccValue = new Token(keyword.name, getPosition());
                } else {
                    yaccValue = new Token(tempVal, getPosition());
                    if (keyword.id0 == Tokens.kDO) return doKeyword(state);
                }

                if (state == LexState.EXPR_BEG || (!isOneEight && state == LexState.EXPR_VALUE)) return keyword.id0;

                if (keyword.id0 != keyword.id1) setState(LexState.EXPR_BEG);

                return keyword.id1;
            }
        }

        if (isBEG() || lex_state == LexState.EXPR_DOT || isARG()) {
            setState(commandState ? LexState.EXPR_CMDARG : LexState.EXPR_ARG);
        } else if (!isOneEight && lex_state == LexState.EXPR_ENDFN) {
            setState(LexState.EXPR_ENDFN);
        } else {
            setState(LexState.EXPR_END);
        }
        
        return identifierToken(last_state, result, tempVal);
    }

    private int leftBracket(boolean spaceSeen) throws IOException {
        parenNest++;
        int c = '[';
        if (lex_state == LexState.EXPR_FNAME || lex_state == LexState.EXPR_DOT) {
            setState(LexState.EXPR_ARG);
            
            if ((c = src.read()) == ']') {
                if (src.peek('=')) {
                    c = src.read();
                    yaccValue = new Token("[]=", getPosition());
                    return Tokens.tASET;
                }
                yaccValue = new Token("[]", getPosition());
                return Tokens.tAREF;
            }
            src.unread(c);
            yaccValue = new Token("[", getPosition());
            return '[';
        } else if (isBEG() || (isARG() && spaceSeen)) {
            c = Tokens.tLBRACK;
        }

        setState(LexState.EXPR_BEG);
        conditionState.stop();
        cmdArgumentState.stop();
        yaccValue = new Token("[", getPosition());
        return c;
    }
    
    private int leftCurly() {
        if (!isOneEight && leftParenBegin > 0 && leftParenBegin == parenNest) {
            setState(LexState.EXPR_BEG);
            leftParenBegin = 0;
            parenNest--;
            conditionState.stop();
            cmdArgumentState.stop();
            yaccValue = new Token("{", getPosition());
            return Tokens.tLAMBEG;
        }

        char c;
        if (isARG() || lex_state == LexState.EXPR_END || (!isOneEight && lex_state == LexState.EXPR_ENDFN)) { // block (primary)
            c = Tokens.tLCURLY;
        } else if (lex_state == LexState.EXPR_ENDARG) { // block (expr)
            c = Tokens.tLBRACE_ARG;
        } else { // hash
            c = Tokens.tLBRACE;
        }

        conditionState.stop();
        cmdArgumentState.stop();
        setState(LexState.EXPR_BEG);
        
        yaccValue = new Token("{", getPosition());
        if (!isOneEight && c != Tokens.tLBRACE) commandStart = true;
        return c;
    }

    private int leftParen(boolean spaceSeen) throws IOException {
        if (isOneEight) commandStart = true;

        int result = Tokens.tLPAREN2;
        if (isBEG()) {
            result = Tokens.tLPAREN;
        } else if (spaceSeen) {
            // ENEBO: 1.9 is IS_ARG, but we need to break apart for 1.8 support.
            if (lex_state == LexState.EXPR_CMDARG) {
                result = Tokens.tLPAREN_ARG;
            } else if (lex_state == LexState.EXPR_ARG) {
                if (isOneEight) {
                    warnings.warn(ID.ARGUMENT_EXTRA_SPACE, getPosition(), "don't put space before argument parentheses");
                    result = Tokens.tLPAREN2;
                } else {
                    result = Tokens.tLPAREN_ARG;
                }
            }
        }

        parenNest++;
        conditionState.stop();
        cmdArgumentState.stop();
        setState(LexState.EXPR_BEG);
        
        yaccValue = new Token("(", getPosition());
        return result;
    }
    
    private int lessThan(boolean spaceSeen) throws IOException {
        int c = src.read();
        if (c == '<' && lex_state != LexState.EXPR_DOT && lex_state != LexState.EXPR_CLASS &&
                !isEND() && (!isARG() || spaceSeen)) {
            int tok = hereDocumentIdentifier();
            
            if (tok != 0) return tok;
        }
        
        determineExpressionState();
        
        switch (c) {
        case '=':
            if ((c = src.read()) == '>') {
                yaccValue = new Token("<=>", getPosition());
                return Tokens.tCMP;
            }
            src.unread(c);
            yaccValue = new Token("<=", getPosition());
            return Tokens.tLEQ;
        case '<':
            if ((c = src.read()) == '=') {
                setState(LexState.EXPR_BEG);
                yaccValue = new Token("<<", getPosition());
                return Tokens.tOP_ASGN;
            }
            src.unread(c);
            yaccValue = new Token("<<", getPosition());
            return Tokens.tLSHFT;
        default:
            yaccValue = new Token("<", getPosition());
            src.unread(c);
            return Tokens.tLT;
        }
    }
    
    private int minus(boolean spaceSeen) throws IOException {
        int c = src.read();
        
        if (lex_state == LexState.EXPR_FNAME || lex_state == LexState.EXPR_DOT) {
            setState(LexState.EXPR_ARG);
            if (c == '@') {
                yaccValue = new Token("-@", getPosition());
                return Tokens.tUMINUS;
            }
            src.unread(c);
            yaccValue = new Token("-", getPosition());
            return Tokens.tMINUS;
        }
        if (c == '=') {
            setState(LexState.EXPR_BEG);
            yaccValue = new Token("-", getPosition());
            return Tokens.tOP_ASGN;
        }
        if (!isOneEight && c == '>') {
            setState(LexState.EXPR_ARG);
            yaccValue = new Token("->", getPosition());
            return Tokens.tLAMBDA;
        }
        if (isBEG() || isSpaceArg(c, spaceSeen)) {
            if (isARG()) arg_ambiguous();
            setState(LexState.EXPR_BEG);
            src.unread(c);
            yaccValue = new Token("-", getPosition());
            if (Character.isDigit(c)) {
                return Tokens.tUMINUS_NUM;
            }
            return Tokens.tUMINUS;
        }
        setState(LexState.EXPR_BEG);
        src.unread(c);
        yaccValue = new Token("-", getPosition());
        return Tokens.tMINUS;
    }

    private int percent(boolean spaceSeen) throws IOException {
        if (isBEG()) return parseQuote(src.read());

        int c = src.read();

        if (c == '=') {
            setState(LexState.EXPR_BEG);
            yaccValue = new Token("%", getPosition());
            return Tokens.tOP_ASGN;
        }
        
        if (isSpaceArg(c, spaceSeen)) return parseQuote(c);
        
        determineExpressionState();
        
        src.unread(c);
        yaccValue = new Token("%", getPosition());
        return Tokens.tPERCENT;
    }

    private int pipe() throws IOException {
        int c = src.read();
        
        switch (c) {
        case '|':
            setState(LexState.EXPR_BEG);
            if ((c = src.read()) == '=') {
                setState(LexState.EXPR_BEG);
                yaccValue = new Token("||", getPosition());
                return Tokens.tOP_ASGN;
            }
            src.unread(c);
            yaccValue = new Token("||", getPosition());
            return Tokens.tOROP;
        case '=':
            setState(LexState.EXPR_BEG);
            yaccValue = new Token("|", getPosition());
            return Tokens.tOP_ASGN;
        default:
            determineExpressionState();
            
            src.unread(c);
            yaccValue = new Token("|", getPosition());
            return Tokens.tPIPE;
        }
    }
    
    private int plus(boolean spaceSeen) throws IOException {
        int c = src.read();
        if (lex_state == LexState.EXPR_FNAME || lex_state == LexState.EXPR_DOT) {
            setState(LexState.EXPR_ARG);
            if (c == '@') {
                yaccValue = new Token("+@", getPosition());
                return Tokens.tUPLUS;
            }
            src.unread(c);
            yaccValue = new Token("+", getPosition());
            return Tokens.tPLUS;
        }
        
        if (c == '=') {
            setState(LexState.EXPR_BEG);
            yaccValue = new Token("+", getPosition());
            return Tokens.tOP_ASGN;
        }
        
        if (isBEG() || isSpaceArg(c, spaceSeen)) {
            if (isARG()) arg_ambiguous();
            setState(LexState.EXPR_BEG);
            src.unread(c);
            if (Character.isDigit(c)) {
                c = '+';
                return parseNumber(c);
            }
            yaccValue = new Token("+", getPosition());
            return Tokens.tUPLUS;
        }
        
        setState(LexState.EXPR_BEG);
        src.unread(c);
        yaccValue = new Token("+", getPosition());
        return Tokens.tPLUS;
    }
    
    private int questionMark() throws IOException {
        int c;
        
        if (isEND()) {
            setState(isOneEight ? LexState.EXPR_BEG : LexState.EXPR_VALUE);
            yaccValue = new Token("?",getPosition());
            return '?';
        }
        
        c = src.read();
        if (c == EOF) throw new SyntaxException(PID.INCOMPLETE_CHAR_SYNTAX, getPosition(), 
                getCurrentLine(), "incomplete character syntax");

        if (Character.isWhitespace(c)){
            if (!isARG()) {
                int c2 = 0;
                switch (c) {
                case ' ':
                    c2 = 's';
                    break;
                case '\n':
                    c2 = 'n';
                    break;
                case '\t':
                    c2 = 't';
                    break;
                        /* What is \v in C?
                    case '\v':
                        c2 = 'v';
                        break;
                        */
                case '\r':
                    c2 = 'r';
                    break;
                case '\f':
                    c2 = 'f';
                    break;
                }
                if (c2 != 0) {
                    warnings.warn(ID.INVALID_CHAR_SEQUENCE, getPosition(), "invalid character syntax; use ?\\" + c2);
                }
            }
            src.unread(c);
            setState(isOneEight ? LexState.EXPR_BEG : LexState.EXPR_VALUE);
            yaccValue = new Token("?", getPosition());
            return '?';
            /*} else if (ismbchar(c)) { // ruby - we don't support them either?
                rb_warn("multibyte character literal not supported yet; use ?\\" + c);
                support.unread(c);
                lexState = LexState.EXPR_BEG;
                return '?';*/
        } else if (isIdentifierChar(c) && !src.peek('\n') && isNext_identchar()) {
            src.unread(c);
            setState(isOneEight ? LexState.EXPR_BEG : LexState.EXPR_VALUE);
            yaccValue = new Token("?", getPosition());
            return '?';
        } else if (c == '\\') {
            // FIXME: peek('u') utf8 stuff for 1.9
            c = readEscape();
        }
        
        setState(LexState.EXPR_END);
        if (isOneEight) {
            c &= 0xff;
            yaccValue = new FixnumNode(getPosition(), c);
        } else {
            String oneCharBL = "" + (char) c;
            yaccValue = new StrNode(getPosition(), oneCharBL);
        }
        // TODO: This should be something else like a tCHAR
        return Tokens.tINTEGER;
    }
    
    private int rightBracket() {
        parenNest--;
        conditionState.restart();
        cmdArgumentState.restart();
        setState(isOneEight ? LexState.EXPR_END : LexState.EXPR_ENDARG);
        yaccValue = new Token(")", getPosition());
        return Tokens.tRBRACK;
    }

    private int rightCurly() {
        conditionState.restart();
        cmdArgumentState.restart();
        setState(isOneEight ? LexState.EXPR_END : LexState.EXPR_ENDARG);
        yaccValue = new Token("}",getPosition());
        return Tokens.tRCURLY;
    }

    private int rightParen() {
        parenNest--;
        conditionState.restart();
        cmdArgumentState.restart();
        setState(isOneEight ? LexState.EXPR_END : LexState.EXPR_ENDFN);
        yaccValue = new Token(")", getPosition());
        return Tokens.tRPAREN;
    }
    
    private int singleQuote() throws IOException {
        lex_strterm = new StringTerm(str_squote, '\0', '\'');
        yaccValue = new Token("'", getPosition());

        return Tokens.tSTRING_BEG;
    }
    
    private int slash(boolean spaceSeen) throws IOException {
        if (isBEG()) {
            lex_strterm = new StringTerm(str_regexp, '\0', '/');
            yaccValue = new Token("/",getPosition());
            return Tokens.tREGEXP_BEG;
        }
        
        int c = src.read();
        
        if (c == '=') {
            yaccValue = new Token("/", getPosition());
            setState(LexState.EXPR_BEG);
            return Tokens.tOP_ASGN;
        }
        src.unread(c);
        if (isSpaceArg(c, spaceSeen)) {
            arg_ambiguous();
            lex_strterm = new StringTerm(str_regexp, '\0', '/');
            yaccValue = new Token("/",getPosition());
            return Tokens.tREGEXP_BEG;
        }
        
        determineExpressionState();
        
        yaccValue = new Token("/", getPosition());
        return Tokens.tDIVIDE;
    }

    private int star(boolean spaceSeen) throws IOException {
        int c = src.read();
        
        switch (c) {
        case '*':
            if ((c = src.read()) == '=') {
                setState(LexState.EXPR_BEG);
                yaccValue = new Token("**", getPosition());
                return Tokens.tOP_ASGN;
            }
            src.unread(c);
            yaccValue = new Token("**", getPosition());
            
            if (isTwoZero && isSpaceArg(c, spaceSeen)) {
                if (warnings.isVerbose()) warnings.warning(ID.ARGUMENT_AS_PREFIX, getPosition(), "`**' interpreted as argument prefix");
                c = Tokens.tDSTAR;
            } else if (isTwoZero && isBEG()) {
                c = Tokens.tDSTAR;
            } else {
                //if (!isOneEight) warn_balanced(c, spaceSeen, "*", "argument prefix");
                c = Tokens.tPOW;
            }
            break;
        case '=':
            setState(LexState.EXPR_BEG);
            yaccValue = new Token("*", getPosition());
            return Tokens.tOP_ASGN;
        default:
            src.unread(c);
            if (isSpaceArg(c, spaceSeen)) {
                if (warnings.isVerbose()) warnings.warning(ID.ARGUMENT_AS_PREFIX, getPosition(), "`*' interpreted as argument prefix", "*");
                c = Tokens.tSTAR;
            } else if (isBEG()) {
                c = Tokens.tSTAR;
            } else {
                c = Tokens.tSTAR2;
            }
            yaccValue = new Token("*", getPosition());
        }
        
        determineExpressionState();
        return c;
    }

    private int tilde() throws IOException {
        int c;
        
        if (lex_state == LexState.EXPR_FNAME || lex_state == LexState.EXPR_DOT) {
            if ((c = src.read()) != '@') src.unread(c);
            setState(LexState.EXPR_ARG);
        } else {
            setState(LexState.EXPR_BEG);
        }
        
        yaccValue = new Token("~", getPosition());
        return Tokens.tTILDE;
    }

    /**
     *  Parse a number from the input stream.
     *
     *@param c The first character of the number.
     *@return A int constant wich represents a token.
     */
    private int parseNumber(int c) throws IOException {
        setState(LexState.EXPR_END);

        tokenBuffer.setLength(0);

        if (c == '-') {
        	tokenBuffer.append(c);
            c = src.read();
        } else if (c == '+') {
        	// We don't append '+' since Java number parser gets confused
            c = src.read();
        }
        
        int nondigit = 0;

        if (c == '0') {
            int startLen = tokenBuffer.length();

            switch (c = src.read()) {
                case 'x' :
                case 'X' : //  hexadecimal
                    c = src.read();
                    if (isHexChar(c)) {
                        for (;; c = src.read()) {
                            if (c == '_') {
                                if (nondigit != '\0') break;
                                nondigit = c;
                            } else if (isHexChar(c)) {
                                nondigit = '\0';
                                tokenBuffer.append(c);
                            } else {
                                break;
                            }
                        }
                    }
                    src.unread(c);

                    if (tokenBuffer.length() == startLen) {
                        throw new SyntaxException(PID.BAD_HEX_NUMBER, getPosition(), 
                                getCurrentLine(), "Hexadecimal number without hex-digits.");
                    } else if (nondigit != '\0') {
                        throw new SyntaxException(PID.TRAILING_UNDERSCORE_IN_NUMBER, getPosition(),
                                getCurrentLine(), "Trailing '_' in number.");
                    }
                    yaccValue = getInteger(tokenBuffer.toString(), 16);
                    return Tokens.tINTEGER;
                case 'b' :
                case 'B' : // binary
                    c = src.read();
                    if (c == '0' || c == '1') {
                        for (;; c = src.read()) {
                            if (c == '_') {
                                if (nondigit != '\0') break;
								nondigit = c;
                            } else if (c == '0' || c == '1') {
                                nondigit = '\0';
                                tokenBuffer.append(c);
                            } else {
                                break;
                            }
                        }
                    }
                    src.unread(c);

                    if (tokenBuffer.length() == startLen) {
                        throw new SyntaxException(PID.EMPTY_BINARY_NUMBER, getPosition(), 
                                getCurrentLine(), "Binary number without digits.");
                    } else if (nondigit != '\0') {
                        throw new SyntaxException(PID.TRAILING_UNDERSCORE_IN_NUMBER,
                                getPosition(), getCurrentLine(), "Trailing '_' in number.");
                    }
                    yaccValue = getInteger(tokenBuffer.toString(), 2);
                    return Tokens.tINTEGER;
                case 'd' :
                case 'D' : // decimal
                    c = src.read();
                    if (Character.isDigit(c)) {
                        for (;; c = src.read()) {
                            if (c == '_') {
                                if (nondigit != '\0') break;
								nondigit = c;
                            } else if (Character.isDigit(c)) {
                                nondigit = '\0';
                                tokenBuffer.append(c);
                            } else {
                                break;
                            }
                        }
                    }
                    src.unread(c);

                    if (tokenBuffer.length() == startLen) {
                        throw new SyntaxException(PID.EMPTY_BINARY_NUMBER, getPosition(),
                                getCurrentLine(), "Binary number without digits.");
                    } else if (nondigit != '\0') {
                        throw new SyntaxException(PID.TRAILING_UNDERSCORE_IN_NUMBER,
                                getPosition(), getCurrentLine(), "Trailing '_' in number.");
                    }
                    yaccValue = getInteger(tokenBuffer.toString(), 10);
                    return Tokens.tINTEGER;
                case 'o':
                case 'O':                    
                    c = src.read();
                case '0': case '1': case '2': case '3': case '4': //Octal
                case '5': case '6': case '7': case '_': 
                    for (;; c = src.read()) {
                        if (c == '_') {
                            if (nondigit != '\0') break;

							nondigit = c;
                        } else if (c >= '0' && c <= '7') {
                            nondigit = '\0';
                            tokenBuffer.append(c);
                        } else {
                            break;
                        }
                    }
                    if (tokenBuffer.length() > startLen) {
                        src.unread(c);

                        if (nondigit != '\0') {
                            throw new SyntaxException(PID.TRAILING_UNDERSCORE_IN_NUMBER, getPosition(), 
                                    getCurrentLine(), "Trailing '_' in number.");
                        }

                        yaccValue = getInteger(tokenBuffer.toString(), 8);
                        return Tokens.tINTEGER;
                    }
                case '8' :
                case '9' :
                    throw new SyntaxException(PID.BAD_OCTAL_DIGIT, getPosition(),
                            getCurrentLine(), "Illegal octal digit.");
                case '.' :
                case 'e' :
                case 'E' :
                	tokenBuffer.append('0');
                    break;
                default :
                    src.unread(c);
                    yaccValue = new FixnumNode(getPosition(), 0);
                    return Tokens.tINTEGER;
            }
        }

        boolean seen_point = false;
        boolean seen_e = false;

        for (;; c = src.read()) {
            switch (c) {
                case '0' :
                case '1' :
                case '2' :
                case '3' :
                case '4' :
                case '5' :
                case '6' :
                case '7' :
                case '8' :
                case '9' :
                    nondigit = '\0';
                    tokenBuffer.append(c);
                    break;
                case '.' :
                    if (nondigit != '\0') {
                        src.unread(c);
                        throw new SyntaxException(PID.TRAILING_UNDERSCORE_IN_NUMBER, getPosition(),
                                getCurrentLine(), "Trailing '_' in number.");
                    } else if (seen_point || seen_e) {
                        src.unread(c);
                        return getNumberToken(tokenBuffer.toString(), true, nondigit);
                    } else {
                    	int c2;
                        if (!Character.isDigit(c2 = src.read())) {
                            src.unread(c2);
                        	src.unread('.');
                            if (c == '_') { 
                            		// Enebo:  c can never be antrhign but '.'
                            		// Why did I put this here?
                            } else {
                                yaccValue = getInteger(tokenBuffer.toString(), 10);
                                return Tokens.tINTEGER;
                            }
                        } else {
                            tokenBuffer.append('.');
                            tokenBuffer.append(c2);
                            seen_point = true;
                            nondigit = '\0';
                        }
                    }
                    break;
                case 'e' :
                case 'E' :
                    if (nondigit != '\0') {
                        throw new SyntaxException(PID.TRAILING_UNDERSCORE_IN_NUMBER,
                                getPosition(), getCurrentLine(), "Trailing '_' in number.");
                    } else if (seen_e) {
                        src.unread(c);
                        return getNumberToken(tokenBuffer.toString(), true, nondigit);
                    } else {
                        tokenBuffer.append(c);
                        seen_e = true;
                        nondigit = c;
                        c = src.read();
                        if (c == '-' || c == '+') {
                            tokenBuffer.append(c);
                            nondigit = c;
                        } else {
                            src.unread(c);
                        }
                    }
                    break;
                case '_' : //  '_' in number just ignored
                    if (nondigit != '\0') {
                        throw new SyntaxException(PID.TRAILING_UNDERSCORE_IN_NUMBER,
                                getPosition(), getCurrentLine(), "Trailing '_' in number.");
                    }
                    nondigit = c;
                    break;
                default :
                    src.unread(c);
                return getNumberToken(tokenBuffer.toString(), seen_e || seen_point, nondigit);
            }
        }
    }

    private int getNumberToken(String number, boolean isFloat, int nondigit) {
        if (nondigit != '\0') {
            throw new SyntaxException(PID.TRAILING_UNDERSCORE_IN_NUMBER,
                    getPosition(), getCurrentLine(), "Trailing '_' in number.");
        } else if (isFloat) {
            return getFloatToken(number);
        }
        yaccValue = getInteger(number, 10);
        return Tokens.tINTEGER;
    }
    
    // Note: parser_tokadd_utf8 variant just for regexp literal parsing.  This variant is to be
    // called when string_literal and regexp_literal.
    public void readUTFEscapeRegexpLiteral(CStringBuilder buffer) throws IOException {
        buffer.append('\\');
        buffer.append('u');

        if (src.peek('{')) { // handle \\u{...}
            do {
                buffer.append(src.read());
                if (scanHexLiteral(buffer, 6, false, "invalid Unicode escape") > 0x10ffff) {
                    throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                            getCurrentLine(), "invalid Unicode codepoint (too large)");
                }
            } while (src.peek(' ') || src.peek('\t'));

            int c = src.read();
            if (c != '}') {
                throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                        getCurrentLine(), "unterminated Unicode escape");
            }
            buffer.append((char) c);
        } else { // handle \\uxxxx
            scanHexLiteral(buffer, 4, true, "Invalid Unicode escape");
        }
    }

    private byte[] mbcBuf = new byte[6];

    //FIXME: This seems like it could be more efficient to ensure size in bytelist and then pass
    // in bytelists byte backing store.  This method would look ugly since realSize would need
    // to be tweaked and I don't know how many bytes this codepoint has up front so I would need
    // to grow by 6 (which may be wasteful).  Another idea is to make Encoding accept an interface
    // for populating bytes and then make ByteList implement that interface.  I like this last idea
    // since it would not leak bytelist impl details all over the place.
    public int tokenAddMBC(int codepoint, CStringBuilder buffer) {
//        int length = buffer.getEncoding().codeToMbc(codepoint, mbcBuf, 0);

  //      if (length <= 0) return EOF;

//        buffer.append(mbcBuf, 0, length);
        buffer.append(codepoint);

//        return length;
        return 1;
    }

    public void tokenAddMBCFromSrc(int c, CStringBuilder buffer) throws IOException {
        // read bytes for length of character
        int length = 1; //buffer.getEncoding().length((byte)c);
        buffer.append((char)c);
//        for (int off = 0; off < length - 1; off++) {
//            buffer.append((byte)src.read());
//        }
    }

    // MRI: parser_tokadd_utf8 sans regexp literal parsing
    public int readUTFEscape(CStringBuilder buffer, boolean stringLiteral, boolean symbolLiteral) throws IOException {
        int codepoint;
        int c;

        if (src.peek('{')) { // handle \\u{...}
            do {
                src.read(); // Eat curly or whitespace
                codepoint = scanHex(6, false, "invalid Unicode escape");
                if (codepoint > 0x10ffff) {
                    throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                            getCurrentLine(), "invalid Unicode codepoint (too large)");
                }

                if (codepoint >= 0x80) {
//                    buffer.setEncoding("UTF-8");
                    if (stringLiteral) tokenAddMBC(codepoint, buffer);
                } else if (stringLiteral) {
                    if (codepoint == 0 && symbolLiteral) {
                        throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                            getCurrentLine(), "symbol cannot contain '\\u0000'");
                    }

                    buffer.append((char) codepoint);
                }
            } while (src.peek(' ') || src.peek('\t'));

            c = src.read();
            if (c != '}') {
                throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                        getCurrentLine(), "unterminated Unicode escape");
            }
        } else { // handle \\uxxxx
            codepoint = scanHex(4, true, "Invalid Unicode escape");
            if (codepoint >= 0x80) {
//                buffer.setEncoding("UTF-8");
                if (stringLiteral) tokenAddMBC(codepoint, buffer);
            } else if (stringLiteral) {
                if (codepoint == 0 && symbolLiteral) {
                    throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                        getCurrentLine(), "symbol cannot contain '\\u0000'");
                }

                buffer.append((char) codepoint);
            }
        }

        return codepoint;
    }
    
    public int readEscape() throws IOException {
        int c = src.read();

        switch (c) {
            case '\\' : // backslash
                return c;
            case 'n' : // newline
                return '\n';
            case 't' : // horizontal tab
                return '\t';
            case 'r' : // carriage return
                return '\r';
            case 'f' : // form feed
                return '\f';
            case 'v' : // vertical tab
                return '\u000B';
            case 'a' : // alarm(bell)
                return '\u0007';
            case 'e' : // escape
                return '\u001B';
            case '0' : case '1' : case '2' : case '3' : // octal constant
            case '4' : case '5' : case '6' : case '7' :
                src.unread(c);
                return scanOct(3);
            case 'x' : // hex constant
                int i = 0;
                //char hexValue = scanHex(2);

                char hexValue = '\0';

                for (; i < 2; i++) {
                    int h1 = src.read();

                    if (!isHexChar(h1)) {
                        src.unread(h1);
                        break;
                    }

                    hexValue <<= 4;
                    hexValue |= Integer.parseInt(""+(char)h1, 16) & 15;
                }
                
                // No hex value after the 'x'.
                if (i == 0) {
                    throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(), 
                            getCurrentLine(), "Invalid escape character syntax");
                }
                return hexValue;
            case 'b' : // backspace
                return '\010';
            case 's' : // space
                return ' ';
            case 'M' :
                if ((c = src.read()) != '-') {
                    throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                            getCurrentLine(), "Invalid escape character syntax");
                } else if ((c = src.read()) == '\\') {
                    return (char) (readEscape() | 0x80);
                } else if (c == EOF) {
                    throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                            getCurrentLine(), "Invalid escape character syntax");
                } 
                return (char) ((c & 0xff) | 0x80);
            case 'C' :
                if ((c = src.read()) != '-') {
                    throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                            getCurrentLine(), "Invalid escape character syntax");
                }
            case 'c' :
                if ((c = src.read()) == '\\') {
                    c = readEscape();
                } else if (c == '?') {
                    return '\u0177';
                } else if (c == EOF) {
                    throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                            getCurrentLine(), "Invalid escape character syntax");
                }
                return (char) (c & 0x9f);
            case EOF :
                throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                        getCurrentLine(), "Invalid escape character syntax");
            default :
                return c;
        }
    }

    /**
     * Read up to count hexadecimal digits and store those digits in a token buffer.  If strict is
     * provided then count number of hex digits must be present. If no digits can be read a syntax
     * exception will be thrown.  This will also return the codepoint as a value so codepoint
     * ranges can be checked.
     */
    private char scanHexLiteral(CStringBuilder buffer, int count, boolean strict, String errorMessage)
            throws IOException {
        int i = 0;
        char hexValue = '\0';

        for (; i < count; i++) {
            int h1 = src.read();

            if (!isHexChar(h1)) {
                src.unread(h1);
                break;
            }

            buffer.append(h1);

            hexValue <<= 4;
            hexValue |= Integer.parseInt("" + (char) h1, 16) & 15;
        }

        // No hex value after the 'x'.
        if (i == 0 || strict && count != i) {
            throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                    getCurrentLine(), errorMessage);
        }

        return hexValue;
    }

    /**
     * Read up to count hexadecimal digits.  If strict is provided then count number of hex
     * digits must be present. If no digits can be read a syntax exception will be thrown.
     */
    private int scanHex(int count, boolean strict, String errorMessage) throws IOException {
        int i = 0;
        int hexValue = '\0';

        for (; i < count; i++) {
            int h1 = src.read();

            if (!isHexChar(h1)) {
                src.unread(h1);
                break;
            }

            hexValue <<= 4;
            hexValue |= Integer.parseInt("" + (char) h1, 16) & 15;
        }

        // No hex value after the 'x'.
        if (i == 0 || (strict && count != i)) {
            throw new SyntaxException(PID.INVALID_ESCAPE_SYNTAX, getPosition(),
                    getCurrentLine(), errorMessage);
        }

        return hexValue;
    }
    
    private char scanOct(int count) throws IOException {
        char value = '\0';

        for (int i = 0; i < count; i++) {
            int c = src.read();

            if (!isOctChar(c)) {
                src.unread(c);
                break;
            }

            value <<= 3;
            value |= Integer.parseInt("" + (char) c, 8);
        }

        return value;
    }
}
