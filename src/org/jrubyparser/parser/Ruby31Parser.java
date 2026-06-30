// created by jay 1.0.2 (c) 2002-2004 ats@cs.rit.edu
// skeleton Java 1.0 (c) 2002 ats@cs.rit.edu

					// line 2 "Ruby31Parser.y"
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

public class Ruby31Parser implements RubyParser {
    protected ParserSupport31 support;
    protected Lexer lexer;

    public Ruby31Parser() {
        this(new ParserSupport31());
    }

    public Ruby31Parser(ParserSupport31 support) {
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
//yyLhs 742
    -1,   181,     0,   141,   142,   142,   142,   142,   143,   184,
   143,    38,    37,    39,    39,    39,    39,    45,   185,    45,
   186,    40,    40,    40,    40,    40,    40,    40,    40,    40,
    40,    40,    40,    40,    40,    40,    40,    40,    40,    40,
    40,    40,    40,    40,    40,    31,    31,    31,    31,    31,
    33,    33,    33,    41,    41,    41,    41,    41,   188,    41,
   189,    41,    41,    46,    32,    32,    60,    60,   190,   116,
   179,    44,    44,    44,    44,    44,    44,    44,    44,    44,
    44,    44,   117,   117,   128,   128,   118,   118,   118,   118,
   118,   118,   118,   118,   118,   118,    77,    77,   103,   103,
   107,   107,    78,    78,    78,    78,    78,    78,    78,    78,
    78,    83,    83,    83,    83,    83,    83,    83,    83,    83,
     6,     6,    30,    30,    30,     7,     7,     7,     7,     7,
   121,   121,   122,   122,    62,   191,    62,     8,     8,     8,
     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,
     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,
     8,     8,     8,     8,     8,     8,     8,   139,   139,   139,
   139,   139,   139,   139,   139,   139,   139,   139,   139,   139,
   139,   139,   139,   139,   139,   139,   139,   139,   139,   139,
   139,   139,   139,   139,   139,   139,   139,   139,   139,   139,
   139,   139,   139,   139,   139,   139,   139,   139,   139,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    34,
    34,    79,    82,    82,    82,    82,    54,    54,    54,    58,
    58,   131,   131,   131,   131,   131,    52,    52,    52,    52,
    52,   193,    56,   110,   110,   109,   109,    85,    85,    85,
    85,    65,    65,    76,    76,    76,    43,    43,    43,    43,
    43,    43,    43,    43,    43,    43,    43,    43,   194,    43,
   195,    43,    43,    43,    43,    43,    43,    43,    43,    43,
    43,    43,    43,    43,    43,    43,    43,    43,    43,    43,
   197,   199,    43,   200,   201,    43,    43,    43,    43,   202,
   203,    43,   204,    43,   206,   207,    43,   208,    43,    43,
    43,    43,    43,    43,    43,    47,   196,   196,   196,   198,
   198,    50,    50,    48,    48,   130,   130,   132,   132,    90,
    90,   133,   133,   133,   133,   133,   133,   133,   133,   133,
    97,    97,    97,    97,    97,    96,    96,    69,    69,    69,
    69,    69,    69,    69,    69,    69,    69,    69,    69,    69,
    69,    69,    71,    71,    70,    70,    70,   125,   125,   124,
   124,   134,   134,   209,   127,    68,    68,   126,   126,   210,
   115,    59,    59,    59,    59,    59,    23,    23,    23,    23,
    23,    23,    23,    23,    23,   211,   114,   212,   114,    80,
    49,    49,   213,   148,   149,   149,   150,   150,   150,   151,
   151,   151,   151,   151,   151,   152,   153,   153,   154,   154,
   176,   176,   177,   177,   155,   155,   155,   155,   155,   155,
   155,   155,   155,   155,   155,   155,   155,   155,   155,   163,
   163,   163,   163,   163,   163,   163,   163,   167,   167,   164,
   164,   168,   168,   165,   166,   166,   169,   170,   170,   170,
   170,   170,   170,   171,   171,   172,   172,   173,   174,   174,
   175,   156,   156,   156,   156,   156,   156,   156,   156,   156,
   156,   156,   157,   157,   157,   157,   157,   157,   157,   157,
   157,   157,   158,   159,   159,   161,   162,   162,   162,   160,
   160,   160,   119,   119,    81,    81,    81,    51,    51,    53,
    53,    28,    28,    28,    15,    16,    16,    16,    17,    18,
    19,    25,    25,    87,    87,    27,    27,    93,    93,    91,
    91,    26,    26,    94,    94,    86,    86,    92,    92,    20,
    20,    21,    21,    24,    24,    22,   214,    22,   215,    22,
    63,    63,    63,    63,     2,     1,     1,     1,     1,    29,
    35,    35,    36,    36,    36,    36,   135,   135,   135,   135,
   135,   136,   136,   136,   136,   136,   136,   136,    57,    57,
   120,   120,    64,    64,    55,   216,    55,    55,    74,   217,
    75,    72,    67,    67,    73,    73,    98,    98,    98,    98,
    98,    95,    95,    66,    66,    66,    66,    66,    66,    66,
    66,    66,    66,    66,    66,    66,    66,    66,    66,    66,
   147,   140,   140,   140,   140,     9,     9,   123,   123,    88,
    88,   178,    99,    99,   100,   100,   101,   101,   102,   102,
   144,   144,   145,   145,   146,    61,   129,   108,   108,    89,
    89,    11,    11,    13,    13,    12,    12,   113,   113,   112,
   112,    14,   218,    14,   106,   106,   104,   104,   105,   105,
   105,   105,   105,     3,     3,     3,     4,     4,     4,     4,
     5,     5,     5,    10,    10,   180,   180,   182,   182,   187,
   187,   137,   138,   192,   192,   192,   205,   205,   183,   183,
    84,   111,
    }, yyLen = {
//yyLen 742
     2,     0,     2,     2,     1,     1,     3,     2,     1,     0,
     5,     4,     2,     1,     1,     3,     2,     1,     0,     5,
     0,     4,     3,     3,     3,     2,     3,     3,     3,     3,
     3,     4,     1,     3,     3,     6,     5,     5,     5,     5,
     3,     3,     5,     3,     1,     3,     4,     6,     4,     6,
     1,     3,     1,     1,     3,     3,     3,     2,     0,     4,
     0,     4,     1,     1,     1,     1,     1,     4,     0,     5,
     1,     2,     3,     4,     5,     4,     5,     2,     2,     2,
     2,     2,     1,     3,     1,     3,     1,     2,     3,     5,
     2,     4,     2,     4,     1,     3,     1,     3,     2,     3,
     1,     3,     1,     1,     4,     3,     3,     3,     3,     2,
     1,     1,     1,     4,     3,     3,     3,     3,     2,     1,
     1,     1,     2,     1,     3,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     0,     4,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     3,
     3,     6,     5,     5,     5,     5,     4,     3,     3,     3,
     2,     2,     2,     2,     3,     3,     3,     3,     3,     3,
     4,     2,     2,     3,     3,     3,     3,     3,     3,     3,
     3,     3,     3,     3,     3,     3,     2,     2,     3,     3,
     3,     3,     3,     6,     4,     6,     4,     6,     1,     1,
     3,     1,     1,     2,     4,     2,     3,     3,     5,     1,
     1,     1,     1,     2,     4,     2,     1,     2,     2,     4,
     1,     0,     2,     2,     1,     2,     1,     1,     2,     3,
     4,     1,     1,     3,     4,     2,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     3,     0,     3,
     0,     4,     3,     3,     2,     3,     3,     1,     4,     3,
     1,     5,     4,     3,     2,     1,     2,     2,     6,     6,
     0,     0,     7,     0,     0,     7,     5,     4,     5,     0,
     0,     9,     0,     6,     0,     0,     8,     0,     5,     4,
     4,     1,     1,     1,     1,     1,     1,     1,     2,     1,
     1,     1,     5,     1,     2,     1,     1,     1,     3,     1,
     3,     1,     4,     6,     3,     5,     2,     4,     1,     3,
     4,     2,     2,     2,     1,     2,     0,     6,     8,     4,
     6,     4,     2,     6,     2,     4,     6,     2,     4,     2,
     4,     1,     1,     1,     3,     1,     4,     1,     4,     1,
     3,     1,     1,     0,     3,     4,     1,     3,     3,     0,
     5,     2,     2,     4,     5,     5,     2,     4,     4,     3,
     3,     3,     2,     1,     4,     0,     5,     0,     5,     5,
     1,     1,     0,     6,     1,     1,     1,     3,     3,     1,
     2,     3,     1,     1,     1,     1,     3,     1,     3,     1,
     1,     1,     1,     1,     1,     4,     4,     4,     3,     4,
     4,     4,     3,     3,     3,     2,     3,     2,     3,     1,
     1,     2,     3,     5,     2,     4,     1,     2,     3,     1,
     3,     1,     3,     5,     2,     1,     1,     3,     1,     2,
     1,     3,     1,     1,     3,     2,     1,     1,     2,     1,
     2,     1,     3,     3,     2,     2,     1,     1,     1,     1,
     2,     2,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     2,     2,     4,     1,     1,     1,     2,
     3,     1,     6,     0,     1,     1,     1,     2,     1,     2,
     1,     1,     1,     1,     1,     1,     1,     2,     3,     3,
     3,     3,     3,     0,     3,     1,     2,     3,     3,     0,
     3,     3,     3,     3,     3,     0,     3,     0,     3,     0,
     2,     0,     2,     0,     2,     1,     0,     3,     0,     4,
     1,     1,     1,     1,     2,     1,     1,     1,     1,     3,
     1,     2,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     0,     4,     2,     2,     0,
     5,     3,     1,     2,     1,     0,     4,     2,     2,     2,
     1,     2,     0,     6,     8,     4,     6,     4,     6,     2,
     4,     6,     2,     4,     2,     4,     1,     1,     3,     0,
     1,     1,     1,     1,     1,     1,     1,     1,     3,     1,
     3,     1,     2,     1,     2,     1,     1,     3,     1,     3,
     1,     1,     2,     1,     2,     3,     3,     1,     3,     1,
     3,     1,     1,     2,     1,     1,     1,     2,     1,     2,
     0,     1,     0,     4,     1,     2,     1,     3,     3,     2,
     1,     4,     2,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     0,     1,     0,
     1,     2,     2,     0,     1,     1,     1,     1,     1,     2,
     0,     0,
    }, yyDefRed = {
//yyDefRed 1239
     1,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   330,   333,     0,     0,     0,   353,   354,     0,
     0,     0,   612,   611,   613,   614,     0,     0,     0,     9,
     0,   616,   615,   617,     0,     0,   608,   607,     0,   610,
   555,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   579,   581,   583,     0,     0,   413,   622,
   623,   602,   603,   605,   604,     0,     0,     0,     0,   552,
    70,   297,     0,   556,   298,   299,     0,   300,   301,   296,
   553,    32,    53,   551,   600,     0,     0,     0,     0,     0,
     0,   304,     0,    65,     0,     0,     0,     0,    96,     0,
     4,   302,   303,     0,     0,    82,     0,     0,     0,     2,
     0,     5,     0,     7,   351,   352,   317,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   123,     0,     0,
   355,     0,   305,     0,     0,   618,   619,     0,   347,   177,
   188,   178,   201,   174,   194,   184,   183,   199,   182,   181,
   176,   202,   186,   175,   189,   193,   195,   187,   180,   196,
   203,   198,     0,     0,     0,     0,   173,   192,   191,   204,
   205,   206,   207,   208,   172,   179,   170,   171,     0,     0,
     0,     0,   127,     0,   162,   163,   159,   140,   141,   142,
   149,   146,   148,   143,   144,   164,   165,   150,   151,   702,
   156,   155,   139,   161,   158,   157,   153,   154,   147,   145,
   137,   160,   138,   166,   152,   628,   128,     0,   701,   129,
   197,   190,   200,   185,   167,   168,   169,   125,   126,   131,
   130,   133,     0,   132,   134,     0,     0,     0,     0,     0,
     0,    14,    13,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   736,   737,     0,     0,     0,
   738,     0,     0,     0,     0,     0,   365,   366,     0,     0,
     0,     0,     0,     0,     0,   579,     0,     0,   276,    80,
     0,     0,     0,   706,   280,    81,    79,     0,    78,     0,
     0,   432,    77,     0,   730,     0,     0,    20,     0,     0,
     0,   231,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   262,     0,     0,     0,   704,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   247,    57,   246,
   597,   596,   598,   594,   595,     0,     0,     0,     0,     0,
     0,     0,     0,   327,     0,     0,     0,     0,     0,     0,
     0,   557,   437,   419,   435,   326,   422,     0,     0,     0,
     0,     0,     0,     0,    58,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
    60,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   421,   725,     0,   726,     0,     0,   723,   724,
     0,     0,   666,   663,   662,   661,   664,   671,   680,     0,
     0,   692,   691,   696,   695,   681,   660,     0,     0,     0,
     0,   689,     0,     0,     0,     0,     0,     0,   656,   678,
     0,   640,   669,   665,     0,     0,     0,   657,     0,     0,
     0,    98,     0,     0,     0,     0,     0,     0,     3,     0,
   426,     0,   324,     0,   601,     0,   120,     0,   122,     0,
   625,   342,   624,     0,     0,     0,   632,     0,     0,   629,
   135,     0,     0,     0,     0,   307,    12,     0,     0,   357,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,   739,     0,     0,     0,     0,     0,
     0,   339,   709,   288,   283,     0,   712,     0,     0,   277,
   286,     0,   278,     0,   319,     0,   282,     0,   272,   271,
     0,     0,     0,     0,     0,   323,    56,    22,    24,    23,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   312,     0,     0,   309,   315,     0,   734,   263,     0,   265,
   705,   316,     0,   100,     0,     0,     0,     0,     0,   588,
   586,   599,   585,   582,   558,   580,   559,   560,   584,   561,
   562,   565,     0,   571,   572,     0,     0,   416,     0,   567,
   568,     0,   573,   574,     0,     0,     0,     0,    26,    27,
    28,    29,    30,    54,    55,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   429,     0,   431,
     0,     0,     0,   718,     0,     0,   719,   430,   716,   717,
     0,    52,     0,    40,   217,     0,     0,     0,     0,   367,
     0,   369,     0,     0,     0,   693,   697,     0,   654,   633,
     0,     0,     0,   649,     0,   652,     0,   637,   684,   682,
     0,   638,   639,   672,     0,     0,     0,    45,   209,     0,
    41,   287,     0,     0,     0,     0,    99,    33,     0,   291,
     0,    34,   210,     6,    68,    72,     0,   627,     0,     0,
     0,     0,     0,   124,     0,     0,     0,     0,     0,     0,
     0,   545,     0,     0,   546,     0,     0,   363,    15,     0,
   358,     0,     0,     0,     0,     0,     0,     0,     0,   442,
     0,     0,     0,   337,   360,   331,   359,   334,     0,     0,
     0,     0,     0,     0,     0,   708,     0,     0,     0,   285,
   707,   318,   731,     0,     0,   266,   267,   322,    21,     0,
     0,    31,     0,   216,     0,   311,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   564,   566,   576,     0,
     0,     0,   414,   570,   578,   405,     0,   403,     0,   402,
     0,     0,   532,   541,   507,     0,     0,     0,     0,     0,
     0,     0,     0,   523,   524,   525,   526,   527,   522,   528,
   529,   531,   530,     0,    59,     0,     0,   455,     0,   459,
   464,     0,   516,   517,     0,   518,   453,   452,     0,   454,
     0,   503,     0,   500,   502,    61,     0,     0,     0,   428,
     0,     0,     0,     0,     0,     0,   270,     0,   427,   269,
     0,     0,     0,     0,     0,     0,     0,     0,     0,   668,
   631,   685,   667,     0,   641,   349,     0,     0,     0,     0,
   670,   658,     0,   690,     0,   679,   699,     0,     0,   350,
     0,     0,     0,     0,    97,     0,     0,     0,     0,   345,
     0,     0,   434,   348,   703,   630,   136,     0,     0,     0,
   548,   364,     0,    11,   550,     0,   361,     0,     0,     0,
     0,     0,     0,     0,     0,   336,   338,     0,     0,     0,
     0,     0,     0,   711,   290,   279,     0,     0,   321,    10,
   264,   101,     0,     0,   590,   591,   592,   587,   593,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   401,   676,
     0,     0,   384,     0,   687,     0,     0,     0,     0,     0,
   121,   539,     0,   475,     0,     0,   486,     0,     0,     0,
   477,     0,   494,   533,   537,   536,   538,     0,   534,   520,
   521,   510,   508,     0,     0,     0,     0,     0,     0,     0,
     0,   460,   461,   462,   463,     0,     0,     0,     0,     0,
     0,    39,   214,    38,   215,    76,     0,   732,    36,   212,
    37,   213,    74,   425,   424,    51,     0,   368,     0,     0,
     0,   370,     0,   655,     0,     0,     0,   647,     0,   645,
     0,   650,   653,   636,     0,     0,     0,     0,     0,    42,
     0,     0,   626,   343,    19,     0,     0,     0,   547,     0,
     0,     0,   549,     0,   328,     0,   329,     0,   289,     0,
     0,     0,   340,     0,   268,   589,   415,     0,   418,   417,
     0,     0,     0,     0,   399,     0,     0,   394,     0,   381,
     0,   397,   404,   382,   383,   438,   420,   436,   478,   473,
   474,     0,     0,     0,   487,   476,     0,   447,   448,   456,
   451,     0,   458,   512,   513,   540,   468,     0,     0,     0,
   472,     0,     0,     0,     0,   491,   504,   497,   501,     0,
    35,   211,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,   440,   439,   441,   332,   335,     0,
   411,     0,   409,   412,     0,   314,     0,     0,   385,   406,
     0,     0,   677,     0,     0,     0,   688,     0,     0,   488,
   535,     0,   465,   466,   467,   469,   470,   471,     0,     0,
     0,   648,     0,   643,   646,   651,    69,   346,     0,     0,
     0,     0,     0,   542,     0,     0,     0,     0,   408,     0,
     0,     0,   400,     0,   391,     0,   389,   380,     0,   395,
   398,     0,     0,     0,   493,   492,     0,     0,     0,   362,
   444,   445,   443,   341,   410,     0,     0,     0,     0,     0,
     0,   644,   393,     0,   387,   390,   396,     0,   388,
    }, yyDgoto = {
//yyDgoto 219
     1,   333,    69,    70,   702,   627,   127,   230,   628,   862,
   400,   418,   419,   420,   217,    71,    72,    73,    74,    75,
   336,   335,   563,   128,   338,    77,    78,   572,    79,    80,
   129,    81,    82,   677,   678,    83,    84,   237,   238,   239,
   240,    86,    87,    88,    89,   241,   245,   302,   906,  1145,
   907,   899,   518,   903,   629,   461,   288,    91,   848,    92,
    93,   421,   232,   937,   247,   688,   422,   423,   578,   946,
   787,   788,   424,   425,   248,   249,   680,    97,    98,   280,
   493,   713,   312,   250,   242,   630,   342,   340,   426,   427,
   650,   346,   348,   101,   102,   658,  1074,  1158,   428,   429,
   949,   950,   430,   103,   521,   283,   319,   554,   951,   509,
   284,   510,   667,   431,   355,   356,   695,   104,   105,   474,
   251,   233,   234,   432,  1151,   939,   782,   811,   309,   954,
   268,   522,   651,   652,  1152,   252,   253,   514,   842,   219,
   433,   109,   110,   111,   434,   435,   436,   437,   731,  1222,
   814,   815,  1092,   817,   818,   819,   820,   821,   822,   823,
   824,   825,   978,   965,   966,   827,   828,   968,  1114,   969,
   829,   830,   831,   832,   833,   834,   995,   996,   438,   137,
   465,     2,   258,   259,   299,   472,   530,   515,   595,   611,
   888,   709,   547,   289,   311,   542,   480,   261,   735,   919,
   262,   920,   743,  1149,   699,   481,   696,  1041,   467,   344,
   586,   587,   585,   914,   775,   774,   698,   708,   468,
    }, yySindex = {
//yySindex 1239
     0,     0, 19071, 19487, 23060, 23456, 22523, 22845, 19210, 21971,
 21971, 12540,     0,     0, 23192, 19625, 19625,     0,     0, 19625,
  -116,  -113,     0,     0,     0,     0,    95, 22737,   255,     0,
   -36,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0, 22109, 22109,  1096,    74, 19349, 21971, 20453, 20867, 18529,
 22109, 22247, 22630,     0,     0,     0,   330,   356,     0,     0,
     0,     0,     0,     0,     0,   375,   386, 22109, 22109,     0,
     0,     0,    57,     0,     0,     0,   -25,     0,     0,     0,
     0,     0,     0,     0,     0,  1916,     4,  4416,     0,   197,
   579,     0,   377,     0,   240,  2952,  2952,   419,     0,   454,
     0,     0,     0, 23324,   552,     0,   309,     0,     0,     0,
   195,     0,   -50,     0,     0,     0,     0,  -116,  -113,   329,
   255,     0,     0,  1096, 21971,   425, 19210,     0,  -108,    83,
     0,   596,     0,  2952,  2952,     0,     0,   -50,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   377,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   646,     0,     0, 19763, 19210,   471,   495,   195,
  1916,     0,     0,   467,     4,   137,   702,   433,  2952,  2952,
   744,   478,     0,     0,   137,     0,     0,   195,   537,   809,
     0, 21971, 21971,   569,   761,     0,     0,     0,   615,     0,
     0, 22109, 22109, 22109, 22109,     0, 22109, 16533,     0,     0,
   559,   855,   860,     0,     0,     0,     0, 12398,     0, 19625,
 19901,     0,     0, 12678,     0, 21971,   -96,     0, 21005,   545,
 19210,     0,  1026,   594,   606,   610, 19349,   617,     0,   255,
     4,   255,   612,     0,   265,   268,   559,     0,   268,   601,
   650, 23588,  1040,     0,   931,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   657,  1154,  1373,  1137,   625,
  1390,   628,   378,     0,  4413,   630,  1534,   640,   424,  4511,
  4511,     0,     0,     0,     0,     0,     0, 21971, 21971, 21971,
 21971, 19763, 21971, 21971,     0, 22109, 22109, 22109, 22109, 22109,
 22109, 22109, 22109, 22109, 22109, 22109, 22109, 22109, 22109, 22109,
     0, 22109, 22109, 22109, 22109, 22109, 22109, 22109, 22109, 22109,
 22109, 22109,     0,     0,  5393,     0, 19625,  5901,     0,     0,
 24729, 22247,     0,     0,     0,     0,     0,     0,     0,  1876,
  2899,     0,     0,     0,     0,     0,     0,   917,   676,   683,
   972,     0,   195, 19210,     0,   943,   974,   981,     0,     0,
   997,     0,     0,     0,    47,  1008,  1008,     0, 22109, 19210,
   993,     0, 21143, 19349, 18662,  1013, 21143, 22247,     0, 18794,
     0,   721,     0,   740,     0,     4,     0,     0,     0,   195,
     0,     0,     0,  6409, 19625,  7416,     0, 19210, 21971,     0,
     0,  1916,   726, 21281,   823,     0,     0, 18932,   610,     0,
 19210,   830,  7935, 19625, 24257, 22109,  1037,  1042, 22109, 22109,
 19210,   505, 21419,   845,     0,    85,    85,     0, 24316, 19625,
 24375,     0,     0,     0,     0,  1170,     0, 22109, 20039,     0,
     0, 20591,     0,   255,     0,   769,     0, 22109,     0,     0,
  1067,  1079,   255,   255,   254,     0,     0,     0,     0,     0,
 22845, 21971, 16533, 19071,   765,  7935, 24257, 22109, 22109,   255,
     0,     0,   255,     0,     0, 20729,     0,     0, 20867,     0,
     0,     0,     0,     0,  1082, 24434, 19625, 24493, 23588,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   217,     0,     0,  1110,  2899,     0,  -184,     0,
     0,   451,     0,     0,  1112,  -106,  -106,  -106,     0,     0,
     0,     0,     0,     0,     0, 23946,   594,  4034,  4034,  4034,
  4034,  2788,  2788,  5539,  5031,  4034,  4034,  4511,  4511,  1466,
  1466, 23946,   594,  1953,   594,   594,   655,   655,  2788,  2788,
  1903,  1903, 13216,   811,     0,   817,  -113,     0,     0,     0,
  1102,   255,   818,     0,   819,  -113,     0,     0,     0,     0,
  -113,     0,   863,     0,     0, 13565,  1113,  1876,  1711,     0,
  1118,     0,   255,   255, 22109,     0,     0,  2384,     0,     0,
   915, 22247,  2899,     0,  2599,     0,   846,     0,     0,     0,
   281,     0,     0,     0,   916, 22247, 22109,     0,     0, 13565,
     0,     0,  1141,   255, 23588,  1149,     0,     0,   884,     0,
     0,     0,     0,     0,     0,     0,   195,     0, 21971, 19210,
     0,     0,  -113,     0,   255,  -113,   933,   254, 22953, 22845,
 19071,     0,     0,   867,     0, 19210,   935,     0,     0,   723,
     0,   877,   878,   255,   900,   904, 22109, 22109,   823,     0,
   957,   980,   164,     0,     0,     0,     0,     0,     0,     0,
   255,     0,     0, 21971, 22109,     0, 22109,   559,   860,     0,
     0,     0,     0, 20177, 20591,     0,     0,     0,     0,   254,
   887,     0,   594,     0,     0,     0,   268, 23588,     0,     0,
   255,     0,     0,  1082, 19210,  1237,     0,     0,     0,   255,
 19210, 19210,     0,     0,     0,     0,  2137,     0, 19210,     0,
 19210, 19210,     0,     0,     0,   541, 24172,  3787,   739,   944,
  1009,  7012,  7012,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,    58,     0,   542,    38,     0,   896,     0,
     0,   731,     0,     0,   609,     0,     0,     0,  1214,     0,
  1220,     0, 24172,     0,     0,     0,  1209, 22247, 22247,     0,
   721, 20039,   950,   926, 22247, 22247,     0,   721,     0,     0,
   197,  -108, 19763, 22109, 22247,   255,  1793,  1235,  2026,     0,
     0,     0,     0,  1245,     0,     0, 13914,  1002,  1246,  1250,
     0,     0,  1259,     0,  1245,     0,     0,   999,  1008,     0,
 18663,  1014,     0, 21557,     0,  1082, 23588, 19763,  -106,     0,
   195,  1046,     0,     0,     0,     0,     0,   952, 23720,   137,
     0,     0, 19210,     0,     0, 21971,     0,  1060, 22109, 22109,
   989, 22109, 22109,  1063, 23946,     0,     0, 21695, 19210, 19210,
 19210,     0,    85,     0,     0,     0,  1286,   255,     0,     0,
     0,     0,     0,   971,     0,     0,     0,     0,     0,   998,
  1281,  1083,   987,  1290, 23852,  1314,   255,  1319,     0,     0,
  1323,  1325,     0,  1018,     0,  1008,  1008,  1115,  1126,  1020,
     0,     0,    92,     0,  1056,   255,     0,   255, 24040,  1352,
     0,  1036,     0,     0,     0,     0,     0, 21971,     0,     0,
     0,     0,     0, 21971, 21971,  1093, 24134, 24172,  7012,  7012,
   541,     0,     0,     0,     0,  2496,  2496, 24172,   470,  1056,
 22109,     0,     0,     0,     0,     0, 22247,     0,     0,     0,
     0,     0,     0,     0,     0,     0, 16533,     0,  1356,  1793,
  1722,     0,  2384,     0, 22109, 22109,  2384,     0,  2599,     0,
  2384,     0,     0,     0, 22109, 22109, 22109,     0,  1082,     0,
 19210, 19210,     0,     0,     0,  1107,  1092,     0,     0,     0,
     0, 19210,     0,   137,     0, 22109,     0,   137,     0,   598,
  1158,  1165,     0, 20591,     0,     0,     0,  2016,     0,     0,
 23852,  1120,  1103,  2817,     0,  1075,  3851,     0,  1127,     0,
  3851,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,    31,  1056,  1397,     0,     0,  1104,     0,     0,     0,
     0,  1398,     0,     0,     0,     0,     0,   255,   255,   255,
     0,   255,   255,   255,  1406,     0,     0,     0,     0, 16533,
     0,     0,  1793,  1356,  1793,  1408, 16533, 16533,  1245,  1409,
  1245,  1245, 16533, 16533,     0,  1098,  1190,     0, 24552, 19625,
 24611,   471, 19210, 19210,     0,     0,     0,     0,     0, 19210,
     0,   273,     0,     0,  1103,     0, 24670,  1421,     0,     0,
  1423,  1434,     0,  1008,  1449,  1421,     0,  1450, 24172,     0,
     0, 24172,     0,     0,     0,     0,     0,     0, 24134,  1356,
  1793,     0,  2384,     0,     0,     0,     0,     0,     0,     0,
   255,     0,     0,     0,   723,   103,  1239,  2016,     0,     0,
     0,  2817,     0,  2817,     0,  3851,     0,     0,  2817,     0,
     0, 24172,  1462,  1462,     0,     0,  1356,  1245,     0,     0,
     0,     0,     0,     0,     0,  1421,  1467,  1421,  1421,  1462,
 24172,     0,     0,  2817,     0,     0,     0,  1421,     0,
    }, yyRindex = {
//yyRindex 1239
     0,     0,   178,     0,     0,     0,     0,     0,  1778,     0,
     0,  1248,     0,     0,     0,  9870,  9978,     0,     0, 10110,
  6827,  6319,     0,     0,     0,     0, 22385,     0, 21833,     0,
     0,     0,     0,     0,  4148,  5303,     0,     0,  4287,     0,
     0,     0,     0,     0,     0,    52,    93,  1168,  1161,   316,
     0,     0,  1560,     0,     0,     0,  1624,   426,     0,     0,
     0,     0,     0,     0,     0,  1641,   510,     0,     0,     0,
     0,     0,  9280,     0,     0,     0, 10327,     0,     0,     0,
     0,     0,     0,     0,     0,    81,  5037,  2704,  9412, 18102,
     0,     0, 18158,     0, 10676,   764,   764,     0,     0,     0,
     0,     0,     0,   678,     0,     0,     0, 10459, 10568,     0,
    78,     0, 20315,     0,     0,     0,     0,  9521,  8582,     0,
  1189, 11113, 11256,     0,     0,     0,    52,     0,  9629,     0,
     0,     0,     0,   208,   208,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,  2070,  4979,  5487,  5995,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,  6503,  6928,
  7510,  8262,     0, 12772,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,  5545,     0,     0,     0,   259,   364,     0,  2098,
  1073,     0,     0,     0,  2557,     0,     0,  9063,   764,   764,
     0,     0,  8714,  8823,     0,     0,     0,   570,     0,   582,
     0,     0,     0,     0,     0,   368,     0,     0,     0,   474,
  1264,  2891,     0,  4529,     0,     0,     0, 16718,     0,     0,
 17031, 12862, 12862,     0,     0,     0,     0,  1193,     0,     0,
   222,     0,     0,  1193,     0,     0,     0,     0,     0,     0,
    94,     0,     0, 13170,  9761, 10791,    52,     0,   221,  1193,
   226,  1193,     0,     0,  1191,  1191,     0,     0,  1174,     0,
     0,     0,     0,  1780,   816,  1783,  1898,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,  -101,     0,     0,     0,     0, 15908,
 16004,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0, 12968, 13069,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,    69,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   234,     0,     0,     0,     0,     0,     0,   140,   895,   233,
   139,     0,     0,  1778, 11978,     0,   139,   139,     0,     0,
   310,     0,     0,     0,   924,   310,   310,     0,   632,  1778,
     0,     0,     0,    52,   940,   954,     0,     0,     0,    84,
     0,  8422,     0,     0,     0,   215,     0, 11693,     0,     0,
     0,     0,     0,     0,    69,     0,     0,  1778,     0,     0,
     0,  1594,     0,   747,   768,     0,     0,  2336,  9172,     0,
   126, 11833,     0,    69,     0,     0,     0,     0,     0,     0,
   591,     0,     0,     0,     0,     0,     0,  1315,     0,    69,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,  1193,     0,     0,     0,   251,     0,     0,
   271,   108,  1193,  1193,  1193,     0,     0,     0,     0,     0,
     0,     0, 16293,    94,     0,     0,     0,     0,     0,  1193,
     0,   763,  1193,     0,     0,  1194,     0,     0,   228,     0,
     0,     0,  1919,     0,   959,     0,    69,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   123,     0,     0,     0,
     0,     0,     0,     0,     0, 12118, 12118, 12259,     0,     0,
     0,     0,     0,     0,     0,  2277, 13317, 15137, 15233, 15330,
 15426, 14699, 14800, 15522, 15811, 15619, 15715, 16100, 16197, 14116,
 14217,  2277, 13418, 14357, 13519, 13666, 13868, 14015, 14896, 15041,
 14458, 14559,  1493,  7188,  5673,  7834, 20315,     0,  5811,     0,
    79,  1212,  7326,     0,  7696,  6689,     0,     0,     0,     0,
  8076,     0,  3054,     0,     0, 16776,     0,     0,   276,     0,
   298,     0,  1193,  1193,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0, 18372,
     0,     0,     0,  1193,     0,   960,     0,     0,  3637,     0,
  3481,     0,     0,     0,     0,     0,     0,     0,     0,  1778,
 11403, 11546,     0,     0,  1212,  8931,     0,  1193,     0,     0,
    94,     0,   306,   181,     0,   128,  1295,     0,     0,  1295,
     0,  4657,  6181,  1212,  4795,  5165,     0,     0,  1295,     0,
     0,     0,     0,     0,     0,     0,     0,     0,  1938,   842,
  1212,  1948,  2076,     0,     0,     0,     0, 17068, 12862,     0,
     0,     0,     0,   300,   113,     0,     0,     0,     0,  1193,
     0,     0, 13767,     0,   207,     0,  1191,     0,  1157,  1222,
  1212,  1230,  1241,  1025,    94,     0,     0,     0,     0,    48,
  1778,    94,     0,     0,     0,     0,    29,     0,  1778,     0,
  1778,    94,     0,     0,     0,     0,  3710,  4885,     0, 17245,
     0,  2750,  2750,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0, 17611,     0, 12914, 18194,     0, 17190,     0,
     0,  3180,     0,     0, 17153,     0,     0,     0, 17701,     0,
 17737,     0,  3689,     0,     0,     0,     0,     0,     0,     0,
 17286,   156, 10219,     0,     0,     0,     0, 17341,     0,     0,
 18255, 17378,     0,     0,     0,  1193,     0,   314,     0,     0,
     0,     0,     0,   139,     0,     0, 16861,  7069,   139,   139,
     0,     0,   139,     0,   139,     0,     0,   924,   310,     0,
 16946,  7568,  3522,     0,     0,  1043,     0,     0, 12259,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   163,     0,     0,     0,     0,     0,     0,     0,
  8474,     0,     0,     0,  2277,     0,     0,     0,  1200,   163,
   163,  1461,     0,     0,     0,     0,   108,  1193,     0,     0,
     0,     0,   885,     0,     0,     0,     0,     0,     0,     0,
   -69,     0,     0,   110,   136,    75,    29,    75,     0,     0,
   112,    75,     0,     0,     0,   112,   112,     0,     0,     0,
     0,     0,  1193,     0, 17431,  1212,     0,  1212, 10900,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0, 10964,  3710,  3966,  8204,
     0,     0,     0,     0,     0,  1814,  1954,  3710, 17789, 17488,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0, 16389,     0,   336,     0,
   338,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0, 18423,  1078,     0,
    94,  1778,     0,     0,     0,     0,     0,   138,     0,   738,
   871,  1778,     0,     0,     0,     0,     0,     0,     0,  1295,
     0,     0,     0,   160,     0,     0,     0,     0,     0,     0,
     0,     0,   167,     0,     0,     0,   169,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0, 17825, 17574, 17881,     0,     0,     0,     0,     0,     0,
     0, 18291,     0,     0,     0,     0,     0,  1193,  1193,  1193,
     0,  1212,  1212,  1212, 17917,     0,     0,     0,     0, 16475,
     0,     0,     0,   342,     0,   360, 16536,  6053,   139,   139,
   139,   139, 16632,  6561, 18436,     0,     0,   988,     0,    69,
     0,   364,   126,   619,     0,     0,     0,     0,     0,   163,
     0,   -53,     0,     0,   176,     0,     0,    75,     0,     0,
    75,    75,     0,   112,    75,    75,     0, 17978,  3710,     0,
     0,  3710,     0,     0,     0,     0,     0,     0,  4903,   361,
     0,     0,     0,     0,     0,     0,     0,     0,  1297,  1441,
  1212,  1411,  1520,     0,  1295,  1295,     0,     0,     0,  1556,
  1424,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,  3710, 18014, 18331,     0,     0,   363,   139,  1674,     0,
     0,     0,     0,     0,     0,    75,    75,    75,    75, 18066,
  3710,     0,     0,     0,     0,     0,     0,    75,     0,
    }, yyGindex = {
//yyGindex 219
     0,     0,    27,     0,  -355,     0,  -114,    17,    11,    80,
  1344,     0,     0,  -570,     0,   415,     0,  1495,   460,   978,
  1293,  1521,  1738,  1691,     0,  1101,  2284,  1228,  2583,    28,
  1573,  -284,   -27,  -295,   225,     0,   124,   -29,  -352,     0,
    56,    40,  3341,    23,    10,  1108,    -3,   829,  -441,     0,
   390,     0,  1376,     0,    19,     0,   -16,  1574,   947,     0,
     0,  -596,     0,     0,  2453,     0,  -201,    72,     0,     0,
     0,  -324,   942,   119,  1874,  2189,  -207,   -73,    25,   951,
  -455,     0,     0,   635,    13,   567,     0,     0,   258,   928,
  -147,     0,     0,  2683,  2939,  -266,   779,   802,  -279,   929,
   523,     0,     0,     0,    51,  -458,     0,  -357,   530,  -251,
  -431,     0,  -371,   -30,   -93,   -79,  -174,  1593,     7,   475,
  2722,     0,    21,  -621,     0,  -583,     0,  1550,  -167,  -891,
     0,  -364,  -837,   982,   421,  2516,    -2,  -181,  -344,     0,
  -867,  -417,     0,    43,   247,  -541,  -540,  -233,   435,     0,
  -528,     0,  -280,     0,     0,   634,     0,  -608,   649,     0,
     0,     0,     0,   -76,  -454,  -689,  -827,     0,  -961,   175,
  -176,     0,   637,     0,   638,   641,     0,     0,     0,  1362,
   -81,     0,    -1,    45,     0,     0,     0,    -6,     0,     0,
     0,     0,  -242,     0,     0,     0,  -241,     0,  -440,     0,
     0,     0,     0,     0,     0,     3,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,
    };
    protected static final short[] yyTable = Ruby31YyTables.yyTable();
    protected static final short[] yyCheck = Ruby31YyTables.yyCheck();

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
    "command_asgn : defn_head f_opt_paren_args '=' command",
    "command_asgn : defn_head f_opt_paren_args '=' command kRESCUE_MOD arg",
    "command_asgn : defs_head f_opt_paren_args '=' command",
    "command_asgn : defs_head f_opt_paren_args '=' command kRESCUE_MOD arg",
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
    "block_arg : tAMPER",
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
    "p_value : p_expr_ref",
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
    "p_var_ref : tCARET nonlocal_var",
    "p_expr_ref : tCARET tLPAREN expr_value tRPAREN",
    "nonlocal_var : tIVAR",
    "nonlocal_var : tGVAR",
    "nonlocal_var : tCVAR",
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
    "f_block_arg : blkarg_mark",
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
    "assoc : tLABEL",
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

static ParserState[] states = new ParserState[742];
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
                    Node body = ((Node)yyVals[0+yyTop]);

