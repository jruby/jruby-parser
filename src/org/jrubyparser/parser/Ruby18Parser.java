// created by jay 1.0.2 (c) 2002-2004 ats@cs.rit.edu
// skeleton Java 1.0 (c) 2002 ats@cs.rit.edu

					// line 2 "Ruby18Parser.y"
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
 * Copyright (C) 2001 Alan Moore <alan_moore@gmx.net>
 * Copyright (C) 2001-2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2001-2004 Stefan Matthias Aust <sma@3plus4.de>
 * Copyright (C) 2001-2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004-2006 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Charles O Nutter <headius@headius.com>
 * Copyright (C) 2006 Miguel Covarrubias <mlcovarrubias@gmail.com>
 * Copyright (C) 2007 Mirko Stocker <me@misto.ch>
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
package org.jrubyparser.parser;

import java.io.IOException;

import org.jrubyparser.ast.ArgsNode;
import org.jrubyparser.ast.ArgumentNode;
import org.jrubyparser.ast.ArrayNode;
import org.jrubyparser.ast.AssignableNode;
import org.jrubyparser.ast.BackRefNode;
import org.jrubyparser.ast.BeginNode;
import org.jrubyparser.ast.BlockAcceptingNode;
import org.jrubyparser.ast.BlockArgNode;
import org.jrubyparser.ast.BlockNode;
import org.jrubyparser.ast.BlockPassNode;
import org.jrubyparser.ast.BreakNode;
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
import org.jrubyparser.ast.IArgumentNode;
import org.jrubyparser.ast.IfNode;
import org.jrubyparser.ast.ImplicitNilNode;
import org.jrubyparser.ast.InstVarNode;
import org.jrubyparser.ast.IterNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.LiteralNode;
import org.jrubyparser.ast.MethodNameNode;
import org.jrubyparser.ast.ModuleNode;
import org.jrubyparser.ast.MultipleAsgnNode;
import org.jrubyparser.ast.NewlineNode;
import org.jrubyparser.ast.NextNode;
import org.jrubyparser.ast.NilNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.NotNode;
import org.jrubyparser.ast.OpAsgnAndNode;
import org.jrubyparser.ast.OpAsgnNode;
import org.jrubyparser.ast.OpAsgnOrNode;
import org.jrubyparser.ast.OptArgNode;
import org.jrubyparser.ast.PostExeNode;
import org.jrubyparser.ast.PreExeNode;
import org.jrubyparser.ast.RedoNode;
import org.jrubyparser.ast.RegexpNode;
import org.jrubyparser.ast.RescueBodyNode;
import org.jrubyparser.ast.RescueNode;
import org.jrubyparser.ast.RestArgNode;
import org.jrubyparser.ast.RetryNode;
import org.jrubyparser.ast.ReturnNode;
import org.jrubyparser.ast.SClassNode;
import org.jrubyparser.ast.SValueNode;
import org.jrubyparser.ast.SelfNode;
import org.jrubyparser.ast.SplatNode;
import org.jrubyparser.ast.StarNode;
import org.jrubyparser.ast.StrNode;
import org.jrubyparser.ast.SymbolNode;
import org.jrubyparser.ast.ToAryNode;
import org.jrubyparser.ast.UnaryCallNode;
import org.jrubyparser.ast.UnnamedRestArgNode;
import org.jrubyparser.ast.UntilNode;
import org.jrubyparser.ast.VAliasNode;
import org.jrubyparser.ast.WhileNode;
import org.jrubyparser.ast.XStrNode;
import org.jrubyparser.ast.YieldNode;
import org.jrubyparser.ast.ZArrayNode;
import org.jrubyparser.ast.ZSuperNode;
import org.jrubyparser.ast.ZeroArgNode;
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

public class Ruby18Parser implements RubyParser {
    protected ParserSupport support;
    protected Lexer lexer;

    public Ruby18Parser() {
        this(new ParserSupport());
    }

    public Ruby18Parser(ParserSupport support) {
        this.support = support;
        lexer = new Lexer();
        lexer.setParserSupport(support);
        support.setLexer(lexer);
    }

    public void setWarnings(IRubyWarnings warnings) {
        support.setWarnings(warnings);
        lexer.setWarnings(warnings);
    }
					// line 153 "-"
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
  public static final int tCOMMENT = 381;
  public static final int tWHITESPACE = 382;
  public static final int tDOCUMENTATION = 383;
  public static final int tLOWEST = 384;
  public static final int yyErrorCode = 256;

  /** number of final state.
    */
  protected static final int yyFinal = 1;

