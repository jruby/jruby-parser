// created by jay 1.0.2 (c) 2002-2004 ats@cs.rit.edu
// skeleton Java 1.0 (c) 2002 ats@cs.rit.edu

					// line 2 "Ruby30Parser.y"
/***** BEGIN LICENSE BLOCK *****
 * Version: EPL 1.0/GPL 2.0/LGPL 2.1
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
 * Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the EPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the EPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jrubyparser.parser;

import java.io.IOException;

import org.jrubyparser.ast.ArgsNode;
import org.jrubyparser.ast.ArrayNode;
import org.jrubyparser.ast.AssignableNode;
import org.jrubyparser.ast.BackRefNode;
import org.jrubyparser.ast.BeginNode;
import org.jrubyparser.ast.BlockAcceptingNode;
import org.jrubyparser.ast.BlockArgNode;
import org.jrubyparser.ast.BlockNode;
import org.jrubyparser.ast.BlockPassNode;
import org.jrubyparser.ast.BreakNode;
import org.jrubyparser.ast.CallNode;
import org.jrubyparser.ast.ClassNode;
import org.jrubyparser.ast.ClassVarNode;
import org.jrubyparser.ast.Colon3Node;
import org.jrubyparser.ast.ConstDeclNode;
import org.jrubyparser.ast.DStrNode;
import org.jrubyparser.ast.DSymbolNode;
import org.jrubyparser.ast.DXStrNode;
import org.jrubyparser.ast.DefinedNode;
import org.jrubyparser.ast.DefnNode;
import org.jrubyparser.ast.DefsNode;
import org.jrubyparser.ast.DotNode;
import org.jrubyparser.ast.EnsureNode;
import org.jrubyparser.ast.EvStrNode;
import org.jrubyparser.ast.FCallNode;
import org.jrubyparser.ast.FixnumNode;
import org.jrubyparser.ast.FloatNode;
import org.jrubyparser.ast.ForNode;
import org.jrubyparser.ast.GlobalVarNode;
import org.jrubyparser.ast.HashNode;
import org.jrubyparser.ast.HashPatternNode;
import org.jrubyparser.ast.ArrayPatternNode;
import org.jrubyparser.ast.FindPatternNode;
import org.jrubyparser.ast.InNode;
import org.jrubyparser.ast.PatternBindNode;
import org.jrubyparser.ast.IfNode;
import org.jrubyparser.ast.ImplicitNilNode;
import org.jrubyparser.ast.InstVarNode;
import org.jrubyparser.ast.IterNode;
import org.jrubyparser.ast.KeywordRestArgNode;
import org.jrubyparser.ast.LambdaNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.LiteralNode;
import org.jrubyparser.ast.MethodNameNode;
import org.jrubyparser.ast.ModuleNode;
import org.jrubyparser.ast.MultipleAsgnNode;
import org.jrubyparser.ast.NextNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.NumericNode;
import org.jrubyparser.ast.OpAsgnAndNode;
import org.jrubyparser.ast.OpAsgnNode;
import org.jrubyparser.ast.OpAsgnOrNode;
import org.jrubyparser.ast.OptArgNode;
import org.jrubyparser.ast.PostExeNode;
import org.jrubyparser.ast.PreExe19Node;
import org.jrubyparser.ast.RationalNode;
import org.jrubyparser.ast.RedoNode;
import org.jrubyparser.ast.RegexpNode;
import org.jrubyparser.ast.RescueBodyNode;
import org.jrubyparser.ast.RescueNode;
import org.jrubyparser.ast.RestArgNode;
import org.jrubyparser.ast.RetryNode;
import org.jrubyparser.ast.ReturnNode;
import org.jrubyparser.ast.SClassNode;
import org.jrubyparser.ast.SelfNode;
import org.jrubyparser.ast.StarNode;
import org.jrubyparser.ast.StrNode;
import org.jrubyparser.ast.SymbolNode;
import org.jrubyparser.ast.UnaryCallNode;
import org.jrubyparser.ast.UnnamedRestArgNode;
import org.jrubyparser.ast.UntilNode;
import org.jrubyparser.ast.VAliasNode;
import org.jrubyparser.ast.WhileNode;
import org.jrubyparser.ast.XStrNode;
import org.jrubyparser.ast.YieldNode;
import org.jrubyparser.ast.ZArrayNode;
import org.jrubyparser.ast.ZSuperNode;
import org.jrubyparser.ast.ZYieldNode;
import org.jrubyparser.ast.ILiteralNode;
import org.jrubyparser.IRubyWarnings;
import org.jrubyparser.IRubyWarnings.ID;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.ISourcePositionHolder;
import org.jrubyparser.lexer.LexerSource;
import org.jrubyparser.lexer.Lexer;
import org.jrubyparser.lexer.Lexer.LexState;
import org.jrubyparser.lexer.StrTerm;
import org.jrubyparser.lexer.SyntaxException;
import org.jrubyparser.lexer.SyntaxException.PID;
import org.jrubyparser.lexer.Token;

public class Ruby30Parser implements RubyParser {
    protected ParserSupport30 support;
    protected Lexer lexer;

    public Ruby30Parser() {
        this(new ParserSupport30());
    }

    public Ruby30Parser(ParserSupport30 support) {
        this.support = support;
        lexer = new Lexer(false);
        lexer.setParserSupport(support);
        support.setLexer(lexer);
    }

    public void setWarnings(IRubyWarnings warnings) {
        support.setWarnings(warnings);
        lexer.setWarnings(warnings);
    }
					// line 147 "-"
  // %token constants
  public static final int kCLASS = 257;
  public static final int kMODULE = 258;
  public static final int kDEF = 259;
  public static final int kUNDEF = 260;
  public static final int kBEGIN = 261;
  public static final int kRESCUE = 262;
  public static final int kENSURE = 263;
  public static final int kEND = 264;
  public static final int kIF = 265;
  public static final int kUNLESS = 266;
  public static final int kTHEN = 267;
  public static final int kELSIF = 268;
  public static final int kELSE = 269;
  public static final int kCASE = 270;
  public static final int kWHEN = 271;
  public static final int kWHILE = 272;
  public static final int kUNTIL = 273;
  public static final int kFOR = 274;
  public static final int kBREAK = 275;
  public static final int kNEXT = 276;
  public static final int kREDO = 277;
  public static final int kRETRY = 278;
  public static final int kIN = 279;
  public static final int kDO = 280;
  public static final int kDO_COND = 281;
  public static final int kDO_BLOCK = 282;
  public static final int kRETURN = 283;
  public static final int kYIELD = 284;
  public static final int kSUPER = 285;
  public static final int kSELF = 286;
  public static final int kNIL = 287;
  public static final int kTRUE = 288;
  public static final int kFALSE = 289;
  public static final int kAND = 290;
  public static final int kOR = 291;
  public static final int kNOT = 292;
  public static final int kIF_MOD = 293;
  public static final int kUNLESS_MOD = 294;
  public static final int kWHILE_MOD = 295;
  public static final int kUNTIL_MOD = 296;
  public static final int kRESCUE_MOD = 297;
  public static final int kALIAS = 298;
  public static final int kDEFINED = 299;
  public static final int klBEGIN = 300;
  public static final int klEND = 301;
  public static final int k__LINE__ = 302;
  public static final int k__FILE__ = 303;
  public static final int k__ENCODING__ = 304;
  public static final int kDO_LAMBDA = 305;
  public static final int tIDENTIFIER = 306;
  public static final int tFID = 307;
  public static final int tGVAR = 308;
  public static final int tIVAR = 309;
  public static final int tCONSTANT = 310;
  public static final int tCVAR = 311;
  public static final int tLABEL = 312;
  public static final int tCHAR = 313;
  public static final int tUPLUS = 314;
  public static final int tUMINUS = 315;
  public static final int tUMINUS_NUM = 316;
  public static final int tPOW = 317;
  public static final int tCMP = 318;
  public static final int tEQ = 319;
  public static final int tEQQ = 320;
  public static final int tNEQ = 321;
  public static final int tGEQ = 322;
  public static final int tLEQ = 323;
  public static final int tANDOP = 324;
  public static final int tOROP = 325;
  public static final int tMATCH = 326;
  public static final int tNMATCH = 327;
  public static final int tDOT = 328;
  public static final int tDOT2 = 329;
  public static final int tDOT3 = 330;
  public static final int tAREF = 331;
  public static final int tASET = 332;
  public static final int tLSHFT = 333;
  public static final int tRSHFT = 334;
  public static final int tCOLON2 = 335;
  public static final int tCOLON3 = 336;
  public static final int tOP_ASGN = 337;
  public static final int tASSOC = 338;
  public static final int tLPAREN = 339;
  public static final int tLPAREN2 = 340;
  public static final int tRPAREN = 341;
  public static final int tLPAREN_ARG = 342;
  public static final int tLBRACK = 343;
  public static final int tRBRACK = 344;
  public static final int tLBRACE = 345;
  public static final int tLBRACE_ARG = 346;
  public static final int tSTAR = 347;
  public static final int tSTAR2 = 348;
  public static final int tAMPER = 349;
  public static final int tAMPER2 = 350;
  public static final int tTILDE = 351;
  public static final int tPERCENT = 352;
  public static final int tDIVIDE = 353;
  public static final int tPLUS = 354;
  public static final int tMINUS = 355;
  public static final int tLT = 356;
  public static final int tGT = 357;
  public static final int tPIPE = 358;
  public static final int tBANG = 359;
  public static final int tCARET = 360;
  public static final int tLCURLY = 361;
  public static final int tRCURLY = 362;
  public static final int tBACK_REF2 = 363;
  public static final int tSYMBEG = 364;
  public static final int tSTRING_BEG = 365;
  public static final int tXSTRING_BEG = 366;
  public static final int tREGEXP_BEG = 367;
  public static final int tWORDS_BEG = 368;
  public static final int tQWORDS_BEG = 369;
  public static final int tSTRING_DBEG = 370;
  public static final int tSTRING_DVAR = 371;
  public static final int tSTRING_END = 372;
  public static final int tLAMBDA = 373;
  public static final int tLAMBEG = 374;
  public static final int tNTH_REF = 375;
  public static final int tBACK_REF = 376;
  public static final int tSTRING_CONTENT = 377;
  public static final int tINTEGER = 378;
  public static final int tFLOAT = 379;
  public static final int tREGEXP_END = 380;
  public static final int tORDER_ALIGNMENT_01 = 381;
  public static final int tORDER_ALIGNMENT_02 = 382;
  public static final int tORDER_ALIGNMENT_03 = 383;
  public static final int tLOWEST = 384;
  public static final int tIMAGINARY = 385;
  public static final int tRATIONAL = 386;
  public static final int tSYMBOLS_BEG = 387;
  public static final int tQSYMBOLS_BEG = 388;
  public static final int tDSTAR = 389;
  public static final int tLABEL_END = 390;
  public static final int tSTRING_DEND = 391;
  public static final int tANDDOT = 392;
  public static final int tBDOT2 = 393;
  public static final int tBDOT3 = 394;
  public static final int yyErrorCode = 256;

  /** number of final state.
    */
  protected static final int yyFinal = 1;

  /** parser tables.
      Order is mandated by <i>jay</i>.
    */
  protected static final short[] yyLhs = {
//yyLhs 729
    -1,   179,     0,   141,   142,   142,   142,   142,   143,   182,
   143,    38,    37,    39,    39,    39,    39,    45,   183,    45,
   184,    40,    40,    40,    40,    40,    40,    40,    40,    40,
    40,    40,    40,    40,    40,    40,    40,    40,    40,    40,
    40,    40,    40,    40,    40,    31,    33,    33,    33,    41,
    41,    41,    41,    41,   186,    41,   187,    41,    41,    46,
    32,    32,    60,    60,   188,   116,   177,    44,    44,    44,
    44,    44,    44,    44,    44,    44,    44,    44,   117,   117,
   128,   128,   118,   118,   118,   118,   118,   118,   118,   118,
   118,   118,    77,    77,   103,   103,   107,   107,    78,    78,
    78,    78,    78,    78,    78,    78,    78,    83,    83,    83,
    83,    83,    83,    83,    83,    83,     6,     6,    30,    30,
    30,     7,     7,     7,     7,     7,   121,   121,   122,   122,
    62,   189,    62,     8,     8,     8,     8,     8,     8,     8,
     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,
     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,
     8,     8,     8,   139,   139,   139,   139,   139,   139,   139,
   139,   139,   139,   139,   139,   139,   139,   139,   139,   139,
   139,   139,   139,   139,   139,   139,   139,   139,   139,   139,
   139,   139,   139,   139,   139,   139,   139,   139,   139,   139,
   139,   139,   139,   139,   139,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    34,    34,    79,    82,    82,
    82,    82,    54,    54,    54,    58,    58,   131,   131,   131,
   131,   131,    52,    52,    52,    52,    52,   191,    56,   110,
   109,   109,    85,    85,    85,    85,    65,    65,    76,    76,
    76,    43,    43,    43,    43,    43,    43,    43,    43,    43,
    43,    43,    43,   192,    43,   193,    43,    43,    43,    43,
    43,    43,    43,    43,    43,    43,    43,    43,    43,    43,
    43,    43,    43,    43,    43,   195,   197,    43,   198,   199,
    43,    43,    43,    43,   200,   201,    43,   202,    43,   204,
   205,    43,   206,    43,    43,    43,    43,    43,    43,    43,
    47,   194,   194,   194,   196,   196,    50,    50,    48,    48,
   130,   130,   132,   132,    90,    90,   133,   133,   133,   133,
   133,   133,   133,   133,   133,    97,    97,    97,    97,    97,
    96,    96,    69,    69,    69,    69,    69,    69,    69,    69,
    69,    69,    69,    69,    69,    69,    69,    71,    71,    70,
    70,    70,   125,   125,   124,   124,   134,   134,   207,   127,
    68,    68,   126,   126,   208,   115,    59,    59,    59,    59,
    59,    23,    23,    23,    23,    23,    23,    23,    23,    23,
   209,   114,   210,   114,    80,    49,    49,   211,   148,   149,
   149,   150,   150,   150,   151,   151,   151,   151,   151,   151,
   152,   153,   153,   154,   154,   174,   174,   175,   175,   155,
   155,   155,   155,   155,   155,   155,   155,   155,   155,   155,
   155,   155,   155,   155,   161,   161,   161,   161,   161,   161,
   161,   161,   165,   165,   162,   162,   166,   166,   163,   164,
   164,   167,   168,   168,   168,   168,   168,   168,   169,   169,
   170,   170,   171,   172,   172,   173,   156,   156,   156,   156,
   156,   156,   156,   156,   156,   156,   157,   157,   157,   157,
   157,   157,   157,   157,   157,   157,   158,   159,   160,   160,
   160,   119,   119,    81,    81,    81,    51,    51,    53,    53,
    28,    28,    28,    15,    16,    16,    16,    17,    18,    19,
    25,    25,    87,    87,    27,    27,    93,    93,    91,    91,
    26,    26,    94,    94,    86,    86,    92,    92,    20,    20,
    21,    21,    24,    24,    22,   212,    22,   213,    22,    63,
    63,    63,    63,     2,     1,     1,     1,     1,    29,    35,
    35,    36,    36,    36,    36,   135,   135,   135,   135,   135,
   136,   136,   136,   136,   136,   136,   136,    57,    57,   120,
   120,    64,    64,    55,   214,    55,    55,    74,   215,    75,
    72,    67,    67,    73,    73,    98,    98,    98,    98,    98,
    95,    95,    66,    66,    66,    66,    66,    66,    66,    66,
    66,    66,    66,    66,    66,    66,    66,    66,    66,   147,
   140,   140,   140,   140,     9,     9,   123,   123,    88,    88,
   176,    99,    99,   100,   100,   101,   101,   102,   102,   144,
   144,   145,   145,   146,    61,   129,   108,   108,    89,    89,
    11,    11,    13,    13,    12,    12,   113,   112,   112,    14,
   216,    14,   106,   106,   104,   104,   105,   105,   105,   105,
     3,     3,     3,     4,     4,     4,     4,     5,     5,     5,
    10,    10,   178,   178,   180,   180,   185,   185,   137,   138,
   190,   190,   190,   203,   203,   181,   181,    84,   111,
    }, yyLen = {
//yyLen 729
     2,     0,     2,     2,     1,     1,     3,     2,     1,     0,
     5,     4,     2,     1,     1,     3,     2,     1,     0,     5,
     0,     4,     3,     3,     3,     2,     3,     3,     3,     3,
     3,     4,     1,     3,     3,     6,     5,     5,     5,     5,
     3,     3,     5,     3,     1,     3,     1,     3,     1,     1,
     3,     3,     3,     2,     0,     4,     0,     4,     1,     1,
     1,     1,     1,     4,     0,     5,     1,     2,     3,     4,
     5,     4,     5,     2,     2,     2,     2,     2,     1,     3,
     1,     3,     1,     2,     3,     5,     2,     4,     2,     4,
     1,     3,     1,     3,     2,     3,     1,     3,     1,     1,
     4,     3,     3,     3,     3,     2,     1,     1,     1,     4,
     3,     3,     3,     3,     2,     1,     1,     1,     2,     1,
     3,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     0,     4,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     3,     3,     6,     5,     5,
     5,     5,     4,     3,     3,     3,     2,     2,     2,     2,
     3,     3,     3,     3,     3,     3,     4,     2,     2,     3,
     3,     3,     3,     3,     3,     3,     3,     3,     3,     3,
     3,     3,     2,     2,     3,     3,     3,     3,     3,     6,
     4,     6,     4,     6,     1,     1,     3,     1,     1,     2,
     4,     2,     3,     3,     5,     1,     1,     1,     1,     2,
     4,     2,     1,     2,     2,     4,     1,     0,     2,     2,
     2,     1,     1,     2,     3,     4,     1,     1,     3,     4,
     2,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     3,     0,     3,     0,     4,     3,     3,     2,
     3,     3,     1,     4,     3,     1,     5,     4,     3,     2,
     1,     2,     2,     6,     6,     0,     0,     7,     0,     0,
     7,     5,     4,     5,     0,     0,     9,     0,     6,     0,
     0,     8,     0,     5,     4,     4,     1,     1,     1,     1,
     1,     1,     1,     2,     1,     1,     1,     5,     1,     2,
     1,     1,     1,     3,     1,     3,     1,     4,     6,     3,
     5,     2,     4,     1,     3,     4,     2,     2,     2,     1,
     2,     0,     6,     8,     4,     6,     4,     2,     6,     2,
     4,     6,     2,     4,     2,     4,     1,     1,     1,     3,
     1,     4,     1,     4,     1,     3,     1,     1,     0,     3,
     4,     1,     3,     3,     0,     5,     2,     2,     4,     5,
     5,     2,     4,     4,     3,     3,     3,     2,     1,     4,
     0,     5,     0,     5,     5,     1,     1,     0,     6,     1,
     1,     1,     3,     3,     1,     2,     3,     1,     1,     1,
     1,     3,     1,     3,     1,     1,     1,     1,     1,     1,
     4,     4,     4,     3,     4,     4,     4,     3,     3,     3,
     2,     3,     2,     3,     1,     1,     2,     3,     5,     2,
     4,     1,     2,     3,     1,     3,     1,     3,     5,     2,
     1,     1,     3,     1,     2,     1,     3,     1,     1,     3,
     2,     1,     1,     2,     1,     2,     1,     3,     3,     2,
     2,     1,     1,     1,     2,     2,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     2,     2,     3,
     1,     6,     0,     1,     1,     1,     2,     1,     2,     1,
     1,     1,     1,     1,     1,     1,     2,     3,     3,     3,
     3,     3,     0,     3,     1,     2,     3,     3,     0,     3,
     3,     3,     3,     3,     0,     3,     0,     3,     0,     2,
     0,     2,     0,     2,     1,     0,     3,     0,     4,     1,
     1,     1,     1,     2,     1,     1,     1,     1,     3,     1,
     2,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     0,     4,     2,     2,     0,     5,
     3,     1,     2,     1,     0,     4,     2,     2,     2,     1,
     2,     0,     6,     8,     4,     6,     4,     6,     2,     4,
     6,     2,     4,     2,     4,     1,     1,     3,     0,     1,
     1,     1,     1,     1,     1,     1,     1,     3,     1,     3,
     1,     2,     1,     2,     1,     1,     3,     1,     3,     1,
     1,     2,     1,     2,     3,     3,     1,     3,     1,     3,
     1,     1,     2,     1,     1,     1,     2,     2,     0,     1,
     0,     4,     1,     2,     1,     3,     3,     2,     4,     2,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     0,     1,     0,     1,     2,     2,
     0,     1,     1,     1,     1,     1,     2,     0,     0,
    }, yyDefRed = {
//yyDefRed 1219
     1,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   325,   328,     0,     0,     0,   348,   349,     0,
     0,     0,   601,   600,   602,   603,     0,     0,     0,     9,
     0,   605,   604,   606,     0,     0,   597,   596,     0,   599,
   544,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   568,   570,   572,     0,     0,   408,   611,
   612,   591,   592,   594,   593,     0,     0,     0,     0,   541,
    66,   292,     0,   545,   293,   294,     0,   295,   296,   291,
   542,    32,    49,   540,   589,     0,     0,     0,     0,     0,
     0,   299,     0,    61,     0,     0,     0,     0,    92,     0,
     4,   297,   298,     0,     0,    78,     0,     0,     0,     2,
     0,     5,     0,     7,   346,   347,   312,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   119,     0,     0,
   350,     0,   300,     0,     0,   607,   608,     0,   342,   173,
   184,   174,   197,   170,   190,   180,   179,   195,   178,   177,
   172,   198,   182,   171,   185,   189,   191,   183,   176,   192,
   199,   194,     0,     0,     0,     0,   169,   188,   187,   200,
   201,   202,   203,   204,   168,   175,   166,   167,     0,     0,
     0,     0,   123,     0,   158,   159,   155,   136,   137,   138,
   145,   142,   144,   139,   140,   160,   161,   146,   147,   690,
   152,   151,   135,   157,   154,   153,   149,   150,   143,   141,
   133,   156,   134,   162,   148,   617,   124,     0,   689,   125,
   193,   186,   196,   181,   163,   164,   165,   121,   122,   127,
   126,   129,     0,   128,   130,     0,     0,     0,     0,     0,
     0,    14,    13,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   723,   724,     0,     0,     0,   725,     0,
     0,     0,     0,     0,   360,   361,     0,     0,     0,     0,
     0,     0,     0,   568,     0,     0,   272,    76,     0,     0,
     0,   694,   276,    77,    75,     0,    74,     0,     0,   427,
    73,     0,   717,     0,     0,    20,     0,     0,     0,   227,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,   258,     0,     0,     0,   692,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   243,    53,   242,   586,   585,
   587,   583,   584,     0,     0,     0,     0,     0,     0,     0,
     0,   322,     0,     0,     0,     0,     0,     0,     0,   546,
   432,   414,   430,   321,   417,     0,     0,     0,     0,     0,
     0,     0,    54,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,    56,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   416,   712,     0,   713,     0,     0,   710,   711,     0,     0,
   655,   652,   651,   650,   653,   660,   669,     0,     0,   681,
   680,   685,   684,   670,   649,     0,     0,     0,     0,   678,
     0,     0,     0,     0,     0,     0,   645,   667,     0,   629,
   658,   654,     0,     0,     0,   646,     0,     0,     0,    94,
     0,     0,     0,     0,     0,     0,     3,     0,   421,     0,
   319,     0,   590,     0,   116,     0,   118,     0,   614,   337,
   613,     0,     0,     0,   621,     0,     0,   618,   131,     0,
     0,     0,     0,   302,    12,     0,     0,   352,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   726,     0,     0,     0,     0,     0,     0,   334,   697,   283,
   279,     0,   699,     0,     0,   273,   281,     0,   274,     0,
   314,     0,   278,     0,   268,   267,     0,     0,     0,     0,
     0,   318,    52,    22,    24,    23,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   307,     0,     0,   304,
   310,     0,   721,   259,     0,   261,   693,   311,     0,    96,
     0,     0,     0,     0,     0,   577,   575,   588,   574,   571,
   547,   569,   548,   549,   573,   550,   551,   554,     0,   560,
   561,     0,     0,   411,     0,   556,   557,     0,   562,   563,
     0,     0,     0,     0,    26,    27,    28,    29,    30,    50,
    51,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   424,     0,   426,     0,     0,     0,   705,
     0,     0,   706,   425,   703,   704,     0,    48,     0,    40,
   213,     0,     0,     0,     0,   362,     0,   364,     0,     0,
     0,   682,   686,     0,   643,   622,     0,     0,     0,   638,
     0,   641,     0,   626,   673,   671,     0,   627,   628,   661,
     0,     0,     0,    45,   205,     0,    41,   282,     0,     0,
     0,     0,    95,    33,     0,   286,     0,    34,   206,     6,
    64,    68,     0,   616,     0,     0,     0,     0,     0,   120,
     0,     0,     0,     0,     0,     0,     0,   534,     0,     0,
   535,     0,     0,   358,    15,     0,   353,     0,     0,     0,
     0,     0,     0,   437,     0,     0,     0,   332,   355,   326,
   354,   329,     0,     0,     0,     0,     0,     0,     0,   696,
     0,     0,     0,   280,   695,   313,   718,     0,     0,   262,
   263,   317,    21,     0,     0,    31,     0,   212,     0,   306,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   553,   555,   565,     0,     0,     0,   409,   559,   567,   400,
     0,   398,     0,   397,     0,     0,   526,   530,   502,     0,
     0,     0,     0,     0,     0,     0,     0,   517,   518,   519,
   520,   521,   516,   522,   523,   525,   524,     0,    55,     0,
     0,   450,     0,   454,   459,     0,   511,   512,     0,   448,
   447,     0,   449,     0,   498,     0,   495,   497,    57,     0,
     0,     0,   423,     0,     0,     0,     0,     0,     0,   266,
     0,   422,   265,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   657,   620,   674,   656,     0,   630,   344,     0,
     0,     0,   659,   647,     0,   679,     0,   668,   687,     0,
     0,   345,     0,     0,     0,    93,     0,     0,     0,     0,
   340,     0,     0,   429,   343,   691,   619,   132,     0,     0,
     0,   537,   359,     0,    11,   539,     0,   356,     0,     0,
     0,     0,     0,     0,     0,     0,   331,   333,     0,     0,
     0,     0,     0,     0,   698,   285,   275,     0,     0,   316,
    10,   260,    97,     0,     0,   579,   580,   581,   576,   582,
     0,     0,     0,     0,     0,     0,     0,     0,     0,   396,
   665,     0,     0,   379,     0,   676,     0,     0,     0,     0,
     0,   117,   528,     0,   470,     0,     0,   481,     0,     0,
     0,   472,     0,   489,   527,   514,   515,   505,   503,     0,
     0,     0,     0,     0,     0,     0,     0,   455,   456,   457,
   458,     0,     0,     0,     0,     0,     0,    39,   210,    38,
   211,    72,     0,   719,    36,   208,    37,   209,    70,   420,
   419,    47,     0,   363,     0,     0,     0,   365,     0,   644,
     0,     0,   636,     0,   634,     0,   639,   642,   625,     0,
     0,     0,     0,    42,     0,     0,   615,   338,    19,     0,
     0,     0,   536,     0,     0,     0,   538,     0,   323,     0,
   324,     0,   284,     0,     0,     0,   335,     0,   264,   578,
   410,     0,   413,   412,     0,     0,     0,     0,   394,     0,
     0,   389,     0,   376,     0,   392,   399,   377,   378,   433,
   415,   431,   473,   468,   469,     0,     0,     0,   482,   471,
   442,   443,   451,   446,     0,   453,   507,   508,   529,   463,
     0,     0,     0,   467,     0,     0,     0,     0,   486,   499,
   492,   496,     0,    35,   207,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   435,   434,   436,   327,   330,
     0,   406,     0,   404,   407,     0,   309,     0,     0,   380,
   401,     0,     0,   666,     0,     0,     0,   677,     0,     0,
   483,     0,   460,   461,   462,   464,   465,   466,     0,     0,
     0,   637,     0,   632,   635,   640,    65,   341,     0,     0,
     0,     0,     0,   531,     0,     0,     0,     0,   403,     0,
     0,     0,   395,     0,   386,     0,   384,   375,     0,   390,
   393,     0,     0,     0,   488,   487,     0,     0,     0,   357,
   439,   440,   438,   336,   405,     0,     0,     0,     0,     0,
     0,   633,   388,     0,   382,   385,   391,     0,   383,
    }, yyDgoto = {
//yyDgoto 217
     1,   331,    69,    70,   698,   623,   127,   230,   624,   855,
   398,   416,   417,   418,   217,    71,    72,    73,    74,    75,
   334,   333,   559,   128,   336,    77,    78,   568,    79,    80,
   129,    81,    82,   673,   674,    83,    84,   237,   238,   239,
   240,    86,    87,    88,    89,   241,   245,   300,   897,  1126,
   898,   890,   514,   894,   625,   459,   286,    91,   841,    92,
    93,   419,   232,   928,   247,   684,   420,   421,   574,   937,
   781,   782,   422,   423,    95,    96,   676,    97,    98,   278,
   489,   709,   310,   248,   242,   626,   340,   338,   424,   425,
   646,   344,   346,   101,   102,   654,  1058,  1139,   857,   427,
   940,   941,   428,   103,   517,   281,   317,   550,   942,   505,
   282,   506,   663,   429,   353,   354,   691,   104,   105,   472,
   249,   233,   234,   430,  1132,   930,   776,   805,   307,   945,
   266,   518,   647,   648,  1133,   250,   251,   510,   835,   219,
   431,   109,   110,   111,   432,   433,   434,   435,   725,  1202,
   808,   809,  1076,   811,   812,   813,   814,   815,   816,   817,
   818,   956,   957,   820,   821,   959,  1097,   960,   822,   823,
   824,   825,   826,   827,   981,   982,   436,   137,   463,     2,
   256,   257,   297,   470,   526,   511,   591,   607,   879,   705,
   543,   287,   309,   538,   478,   259,   729,   910,   260,   911,
   737,  1130,   695,   479,   692,  1025,   465,   342,   582,   583,
   581,   905,   769,   768,   694,   704,   466,
    }, yySindex = {
//yySindex 1219
     0,     0, 18850, 19266, 22839, 23235, 22302, 22624, 18989, 21750,
 21750, 12330,     0,     0, 22971, 19404, 19404,     0,     0, 19404,
  -195,  -155,     0,     0,     0,     0,    89, 22516,   250,     0,
  -201,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0, 21888, 21888,   303,   -39, 19128, 21750, 20232, 20646, 18309,
 21888, 22026, 22409,     0,     0,     0,   297,   352,     0,     0,
     0,     0,     0,     0,     0,   437,   447, 21888, 21888,     0,
     0,     0,   262,     0,     0,     0,   784,     0,     0,     0,
     0,     0,     0,     0,     0,  1843,   321,  5592,     0,   233,
   624,     0,   476,     0,   194,  2635,  2635,   538,     0,   635,
     0,     0,     0, 23103,   645,     0,   384,     0,     0,     0,
   206,     0,  -116,     0,     0,     0,     0,  -195,  -155,   372,
   250,     0,     0,   303, 21750,   423, 18989,     0,   -98,    94,
     0,   751,     0,  2635,  2635,     0,     0,  -116,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   476,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   724,     0,     0, 19542, 18989,   509,   536,   206,
  1843,     0,     0,   506,   321,   191,   841,   497,   796,   532,
     0,     0,   191,     0,     0,   206,   601,   862,     0, 21750,
 21750,   589,   909,     0,     0,     0,   640,     0,     0, 21888,
 21888, 21888, 21888,     0, 21888,  8387,     0,     0,   592,   892,
   895,     0,     0,     0,     0, 12187,     0, 19404, 19680,     0,
     0,  4155,     0, 21750,   338,     0, 20784,   603, 18989,     0,
   923,   633,   650,   632, 19128,   636,     0,   250,   321,   250,
   638,     0,   202,   210,   592,     0,   210,   628,   687, 23367,
   983,     0,   962,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,  1302,  1364,  1400,  1167,   641,  1472,   646,
   355,     0,  2981,   656,  1584,   659,   442,  3685,  3685,     0,
     0,     0,     0,     0,     0, 21750, 21750, 21750, 21750, 19542,
 21750, 21750,     0, 21888, 21888, 21888, 21888, 21888, 21888, 21888,
 21888, 21888, 21888, 21888, 21888, 21888, 21888, 21888,     0, 21888,
 21888, 21888, 21888, 21888, 21888, 21888, 21888, 21888, 21888, 21888,
     0,     0,  4536,     0, 19404,  7575,     0,     0, 24626, 22026,
     0,     0,     0,     0,     0,     0,     0,  1713,  3239,     0,
     0,     0,     0,     0,     0,   974,   734,   735,   998,     0,
   206, 18989,     0,   984,  1006,  1007,     0,     0,  1009,     0,
     0,     0,   -64,  1011,  1011,     0, 21888, 18989,   995,     0,
 20922, 19128, 18441,  1016, 20922, 22026,     0, 18573,     0,   721,
     0,   755,     0,   321,     0,     0,     0,   206,     0,     0,
     0, 14885, 19404, 24036,     0, 18989, 21750,     0,     0,  1843,
   714, 21060,   828,     0,     0, 18711,   632,     0, 18989,   845,
 24095, 19404, 24154, 21888, 21888, 21888, 18989,   -72, 21198,   852,
     0,    97,    97,     0, 24213, 19404, 24272,     0,     0,     0,
     0,  1293,     0, 21888, 19818,     0,     0, 20370,     0,   250,
     0,   777,     0, 21888,     0,     0,  1085,  1087,   250,   250,
   103,     0,     0,     0,     0,     0, 22624, 21750,  8387, 18850,
   773, 24095, 24154, 21888, 21888,   250,     0,     0,   250,     0,
     0, 20508,     0,     0, 20646,     0,     0,     0,     0,     0,
  1096, 24331, 19404, 24390, 23367,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   225,     0,
     0,  1110,  3239,     0,  -107,     0,     0,   249,     0,     0,
  1112,   -48,   -48,   -48,     0,     0,     0,     0,     0,     0,
     0, 23725,   633,  2818,  2818,  2818,  2818,  2882,  2882,  5036,
  4682,  2818,  2818,  3685,  3685,  2179,  2179, 23725,   633,  2232,
   633,   633,   667,   667,  2882,  2882,  2713,  2713,  7964,   816,
     0,   820,  -155,     0,     0,     0,  1115,   250,   824,     0,
   826,  -155,     0,     0,     0,     0,  -155,     0,   865,     0,
     0,  6100,  1109,  1713,  1130,     0,  1127,     0,   250,   250,
 21888,     0,     0,  1982,     0,     0,   908, 21888,  3239,     0,
  3331,     0,  1078,     0,     0,     0,   479,     0,     0,     0,
   920, 21888, 21888,     0,     0,  6100,     0,     0,  1148,   250,
 23367,  1152,     0,     0,   903,     0,     0,     0,     0,     0,
     0,     0,   206,     0, 21750, 18989,     0,     0,  -155,     0,
   250,  -155,   939,   103, 22732, 22624, 18850,     0,     0,   867,
     0, 18989,   946,     0,     0,   582,     0,   874,   880,   250,
   901,   902,   828,     0,   977,   985,   754,     0,     0,     0,
     0,     0,     0,     0,   250,     0,     0, 21750, 21888,     0,
 21888,   592,   895,     0,     0,     0,     0, 19956, 20370,     0,
     0,     0,     0,   103,   870,     0,   633,     0,     0,     0,
   210, 23367,     0,     0,   250,     0,     0,  1096, 18989,   776,
     0,     0,     0,   250, 18989, 18989,     0,     0,     0,     0,
  1937,     0, 18989,     0, 18989, 18989,     0,     0,     0,   612,
 23951,  1535,   585,   944,   947,  5115,  5115,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   457,     0,   600,
    74,     0,   897,     0,     0,   597,     0,     0,  1170,     0,
     0,  1212,     0,  1213,     0, 23951,     0,     0,     0,  1202,
 22026, 22026,     0,   721, 19818,   938,   935, 22026, 22026,     0,
   721,     0,     0,   233,   -98, 19542, 21888, 22026,   250,  1600,
  1246,  1923,     0,     0,     0,     0,  1251,     0,     0,  6614,
  1261,  1266,     0,     0,  1272,     0,  1251,     0,     0,  1013,
  1011,     0,  7107,     0, 21336,     0,  1096, 23367, 19542,   -48,
     0,   206,  1061,     0,     0,     0,     0,     0,   965, 23499,
   191,     0,     0, 18989,     0,     0, 21750,     0,  1067, 21888,
 21888,   996, 21888, 21888,  1072, 23725,     0,     0, 21474, 18989,
 18989, 18989,     0,    97,     0,     0,     0,  1294,   250,     0,
     0,     0,     0,     0,   975,     0,     0,     0,     0,     0,
  1003,  1291,  1081,   992,  1297, 23631,  1315,   250,  1321,     0,
     0,  1322,  1326,     0,  1015,     0,  1011,  1011,  1114,  1116,
  1017,     0,     0,    57,     0,  1043,   250,     0,   250, 23819,
  1344,     0,  1036,     0,     0,     0,     0,     0,     0, 21750,
 21750,  1088, 23913, 23951,  5115,  5115,   612,     0,     0,     0,
     0,  5685,  5685, 23951,   -24,  1043, 21888,     0,     0,     0,
     0,     0, 22026,     0,     0,     0,     0,     0,     0,     0,
     0,     0,  8387,     0,  1356,  1600,  1340,     0,  1982,     0,
 21888,  1982,     0,  3331,     0,  1982,     0,     0,     0, 21888,
 21888,     0,  1096,     0, 18989, 18989,     0,     0,     0,  1093,
  1020,     0,     0,     0,     0, 18989,     0,   191,     0, 21888,
     0,   191,     0,   487,  1141,  1142,     0, 20370,     0,     0,
     0,  2040,     0,     0, 23631,  1105,  1032,  2376,     0,  1059,
  3550,     0,  1162,     0,  3550,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   126,  1043,  1374,     0,     0,
     0,     0,     0,     0,  1375,     0,     0,     0,     0,     0,
   250,   250,   250,     0,   250,   250,   250,  1378,     0,     0,
     0,     0,  8387,     0,     0,  1600,  1356,  1600,  1382,  8387,
  1251,  1393,  1251,  1251,  8387,     0,  1099,  1185,     0, 24449,
 19404, 24508,   509, 18989, 18989,     0,     0,     0,     0,     0,
 18989,     0,   243,     0,     0,  1032,     0, 24567,  1406,     0,
     0,  1414,  1420,     0,  1011,  1422,  1406,     0,  1424, 23951,
     0, 23951,     0,     0,     0,     0,     0,     0, 23913,  1356,
  1600,     0,  1982,     0,     0,     0,     0,     0,     0,     0,
   250,     0,     0,     0,   582,    32,  1206,  2040,     0,     0,
     0,  2376,     0,  2376,     0,  3550,     0,     0,  2376,     0,
     0, 23951,  1427,  1427,     0,     0,  1356,  1251,     0,     0,
     0,     0,     0,     0,     0,  1406,  1442,  1406,  1406,  1427,
 23951,     0,     0,  2376,     0,     0,     0,  1406,     0,
    }, yyRindex = {
//yyRindex 1219
     0,     0,  1230,     0,     0,     0,     0,     0,  1183,     0,
     0,  1207,     0,     0,     0,  9737,  9845,     0,     0,  9977,
  6478,  5970,     0,     0,     0,     0, 22164,     0, 21612,     0,
     0,     0,     0,     0,  3794,  4954,     0,     0,  3938,     0,
     0,     0,     0,     0,     0,   181,    81,  1146,  1134,   110,
     0,     0,  1587,     0,     0,     0,  1712,   511,     0,     0,
     0,     0,     0,     0,     0,  1721,   512,     0,     0,     0,
     0,     0,  9147,     0,     0,     0, 10194,     0,     0,     0,
     0,     0,     0,     0,     0,    90,  3273, 17983,  9279, 17718,
     0,     0, 17754,     0, 10543,   854,   854,     0,     0,     0,
     0,     0,     0,   228,     0,     0,     0, 10326, 10435,     0,
    51,     0, 20094,     0,     0,     0,     0,  9388,  8341,     0,
  1149, 10903, 11042,     0,     0,     0,   181,     0,  9496,     0,
     0,     0,     0,   639,   639,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,  1145,  2005,  2167,  2438,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,  2537,  2743,
  3479,  4630,     0,  5138,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,  4688,     0,     0,     0,   807,   523,     0,  1330,
  1728,     0,     0,     0, 18196,     0,     0,  8930,     0,     0,
  8581,  8690,     0,     0,     0,    29,     0,   129,     0,     0,
     0,     0,     0,   178,     0,     0,     0,   606,   704,     0,
     0,     0,     0,     0,     0, 16250,     0,     0, 16662,  6212,
  6212,     0,     0,     0,     0,  1156,     0,     0,   209,     0,
     0,  1156,     0,     0,     0,     0,     0,     0,    76,     0,
     0, 12768,  9628, 10658,   181,     0,   207,  1156,   216,  1156,
     0,     0,  1164,  1164,     0,     0,  1138,     0,     0,     0,
     0,  1028,   275,  1438,  1525,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   -75,     0,     0,     0,     0, 15582, 15671,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0, 12527, 12667,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,    47,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   217,     0,
     0,     0,     0,     0,     0,   281,   732,     0,   328,     0,
     0,  1183, 11768,     0,   328,   328,     0,     0,   375,     0,
     0,     0,   968,   375,   375,     0,   316,  1183,     0,     0,
     0,   181,   276,   293,     0,     0,     0,    61,     0,  7219,
     0,     0,     0,   716,     0, 11483,     0,     0,     0,     0,
     0,     0,    47,     0,     0,  1183,     0,     0,     0,  2101,
     0,   443,   761,     0,     0,  1434,  9039,     0,  1351, 11622,
     0,    47,     0,     0,     0,     0,  1128,     0,     0,     0,
     0,     0,     0,   860,     0,    47,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,  1156,
     0,     0,     0,   226,     0,     0,   251,    83,  1156,  1156,
  1156,     0,     0,     0,     0,     0,     0,     0, 15936,    76,
     0,     0,     0,     0,     0,  1156,     0,   270,  1156,     0,
     0,  1177,     0,     0,  -113,     0,     0,     0,  1646,     0,
   346,     0,    47,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   203,     0,     0,     0,     0,     0,     0,     0,
     0, 11907, 11907, 12049,     0,     0,     0,     0,     0,     0,
     0,  1722, 12893, 14753, 14839, 14994, 15079, 14312, 14416, 15165,
 15457, 15250, 15339, 15758, 15847, 13744, 13857,  1722, 13016, 13959,
 13133, 13242, 13515, 13617, 14538, 14657, 14083, 14199,  1449,  6839,
  5324,  7485, 20094,     0,  5462,     0,   118,  1179,  6977,     0,
  7347,  6340,     0,     0,     0,     0,  7727,     0,  5196,     0,
     0, 16373,     0,     0,   266,     0,   317,     0,  1156,  1156,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0, 18019,     0,     0,     0,  1156,
     0,   524,     0,     0,  6720,     0, 18248,     0,     0,     0,
     0,     0,     0,     0,     0,  1183, 11193, 11332,     0,     0,
  1179,  8798,     0,  1156,     0,     0,    76,     0,   795,   231,
     0,   660,   852,     0,     0,   852,     0,  4308,  5832,  1179,
  4446,  4816,   852,     0,     0,     0,     0,     0,     0,     0,
     0,     0,  1806,   570,  1179,  2062,  2082,     0,     0,     0,
     0, 16698,  6212,     0,     0,     0,     0,   322,    92,     0,
     0,     0,     0,  1156,     0,     0, 13377,     0,   142,     0,
  1164,     0,  1073,  1296,  1179,  1163,  1286,   747,    76,     0,
     0,     0,     0,   133,  1183,    76,     0,     0,     0,     0,
    28,     0,  1183,     0,  1183,    76,     0,     0,     0,     0,
  2259,  4028,     0, 17015,     0,  2489,  2489,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0, 17239,     0, 15899,
 17806,     0, 16927,     0,     0, 16759,     0,     0, 16814,     0,
     0, 17275,     0, 17327,     0,  3394,     0,     0,     0,     0,
     0,     0,     0,  7969,   124, 10086,     0,     0,     0,     0,
  8492,     0,     0, 17842, 16851,     0,     0,     0,  1156,     0,
   325,     0,     0,     0,     0,     0,   328,     0,     0, 16516,
   328,   328,     0,     0,   328,     0,   328,     0,     0,   968,
   375,     0, 16601, 18071,     0,     0,   825,     0,     0, 12049,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   701,     0,     0,     0,     0,     0,     0,
     0,  8214,     0,     0,     0,  1722,     0,     0,     0,   896,
   701,   701,    36,     0,     0,     0,     0,    83,  1156,     0,
     0,     0,     0,  1415,     0,     0,     0,     0,     0,     0,
     0,   -28,     0,     0,   106,   145,    85,    28,    85,     0,
     0,   162,    85,     0,     0,     0,   162,   162,     0,     0,
     0,     0,     0,  1156,     0, 17057,  1179,     0,  1179,  3624,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0, 10767,  2259,  7855,  8097,     0,     0,     0,     0,
     0,  1705,  1863,  2259, 17364, 17143,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0, 16024,     0,   331,     0,   334,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0, 18107,   853,     0,    76,  1183,     0,     0,     0,     0,
     0,   109,     0,   308,  1095,  1183,     0,     0,     0,     0,
     0,     0,     0,   852,     0,     0,     0,   136,     0,     0,
     0,     0,     0,     0,     0,     0,   166,     0,     0,     0,
   184,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0, 17416, 17183, 17453,     0,     0,
     0,     0,     0,     0, 17894,     0,     0,     0,     0,     0,
  1156,  1156,  1156,     0,  1179,  1179,  1179, 17540,     0,     0,
     0,     0, 16064,     0,     0,     0,   335,     0,   336, 16125,
   328,   328,   328,   328, 16210, 18159,     0,     0,  1526,     0,
    47,     0,   523,  1351,   950,     0,     0,     0,     0,     0,
   701,     0,   310,     0,     0,   170,     0,     0,    85,     0,
     0,    85,    85,     0,   162,    85,    85,     0, 17577,  2259,
     0,  2259,     0,     0,     0,     0,     0,     0,  2116,   342,
     0,     0,     0,     0,     0,     0,     0,     0,   943,  1473,
  1179,  1221,  1625,     0,   852,   852,     0,     0,     0,  1471,
   248,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,  2259, 17629, 17931,     0,     0,   345,   328,  1683,     0,
     0,     0,     0,     0,     0,    85,    85,    85,    85, 17666,
  2259,     0,     0,     0,     0,     0,     0,    85,     0,
    }, yyGindex = {
//yyGindex 217
     0,     0,    25,     0,  -342,     0,  -118,    70,     3,    41,
  1303,     0,     0,  -543,     0,   253,     0,  1453,  1176,  1487,
  1241,  1477,  1482,  1744,     0,  2284,  2322,  1182,  2406,    35,
  1522,  -193,    15,  -257,   483,     0,   150,   -32,  -270,     0,
    71,    60,  2975,    23,    10,  1065,    24,   822,  -452,     0,
   367,     0,   840,     0,    -7,     0,    17,  1540,   912,     0,
     0,  -611,     0,     0,  1882,     0,  -180,   148,     0,     0,
     0,  -308,   877,  1454,    40,    54,   157,   -87,    -1,   928,
  -465,     0,     0,  2375,    16,   989,     0,     0,  -521,   894,
  -259,     0,     0,  2601,  2626,   135,  1397,   774,    63,   891,
   495,     0,     0,     0,    62,  -484,     0,  -471,   498,  -261,
  -468,     0,  -337,   208,   -97,   -81,     7,  1546,     1,   441,
  2194,     0,   -10,  -634,     0,  -614,     0,  1509,  -163,  -891,
     0,  -383,  -743,   929,   396,  2253,    -2,   232,  -586,     0,
  -969,  -348,     0,    72,  -444,   158,  -120,  -247,   402,     0,
  -515,     0,  -160,     0,     0,   607,     0,   307,   608,     0,
     0,  -648,  -352,  -660,  -849,     0,  -788,   785,  -613,     0,
   604,     0,   605,   611,     0,     0,     0,  1283,   -85,     0,
   563,   138,     0,     0,     0,     5,     0,     0,     0,     0,
  -228,     0,     0,     0,  -231,     0,  -427,     0,     0,     0,
     0,     0,     0,    73,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,
    };
    protected static final short[] yyTable = Ruby30YyTables.yyTable();
    protected static final short[] yyCheck = Ruby30YyTables.yyCheck();

  /** maps symbol value to printable name.
      @see #yyExpecting
    */
  protected static final String[] yyNames = {
    "end-of-file",null,null,null,null,null,null,null,null,null,"'\\n'",
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,"' '",null,null,null,null,null,
    null,null,null,null,null,null,"','",null,null,null,null,null,null,
    null,null,null,null,null,null,null,"':'","';'",null,"'='",null,"'?'",
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,
    "'['",null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,
    "kCLASS","kMODULE","kDEF","kUNDEF","kBEGIN","kRESCUE","kENSURE",
    "kEND","kIF","kUNLESS","kTHEN","kELSIF","kELSE","kCASE","kWHEN",
    "kWHILE","kUNTIL","kFOR","kBREAK","kNEXT","kREDO","kRETRY","kIN",
    "kDO","kDO_COND","kDO_BLOCK","kRETURN","kYIELD","kSUPER","kSELF",
    "kNIL","kTRUE","kFALSE","kAND","kOR","kNOT","kIF_MOD","kUNLESS_MOD",
    "kWHILE_MOD","kUNTIL_MOD","kRESCUE_MOD","kALIAS","kDEFINED","klBEGIN",
    "klEND","k__LINE__","k__FILE__","k__ENCODING__","kDO_LAMBDA",
    "tIDENTIFIER","tFID","tGVAR","tIVAR","tCONSTANT","tCVAR","tLABEL",
    "tCHAR","tUPLUS","tUMINUS","tUMINUS_NUM","tPOW","tCMP","tEQ","tEQQ",
    "tNEQ","tGEQ","tLEQ","tANDOP","tOROP","tMATCH","tNMATCH","tDOT",
    "tDOT2","tDOT3","tAREF","tASET","tLSHFT","tRSHFT","tCOLON2","tCOLON3",
    "tOP_ASGN","tASSOC","tLPAREN","tLPAREN2","tRPAREN","tLPAREN_ARG",
    "tLBRACK","tRBRACK","tLBRACE","tLBRACE_ARG","tSTAR","tSTAR2","tAMPER",
    "tAMPER2","tTILDE","tPERCENT","tDIVIDE","tPLUS","tMINUS","tLT","tGT",
    "tPIPE","tBANG","tCARET","tLCURLY","tRCURLY","tBACK_REF2","tSYMBEG",
    "tSTRING_BEG","tXSTRING_BEG","tREGEXP_BEG","tWORDS_BEG","tQWORDS_BEG",
    "tSTRING_DBEG","tSTRING_DVAR","tSTRING_END","tLAMBDA","tLAMBEG",
    "tNTH_REF","tBACK_REF","tSTRING_CONTENT","tINTEGER","tFLOAT",
    "tREGEXP_END","tORDER_ALIGNMENT_01","tORDER_ALIGNMENT_02",
    "tORDER_ALIGNMENT_03","tLOWEST","tIMAGINARY","tRATIONAL",
    "tSYMBOLS_BEG","tQSYMBOLS_BEG","tDSTAR","tLABEL_END","tSTRING_DEND",
    "tANDDOT","tBDOT2","tBDOT3",
    };

  /** printable rules for debugging.
    */
  protected static final String [] yyRule = {
    "$accept : program",
    "$$1 :",
    "program : $$1 top_compstmt",
    "top_compstmt : top_stmts opt_terms",
    "top_stmts : none",
    "top_stmts : top_stmt",
    "top_stmts : top_stmts terms top_stmt",
    "top_stmts : error top_stmt",
    "top_stmt : stmt",
    "$$2 :",
    "top_stmt : klBEGIN $$2 tLCURLY top_compstmt tRCURLY",
    "bodystmt : compstmt opt_rescue opt_else opt_ensure",
    "compstmt : stmts opt_terms",
    "stmts : none",
    "stmts : stmt_or_begin",
    "stmts : stmts terms stmt_or_begin",
    "stmts : error stmt",
    "stmt_or_begin : stmt",
    "$$3 :",
    "stmt_or_begin : kBEGIN $$3 tLCURLY top_compstmt tRCURLY",
    "$$4 :",
    "stmt : kALIAS fitem $$4 fitem",
    "stmt : kALIAS tGVAR tGVAR",
    "stmt : kALIAS tGVAR tBACK_REF",
    "stmt : kALIAS tGVAR tNTH_REF",
    "stmt : kUNDEF undef_list",
    "stmt : stmt kIF_MOD expr_value",
    "stmt : stmt kUNLESS_MOD expr_value",
    "stmt : stmt kWHILE_MOD expr_value",
    "stmt : stmt kUNTIL_MOD expr_value",
    "stmt : stmt kRESCUE_MOD stmt",
    "stmt : klEND tLCURLY compstmt tRCURLY",
    "stmt : command_asgn",
    "stmt : mlhs '=' command_rhs",
    "stmt : var_lhs tOP_ASGN command_rhs",
    "stmt : primary_value '[' opt_call_args rbracket tOP_ASGN command_rhs",
    "stmt : primary_value call_op tIDENTIFIER tOP_ASGN command_rhs",
    "stmt : primary_value call_op tCONSTANT tOP_ASGN command_rhs",
    "stmt : primary_value tCOLON2 tCONSTANT tOP_ASGN command_rhs",
    "stmt : primary_value tCOLON2 tIDENTIFIER tOP_ASGN command_rhs",
    "stmt : backref tOP_ASGN command_rhs",
    "stmt : lhs '=' mrhs",
    "stmt : mlhs '=' mrhs_arg kRESCUE_MOD stmt",
    "stmt : mlhs '=' mrhs_arg",
    "stmt : expr",
    "command_asgn : lhs '=' command_rhs",
    "command_rhs : command_call",
    "command_rhs : command_call kRESCUE_MOD stmt",
    "command_rhs : command_asgn",
    "expr : command_call",
    "expr : expr kAND expr",
    "expr : expr kOR expr",
    "expr : kNOT opt_nl expr",
    "expr : tBANG command_call",
    "$$5 :",
    "expr : arg kIN $$5 p_top_expr",
    "$$6 :",
    "expr : arg tASSOC $$6 p_top_expr",
    "expr : arg",
    "expr_value : expr",
    "command_call : command",
    "command_call : block_command",
    "block_command : block_call",
    "block_command : block_call dot_or_colon operation2 command_args",
    "$$7 :",
    "cmd_brace_block : tLBRACE_ARG $$7 opt_block_param compstmt tRCURLY",
    "fcall : operation",
    "command : fcall command_args",
    "command : fcall command_args cmd_brace_block",
    "command : primary_value call_op operation2 command_args",
    "command : primary_value call_op operation2 command_args cmd_brace_block",
    "command : primary_value tCOLON2 operation2 command_args",
    "command : primary_value tCOLON2 operation2 command_args cmd_brace_block",
    "command : kSUPER command_args",
    "command : kYIELD command_args",
    "command : kRETURN call_args",
    "command : kBREAK call_args",
    "command : kNEXT call_args",
    "mlhs : mlhs_basic",
    "mlhs : tLPAREN mlhs_inner rparen",
    "mlhs_inner : mlhs_basic",
    "mlhs_inner : tLPAREN mlhs_inner rparen",
    "mlhs_basic : mlhs_head",
    "mlhs_basic : mlhs_head mlhs_item",
    "mlhs_basic : mlhs_head tSTAR mlhs_node",
    "mlhs_basic : mlhs_head tSTAR mlhs_node ',' mlhs_post",
    "mlhs_basic : mlhs_head tSTAR",
    "mlhs_basic : mlhs_head tSTAR ',' mlhs_post",
    "mlhs_basic : tSTAR mlhs_node",
    "mlhs_basic : tSTAR mlhs_node ',' mlhs_post",
    "mlhs_basic : tSTAR",
    "mlhs_basic : tSTAR ',' mlhs_post",
    "mlhs_item : mlhs_node",
    "mlhs_item : tLPAREN mlhs_inner rparen",
    "mlhs_head : mlhs_item ','",
    "mlhs_head : mlhs_head mlhs_item ','",
    "mlhs_post : mlhs_item",
    "mlhs_post : mlhs_post ',' mlhs_item",
    "mlhs_node : user_variable",
    "mlhs_node : keyword_variable",
    "mlhs_node : primary_value '[' opt_call_args rbracket",
    "mlhs_node : primary_value call_op tIDENTIFIER",
    "mlhs_node : primary_value tCOLON2 tIDENTIFIER",
    "mlhs_node : primary_value call_op tCONSTANT",
    "mlhs_node : primary_value tCOLON2 tCONSTANT",
    "mlhs_node : tCOLON3 tCONSTANT",
    "mlhs_node : backref",
    "lhs : user_variable",
    "lhs : keyword_variable",
    "lhs : primary_value '[' opt_call_args rbracket",
    "lhs : primary_value call_op tIDENTIFIER",
    "lhs : primary_value tCOLON2 tIDENTIFIER",
    "lhs : primary_value call_op tCONSTANT",
    "lhs : primary_value tCOLON2 tCONSTANT",
    "lhs : tCOLON3 tCONSTANT",
    "lhs : backref",
    "cname : tIDENTIFIER",
    "cname : tCONSTANT",
    "cpath : tCOLON3 cname",
    "cpath : cname",
    "cpath : primary_value tCOLON2 cname",
    "fname : tIDENTIFIER",
    "fname : tCONSTANT",
    "fname : tFID",
    "fname : op",
    "fname : reswords",
    "fsym : fname",
    "fsym : symbol",
    "fitem : fsym",
    "fitem : dsym",
    "undef_list : fitem",
    "$$8 :",
    "undef_list : undef_list ',' $$8 fitem",
    "op : tPIPE",
    "op : tCARET",
    "op : tAMPER2",
    "op : tCMP",
    "op : tEQ",
    "op : tEQQ",
    "op : tMATCH",
    "op : tNMATCH",
    "op : tGT",
    "op : tGEQ",
    "op : tLT",
    "op : tLEQ",
    "op : tNEQ",
    "op : tLSHFT",
    "op : tRSHFT",
    "op : tDSTAR",
    "op : tPLUS",
    "op : tMINUS",
    "op : tSTAR2",
    "op : tSTAR",
    "op : tDIVIDE",
    "op : tPERCENT",
    "op : tPOW",
    "op : tBANG",
    "op : tTILDE",
    "op : tUPLUS",
    "op : tUMINUS",
    "op : tAREF",
    "op : tASET",
    "op : tBACK_REF2",
    "reswords : k__LINE__",
    "reswords : k__FILE__",
    "reswords : k__ENCODING__",
    "reswords : klBEGIN",
    "reswords : klEND",
    "reswords : kALIAS",
    "reswords : kAND",
    "reswords : kBEGIN",
    "reswords : kBREAK",
    "reswords : kCASE",
    "reswords : kCLASS",
    "reswords : kDEF",
    "reswords : kDEFINED",
    "reswords : kDO",
    "reswords : kELSE",
    "reswords : kELSIF",
    "reswords : kEND",
    "reswords : kENSURE",
    "reswords : kFALSE",
    "reswords : kFOR",
    "reswords : kIN",
    "reswords : kMODULE",
    "reswords : kNEXT",
    "reswords : kNIL",
    "reswords : kNOT",
    "reswords : kOR",
    "reswords : kREDO",
    "reswords : kRESCUE",
    "reswords : kRETRY",
    "reswords : kRETURN",
    "reswords : kSELF",
    "reswords : kSUPER",
    "reswords : kTHEN",
    "reswords : kTRUE",
    "reswords : kUNDEF",
    "reswords : kWHEN",
    "reswords : kYIELD",
    "reswords : kIF_MOD",
    "reswords : kUNLESS_MOD",
    "reswords : kWHILE_MOD",
    "reswords : kUNTIL_MOD",
    "reswords : kRESCUE_MOD",
    "arg : lhs '=' arg_rhs",
    "arg : var_lhs tOP_ASGN arg_rhs",
    "arg : primary_value '[' opt_call_args rbracket tOP_ASGN arg_rhs",
    "arg : primary_value call_op tIDENTIFIER tOP_ASGN arg_rhs",
    "arg : primary_value call_op tCONSTANT tOP_ASGN arg_rhs",
    "arg : primary_value tCOLON2 tIDENTIFIER tOP_ASGN arg_rhs",
    "arg : primary_value tCOLON2 tCONSTANT tOP_ASGN arg_rhs",
    "arg : tCOLON3 tCONSTANT tOP_ASGN arg_rhs",
    "arg : backref tOP_ASGN arg_rhs",
    "arg : arg tDOT2 arg",
    "arg : arg tDOT3 arg",
    "arg : arg tDOT2",
    "arg : arg tDOT3",
    "arg : tBDOT2 arg",
    "arg : tBDOT3 arg",
    "arg : arg tPLUS arg",
    "arg : arg tMINUS arg",
    "arg : arg tSTAR2 arg",
    "arg : arg tDIVIDE arg",
    "arg : arg tPERCENT arg",
    "arg : arg tPOW arg",
    "arg : tUMINUS_NUM simple_numeric tPOW arg",
    "arg : tUPLUS arg",
    "arg : tUMINUS arg",
    "arg : arg tPIPE arg",
    "arg : arg tCARET arg",
    "arg : arg tAMPER2 arg",
    "arg : arg tCMP arg",
    "arg : arg tGT arg",
    "arg : arg tGEQ arg",
    "arg : arg tLT arg",
    "arg : arg tLEQ arg",
    "arg : arg tEQ arg",
    "arg : arg tEQQ arg",
    "arg : arg tNEQ arg",
    "arg : arg tMATCH arg",
    "arg : arg tNMATCH arg",
    "arg : tBANG arg",
    "arg : tTILDE arg",
    "arg : arg tLSHFT arg",
    "arg : arg tRSHFT arg",
    "arg : arg tANDOP arg",
    "arg : arg tOROP arg",
    "arg : kDEFINED opt_nl arg",
    "arg : arg '?' arg opt_nl ':' arg",
    "arg : defn_head f_opt_paren_args '=' arg",
    "arg : defn_head f_opt_paren_args '=' arg kRESCUE_MOD arg",
    "arg : defs_head f_opt_paren_args '=' arg",
    "arg : defs_head f_opt_paren_args '=' arg kRESCUE_MOD arg",
    "arg : primary",
    "arg_rhs : arg",
    "arg_rhs : arg kRESCUE_MOD arg",
    "arg_value : arg",
    "aref_args : none",
    "aref_args : args trailer",
    "aref_args : args ',' assocs trailer",
    "aref_args : assocs trailer",
    "paren_args : tLPAREN2 opt_call_args rparen",
    "paren_args : tLPAREN2 args_forward rparen",
    "paren_args : tLPAREN2 args ',' args_forward rparen",
    "opt_paren_args : none",
    "opt_paren_args : paren_args",
    "opt_call_args : none",
    "opt_call_args : call_args",
    "opt_call_args : args ','",
    "opt_call_args : args ',' assocs ','",
    "opt_call_args : assocs ','",
    "call_args : command",
    "call_args : args opt_block_arg",
    "call_args : assocs opt_block_arg",
    "call_args : args ',' assocs opt_block_arg",
    "call_args : block_arg",
    "$$9 :",
    "command_args : $$9 call_args",
    "block_arg : tAMPER arg_value",
    "opt_block_arg : ',' block_arg",
    "opt_block_arg : none_block_pass",
    "args : arg_value",
    "args : tSTAR arg_value",
    "args : args ',' arg_value",
    "args : args ',' tSTAR arg_value",
    "mrhs_arg : mrhs",
    "mrhs_arg : arg_value",
    "mrhs : args ',' arg_value",
    "mrhs : args ',' tSTAR arg_value",
    "mrhs : tSTAR arg_value",
    "primary : literal",
    "primary : strings",
    "primary : xstring",
    "primary : regexp",
    "primary : words",
    "primary : qwords",
    "primary : symbols",
    "primary : qsymbols",
    "primary : var_ref",
    "primary : backref",
    "primary : tFID",
    "primary : kBEGIN bodystmt kEND",
    "$$10 :",
    "primary : tLPAREN_ARG $$10 rparen",
    "$$11 :",
    "primary : tLPAREN_ARG expr $$11 rparen",
    "primary : tLPAREN compstmt tRPAREN",
    "primary : primary_value tCOLON2 tCONSTANT",
    "primary : tCOLON3 tCONSTANT",
    "primary : tLBRACK aref_args tRBRACK",
    "primary : tLBRACE assoc_list tRCURLY",
    "primary : kRETURN",
    "primary : kYIELD tLPAREN2 call_args rparen",
    "primary : kYIELD tLPAREN2 rparen",
    "primary : kYIELD",
    "primary : kDEFINED opt_nl tLPAREN2 expr rparen",
    "primary : kNOT tLPAREN2 expr rparen",
    "primary : kNOT tLPAREN2 rparen",
    "primary : fcall brace_block",
    "primary : method_call",
    "primary : method_call brace_block",
    "primary : tLAMBDA lambda",
    "primary : kIF expr_value then compstmt if_tail kEND",
    "primary : kUNLESS expr_value then compstmt opt_else kEND",
    "$$12 :",
    "$$13 :",
    "primary : kWHILE $$12 expr_value do $$13 compstmt kEND",
    "$$14 :",
    "$$15 :",
    "primary : kUNTIL $$14 expr_value do $$15 compstmt kEND",
    "primary : kCASE expr_value opt_terms case_body kEND",
    "primary : kCASE opt_terms case_body kEND",
    "primary : kCASE expr_value opt_terms p_case_body kEND",
    "$$16 :",
    "$$17 :",
    "primary : kFOR for_var kIN $$16 expr_value do $$17 compstmt kEND",
    "$$18 :",
    "primary : kCLASS cpath superclass $$18 bodystmt kEND",
    "$$19 :",
    "$$20 :",
    "primary : kCLASS tLSHFT expr $$19 term $$20 bodystmt kEND",
    "$$21 :",
    "primary : kMODULE cpath $$21 bodystmt kEND",
    "primary : defn_head f_arglist bodystmt kEND",
    "primary : defs_head f_arglist bodystmt kEND",
    "primary : kBREAK",
    "primary : kNEXT",
    "primary : kREDO",
    "primary : kRETRY",
    "primary_value : primary",
    "then : term",
    "then : kTHEN",
    "then : term kTHEN",
    "do : term",
    "do : kDO_COND",
    "if_tail : opt_else",
    "if_tail : kELSIF expr_value then compstmt if_tail",
    "opt_else : none",
    "opt_else : kELSE compstmt",
    "for_var : lhs",
    "for_var : mlhs",
    "f_marg : f_norm_arg",
    "f_marg : tLPAREN f_margs rparen",
    "f_marg_list : f_marg",
    "f_marg_list : f_marg_list ',' f_marg",
    "f_margs : f_marg_list",
    "f_margs : f_marg_list ',' tSTAR f_norm_arg",
    "f_margs : f_marg_list ',' tSTAR f_norm_arg ',' f_marg_list",
    "f_margs : f_marg_list ',' tSTAR",
    "f_margs : f_marg_list ',' tSTAR ',' f_marg_list",
    "f_margs : tSTAR f_norm_arg",
    "f_margs : tSTAR f_norm_arg ',' f_marg_list",
    "f_margs : tSTAR",
    "f_margs : tSTAR ',' f_marg_list",
    "block_args_tail : f_block_kwarg ',' f_kwrest opt_f_block_arg",
    "block_args_tail : f_block_kwarg opt_f_block_arg",
    "block_args_tail : f_kwrest opt_f_block_arg",
    "block_args_tail : f_no_kwarg opt_f_block_arg",
    "block_args_tail : f_block_arg",
    "opt_block_args_tail : ',' block_args_tail",
    "opt_block_args_tail :",
    "block_param : f_arg ',' f_block_optarg ',' f_rest_arg opt_block_args_tail",
    "block_param : f_arg ',' f_block_optarg ',' f_rest_arg ',' f_arg opt_block_args_tail",
    "block_param : f_arg ',' f_block_optarg opt_block_args_tail",
    "block_param : f_arg ',' f_block_optarg ',' f_arg opt_block_args_tail",
    "block_param : f_arg ',' f_rest_arg opt_block_args_tail",
    "block_param : f_arg ','",
    "block_param : f_arg ',' f_rest_arg ',' f_arg opt_block_args_tail",
    "block_param : f_arg opt_block_args_tail",
    "block_param : f_block_optarg ',' f_rest_arg opt_block_args_tail",
    "block_param : f_block_optarg ',' f_rest_arg ',' f_arg opt_block_args_tail",
    "block_param : f_block_optarg opt_block_args_tail",
    "block_param : f_block_optarg ',' f_arg opt_block_args_tail",
    "block_param : f_rest_arg opt_block_args_tail",
    "block_param : f_rest_arg ',' f_arg opt_block_args_tail",
    "block_param : block_args_tail",
    "opt_block_param : none",
    "opt_block_param : block_param_def",
    "block_param_def : tPIPE opt_bv_decl tPIPE",
    "block_param_def : tOROP",
    "block_param_def : tPIPE block_param opt_bv_decl tPIPE",
    "opt_bv_decl : opt_nl",
    "opt_bv_decl : opt_nl ';' bv_decls opt_nl",
    "bv_decls : bvar",
    "bv_decls : bv_decls ',' bvar",
    "bvar : tIDENTIFIER",
    "bvar : f_bad_arg",
    "$$22 :",
    "lambda : $$22 f_larglist lambda_body",
    "f_larglist : tLPAREN2 f_args opt_bv_decl tRPAREN",
    "f_larglist : f_args",
    "lambda_body : tLAMBEG compstmt tRCURLY",
    "lambda_body : kDO_LAMBDA bodystmt kEND",
    "$$23 :",
    "do_block : kDO_BLOCK $$23 opt_block_param bodystmt kEND",
    "block_call : command do_block",
    "block_call : method_call do_block",
    "block_call : block_call dot_or_colon operation2 opt_paren_args",
    "block_call : block_call dot_or_colon operation2 opt_paren_args brace_block",
    "block_call : block_call dot_or_colon operation2 command_args do_block",
    "method_call : fcall paren_args",
    "method_call : primary_value call_op operation2 opt_paren_args",
    "method_call : primary_value tCOLON2 operation2 paren_args",
    "method_call : primary_value tCOLON2 operation3",
    "method_call : primary_value call_op paren_args",
    "method_call : primary_value tCOLON2 paren_args",
    "method_call : kSUPER paren_args",
    "method_call : kSUPER",
    "method_call : primary_value '[' opt_call_args rbracket",
    "$$24 :",
    "brace_block : tLCURLY $$24 opt_block_param compstmt tRCURLY",
    "$$25 :",
    "brace_block : kDO $$25 opt_block_param bodystmt kEND",
    "case_body : kWHEN args then compstmt cases",
    "cases : opt_else",
    "cases : case_body",
    "$$26 :",
    "p_case_body : kIN $$26 p_top_expr then compstmt p_cases",
    "p_cases : opt_else",
    "p_cases : p_case_body",
    "p_top_expr : p_top_expr_body",
    "p_top_expr : p_top_expr_body kIF_MOD expr_value",
    "p_top_expr : p_top_expr_body kUNLESS_MOD expr_value",
    "p_top_expr_body : p_expr",
    "p_top_expr_body : p_expr ','",
    "p_top_expr_body : p_expr ',' p_args",
    "p_top_expr_body : p_find",
    "p_top_expr_body : p_args_tail",
    "p_top_expr_body : p_kwargs",
    "p_expr : p_as",
    "p_as : p_expr tASSOC p_variable",
    "p_as : p_alt",
    "p_alt : p_alt tPIPE p_expr_basic",
    "p_alt : p_expr_basic",
    "p_lparen : tLPAREN",
    "p_lparen : tLPAREN2",
    "p_lbracket : tLBRACK",
    "p_lbracket : '['",
    "p_expr_basic : p_value",
    "p_expr_basic : p_const p_lparen p_args rparen",
    "p_expr_basic : p_const p_lparen p_find rparen",
    "p_expr_basic : p_const p_lparen p_kwargs rparen",
    "p_expr_basic : p_const p_lparen rparen",
    "p_expr_basic : p_const p_lbracket p_args rbracket",
    "p_expr_basic : p_const p_lbracket p_find rbracket",
    "p_expr_basic : p_const p_lbracket p_kwargs rbracket",
    "p_expr_basic : p_const p_lbracket rbracket",
    "p_expr_basic : tLBRACK p_args rbracket",
    "p_expr_basic : tLBRACK p_find rbracket",
    "p_expr_basic : tLBRACK rbracket",
    "p_expr_basic : tLBRACE p_kwargs tRCURLY",
    "p_expr_basic : tLBRACE tRCURLY",
    "p_expr_basic : tLPAREN p_expr rparen",
    "p_args : p_expr",
    "p_args : p_args_head",
    "p_args : p_args_head p_arg",
    "p_args : p_args_head tSTAR tIDENTIFIER",
    "p_args : p_args_head tSTAR tIDENTIFIER ',' p_args_post",
    "p_args : p_args_head tSTAR",
    "p_args : p_args_head tSTAR ',' p_args_post",
    "p_args : p_args_tail",
    "p_args_head : p_arg ','",
    "p_args_head : p_args_head p_arg ','",
    "p_args_tail : p_rest",
    "p_args_tail : p_rest ',' p_args_post",
    "p_args_post : p_arg",
    "p_args_post : p_args_post ',' p_arg",
    "p_find : p_rest ',' p_args_post ',' p_rest",
    "p_rest : tSTAR tIDENTIFIER",
    "p_rest : tSTAR",
    "p_arg : p_expr",
    "p_kwargs : p_kwarg ',' p_kwrest",
    "p_kwargs : p_kwarg",
    "p_kwargs : p_kwarg ','",
    "p_kwargs : p_kwrest",
    "p_kwargs : p_kwarg ',' p_kwnorest",
    "p_kwargs : p_kwnorest",
    "p_kwarg : p_kw",
    "p_kwarg : p_kwarg ',' p_kw",
    "p_kw : p_kw_label p_expr",
    "p_kw : p_kw_label",
    "p_kw_label : tLABEL",
    "p_kwrest : kwrest_mark tIDENTIFIER",
    "p_kwrest : kwrest_mark",
    "p_kwnorest : kwrest_mark kNIL",
    "p_value : p_primitive",
    "p_value : p_primitive tDOT2 p_primitive",
    "p_value : p_primitive tDOT3 p_primitive",
    "p_value : p_primitive tDOT2",
    "p_value : p_primitive tDOT3",
    "p_value : p_variable",
    "p_value : p_var_ref",
    "p_value : p_const",
    "p_value : tBDOT2 p_primitive",
    "p_value : tBDOT3 p_primitive",
    "p_primitive : literal",
    "p_primitive : strings",
    "p_primitive : xstring",
    "p_primitive : regexp",
    "p_primitive : words",
    "p_primitive : qwords",
    "p_primitive : symbols",
    "p_primitive : qsymbols",
    "p_primitive : keyword_variable",
    "p_primitive : lambda",
    "p_variable : tIDENTIFIER",
    "p_var_ref : tCARET tIDENTIFIER",
    "p_const : tCOLON3 cname",
    "p_const : p_const tCOLON2 cname",
    "p_const : tCONSTANT",
    "opt_rescue : kRESCUE exc_list exc_var then compstmt opt_rescue",
    "opt_rescue :",
    "exc_list : arg_value",
    "exc_list : mrhs",
    "exc_list : none",
    "exc_var : tASSOC lhs",
    "exc_var : none",
    "opt_ensure : kENSURE compstmt",
    "opt_ensure : none",
    "literal : numeric",
    "literal : symbol",
    "literal : dsym",
    "strings : string",
    "string : tCHAR",
    "string : string1",
    "string : string string1",
    "string1 : tSTRING_BEG string_contents tSTRING_END",
    "xstring : tXSTRING_BEG xstring_contents tSTRING_END",
    "regexp : tREGEXP_BEG regexp_contents tREGEXP_END",
    "words : tWORDS_BEG ' ' tSTRING_END",
    "words : tWORDS_BEG word_list tSTRING_END",
    "word_list :",
    "word_list : word_list word ' '",
    "word : string_content",
    "word : word string_content",
    "symbols : tSYMBOLS_BEG ' ' tSTRING_END",
    "symbols : tSYMBOLS_BEG symbol_list tSTRING_END",
    "symbol_list :",
    "symbol_list : symbol_list word ' '",
    "qwords : tQWORDS_BEG ' ' tSTRING_END",
    "qwords : tQWORDS_BEG qword_list tSTRING_END",
    "qsymbols : tQSYMBOLS_BEG ' ' tSTRING_END",
    "qsymbols : tQSYMBOLS_BEG qsym_list tSTRING_END",
    "qword_list :",
    "qword_list : qword_list tSTRING_CONTENT ' '",
    "qsym_list :",
    "qsym_list : qsym_list tSTRING_CONTENT ' '",
    "string_contents :",
    "string_contents : string_contents string_content",
    "xstring_contents :",
    "xstring_contents : xstring_contents string_content",
    "regexp_contents :",
    "regexp_contents : regexp_contents string_content",
    "string_content : tSTRING_CONTENT",
    "$$27 :",
    "string_content : tSTRING_DVAR $$27 string_dvar",
    "$$28 :",
    "string_content : tSTRING_DBEG $$28 compstmt tRCURLY",
    "string_dvar : tGVAR",
    "string_dvar : tIVAR",
    "string_dvar : tCVAR",
    "string_dvar : backref",
    "symbol : tSYMBEG sym",
    "sym : fname",
    "sym : tIVAR",
    "sym : tGVAR",
    "sym : tCVAR",
    "dsym : tSYMBEG xstring_contents tSTRING_END",
    "numeric : simple_numeric",
    "numeric : tUMINUS_NUM simple_numeric",
    "simple_numeric : tINTEGER",
    "simple_numeric : tFLOAT",
    "simple_numeric : tRATIONAL",
    "simple_numeric : tIMAGINARY",
    "user_variable : tIDENTIFIER",
    "user_variable : tIVAR",
    "user_variable : tGVAR",
    "user_variable : tCONSTANT",
    "user_variable : tCVAR",
    "keyword_variable : kNIL",
    "keyword_variable : kSELF",
    "keyword_variable : kTRUE",
    "keyword_variable : kFALSE",
    "keyword_variable : k__FILE__",
    "keyword_variable : k__LINE__",
    "keyword_variable : k__ENCODING__",
    "var_ref : user_variable",
    "var_ref : keyword_variable",
    "var_lhs : user_variable",
    "var_lhs : keyword_variable",
    "backref : tNTH_REF",
    "backref : tBACK_REF",
    "superclass : term",
    "$$29 :",
    "superclass : tLT $$29 expr_value term",
    "superclass : error term",
    "defn_head : kDEF fname",
    "$$30 :",
    "defs_head : kDEF singleton dot_or_colon $$30 fname",
    "f_paren_args : tLPAREN2 f_args rparen",
    "f_arglist : f_paren_args",
    "f_arglist : f_args term",
    "f_opt_paren_args : f_paren_args",
    "f_opt_paren_args :",
    "args_tail : f_kwarg ',' f_kwrest opt_f_block_arg",
    "args_tail : f_kwarg opt_f_block_arg",
    "args_tail : f_kwrest opt_f_block_arg",
    "args_tail : f_no_kwarg opt_f_block_arg",
    "args_tail : f_block_arg",
    "opt_args_tail : ',' args_tail",
    "opt_args_tail :",
    "f_args : f_arg ',' f_optarg ',' f_rest_arg opt_args_tail",
    "f_args : f_arg ',' f_optarg ',' f_rest_arg ',' f_arg opt_args_tail",
    "f_args : f_arg ',' f_optarg opt_args_tail",
    "f_args : f_arg ',' f_optarg ',' f_arg opt_args_tail",
    "f_args : f_arg ',' f_rest_arg opt_args_tail",
    "f_args : f_arg ',' f_rest_arg ',' f_arg opt_args_tail",
    "f_args : f_arg opt_args_tail",
    "f_args : f_optarg ',' f_rest_arg opt_args_tail",
    "f_args : f_optarg ',' f_rest_arg ',' f_arg opt_args_tail",
    "f_args : f_optarg opt_args_tail",
    "f_args : f_optarg ',' f_arg opt_args_tail",
    "f_args : f_rest_arg opt_args_tail",
    "f_args : f_rest_arg ',' f_arg opt_args_tail",
    "f_args : args_tail",
    "f_args : args_forward",
    "f_args : f_arg ',' args_forward",
    "f_args :",
    "args_forward : tBDOT3",
    "f_bad_arg : tCONSTANT",
    "f_bad_arg : tIVAR",
    "f_bad_arg : tGVAR",
    "f_bad_arg : tCVAR",
    "f_norm_arg : f_bad_arg",
    "f_norm_arg : tIDENTIFIER",
    "f_arg_item : f_norm_arg",
    "f_arg_item : tLPAREN f_margs rparen",
    "f_arg : f_arg_item",
    "f_arg : f_arg ',' f_arg_item",
    "f_label : tLABEL",
    "f_kw : f_label arg_value",
    "f_kw : f_label",
    "f_block_kw : tLABEL primary_value",
    "f_block_kw : tLABEL",
    "f_block_kwarg : f_block_kw",
    "f_block_kwarg : f_block_kwarg ',' f_block_kw",
    "f_kwarg : f_kw",
    "f_kwarg : f_kwarg ',' f_kw",
    "kwrest_mark : tPOW",
    "kwrest_mark : tDSTAR",
    "f_kwrest : kwrest_mark tIDENTIFIER",
    "f_kwrest : kwrest_mark",
    "f_no_kwarg : kwrest_mark kNIL",
    "f_opt : f_norm_arg '=' arg_value",
    "f_block_opt : tIDENTIFIER '=' primary_value",
    "f_block_optarg : f_block_opt",
    "f_block_optarg : f_block_optarg ',' f_block_opt",
    "f_optarg : f_opt",
    "f_optarg : f_optarg ',' f_opt",
    "restarg_mark : tSTAR2",
    "restarg_mark : tSTAR",
    "f_rest_arg : restarg_mark tIDENTIFIER",
    "f_rest_arg : restarg_mark",
    "blkarg_mark : tAMPER2",
    "blkarg_mark : tAMPER",
    "f_block_arg : blkarg_mark tIDENTIFIER",
    "opt_f_block_arg : ',' f_block_arg",
    "opt_f_block_arg :",
    "singleton : var_ref",
    "$$31 :",
    "singleton : tLPAREN2 $$31 expr rparen",
    "assoc_list : none",
    "assoc_list : assocs trailer",
    "assocs : assoc",
    "assocs : assocs ',' assoc",
    "assoc : arg_value tASSOC arg_value",
    "assoc : tLABEL arg_value",
    "assoc : tSTRING_BEG string_contents tLABEL_END arg_value",
    "assoc : tDSTAR arg_value",
    "operation : tIDENTIFIER",
    "operation : tCONSTANT",
    "operation : tFID",
    "operation2 : tIDENTIFIER",
    "operation2 : tCONSTANT",
    "operation2 : tFID",
    "operation2 : op",
    "operation3 : tIDENTIFIER",
    "operation3 : tFID",
    "operation3 : op",
    "dot_or_colon : tDOT",
    "dot_or_colon : tCOLON2",
    "call_op : tDOT",
    "call_op : tANDDOT",
    "opt_terms :",
    "opt_terms : terms",
    "opt_nl :",
    "opt_nl : '\\n'",
    "rparen : opt_nl tRPAREN",
    "rbracket : opt_nl tRBRACK",
    "trailer :",
    "trailer : '\\n'",
    "trailer : ','",
    "term : ';'",
    "term : '\\n'",
    "terms : term",
    "terms : terms ';'",
    "none :",
    "none_block_pass :",
    };

  /** debugging support, requires the package {@code jay.yydebug}.
      Set to {@code null} to suppress debugging messages.
    */
  protected jay.yydebug.yyDebug yydebug;

  /** index-checked interface to {@link #yyNames}.
      @param token single character or {@code %token} value.
      @return token name or {@code [illegal]} or {@code [unknown]}.
    */
  public static final String yyName (int token) {
    if (token < 0 || token > yyNames.length) return "[illegal]";
    String name;
    if ((name = yyNames[token]) != null) return name;
    return "[unknown]";
  }


  /** computes list of expected tokens on error by tracing the tables.
      @param state for which to compute the list.
      @return list of token names.
    */
  protected String[] yyExpecting (int state) {
    int token, n, len = 0;
    boolean[] ok = new boolean[yyNames.length];

    if ((n = yySindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }
    if ((n = yyRindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }

    String result[] = new String[len];
    for (n = token = 0; n < len;  ++ token)
      if (ok[token]) result[n++] = yyNames[token];
    return result;
  }

  /** the generated parser, with debugging messages.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @param ayydebug debug message writer implementing {@code yyDebug}, or {@code null}.
      @return result of the last reduction, if any.
      @throws IOException if crap happens
    */
  public Object yyparse (Lexer yyLex, Object ayydebug)
				throws java.io.IOException {
    this.yydebug = (jay.yydebug.yyDebug)ayydebug;
    return yyparse(yyLex);
  }

  /** initial size and increment of the state/value stack [default 256].
      This is not final so that it can be overwritten outside of invocations
      of {@link #yyparse}.
    */
  protected int yyMax;

  /** executed at the beginning of a reduce action.
      Used as {@code $$ = yyDefault($1)}, prior to the user-specified action, if any.
      Can be overwritten to provide deep copy, etc.
      @param first value for {@code $1}, or {@code null}.
      @return first.
    */
  protected Object yyDefault (Object first) {
    return first;
  }

  /** the generated parser.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @return result of the last reduction, if any.
      @throws IOException if crap happens
    */
  public Object yyparse (Lexer yyLex) throws java.io.IOException {
    if (yyMax <= 0) yyMax = 256;			// initial size
    int yyState = 0, yyStates[] = new int[yyMax];	// state stack
    Object yyVal = null, yyVals[] = new Object[yyMax];	// value stack
    int yyToken = -1;					// current input
    int yyErrorFlag = 0;				// #tokens to shift

    yyLoop: for (int yyTop = 0;; ++ yyTop) {
      if (yyTop >= yyStates.length) {			// dynamically increase
        int[] i = new int[yyStates.length+yyMax];
        System.arraycopy(yyStates, 0, i, 0, yyStates.length);
        yyStates = i;
        Object[] o = new Object[yyVals.length+yyMax];
        System.arraycopy(yyVals, 0, o, 0, yyVals.length);
        yyVals = o;
      }
      yyStates[yyTop] = yyState;
      yyVals[yyTop] = yyVal;
      if (yydebug != null) yydebug.push(yyState, yyVal);

      yyDiscarded: for (;;) {	// discarding a token does not change stack
        int yyN;
        if ((yyN = yyDefRed[yyState]) == 0) {	// else [default] reduce (yyN)
          if (yyToken < 0) {
//            yyToken = yyLex.advance() ? yyLex.token() : 0;
            yyToken = yyLex.nextToken();
            if (yydebug != null)
              yydebug.lex(yyState, yyToken, yyName(yyToken), yyLex.value());
          }
          if ((yyN = yySindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken) {
            if (yydebug != null)
              yydebug.shift(yyState, yyTable[yyN], yyErrorFlag-1);
            yyState = yyTable[yyN];		// shift to yyN
            yyVal = yyLex.value();
            yyToken = -1;
            if (yyErrorFlag > 0) -- yyErrorFlag;
            continue yyLoop;
          }
          if ((yyN = yyRindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken)
            yyN = yyTable[yyN];			// reduce (yyN)
          else
            switch (yyErrorFlag) {
  
            case 0:
              support.yyerror("syntax error", yyExpecting(yyState), yyNames[yyToken]);
              if (yydebug != null) yydebug.error("syntax error");
  
            case 1: case 2:
              yyErrorFlag = 3;
              do {
                if ((yyN = yySindex[yyStates[yyTop]]) != 0
                    && (yyN += yyErrorCode) >= 0 && yyN < yyTable.length
                    && yyCheck[yyN] == yyErrorCode) {
                  if (yydebug != null)
                    yydebug.shift(yyStates[yyTop], yyTable[yyN], 3);
                  yyState = yyTable[yyN];
                  yyVal = yyLex.value();
                  continue yyLoop;
                }
                if (yydebug != null) yydebug.pop(yyStates[yyTop]);
              } while (-- yyTop >= 0);
              if (yydebug != null) yydebug.reject();
              support.yyerror("irrecoverable syntax error");
  
            case 3:
              if (yyToken == 0) {
                if (yydebug != null) yydebug.reject();
                support.yyerror("irrecoverable syntax error at end-of-file");
              }
              if (yydebug != null)
                yydebug.discard(yyState, yyToken, yyName(yyToken),
  							yyLex.value());
              yyToken = -1;
              continue yyDiscarded;		// leave stack alone
            }
        }
        int yyV = yyTop + 1-yyLen[yyN];
        if (yydebug != null)
          yydebug.reduce(yyState, yyStates[yyV-1], yyN, yyRule[yyN], yyLen[yyN]);
        ParserState state = states[yyN];
        if (state == null) {
            yyVal = yyDefault(yyV > yyTop ? null : yyVals[yyV]);
        } else {
            yyVal = state.execute(support, lexer, yyVal, yyVals, yyTop);
        }
//        switch (yyN) {
// ACTIONS_END
//        }
        yyTop -= yyLen[yyN];
        yyState = yyStates[yyTop];
        int yyM = yyLhs[yyN];
        if (yyState == 0 && yyM == 0) {
          if (yydebug != null) yydebug.shift(0, yyFinal);
          yyState = yyFinal;
          if (yyToken < 0) {
            yyToken = yyLex.nextToken();
//            yyToken = yyLex.advance() ? yyLex.token() : 0;
            if (yydebug != null)
               yydebug.lex(yyState, yyToken,yyName(yyToken), yyLex.value());
          }
          if (yyToken == 0) {
            if (yydebug != null) yydebug.accept(yyVal);
            return yyVal;
          }
          continue yyLoop;
        }
        if ((yyN = yyGindex[yyM]) != 0 && (yyN += yyState) >= 0
            && yyN < yyTable.length && yyCheck[yyN] == yyState)
          yyState = yyTable[yyN];
        else
          yyState = yyDgoto[yyM];
        if (yydebug != null) yydebug.shift(yyStates[yyTop], yyState);
        continue yyLoop;
      }
    }
  }

static ParserState[] states = new ParserState[729];
static {
states[1] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.setState(LexState.EXPR_BEG);
                  support.initTopLocalVariables();
    return yyVal;
  }
};
states[2] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
  /* ENEBO: Removed !compile_for_eval which probably is to reduce warnings*/
                  if (((Node)yyVals[0+yyTop]) != null) {
                      /* last expression should not be void */
                      if (((Node)yyVals[0+yyTop]) instanceof BlockNode) {
                          support.checkUselessStatement(((BlockNode)yyVals[0+yyTop]).getLast());
                      } else {
                          support.checkUselessStatement(((Node)yyVals[0+yyTop]));
                      }
                  }
                  support.getResult().setAST(support.addRootNode(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[3] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((Node)yyVals[-1+yyTop]) instanceof BlockNode) {
                      support.checkUselessStatements(((BlockNode)yyVals[-1+yyTop]));
                  }
                  yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[5] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop]), true));
    return yyVal;
  }
};
states[6] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((Node)yyVals[-2+yyTop]), support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop]), true)));
    return yyVal;
  }
};
states[7] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[9] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("BEGIN in method");
                    }
    return yyVal;
  }
};
states[10] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.getResult().addBeginNode(new PreExe19Node(((Token)yyVals[-4+yyTop]).getPosition(), support.getCurrentScope(), ((Node)yyVals[-1+yyTop])));
                    yyVal = null;
    return yyVal;
  }
};
states[11] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  Node node = ((Node)yyVals[-3+yyTop]);

                  if (((RescueBodyNode)yyVals[-2+yyTop]) != null) {
                      node = new RescueNode(support.getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop]), ((RescueBodyNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
                  } else if (((Node)yyVals[-1+yyTop]) != null) {
                      support.warn(ID.ELSE_WITHOUT_RESCUE, support.getPosition(((Node)yyVals[-3+yyTop])), "else without rescue is useless");
                      node = support.appendToBlock(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
                  }
                  if (((Node)yyVals[0+yyTop]) != null) {
                      node = new EnsureNode(support.getPosition(((Node)yyVals[-3+yyTop])), node, ((Node)yyVals[0+yyTop]));
                  }

                  yyVal = node;
    return yyVal;
  }
};
states[12] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-1+yyTop]) instanceof BlockNode) {
                        support.checkUselessStatements(((BlockNode)yyVals[-1+yyTop]));
                    }
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[14] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop]), true));
    return yyVal;
  }
};
states[15] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((Node)yyVals[-2+yyTop]), support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop]), true)));
    return yyVal;
  }
};
states[16] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[17] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[18] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   support.yyerror("BEGIN is permitted only at toplevel");
    return yyVal;
  }
};
states[19] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new BeginNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop])), ((Node)yyVals[-3+yyTop]));
    return yyVal;
  }
};
states[20] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[21] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newAlias(support.union(((Token)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[22] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new VAliasNode(((Token)yyVals[-2+yyTop]).getPosition(), (String) ((Token)yyVals[-1+yyTop]).getValue(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[23] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new VAliasNode(((Token)yyVals[-2+yyTop]).getPosition(), (String) ((Token)yyVals[-1+yyTop]).getValue(), "$" + ((BackRefNode)yyVals[0+yyTop]).getType());
    return yyVal;
  }
};
states[24] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("can't make alias for the number variables");
    return yyVal;
  }
};
states[25] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[26] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null);
    return yyVal;
  }
};
states[27] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), null, ((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[28] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                        yyVal = new WhileNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((BeginNode)yyVals[-2+yyTop]).getBodyNode(), false);
                    } else {
                        yyVal = new WhileNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), true);
                    }
    return yyVal;
  }
};
states[29] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                        yyVal = new UntilNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((BeginNode)yyVals[-2+yyTop]).getBodyNode(), false);
                    } else {
                        yyVal = new UntilNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), true);
                    }
    return yyVal;
  }
};
states[30] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]);
                    yyVal = new RescueNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(support.getPosition(((Node)yyVals[-2+yyTop])), null, body, null), null);
    return yyVal;
  }
};
states[31] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.warn(ID.END_IN_METHOD, ((Token)yyVals[-3+yyTop]).getPosition(), "END in method; use at_exit");
                    }
                    yyVal = new PostExeNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[33] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                    yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[34] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    SourcePosition pos = support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    String asgnOp = (String) ((Token)yyVals[-1+yyTop]).getValue();
                    if (asgnOp.equals("||")) {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                        yyVal = new OpAsgnOrNode(pos, support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
                    } else if (asgnOp.equals("&&")) {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                        yyVal = new OpAsgnAndNode(pos, support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
                    } else {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(((AssignableNode)yyVals[-2+yyTop])), asgnOp, ((Node)yyVals[0+yyTop])));
                        ((AssignableNode)yyVals[-2+yyTop]).setPosition(pos);
                        yyVal = ((AssignableNode)yyVals[-2+yyTop]);
                    }
    return yyVal;
  }
};
states[35] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
  /* FIXME: arg_concat logic missing for opt_call_args*/
                    yyVal = support.new_opElementAsgnNode(support.union(((Node)yyVals[-5+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), (String) ((Token)yyVals[-1+yyTop]).getValue(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[36] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOpAsgn(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), (String) ((Token)yyVals[-3+yyTop]).getValue(), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[37] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOpAsgn(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), (String) ((Token)yyVals[-3+yyTop]).getValue(), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[38] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("can't make alias for the number variables");
                    yyVal = null;
    return yyVal;
  }
};
states[39] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOpAsgn(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), (String) ((Token)yyVals[-3+yyTop]).getValue(), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[40] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[41] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[42] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]);
                    Node rescued = new RescueNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(support.getPosition(((Node)yyVals[-2+yyTop])), null, body, null), null);
                    ((AssignableNode)yyVals[-4+yyTop]).setValueNode(rescued);
                    yyVal = ((MultipleAsgnNode)yyVals[-4+yyTop]);
                    ((MultipleAsgnNode)yyVals[-4+yyTop]).setPosition(support.getPosition(((MultipleAsgnNode)yyVals[-4+yyTop])));
    return yyVal;
  }
};
states[43] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                    yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
                    ((MultipleAsgnNode)yyVals[-2+yyTop]).setPosition(support.getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])));
    return yyVal;
  }
};
states[45] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[46] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[47] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]);
                    yyVal = new RescueNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, body, null), null);
    return yyVal;
  }
};
states[50] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newAndNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[51] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOrNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[52] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Token)yyVals[-2+yyTop]), support.getConditionNode(((Node)yyVals[0+yyTop])));
                    ((CallNode)yyVal).setName("!");
    return yyVal;
  }
};
states[53] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Token)yyVals[-1+yyTop]), support.getConditionNode(((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[54] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = false;
    return yyVal;
  }
};
states[55] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-3+yyTop]));
                    InNode in = ((ParserSupport30)support).newInNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]), null, null);
                    yyVal = ((ParserSupport30)support).newCaseInNode(support.union(((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), in);
    return yyVal;
  }
};
states[56] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = false;
    return yyVal;
  }
};
states[57] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-3+yyTop]));
                    InNode in = ((ParserSupport30)support).newInNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]), null, null);
                    yyVal = ((ParserSupport30)support).newCaseInNode(support.union(((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), in);
    return yyVal;
  }
};
states[59] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[63] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[64] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[65] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(((Token)yyVals[-4+yyTop]).getPosition(), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[66] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_fcall(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[67] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                    yyVal = ((FCallNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[68] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
                    yyVal = ((FCallNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[69] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[70] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
    return yyVal;
  }
};
states[71] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[72] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[73] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop])); /* .setPosFrom($2);*/
    return yyVal;
  }
};
states[74] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_yield(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[75] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ReturnNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), ((Node)yyVals[0+yyTop]).getPosition()));
    return yyVal;
  }
};
states[76] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BreakNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), ((Node)yyVals[0+yyTop]).getPosition()));
    return yyVal;
  }
};
states[77] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new NextNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), ((Node)yyVals[0+yyTop]).getPosition()));
    return yyVal;
  }
};
states[79] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[80] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((MultipleAsgnNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[81] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                    yyVal = new MultipleAsgnNode(pos, support.newArrayNode(pos, ((Node)yyVals[-1+yyTop])), null, null);
    return yyVal;
  }
};
states[82] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[83] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]).add(((Node)yyVals[0+yyTop])), null, null);
    return yyVal;
  }
};
states[84] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), (ListNode) null);
    return yyVal;
  }
};
states[85] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]), ((Node)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[86] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[87] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), new StarNode(lexer.getPosition()), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[88] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[89] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-3+yyTop]).getPosition(), null, ((Node)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[90] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      yyVal = new MultipleAsgnNode(((Token)yyVals[0+yyTop]).getPosition(), null, new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[91] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      yyVal = new MultipleAsgnNode(((Token)yyVals[-2+yyTop]).getPosition(), null, new StarNode(lexer.getPosition()), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[93] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[94] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[95] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[96] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[97] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[98] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[99] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[100] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[101] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[102] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[103] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[104] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon2(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[105] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon3(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[106] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[107] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[108] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[109] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[110] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[111] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[112] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[113] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon2(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[114] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon3(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[115] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[116] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("class/module name must be CONSTANT");
    return yyVal;
  }
};
states[118] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon3(((Token)yyVals[-1+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[119] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(((Token)yyVals[0+yyTop]).getPosition(), null, (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[120] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[124] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_ENDFN);
                   yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[125] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_ENDFN);
                   yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[126] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new LiteralNode(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[127] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new LiteralNode(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[128] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((LiteralNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[129] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[130] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newUndef(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[131] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[132] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((Node)yyVals[-3+yyTop]), support.newUndef(((Node)yyVals[-3+yyTop]).getPosition(), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[205] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    /* FIXME: Consider fixing node_assign itself rather than single case*/
                    ((Node)yyVal).setPosition(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[206] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    SourcePosition pos = support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    String asgnOp = (String) ((Token)yyVals[-1+yyTop]).getValue();
                    if (asgnOp.equals("||")) {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                        yyVal = new OpAsgnOrNode(pos, support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
                    } else if (asgnOp.equals("&&")) {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                        yyVal = new OpAsgnAndNode(pos, support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
                    } else {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(((AssignableNode)yyVals[-2+yyTop])), asgnOp, ((Node)yyVals[0+yyTop])));
                        ((AssignableNode)yyVals[-2+yyTop]).setPosition(pos);
                        yyVal = ((AssignableNode)yyVals[-2+yyTop]);
                    }
    return yyVal;
  }
};
states[207] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
  /* FIXME: arg_concat missing for opt_call_args*/
                    yyVal = support.new_opElementAsgnNode(support.union(((Node)yyVals[-5+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), (String) ((Token)yyVals[-1+yyTop]).getValue(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[208] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOpAsgn(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), (String) ((Token)yyVals[-3+yyTop]).getValue(), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[209] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOpAsgn(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), (String) ((Token)yyVals[-3+yyTop]).getValue(), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[210] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOpAsgn(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), (String) ((Token)yyVals[-3+yyTop]).getValue(), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[211] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("constant re-assignment");
    return yyVal;
  }
};
states[212] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("constant re-assignment");
    return yyVal;
  }
};
states[213] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[214] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));
    
                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[215] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]),  ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[216] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-1+yyTop]));

                    boolean isLiteral = ((Node)yyVals[-1+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), new ImplicitNilNode(((Token)yyVals[0+yyTop]).getPosition()), false, isLiteral);
    return yyVal;
  }
};
states[217] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-1+yyTop]));

                    boolean isLiteral = ((Node)yyVals[-1+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), new ImplicitNilNode(((Token)yyVals[0+yyTop]).getPosition()), true, isLiteral);
    return yyVal;
  }
};
states[218] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    boolean isLiteral = ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), new ImplicitNilNode(((Token)yyVals[-1+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[219] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    boolean isLiteral = ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), new ImplicitNilNode(((Token)yyVals[-1+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[220] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "+", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[221] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "-", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[222] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "*", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[223] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "/", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[224] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "%", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[225] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[226] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((NumericNode)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[227] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getUnaryCallNode(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[228] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getUnaryCallNode(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[229] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "|", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[230] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "^", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[231] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "&", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[232] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=>", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[233] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[234] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">=", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[235] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[236] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[237] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "==", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[238] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "===", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[239] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "!=", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[240] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getMatchNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                  /* ENEBO
                        $$ = match_op($1, $3);
                        if (nd_type($1) == NODE_LIT && TYPE($1->nd_lit) == T_REGEXP) {
                            $$ = reg_named_capture_assign($1->nd_lit, $$);
                        }
                  */
    return yyVal;
  }
};
states[241] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "!~", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[242] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), "!");
    return yyVal;
  }
};
states[243] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]), "~");
    return yyVal;
  }
};
states[244] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<<", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[245] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">>", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[246] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newAndNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[247] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOrNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[248] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new DefinedNode(((Token)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[249] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.getPosition(((Node)yyVals[-5+yyTop])), support.getConditionNode(((Node)yyVals[-5+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[250] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]);

                    yyVal = new DefnNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
    return yyVal;
  }
};
states[251] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = new RescueNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[0+yyTop]), null), null);

                    yyVal = new DefnNode(support.union(((DefHolder)yyVals[-5+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-5+yyTop]).nameNode, ((ArgsNode)yyVals[-4+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
    return yyVal;
  }
};
states[252] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]);

                    yyVal = new DefsNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).receiver, ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[253] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = new RescueNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[0+yyTop]), null), null);

                    yyVal = new DefsNode(support.union(((DefHolder)yyVals[-5+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-5+yyTop]).receiver, ((DefHolder)yyVals[-5+yyTop]).nameNode, ((ArgsNode)yyVals[-4+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[254] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[255] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[256] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    Node body = ((Node)yyVals[0+yyTop]);
                    yyVal = new RescueNode(position, ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, body, null), null);
    return yyVal;
  }
};
states[257] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[259] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[260] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[261] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((ListNode)yyVals[-1+yyTop]).getPosition(), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[262] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                    
                    if (((Node)yyVals[-1+yyTop]) == null) {
                        yyVal = new ArrayNode(pos);
                    } else {
                        yyVal = ((Node)yyVals[-1+yyTop]);
                        ((Node)yyVal).setPosition(pos);
                    }
    return yyVal;
  }
};
states[263] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                    Node fwd = support.getCurrentScope().declare(((Token)yyVals[-1+yyTop]).getPosition(), "...");
                    Node splat = support.newSplatNode(pos, fwd);
                    yyVal = support.newArrayNode(pos, splat);
    return yyVal;
  }
};
states[264] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop]));
                    Node fwd = support.getCurrentScope().declare(((Token)yyVals[-1+yyTop]).getPosition(), "...");
                    Node splat = support.newSplatNode(((Token)yyVals[-1+yyTop]).getPosition(), fwd);
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), splat);
                    ((Node)yyVal).setPosition(pos);
    return yyVal;
  }
};
states[269] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[270] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[271] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((ListNode)yyVals[-1+yyTop]).getPosition(), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[272] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[273] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_blk_pass(((Node)yyVals[-1+yyTop]), ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[274] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((ListNode)yyVals[-1+yyTop]).getPosition(), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
                    yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[275] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
                    yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[276] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[277] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = Long.valueOf(lexer.getCmdArgumentState().begin());
    return yyVal;
  }
};
states[278] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getCmdArgumentState().reset(((Long)yyVals[-1+yyTop]).longValue());
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[279] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockPassNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[280] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((BlockPassNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[282] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Node)yyVals[0+yyTop]) == null ? lexer.getPosition() : ((Node)yyVals[0+yyTop]).getPosition();
                    yyVal = support.newArrayNode(pos, ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[283] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newSplatNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[284] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node = support.splat_array(((Node)yyVals[-2+yyTop]));

                    if (node != null) {
                        yyVal = support.list_append(node, ((Node)yyVals[0+yyTop]));
                    } else {
                        yyVal = support.arg_append(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    }
    return yyVal;
  }
};
states[285] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node = null;

                    /* FIXME: lose syntactical elements here (and others like this)*/
                    if (((Node)yyVals[0+yyTop]) instanceof ArrayNode &&
                        (node = support.splat_array(((Node)yyVals[-3+yyTop]))) != null) {
                        yyVal = support.list_concat(node, ((Node)yyVals[0+yyTop]));
                    } else {
                        yyVal = support.arg_concat(support.getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
                    }
    return yyVal;
  }
};
states[286] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[287] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[288] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node = support.splat_array(((Node)yyVals[-2+yyTop]));

                    if (node != null) {
                        yyVal = support.list_append(node, ((Node)yyVals[0+yyTop]));
                    } else {
                        yyVal = support.arg_append(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    }
    return yyVal;
  }
};
states[289] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node = null;

                    if (((Node)yyVals[0+yyTop]) instanceof ArrayNode &&
                        (node = support.splat_array(((Node)yyVals[-3+yyTop]))) != null) {
                        yyVal = support.list_concat(node, ((Node)yyVals[0+yyTop]));
                    } else {
                        yyVal = support.arg_concat(support.union(((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
                    }
    return yyVal;
  }
};
states[290] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newSplatNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));  
    return yyVal;
  }
};
states[297] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((ListNode)yyVals[0+yyTop]); /* FIXME: Why complaining without $$ = $1;*/
    return yyVal;
  }
};
states[298] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((ListNode)yyVals[0+yyTop]); /* FIXME: Why complaining without $$ = $1;*/
    return yyVal;
  }
};
states[301] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.new_fcall(((Token)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[302] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BeginNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[303] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_ENDARG);
    return yyVal;
  }
};
states[304] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null; /*FIXME: Should be implicit nil?*/
    return yyVal;
  }
};
states[305] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_ENDARG); 
    return yyVal;
  }
};
states[306] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.warning(ID.GROUPED_EXPRESSION, ((Token)yyVals[-3+yyTop]).getPosition(), "(...) interpreted as grouped expression");
                    yyVal = ((Node)yyVals[-2+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[307] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                    Node value = ((Node)yyVals[-1+yyTop]) == null ? new ImplicitNilNode(pos) : ((Node)yyVals[-1+yyTop]);
                    yyVal = value;
                    ((ISourcePositionHolder)yyVal).setPosition(pos);
    return yyVal;
  }
};
states[308] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[309] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon3(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[310] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition position = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                    if (((Node)yyVals[-1+yyTop]) == null) {
                        yyVal = new ZArrayNode(position); /* zero length array */
                    } else {
                        yyVal = ((Node)yyVals[-1+yyTop]);
                        ((ISourcePositionHolder)yyVal).setPosition(position);
                    }
    return yyVal;
  }
};
states[311] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new HashNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[312] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ReturnNode(((Token)yyVals[0+yyTop]).getPosition(), null);
    return yyVal;
  }
};
states[313] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_yield(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[314] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZYieldNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[315] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZYieldNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[316] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new DefinedNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[317] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Token)yyVals[-3+yyTop]), support.getConditionNode(((Node)yyVals[-1+yyTop])));
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                    ((CallNode)yyVal).setName("!");
    return yyVal;
  }
};
states[318] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Token)yyVals[-2+yyTop]), null);

                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
                    ((CallNode)yyVal).setName("!");
    return yyVal;
  }
};
states[319] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-1+yyTop]), null, ((IterNode)yyVals[0+yyTop]));
                    yyVal = ((FCallNode)yyVals[-1+yyTop]);                    
    return yyVal;
  }
};
states[321] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-1+yyTop]) != null && 
                          ((BlockAcceptingNode)yyVals[-1+yyTop]).getIter() instanceof BlockPassNode) {
                        throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, ((Node)yyVals[-1+yyTop]).getPosition(), lexer.getCurrentLine(), "Both block arg and actual block given.");
                    }
                    ((BlockAcceptingNode)yyVals[-1+yyTop]).setIter(((IterNode)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[322] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((LambdaNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[323] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[324] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[325] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().begin();
    return yyVal;
  }
};
states[326] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().end();
    return yyVal;
  }
};
states[327] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);
                    yyVal = new WhileNode(support.union(((Token)yyVals[-6+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), body);
    return yyVal;
  }
};
states[328] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().begin();
    return yyVal;
  }
};
states[329] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().end();
    return yyVal;
  }
};
states[330] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);
                    yyVal = new UntilNode(support.union(((Token)yyVals[-6+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), body);
    return yyVal;
  }
};
states[331] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newCaseNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[332] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newCaseNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), null, ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[333] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newCaseInNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[334] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().begin();
    return yyVal;
  }
};
states[335] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().end();
    return yyVal;
  }
};
states[336] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      /* ENEBO: Lots of optz in 1.9 parser here*/
                    yyVal = new ForNode(support.union(((Token)yyVals[-8+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-7+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-4+yyTop]), support.getCurrentScope());
    return yyVal;
  }
};
states[337] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("class definition in method body");
                    }
                    support.pushLocalScope();
    return yyVal;
  }
};
states[338] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);

                    yyVal = new ClassNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-4+yyTop]), support.getCurrentScope(), body, ((Node)yyVals[-3+yyTop]));
                    support.popCurrentScope();
    return yyVal;
  }
};
states[339] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = Boolean.valueOf(support.isInDef());
                    support.setInDef(false);
    return yyVal;
  }
};
states[340] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = Integer.valueOf(support.getInSingle());
                    support.setInSingle(0);
                    support.pushLocalScope();
    return yyVal;
  }
};
states[341] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new SClassNode(support.union(((Token)yyVals[-7+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                    support.popCurrentScope();
                    support.setInDef(((Boolean)yyVals[-4+yyTop]).booleanValue());
                    support.setInSingle(((Integer)yyVals[-2+yyTop]).intValue());
    return yyVal;
  }
};
states[342] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) { 
                        support.yyerror("module definition in method body");
                    }
                    support.pushLocalScope();
    return yyVal;
  }
};
states[343] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);

                    yyVal = new ModuleNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-3+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
    return yyVal;
  }
};
states[344] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);

                    yyVal = new DefnNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Token)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
    return yyVal;
  }
};
states[345] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);

                    yyVal = new DefsNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Token)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).receiver, ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[346] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BreakNode(((Token)yyVals[0+yyTop]).getPosition(), null);
    return yyVal;
  }
};
states[347] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new NextNode(((Token)yyVals[0+yyTop]).getPosition(), null);
    return yyVal;
  }
};
states[348] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new RedoNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[349] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new RetryNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[350] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[357] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(((Token)yyVals[-4+yyTop]).getPosition(), support.getConditionNode(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[359] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[361] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[362] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[363] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[364] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[365] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[366] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[367] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), support.assignable(((Token)yyVals[0+yyTop]), null), null);
    return yyVal;
  }
};
states[368] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-5+yyTop]).getPosition(), ((ListNode)yyVals[-5+yyTop]), support.assignable(((Token)yyVals[-2+yyTop]), null), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[369] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[370] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]), new StarNode(lexer.getPosition()), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[371] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-1+yyTop]).getPosition(), null, support.assignable(((Token)yyVals[0+yyTop]), null), null);
    return yyVal;
  }
};
states[372] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-3+yyTop]).getPosition(), null, support.assignable(((Token)yyVals[-2+yyTop]), null), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[373] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[0+yyTop]).getPosition(), null, new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[374] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-2+yyTop]).getPosition(), null, null, ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[375] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[376] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[377] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[378] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArgsTailHolder(((KeywordRestArgNode)yyVals[-1+yyTop]).getPosition(), null, ((KeywordRestArgNode)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[379] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((BlockArgNode)yyVals[0+yyTop]).getPosition(), null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[380] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsTailHolder)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[381] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(lexer.getPosition(), null, null, null);
    return yyVal;
  }
};
states[382] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[383] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-7+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-7+yyTop]), ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[384] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[385] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[386] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[387] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    RestArgNode rest = new UnnamedRestArgNode(((ListNode)yyVals[-1+yyTop]).getPosition(), null, support.getCurrentScope().addVariable("*"));
                    yyVal = support.new_args(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, rest, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[388] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[389] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]), null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[390] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[391] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[392] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[393] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[394] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((RestArgNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[395] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((RestArgNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[396] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ArgsTailHolder)yyVals[0+yyTop]).getPosition(), null, null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[397] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* was $$ = null;*/
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[398] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.commandStart = true;
                    yyVal = ((ArgsNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[399] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, null, null, null, (BlockArgNode) null);
                    ((ArgsNode)yyVal).setShadow(((ListNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[400] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((Token)yyVals[0+yyTop]).getPosition(), null, null, null, null, (BlockArgNode) null);
    return yyVal;
  }
};
states[401] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ((Node)yyVals[-2+yyTop]).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                    ((ArgsNode)yyVals[-2+yyTop]).setShadow(((ListNode)yyVals[-1+yyTop]));
                    yyVal = ((ArgsNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[402] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[403] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[404] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[405] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[406] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_bv(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[407] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[408] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
                    yyVal = lexer.getLeftParenBegin();
                    lexer.setLeftParenBegin(lexer.incrementParenNest());
    return yyVal;
  }
};
states[409] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new LambdaNode(support.union(((ArgsNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((ArgsNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
                    lexer.setLeftParenBegin(((Integer)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[410] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-2+yyTop]);
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[411] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[412] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[413] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[414] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[415] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[416] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    /* Workaround for JRUBY-2326 (MRI does not enter this production for some reason)*/
                    if (((Node)yyVals[-1+yyTop]) instanceof YieldNode) {
                        throw new SyntaxException(PID.BLOCK_GIVEN_TO_YIELD, ((Node)yyVals[-1+yyTop]).getPosition(), lexer.getCurrentLine(), "block given to yield");
                    }
                    if (((BlockAcceptingNode)yyVals[-1+yyTop]).getIter() instanceof BlockPassNode) {
                        throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, ((Node)yyVals[-1+yyTop]).getPosition(), lexer.getCurrentLine(), "Both block arg and actual block given.");
                    }
                    ((BlockAcceptingNode)yyVals[-1+yyTop]).setIter(((IterNode)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[417] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((BlockAcceptingNode)yyVals[-1+yyTop]).getIter() instanceof BlockPassNode) {
                        throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, ((Node)yyVals[-1+yyTop]).getPosition(), lexer.getCurrentLine(), "Both block arg and actual block given.");
                    }
                    ((BlockAcceptingNode)yyVals[-1+yyTop]).setIter(((IterNode)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[418] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[419] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[420] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[421] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                    yyVal = ((FCallNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[422] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[423] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[424] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[425] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-2+yyTop]), new Token("call", ((Node)yyVals[-2+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[426] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-2+yyTop]), new Token("call", ((Node)yyVals[-2+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[427] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[428] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZSuperNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[429] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-3+yyTop]) instanceof SelfNode) {
                      yyVal = support.new_fcall(new Token("[]", support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]))), ((Node)yyVals[-1+yyTop]), null);
                    } else {
                      yyVal = support.new_call(((Node)yyVals[-3+yyTop]), new Token("[]", support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]))), ((Node)yyVals[-1+yyTop]), null);
                    }
    return yyVal;
  }
};
states[430] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[431] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[432] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[433] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[434] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newWhenNode(support.union(((Token)yyVals[-4+yyTop]), support.unwrapNewlineNode(((Node)yyVals[-1+yyTop]))), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[437] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = false;
    return yyVal;
  }
};
states[438] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newInNode(support.union(((Token)yyVals[-5+yyTop]), support.unwrapNewlineNode(((Node)yyVals[-1+yyTop]))), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[442] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null);
    return yyVal;
  }
};
states[443] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), null, ((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[445] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ArrayPatternNode tail = ((ParserSupport30)support).newArrayPatternTail(((Node)yyVals[-1+yyTop]).getPosition(), null, true, null, null);
                    yyVal = ((ParserSupport30)support).newArrayPattern(((Node)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[-1+yyTop]), tail);
    return yyVal;
  }
};
states[446] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPattern(((Node)yyVals[-2+yyTop]).getPosition(), null, ((Node)yyVals[-2+yyTop]), ((ArrayPatternNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[447] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newFindPattern(((FindPatternNode)yyVals[0+yyTop]).getPosition(), null, ((FindPatternNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[448] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPattern(((ArrayPatternNode)yyVals[0+yyTop]).getPosition(), null, null, ((ArrayPatternNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[449] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPattern(((HashPatternNode)yyVals[0+yyTop]).getPosition(), null, ((HashPatternNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[451] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newPatternBind(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[453] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOrNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[460] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), null, ((ArrayPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[461] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newFindPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((FindPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[462] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((HashPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[463] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ArrayPatternNode tail = ((ParserSupport30)support).newArrayPatternTail(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, false, null, null);
                    yyVal = ((ParserSupport30)support).newArrayPattern(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null, tail);
    return yyVal;
  }
};
states[464] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), null, ((ArrayPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[465] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newFindPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((FindPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[466] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((HashPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[467] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ArrayPatternNode tail = ((ParserSupport30)support).newArrayPatternTail(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, false, null, null);
                    yyVal = ((ParserSupport30)support).newArrayPattern(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null, tail);
    return yyVal;
  }
};
states[468] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPattern(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, null, ((ArrayPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[469] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newFindPattern(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, ((FindPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[470] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ArrayPatternNode tail = ((ParserSupport30)support).newArrayPatternTail(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), null, false, null, null);
                    yyVal = ((ParserSupport30)support).newArrayPattern(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), null, null, tail);
    return yyVal;
  }
};
states[471] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPattern(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, ((HashPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[472] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    HashPatternNode tail = ((ParserSupport30)support).newHashPatternTail(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), null, null, false);
                    yyVal = ((ParserSupport30)support).newHashPattern(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), null, tail);
    return yyVal;
  }
};
states[473] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[474] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ListNode pre = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
                    yyVal = ((ParserSupport30)support).newArrayPatternTail(((Node)yyVals[0+yyTop]).getPosition(), pre, false, null, null);
    return yyVal;
  }
};
states[475] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPatternTail(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), true, null, null);
    return yyVal;
  }
};
states[476] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ListNode pre = ((ListNode)yyVals[-1+yyTop]);
                    for (Node n : ((ListNode)yyVals[0+yyTop]).childNodes()) pre.add(n);
                    yyVal = ((ParserSupport30)support).newArrayPatternTail(((ListNode)yyVals[-1+yyTop]).getPosition(), pre, false, null, null);
    return yyVal;
  }
};
states[477] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPatternTail(support.union(((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((ListNode)yyVals[-2+yyTop]), true, ((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[478] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPatternTail(support.union(((ListNode)yyVals[-4+yyTop]), ((ListNode)yyVals[0+yyTop])), ((ListNode)yyVals[-4+yyTop]), true, ((Token)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[479] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPatternTail(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), true, null, null);
    return yyVal;
  }
};
states[480] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPatternTail(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), true, null, ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[482] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[483] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ListNode head = ((ListNode)yyVals[-2+yyTop]);
                    for (Node n : ((ListNode)yyVals[-1+yyTop]).childNodes()) head.add(n);
                    yyVal = head;
    return yyVal;
  }
};
states[484] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPatternTail(((Token)yyVals[0+yyTop]) != null ? ((Token)yyVals[0+yyTop]).getPosition() : lexer.getPosition(), null, true, ((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[485] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newArrayPatternTail(((Token)yyVals[-2+yyTop]) != null ? ((Token)yyVals[-2+yyTop]).getPosition() : lexer.getPosition(), null, true, ((Token)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[487] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ListNode post = ((ListNode)yyVals[-2+yyTop]);
                    for (Node n : ((ListNode)yyVals[0+yyTop]).childNodes()) post.add(n);
                    yyVal = post;
    return yyVal;
  }
};
states[488] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newFindPatternTail(((ListNode)yyVals[-2+yyTop]).getPosition(), ((Token)yyVals[-4+yyTop]), ((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[489] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[490] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[491] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[492] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPatternTail(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]), false);
    return yyVal;
  }
};
states[493] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPatternTail(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), null, false);
    return yyVal;
  }
};
states[494] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPatternTail(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, false);
    return yyVal;
  }
};
states[495] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPatternTail(((Token)yyVals[0+yyTop]).getPosition(), null, ((Token)yyVals[0+yyTop]), false);
    return yyVal;
  }
};
states[496] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPatternTail(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), null, true);
    return yyVal;
  }
};
states[497] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).newHashPatternTail(((Token)yyVals[0+yyTop]).getPosition(), null, null, true);
    return yyVal;
  }
};
states[499] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).addAll(((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[500] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Token)yyVals[-1+yyTop]).getPosition();
                    yyVal = support.newArrayNode(pos, new SymbolNode(pos, (String) ((Token)yyVals[-1+yyTop]).getValue())).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[501] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Token)yyVals[0+yyTop]).getPosition();
                    Node value = ((ParserSupport30)support).assignablePatternVariable(pos, (String) ((Token)yyVals[0+yyTop]).getValue());
                    yyVal = support.newArrayNode(pos, new SymbolNode(pos, (String) ((Token)yyVals[0+yyTop]).getValue())).add(value);
    return yyVal;
  }
};
states[502] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[503] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[504] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[505] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[507] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[508] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[509] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-1+yyTop]));
                    boolean isLiteral = ((Node)yyVals[-1+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), new ImplicitNilNode(((Token)yyVals[0+yyTop]).getPosition()), false, isLiteral);
    return yyVal;
  }
};
states[510] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-1+yyTop]));
                    boolean isLiteral = ((Node)yyVals[-1+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), new ImplicitNilNode(((Token)yyVals[0+yyTop]).getPosition()), true, isLiteral);
    return yyVal;
  }
};
states[514] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    boolean isLiteral = ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), new ImplicitNilNode(((Token)yyVals[-1+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[515] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    boolean isLiteral = ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), new ImplicitNilNode(((Token)yyVals[-1+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[524] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[526] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).assignablePatternVariable(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[527] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport30)support).patternVarRef(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[528] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Colon3Node(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[529] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[530] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[531] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node;
                    if (((Node)yyVals[-3+yyTop]) != null) {
                        node = support.appendToBlock(support.node_assign(((Node)yyVals[-3+yyTop]), new GlobalVarNode(((Token)yyVals[-5+yyTop]).getPosition(), "$!")), ((Node)yyVals[-1+yyTop]));
                        if (((Node)yyVals[-1+yyTop]) != null) {
                            node.setPosition(support.unwrapNewlineNode(((Node)yyVals[-1+yyTop])).getPosition());
                        }
                    } else {
                        node = ((Node)yyVals[-1+yyTop]);
                    }
                    Node body = node;
                    yyVal = new RescueBodyNode(((Token)yyVals[-5+yyTop]).getPosition(), ((Node)yyVals[-4+yyTop]), body, ((RescueBodyNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[532] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null; 
    return yyVal;
  }
};
states[533] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[534] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.splat_array(((Node)yyVals[0+yyTop]));
                    if (yyVal == null) yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[536] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[538] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[540] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((NumericNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[541] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    /* FIXME: We may be intern'ing more than once.*/
                    yyVal = new SymbolNode(((Token)yyVals[0+yyTop]).getPosition(), ((String) ((Token)yyVals[0+yyTop]).getValue()).intern());
    return yyVal;
  }
};
states[543] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]) instanceof EvStrNode ? new DStrNode(((Node)yyVals[0+yyTop]).getPosition()).add(((Node)yyVals[0+yyTop])) : ((Node)yyVals[0+yyTop]);
                    /*
                    NODE *node = $1;
                    if (!node) {
                        node = NEW_STR(STR_NEW0());
                    } else {
                        node = evstr2dstr(node);
                    }
                    $$ = node;
                    */
    return yyVal;
  }
};
states[544] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (yyVals[0+yyTop] instanceof StrNode) {
                        yyVal = ((StrNode)yyVals[0+yyTop]);
                    } else {
                        yyVal = new StrNode(((Token)yyVals[-1+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
                    }
    return yyVal;
  }
};
states[545] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[546] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.literal_concat(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[547] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.isDedentingHeredoc(((Token)yyVals[-2+yyTop])) ? support.dedentHeredoc(((Node)yyVals[-1+yyTop])) : ((Node)yyVals[-1+yyTop]);

                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
                    int extraLength = ((String) ((Token)yyVals[-2+yyTop]).getValue()).length() - 1;

                    /* We may need to subtract addition offset off of first */
                    /* string fragment (we optimistically take one off in*/
                    /* ParserSupport.literal_concat).  Check token length*/
                    /* and subtract as neeeded.*/
                    if ((((Node)yyVals[-1+yyTop]) instanceof DStrNode) && extraLength > 0) {
                      Node strNode = ((DStrNode)((Node)yyVals[-1+yyTop])).get(0);
                    }
    return yyVal;
  }
};
states[548] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node xstringContent = support.isDedentingHeredoc(((Token)yyVals[-2+yyTop])) ? support.dedentHeredoc(((Node)yyVals[-1+yyTop])) : ((Node)yyVals[-1+yyTop]);
                    SourcePosition position = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                    if (xstringContent == null) {
                        yyVal = new XStrNode(position, null);
                    } else if (xstringContent instanceof StrNode) {
                        yyVal = new XStrNode(position, ((StrNode) xstringContent).getValue());
                    } else if (xstringContent instanceof DStrNode) {
                        yyVal = new DXStrNode(position, (DStrNode) xstringContent);

                        ((Node)yyVal).setPosition(position);
                    } else {
                        yyVal = new DXStrNode(position).add(xstringContent);
                    }
    return yyVal;
  }
};
states[549] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newRegexpNode(support.union(((Token)yyVals[-2+yyTop]), ((RegexpNode)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), (RegexpNode) ((RegexpNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[550] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[551] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[552] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[553] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]) instanceof EvStrNode ? new DStrNode(((ListNode)yyVals[-2+yyTop]).getPosition()).add(((Node)yyVals[-1+yyTop])) : ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[555] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[556] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[557] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[558] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[559] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]) instanceof EvStrNode ? new DSymbolNode(((ListNode)yyVals[-2+yyTop]).getPosition()).add(((Node)yyVals[-1+yyTop])) : support.asSymbol(((ListNode)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[560] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[561] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[562] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[563] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[564] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[565] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[566] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[567] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(support.asSymbol(((ListNode)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[568] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new StrNode(((Token)yyVals[0+yyTop]).getPosition(), "");
    return yyVal;
  }
};
states[569] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.literal_concat(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[570] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[571] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[572] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[573] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* FIXME: mri is different here.*/
                    yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[574] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[575] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = lexer.getStrTerm();
                    lexer.setStrTerm(null);
                    lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[576] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setStrTerm(((StrTerm)yyVals[-1+yyTop]));
                    yyVal = new EvStrNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[577] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = lexer.getStrTerm();
                   lexer.getConditionState().stop();
                   lexer.getCmdArgumentState().stop();
                   lexer.setStrTerm(null);
                   lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[578] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.getConditionState().restart();
                   lexer.getCmdArgumentState().restart();
                   lexer.setStrTerm(((StrTerm)yyVals[-2+yyTop]));

                   yyVal = support.newEvStrNode(support.union(((Token)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[579] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new GlobalVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[580] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new InstVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[581] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new ClassVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[583] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     lexer.setState(LexState.EXPR_END);
                     yyVal = ((Token)yyVals[0+yyTop]);
                     ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[588] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     lexer.setState(LexState.EXPR_END);

                     /* DStrNode: :"some text #{some expression}"*/
                     /* StrNode: :"some text"*/
                     /* EvStrNode :"#{some expression}"*/
                     /* Ruby 1.9 allows empty strings as symbols*/
                     if (((Node)yyVals[-1+yyTop]) == null) {
                         yyVal = new SymbolNode(((Token)yyVals[-2+yyTop]).getPosition(), "");
                     } else if (((Node)yyVals[-1+yyTop]) instanceof DStrNode) {
                         yyVal = new DSymbolNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((DStrNode)yyVals[-1+yyTop]));
                     } else if (((Node)yyVals[-1+yyTop]) instanceof StrNode) {
                         yyVal = new SymbolNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((StrNode)yyVals[-1+yyTop]).getValue().toString().intern());
                     } else {
                         SourcePosition position = support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                         /* We substract one since tsymbeg is longer than one*/
                         /* and we cannot union it directly so we assume quote*/
                         /* is one character long and subtract for it.*/
                         position.adjustStartOffset(-1);
                         ((Node)yyVals[-1+yyTop]).setPosition(position);

                         yyVal = new DSymbolNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
                         ((DSymbolNode)yyVal).add(((Node)yyVals[-1+yyTop]));
                     }
    return yyVal;
  }
};
states[589] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((NumericNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[590] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.negateNumeric(((NumericNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[591] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[592] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((FloatNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[593] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((RationalNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[594] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[600] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("nil", Tokens.kNIL, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[601] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("self", Tokens.kSELF, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[602] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("true", Tokens.kTRUE, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[603] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("false", Tokens.kFALSE, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[604] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("__FILE__", Tokens.k__FILE__, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[605] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("__LINE__", Tokens.k__LINE__, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[606] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("__ENCODING__", Tokens.k__ENCODING__, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[607] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[608] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[609] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[610] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[611] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[612] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[613] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[614] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[615] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[616] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = null;
    return yyVal;
  }
};
states[617] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.setInDef(true);
                    support.pushLocalScope();
                    yyVal = new DefHolder(((Token)yyVals[-1+yyTop]), new MethodNameNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue()));
    return yyVal;
  }
};
states[618] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[619] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.setInSingle(support.getInSingle() + 1);
                    support.pushLocalScope();
                    lexer.setState(LexState.EXPR_ENDFN); /* force for args */
                    yyVal = new DefHolder(((Token)yyVals[-4+yyTop]), new MethodNameNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue()), ((Node)yyVals[-3+yyTop]));
    return yyVal;
  }
};
states[620] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-1+yyTop]);
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = true;
    return yyVal;
  }
};
states[622] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-1+yyTop]);
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = true;
    return yyVal;
  }
};
states[624] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[625] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(support.union(((ListNode)yyVals[-3+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[626] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(support.union(((ListNode)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[627] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(support.union(((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), null, ((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[628] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArgsTailHolder(((KeywordRestArgNode)yyVals[-1+yyTop]).getPosition(), null, ((KeywordRestArgNode)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[629] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((BlockArgNode)yyVals[0+yyTop]).getPosition(), null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[630] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsTailHolder)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[631] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(lexer.getPosition(), null, null, null);
    return yyVal;
  }
};
states[632] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[633] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-7+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-7+yyTop]), ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[634] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[635] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[636] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[637] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[638] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]), null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[639] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[640] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[641] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[642] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[643] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((RestArgNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[644] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((RestArgNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[645] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ArgsTailHolder)yyVals[0+yyTop]).getPosition(), null, null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[646] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Token)yyVals[0+yyTop]).getPosition();
                    int slot = support.getCurrentScope().exists("...");
                    if (slot == -1) slot = support.getCurrentScope().addVariable("...");
                    RestArgNode rest = new RestArgNode(pos, "...", slot);
                    BlockArgNode block = new BlockArgNode(pos, slot, "...");
                    yyVal = support.new_args(pos, null, null, rest, null, block);
    return yyVal;
  }
};
states[647] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Token)yyVals[0+yyTop]).getPosition();
                    int slot = support.getCurrentScope().exists("...");
                    if (slot == -1) slot = support.getCurrentScope().addVariable("...");
                    RestArgNode rest = new RestArgNode(pos, "...", slot);
                    BlockArgNode block = new BlockArgNode(pos, slot, "...");
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((ListNode)yyVals[-2+yyTop]), null, rest, null, block);
    return yyVal;
  }
};
states[648] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[649] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[650] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be a constant");
    return yyVal;
  }
};
states[651] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be an instance variable");
    return yyVal;
  }
};
states[652] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be a global variable");
    return yyVal;
  }
};
states[653] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be a class variable");
    return yyVal;
  }
};
states[655] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.formal_argument(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[656] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_var(((Token)yyVals[0+yyTop]));
  /*
                    $$ = new ArgAuxiliaryNode($1.getPosition(), (String) $1.getValue(), 1);
  */
    return yyVal;
  }
};
states[657] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
                    /*            {
            ID tid = internal_id();
            arg_var(tid);
            if (dyna_in_block()) {
                $2->nd_value = NEW_DVAR(tid);
            }
            else {
                $2->nd_value = NEW_LVAR(tid);
            }
            $$ = NEW_ARGS_AUX(tid, 1);
            $$->nd_next = $2;*/
    return yyVal;
  }
};
states[658] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[659] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
                    yyVal = ((ListNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[660] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[661] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((Token)yyVals[-1+yyTop])));
                    yyVal = support.keyword_arg(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.assignable(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[662] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    /* FIXME: null is a node type in main parser*/
                    yyVal = support.keyword_arg(((Token)yyVals[0+yyTop]).getPosition(), support.assignable(((Token)yyVals[0+yyTop]), null));
    return yyVal;
  }
};
states[663] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((Token)yyVals[-1+yyTop])));
                    yyVal = support.keyword_arg(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.assignable(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[664] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((Token)yyVals[0+yyTop])));
                    yyVal = support.keyword_arg(((Token)yyVals[0+yyTop]).getPosition(), support.assignable(((Token)yyVals[0+yyTop]), null));
    return yyVal;
  }
};
states[665] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[666] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[667] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[668] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[669] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[670] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[671] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[672] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("**", ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[673] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new KeywordRestArgNode(((Token)yyVals[-1+yyTop]).getPosition(), "nil", -1);
    return yyVal;
  }
};
states[674] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((Token)yyVals[-2+yyTop])));
                    yyVal = new OptArgNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.assignable(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[675] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((Token)yyVals[-2+yyTop])));
                    yyVal = new OptArgNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.assignable(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[676] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockNode(support.getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[677] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[678] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockNode(support.getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[679] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[682] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (!support.is_local_id(((Token)yyVals[0+yyTop]))) {
                        support.yyerror("rest argument must be local variable");
                    }
                    
                    yyVal = new RestArgNode(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), support.arg_var(support.shadowing_lvar(((Token)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[683] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new UnnamedRestArgNode(((Token)yyVals[0+yyTop]).getPosition(), "", support.getCurrentScope().addVariable("*"));
    return yyVal;
  }
};
states[686] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (!support.is_local_id(((Token)yyVals[0+yyTop]))) {
                        support.yyerror("block argument must be local variable");
                    }
                    
                    yyVal = new BlockArgNode(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), support.arg_var(support.shadowing_lvar(((Token)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[687] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((BlockArgNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[688] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[689] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (!(((Node)yyVals[0+yyTop]) instanceof SelfNode)) {
                        support.checkExpression(((Node)yyVals[0+yyTop]));
                    }
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[690] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[691] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-1+yyTop]) == null) {
                        support.yyerror("can't define single method for ().");
                    } else if (((Node)yyVals[-1+yyTop]) instanceof ILiteralNode) {
                        support.yyerror("can't define single method for literals.");
                    }
                    support.checkExpression(((Node)yyVals[-1+yyTop]));
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[692] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[693] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[695] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).addAll(((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[696] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos;
                    if (((Node)yyVals[-2+yyTop]) == null && ((Node)yyVals[0+yyTop]) == null) {
                        pos = ((Token)yyVals[-1+yyTop]).getPosition();
                    } else {
                        pos = ((Node)yyVals[-2+yyTop]).getPosition();
                    }

                    yyVal = support.newArrayNode(pos, ((Node)yyVals[-2+yyTop])).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[697] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Token)yyVals[-1+yyTop]).getPosition();
                    yyVal = support.newArrayNode(pos, new SymbolNode(pos, (String) ((Token)yyVals[-1+yyTop]).getValue())).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[698] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      SourcePosition pos = ((Token)yyVals[-3+yyTop]).getPosition();
                      yyVal = support.newArrayNode(pos, new SymbolNode(pos, ((StrNode) ((Node)yyVals[-2+yyTop])).getValue())).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[699] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[0+yyTop])).add(null);
    return yyVal;
  }
};
states[718] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[719] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[727] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      yyVal = null;
    return yyVal;
  }
};
states[728] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = null;
    return yyVal;
  }
};
}
					// line 2717 "Ruby30Parser.y"

    /** The parse method use an lexer stream and parse it to an AST node 
     * structure
     */
    public ParserResult parse(ParserConfiguration configuration, LexerSource source) throws IOException {
        support.reset();
        support.setConfiguration(configuration);
        support.setResult(new ParserResult());
        
        lexer.reset();
        lexer.setSource(source);

        Object debugger = null;
        if (configuration.isDebug()) {
            try {
                Class yyDebugAdapterClass = Class.forName("jay.yydebug.yyDebugAdapter");
                debugger = yyDebugAdapterClass.newInstance();
            } catch (IllegalAccessException iae) {
                // ignore, no debugger present
            } catch (InstantiationException ie) {
                // ignore, no debugger present
            } catch (ClassNotFoundException cnfe) {
                // ignore, no debugger present
            }
        }
        //yyparse(lexer, new jay.yydebug.yyAnim("JRuby", 9));
        yyparse(lexer, debugger);
        
        return support.getResult();
    }
}
					// line 10612 "-"