                    yyVal = new DefnNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
    return yyVal;
  }
};
states[47] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = new RescueNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[0+yyTop]), null), null);

                    yyVal = new DefnNode(support.union(((DefHolder)yyVals[-5+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-5+yyTop]).nameNode, ((ArgsNode)yyVals[-4+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
    return yyVal;
  }
};
states[48] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]);

                    yyVal = new DefsNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).receiver, ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[49] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = new RescueNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[0+yyTop]), null), null);

                    yyVal = new DefsNode(support.union(((DefHolder)yyVals[-5+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-5+yyTop]).receiver, ((DefHolder)yyVals[-5+yyTop]).nameNode, ((ArgsNode)yyVals[-4+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[50] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[51] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]);
                    yyVal = new RescueNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, body, null), null);
    return yyVal;
  }
};
states[54] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newAndNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[55] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOrNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[56] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Token)yyVals[-2+yyTop]), support.getConditionNode(((Node)yyVals[0+yyTop])));
                    ((CallNode)yyVal).setName("!");
    return yyVal;
  }
};
states[57] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Token)yyVals[-1+yyTop]), support.getConditionNode(((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[58] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = false;
    return yyVal;
  }
};
states[59] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-3+yyTop]));
                    InNode in = ((ParserSupport31)support).newInNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]), null, null);
                    yyVal = ((ParserSupport31)support).newCaseInNode(support.union(((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), in);
    return yyVal;
  }
};
states[60] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = false;
    return yyVal;
  }
};
states[61] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-3+yyTop]));
                    InNode in = ((ParserSupport31)support).newInNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]), null, null);
                    yyVal = ((ParserSupport31)support).newCaseInNode(support.union(((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), in);
    return yyVal;
  }
};
states[63] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[67] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[68] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[69] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(((Token)yyVals[-4+yyTop]).getPosition(), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[70] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_fcall(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[71] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                    yyVal = ((FCallNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[72] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
                    yyVal = ((FCallNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[73] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[74] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
    return yyVal;
  }
};
states[75] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[76] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[77] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop])); /* .setPosFrom($2);*/
    return yyVal;
  }
};
states[78] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_yield(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[79] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ReturnNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), ((Node)yyVals[0+yyTop]).getPosition()));
    return yyVal;
  }
};
states[80] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BreakNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), ((Node)yyVals[0+yyTop]).getPosition()));
    return yyVal;
  }
};
states[81] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new NextNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), ((Node)yyVals[0+yyTop]).getPosition()));
    return yyVal;
  }
};
states[83] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[84] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((MultipleAsgnNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[85] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                    yyVal = new MultipleAsgnNode(pos, support.newArrayNode(pos, ((Node)yyVals[-1+yyTop])), null, null);
    return yyVal;
  }
};
states[86] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[87] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]).add(((Node)yyVals[0+yyTop])), null, null);
    return yyVal;
  }
};
states[88] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), (ListNode) null);
    return yyVal;
  }
};
states[89] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]), ((Node)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[90] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[91] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), new StarNode(lexer.getPosition()), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[92] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[93] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-3+yyTop]).getPosition(), null, ((Node)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[94] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      yyVal = new MultipleAsgnNode(((Token)yyVals[0+yyTop]).getPosition(), null, new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[95] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      yyVal = new MultipleAsgnNode(((Token)yyVals[-2+yyTop]).getPosition(), null, new StarNode(lexer.getPosition()), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[97] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[98] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[99] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[100] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[101] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[102] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[103] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[104] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[105] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[106] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[107] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[108] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon2(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[109] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon3(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[110] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[111] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[112] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[113] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[114] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[115] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[116] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[117] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon2(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[118] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon3(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[119] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[120] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("class/module name must be CONSTANT");
    return yyVal;
  }
};
states[122] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon3(((Token)yyVals[-1+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[123] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(((Token)yyVals[0+yyTop]).getPosition(), null, (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[124] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[128] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_ENDFN);
                   yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[129] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_ENDFN);
                   yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[130] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new LiteralNode(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[131] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new LiteralNode(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[132] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((LiteralNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[133] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[134] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newUndef(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[135] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[136] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((Node)yyVals[-3+yyTop]), support.newUndef(((Node)yyVals[-3+yyTop]).getPosition(), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[209] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    /* FIXME: Consider fixing node_assign itself rather than single case*/
                    ((Node)yyVal).setPosition(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[210] = new ParserState() {
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
states[211] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
  /* FIXME: arg_concat missing for opt_call_args*/
                    yyVal = support.new_opElementAsgnNode(support.union(((Node)yyVals[-5+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), (String) ((Token)yyVals[-1+yyTop]).getValue(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[212] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOpAsgn(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), (String) ((Token)yyVals[-3+yyTop]).getValue(), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[213] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOpAsgn(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), (String) ((Token)yyVals[-3+yyTop]).getValue(), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[214] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOpAsgn(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), (String) ((Token)yyVals[-3+yyTop]).getValue(), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[215] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("constant re-assignment");
    return yyVal;
  }
};
states[216] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("constant re-assignment");
    return yyVal;
  }
};
states[217] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[218] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));
    
                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[219] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]),  ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[220] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-1+yyTop]));

                    boolean isLiteral = ((Node)yyVals[-1+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), new ImplicitNilNode(((Token)yyVals[0+yyTop]).getPosition()), false, isLiteral);
    return yyVal;
  }
};
states[221] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-1+yyTop]));

                    boolean isLiteral = ((Node)yyVals[-1+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), new ImplicitNilNode(((Token)yyVals[0+yyTop]).getPosition()), true, isLiteral);
    return yyVal;
  }
};
states[222] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    boolean isLiteral = ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), new ImplicitNilNode(((Token)yyVals[-1+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[223] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    boolean isLiteral = ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), new ImplicitNilNode(((Token)yyVals[-1+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[224] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "+", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[225] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "-", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[226] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "*", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[227] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "/", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[228] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "%", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[229] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[230] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((NumericNode)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[231] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getUnaryCallNode(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[232] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getUnaryCallNode(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[233] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "|", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[234] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "^", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[235] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "&", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[236] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=>", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[237] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[238] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">=", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[239] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[240] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[241] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "==", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[242] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "===", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[243] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "!=", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[244] = new ParserState() {
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
states[245] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "!~", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[246] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), "!");
    return yyVal;
  }
};
states[247] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]), "~");
    return yyVal;
  }
};
states[248] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<<", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[249] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">>", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[250] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newAndNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[251] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOrNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[252] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new DefinedNode(((Token)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[253] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.getPosition(((Node)yyVals[-5+yyTop])), support.getConditionNode(((Node)yyVals[-5+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[254] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]);

                    yyVal = new DefnNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
    return yyVal;
  }
};
states[255] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = new RescueNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[0+yyTop]), null), null);

                    yyVal = new DefnNode(support.union(((DefHolder)yyVals[-5+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-5+yyTop]).nameNode, ((ArgsNode)yyVals[-4+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
    return yyVal;
  }
};
states[256] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]);

                    yyVal = new DefsNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).receiver, ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[257] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = new RescueNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[0+yyTop]), null), null);

                    yyVal = new DefsNode(support.union(((DefHolder)yyVals[-5+yyTop]).keyword, ((Node)yyVals[0+yyTop])), ((DefHolder)yyVals[-5+yyTop]).receiver, ((DefHolder)yyVals[-5+yyTop]).nameNode, ((ArgsNode)yyVals[-4+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[258] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[259] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[260] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    Node body = ((Node)yyVals[0+yyTop]);
                    yyVal = new RescueNode(position, ((Node)yyVals[-2+yyTop]), new RescueBodyNode(((Token)yyVals[-1+yyTop]).getPosition(), null, body, null), null);
    return yyVal;
  }
};
states[261] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[263] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[264] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[265] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((ListNode)yyVals[-1+yyTop]).getPosition(), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[266] = new ParserState() {
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
states[267] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                    Node fwd = support.getCurrentScope().declare(((Token)yyVals[-1+yyTop]).getPosition(), "...");
                    Node splat = support.newSplatNode(pos, fwd);
                    yyVal = support.newArrayNode(pos, splat);
    return yyVal;
  }
};
states[268] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop]));
                    Node fwd = support.getCurrentScope().declare(((Token)yyVals[-1+yyTop]).getPosition(), "...");
                    Node splat = support.newSplatNode(((Token)yyVals[-1+yyTop]).getPosition(), fwd);
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), splat);
                    ((Node)yyVal).setPosition(pos);
    return yyVal;
  }
};
states[273] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[274] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[275] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((ListNode)yyVals[-1+yyTop]).getPosition(), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[276] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[277] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_blk_pass(((Node)yyVals[-1+yyTop]), ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[278] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((ListNode)yyVals[-1+yyTop]).getPosition(), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
                    yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[279] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
                    yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[280] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[281] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = Long.valueOf(lexer.getCmdArgumentState().begin());
    return yyVal;
  }
};
states[282] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getCmdArgumentState().reset(((Long)yyVals[-1+yyTop]).longValue());
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[283] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockPassNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[284] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node block = support.getCurrentScope().declare(((Token)yyVals[0+yyTop]).getPosition(), "&");
                    yyVal = new BlockPassNode(((Token)yyVals[0+yyTop]).getPosition(), block);
    return yyVal;
  }
};
states[285] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((BlockPassNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[287] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Node)yyVals[0+yyTop]) == null ? lexer.getPosition() : ((Node)yyVals[0+yyTop]).getPosition();
                    yyVal = support.newArrayNode(pos, ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[288] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newSplatNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[289] = new ParserState() {
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
states[290] = new ParserState() {
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
states[291] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[292] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[293] = new ParserState() {
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
states[294] = new ParserState() {
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
states[295] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newSplatNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));  
    return yyVal;
  }
};
states[302] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((ListNode)yyVals[0+yyTop]); /* FIXME: Why complaining without $$ = $1;*/
    return yyVal;
  }
};
states[303] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((ListNode)yyVals[0+yyTop]); /* FIXME: Why complaining without $$ = $1;*/
    return yyVal;
  }
};
states[306] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.new_fcall(((Token)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[307] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BeginNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[308] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_ENDARG);
    return yyVal;
  }
};
states[309] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null; /*FIXME: Should be implicit nil?*/
    return yyVal;
  }
};
states[310] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_ENDARG); 
    return yyVal;
  }
};
states[311] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.warning(ID.GROUPED_EXPRESSION, ((Token)yyVals[-3+yyTop]).getPosition(), "(...) interpreted as grouped expression");
                    yyVal = ((Node)yyVals[-2+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[312] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                    Node value = ((Node)yyVals[-1+yyTop]) == null ? new ImplicitNilNode(pos) : ((Node)yyVals[-1+yyTop]);
                    yyVal = value;
                    ((ISourcePositionHolder)yyVal).setPosition(pos);
    return yyVal;
  }
};
states[313] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[314] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon3(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[315] = new ParserState() {
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
states[316] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new HashNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[317] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ReturnNode(((Token)yyVals[0+yyTop]).getPosition(), null);
    return yyVal;
  }
};
states[318] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_yield(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[319] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZYieldNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[320] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZYieldNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[321] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new DefinedNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[322] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Token)yyVals[-3+yyTop]), support.getConditionNode(((Node)yyVals[-1+yyTop])));
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                    ((CallNode)yyVal).setName("!");
    return yyVal;
  }
};
states[323] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Token)yyVals[-2+yyTop]), null);

                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
                    ((CallNode)yyVal).setName("!");
    return yyVal;
  }
};
states[324] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-1+yyTop]), null, ((IterNode)yyVals[0+yyTop]));
                    yyVal = ((FCallNode)yyVals[-1+yyTop]);                    
    return yyVal;
  }
};
states[326] = new ParserState() {
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
states[327] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((LambdaNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[328] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[329] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[330] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().begin();
    return yyVal;
  }
};
states[331] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().end();
    return yyVal;
  }
};
states[332] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);
                    yyVal = new WhileNode(support.union(((Token)yyVals[-6+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), body);
    return yyVal;
  }
};
states[333] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().begin();
    return yyVal;
  }
};
states[334] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().end();
    return yyVal;
  }
};
states[335] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);
                    yyVal = new UntilNode(support.union(((Token)yyVals[-6+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), body);
    return yyVal;
  }
};
states[336] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newCaseNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[337] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newCaseNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), null, ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[338] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newCaseInNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[339] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().begin();
    return yyVal;
  }
};
states[340] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().end();
    return yyVal;
  }
};
states[341] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      /* ENEBO: Lots of optz in 1.9 parser here*/
                    yyVal = new ForNode(support.union(((Token)yyVals[-8+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-7+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-4+yyTop]), support.getCurrentScope());
    return yyVal;
  }
};
states[342] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("class definition in method body");
                    }
                    support.pushLocalScope();
    return yyVal;
  }
};
states[343] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);

                    yyVal = new ClassNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-4+yyTop]), support.getCurrentScope(), body, ((Node)yyVals[-3+yyTop]));
                    support.popCurrentScope();
    return yyVal;
  }
};
states[344] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = Boolean.valueOf(support.isInDef());
                    support.setInDef(false);
    return yyVal;
  }
};
states[345] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = Integer.valueOf(support.getInSingle());
                    support.setInSingle(0);
                    support.pushLocalScope();
    return yyVal;
  }
};
states[346] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new SClassNode(support.union(((Token)yyVals[-7+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                    support.popCurrentScope();
                    support.setInDef(((Boolean)yyVals[-4+yyTop]).booleanValue());
                    support.setInSingle(((Integer)yyVals[-2+yyTop]).intValue());
    return yyVal;
  }
};
states[347] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) { 
                        support.yyerror("module definition in method body");
                    }
                    support.pushLocalScope();
    return yyVal;
  }
};
states[348] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);

                    yyVal = new ModuleNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-3+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
    return yyVal;
  }
};
states[349] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);

                    yyVal = new DefnNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Token)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
    return yyVal;
  }
};
states[350] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);

                    yyVal = new DefsNode(support.union(((DefHolder)yyVals[-3+yyTop]).keyword, ((Token)yyVals[0+yyTop])), ((DefHolder)yyVals[-3+yyTop]).receiver, ((DefHolder)yyVals[-3+yyTop]).nameNode, ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[351] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BreakNode(((Token)yyVals[0+yyTop]).getPosition(), null);
    return yyVal;
  }
};
states[352] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new NextNode(((Token)yyVals[0+yyTop]).getPosition(), null);
    return yyVal;
  }
};
states[353] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new RedoNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[354] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new RetryNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[355] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[362] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(((Token)yyVals[-4+yyTop]).getPosition(), support.getConditionNode(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[364] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[366] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[367] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[368] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[369] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[370] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[371] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[372] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), support.assignable(((Token)yyVals[0+yyTop]), null), null);
    return yyVal;
  }
};
states[373] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-5+yyTop]).getPosition(), ((ListNode)yyVals[-5+yyTop]), support.assignable(((Token)yyVals[-2+yyTop]), null), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[374] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[375] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]), new StarNode(lexer.getPosition()), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[376] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-1+yyTop]).getPosition(), null, support.assignable(((Token)yyVals[0+yyTop]), null), null);
    return yyVal;
  }
};
states[377] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-3+yyTop]).getPosition(), null, support.assignable(((Token)yyVals[-2+yyTop]), null), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[378] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[0+yyTop]).getPosition(), null, new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[379] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgnNode(((Token)yyVals[-2+yyTop]).getPosition(), null, null, ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[380] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[381] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[382] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((Token)yyVals[-1+yyTop]).getPosition(), null, ((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[383] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArgsTailHolder(((KeywordRestArgNode)yyVals[-1+yyTop]).getPosition(), null, ((KeywordRestArgNode)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[384] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((BlockArgNode)yyVals[0+yyTop]).getPosition(), null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[385] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsTailHolder)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[386] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(lexer.getPosition(), null, null, null);
    return yyVal;
  }
};
states[387] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[388] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-7+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-7+yyTop]), ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[389] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[390] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[391] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[392] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    RestArgNode rest = new UnnamedRestArgNode(((ListNode)yyVals[-1+yyTop]).getPosition(), null, support.getCurrentScope().addVariable("*"));
                    yyVal = support.new_args(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, rest, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[393] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[394] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]), null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[395] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[396] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[397] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[398] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[399] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((RestArgNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[400] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((RestArgNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[401] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ArgsTailHolder)yyVals[0+yyTop]).getPosition(), null, null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[402] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* was $$ = null;*/
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[403] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.commandStart = true;
                    yyVal = ((ArgsNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[404] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, null, null, null, (BlockArgNode) null);
                    ((ArgsNode)yyVal).setShadow(((ListNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[405] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((Token)yyVals[0+yyTop]).getPosition(), null, null, null, null, (BlockArgNode) null);
    return yyVal;
  }
};
states[406] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ((Node)yyVals[-2+yyTop]).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                    ((ArgsNode)yyVals[-2+yyTop]).setShadow(((ListNode)yyVals[-1+yyTop]));
                    yyVal = ((ArgsNode)yyVals[-2+yyTop]);
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
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[409] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[410] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[411] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_bv(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[412] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[413] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
                    yyVal = lexer.getLeftParenBegin();
                    lexer.setLeftParenBegin(lexer.incrementParenNest());
    return yyVal;
  }
};
states[414] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new LambdaNode(support.union(((ArgsNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((ArgsNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
                    lexer.setLeftParenBegin(((Integer)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[415] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-2+yyTop]);
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[416] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[417] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[418] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[419] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[420] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[421] = new ParserState() {
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
states[422] = new ParserState() {
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
states[423] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[424] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[425] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[426] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                    yyVal = ((FCallNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[427] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[428] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[429] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[430] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-2+yyTop]), new Token("call", ((Node)yyVals[-2+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[431] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-2+yyTop]), new Token("call", ((Node)yyVals[-2+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[432] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[433] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZSuperNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[434] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-3+yyTop]) instanceof SelfNode) {
                      yyVal = support.new_fcall(new Token("[]", support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]))), ((Node)yyVals[-1+yyTop]), null);
                    } else {
                      yyVal = support.new_call(((Node)yyVals[-3+yyTop]), new Token("[]", support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]))), ((Node)yyVals[-1+yyTop]), null);
                    }
    return yyVal;
  }
};
states[435] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[436] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[437] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[438] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[439] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newWhenNode(support.union(((Token)yyVals[-4+yyTop]), support.unwrapNewlineNode(((Node)yyVals[-1+yyTop]))), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[442] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = false;
    return yyVal;
  }
};
states[443] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newInNode(support.union(((Token)yyVals[-5+yyTop]), support.unwrapNewlineNode(((Node)yyVals[-1+yyTop]))), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[447] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null);
    return yyVal;
  }
};
states[448] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), null, ((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[450] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ArrayPatternNode tail = ((ParserSupport31)support).newArrayPatternTail(((Node)yyVals[-1+yyTop]).getPosition(), null, true, null, null);
                    yyVal = ((ParserSupport31)support).newArrayPattern(((Node)yyVals[-1+yyTop]).getPosition(), null, ((Node)yyVals[-1+yyTop]), tail);
    return yyVal;
  }
};
states[451] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPattern(((Node)yyVals[-2+yyTop]).getPosition(), null, ((Node)yyVals[-2+yyTop]), ((ArrayPatternNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[452] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newFindPattern(((FindPatternNode)yyVals[0+yyTop]).getPosition(), null, ((FindPatternNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[453] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPattern(((ArrayPatternNode)yyVals[0+yyTop]).getPosition(), null, null, ((ArrayPatternNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[454] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPattern(((HashPatternNode)yyVals[0+yyTop]).getPosition(), null, ((HashPatternNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[456] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newPatternBind(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[458] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOrNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[465] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), null, ((ArrayPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[466] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newFindPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((FindPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[467] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((HashPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[468] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ArrayPatternNode tail = ((ParserSupport31)support).newArrayPatternTail(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, false, null, null);
                    yyVal = ((ParserSupport31)support).newArrayPattern(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null, tail);
    return yyVal;
  }
};
states[469] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), null, ((ArrayPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[470] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newFindPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((FindPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[471] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPattern(support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((HashPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[472] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ArrayPatternNode tail = ((ParserSupport31)support).newArrayPatternTail(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, false, null, null);
                    yyVal = ((ParserSupport31)support).newArrayPattern(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null, tail);
    return yyVal;
  }
};
states[473] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPattern(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, null, ((ArrayPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[474] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newFindPattern(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, ((FindPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[475] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ArrayPatternNode tail = ((ParserSupport31)support).newArrayPatternTail(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), null, false, null, null);
                    yyVal = ((ParserSupport31)support).newArrayPattern(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), null, null, tail);
    return yyVal;
  }
};
states[476] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPattern(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, ((HashPatternNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[477] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    HashPatternNode tail = ((ParserSupport31)support).newHashPatternTail(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), null, null, false);
                    yyVal = ((ParserSupport31)support).newHashPattern(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), null, tail);
    return yyVal;
  }
};
states[478] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[479] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ListNode pre = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
                    yyVal = ((ParserSupport31)support).newArrayPatternTail(((Node)yyVals[0+yyTop]).getPosition(), pre, false, null, null);
    return yyVal;
  }
};
states[480] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPatternTail(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), true, null, null);
    return yyVal;
  }
};
states[481] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ListNode pre = ((ListNode)yyVals[-1+yyTop]);
                    for (Node n : ((ListNode)yyVals[0+yyTop]).childNodes()) pre.add(n);
                    yyVal = ((ParserSupport31)support).newArrayPatternTail(((ListNode)yyVals[-1+yyTop]).getPosition(), pre, false, null, null);
    return yyVal;
  }
};
states[482] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPatternTail(support.union(((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((ListNode)yyVals[-2+yyTop]), true, ((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[483] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPatternTail(support.union(((ListNode)yyVals[-4+yyTop]), ((ListNode)yyVals[0+yyTop])), ((ListNode)yyVals[-4+yyTop]), true, ((Token)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[484] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPatternTail(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), true, null, null);
    return yyVal;
  }
};
states[485] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPatternTail(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), true, null, ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[487] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[488] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ListNode head = ((ListNode)yyVals[-2+yyTop]);
                    for (Node n : ((ListNode)yyVals[-1+yyTop]).childNodes()) head.add(n);
                    yyVal = head;
    return yyVal;
  }
};
states[489] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPatternTail(((Token)yyVals[0+yyTop]) != null ? ((Token)yyVals[0+yyTop]).getPosition() : lexer.getPosition(), null, true, ((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[490] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newArrayPatternTail(((Token)yyVals[-2+yyTop]) != null ? ((Token)yyVals[-2+yyTop]).getPosition() : lexer.getPosition(), null, true, ((Token)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[492] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ListNode post = ((ListNode)yyVals[-2+yyTop]);
                    for (Node n : ((ListNode)yyVals[0+yyTop]).childNodes()) post.add(n);
                    yyVal = post;
    return yyVal;
  }
};
states[493] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newFindPatternTail(((ListNode)yyVals[-2+yyTop]).getPosition(), ((Token)yyVals[-4+yyTop]), ((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[494] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[495] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[496] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[497] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPatternTail(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]), false);
    return yyVal;
  }
};
states[498] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPatternTail(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), null, false);
    return yyVal;
  }
};
states[499] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPatternTail(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, false);
    return yyVal;
  }
};
states[500] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPatternTail(((Token)yyVals[0+yyTop]).getPosition(), null, ((Token)yyVals[0+yyTop]), false);
    return yyVal;
  }
};
states[501] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPatternTail(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), null, true);
    return yyVal;
  }
};
states[502] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).newHashPatternTail(((Token)yyVals[0+yyTop]).getPosition(), null, null, true);
    return yyVal;
  }
};
states[504] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).addAll(((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[505] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Token)yyVals[-1+yyTop]).getPosition();
                    yyVal = support.newArrayNode(pos, new SymbolNode(pos, (String) ((Token)yyVals[-1+yyTop]).getValue())).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[506] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Token)yyVals[0+yyTop]).getPosition();
                    Node value = ((ParserSupport31)support).assignablePatternVariable(pos, (String) ((Token)yyVals[0+yyTop]).getValue());
                    yyVal = support.newArrayNode(pos, new SymbolNode(pos, (String) ((Token)yyVals[0+yyTop]).getValue())).add(value);
    return yyVal;
  }
};
states[507] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[508] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[509] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[510] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[512] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[513] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[514] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-1+yyTop]));
                    boolean isLiteral = ((Node)yyVals[-1+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), new ImplicitNilNode(((Token)yyVals[0+yyTop]).getPosition()), false, isLiteral);
    return yyVal;
  }
};
states[515] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-1+yyTop]));
                    boolean isLiteral = ((Node)yyVals[-1+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), new ImplicitNilNode(((Token)yyVals[0+yyTop]).getPosition()), true, isLiteral);
    return yyVal;
  }
};
states[520] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    boolean isLiteral = ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), new ImplicitNilNode(((Token)yyVals[-1+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[521] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    boolean isLiteral = ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), new ImplicitNilNode(((Token)yyVals[-1+yyTop]).getPosition()), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[530] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[532] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).assignablePatternVariable(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[533] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ParserSupport31)support).patternVarRef(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[534] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[535] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-1+yyTop]));
                    yyVal = new BeginNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[539] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Colon3Node(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[540] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[541] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[542] = new ParserState() {
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
states[543] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null; 
    return yyVal;
  }
};
states[544] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[545] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.splat_array(((Node)yyVals[0+yyTop]));
                    if (yyVal == null) yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[547] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[549] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[551] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((NumericNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[552] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    /* FIXME: We may be intern'ing more than once.*/
                    yyVal = new SymbolNode(((Token)yyVals[0+yyTop]).getPosition(), ((String) ((Token)yyVals[0+yyTop]).getValue()).intern());
    return yyVal;
  }
};
states[554] = new ParserState() {
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
states[555] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (yyVals[0+yyTop] instanceof StrNode) {
                        yyVal = ((StrNode)yyVals[0+yyTop]);
                    } else {
                        yyVal = new StrNode(((Token)yyVals[-1+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
                    }
    return yyVal;
  }
};
states[556] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[557] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.literal_concat(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[558] = new ParserState() {
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
states[559] = new ParserState() {
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
states[560] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newRegexpNode(support.union(((Token)yyVals[-2+yyTop]), ((RegexpNode)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), (RegexpNode) ((RegexpNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[561] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[562] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[563] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[564] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]) instanceof EvStrNode ? new DStrNode(((ListNode)yyVals[-2+yyTop]).getPosition()).add(((Node)yyVals[-1+yyTop])) : ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[566] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[567] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[568] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[569] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[570] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]) instanceof EvStrNode ? new DSymbolNode(((ListNode)yyVals[-2+yyTop]).getPosition()).add(((Node)yyVals[-1+yyTop])) : support.asSymbol(((ListNode)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[571] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[572] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[573] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[574] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[575] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[576] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[577] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[578] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(support.asSymbol(((ListNode)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[579] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new StrNode(((Token)yyVals[0+yyTop]).getPosition(), "");
    return yyVal;
  }
};
states[580] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.literal_concat(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[581] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[582] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[583] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[584] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* FIXME: mri is different here.*/
                    yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[585] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[586] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = lexer.getStrTerm();
                    lexer.setStrTerm(null);
                    lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[587] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setStrTerm(((StrTerm)yyVals[-1+yyTop]));
                    yyVal = new EvStrNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[588] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = lexer.getStrTerm();
                   lexer.getConditionState().stop();
                   lexer.getCmdArgumentState().stop();
                   lexer.setStrTerm(null);
                   lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[589] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.getConditionState().restart();
                   lexer.getCmdArgumentState().restart();
                   lexer.setStrTerm(((StrTerm)yyVals[-2+yyTop]));

                   yyVal = support.newEvStrNode(support.union(((Token)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[590] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new GlobalVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[591] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new InstVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[592] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new ClassVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[594] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     lexer.setState(LexState.EXPR_END);
                     yyVal = ((Token)yyVals[0+yyTop]);
                     ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[599] = new ParserState() {
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
states[600] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((NumericNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[601] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.negateNumeric(((NumericNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[602] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[603] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((FloatNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[604] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((RationalNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[605] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[611] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("nil", Tokens.kNIL, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[612] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("self", Tokens.kSELF, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[613] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("true", Tokens.kTRUE, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[614] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("false", Tokens.kFALSE, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[615] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("__FILE__", Tokens.k__FILE__, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[616] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("__LINE__", Tokens.k__LINE__, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[617] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("__ENCODING__", Tokens.k__ENCODING__, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[618] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[619] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[620] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[621] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[622] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[623] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[624] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[625] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[626] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[627] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = null;
    return yyVal;
  }
};
states[628] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.setInDef(true);
                    support.pushLocalScope();
                    yyVal = new DefHolder(((Token)yyVals[-1+yyTop]), new MethodNameNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue()));
    return yyVal;
  }
};
states[629] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[630] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.setInSingle(support.getInSingle() + 1);
                    support.pushLocalScope();
                    lexer.setState(LexState.EXPR_ENDFN); /* force for args */
                    yyVal = new DefHolder(((Token)yyVals[-4+yyTop]), new MethodNameNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue()), ((Node)yyVals[-3+yyTop]));
    return yyVal;
  }
};
states[631] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-1+yyTop]);
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = true;
    return yyVal;
  }
};
states[633] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-1+yyTop]);
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = true;
    return yyVal;
  }
};
states[635] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[636] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(support.union(((ListNode)yyVals[-3+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[637] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(support.union(((ListNode)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[638] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(support.union(((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), null, ((Token)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[639] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArgsTailHolder(((KeywordRestArgNode)yyVals[-1+yyTop]).getPosition(), null, ((KeywordRestArgNode)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[640] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((BlockArgNode)yyVals[0+yyTop]).getPosition(), null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[641] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsTailHolder)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[642] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(lexer.getPosition(), null, null, null);
    return yyVal;
  }
};
states[643] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[644] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-7+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-7+yyTop]), ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[645] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[646] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[647] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[648] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[649] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]), null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[650] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[651] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[652] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[653] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[654] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((RestArgNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[655] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.union(((RestArgNode)yyVals[-3+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop])), null, null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[656] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ArgsTailHolder)yyVals[0+yyTop]).getPosition(), null, null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[657] = new ParserState() {
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
states[658] = new ParserState() {
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
states[659] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
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
                    support.yyerror("formal argument cannot be a constant");
    return yyVal;
  }
};
states[662] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be an instance variable");
    return yyVal;
  }
};
states[663] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be a global variable");
    return yyVal;
  }
};
states[664] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be a class variable");
    return yyVal;
  }
};
states[666] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.formal_argument(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[667] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_var(((Token)yyVals[0+yyTop]));
  /*
                    $$ = new ArgAuxiliaryNode($1.getPosition(), (String) $1.getValue(), 1);
  */
    return yyVal;
  }
};
states[668] = new ParserState() {
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
states[669] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[670] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
                    yyVal = ((ListNode)yyVals[-2+yyTop]);
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
                    support.arg_var(support.formal_argument(((Token)yyVals[-1+yyTop])));
                    yyVal = support.keyword_arg(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.assignable(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[673] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    /* FIXME: null is a node type in main parser*/
                    yyVal = support.keyword_arg(((Token)yyVals[0+yyTop]).getPosition(), support.assignable(((Token)yyVals[0+yyTop]), null));
    return yyVal;
  }
};
states[674] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((Token)yyVals[-1+yyTop])));
                    yyVal = support.keyword_arg(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.assignable(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[675] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((Token)yyVals[0+yyTop])));
                    yyVal = support.keyword_arg(((Token)yyVals[0+yyTop]).getPosition(), support.assignable(((Token)yyVals[0+yyTop]), null));
    return yyVal;
  }
};
states[676] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[677] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[678] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[679] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[680] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[681] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[682] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[683] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new Token("**", ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[684] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new KeywordRestArgNode(((Token)yyVals[-1+yyTop]).getPosition(), "nil", -1);
    return yyVal;
  }
};
states[685] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((Token)yyVals[-2+yyTop])));
                    yyVal = new OptArgNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.assignable(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[686] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((Token)yyVals[-2+yyTop])));
                    yyVal = new OptArgNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.assignable(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[687] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockNode(support.getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[688] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[689] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockNode(support.getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[690] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[693] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (!support.is_local_id(((Token)yyVals[0+yyTop]))) {
                        support.yyerror("rest argument must be local variable");
                    }
                    
                    yyVal = new RestArgNode(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), support.arg_var(support.shadowing_lvar(((Token)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[694] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new UnnamedRestArgNode(((Token)yyVals[0+yyTop]).getPosition(), "", support.getCurrentScope().addVariable("*"));
    return yyVal;
  }
};
states[697] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (!support.is_local_id(((Token)yyVals[0+yyTop]))) {
                        support.yyerror("block argument must be local variable");
                    }
                    
                    yyVal = new BlockArgNode(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), support.arg_var(support.shadowing_lvar(((Token)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[698] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockArgNode(((Token)yyVals[0+yyTop]).getPosition(), support.getCurrentScope().addVariable("&"), "&");
    return yyVal;
  }
};
states[699] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((BlockArgNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[700] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[701] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (!(((Node)yyVals[0+yyTop]) instanceof SelfNode)) {
                        support.checkExpression(((Node)yyVals[0+yyTop]));
                    }
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[702] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[703] = new ParserState() {
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
states[704] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[705] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[707] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).addAll(((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[708] = new ParserState() {
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
states[709] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Token)yyVals[-1+yyTop]).getPosition();
                    yyVal = support.newArrayNode(pos, new SymbolNode(pos, (String) ((Token)yyVals[-1+yyTop]).getValue())).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[710] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SourcePosition pos = ((Token)yyVals[0+yyTop]).getPosition();
                    String name = (String) ((Token)yyVals[0+yyTop]).getValue();
                    Node val = support.getCurrentScope().declare(pos, name);
                    yyVal = support.newArrayNode(pos, new SymbolNode(pos, name)).add(val);
    return yyVal;
  }
};
states[711] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      SourcePosition pos = ((Token)yyVals[-3+yyTop]).getPosition();
                      yyVal = support.newArrayNode(pos, new SymbolNode(pos, ((StrNode) ((Node)yyVals[-2+yyTop])).getValue())).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[712] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Token)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[0+yyTop])).add(null);
    return yyVal;
  }
};
states[731] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[732] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[740] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      yyVal = null;
    return yyVal;
  }
};
states[741] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = null;
    return yyVal;
  }
};
}
					// line 2772 "Ruby31Parser.y"

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
					// line 10730 "-"