  /** parser tables.
      Order is mandated by <i>jay</i>.
    */
  protected static final short[] yyLhs = {
//yyLhs 510
    -1,   102,     0,    32,    31,    33,    33,    33,    33,   105,
    34,    34,    34,    34,    34,    34,    34,    34,    34,    34,
   106,    34,    34,    34,    34,    34,    34,    34,    34,    34,
    34,    34,    34,    34,    34,    35,    35,    35,    35,    35,
    35,    39,    30,    30,    30,    30,    30,    55,    55,    55,
   107,    90,    38,    38,    38,    38,    38,    38,    38,    38,
    91,    91,    93,    93,    92,    92,    92,    92,    92,    92,
    63,    63,    78,    78,    64,    64,    64,    64,    64,    64,
    64,    64,    71,    71,    71,    71,    71,    71,    71,    71,
     7,     7,    29,    29,    29,     8,     8,     8,     8,     8,
    96,    96,    97,    97,    59,   109,    59,     9,     9,     9,
     9,     9,     9,     9,     9,     9,     9,     9,     9,     9,
     9,     9,     9,     9,     9,     9,     9,     9,     9,     9,
     9,     9,     9,   108,   108,   108,   108,   108,   108,   108,
   108,   108,   108,   108,   108,   108,   108,   108,   108,   108,
   108,   108,   108,   108,   108,   108,   108,   108,   108,   108,
   108,   108,   108,   108,   108,   108,   108,   108,   108,   108,
   108,   108,   108,   108,    36,    36,    36,    36,    36,    36,
    36,    36,    36,    36,    36,    36,    36,    36,    36,    36,
    36,    36,    36,    36,    36,    36,    36,    36,    36,    36,
    36,    36,    36,    36,    36,    36,    36,    36,    36,    36,
    36,    36,    36,    36,    36,    36,    36,    36,    65,    68,
    68,    68,    68,    68,    68,    49,    49,    49,    49,    53,
    53,    45,    45,    45,    45,    45,    45,    45,    45,    45,
    46,    46,    46,    46,    46,    46,    46,    46,    46,    46,
    46,    46,   112,    51,    47,   113,    47,   114,    47,    84,
    83,    83,    77,    77,    62,    62,    62,    37,    37,    37,
    37,    37,    37,    37,    37,    37,    37,   115,    37,    37,
    37,    37,    37,    37,    37,    37,    37,    37,    37,    37,
    37,    37,    37,    37,    37,   117,   119,    37,   120,   121,
    37,    37,    37,    37,   122,   123,    37,   124,    37,   126,
   127,    37,   128,    37,   129,    37,   130,   131,    37,    37,
    37,    37,    37,    40,   116,   116,   116,   116,   118,   118,
   118,    43,    43,    41,    41,    98,    98,    99,    99,    69,
    69,    69,    69,    69,    69,    69,    69,    69,    69,    69,
    69,    70,    70,    70,    70,   132,    89,    54,    54,    54,
    22,    22,    22,    22,    22,    22,   133,    88,   134,    88,
    66,    82,    82,    82,    42,    42,    94,    94,    67,    67,
    67,    44,    44,    48,    48,    26,    26,    26,    14,    15,
    15,    16,    17,    18,    23,    23,    74,    74,    25,    25,
    24,    24,    73,    73,    19,    19,    20,    20,    21,   135,
    21,   136,    21,    60,    60,    60,    60,     3,     2,     2,
     2,     2,    28,    27,    27,    27,    27,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,    52,    95,
    61,    61,    50,   137,    50,    50,    56,    56,    57,    57,
    57,    57,    57,    57,    57,    57,    57,   100,   100,   100,
   100,   100,    75,    75,    58,    76,    76,    11,    11,   101,
   101,    12,    12,    87,    86,    86,    13,   138,    13,    81,
    81,    81,    79,    79,    80,     4,     4,     4,     5,     5,
     5,     5,     6,     6,     6,    10,    10,   103,   103,   110,
   110,   111,   111,   111,   125,   125,   104,   104,    72,    85,
    }, yyLen = {
//yyLen 510
     2,     0,     2,     4,     2,     1,     1,     3,     2,     0,
     4,     3,     3,     3,     2,     3,     3,     3,     3,     3,
     0,     5,     4,     3,     3,     3,     6,     5,     5,     5,
     3,     3,     3,     3,     1,     1,     3,     3,     2,     2,
     1,     1,     1,     1,     2,     2,     2,     1,     4,     4,
     0,     5,     2,     3,     4,     5,     4,     5,     2,     2,
     1,     3,     1,     3,     1,     2,     3,     2,     2,     1,
     1,     3,     2,     3,     1,     4,     3,     3,     3,     3,
     2,     1,     1,     4,     3,     3,     3,     3,     2,     1,
     1,     1,     2,     1,     3,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     0,     4,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     3,     5,     3,     6,     5,     5,
     5,     5,     4,     3,     3,     3,     3,     3,     3,     3,
     3,     3,     4,     4,     2,     2,     3,     3,     3,     3,
     3,     3,     3,     3,     3,     3,     3,     3,     3,     2,
     2,     3,     3,     3,     3,     3,     5,     1,     1,     1,
     2,     2,     5,     2,     3,     3,     4,     4,     6,     1,
     1,     1,     2,     5,     2,     5,     4,     7,     3,     1,
     4,     3,     5,     7,     2,     5,     4,     6,     7,     9,
     3,     1,     0,     2,     1,     0,     3,     0,     4,     2,
     2,     1,     1,     3,     3,     4,     2,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     3,     0,     5,     3,
     3,     2,     4,     3,     3,     1,     4,     3,     1,     5,
     2,     1,     2,     6,     6,     0,     0,     7,     0,     0,
     7,     5,     4,     5,     0,     0,     9,     0,     6,     0,
     0,     8,     0,     5,     0,     6,     0,     0,     9,     1,
     1,     1,     1,     1,     1,     1,     1,     2,     1,     1,
     1,     1,     5,     1,     2,     1,     1,     1,     3,     1,
     2,     4,     7,     6,     4,     3,     5,     4,     2,     1,
     2,     1,     2,     1,     3,     0,     5,     2,     4,     4,
     2,     4,     4,     3,     2,     1,     0,     5,     0,     5,
     5,     1,     4,     2,     1,     1,     6,     0,     1,     1,
     1,     2,     1,     2,     1,     1,     1,     1,     1,     1,
     2,     3,     3,     3,     3,     3,     0,     3,     1,     2,
     3,     3,     0,     3,     0,     2,     0,     2,     1,     0,
     3,     0,     4,     1,     1,     1,     1,     2,     1,     1,
     1,     1,     3,     1,     1,     2,     2,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     0,     4,     2,     4,     2,     6,     4,
     4,     2,     4,     2,     2,     1,     0,     1,     1,     1,
     1,     1,     1,     3,     3,     1,     3,     1,     1,     2,
     1,     1,     1,     2,     2,     0,     1,     0,     5,     1,
     2,     2,     1,     3,     3,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     0,     1,     0,
     1,     0,     1,     1,     1,     1,     1,     2,     0,     0,
    }, yyDefRed = {
//yyDefRed 894
     1,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   295,   298,     0,     0,     0,   321,   322,     0,
     0,     0,   433,   432,   434,   435,     0,     0,     0,    20,
     0,   437,   436,     0,     0,   429,   428,     0,   431,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,   404,   406,   406,     0,     0,   440,   441,   423,   424,
     0,   386,     0,   268,     0,   389,   269,   270,     0,   271,
   272,   267,   385,   387,    35,     2,     0,     0,     0,     0,
     0,     0,     0,   273,     0,    43,     0,     0,    70,     0,
     5,     0,     0,    60,     0,     0,   319,   320,   285,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   438,     0,
    93,     0,   323,     0,   274,   312,   142,   153,   143,   166,
   139,   159,   149,   148,   164,   147,   146,   141,   167,   151,
   140,   154,   158,   160,   152,   145,   161,   168,   163,     0,
     0,     0,     0,   138,   157,   156,   169,   170,   171,   172,
   173,   137,   144,   135,   136,     0,     0,     0,    97,     0,
   128,   129,   126,   110,   111,   112,   115,   117,   113,   130,
   131,   118,   119,   477,   123,   122,   109,   127,   125,   124,
   120,   121,   116,   114,   107,   108,   132,   314,    98,     0,
   476,    99,   162,   155,   165,   150,   133,   134,    95,    96,
   101,   100,   103,     0,   102,   104,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   505,   504,     0,
     0,     0,   506,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   337,     0,     0,     0,     0,     0,     0,   231,
    45,     0,     0,     0,   482,   239,    46,    44,     0,    59,
     0,     0,   364,    58,    38,     0,     9,   500,     0,     0,
     0,   194,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   219,     0,     0,   479,     0,
     0,     0,    68,   210,    39,   209,   420,   419,   421,   417,
   418,     0,     0,     0,     0,     0,     0,     0,     0,   368,
   366,   360,     0,   290,   390,   292,     4,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,   355,   357,     0,     0,     0,     0,     0,     0,    72,
     0,     0,     0,     0,     0,     0,   425,   426,     0,    90,
     0,    92,     0,   443,   307,   442,     0,     0,     0,     0,
     0,     0,   495,   496,   316,   105,     0,     0,   276,     0,
   326,   325,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   507,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   350,     0,     0,     0,
   304,     0,     0,   259,     0,     0,   232,   261,     0,   234,
   287,     0,     0,   254,   253,     0,     0,     0,     0,     0,
    11,    13,    12,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,   279,     0,     0,     0,   220,   283,
     0,   502,   221,     0,   223,     0,   481,   480,   284,   411,
   409,   422,   408,   407,   391,   405,   392,   393,   394,   395,
   398,     0,   400,   401,     0,     0,     0,    50,    53,     0,
    15,    16,    17,    18,    19,    36,    37,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   490,     0,     0,   491,     0,
     0,     0,     0,   363,     0,     0,   488,   489,     0,     0,
    30,     0,     0,    23,     0,    31,   262,     0,    66,    73,
    24,    33,     0,    25,     0,     0,   445,     0,     0,     0,
     0,     0,     0,    94,     0,     0,     0,     0,   459,   458,
   457,   460,     0,   468,   467,   472,   471,     0,     0,     0,
     0,   465,     0,     0,   455,   462,     0,     0,     0,   379,
     0,     0,   380,     0,     0,   333,     0,   327,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   302,   330,   329,   296,   328,   299,    71,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,   338,     0,   238,   484,     0,     0,     0,   260,     0,
     0,   483,   286,     0,     0,   257,     0,     0,   251,     0,
     0,     0,     0,     0,   225,     0,    10,     0,     0,    22,
     0,     0,     0,     0,     0,   224,     0,   263,     0,     0,
   397,   399,   403,   353,     0,     0,   351,     0,     0,     0,
     0,     0,     0,   230,     0,   361,   229,     0,     0,   362,
     0,     0,    48,   358,    49,   359,   266,     0,     0,   310,
     0,     0,   282,   313,     0,     0,     0,   469,   473,     0,
   447,     0,   451,     0,   453,     0,   454,   317,   106,     0,
     0,   382,   334,     0,     3,   384,     0,   331,     0,     0,
     0,     0,     0,     0,   301,   303,   373,     0,     0,     0,
     0,   347,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   341,     0,     0,   236,     0,     0,     0,     0,
     0,   244,   256,   226,     0,     0,   227,     0,     0,   289,
    21,   278,     0,     0,   413,   414,   415,   410,   416,   352,
     0,     0,     0,     0,     0,     0,    27,     0,    28,     0,
    55,    29,     0,     0,    57,     0,     0,     0,     0,     0,
   444,   308,   478,   464,     0,   315,     0,   474,   463,     0,
     0,   466,     0,     0,   381,     0,   383,     0,   293,     0,
   294,     0,     0,     0,     0,     0,   346,   305,     0,     0,
   233,     0,   235,   250,   258,     0,     0,     0,   241,     0,
     0,   222,   412,   354,   369,   367,     0,   356,    26,     0,
   265,     0,   446,     0,   449,   450,   452,     0,     0,     0,
   372,   374,   370,   375,   297,   300,     0,   343,     0,     0,
     0,     0,   240,     0,   246,     0,   228,    51,   311,     0,
     0,   376,     0,     0,   342,   237,   242,     0,     0,     0,
   245,   448,   318,   332,   306,     0,     0,   247,     0,   243,
     0,   248,     0,   249,
    }, yyDgoto = {
//yyDgoto 139
     1,   209,   289,    61,   109,   549,   523,   110,   201,   518,
   374,   567,   568,   189,    63,    64,    65,    66,    67,   292,
   291,   463,    68,    69,    70,   471,    71,    72,    73,   111,
    74,   206,   207,    76,    77,    78,    79,    80,    81,   211,
   262,   717,   852,   718,   710,   240,   635,   424,   714,   673,
   364,   249,    83,   675,    84,    85,   569,   570,   571,   203,
   767,   213,   535,    87,    88,   241,   394,   581,   274,   233,
   665,   214,    90,   298,   296,   572,   573,   276,    91,   243,
   244,   281,   599,   416,   628,   417,   702,   797,   303,   342,
   478,    92,    93,   270,   377,   215,   204,   205,     0,   234,
   575,   576,     2,   220,   221,   433,   259,   668,   191,   578,
   258,   452,   250,   639,   748,   446,   382,   223,   603,   729,
   224,   730,   618,   856,   548,   383,   545,   789,   369,   371,
   577,   803,   513,   476,   475,   659,   658,   547,   370,
    }, yySindex = {
//yySindex 894
     0,     0,  5451, 13655, 17222, 17468, 18053, 17945,  5451, 15500,
 15500,  7048,     0,     0, 16976, 14024, 14024,     0,     0, 14024,
  -276,  -255,     0,     0,     0,     0, 15500, 17837,   174,     0,
  -132,     0,     0,     0,     0,     0,     0,     0,     0, 16730,
 16730,   -88,  -142, 13532, 15500, 15623, 16730, 17591, 16730, 16853,
 18160,     0,     0,     0,   200,   208,     0,     0,     0,     0,
     0,     0,  -207,     0,  -128,     0,     0,     0,  -201,     0,
     0,     0,     0,     0,     0,     0,   106,   651,   -13,  4499,
     0,   -20,   -41,     0,  -109,     0,   -72,   226,     0,   236,
     0, 17345,   286,     0,    23,   651,     0,     0,     0,  -276,
  -255,   174,     0,     0,   -83, 15500,  -105,  5451,     0,  -207,
     0,    29,     0,   197,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,  -101,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   326,     0,     0,   156,   164,   126,     0,
   -13,    70,   266,   117,   398,   120,    70,     0,     0,   106,
   -55,   409,     0, 15500, 15500,   168, 13532, 13162, 17714,     0,
   397,     0,     0,   217,   457, 16730, 16730, 16730,  4499,     0,
     0,   175,   460,   477,     0,     0,     0,     0, 13778,     0,
 14147, 14024,     0,     0,     0,  -190,     0,     0, 15746,   170,
  5451,     0,   419,   207,   249,   252,   233, 13532,   230,     0,
   231,   -13, 16730,   174,   237,     0,   114,   163,     0,   166,
   163,   212,     0,     0,     0,     0,     0,     0,     0,     0,
     0,  -215,   -96,   275,   188,   203,   319,   213,  -240,     0,
     0,     0,   238,     0,     0,     0,     0, 13408, 15500, 15500,
 15500, 15500, 13655, 15500, 15500, 16730, 16730, 16730, 16730, 16730,
 16730, 16730, 16730, 16730, 16730, 16730, 16730, 16730, 16730, 16730,
 16730, 16730, 16730, 16730, 16730, 16730, 16730, 16730, 16730, 16730,
 16730,     0,     0, 18322, 18377, 15623, 18432, 18432, 16853,     0,
 15869, 17591,   539, 15869, 16853,   246,     0,     0,   -13,     0,
     0,     0,   106,     0,     0,     0, 18432, 18487, 15623,  5451,
 15500,   590,     0,     0,     0,     0, 15992,   329,     0,   233,
     0,     0,  5451,   335, 18542, 18597, 15623, 16730, 16730, 16730,
  5451,   333,  5451, 16115,   341,     0,   139,   139,     0,   265,
   297,   260,     0,   431,     0,   569,     0, 18652, 18707, 15623,
     0, 17099,   571,     0, 16730, 14270,     0,     0, 14393,     0,
     0,   276, 13901,     0,     0,   -20,   174,   111,   278,   572,
     0,     0,     0, 17945, 15500,  4499,  5451,   258, 18542, 18597,
 16730, 16730, 16730,   280,     0,     0,   174,  2437,     0,     0,
 16238,     0,     0, 16730,     0, 16730,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,    40,     0,     0,   591,  -217,  -217,     0,     0,   651,
     0,     0,     0,     0,     0,     0,     0,   207,   691,   691,
   691,   691,  1891,  1891,  3448,  2962,   691,   691,  2561,  2561,
   755,   755,   207,  1259,   207,   207,   -69,   -69,  1891,  1891,
   822,   822,  3534,  -217,   287,     0,   288,  -255,     0,   290,
     0,   292,  -255,     0,     0,   295,     0,     0,  -255,  -255,
     0,  4499, 16730,     0,  4013,     0,     0,   587,     0,     0,
     0,     0,     0,     0,  4499,   106,     0, 15500,  5451,  -255,
     0,     0,  -255,     0,   296,   371,   140,   575,     0,     0,
     0,     0,   480,     0,     0,     0,     0,   337,   342,  5451,
   106,     0,   609,   617,     0,     0,   625, 18267, 17945,     0,
     0,   332,     0,  5451,   411,     0,   240,     0,   340,   344,
   346,   292,   334,  4013,   329,   415,   424, 16730,   649,    70,
     0,     0,     0,     0,     0,     0,     0,     0, 17714, 18762,
 18817, 15623,   359,     0,     0,     0,     0,   366, 15500, 13285,
 17714,     0,   362,     0,     0, 16730,   175,   670,     0, 16730,
   175,     0,     0, 16730,  4499,     0,    30,   679,     0,   383,
   387, 18432, 18432,   389,     0, 14516,     0,  -171,   379,     0,
   207,   207,  4499,     0,   410,     0, 16730,     0,  5451,  -117,
     0,     0,     0,     0,  4963,  5451,     0,  5451,  -217, 16730,
  5451, 16853, 16853,     0,   238,     0,     0, 16853, 16730,     0,
   238,   416,     0,     0,     0,     0,     0, 16730, 16361,     0,
   106,   493,     0,     0,   426, 16730,   174,     0,     0,   496,
     0,   480,     0,   -81,     0,   211,     0,     0,     0, 17714,
    70,     0,     0,  5451,     0,     0, 15500,     0,   504, 16730,
 16730, 16730,   436,   510,     0,     0,     0, 16484,  5451,  5451,
  5451,     0,     0,     0,     0,     0,   432, 17714,     0,   139,
   428,   743,     0,   571, 14639,     0,   571,   571,   451, 14762,
 14885,     0,     0,     0,  -255,  -255,     0,   -20,   111,     0,
     0,     0,  2437,   439,     0,     0,     0,     0,     0,     0,
   435,   544,   441,  5451,  4499,   545,     0,  4499,     0,  4499,
     0,     0,  4499,  4499,     0, 16853,  4499, 16730,     0,  5451,
     0,     0,     0,     0,   474,     0,   773,     0,     0,   625,
   575,     0,   625,   590,     0,  5451,     0,    70,     0, 16730,
     0, 16730,    11,   554,   559,     0,     0,     0, 17714,   482,
     0, 16730,     0,     0,     0, 16730,   789,   790,     0, 16730,
   494,     0,     0,     0,     0,     0,   476,     0,     0,  4499,
     0,   578,     0,   -81,     0,     0,     0,  5451,   156,  5451,
     0,     0,     0,     0,     0,     0,  5451,     0, 17714,  3041,
   571, 15008,     0, 15131,     0,   571,     0,     0,     0,   625,
   579,     0,   240,   580,     0,     0,     0, 16730,   807, 16730,
     0,     0,     0,     0,     0,   571, 15254,     0,   571,     0,
 16730,     0,   571,     0,
    }, yyRindex = {
//yyRindex 894
     0,     0,   130,     0,     0,     0,     0,     0,   536,     0,
     0,   274,     0,     0,     0,  8628,  8757,     0,     0,  8868,
  4750,  4141,     0,     0,     0,     0,     0,     0, 16607,     0,
     0,     0,     0,  2196,  3292,     0,     0,  2320,     0,     0,
     0,     0,     0,     9,     0,   511,   498,   -14,     0,     0,
   502,     0,     0,     0,   513,  -189,     0,     0,     0,     0,
  9828,     0, 15377,     0,  7908,     0,     0,     0,  8037,     0,
     0,     0,     0,     0,     0,     0,   187,   716,  1888,  3411,
  8148,  3897,     0,     0,  4383,     0,  9957,     0,     0,     0,
     0,   -12,     0,     0,     0,   849,     0,     0,     0,  8277,
  7188,   516,  5992,  6134,     0,     0,     0,     9,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,   997,
  1623,  1969,  2081,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,  2928,  3414,  3900,     0,  4386,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,  5352,     0,     0,  -116,     0,     0,  7317,
  4869,     0,     0,  7557,     0,     0,     0,     0,     0,   583,
     0,   281,     0,     0,     0,     0,     9,  -192,     0,   882,
     0,   937,     0,     0,  -184,     0,     0,     0,  1416,     0,
     0, 12898,  2438,  2438,     0,     0,     0,     0,     0,     0,
     0,   527,     0,     0,     0,     0,     0,     0,     0,     0,
    25,     0,     0,  8997,  8388,  8517, 10068,     9,     0,    21,
     0,    34,     0,   526,     0,     0,   533,   533,     0,   524,
   524,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,  1124,     0,     0,     0,     0,   396,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   511,     0,     0,     0,     0,
     0,    51,    75,     0,     0,     0,     0,     0,   112,     0,
  6521,     0,     0,     0,     0,     0,     0,     0,   511,   536,
     0,   113,     0,     0,     0,     0,    76,   291,     0,  7668,
     0,     0,   481,  6650,     0,     0,   511,     0,     0,     0,
   492,     0,   100,     0,     0,     0,     0,     0,  1316,     0,
     0,     0,    87,     0,   553,  -181,     0,     0,     0,   511,
     0,  -179,  2438,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   537,     0,     0,    38,   546,   546,     0,    52,
     0,     0,     0,     0,     0, 12117,    25,     0,     0,     0,
     0,     0,     0,     0,     0,    83,   546,   526,     0,     0,
   547,     0,     0,  -193,     0,   540,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,  6779,  6919,     0,     0,   950,
     0,     0,     0,     0,     0,     0,     0,  9108, 11285, 11370,
 11471, 11558, 10847, 10953, 11643, 11903, 11731, 11818, 11991, 12078,
 10400, 10515,  9237, 10621,  9348,  9477,  1751, 10289, 11064, 11179,
  1188, 10732,     0,  6779,  5111,     0,  5234,  4264,     0,  5597,
  3655,  5720, 15377,     0,  3778,     0,     0,     0,  5843,  5843,
     0, 12219,     0,     0,  1071,     0,     0,     0,     0,     0,
     0,     0,  1659,     0, 12256,     0,     0,     0,   536,  7428,
  6263,  6392,     0,     0,     0,     0,   546,    45,     0,     0,
     0,     0,    57,     0,     0,     0,     0,    47,     0,   536,
     0,     0,    48,    48,     0,     0,    48,     0,     0,     0,
    67,   202,     0,    35,   631,     0,   631,     0,  2683,  2806,
  3169,  4627,     0, 12990,   631,     0,     0,     0,   205,     0,
     0,     0,     0,     0,     0,     0,     0,  1477,     0,     0,
     0,   511,     0,  1786,  8971,  9082,   624,     0,     0,  -177,
     0,     0,     0,     0,     0,     0, 13051,  2438,     0,     0,
     0,     0,     0,     0,   151,     0,     0,   548,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
  9588,  9717, 12377,    10,     0,     0,     0,     0,    25,     0,
     0,     0,     0,     0,     0,   100,     0,    25,  6919,     0,
   100,     0,     0,     0,  2566,     0,     0,     0,     0,     0,
  2925, 10179,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   546,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   100,     0,     0,     0,     0,     0,     0,
     0,     0,  7797,     0,     0,     0,     0,     0,   196,   100,
   100,     0,  1294,  1461,  1784,   165,     0,     0,  1518,     0,
     0,  -176,     0,  2438,     0,     0,  2438,   548,     0,     0,
     0,     0,     0,     0,   142,   142,     0,     0,   546,     0,
     0,     0,   526,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,    25, 12415,     0,     0, 12511,     0, 12548,
     0,     0, 12655, 12691,     0,     0, 12776,     0, 13092,   536,
     0,     0,     0,     0,     0,     0,    48,     0,     0,    48,
     0,     0,    48,   113,     0,   536,     0,     0,     0,     0,
     0,     0,   631,     0,     0,  2024,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   548,   548,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0, 12837,
     0,     0,     0,     0,     0,     0,     0,   536,  -116,   481,
     0,     0,     0,     0,     0,     0,   100,     0,     0,  2438,
   548,     0,     0,     0,     0,   548,     0,     0,     0,    48,
     0,     0,   631,     0,     0,     0,     0,     0,   548,     0,
     0,     0,     0,     0,     0,   548,     0,     0,   548,     0,
     0,     0,   548,     0,
    }, yyGindex = {
//yyGindex 139
     0,   585,     0,     1,  1556,  1791,     0,   -31,     3,   550,
     0,     0,     0,     0,     0,     0,   840,     0,     0,     0,
   485,  -200,     0,     0,     0,     0,     0,     0,    14,   901,
   -22,   137,  -352,     0,    33,   692,   -15,    18,    98,   153,
   471,  -364,     0,    37,     0,   514,     0,     0,     0,   -10,
     0,    19,   908,  -256,  -222,     0,   115,   357,  -643,     0,
     0,   919,  -238,     2,     4,  1465,  -388,     0,  -178,   256,
  -430,  1056,   158,     0,     0,     0,   224,    -9,     0,    -3,
  -392,     0,     0,   210,    -1,     0,  -510,  -351,   860,     0,
  -481,     0,    26,  -125,    81,  1595,     0,   -25,     0,     0,
   248,  -625,     0,   -17,   855,     0,     0,     0,     0,     0,
   -45,   -26,     0,     0,     0,     0,  -204,     0,  -359,     0,
     0,     0,     0,     0,     0,    -6,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,
    };
    protected static final short[] yyTable = Ruby18YyTables.yyTable();
    protected static final short[] yyCheck = Ruby18YyTables.yyCheck();

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
    "tREGEXP_END","tCOMMENT","tWHITESPACE","tDOCUMENTATION","tLOWEST",
    };

  /** printable rules for debugging.
    */
  protected static final String [] yyRule = {
    "$accept : program",
    "$$1 :",
    "program : $$1 compstmt",
    "bodystmt : compstmt opt_rescue opt_else opt_ensure",
    "compstmt : stmts opt_terms",
    "stmts : none",
    "stmts : stmt",
    "stmts : stmts terms stmt",
    "stmts : error stmt",
    "$$2 :",
    "stmt : kALIAS fitem $$2 fitem",
    "stmt : kALIAS tGVAR tGVAR",
    "stmt : kALIAS tGVAR tBACK_REF",
    "stmt : kALIAS tGVAR tNTH_REF",
    "stmt : kUNDEF undef_list",
    "stmt : stmt kIF_MOD expr_value",
    "stmt : stmt kUNLESS_MOD expr_value",
    "stmt : stmt kWHILE_MOD expr_value",
    "stmt : stmt kUNTIL_MOD expr_value",
    "stmt : stmt kRESCUE_MOD stmt",
    "$$3 :",
    "stmt : klBEGIN $$3 tLCURLY compstmt tRCURLY",
    "stmt : klEND tLCURLY compstmt tRCURLY",
    "stmt : lhs '=' command_call",
    "stmt : mlhs '=' command_call",
    "stmt : var_lhs tOP_ASGN command_call",
    "stmt : primary_value '[' aref_args tRBRACK tOP_ASGN command_call",
    "stmt : primary_value tDOT tIDENTIFIER tOP_ASGN command_call",
    "stmt : primary_value tDOT tCONSTANT tOP_ASGN command_call",
    "stmt : primary_value tCOLON2 tIDENTIFIER tOP_ASGN command_call",
    "stmt : backref tOP_ASGN command_call",
    "stmt : lhs '=' mrhs",
    "stmt : mlhs '=' arg_value",
    "stmt : mlhs '=' mrhs",
    "stmt : expr",
    "expr : command_call",
    "expr : expr kAND expr",
    "expr : expr kOR expr",
    "expr : kNOT expr",
    "expr : tBANG command_call",
    "expr : arg",
    "expr_value : expr",
    "command_call : command",
    "command_call : block_command",
    "command_call : kRETURN call_args",
    "command_call : kBREAK call_args",
    "command_call : kNEXT call_args",
    "block_command : block_call",
    "block_command : block_call tDOT operation2 command_args",
    "block_command : block_call tCOLON2 operation2 command_args",
    "$$4 :",
    "cmd_brace_block : tLBRACE_ARG $$4 opt_block_var compstmt tRCURLY",
    "command : operation command_args",
    "command : operation command_args cmd_brace_block",
    "command : primary_value tDOT operation2 command_args",
    "command : primary_value tDOT operation2 command_args cmd_brace_block",
    "command : primary_value tCOLON2 operation2 command_args",
    "command : primary_value tCOLON2 operation2 command_args cmd_brace_block",
    "command : kSUPER command_args",
    "command : kYIELD command_args",
    "mlhs : mlhs_basic",
    "mlhs : tLPAREN mlhs_entry tRPAREN",
    "mlhs_entry : mlhs_basic",
    "mlhs_entry : tLPAREN mlhs_entry tRPAREN",
    "mlhs_basic : mlhs_head",
    "mlhs_basic : mlhs_head mlhs_item",
    "mlhs_basic : mlhs_head tSTAR mlhs_node",
    "mlhs_basic : mlhs_head tSTAR",
    "mlhs_basic : tSTAR mlhs_node",
    "mlhs_basic : tSTAR",
    "mlhs_item : mlhs_node",
    "mlhs_item : tLPAREN mlhs_entry tRPAREN",
    "mlhs_head : mlhs_item ','",
    "mlhs_head : mlhs_head mlhs_item ','",
    "mlhs_node : variable",
    "mlhs_node : primary_value '[' aref_args tRBRACK",
    "mlhs_node : primary_value tDOT tIDENTIFIER",
    "mlhs_node : primary_value tCOLON2 tIDENTIFIER",
    "mlhs_node : primary_value tDOT tCONSTANT",
    "mlhs_node : primary_value tCOLON2 tCONSTANT",
    "mlhs_node : tCOLON3 tCONSTANT",
    "mlhs_node : backref",
    "lhs : variable",
    "lhs : primary_value '[' aref_args tRBRACK",
    "lhs : primary_value tDOT tIDENTIFIER",
    "lhs : primary_value tCOLON2 tIDENTIFIER",
    "lhs : primary_value tDOT tCONSTANT",
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
    "$$5 :",
    "undef_list : undef_list ',' $$5 fitem",
    "op : tPIPE",
    "op : tCARET",
    "op : tAMPER2",
    "op : tCMP",
    "op : tEQ",
    "op : tEQQ",
    "op : tMATCH",
    "op : tGT",
    "op : tGEQ",
    "op : tLT",
    "op : tLEQ",
    "op : tLSHFT",
    "op : tRSHFT",
    "op : tPLUS",
    "op : tMINUS",
    "op : tSTAR2",
    "op : tSTAR",
    "op : tDIVIDE",
    "op : tPERCENT",
    "op : tPOW",
    "op : tTILDE",
    "op : tUPLUS",
    "op : tUMINUS",
    "op : tAREF",
    "op : tASET",
    "op : tBACK_REF2",
    "reswords : k__LINE__",
    "reswords : k__FILE__",
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
    "arg : lhs '=' arg",
    "arg : lhs '=' arg kRESCUE_MOD arg",
    "arg : var_lhs tOP_ASGN arg",
    "arg : primary_value '[' aref_args tRBRACK tOP_ASGN arg",
    "arg : primary_value tDOT tIDENTIFIER tOP_ASGN arg",
    "arg : primary_value tDOT tCONSTANT tOP_ASGN arg",
    "arg : primary_value tCOLON2 tIDENTIFIER tOP_ASGN arg",
    "arg : primary_value tCOLON2 tCONSTANT tOP_ASGN arg",
    "arg : tCOLON3 tCONSTANT tOP_ASGN arg",
    "arg : backref tOP_ASGN arg",
    "arg : arg tDOT2 arg",
    "arg : arg tDOT3 arg",
    "arg : arg tPLUS arg",
    "arg : arg tMINUS arg",
    "arg : arg tSTAR2 arg",
    "arg : arg tDIVIDE arg",
    "arg : arg tPERCENT arg",
    "arg : arg tPOW arg",
    "arg : tUMINUS_NUM tINTEGER tPOW arg",
    "arg : tUMINUS_NUM tFLOAT tPOW arg",
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
    "arg : arg '?' arg ':' arg",
    "arg : primary",
    "arg_value : arg",
    "aref_args : none",
    "aref_args : command opt_nl",
    "aref_args : args trailer",
    "aref_args : args ',' tSTAR arg opt_nl",
    "aref_args : assocs trailer",
    "aref_args : tSTAR arg opt_nl",
    "paren_args : tLPAREN2 none tRPAREN",
    "paren_args : tLPAREN2 call_args opt_nl tRPAREN",
    "paren_args : tLPAREN2 block_call opt_nl tRPAREN",
    "paren_args : tLPAREN2 args ',' block_call opt_nl tRPAREN",
    "opt_paren_args : none",
    "opt_paren_args : paren_args",
    "call_args : command",
    "call_args : args opt_block_arg",
    "call_args : args ',' tSTAR arg_value opt_block_arg",
    "call_args : assocs opt_block_arg",
    "call_args : assocs ',' tSTAR arg_value opt_block_arg",
    "call_args : args ',' assocs opt_block_arg",
    "call_args : args ',' assocs ',' tSTAR arg opt_block_arg",
    "call_args : tSTAR arg_value opt_block_arg",
    "call_args : block_arg",
    "call_args2 : arg_value ',' args opt_block_arg",
    "call_args2 : arg_value ',' block_arg",
    "call_args2 : arg_value ',' tSTAR arg_value opt_block_arg",
    "call_args2 : arg_value ',' args ',' tSTAR arg_value opt_block_arg",
    "call_args2 : assocs opt_block_arg",
    "call_args2 : assocs ',' tSTAR arg_value opt_block_arg",
    "call_args2 : arg_value ',' assocs opt_block_arg",
    "call_args2 : arg_value ',' args ',' assocs opt_block_arg",
    "call_args2 : arg_value ',' assocs ',' tSTAR arg_value opt_block_arg",
    "call_args2 : arg_value ',' args ',' assocs ',' tSTAR arg_value opt_block_arg",
    "call_args2 : tSTAR arg_value opt_block_arg",
    "call_args2 : block_arg",
    "$$6 :",
    "command_args : $$6 open_args",
    "open_args : call_args",
    "$$7 :",
    "open_args : tLPAREN_ARG $$7 tRPAREN",
    "$$8 :",
    "open_args : tLPAREN_ARG call_args2 $$8 tRPAREN",
    "block_arg : tAMPER arg_value",
    "opt_block_arg : ',' block_arg",
    "opt_block_arg : none_block_pass",
    "args : arg_value",
    "args : args ',' arg_value",
    "mrhs : args ',' arg_value",
    "mrhs : args ',' tSTAR arg_value",
    "mrhs : tSTAR arg_value",
    "primary : literal",
    "primary : strings",
    "primary : xstring",
    "primary : regexp",
    "primary : words",
    "primary : qwords",
    "primary : var_ref",
    "primary : backref",
    "primary : tFID",
    "primary : kBEGIN bodystmt kEND",
    "$$9 :",
    "primary : tLPAREN_ARG expr $$9 opt_nl tRPAREN",
    "primary : tLPAREN compstmt tRPAREN",
    "primary : primary_value tCOLON2 tCONSTANT",
    "primary : tCOLON3 tCONSTANT",
    "primary : primary_value '[' aref_args tRBRACK",
    "primary : tLBRACK aref_args tRBRACK",
    "primary : tLBRACE assoc_list tRCURLY",
    "primary : kRETURN",
    "primary : kYIELD tLPAREN2 call_args tRPAREN",
    "primary : kYIELD tLPAREN2 tRPAREN",
    "primary : kYIELD",
    "primary : kDEFINED opt_nl tLPAREN2 expr tRPAREN",
    "primary : operation brace_block",
    "primary : method_call",
    "primary : method_call brace_block",
    "primary : kIF expr_value then compstmt if_tail kEND",
    "primary : kUNLESS expr_value then compstmt opt_else kEND",
    "$$10 :",
    "$$11 :",
    "primary : kWHILE $$10 expr_value do $$11 compstmt kEND",
    "$$12 :",
    "$$13 :",
    "primary : kUNTIL $$12 expr_value do $$13 compstmt kEND",
    "primary : kCASE expr_value opt_terms case_body kEND",
    "primary : kCASE opt_terms case_body kEND",
    "primary : kCASE opt_terms kELSE compstmt kEND",
    "$$14 :",
    "$$15 :",
    "primary : kFOR block_var kIN $$14 expr_value do $$15 compstmt kEND",
    "$$16 :",
    "primary : kCLASS cpath superclass $$16 bodystmt kEND",
    "$$17 :",
    "$$18 :",
    "primary : kCLASS tLSHFT expr $$17 term $$18 bodystmt kEND",
    "$$19 :",
    "primary : kMODULE cpath $$19 bodystmt kEND",
    "$$20 :",
    "primary : kDEF fname $$20 f_arglist bodystmt kEND",
    "$$21 :",
    "$$22 :",
    "primary : kDEF singleton dot_or_colon $$21 fname $$22 f_arglist bodystmt kEND",
    "primary : kBREAK",
    "primary : kNEXT",
    "primary : kREDO",
    "primary : kRETRY",
    "primary_value : primary",
    "then : term",
    "then : ':'",
    "then : kTHEN",
    "then : term kTHEN",
    "do : term",
    "do : ':'",
    "do : kDO_COND",
    "if_tail : opt_else",
    "if_tail : kELSIF expr_value then compstmt if_tail",
    "opt_else : none",
    "opt_else : kELSE compstmt",
    "for_var : lhs",
    "for_var : mlhs",
    "block_par : mlhs_item",
    "block_par : block_par ',' mlhs_item",
    "block_var : block_par",
    "block_var : block_par ','",
    "block_var : block_par ',' tAMPER lhs",
    "block_var : block_par ',' tSTAR lhs ',' tAMPER lhs",
    "block_var : block_par ',' tSTAR ',' tAMPER lhs",
    "block_var : block_par ',' tSTAR lhs",
    "block_var : block_par ',' tSTAR",
    "block_var : tSTAR lhs ',' tAMPER lhs",
    "block_var : tSTAR ',' tAMPER lhs",
    "block_var : tSTAR lhs",
    "block_var : tSTAR",
    "block_var : tAMPER lhs",
    "opt_block_var : none",
    "opt_block_var : tPIPE tPIPE",
    "opt_block_var : tOROP",
    "opt_block_var : tPIPE block_var tPIPE",
    "$$23 :",
    "do_block : kDO_BLOCK $$23 opt_block_var compstmt kEND",
    "block_call : command do_block",
    "block_call : block_call tDOT operation2 opt_paren_args",
    "block_call : block_call tCOLON2 operation2 opt_paren_args",
    "method_call : operation paren_args",
    "method_call : primary_value tDOT operation2 opt_paren_args",
    "method_call : primary_value tCOLON2 operation2 paren_args",
    "method_call : primary_value tCOLON2 operation3",
    "method_call : kSUPER paren_args",
    "method_call : kSUPER",
    "$$24 :",
    "brace_block : tLCURLY $$24 opt_block_var compstmt tRCURLY",
    "$$25 :",
    "brace_block : kDO $$25 opt_block_var compstmt kEND",
    "case_body : kWHEN when_args then compstmt cases",
    "when_args : args",
    "when_args : args ',' tSTAR arg_value",
    "when_args : tSTAR arg_value",
    "cases : opt_else",
    "cases : case_body",
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
    "string : string1",
    "string : string string1",
    "string1 : tSTRING_BEG string_contents tSTRING_END",
    "xstring : tXSTRING_BEG xstring_contents tSTRING_END",
    "regexp : tREGEXP_BEG xstring_contents tREGEXP_END",
    "words : tWORDS_BEG ' ' tSTRING_END",
    "words : tWORDS_BEG word_list tSTRING_END",
    "word_list :",
    "word_list : word_list word ' '",
    "word : string_content",
    "word : word string_content",
    "qwords : tQWORDS_BEG ' ' tSTRING_END",
    "qwords : tQWORDS_BEG qword_list tSTRING_END",
    "qword_list :",
    "qword_list : qword_list tSTRING_CONTENT ' '",
    "string_contents :",
    "string_contents : string_contents string_content",
    "xstring_contents :",
    "xstring_contents : xstring_contents string_content",
    "string_content : tSTRING_CONTENT",
    "$$26 :",
    "string_content : tSTRING_DVAR $$26 string_dvar",
    "$$27 :",
    "string_content : tSTRING_DBEG $$27 compstmt tRCURLY",
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
    "numeric : tINTEGER",
    "numeric : tFLOAT",
    "numeric : tUMINUS_NUM tINTEGER",
    "numeric : tUMINUS_NUM tFLOAT",
    "variable : tIDENTIFIER",
    "variable : tIVAR",
    "variable : tGVAR",
    "variable : tCONSTANT",
    "variable : tCVAR",
    "variable : kNIL",
    "variable : kSELF",
    "variable : kTRUE",
    "variable : kFALSE",
    "variable : k__FILE__",
    "variable : k__LINE__",
    "var_ref : variable",
    "var_lhs : variable",
    "backref : tNTH_REF",
    "backref : tBACK_REF",
    "superclass : term",
    "$$28 :",
    "superclass : tLT $$28 expr_value term",
    "superclass : error term",
    "f_arglist : tLPAREN2 f_args opt_nl tRPAREN",
    "f_arglist : f_args term",
    "f_args : f_arg ',' f_optarg ',' f_rest_arg opt_f_block_arg",
    "f_args : f_arg ',' f_optarg opt_f_block_arg",
    "f_args : f_arg ',' f_rest_arg opt_f_block_arg",
    "f_args : f_arg opt_f_block_arg",
    "f_args : f_optarg ',' f_rest_arg opt_f_block_arg",
    "f_args : f_optarg opt_f_block_arg",
    "f_args : f_rest_arg opt_f_block_arg",
    "f_args : f_block_arg",
    "f_args :",
    "f_norm_arg : tCONSTANT",
    "f_norm_arg : tIVAR",
    "f_norm_arg : tGVAR",
    "f_norm_arg : tCVAR",
    "f_norm_arg : tIDENTIFIER",
    "f_arg : f_norm_arg",
    "f_arg : f_arg ',' f_norm_arg",
    "f_opt : tIDENTIFIER '=' arg_value",
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
    "$$29 :",
    "singleton : tLPAREN2 $$29 expr opt_nl tRPAREN",
    "assoc_list : none",
    "assoc_list : assocs trailer",
    "assoc_list : args trailer",
    "assocs : assoc",
    "assocs : assocs ',' assoc",
    "assoc : arg_value tASSOC arg_value",
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
    "opt_terms :",
    "opt_terms : terms",
    "opt_nl :",
    "opt_nl : '\\n'",
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

  /** debugging support, requires the package <tt>jay.yydebug</tt>.
      Set to <tt>null</tt> to suppress debugging messages.
    */
  protected jay.yydebug.yyDebug yydebug;

  /** index-checked interface to {@link #yyNames}.
      @param token single character or <tt>%token</tt> value.
      @return token name or <tt>[illegal]</tt> or <tt>[unknown]</tt>.
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
      @param ayydebug debug message writer implementing <tt>yyDebug</tt>, or <tt>null</tt>.
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
      Used as <tt>$$ = yyDefault($1)</tt>, prior to the user-specified action, if any.
      Can be overwritten to provide deep copy, etc.
      @param first value for <tt>$1</tt>, or <tt>null</tt>.
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

static ParserState[] states = new ParserState[510];
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
                  Node node = ((Node)yyVals[-3+yyTop]);

		  if (((RescueBodyNode)yyVals[-2+yyTop]) != null) {
		      node = new RescueNode(support.getPosition(((Node)yyVals[-3+yyTop]), true), ((Node)yyVals[-3+yyTop]), ((RescueBodyNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
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
states[4] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((Node)yyVals[-1+yyTop]) instanceof BlockNode) {
                      support.checkUselessStatements(((BlockNode)yyVals[-1+yyTop]));
		  }
                  yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[6] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop]), true));
    return yyVal;
  }
};
states[7] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
	          yyVal = support.appendToBlock(((Node)yyVals[-2+yyTop]), support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop]), true)));
    return yyVal;
  }
};
states[8] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[9] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[10] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newAlias(support.union(((Token)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[11] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new VAliasNode(support.getPosition(((Token)yyVals[-2+yyTop])), (String) ((Token)yyVals[-1+yyTop]).getValue(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[12] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new VAliasNode(support.getPosition(((Token)yyVals[-2+yyTop])), (String) ((Token)yyVals[-1+yyTop]).getValue(), "$" + ((BackRefNode)yyVals[0+yyTop]).getType()); /* XXX*/
    return yyVal;
  }
};
states[13] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.yyerror("can't make alias for the number variables");
    return yyVal;
  }
};
states[14] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[15] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null);
    return yyVal;
  }
};
states[16] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), null, ((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[17] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                      yyVal = new WhileNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((BeginNode)yyVals[-2+yyTop]).getBodyNode(), false);
                  } else {
                      yyVal = new WhileNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), true);
                  }
    return yyVal;
  }
};
states[18] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                      yyVal = new UntilNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((BeginNode)yyVals[-2+yyTop]).getBodyNode(), false);
                  } else {
                      yyVal = new UntilNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), true);
                  }
    return yyVal;
  }
};
states[19] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  Node body = ((Node)yyVals[0+yyTop]);
	          yyVal = new RescueNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(support.getPosition(((Node)yyVals[-2+yyTop])), null, body, null), null);
    return yyVal;
  }
};
states[20] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (support.isInDef() || support.isInSingle()) {
                      support.yyerror("BEGIN in method");
                  }
		  support.pushLocalScope();
    return yyVal;
  }
};
states[21] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.getResult().addBeginNode(new PreExeNode(support.getPosition(((Node)yyVals[-1+yyTop])), support.getCurrentScope(), ((Node)yyVals[-1+yyTop])));
                  support.popCurrentScope();
                  yyVal = null; /*XXX 0;*/
    return yyVal;
  }
};
states[22] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (support.isInDef() || support.isInSingle()) {
                      support.warn(ID.END_IN_METHOD, support.getPosition(((Token)yyVals[-3+yyTop])), "END in method; use at_exit");
                  }
                  yyVal = new PostExeNode(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[23] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[24] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
		  if (((MultipleAsgnNode)yyVals[-2+yyTop]).getPre() != null) {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(new ToAryNode(support.getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  } else {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(support.newArrayNode(support.getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  }
		  yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[25] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
 	          support.checkExpression(((Node)yyVals[0+yyTop]));

		  String asgnOp = (String) ((Token)yyVals[-1+yyTop]).getValue();
		  if (asgnOp.equals("||")) {
	              ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
	              yyVal = new OpAsgnOrNode(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
		  } else if (asgnOp.equals("&&")) {
	              ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                      yyVal = new OpAsgnAndNode(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
		  } else {
                      ((AssignableNode)yyVals[-2+yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(((AssignableNode)yyVals[-2+yyTop])), asgnOp, ((Node)yyVals[0+yyTop])));
                      ((AssignableNode)yyVals[-2+yyTop]).setPosition(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
		      yyVal = ((AssignableNode)yyVals[-2+yyTop]);
		  }
    return yyVal;
  }
};
states[26] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.checkExpression(((Node)yyVals[0+yyTop]));

                  yyVal = support.new_opElementAsgnNode(support.union(((Node)yyVals[-5+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), (String) ((Token)yyVals[-1+yyTop]).getValue(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));

    return yyVal;
  }
};
states[27] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.checkExpression(((Node)yyVals[0+yyTop]));

                  yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[28] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.checkExpression(((Node)yyVals[0+yyTop]));

                  yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[29] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.checkExpression(((Node)yyVals[0+yyTop]));

                  yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[30] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.backrefAssignError(((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[31] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), new SValueNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[32] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((MultipleAsgnNode)yyVals[-2+yyTop]).getPre() != null) {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(new ToAryNode(support.getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  } else {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(support.newArrayNode(support.getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  }
		  yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[33] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
		  yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
                  ((MultipleAsgnNode)yyVals[-2+yyTop]).setPosition(support.union(((MultipleAsgnNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[36] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newAndNode(support.getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[37] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newOrNode(support.getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[38] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new NotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[39] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new NotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[41] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[44] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new ReturnNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), support.getPosition(((Token)yyVals[-1+yyTop]))));
    return yyVal;
  }
};
states[45] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new BreakNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), support.getPosition(((Token)yyVals[-1+yyTop]))));
    return yyVal;
  }
};
states[46] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new NextNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), support.getPosition(((Token)yyVals[-1+yyTop]))));
    return yyVal;
  }
};
states[48] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[49] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[50] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[51] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_iter(support.getPosition(((Token)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                    support.popCurrentScope();
    return yyVal;
  }
};
states[52] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_fcall(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[53] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_fcall(((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
    return yyVal;
  }
};
states[54] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[55] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
    return yyVal;
  }
};
states[56] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[57] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
    return yyVal;
  }
};
states[58] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop])); /* .setPosFrom($2);*/
    return yyVal;
  }
};
states[59] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_yield(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[61] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((MultipleAsgnNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[63] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.getPosition(((Token)yyVals[-2+yyTop])), support.newArrayNode(support.getPosition(((Token)yyVals[-2+yyTop])), ((MultipleAsgnNode)yyVals[-1+yyTop])), null);
    return yyVal;
  }
};
states[64] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.getPosition(((ListNode)yyVals[0+yyTop])), ((ListNode)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[65] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
/*mirko: check*/
                  yyVal = new MultipleAsgnNode(support.union(((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]).add(((Node)yyVals[0+yyTop])), null);
                  ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[66] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.getPosition(((ListNode)yyVals[-2+yyTop])), ((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[67] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.getPosition(((ListNode)yyVals[-1+yyTop])), ((ListNode)yyVals[-1+yyTop]), new StarNode(support.getPosition(null)));
    return yyVal;
  }
};
states[68] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.getPosition(((Token)yyVals[-1+yyTop])), null, ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[69] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.getPosition(((Token)yyVals[0+yyTop])), null, new StarNode(support.getPosition(null)));
    return yyVal;
  }
};
states[71] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((MultipleAsgnNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[72] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newArrayNode(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[73] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[74] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[75] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[76] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[77] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[78] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[79] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (support.isInDef() || support.isInSingle()) {
		      support.yyerror("dynamic constant assignment");
		  }

		  SourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, support.new_colon2(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[80] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (support.isInDef() || support.isInSingle()) {
		      support.yyerror("dynamic constant assignment");
		  }

                  SourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, support.new_colon3(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[81] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
	          support.backrefAssignError(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[82] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[83] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[84] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[85] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[86] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[87] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (support.isInDef() || support.isInSingle()) {
		      support.yyerror("dynamic constant assignment");
		  }
			
		  SourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, support.new_colon2(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[88] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (support.isInDef() || support.isInSingle()) {
		      support.yyerror("dynamic constant assignment");
		  }

                  SourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, support.new_colon3(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
    return yyVal;
  }
};
states[89] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   support.backrefAssignError(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[90] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.yyerror("class/module name must be CONSTANT");
    return yyVal;
  }
};
states[92] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_colon3(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[93] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_colon2(((Token)yyVals[0+yyTop]).getPosition(), null, (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[94] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_colon2(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[98] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.setState(LexState.EXPR_END);
                  yyVal = ((Token)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[99] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.setState(LexState.EXPR_END);
                  yyVal = yyVals[0+yyTop];
    return yyVal;
  }
};
states[100] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new LiteralNode(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[101] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new LiteralNode(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[102] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((LiteralNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[103] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[104] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newUndef(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[105] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[106] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.appendToBlock(((Node)yyVals[-3+yyTop]), support.newUndef(support.getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[174] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
		  /* FIXME: Consider fixing node_assign itself rather than single case*/
		  ((Node)yyVal).setPosition(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[175] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
                  Node body = ((Node)yyVals[0+yyTop]);
                  yyVal = support.node_assign(((Node)yyVals[-4+yyTop]), new RescueNode(position, ((Node)yyVals[-2+yyTop]), new RescueBodyNode(position, null, body, null), null));
    return yyVal;
  }
};
states[176] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  support.checkExpression(((Node)yyVals[0+yyTop]));
		  String asgnOp = (String) ((Token)yyVals[-1+yyTop]).getValue();

		  if (asgnOp.equals("||")) {
	              ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
	              yyVal = new OpAsgnOrNode(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
		  } else if (asgnOp.equals("&&")) {
	              ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                      yyVal = new OpAsgnAndNode(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
		  } else {
		      ((AssignableNode)yyVals[-2+yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(((AssignableNode)yyVals[-2+yyTop])), asgnOp, ((Node)yyVals[0+yyTop])));
                      ((AssignableNode)yyVals[-2+yyTop]).setPosition(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
		      yyVal = ((AssignableNode)yyVals[-2+yyTop]);
		  }
    return yyVal;
  }
};
states[177] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  support.checkExpression(((Node)yyVals[0+yyTop]));

                  yyVal = support.new_opElementAsgnNode(support.union(((Node)yyVals[-5+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), (String) ((Token)yyVals[-1+yyTop]).getValue(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[178] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  support.checkExpression(((Node)yyVals[0+yyTop]));

                  yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[179] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  support.checkExpression(((Node)yyVals[0+yyTop]));

                  yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[180] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  support.checkExpression(((Node)yyVals[0+yyTop]));

                  yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
    return yyVal;
  }
};
states[181] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
	          support.yyerror("constant re-assignment");
    return yyVal;
  }
};
states[182] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  support.yyerror("constant re-assignment");
    return yyVal;
  }
};
states[183] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.backrefAssignError(((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[184] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  support.checkExpression(((Node)yyVals[-2+yyTop]));
		  support.checkExpression(((Node)yyVals[0+yyTop]));
    
                  boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                  yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[185] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  support.checkExpression(((Node)yyVals[-2+yyTop]));
		  support.checkExpression(((Node)yyVals[0+yyTop]));
                  boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                  yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[186] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "+", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[187] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "-", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[188] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "*", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[189] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "/", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[190] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "%", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[191] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[192] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getUnaryCallNode(support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), support.getPosition(null)), ((Token)yyVals[-3+yyTop]));
    return yyVal;
  }
};
states[193] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getUnaryCallNode(support.getOperatorCallNode(((FloatNode)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), support.getPosition(null)), ((Token)yyVals[-3+yyTop]));
    return yyVal;
  }
};
states[194] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (support.isLiteral(((Node)yyVals[0+yyTop]))) {
		      yyVal = ((Node)yyVals[0+yyTop]);
		  } else {
                      yyVal = support.getUnaryCallNode(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
		  }
    return yyVal;
  }
};
states[195] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getUnaryCallNode(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[196] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "|", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[197] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "^", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[198] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "&", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[199] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=>", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[200] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[201] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">=", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[202] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[203] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[204] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "==", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[205] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "===", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[206] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "!=", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[207] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getMatchNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[208] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new NotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getMatchNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[209] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new NotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[210] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]), "~");
    return yyVal;
  }
};
states[211] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<<", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[212] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">>", ((Node)yyVals[0+yyTop]), support.getPosition(null));
    return yyVal;
  }
};
states[213] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newAndNode(support.getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[214] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newOrNode(support.getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[215] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new DefinedNode(support.getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[216] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new IfNode(support.getPosition(((Node)yyVals[-4+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[217] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[218] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
	          support.checkExpression(((Node)yyVals[0+yyTop]));
	          yyVal = ((Node)yyVals[0+yyTop]);   
    return yyVal;
  }
};
states[220] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newArrayNode(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[221] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[222] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_concat(support.getPosition(((ListNode)yyVals[-4+yyTop])), ((ListNode)yyVals[-4+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[223] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition pos = ((ListNode)yyVals[-1+yyTop]).getPosition();
                  yyVal = support.newArrayNode(pos, new HashNode(pos, ((ListNode)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[224] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition pos = ((Token)yyVals[-2+yyTop]).getPosition();
                  /*FIXME: Should be a union between 1,2*/
		  yyVal = new NewlineNode(pos, support.newSplatNode(pos, ((Node)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[225] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new ArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[226] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[-2+yyTop]);
		  ((Node)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[227] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newArrayNode(support.getPosition(((Token)yyVals[-3+yyTop])), ((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[228] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((ListNode)yyVals[-4+yyTop]).add(((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[231] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newArrayNode(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[232] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_blk_pass(((ListNode)yyVals[-1+yyTop]), ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[233] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_concat(support.getPosition(((ListNode)yyVals[-4+yyTop])), ((ListNode)yyVals[-4+yyTop]), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass(((Node)yyVal), ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[234] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition pos = ((ListNode)yyVals[-1+yyTop]).getPosition();
                  yyVal = support.newArrayNode(pos, new HashNode(pos, ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[235] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition pos = ((ListNode)yyVals[-4+yyTop]).getPosition();
                  yyVal = support.arg_concat(pos, support.newArrayNode(pos, new HashNode(pos, ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[236] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((ListNode)yyVals[-3+yyTop]).add(new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[237] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.checkExpression(((Node)yyVals[-1+yyTop]));
		  yyVal = support.arg_concat(((ListNode)yyVals[-6+yyTop]).getPosition(), ((ListNode)yyVals[-6+yyTop]).add(new HashNode(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[238] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_blk_pass(support.newSplatNode(support.getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[-1+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[239] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[240] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_blk_pass(support.newArrayNode(support.getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop])).addAll(((ListNode)yyVals[-1+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[241] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_blk_pass(support.newArrayNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[242] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_concat(support.getPosition(((Node)yyVals[-4+yyTop])), support.newArrayNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[243] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_concat(support.getPosition(((Node)yyVals[-6+yyTop])), support.newArrayNode(support.getPosition(((Node)yyVals[-6+yyTop])), ((Node)yyVals[-6+yyTop])).addAll(new HashNode(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[244] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition pos = ((ListNode)yyVals[-1+yyTop]).getPosition();
                  yyVal = support.newArrayNode(pos, new HashNode(pos, ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[245] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition pos = ((ListNode)yyVals[-4+yyTop]).getPosition();
                  yyVal = support.arg_concat(pos, support.newArrayNode(pos, new HashNode(pos, ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[246] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newArrayNode(((Node)yyVals[-3+yyTop]).getPosition(), ((Node)yyVals[-3+yyTop])).add(new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[247] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newArrayNode(((Node)yyVals[-5+yyTop]).getPosition(), ((Node)yyVals[-5+yyTop])).addAll(((ListNode)yyVals[-3+yyTop])).add(new HashNode(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[248] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_concat(support.getPosition(((Node)yyVals[-6+yyTop])), support.newArrayNode(support.getPosition(((Node)yyVals[-6+yyTop])), ((Node)yyVals[-6+yyTop])).add(new HashNode(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[249] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_concat(support.getPosition(((Node)yyVals[-8+yyTop])), support.newArrayNode(support.getPosition(((Node)yyVals[-8+yyTop])), ((Node)yyVals[-8+yyTop])).addAll(((ListNode)yyVals[-6+yyTop])).add(new HashNode(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[250] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_blk_pass(support.newSplatNode(support.getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[-1+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[251] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[252] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
	          yyVal = new Long(lexer.getCmdArgumentState().begin());
    return yyVal;
  }
};
states[253] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getCmdArgumentState().reset(((Long)yyVals[-1+yyTop]).longValue());
                  yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[255] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  lexer.setState(LexState.EXPR_ENDARG);
    return yyVal;
  }
};
states[256] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.warn(ID.ARGUMENT_EXTRA_SPACE, support.getPosition(((Token)yyVals[-2+yyTop])), "don't put space before argument parentheses");
	          yyVal = null;
    return yyVal;
  }
};
states[257] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  lexer.setState(LexState.EXPR_ENDARG);
    return yyVal;
  }
};
states[258] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.warn(ID.ARGUMENT_EXTRA_SPACE, support.getPosition(((Token)yyVals[-3+yyTop])), "don't put space before argument parentheses");
		  yyVal = ((Node)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[259] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
                  yyVal = new BlockPassNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[260] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((BlockPassNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[262] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newArrayNode(support.getPosition2(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[263] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[264] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[265] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_concat(support.getPosition(((ListNode)yyVals[-3+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[266] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newSplatNode(support.getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[275] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new FCallNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue(), null);
    return yyVal;
  }
};
states[276] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new BeginNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[277] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.setState(LexState.EXPR_ENDARG); 
    return yyVal;
  }
};
states[278] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  support.warning(ID.GROUPED_EXPRESSION, support.getPosition(((Token)yyVals[-4+yyTop])), "(...) interpreted as grouped expression");
                  yyVal = ((Node)yyVals[-3+yyTop]);
    return yyVal;
  }
};
states[279] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition pos = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                  Node implicitNil = ((Node)yyVals[-1+yyTop]) == null ? new ImplicitNilNode(pos) : ((Node)yyVals[-1+yyTop]);
                  /* compstmt position includes both parens around it*/
                  if (implicitNil != null) implicitNil.setPosition(pos);

                  yyVal = implicitNil;
    return yyVal;
  }
};
states[280] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_colon2(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[281] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_colon3(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[282] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((Node)yyVals[-3+yyTop]) instanceof SelfNode) {
                      yyVal = support.new_fcall(new Token("[]", support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]))), ((Node)yyVals[-1+yyTop]), null);
                  } else {
                      yyVal = support.new_aref(((Node)yyVals[-3+yyTop]), new Token("[]", support.union(((Node)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  }
    return yyVal;
  }
};
states[283] = new ParserState() {
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
states[284] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new HashNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[285] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  yyVal = new ReturnNode(((Token)yyVals[0+yyTop]).getPosition(), null);
    return yyVal;
  }
};
states[286] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_yield(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[287] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new YieldNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, false);
    return yyVal;
  }
};
states[288] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new YieldNode(((Token)yyVals[0+yyTop]).getPosition(), null, false);
    return yyVal;
  }
};
states[289] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new DefinedNode(support.getPosition(((Token)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[290] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new FCallNode(support.union(((Token)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])), (String) ((Token)yyVals[-1+yyTop]).getValue(), null, ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[292] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
	          if (((Node)yyVals[-1+yyTop]) != null && 
                      ((BlockAcceptingNode)yyVals[-1+yyTop]).getIter() instanceof BlockPassNode) {
                      throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, support.getPosition(((Node)yyVals[-1+yyTop])), "Both block arg and actual block given.");
		  }
		  ((BlockAcceptingNode)yyVals[-1+yyTop]).setIter(((IterNode)yyVals[0+yyTop]));
                  yyVal = ((Node)yyVals[-1+yyTop]);
		  ((Node)yyVal).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[293] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new IfNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[294] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new IfNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[295] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().begin();
    return yyVal;
  }
};
states[296] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  lexer.getConditionState().end();
    return yyVal;
  }
};
states[297] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  Node body = ((Node)yyVals[-1+yyTop]);
                  yyVal = new WhileNode(support.union(((Token)yyVals[-6+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), body);
    return yyVal;
  }
};
states[298] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().begin();
    return yyVal;
  }
};
states[299] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().end();
    return yyVal;
  }
};
states[300] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  Node body = ((Node)yyVals[-1+yyTop]);
                  yyVal = new UntilNode(support.getPosition(((Token)yyVals[-6+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), body);
    return yyVal;
  }
};
states[301] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newCaseNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[302] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
/* TODO: MRI is just a when node.  We need this extra logic for IDE consumers (null in casenode statement should be implicit nil)*/
/*                  if (support.getConfiguration().hasExtraPositionInformation()) {*/
                      yyVal = support.newCaseNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), null, ((Node)yyVals[-1+yyTop]));
/*                  } else {*/
/*                      $$ = $3;*/
/*                  }*/
    return yyVal;
  }
};
states[303] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[304] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().begin();
    return yyVal;
  }
};
states[305] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().end();
    return yyVal;
  }
};
states[306] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new ForNode(support.union(((Token)yyVals[-8+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-7+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-4+yyTop]), support.getCurrentScope());
    return yyVal;
  }
};
states[307] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (support.isInDef() || support.isInSingle()) {
                      support.yyerror("class definition in method body");
                  }
		  support.pushLocalScope();
    return yyVal;
  }
};
states[308] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  Node body = ((Node)yyVals[-1+yyTop]);

                  yyVal = new ClassNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-4+yyTop]), support.getCurrentScope(), body, ((Node)yyVals[-3+yyTop]));
                  support.popCurrentScope();
    return yyVal;
  }
};
states[309] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new Boolean(support.isInDef());
                  support.setInDef(false);
    return yyVal;
  }
};
states[310] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new Integer(support.getInSingle());
                  support.setInSingle(0);
		  support.pushLocalScope();
    return yyVal;
  }
};
states[311] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new SClassNode(support.union(((Token)yyVals[-7+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  support.popCurrentScope();
                  support.setInDef(((Boolean)yyVals[-4+yyTop]).booleanValue());
                  support.setInSingle(((Integer)yyVals[-2+yyTop]).intValue());
    return yyVal;
  }
};
states[312] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (support.isInDef() || support.isInSingle()) { 
                      support.yyerror("module definition in method body");
                  }
		  support.pushLocalScope();
    return yyVal;
  }
};
states[313] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   Node body = ((Node)yyVals[-1+yyTop]);

                  yyVal = new ModuleNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-3+yyTop]), support.getCurrentScope(), body);
                  support.popCurrentScope();
    return yyVal;
  }
};
states[314] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.setInDef(true);
		  support.pushLocalScope();
    return yyVal;
  }
};
states[315] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  Node body = ((Node)yyVals[-1+yyTop]);

                  /* NOEX_PRIVATE for toplevel */
                  yyVal = new DefnNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), new MethodNameNode(((Token)yyVals[-4+yyTop]).getPosition(), (String) ((Token)yyVals[-4+yyTop]).getValue()), ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                  support.popCurrentScope();
                  support.setInDef(false);
    return yyVal;
  }
};
states[316] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[317] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.setInSingle(support.getInSingle() + 1);
		  support.pushLocalScope();
                  lexer.setState(LexState.EXPR_END); /* force for args */
    return yyVal;
  }
};
states[318] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  Node body = ((Node)yyVals[-1+yyTop]);

                  yyVal = new DefsNode(support.union(((Token)yyVals[-8+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-7+yyTop]), new MethodNameNode(((Token)yyVals[-4+yyTop]).getPosition(), (String) ((Token)yyVals[-4+yyTop]).getValue()), ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                  support.popCurrentScope();
                  support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[319] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new BreakNode(((Token)yyVals[0+yyTop]).getPosition(), null);
    return yyVal;
  }
};
states[320] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new NextNode(((Token)yyVals[0+yyTop]).getPosition(), null);
    return yyVal;
  }
};
states[321] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new RedoNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[322] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new RetryNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[323] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
		  yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[332] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
/*mirko: support.union($<ISourcePositionHolder>1.getPosition(), support.getPosition($<ISourcePositionHolder>1)) ?*/
                  yyVal = new IfNode(support.getPosition(((Token)yyVals[-4+yyTop])), support.getConditionNode(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[334] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[336] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[337] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newArrayNode(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[338] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[339] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((ListNode)yyVals[0+yyTop]).size() == 1) {
                      yyVal = ((ListNode)yyVals[0+yyTop]).get(0);
                  } else {
                      yyVal = new MultipleAsgnNode(support.getPosition(((ListNode)yyVals[0+yyTop])), ((ListNode)yyVals[0+yyTop]), null);
                  }
    return yyVal;
  }
};
states[340] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.getPosition(((ListNode)yyVals[-1+yyTop])), ((ListNode)yyVals[-1+yyTop]), null);
    return yyVal;
  }
};
states[341] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition pos = support.union(((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));

                  yyVal = support.newBlockArg18(pos, ((Node)yyVals[0+yyTop]), new MultipleAsgnNode(pos, ((ListNode)yyVals[-3+yyTop]), null));
    return yyVal;
  }
};
states[342] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newBlockArg18(support.union(((ListNode)yyVals[-6+yyTop]), ((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]), new MultipleAsgnNode(support.union(((ListNode)yyVals[-6+yyTop]), ((Node)yyVals[-3+yyTop])), ((ListNode)yyVals[-6+yyTop]), ((Node)yyVals[-3+yyTop])));
    return yyVal;
  }
};
states[343] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newBlockArg18(support.union(((ListNode)yyVals[-5+yyTop]), ((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]), new MultipleAsgnNode(support.union(((ListNode)yyVals[-5+yyTop]), ((Token)yyVals[-3+yyTop])), ((ListNode)yyVals[-5+yyTop]), new StarNode(support.getPosition(((Token)yyVals[-3+yyTop])))));
    return yyVal;
  }
};
states[344] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.union(((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[345] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.union(((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((ListNode)yyVals[-2+yyTop]), new StarNode(support.getPosition(((Token)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[346] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newBlockArg18(support.union(((Token)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]), new MultipleAsgnNode(support.union(((Token)yyVals[-4+yyTop]), ((Node)yyVals[-3+yyTop])), null, ((Node)yyVals[-3+yyTop])));
    return yyVal;
  }
};
states[347] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newBlockArg18(support.union(((Token)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]), new MultipleAsgnNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop])), null, new StarNode(support.getPosition(((Token)yyVals[-1+yyTop])))));
    return yyVal;
  }
};
states[348] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), null, ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[349] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new MultipleAsgnNode(support.getPosition(((Token)yyVals[0+yyTop])), null, new StarNode(support.getPosition(((Token)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[350] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newBlockArg18(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[352] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new ZeroArgNode(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])));
                  lexer.commandStart = true;
    return yyVal;
  }
};
states[353] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new ZeroArgNode(((Token)yyVals[0+yyTop]).getPosition());
                  lexer.commandStart = true;
    return yyVal;
  }
};
states[354] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[-1+yyTop]);
                  lexer.commandStart = true;

		  /* Include pipes on multiple arg type*/
                  if (((Node)yyVals[-1+yyTop]) instanceof MultipleAsgnNode) {
		      ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
		  } 
    return yyVal;
  }
};
states[355] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.pushBlockScope();
    return yyVal;
  }
};
states[356] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_iter(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  support.popCurrentScope();
    return yyVal;
  }
};
states[357] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  /* Workaround for JRUBY-2326 (MRI does not enter this production for some reason)*/
                  if (((Node)yyVals[-1+yyTop]) instanceof YieldNode) {
                      throw new SyntaxException(PID.BLOCK_GIVEN_TO_YIELD, support.getPosition(((Node)yyVals[-1+yyTop])), "block given to yield");
                  }
	          if (((BlockAcceptingNode)yyVals[-1+yyTop]).getIter() instanceof BlockPassNode) {
                      throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, support.getPosition(((Node)yyVals[-1+yyTop])), "Both block arg and actual block given.");
                  }
		  ((BlockAcceptingNode)yyVals[-1+yyTop]).setIter(((IterNode)yyVals[0+yyTop]));
                  yyVal = ((Node)yyVals[-1+yyTop]);
		  ((Node)yyVal).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[358] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                  ((IArgumentNode)yyVal).setHasParens(true);
    return yyVal;
  }
};
states[359] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                  ((IArgumentNode)yyVal).setHasParens(true);
    return yyVal;
  }
};
states[360] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_fcall(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                  ((IArgumentNode)yyVal).setHasParens(true);
    return yyVal;
  }
};
states[361] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                  ((IArgumentNode)yyVal).setHasParens(true);
    return yyVal;
  }
};
states[362] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                  ((IArgumentNode)yyVal).setHasParens(true);
    return yyVal;
  }
};
states[363] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_call(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[364] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
                  ((IArgumentNode)yyVal).setHasParens(true);
    return yyVal;
  }
};
states[365] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new ZSuperNode(((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[366] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.pushBlockScope();
    return yyVal;
  }
};
states[367] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_iter(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  support.popCurrentScope();
    return yyVal;
  }
};
states[368] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  support.pushBlockScope();
    return yyVal;
  }
};
states[369] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.new_iter(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  ((ISourcePositionHolder)yyVals[-5+yyTop]).setPosition(support.union(((ISourcePositionHolder)yyVals[-5+yyTop]), ((ISourcePositionHolder)yyVal)));
                  support.popCurrentScope();
    return yyVal;
  }
};
states[370] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newWhenNode(support.union(((Token)yyVals[-4+yyTop]), support.unwrapNewlineNode(((Node)yyVals[-1+yyTop]))), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[372] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.arg_concat(support.union(((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[373] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new SplatNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[376] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  Node node;
                  if (((Node)yyVals[-3+yyTop]) != null) {
                     node = support.appendToBlock(support.node_assign(((Node)yyVals[-3+yyTop]), new GlobalVarNode(support.getPosition(((Token)yyVals[-5+yyTop])), "$!")), ((Node)yyVals[-1+yyTop]));
                     if(((Node)yyVals[-1+yyTop]) != null) {
                        node.setPosition(support.unwrapNewlineNode(((Node)yyVals[-1+yyTop])).getPosition());
                     }
		  } else {
		     node = ((Node)yyVals[-1+yyTop]);
                  }
                  Node body = node;
                  yyVal = new RescueBodyNode(support.getPosition(((Token)yyVals[-5+yyTop]), true), ((Node)yyVals[-4+yyTop]), body, ((RescueBodyNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[377] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = null;
    return yyVal;
  }
};
states[378] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[381] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[383] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((Node)yyVals[0+yyTop]) != null) {
                      yyVal = ((Node)yyVals[0+yyTop]);
                  } else {
                      yyVal = new NilNode(support.getPosition(null));
                  }
    return yyVal;
  }
};
states[386] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  /* FIXME: We may be intern'ing more than once.*/
                  yyVal = new SymbolNode(((Token)yyVals[0+yyTop]).getPosition(), ((String) ((Token)yyVals[0+yyTop]).getValue()).intern());
    return yyVal;
  }
};
states[388] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[0+yyTop]) instanceof EvStrNode ? new DStrNode(support.getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop])) : ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[390] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[391] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((Node)yyVals[-1+yyTop]);
                  ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
		  int extraLength = ((String) ((Token)yyVals[-2+yyTop]).getValue()).length() - 1;

                  /* We may need to subtract addition offset off of first */
		  /* string fragment (we optimistically take one off in*/
		  /* ParserSupport.literal_concat).  Check token length*/
		  /* and subtract as neeeded.*/
		  if ((((Node)yyVals[-1+yyTop]) instanceof DStrNode) && extraLength > 0) {
		     Node strNode = ((DStrNode)((Node)yyVals[-1+yyTop])).get(0);
		     assert strNode != null;
		     strNode.getPosition().adjustStartOffset(-extraLength);
		  }
    return yyVal;
  }
};
states[392] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition position = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

		  if (((Node)yyVals[-1+yyTop]) == null) {
		      yyVal = new XStrNode(position, null);
		  } else if (((Node)yyVals[-1+yyTop]) instanceof StrNode) {
                      yyVal = new XStrNode(position, ((StrNode)yyVals[-1+yyTop]).getValue());
		  } else if (((Node)yyVals[-1+yyTop]) instanceof DStrNode) {
                      yyVal = new DXStrNode(position, ((DStrNode)yyVals[-1+yyTop]));

                      ((Node)yyVal).setPosition(position);
                  } else {
                      yyVal = new DXStrNode(position).add(((Node)yyVals[-1+yyTop]));
		  }
    return yyVal;
  }
};
states[393] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newRegexpNode(support.union(((Token)yyVals[-2+yyTop]), ((RegexpNode)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), (RegexpNode) ((RegexpNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[394] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[395] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		   yyVal = ((ListNode)yyVals[-1+yyTop]);
                   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[396] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new ArrayNode(support.getPosition(null));
    return yyVal;
  }
};
states[397] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]) instanceof EvStrNode ? new DStrNode(support.getPosition(((ListNode)yyVals[-2+yyTop]))).add(((Node)yyVals[-1+yyTop])) : ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[399] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[400] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[401] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		   yyVal = ((ListNode)yyVals[-1+yyTop]);
                   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[402] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new ArrayNode(support.getPosition(null));
    return yyVal;
  }
};
states[403] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[404] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new StrNode(((Token)yyVals[0+yyTop]).getPosition(), "");
    return yyVal;
  }
};
states[405] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[406] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		   yyVal = null;
    return yyVal;
  }
};
states[407] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[408] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[409] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = lexer.getStrTerm();
		   lexer.setStrTerm(null);
		   lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[410] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		   lexer.setStrTerm(((StrTerm)yyVals[-1+yyTop]));
	           yyVal = new EvStrNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[411] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		   yyVal = lexer.getStrTerm();
		   lexer.setStrTerm(null);
		   lexer.setState(LexState.EXPR_BEG);
                   lexer.getConditionState().stop();
	           lexer.getCmdArgumentState().stop();
    return yyVal;
  }
};
states[412] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		   lexer.setStrTerm(((StrTerm)yyVals[-2+yyTop]));
                   lexer.getConditionState().restart();
	           lexer.getCmdArgumentState().restart();

		   yyVal = support.newEvStrNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[413] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new GlobalVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[414] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new InstVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[415] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new ClassVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
    return yyVal;
  }
};
states[417] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_END);
                   yyVal = ((Token)yyVals[0+yyTop]);
		   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[422] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_END);

		   /* DStrNode: :"some text #{some expression}"*/
                   /* StrNode: :"some text"*/
		   /* EvStrNode :"#{some expression}"*/
                   if (((Node)yyVals[-1+yyTop]) == null) {
                       support.yyerror("empty symbol literal");
                   }

		   if (((Node)yyVals[-1+yyTop]) instanceof DStrNode) {
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
states[424] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = ((FloatNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[425] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.negateInteger(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[426] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.negateFloat(((FloatNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[432] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new Token("nil", Tokens.kNIL, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[433] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new Token("self", Tokens.kSELF, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[434] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new Token("true", Tokens.kTRUE, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[435] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new Token("false", Tokens.kFALSE, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[436] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new Token("__FILE__", Tokens.k__FILE__, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[437] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new Token("__LINE__", Tokens.k__LINE__, ((Token)yyVals[0+yyTop]).getPosition());
    return yyVal;
  }
};
states[438] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.gettable(((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[439] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[442] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = null;
    return yyVal;
  }
};
states[443] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[444] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[445] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = null;
    return yyVal;
  }
};
states[446] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = ((Node)yyVals[-2+yyTop]);
                   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                   lexer.setState(LexState.EXPR_BEG);
                   lexer.commandStart = true;
    return yyVal;
  }
};
states[447] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[448] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.new_args(support.union(((ListNode)yyVals[-5+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[449] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.new_args(support.getPosition(((ListNode)yyVals[-3+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[450] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.new_args(support.union(((ListNode)yyVals[-3+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), null, ((RestArgNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[451] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.new_args(((ISourcePositionHolder)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[452] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.new_args(support.getPosition(((ListNode)yyVals[-3+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[453] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.new_args(support.getPosition(((ListNode)yyVals[-1+yyTop])), null, ((ListNode)yyVals[-1+yyTop]), null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[454] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.new_args(support.getPosition(((RestArgNode)yyVals[-1+yyTop])), null, null, ((RestArgNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[455] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.new_args(support.getPosition(((BlockArgNode)yyVals[0+yyTop])), null, null, null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[456] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = support.new_args(support.createEmptyArgsNodePosition(support.getPosition(null)), null, null, null, null, (BlockArgNode) null);
    return yyVal;
  }
};
states[457] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   support.yyerror("formal argument cannot be a constant");
    return yyVal;
  }
};
states[458] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   support.yyerror("formal argument cannot be a instance variable");
    return yyVal;
  }
};
states[459] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   support.yyerror("formal argument cannot be an global variable");
    return yyVal;
  }
};
states[460] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   support.yyerror("formal argument cannot be a class variable");
    return yyVal;
  }
};
states[461] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   String identifier = (String) ((Token)yyVals[0+yyTop]).getValue();
                   if (support.getCurrentScope().getLocalScope().isDefined(identifier) >= 0) {
                       support.yyerror("duplicate argument name");
                   }

		   int location = support.getCurrentScope().getLocalScope().addVariable(identifier);
                   yyVal = new ArgumentNode(((Token)yyVals[0+yyTop]).getPosition(), identifier, location);
    return yyVal;
  }
};
states[462] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ListNode(((ISourcePositionHolder)yyVals[0+yyTop]).getPosition()).add(((ArgumentNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[463] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   ((ListNode)yyVals[-2+yyTop]).add(((ArgumentNode)yyVals[0+yyTop]));
                   ((ListNode)yyVals[-2+yyTop]).setPosition(support.union(((ListNode)yyVals[-2+yyTop]), ((ArgumentNode)yyVals[0+yyTop])));
		   yyVal = ((ListNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[464] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   String identifier = (String) ((Token)yyVals[-2+yyTop]).getValue();

                   if (support.getCurrentScope().getLocalScope().isDefined(identifier) >= 0) {
                       support.yyerror("duplicate optional argument name");
                   }
		   support.getCurrentScope().getLocalScope().addVariable(identifier);
                   yyVal = new OptArgNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.assignable(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[465] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new BlockNode(support.getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[466] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.appendToBlock(((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[469] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  String identifier = (String) ((Token)yyVals[0+yyTop]).getValue();

                  if (support.getCurrentScope().getLocalScope().isDefined(identifier) >= 0) {
                      support.yyerror("duplicate rest argument name");
                  }

                  yyVal = new RestArgNode(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue(), support.getCurrentScope().getLocalScope().addVariable(identifier));
    return yyVal;
  }
};
states[470] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new UnnamedRestArgNode(((Token)yyVals[0+yyTop]).getPosition(), support.getCurrentScope().getLocalScope().addVariable("*"));
    return yyVal;
  }
};
states[473] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = support.newBlockArg(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), ((Token)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[474] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((BlockArgNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[475] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
	          yyVal = null;
    return yyVal;
  }
};
states[476] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
		  yyVal = ((Node)yyVals[0+yyTop]);
                  support.checkExpression(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[477] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[478] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((Node)yyVals[-2+yyTop]) == null) {
                      support.yyerror("can't define single method for ().");
                  } else if (((Node)yyVals[-2+yyTop]) instanceof ILiteralNode) {
                      support.yyerror("can't define single method for literals.");
                  }
		  support.checkExpression(((Node)yyVals[-2+yyTop]));
                  yyVal = ((Node)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[479] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = new ArrayNode(support.getPosition(null));
    return yyVal;
  }
};
states[480] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[481] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((ListNode)yyVals[-1+yyTop]).size() % 2 != 0) {
                      support.yyerror("odd number list for Hash.");
                  }
                  yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[483] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = ((ListNode)yyVals[-2+yyTop]).addAll(((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[484] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  SourcePosition position;
                  if (((Node)yyVals[-2+yyTop]) == null && ((Node)yyVals[0+yyTop]) == null) {
                      position = support.getPosition(((Token)yyVals[-1+yyTop]));
                  } else {
                      position = support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                  }

                  yyVal = support.newArrayNode(position, ((Node)yyVals[-2+yyTop])).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[504] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[507] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[508] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = null;
    return yyVal;
  }
};
states[509] = new ParserState() {
  public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = null;
    return yyVal;
  }
};
}
					// line 1911 "Ruby18Parser.y"

    /** The parse method use an lexer stream and parse it to an AST node 
     * structure
     */
    public ParserResult parse(ParserConfiguration configuration, LexerSource source) throws IOException {
        support.reset();
        support.setConfiguration(configuration);
        support.setResult(new ParserResult());
        
        lexer.reset();
        lexer.setSource(source);
        lexer.setEncoding(configuration.getEncoding());

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

        yyparse(lexer, debugger);
        
        return support.getResult();
    }

    // +++
    // Helper Methods
}
					// line 7842 "-"
