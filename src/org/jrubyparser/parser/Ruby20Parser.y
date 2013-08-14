%{
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
 * Copyright (C) 2008-2009 Thomas E Enebo <enebo@acm.org>
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
import org.jrubyparser.ast.FixnumNode;
import org.jrubyparser.ast.FloatNode;
import org.jrubyparser.ast.ForNode;
import org.jrubyparser.ast.GlobalVarNode;
import org.jrubyparser.ast.HashNode;
import org.jrubyparser.ast.IfNode;
import org.jrubyparser.ast.ImplicitNilNode;
import org.jrubyparser.ast.InstVarNode;
import org.jrubyparser.ast.IterNode;
import org.jrubyparser.ast.LambdaNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.LiteralNode;
import org.jrubyparser.ast.MethodNameNode;
import org.jrubyparser.ast.ModuleNode;
import org.jrubyparser.ast.MultipleAsgnNode;
import org.jrubyparser.ast.NextNode;
import org.jrubyparser.ast.NilNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.OpAsgnAndNode;
import org.jrubyparser.ast.OpAsgnNode;
import org.jrubyparser.ast.OpAsgnOrNode;
import org.jrubyparser.ast.OptArgNode;
import org.jrubyparser.ast.PostExeNode;
import org.jrubyparser.ast.PreExe19Node;
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

public class Ruby20Parser implements RubyParser {
    protected ParserSupport19 support;
    protected Lexer lexer;

    public Ruby20Parser() {
        this(new ParserSupport19());
    }

    public Ruby20Parser(ParserSupport19 support) {
        this.support = support;
        lexer = new Lexer(false);
        lexer.setParserSupport(support);
        support.setLexer(lexer);
    }

    public void setWarnings(IRubyWarnings warnings) {
        support.setWarnings(warnings);
        lexer.setWarnings(warnings);
    }
%}

// We need to make sure we have same tokens in the same order and up
// front so 1.8 and 1.9 parser can use the same Tokens.java file.
%token <Token> kCLASS kMODULE kDEF kUNDEF kBEGIN kRESCUE kENSURE kEND kIF
  kUNLESS kTHEN kELSIF kELSE kCASE kWHEN kWHILE kUNTIL kFOR kBREAK kNEXT
  kREDO kRETRY kIN kDO kDO_COND kDO_BLOCK kRETURN kYIELD kSUPER kSELF kNIL
  kTRUE kFALSE kAND kOR kNOT kIF_MOD kUNLESS_MOD kWHILE_MOD kUNTIL_MOD
  kRESCUE_MOD kALIAS kDEFINED klBEGIN klEND k__LINE__ k__FILE__
  k__ENCODING__ kDO_LAMBDA 

%token <Token> tIDENTIFIER tFID tGVAR tIVAR tCONSTANT tCVAR tLABEL tCHAR
%type <Token> sym symbol operation operation2 operation3 cname fname op 
%type <Token> f_norm_arg dot_or_colon restarg_mark blkarg_mark
%token <Token> tUPLUS         /* unary+ */
%token <Token> tUMINUS        /* unary- */
%token <Token> tUMINUS_NUM    /* unary- */
%token <Token> tPOW           /* ** */
%token <Token> tCMP           /* <=> */
%token <Token> tEQ            /* == */
%token <Token> tEQQ           /* === */
%token <Token> tNEQ           /* != */
%token <Token> tGEQ           /* >= */
%token <Token> tLEQ           /* <= */
%token <Token> tANDOP tOROP   /* && and || */
%token <Token> tMATCH tNMATCH /* =~ and !~ */
%token <Token>  tDOT           /* Is just '.' in ruby and not a token */
%token <Token> tDOT2 tDOT3    /* .. and ... */
%token <Token> tAREF tASET    /* [] and []= */
%token <Token> tLSHFT tRSHFT  /* << and >> */
%token <Token> tCOLON2        /* :: */
%token <Token> tCOLON3        /* :: at EXPR_BEG */
%token <Token> tOP_ASGN       /* +=, -=  etc. */
%token <Token> tASSOC         /* => */
%token <Token> tLPAREN        /* ( */
%token <Token> tLPAREN2        /* ( Is just '(' in ruby and not a token */
%token <Token> tRPAREN        /* ) */
%token <Token> tLPAREN_ARG    /* ( */
%token <Token> tLBRACK        /* [ */
%token <Token> tRBRACK        /* ] */
%token <Token> tLBRACE        /* { */
%token <Token> tLBRACE_ARG    /* { */
%token <Token> tSTAR          /* * */
%token <Token> tSTAR2         /* *  Is just '*' in ruby and not a token */
%token <Token> tAMPER         /* & */
%token <Token> tAMPER2        /* &  Is just '&' in ruby and not a token */
%token <Token> tTILDE         /* ` is just '`' in ruby and not a token */
%token <Token> tPERCENT       /* % is just '%' in ruby and not a token */
%token <Token> tDIVIDE        /* / is just '/' in ruby and not a token */
%token <Token> tPLUS          /* + is just '+' in ruby and not a token */
%token <Token> tMINUS         /* - is just '-' in ruby and not a token */
%token <Token> tLT            /* < is just '<' in ruby and not a token */
%token <Token> tGT            /* > is just '>' in ruby and not a token */
%token <Token> tPIPE          /* | is just '|' in ruby and not a token */
%token <Token> tBANG          /* ! is just '!' in ruby and not a token */
%token <Token> tCARET         /* ^ is just '^' in ruby and not a token */
%token <Token> tLCURLY        /* { is just '{' in ruby and not a token */
%token <Token> tRCURLY        /* } is just '}' in ruby and not a token */
%token <Token> tBACK_REF2     /* { is just '`' in ruby and not a token */
%token <Token> tSYMBEG tSTRING_BEG tXSTRING_BEG tREGEXP_BEG tWORDS_BEG tQWORDS_BEG
%token <Token> tSTRING_DBEG tSTRING_DVAR tSTRING_END
%token <Token> tLAMBDA tLAMBEG
%token <Node> tNTH_REF tBACK_REF tSTRING_CONTENT tINTEGER
%token <FloatNode> tFLOAT  
%token <RegexpNode>  tREGEXP_END
%type <RestArgNode> f_rest_arg 
%type <Node> singleton strings string string1 xstring regexp
%type <Node> string_contents xstring_contents string_content method_call
%type <Node> regexp_contents
%type <Node> words qwords word literal numeric dsym cpath command_asgn command_call
%type <Node> compstmt bodystmt stmts stmt expr arg primary command 
%type <Node> stmt_or_begin
%type <Node> expr_value primary_value opt_else cases if_tail exc_var
   // ENEBO: missing call_args2, open_args
%type <Node> call_args opt_ensure paren_args superclass
%type <Node> command_args var_ref opt_paren_args block_call block_command
%type <Node> f_opt undef_list string_dvar backref
%type <ArgsNode> f_args f_arglist f_larglist block_param block_param_def opt_block_param 
%type <Node> mrhs mlhs_item mlhs_node arg_value case_body exc_list aref_args
   // ENEBO: missing block_var == for_var, opt_block_var
%type <Node> lhs none args
%type <ListNode> qword_list word_list f_arg f_optarg f_marg_list, symbol_list
%type <ListNode> qsym_list, symbols, qsymbols
   // FIXME: These are node until a better understanding of underlying type
%type <ArgsTailHolder> opt_args_tail, opt_block_args_tail, block_args_tail, args_tail
%type <Node> f_kw, f_block_kw
%type <ListNode> f_block_kwarg, f_kwarg
   // ENEBO: missing when_args
%type <ListNode> mlhs_head assocs assoc assoc_list mlhs_post f_block_optarg
%type <BlockPassNode> opt_block_arg block_arg none_block_pass
%type <BlockArgNode> opt_f_block_arg f_block_arg
%type <IterNode> brace_block do_block cmd_brace_block
   // ENEBO: missing mhls_entry
%type <MultipleAsgnNode> mlhs mlhs_basic 
%type <RescueBodyNode> opt_rescue
%type <AssignableNode> var_lhs
%type <LiteralNode> fsym
%type <Node> fitem
   // ENEBO: begin all new types
%type <Node> f_arg_item
%type <Node> bv_decls opt_bv_decl lambda_body 
%type <LambdaNode> lambda
%type <Node> mlhs_inner f_block_opt for_var
%type <Node> opt_call_args f_marg f_margs
%type <Token> bvar
%type <Token> user_variable, keyword_variable
   // ENEBO: end all new types

%type <Token> rparen rbracket reswords f_bad_arg
%type <Node> top_compstmt top_stmts top_stmt
%token <Token> tSYMBOLS_BEG
%token <Token> tQSYMBOLS_BEG
%token <Token> tDSTAR
%token <Token> tSTRING_DEND
%type <Token> kwrest_mark, f_kwrest

/*
 *    precedence table
 */

%nonassoc tLOWEST
%nonassoc tLBRACE_ARG

%nonassoc  kIF_MOD kUNLESS_MOD kWHILE_MOD kUNTIL_MOD
%left  kOR kAND
%right kNOT
%nonassoc kDEFINED
%right '=' tOP_ASGN
%left kRESCUE_MOD
%right '?' ':'
%nonassoc tDOT2 tDOT3
%left  tOROP
%left  tANDOP
%nonassoc  tCMP tEQ tEQQ tNEQ tMATCH tNMATCH
%left  tGT tGEQ tLT tLEQ
%left  tPIPE tCARET
%left  tAMPER2
%left  tLSHFT tRSHFT
%left  tPLUS tMINUS
%left  tSTAR2 tDIVIDE tPERCENT
%right tUMINUS_NUM tUMINUS
%right tPOW
%right tBANG tTILDE tUPLUS

   //%token <Integer> tLAST_TOKEN

%%
program       : {
                  lexer.setState(LexState.EXPR_BEG);
                  support.initTopLocalVariables();
              } top_compstmt {
  // ENEBO: Removed !compile_for_eval which probably is to reduce warnings
                  if ($2 != null) {
                      /* last expression should not be void */
                      if ($2 instanceof BlockNode) {
                          support.checkUselessStatement($<BlockNode>2.getLast());
                      } else {
                          support.checkUselessStatement($2);
                      }
                  }
                  support.getResult().setAST(support.addRootNode($2, support.getPosition($2)));
              }

top_compstmt  : top_stmts opt_terms {
                  if ($1 instanceof BlockNode) {
                      support.checkUselessStatements($<BlockNode>1);
                  }
                  $$ = $1;
              }

top_stmts     : none
              | top_stmt {
                    $$ = support.newline_node($1, support.getPosition($1, true));
              }
              | top_stmts terms top_stmt {
                    $$ = support.appendToBlock($1, support.newline_node($3, support.getPosition($3)));
              }
              | error top_stmt {
                    $$ = $2;
              }

top_stmt      : stmt
              | klBEGIN {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("BEGIN in method");
                    }
              } tLCURLY top_compstmt tRCURLY {
                    support.getResult().addBeginNode(new PreExe19Node($1.getPosition(), support.getCurrentScope(), $4));
                    $$ = null;
              }

bodystmt      : compstmt opt_rescue opt_else opt_ensure {
                  Node node = $1;

                  if ($2 != null) {
                      node = new RescueNode(support.getPosition($1), $1, $2, $3);
                  } else if ($3 != null) {
                      support.warn(ID.ELSE_WITHOUT_RESCUE, support.getPosition($1), "else without rescue is useless");
                      node = support.appendToBlock($1, $3);
                  }
                  if ($4 != null) {
                      node = new EnsureNode(support.getPosition($1), node, $4);
                  }

                  $$ = node;
                }

compstmt        : stmts opt_terms {
                    if ($1 instanceof BlockNode) {
                        support.checkUselessStatements($<BlockNode>1);
                    }
                    $$ = $1;
                }

stmts           : none
                | stmt_or_begin {
                    $$ = support.newline_node($1, support.getPosition($1, true));
                }
                | stmts terms stmt_or_begin {
                    $$ = support.appendToBlock($1, support.newline_node($3, support.getPosition($3)));
                }
                | error stmt {
                    $$ = $2;
                }

stmt_or_begin   : stmt {
                    $$ = $1;
                }
// FIXME: How can this new begin ever work?  is yyerror conditional in MRI?
                | kBEGIN {
                   support.yyerror("BEGIN is permitted only at toplevel");
                } tLCURLY top_compstmt tRCURLY {
                    $$ = new BeginNode(support.getPosition($1), $2);
                }

stmt            : kALIAS fitem {
                    lexer.setState(LexState.EXPR_FNAME);
                } fitem {
                    $$ = support.newAlias(support.union($1, $4), $2, $4);
                }
                | kALIAS tGVAR tGVAR {
                    $$ = new VAliasNode($1.getPosition(), (String) $2.getValue(), (String) $3.getValue());
                }
                | kALIAS tGVAR tBACK_REF {
                    $$ = new VAliasNode($1.getPosition(), (String) $2.getValue(), "$" + $<BackRefNode>3.getType());
                }
                | kALIAS tGVAR tNTH_REF {
                    support.yyerror("can't make alias for the number variables");
                }
                | kUNDEF undef_list {
                    $$ = $2;
                }
                | stmt kIF_MOD expr_value {
                    $$ = new IfNode(support.union($1, $3), support.getConditionNode($3), $1, null);
                }
                | stmt kUNLESS_MOD expr_value {
                    $$ = new IfNode(support.union($1, $3), support.getConditionNode($3), null, $1);
                }
                | stmt kWHILE_MOD expr_value {
                    if ($1 != null && $1 instanceof BeginNode) {
                        $$ = new WhileNode(support.getPosition($1), support.getConditionNode($3), $<BeginNode>1.getBodyNode(), false);
                    } else {
                        $$ = new WhileNode(support.getPosition($1), support.getConditionNode($3), $1, true);
                    }
                }
                | stmt kUNTIL_MOD expr_value {
                    if ($1 != null && $1 instanceof BeginNode) {
                        $$ = new UntilNode(support.getPosition($1), support.getConditionNode($3), $<BeginNode>1.getBodyNode(), false);
                    } else {
                        $$ = new UntilNode(support.getPosition($1), support.getConditionNode($3), $1, true);
                    }
                }
                | stmt kRESCUE_MOD stmt {
                    Node body = $3;
                    $$ = new RescueNode(support.getPosition($1), $1, new RescueBodyNode(support.getPosition($1), null, body, null), null);
                }
                | klEND tLCURLY compstmt tRCURLY {
                    if (support.isInDef() || support.isInSingle()) {
                        support.warn(ID.END_IN_METHOD, $1.getPosition(), "END in method; use at_exit");
                    }
                    $$ = new PostExeNode($1.getPosition(), $3);
                }
                | command_asgn
                | mlhs '=' command_call {
                    support.checkExpression($3);
                    $1.setValueNode($3);
                    $$ = $1;
                }
                | var_lhs tOP_ASGN command_call {
                    support.checkExpression($3);

                    SourcePosition pos = support.union($1, $3);
                    String asgnOp = (String) $2.getValue();
                    if (asgnOp.equals("||")) {
                        $1.setValueNode($3);
                        $$ = new OpAsgnOrNode(pos, support.gettable2($1), $1);
                    } else if (asgnOp.equals("&&")) {
                        $1.setValueNode($3);
                        $$ = new OpAsgnAndNode(pos, support.gettable2($1), $1);
                    } else {
                        $1.setValueNode(support.getOperatorCallNode(support.gettable2($1), asgnOp, $3));
                        $1.setPosition(pos);
                        $$ = $1;
                    }
                }
                | primary_value '[' opt_call_args rbracket tOP_ASGN command_call {
  // FIXME: arg_concat logic missing for opt_call_args
                    $$ = support.new_opElementAsgnNode(support.union($1, $6), $1, (String) $5.getValue(), $3, $6);
                }
                | primary_value tDOT tIDENTIFIER tOP_ASGN command_call {
                    $$ = new OpAsgnNode(support.getPosition($1), $1, $5, (String) $3.getValue(), (String) $4.getValue());
                }
                | primary_value tDOT tCONSTANT tOP_ASGN command_call {
                    $$ = new OpAsgnNode(support.getPosition($1), $1, $5, (String) $3.getValue(), (String) $4.getValue());
                }
                | primary_value tCOLON2 tCONSTANT tOP_ASGN command_call {
                    support.yyerror("can't make alias for the number variables");
                    $$ = null;
                }

                | primary_value tCOLON2 tIDENTIFIER tOP_ASGN command_call {
                    $$ = new OpAsgnNode(support.getPosition($1), $1, $5, (String) $3.getValue(), (String) $4.getValue());
                }
                | backref tOP_ASGN command_call {
                    support.backrefAssignError($1);
                }
                | lhs '=' mrhs {
                    $$ = support.node_assign($1, $3);
                }
                | mlhs '=' arg_value {
                    $1.setValueNode($3);
                    $$ = $1;
                }
                | mlhs '=' mrhs {
                    $<AssignableNode>1.setValueNode($3);
                    $$ = $1;
                    $1.setPosition(support.getPosition($1));
                }
                | expr

command_asgn    : lhs '=' command_call {
                    support.checkExpression($3);
                    $$ = support.node_assign($1, $3);
                }
                | lhs '=' command_asgn {
                    support.checkExpression($3);
                    $$ = support.node_assign($1, $3);
                }

// Node:expr *CURRENT* all but arg so far
expr            : command_call
                | expr kAND expr {
                    $$ = support.newAndNode($2.getPosition(), $1, $3);
                }
                | expr kOR expr {
                    $$ = support.newOrNode($2.getPosition(), $1, $3);
                }
                | kNOT opt_nl expr {
                    $$ = support.getOperatorCallNode(support.getConditionNode($3), "!");
                }
                | tBANG command_call {
                    $$ = support.getOperatorCallNode(support.getConditionNode($2), "!");
                }
                | arg

expr_value      : expr {
                    support.checkExpression($1);
                }

// Node:command - call with or with block on end [!null]
command_call    : command
                | block_command

// Node:block_command - A call with a block (foo.bar {...}, foo::bar {...}, bar {...}) [!null]
block_command   : block_call
                | block_call dot_or_colon operation2 command_args {
                    $$ = support.new_call($1, $3, $4, null);
                }

// :brace_block - [!null]
cmd_brace_block : tLBRACE_ARG {
                    support.pushBlockScope();
                } opt_block_param compstmt tRCURLY {
                    $$ = new IterNode($1.getPosition(), $3, $4, support.getCurrentScope());
                    support.popCurrentScope();
                }

// Node:command - fcall/call/yield/super [!null]
command        : operation command_args %prec tLOWEST {
                    $$ = support.new_fcall($1, $2, null);
                }
                | operation command_args cmd_brace_block {
                    $$ = support.new_fcall($1, $2, $3);
                }
                | primary_value tDOT operation2 command_args %prec tLOWEST {
                    $$ = support.new_call($1, $3, $4, null);
                }
                | primary_value tDOT operation2 command_args cmd_brace_block {
                    $$ = support.new_call($1, $3, $4, $5); 
                }
                | primary_value tCOLON2 operation2 command_args %prec tLOWEST {
                    $$ = support.new_call($1, $3, $4, null);
                }
                | primary_value tCOLON2 operation2 command_args cmd_brace_block {
                    $$ = support.new_call($1, $3, $4, $5);
                }
                | kSUPER command_args {
                    $$ = support.new_super($2, $1); // .setPosFrom($2);
                }
                | kYIELD command_args {
                    $$ = support.new_yield($1.getPosition(), $2);
                }
                | kRETURN call_args {
                    $$ = new ReturnNode(support.union($1, $2), support.ret_args($2, $1.getPosition()));
                }
                | kBREAK call_args {
                    $$ = new BreakNode(support.union($1, $2), support.ret_args($2, $1.getPosition()));
                }
                | kNEXT call_args {
                    $$ = new NextNode(support.union($1, $2), support.ret_args($2, $1.getPosition()));
                }

// MultipleAsgnNode:mlhs - [!null]
mlhs            : mlhs_basic
                | tLPAREN mlhs_inner rparen {
                    $$ = $2;
                }

// MultipleAsgnNode:mlhs_entry - mlhs w or w/o parens [!null]
mlhs_inner      : mlhs_basic {
                    $$ = $1;
                }
                | tLPAREN mlhs_inner rparen {
                    $$ = new MultipleAsgnNode($1.getPosition(), support.newArrayNode($1.getPosition(), $2), null, null);
                }

// MultipleAsgnNode:mlhs_basic - multiple left hand side (basic because used in multiple context) [!null]
mlhs_basic      : mlhs_head {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, null, null);
                }
                | mlhs_head mlhs_item {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1.add($2), null, null);
                }
                | mlhs_head tSTAR mlhs_node {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, $3, (ListNode) null);
                }
                | mlhs_head tSTAR mlhs_node ',' mlhs_post {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, $3, $5);
                }
                | mlhs_head tSTAR {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, new StarNode(lexer.getPosition()), null);
                }
                | mlhs_head tSTAR ',' mlhs_post {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, new StarNode(lexer.getPosition()), $4);
                }
                | tSTAR mlhs_node {
                    $$ = new MultipleAsgnNode($1.getPosition(), null, $2, null);
                }
                | tSTAR mlhs_node ',' mlhs_post {
                    $$ = new MultipleAsgnNode($1.getPosition(), null, $2, $4);
                }
                | tSTAR {
                      $$ = new MultipleAsgnNode($1.getPosition(), null, new StarNode(lexer.getPosition()), null);
                }
                | tSTAR ',' mlhs_post {
                      $$ = new MultipleAsgnNode($1.getPosition(), null, new StarNode(lexer.getPosition()), $3);
                }

mlhs_item       : mlhs_node
                | tLPAREN mlhs_inner rparen {
                    $$ = $2;
                }

// Set of mlhs terms at front of mlhs (a, *b, d, e = arr  # a is head)
mlhs_head       : mlhs_item ',' {
                    $$ = support.newArrayNode($1.getPosition(), $1);
                }
                | mlhs_head mlhs_item ',' {
                    $$ = $1.add($2);
                }

// Set of mlhs terms at end of mlhs (a, *b, d, e = arr  # d,e is post)
mlhs_post       : mlhs_item {
                    $$ = support.newArrayNode($1.getPosition(), $1);
                }
                | mlhs_post ',' mlhs_item {
                    $$ = $1.add($3);
                }

mlhs_node       : user_variable {
                    $$ = support.assignable($1, null);
                }
                | keyword_variable {
                    $$ = support.assignable($1, null);
                }
                | primary_value '[' opt_call_args rbracket {
                    $$ = support.aryset($1, $3);
                }
                | primary_value tDOT tIDENTIFIER {
                    $$ = support.attrset($1, (String) $3.getValue());
                }
                | primary_value tCOLON2 tIDENTIFIER {
                    $$ = support.attrset($1, (String) $3.getValue());
                }
                | primary_value tDOT tCONSTANT {
                    $$ = support.attrset($1, (String) $3.getValue());
                }
                | primary_value tCOLON2 tCONSTANT {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.getPosition($1);

                    $$ = new ConstDeclNode(position, null, support.new_colon2(position, $1, (String) $3.getValue()), null);
                }
                | tCOLON3 tCONSTANT {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = $1.getPosition();

                    $$ = new ConstDeclNode(position, null, support.new_colon3(position, (String) $2.getValue()), null);
                }
                | backref {
                    support.backrefAssignError($1);
                }

lhs             : user_variable {
                    $$ = support.assignable($1, null);
                }
                | keyword_variable {
                    $$ = support.assignable($1, null);
                }
                | primary_value '[' opt_call_args rbracket {
                    $$ = support.aryset($1, $3);
                }
                | primary_value tDOT tIDENTIFIER {
                    $$ = support.attrset($1, (String) $3.getValue());
                }
                | primary_value tCOLON2 tIDENTIFIER {
                    $$ = support.attrset($1, (String) $3.getValue());
                }
                | primary_value tDOT tCONSTANT {
                    $$ = support.attrset($1, (String) $3.getValue());
                }
                | primary_value tCOLON2 tCONSTANT {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = support.getPosition($1);

                    $$ = new ConstDeclNode(position, null, support.new_colon2(position, $1, (String) $3.getValue()), null);
                }
                | tCOLON3 tCONSTANT {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    SourcePosition position = $1.getPosition();

                    $$ = new ConstDeclNode(position, null, support.new_colon3(position, (String) $2.getValue()), null);
                }
                | backref {
                    support.backrefAssignError($1);
                }

cname           : tIDENTIFIER {
                    support.yyerror("class/module name must be CONSTANT");
                }
                | tCONSTANT

cpath           : tCOLON3 cname {
                    $$ = support.new_colon3($1.getPosition(), (String) $2.getValue());
                }
                | cname {
                    $$ = support.new_colon2($1.getPosition(), null, (String) $1.getValue());
                }
                | primary_value tCOLON2 cname {
                    $$ = support.new_colon2(support.getPosition($1), $1, (String) $3.getValue());
                }

// Token:fname - A function name [!null]
fname          : tIDENTIFIER | tCONSTANT | tFID 
               | op {
                   lexer.setState(LexState.EXPR_ENDFN);
                   $$ = $1;
               }
               | reswords {
                   lexer.setState(LexState.EXPR_ENDFN);
                   $$ = $1;
               }

// LiteralNode:fsym
fsym           : fname {
                    $$ = new LiteralNode($1);
                }
                | symbol {
                    $$ = new LiteralNode($1);
                }

// Node:fitem
fitem           : fsym {
                    $$ = $1;
                }
                | dsym {
                    $$ = $1;
                }

undef_list      : fitem {
                    $$ = support.newUndef($1.getPosition(), $1);
                }
                | undef_list ',' {
                    lexer.setState(LexState.EXPR_FNAME);
                } fitem {
                    $$ = support.appendToBlock($1, support.newUndef($1.getPosition(), $4));
                }

// Token:op
op              : tPIPE | tCARET | tAMPER2 | tCMP | tEQ | tEQQ | tMATCH
                | tNMATCH | tGT | tGEQ | tLT | tLEQ | tNEQ | tLSHFT | tRSHFT
                | tDSTAR | tPLUS | tMINUS | tSTAR2 | tSTAR | tDIVIDE | tPERCENT 
                | tPOW | tBANG | tTILDE | tUPLUS | tUMINUS | tAREF | tASET 
                | tBACK_REF2

// Token:op
reswords        : k__LINE__ | k__FILE__ | k__ENCODING__ | klBEGIN | klEND
                | kALIAS | kAND | kBEGIN | kBREAK | kCASE | kCLASS | kDEF
                | kDEFINED | kDO | kELSE | kELSIF | kEND | kENSURE | kFALSE
                | kFOR | kIN | kMODULE | kNEXT | kNIL | kNOT
                | kOR | kREDO | kRESCUE | kRETRY | kRETURN | kSELF | kSUPER
                | kTHEN | kTRUE | kUNDEF | kWHEN | kYIELD
                | kIF_MOD | kUNLESS_MOD | kWHILE_MOD | kUNTIL_MOD | kRESCUE_MOD

arg             : lhs '=' arg {
                    $$ = support.node_assign($1, $3);
                    // FIXME: Consider fixing node_assign itself rather than single case
                    $<Node>$.setPosition(support.union($1, $3));
                }
                | lhs '=' arg kRESCUE_MOD arg {
                    SourcePosition position = $4.getPosition();
                    Node body = $5;
                    $$ = support.node_assign($1, new RescueNode(position, $3, new RescueBodyNode(position, null, body, null), null));
                }
                | var_lhs tOP_ASGN arg {
                    support.checkExpression($3);

                    SourcePosition pos = support.union($1, $3);
                    String asgnOp = (String) $2.getValue();
                    if (asgnOp.equals("||")) {
                        $1.setValueNode($3);
                        $$ = new OpAsgnOrNode(pos, support.gettable2($1), $1);
                    } else if (asgnOp.equals("&&")) {
                        $1.setValueNode($3);
                        $$ = new OpAsgnAndNode(pos, support.gettable2($1), $1);
                    } else {
                        $1.setValueNode(support.getOperatorCallNode(support.gettable2($1), asgnOp, $3));
                        $1.setPosition(pos);
                        $$ = $1;
                    }
                }
                | var_lhs tOP_ASGN arg kRESCUE_MOD arg {
                    support.checkExpression($3);
                    SourcePosition pos = support.union($4, $5);
                    Node body = $5;
                    Node rescue = new RescueNode(pos, $3, new RescueBodyNode($4.getPosition(), null, body, null), null);

                    pos = support.union($1, $3);
                    String asgnOp = (String) $2.getValue();
                    if (asgnOp.equals("||")) {
                        $1.setValueNode(rescue);
                        $$ = new OpAsgnOrNode(pos, support.gettable2($1), $1);
                    } else if (asgnOp.equals("&&")) {
                        $1.setValueNode(rescue);
                        $$ = new OpAsgnAndNode(pos, support.gettable2($1), $1);
                    } else {
                        $1.setValueNode(support.getOperatorCallNode(support.gettable2($1), asgnOp, rescue));
                        $1.setPosition(pos);
                        $$ = $1;
                    }
                }
                | primary_value '[' opt_call_args rbracket tOP_ASGN arg {
  // FIXME: arg_concat missing for opt_call_args
                    $$ = support.new_opElementAsgnNode(support.union($1, $6), $1, (String) $5.getValue(), $3, $6);
                }
                | primary_value tDOT tIDENTIFIER tOP_ASGN arg {
                    $$ = new OpAsgnNode(support.getPosition($1), $1, $5, (String) $3.getValue(), (String) $4.getValue());
                }
                | primary_value tDOT tCONSTANT tOP_ASGN arg {
                    $$ = new OpAsgnNode(support.getPosition($1), $1, $5, (String) $3.getValue(), (String) $4.getValue());
                }
                | primary_value tCOLON2 tIDENTIFIER tOP_ASGN arg {
                    $$ = new OpAsgnNode(support.getPosition($1), $1, $5, (String) $3.getValue(), (String) $4.getValue());
                }
                | primary_value tCOLON2 tCONSTANT tOP_ASGN arg {
                    support.yyerror("constant re-assignment");
                }
                | tCOLON3 tCONSTANT tOP_ASGN arg {
                    support.yyerror("constant re-assignment");
                }
                | backref tOP_ASGN arg {
                    support.backrefAssignError($1);
                }
                | arg tDOT2 arg {
                    support.checkExpression($1);
                    support.checkExpression($3);
    
                    boolean isLiteral = $1 instanceof FixnumNode && $3 instanceof FixnumNode;
                    $$ = new DotNode(support.union($1, $3), $1, $3, false, isLiteral);
                }
                | arg tDOT3 arg {
                    support.checkExpression($1);
                    support.checkExpression($3);

                    boolean isLiteral = $1 instanceof FixnumNode && $3 instanceof FixnumNode;
                    $$ = new DotNode(support.union($1,  $3), $1, $3, true, isLiteral);
                }
                | arg tPLUS arg {
                    $$ = support.getOperatorCallNode($1, "+", $3, lexer.getPosition());
                }
                | arg tMINUS arg {
                    $$ = support.getOperatorCallNode($1, "-", $3, lexer.getPosition());
                }
                | arg tSTAR2 arg {
                    $$ = support.getOperatorCallNode($1, "*", $3, lexer.getPosition());
                }
                | arg tDIVIDE arg {
                    $$ = support.getOperatorCallNode($1, "/", $3, lexer.getPosition());
                }
                | arg tPERCENT arg {
                    $$ = support.getOperatorCallNode($1, "%", $3, lexer.getPosition());
                }
                | arg tPOW arg {
                    $$ = support.getOperatorCallNode($1, "**", $3, lexer.getPosition());
                }
                | tUMINUS_NUM tINTEGER tPOW arg {
                    $$ = support.getUnaryCallNode(support.getOperatorCallNode($2, "**", $4, lexer.getPosition()), $1);
                }
                | tUMINUS_NUM tFLOAT tPOW arg {
                    $$ = support.getUnaryCallNode(support.getOperatorCallNode($2, "**", $4, lexer.getPosition()), $1);
                }
                | tUPLUS arg {
                    $$ = support.getUnaryCallNode($2, $1);
                }
                | tUMINUS arg {
                    $$ = support.getUnaryCallNode($2, $1);
                }
                | arg tPIPE arg {
                    $$ = support.getOperatorCallNode($1, "|", $3, lexer.getPosition());
                }
                | arg tCARET arg {
                    $$ = support.getOperatorCallNode($1, "^", $3, lexer.getPosition());
                }
                | arg tAMPER2 arg {
                    $$ = support.getOperatorCallNode($1, "&", $3, lexer.getPosition());
                }
                | arg tCMP arg {
                    $$ = support.getOperatorCallNode($1, "<=>", $3, lexer.getPosition());
                }
                | arg tGT arg {
                    $$ = support.getOperatorCallNode($1, ">", $3, lexer.getPosition());
                }
                | arg tGEQ arg {
                    $$ = support.getOperatorCallNode($1, ">=", $3, lexer.getPosition());
                }
                | arg tLT arg {
                    $$ = support.getOperatorCallNode($1, "<", $3, lexer.getPosition());
                }
                | arg tLEQ arg {
                    $$ = support.getOperatorCallNode($1, "<=", $3, lexer.getPosition());
                }
                | arg tEQ arg {
                    $$ = support.getOperatorCallNode($1, "==", $3, lexer.getPosition());
                }
                | arg tEQQ arg {
                    $$ = support.getOperatorCallNode($1, "===", $3, lexer.getPosition());
                }
                | arg tNEQ arg {
                    $$ = support.getOperatorCallNode($1, "!=", $3, lexer.getPosition());
                }
                | arg tMATCH arg {
                    $$ = support.getMatchNode($1, $3);
                  /* ENEBO
                        $$ = match_op($1, $3);
                        if (nd_type($1) == NODE_LIT && TYPE($1->nd_lit) == T_REGEXP) {
                            $$ = reg_named_capture_assign($1->nd_lit, $$);
                        }
                  */
                }
                | arg tNMATCH arg {
                    $$ = support.getOperatorCallNode($1, "!~", $3, lexer.getPosition());
                }
                | tBANG arg {
                    $$ = support.getOperatorCallNode(support.getConditionNode($2), "!");
                }
                | tTILDE arg {
                    $$ = support.getOperatorCallNode($2, "~");
                }
                | arg tLSHFT arg {
                    $$ = support.getOperatorCallNode($1, "<<", $3, lexer.getPosition());
                }
                | arg tRSHFT arg {
                    $$ = support.getOperatorCallNode($1, ">>", $3, lexer.getPosition());
                }
                | arg tANDOP arg {
                    $$ = support.newAndNode($2.getPosition(), $1, $3);
                }
                | arg tOROP arg {
                    $$ = support.newOrNode($2.getPosition(), $1, $3);
                }
                | kDEFINED opt_nl arg {
                    $$ = new DefinedNode($1.getPosition(), $3);
                }
                | arg '?' arg opt_nl ':' arg {
                    $$ = new IfNode(support.getPosition($1), support.getConditionNode($1), $3, $6);
                }
                | primary {
                    $$ = $1;
                }

arg_value       : arg {
                    support.checkExpression($1);
                    $$ = $1;
                }

aref_args       : none
                | args trailer {
                    $$ = $1;
                }
                | args ',' assocs trailer {
                    $$ = support.arg_append($1, new HashNode(lexer.getPosition(), $3));
                }
                | assocs trailer {
                    $$ = support.newArrayNode($1.getPosition(), new HashNode(lexer.getPosition(), $1));
                }

paren_args      : tLPAREN2 opt_call_args rparen {
                    SourcePosition pos = support.union($1, $3);
                    
                    if ($2 == null) {
                        $$ = new ArrayNode(pos);
                    } else {
                        $$ = $2;
                        $<Node>$.setPosition(pos);
                    }
                }

opt_paren_args  : none | paren_args

opt_call_args   : none 
                | call_args
                | args ',' {
                    $$ = $1;
                }
                | args ',' assocs ',' {
                    $$ = support.arg_append($1, new HashNode(lexer.getPosition(), $3));
                }
                | assocs ',' {
                    $$ = support.newArrayNode($1.getPosition(), new HashNode(lexer.getPosition(), $1));
                }
   

// [!null]
call_args       : command {
                    $$ = support.newArrayNode(support.getPosition($1), $1);
                }
                | args opt_block_arg {
                    $$ = support.arg_blk_pass($1, $2);
                }
                | assocs opt_block_arg {
                    $$ = support.newArrayNode($1.getPosition(), new HashNode(lexer.getPosition(), $1));
                    $$ = support.arg_blk_pass((Node)$$, $2);
                }
                | args ',' assocs opt_block_arg {
                    $$ = support.arg_append($1, new HashNode(lexer.getPosition(), $3));
                    $$ = support.arg_blk_pass((Node)$$, $4);
                }
                | block_arg {
                }

command_args    : /* none */ {
                    $$ = Long.valueOf(lexer.getCmdArgumentState().begin());
                } call_args {
                    lexer.getCmdArgumentState().reset($<Long>1.longValue());
                    $$ = $2;
                }

block_arg       : tAMPER arg_value {
                    $$ = new BlockPassNode(support.union($1, $2), $2);
                }

opt_block_arg   : ',' block_arg {
                    $$ = $2;
                }
                | none_block_pass

// [!null]
args            : arg_value {
                    SourcePosition pos = $1 == null ? lexer.getPosition() : $1.getPosition();
                    $$ = support.newArrayNode(pos, $1);
                }
                | tSTAR arg_value {
                    $$ = support.newSplatNode(support.union($1, $2), $2);
                }
                | args ',' arg_value {
                    Node node = support.splat_array($1);

                    if (node != null) {
                        $$ = support.list_append(node, $3);
                    } else {
                        $$ = support.arg_append($1, $3);
                    }
                }
                | args ',' tSTAR arg_value {
                    Node node = null;

                    // FIXME: lose syntactical elements here (and others like this)
                    if ($4 instanceof ArrayNode &&
                        (node = support.splat_array($1)) != null) {
                        $$ = support.list_concat(node, $4);
                    } else {
                        $$ = support.arg_concat(support.getPosition($1), $1, $4);
                    }
                }

mrhs            : args ',' arg_value {
                    Node node = support.splat_array($1);

                    if (node != null) {
                        $$ = support.list_append(node, $3);
                    } else {
                        $$ = support.arg_append($1, $3);
                    }
                }
                | args ',' tSTAR arg_value {
                    Node node = null;

                    if ($4 instanceof ArrayNode &&
                        (node = support.splat_array($1)) != null) {
                        $$ = support.list_concat(node, $4);
                    } else {
                        $$ = support.arg_concat(support.union($1, $4), $1, $4);
                    }
                }
                | tSTAR arg_value {
                    $$ = support.newSplatNode(support.union($1, $2), $2);  
                }

primary         : literal
                | strings
                | xstring
                | regexp
                | words
                | qwords
                | symbols { 
                     $$ = $1; // FIXME: Why complaining without $$ = $1;
                }
                | qsymbols {
                     $$ = $1; // FIXME: Why complaining without $$ = $1;
                }
                | var_ref
                | backref
                | tFID {
                     $$ = support.new_fcall($1, null, null);
                }
                | kBEGIN bodystmt kEND {
                    $$ = new BeginNode(support.union($1, $3), $2);
                }
                | tLPAREN_ARG {
                    lexer.setState(LexState.EXPR_ENDARG);
                } rparen {
                    $$ = null; //FIXME: Should be implicit nil?
                }
                | tLPAREN_ARG expr {
                    lexer.setState(LexState.EXPR_ENDARG); 
                } rparen {
                    support.warning(ID.GROUPED_EXPRESSION, $1.getPosition(), "(...) interpreted as grouped expression");
                    $$ = $2;
                }
                | tLPAREN compstmt tRPAREN {
                    SourcePosition pos = support.union($1, $3);
                    Node implicitNil = $2 == null ? new ImplicitNilNode(pos) : $2;
                    // compstmt position includes both parens around it
                    if (implicitNil != null) implicitNil.setPosition(pos);

                    $$ = implicitNil;
                }
                | primary_value tCOLON2 tCONSTANT {
                    $$ = support.new_colon2(support.union($1, $3), $1, (String) $3.getValue());
                }
                | tCOLON3 tCONSTANT {
                    $$ = support.new_colon3(support.union($1, $2), (String) $2.getValue());
                }
                | tLBRACK aref_args tRBRACK {
                    SourcePosition position = support.union($1, $3);
                    if ($2 == null) {
                        $$ = new ZArrayNode(position); /* zero length array */
                    } else {
                        $$ = $2;
                        $<ISourcePositionHolder>$.setPosition(position);
                    }
                }
                | tLBRACE assoc_list tRCURLY {
                    $$ = new HashNode(support.union($1, $3), $2);
                }
                | kRETURN {
                    $$ = new ReturnNode($1.getPosition(), null);
                }
                | kYIELD tLPAREN2 call_args rparen {
                    $$ = support.new_yield(support.union($1, $4), $3);
                }
                | kYIELD tLPAREN2 rparen {
                    $$ = new ZYieldNode(support.union($1, $3));
                }
                | kYIELD {
                    $$ = new ZYieldNode($1.getPosition());
                }
                | kDEFINED opt_nl tLPAREN2 expr rparen {
                    $$ = new DefinedNode(support.union($1, $5), $4);
                }
                | kNOT tLPAREN2 expr rparen {
                    $$ = support.getOperatorCallNode(support.getConditionNode($3), "!");
                }
                | kNOT tLPAREN2 rparen {
                    $$ = support.getOperatorCallNode(null, "!");
                }
                | operation brace_block {
                    $$ = support.new_fcall($1, $2, null);
                }
                | method_call
                | method_call brace_block {
                    if ($1 != null && 
                          $<BlockAcceptingNode>1.getIter() instanceof BlockPassNode) {
                        throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, $1.getPosition(), lexer.getCurrentLine(), "Both block arg and actual block given.");
                    }
                    $<BlockAcceptingNode>1.setIter($2);
                    $$ = $1;
                    $<Node>$.setPosition(support.union($1, $2));
                }
                | tLAMBDA lambda {
                    $$ = $2;
                }
                | kIF expr_value then compstmt if_tail kEND {
                    $$ = new IfNode(support.union($1, $6), support.getConditionNode($2), $4, $5);
                }
                | kUNLESS expr_value then compstmt opt_else kEND {
                    $$ = new IfNode(support.union($1, $6), support.getConditionNode($2), $5, $4);
                }
                | kWHILE {
                    lexer.getConditionState().begin();
                } expr_value do {
                    lexer.getConditionState().end();
                } compstmt kEND {
                    Node body = $6;
                    $$ = new WhileNode(support.union($1, $7), support.getConditionNode($3), body);
                }
                | kUNTIL {
                  lexer.getConditionState().begin();
                } expr_value do {
                  lexer.getConditionState().end();
                } compstmt kEND {
                    Node body = $6;
                    $$ = new UntilNode(support.union($1, $7), support.getConditionNode($3), body);
                }
                | kCASE expr_value opt_terms case_body kEND {
                    $$ = support.newCaseNode(support.union($1, $5), $2, $4);
                }
                | kCASE opt_terms case_body kEND {
                    $$ = support.newCaseNode(support.union($1, $4), null, $3);
                }
                | kFOR for_var kIN {
                    lexer.getConditionState().begin();
                } expr_value do {
                    lexer.getConditionState().end();
                } compstmt kEND {
                      // ENEBO: Lots of optz in 1.9 parser here
                    $$ = new ForNode(support.union($1, $9), $2, $8, $5, support.getCurrentScope());
                }
                | kCLASS cpath superclass {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("class definition in method body");
                    }
                    support.pushLocalScope();
                } bodystmt kEND {
                    Node body = $5;

                    $$ = new ClassNode(support.union($1, $6), $<Colon3Node>2, support.getCurrentScope(), body, $3);
                    support.popCurrentScope();
                }
                | kCLASS tLSHFT expr {
                    $$ = Boolean.valueOf(support.isInDef());
                    support.setInDef(false);
                } term {
                    $$ = Integer.valueOf(support.getInSingle());
                    support.setInSingle(0);
                    support.pushLocalScope();
                } bodystmt kEND {
                    $$ = new SClassNode(support.union($1, $8), $3, support.getCurrentScope(), $7);
                    support.popCurrentScope();
                    support.setInDef($<Boolean>4.booleanValue());
                    support.setInSingle($<Integer>6.intValue());
                }
                | kMODULE cpath {
                    if (support.isInDef() || support.isInSingle()) { 
                        support.yyerror("module definition in method body");
                    }
                    support.pushLocalScope();
                } bodystmt kEND {
                    Node body = $4;

                    $$ = new ModuleNode(support.union($1, $5), $<Colon3Node>2, support.getCurrentScope(), body);
                    support.popCurrentScope();
                }
                | kDEF fname {
                    support.setInDef(true);
                    support.pushLocalScope();
                } f_arglist bodystmt kEND {
                    Node body = $5;

                    $$ = new DefnNode(support.union($1, $6), new MethodNameNode($2.getPosition(), (String) $2.getValue()), $4, support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
                }
                | kDEF singleton dot_or_colon {
                    lexer.setState(LexState.EXPR_FNAME);
                } fname {
                    support.setInSingle(support.getInSingle() + 1);
                    support.pushLocalScope();
                    lexer.setState(LexState.EXPR_ENDFN); /* force for args */
                } f_arglist bodystmt kEND {
                    Node body = $8;

                    $$ = new DefsNode(support.union($1, $9), $2, new MethodNameNode($5.getPosition(), (String) $5.getValue()), $7, support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
                }
                | kBREAK {
                    $$ = new BreakNode($1.getPosition(), null);
                }
                | kNEXT {
                    $$ = new NextNode($1.getPosition(), null);
                }
                | kREDO {
                    $$ = new RedoNode($1.getPosition());
                }
                | kRETRY {
                    $$ = new RetryNode($1.getPosition());
                }

primary_value   : primary {
                    support.checkExpression($1);
                    $$ = $1;
                }

then            : term
                | kTHEN
                | term kTHEN

do              : term
                | kDO_COND

if_tail         : opt_else
                | kELSIF expr_value then compstmt if_tail {
                    $$ = new IfNode($1.getPosition(), support.getConditionNode($2), $4, $5);
                }

opt_else        : none
                | kELSE compstmt {
                    $$ = $2;
                }

for_var         : lhs
                | mlhs {
                }

f_marg          : f_norm_arg {
                     $$ = support.assignable($1, null);
                }
                | tLPAREN f_margs rparen {
                    $$ = $2;
                    $<Node>$.setPosition(support.union($1, $3));
                }

// [!null]
f_marg_list     : f_marg {
                    $$ = support.newArrayNode($1.getPosition(), $1);
                }
                | f_marg_list ',' f_marg {
                    $$ = $1.add($3);
                }

f_margs         : f_marg_list {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, null, null);
                }
                | f_marg_list ',' tSTAR f_norm_arg {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, support.assignable($4, null), null);
                }
                | f_marg_list ',' tSTAR f_norm_arg ',' f_marg_list {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, support.assignable($4, null), $6);
                }
                | f_marg_list ',' tSTAR {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, new StarNode(lexer.getPosition()), null);
                }
                | f_marg_list ',' tSTAR ',' f_marg_list {
                    $$ = new MultipleAsgnNode($1.getPosition(), $1, new StarNode(lexer.getPosition()), $5);
                }
                | tSTAR f_norm_arg {
                    $$ = new MultipleAsgnNode($1.getPosition(), null, support.assignable($2, null), null);
                }
                | tSTAR f_norm_arg ',' f_marg_list {
                    $$ = new MultipleAsgnNode($1.getPosition(), null, support.assignable($2, null), $4);
                }
                | tSTAR {
                    $$ = new MultipleAsgnNode($1.getPosition(), null, new StarNode(lexer.getPosition()), null);
                }
                | tSTAR ',' f_marg_list {
                    $$ = new MultipleAsgnNode($1.getPosition(), null, null, $3);
                }

block_args_tail : f_block_kwarg ',' f_kwrest opt_f_block_arg {
                    $$ = support.new_args_tail($1.getPosition(), $1, $3, $4);
                }
                | f_block_kwarg opt_f_block_arg {
                    $$ = support.new_args_tail($1.getPosition(), $1, null, $2);
                }
                | f_kwrest opt_f_block_arg {
                    $$ = support.new_args_tail($1.getPosition(), null, $1, $2);
                }
                | f_block_arg {
                    $$ = support.new_args_tail($1.getPosition(), null, null, $1);
                }

opt_block_args_tail : ',' block_args_tail {
                    $$ = $2;
                }
                | /* none */ {
                    $$ = support.new_args_tail(lexer.getPosition(), null, null, null);
                }

// [!null]
block_param     : f_arg ',' f_block_optarg ',' f_rest_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $6), $1, $3, $5, null, $6);
                }
                | f_arg ',' f_block_optarg ',' f_rest_arg ',' f_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $8), $1, $3, $5, $7, $8);
                }
                | f_arg ',' f_block_optarg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $4), $1, $3, null, null, $4);
                }
                | f_arg ',' f_block_optarg ',' f_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $6), $1, $3, null, $5, $6);
                }
                | f_arg ',' f_rest_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $4), $1, null, $3, null, $4);
                }
                | f_arg ',' {
                    RestArgNode rest = new UnnamedRestArgNode($1.getPosition(), null, support.getCurrentScope().addVariable("*"));
                    $$ = support.new_args($1.getPosition(), $1, null, rest, null, (ArgsTailHolder) null);
                }
                | f_arg ',' f_rest_arg ',' f_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $6), $1, null, $3, $5, $6);
                }
                | f_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $2), $1, null, null, null, $2);
                }
                | f_block_optarg ',' f_rest_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $4), null, $1, $3, null, $4);
                }
                | f_block_optarg ',' f_rest_arg ',' f_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $6), null, $1, $3, $5, $6);
                }
                | f_block_optarg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $2), null, $1, null, null, $2);
                }
                | f_block_optarg ',' f_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $4), null, $1, null, $3, $4);
                }
                | f_rest_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $2), null, null, $1, null, $2);
                }
                | f_rest_arg ',' f_arg opt_block_args_tail {
                    $$ = support.new_args(support.union($1, $4), null, null, $1, $3, $4);
                }
                | block_args_tail {
                    $$ = support.new_args($1.getPosition(), null, null, null, null, $1);
                }

opt_block_param : none {
    // was $$ = null;
                    $$ = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
                }
                | block_param_def {
                    lexer.commandStart = true;
                    $$ = $1;
                }

block_param_def : tPIPE opt_bv_decl tPIPE {
                    $$ = support.new_args($1.getPosition(), null, null, null, null, (ArgsTailHolder) null);
                }
                | tOROP {
                    $$ = support.new_args($1.getPosition(), null, null, null, null, (ArgsTailHolder) null);
                }
                | tPIPE block_param opt_bv_decl tPIPE {
                    $$ = $2;
                }

// shadowed block variables....
opt_bv_decl     : opt_nl {
                    $$ = null;
                }
                | opt_nl ';' bv_decls opt_nl {
                    $$ = null;
                }

// ENEBO: This is confusing...
bv_decls        : bvar {
                    $$ = null;
                }
                | bv_decls ',' bvar {
                    $$ = null;
                }

bvar            : tIDENTIFIER {
                    support.new_bv($1);
                }
                | f_bad_arg {
                    $$ = null;
                }

lambda          : /* none */  {
                    support.pushBlockScope();
                    $$ = lexer.getLeftParenBegin();
                    lexer.setLeftParenBegin(lexer.incrementParenNest());
                } f_larglist lambda_body {
                    $$ = new LambdaNode(support.union($2, $3), $2, $3, support.getCurrentScope());
                    support.popCurrentScope();
                    lexer.setLeftParenBegin($<Integer>1);
                }

f_larglist      : tLPAREN2 f_args opt_bv_decl tRPAREN {
                    $$ = $2;
                    $<ISourcePositionHolder>$.setPosition(support.union($1, $3));
                }
                | f_args opt_bv_decl {
                    $$ = $1;
                    $<ISourcePositionHolder>$.setPosition(support.union($1, $2));
                }

lambda_body     : tLAMBEG compstmt tRCURLY {
                    $$ = $2;
                }
                | kDO_LAMBDA compstmt kEND {
                    $$ = $2;
                }

do_block        : kDO_BLOCK {
                    support.pushBlockScope();
                } opt_block_param compstmt kEND {
                    $$ = new IterNode(support.union($1, $5), $3, $4, support.getCurrentScope());
                    support.popCurrentScope();
                }

block_call      : command do_block {
                    // Workaround for JRUBY-2326 (MRI does not enter this production for some reason)
                    if ($1 instanceof YieldNode) {
                        throw new SyntaxException(PID.BLOCK_GIVEN_TO_YIELD, $1.getPosition(), lexer.getCurrentLine(), "block given to yield");
                    }
                    if ($<BlockAcceptingNode>1.getIter() instanceof BlockPassNode) {
                        throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, $1.getPosition(), lexer.getCurrentLine(), "Both block arg and actual block given.");
                    }
                    $<BlockAcceptingNode>1.setIter($2);
                    $$ = $1;
                    $<Node>$.setPosition(support.union($1, $2));
                }
                | block_call dot_or_colon operation2 opt_paren_args {
                    $$ = support.new_call($1, $3, $4, null);
                }
                | block_call dot_or_colon operation2 opt_paren_args brace_block {
                    $$ = support.new_call($1, $3, $4, $5);
                }
                | block_call dot_or_colon operation2 command_args do_block {
                    $$ = support.new_call($1, $3, $4, $5);
                }

// [!null]
method_call     : operation paren_args {
                    $$ = support.new_fcall($1, $2, null);
                }
                | primary_value tDOT operation2 opt_paren_args {
                    $$ = support.new_call($1, $3, $4, null);
                }
                | primary_value tCOLON2 operation2 paren_args {
                    $$ = support.new_call($1, $3, $4, null);
                }
                | primary_value tCOLON2 operation3 {
                    $$ = support.new_call($1, $3, null, null);
                }
                | primary_value tDOT paren_args {
                    $$ = support.new_call($1, new Token("call", $1.getPosition()), $3, null);
                }
                | primary_value tCOLON2 paren_args {
                    $$ = support.new_call($1, new Token("call", $1.getPosition()), $3, null);
                }
                | kSUPER paren_args {
                    $$ = support.new_super($2, $1);
                }
                | kSUPER {
                    $$ = new ZSuperNode($1.getPosition());
                }
                | primary_value '[' opt_call_args rbracket {
                    if ($1 instanceof SelfNode) {
                      $$ = support.new_fcall(new Token("[]", support.union($1, $4)), $3, null);
                    } else {
                      $$ = support.new_call($1, new Token("[]", support.union($1, $4)), $3, null);
                    }
                }

brace_block     : tLCURLY {
                    support.pushBlockScope();
                } opt_block_param compstmt tRCURLY {
                    $$ = new IterNode(support.union($1, $5), $3, $4, support.getCurrentScope());
                    support.popCurrentScope();
                }
                | kDO {
                    support.pushBlockScope();
                } opt_block_param compstmt kEND {
                    $$ = new IterNode(support.union($1, $5), $3, $4, support.getCurrentScope());
                    support.popCurrentScope();
                }

case_body       : kWHEN args then compstmt cases {
                    $$ = support.newWhenNode(support.union($1, support.unwrapNewlineNode($4)), $2, $4, $5);
                }

cases           : opt_else | case_body

opt_rescue      : kRESCUE exc_list exc_var then compstmt opt_rescue {
                    Node node;
                    if ($3 != null) {
                        node = support.appendToBlock(support.node_assign($3, new GlobalVarNode($1.getPosition(), "$!")), $5);
                        if ($5 != null) {
                            node.setPosition(support.unwrapNewlineNode($5).getPosition());
                        }
                    } else {
                        node = $5;
                    }
                    Node body = node;
                    $$ = new RescueBodyNode($1.getPosition(), $2, body, $6);
                }
                | { 
                    $$ = null; 
                }

exc_list        : arg_value {
                    $$ = support.newArrayNode($1.getPosition(), $1);
                }
                | mrhs {
                    $$ = support.splat_array($1);
                    if ($$ == null) $$ = $1;
                }
                | none

exc_var         : tASSOC lhs {
                    $$ = $2;
                }
                | none

opt_ensure      : kENSURE compstmt {
                    $$ = $2;
                }
                | none

literal         : numeric
                | symbol {
                    // FIXME: We may be intern'ing more than once.
                    $$ = new SymbolNode($1.getPosition(), ((String) $1.getValue()).intern());
                }
                | dsym

strings         : string {
                    $$ = $1 instanceof EvStrNode ? new DStrNode($1.getPosition()).add($1) : $1;
                    /*
                    NODE *node = $1;
                    if (!node) {
                        node = NEW_STR(STR_NEW0());
                    } else {
                        node = evstr2dstr(node);
                    }
                    $$ = node;
                    */
                }

// [!null]
string          : tCHAR {
                    $$ = new StrNode($<Token>0.getPosition(), (String) $1.getValue());
                }
                | string1 {
                    $$ = $1;
                }
                | string string1 {
                    $$ = support.literal_concat($1.getPosition(), $1, $2);
                }

string1         : tSTRING_BEG string_contents tSTRING_END {
                    $$ = $2;

                    $<ISourcePositionHolder>$.setPosition(support.union($1, $3));
                    int extraLength = ((String) $1.getValue()).length() - 1;

                    // We may need to subtract addition offset off of first 
                    // string fragment (we optimistically take one off in
                    // ParserSupport.literal_concat).  Check token length
                    // and subtract as neeeded.
                    if (($2 instanceof DStrNode) && extraLength > 0) {
                      Node strNode = ((DStrNode)$2).get(0);
                    }
                }

xstring         : tXSTRING_BEG xstring_contents tSTRING_END {
                    SourcePosition position = support.union($1, $3);

                    if ($2 == null) {
                        $$ = new XStrNode(position, null);
                    } else if ($2 instanceof StrNode) {
                        $$ = new XStrNode(position, $<StrNode>2.getValue());
                    } else if ($2 instanceof DStrNode) {
                        $$ = new DXStrNode(position, $<DStrNode>2);

                        $<Node>$.setPosition(position);
                    } else {
                        $$ = new DXStrNode(position).add($2);
                    }
                }

regexp          : tREGEXP_BEG regexp_contents tREGEXP_END {
                    $$ = support.newRegexpNode(support.union($1, $3), $2, (RegexpNode) $3);
                }

words           : tWORDS_BEG ' ' tSTRING_END {
                    $$ = new ZArrayNode(support.union($1, $3));
                }
                | tWORDS_BEG word_list tSTRING_END {
                    $$ = $2;
                }

word_list       : /* none */ {
                    $$ = new ArrayNode(lexer.getPosition());
                }
                | word_list word ' ' {
                     $$ = $1.add($2 instanceof EvStrNode ? new DStrNode($1.getPosition()).add($2) : $2);
                }

word            : string_content
                | word string_content {
                     $$ = support.literal_concat(support.getPosition($1), $1, $2);
                }

symbols         : tSYMBOLS_BEG ' ' tSTRING_END {
                    $$ = new ArrayNode(lexer.getPosition());
                }
                | tSYMBOLS_BEG symbol_list tSTRING_END {
                    $$ = $2;
                }

symbol_list     : /* none */ {
                    $$ = new ArrayNode(lexer.getPosition());
                }
                | symbol_list word ' ' {
                    $$ = $1.add($2 instanceof EvStrNode ? new DSymbolNode($1.getPosition()).add($2) : support.asSymbol($1.getPosition(), $2));
                }

qwords          : tQWORDS_BEG ' ' tSTRING_END {
                     $$ = new ZArrayNode(support.union($1, $3));
                }
                | tQWORDS_BEG qword_list tSTRING_END {
                    $$ = $2;
                    $<ISourcePositionHolder>$.setPosition(support.union($1, $3));
                }

qsymbols        : tQSYMBOLS_BEG ' ' tSTRING_END {
                    $$ = new ZArrayNode(support.union($1, $3));
                }
                | tQSYMBOLS_BEG qsym_list tSTRING_END {
                    $$ = $2;
                    $<ISourcePositionHolder>$.setPosition(support.union($1, $3));
                }


qword_list      : /* none */ {
                    $$ = new ArrayNode(lexer.getPosition());
                }
                | qword_list tSTRING_CONTENT ' ' {
                    $$ = $1.add($2);
                }

qsym_list      : /* none */ {
                    $$ = new ArrayNode(lexer.getPosition());
                }
                | qsym_list tSTRING_CONTENT ' ' {
                    $$ = $1.add(support.asSymbol($1.getPosition(), $2));
                }

string_contents : /* none */ {
                    $$ = new StrNode($<Token>0.getPosition(), "");
                }
                | string_contents string_content {
                    $$ = support.literal_concat($1.getPosition(), $1, $2);
                }

xstring_contents: /* none */ {
                    $$ = null;
                }
                | xstring_contents string_content {
                    $$ = support.literal_concat(support.getPosition($1), $1, $2);
                }

regexp_contents :  /* none */ {
                    $$ = null;
                }
                | regexp_contents string_content {
    // FIXME: mri is different here.
                    $$ = support.literal_concat(support.getPosition($1), $1, $2);
                }

string_content  : tSTRING_CONTENT {
                    $$ = $1;
                }
                | tSTRING_DVAR {
                    $$ = lexer.getStrTerm();
                    lexer.setStrTerm(null);
                    lexer.setState(LexState.EXPR_BEG);
                } string_dvar {
                    lexer.setStrTerm($<StrTerm>2);
                    $$ = new EvStrNode(support.union($1, $3), $3);
                }
                | tSTRING_DBEG {
                   $$ = lexer.getStrTerm();
                   lexer.getConditionState().stop();
                   lexer.getCmdArgumentState().stop();
                   lexer.setStrTerm(null);
                   lexer.setState(LexState.EXPR_BEG);
                } compstmt tRCURLY {
                   lexer.getConditionState().restart();
                   lexer.getCmdArgumentState().restart();
                   lexer.setStrTerm($<StrTerm>2);

                   $$ = support.newEvStrNode(support.union($1, $3), $3);
                }

string_dvar     : tGVAR {
                     $$ = new GlobalVarNode($1.getPosition(), (String) $1.getValue());
                }
                | tIVAR {
                     $$ = new InstVarNode($1.getPosition(), (String) $1.getValue());
                }
                | tCVAR {
                     $$ = new ClassVarNode($1.getPosition(), (String) $1.getValue());
                }
                | backref

// Token:symbol
symbol          : tSYMBEG sym {
                     lexer.setState(LexState.EXPR_END);
                     $$ = $2;
                     $<ISourcePositionHolder>$.setPosition(support.union($1, $2));
                }

// Token:symbol
sym             : fname | tIVAR | tGVAR | tCVAR

dsym            : tSYMBEG xstring_contents tSTRING_END {
                     lexer.setState(LexState.EXPR_END);

                     // DStrNode: :"some text #{some expression}"
                     // StrNode: :"some text"
                     // EvStrNode :"#{some expression}"
                     // Ruby 1.9 allows empty strings as symbols
                     if ($2 == null) {
                         $$ = new SymbolNode($1.getPosition(), "");
                     } else if ($2 instanceof DStrNode) {
                         $$ = new DSymbolNode(support.union($1, $3), $<DStrNode>2);
                     } else if ($2 instanceof StrNode) {
                         $$ = new SymbolNode(support.union($1, $3), $<StrNode>2.getValue().toString().intern());
                     } else {
                         SourcePosition position = support.union($2, $3);

                         // We substract one since tsymbeg is longer than one
                         // and we cannot union it directly so we assume quote
                         // is one character long and subtract for it.
                         position.adjustStartOffset(-1);
                         $2.setPosition(position);

                         $$ = new DSymbolNode(support.union($1, $3));
                         $<DSymbolNode>$.add($2);
                     }
                }

numeric         : tINTEGER {
                    $$ = $1;
                }
                | tFLOAT {
                     $$ = $1;
                }
                | tUMINUS_NUM tINTEGER %prec tLOWEST {
                     $$ = support.negateInteger($2);
                }
                | tUMINUS_NUM tFLOAT %prec tLOWEST {
                     $$ = support.negateFloat($2);
                }

// [!null]
user_variable   : tIDENTIFIER | tIVAR | tGVAR | tCONSTANT | tCVAR

keyword_variable : kNIL { 
                    $$ = new Token("nil", Tokens.kNIL, $1.getPosition());
                }
                | kSELF {
                    $$ = new Token("self", Tokens.kSELF, $1.getPosition());
                }
                | kTRUE { 
                    $$ = new Token("true", Tokens.kTRUE, $1.getPosition());
                }
                | kFALSE {
                    $$ = new Token("false", Tokens.kFALSE, $1.getPosition());
                }
                | k__FILE__ {
                    $$ = new Token("__FILE__", Tokens.k__FILE__, $1.getPosition());
                }
                | k__LINE__ {
                    $$ = new Token("__LINE__", Tokens.k__LINE__, $1.getPosition());
                }
                | k__ENCODING__ {
                    $$ = new Token("__ENCODING__", Tokens.k__ENCODING__, $1.getPosition());
                }

// [!null]
var_ref         : user_variable {
                    $$ = support.gettable($1);
                }
                | keyword_variable {
                    $$ = support.gettable($1);
                }

// [!null]
var_lhs         : user_variable {
                    $$ = support.assignable($1, null);
                }
                | keyword_variable {
                    $$ = support.assignable($1, null);
                }

// [!null]
backref         : tNTH_REF {
                    $$ = $1;
                }
                | tBACK_REF {
                    $$ = $1;
                }

superclass      : term {
                    $$ = null;
                }
                | tLT {
                   lexer.setState(LexState.EXPR_BEG);
                } expr_value term {
                    $$ = $3;
                }
                | error term {
                   $$ = null;
                }

// [!null]
// ENEBO: Look at command_start stuff I am ripping out
f_arglist       : tLPAREN2 f_args rparen {
                    $$ = $2;
                    $<ISourcePositionHolder>$.setPosition(support.union($1, $3));
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = true;
                }
                | f_args term {
                    $$ = $1;
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = true;
                }


args_tail       : f_kwarg ',' f_kwrest opt_f_block_arg {
                    $$ = support.new_args_tail(support.union($1, $4), $1, $3, $4);
                }
                | f_kwarg opt_f_block_arg {
                    $$ = support.new_args_tail(support.union($1, $2), $1, null, $2);
                }
                | f_kwrest opt_f_block_arg {
                    $$ = support.new_args_tail(support.union($1, $2), null, $1, $2);
                }
                | f_block_arg {
                    $$ = support.new_args_tail($1.getPosition(), null, null, $1);
                }

opt_args_tail   : ',' args_tail {
                    $$ = $2;
                }
                | /* none */ {
                    $$ = support.new_args_tail(lexer.getPosition(), null, null, null);
                }

// [!null]
f_args          : f_arg ',' f_optarg ',' f_rest_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $6), $1, $3, $5, null, $6);
                }
                | f_arg ',' f_optarg ',' f_rest_arg ',' f_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $8), $1, $3, $5, $7, $8);
                }
                | f_arg ',' f_optarg opt_args_tail {
                    $$ = support.new_args(support.union($1, $4), $1, $3, null, null, $4);
                }
                | f_arg ',' f_optarg ',' f_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $6), $1, $3, null, $5, $6);
                }
                | f_arg ',' f_rest_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $4), $1, null, $3, null, $4);
                }
                | f_arg ',' f_rest_arg ',' f_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $6), $1, null, $3, $5, $6);
                }
                | f_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $2), $1, null, null, null, $2);
                }
                | f_optarg ',' f_rest_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $4), null, $1, $3, null, $4);
                }
                | f_optarg ',' f_rest_arg ',' f_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $6), null, $1, $3, $5, $6);
                }
                | f_optarg opt_args_tail {
                    $$ = support.new_args(support.union($1, $2), null, $1, null, null, $2);
                }
                | f_optarg ',' f_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $4), null, $1, null, $3, $4);
                }
                | f_rest_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $2), null, null, $1, null, $2);
                }
                | f_rest_arg ',' f_arg opt_args_tail {
                    $$ = support.new_args(support.union($1, $4), null, null, $1, $3, $4);
                }
                | args_tail {
                    $$ = support.new_args($1.getPosition(), null, null, null, null, $1);
                }
                | /* none */ {
                    $$ = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
                }

f_bad_arg       : tCONSTANT {
                    support.yyerror("formal argument cannot be a constant");
                }
                | tIVAR {
                    support.yyerror("formal argument cannot be an instance variable");
                }
                | tGVAR {
                    support.yyerror("formal argument cannot be a global variable");
                }
                | tCVAR {
                    support.yyerror("formal argument cannot be a class variable");
                }

// Token:f_norm_arg [!null]
f_norm_arg      : f_bad_arg
                | tIDENTIFIER {
                    $$ = support.formal_argument($1);
                }

f_arg_item      : f_norm_arg {
                    $$ = support.arg_var($1);
  /*
                    $$ = new ArgAuxiliaryNode($1.getPosition(), (String) $1.getValue(), 1);
  */
                }
                | tLPAREN f_margs rparen {
                    $$ = $2;
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
                }

// [!null]
f_arg           : f_arg_item {
                    $$ = new ArrayNode($1.getPosition(), $1);
                }
                | f_arg ',' f_arg_item {
                    $1.add($3);
                    $$ = $1;
                }

f_kw            : tLABEL arg_value {
                    support.arg_var(support.formal_argument($1));
                    $$ = support.keyword_arg(support.union($1, $2), support.assignable($1, $2));
                }

f_block_kw      : tLABEL primary_value {
                    support.arg_var(support.formal_argument($1));
                    $$ = support.keyword_arg(support.union($1, $2), support.assignable($1, $2));
                }

f_block_kwarg   : f_block_kw {
                    $$ = new ArrayNode($1.getPosition(), $1);
                }
                | f_block_kwarg ',' f_block_kw {
                    $$ = $1.add($3);
                }

f_kwarg         : f_kw {
                    $$ = new ArrayNode($1.getPosition(), $1);
                }
                | f_kwarg ',' f_kw {
                    $$ = $1.add($3);
                }

kwrest_mark     : tPOW {
                    $$ = $1;
                }
                | tDSTAR {
                    $$ = $1;
                }

f_kwrest        : kwrest_mark tIDENTIFIER {
                    $$ = $2;
                }

f_opt           : tIDENTIFIER '=' arg_value {
                    support.arg_var(support.formal_argument($1));
                    $$ = new OptArgNode(support.union($1, $3), support.assignable($1, $3));
                }

f_block_opt     : tIDENTIFIER '=' primary_value {
                    support.arg_var(support.formal_argument($1));
                    $$ = new OptArgNode(support.union($1, $3), support.assignable($1, $3));
                }

f_block_optarg  : f_block_opt {
                    $$ = new BlockNode($1.getPosition()).add($1);
                }
                | f_block_optarg ',' f_block_opt {
                    $$ = support.appendToBlock($1, $3);
                }

f_optarg        : f_opt {
                    $$ = new BlockNode($1.getPosition()).add($1);
                }
                | f_optarg ',' f_opt {
                    $$ = support.appendToBlock($1, $3);
                }

restarg_mark    : tSTAR2 | tSTAR

// [!null]
f_rest_arg      : restarg_mark tIDENTIFIER {
                    if (!support.is_local_id($2)) {
                        support.yyerror("rest argument must be local variable");
                    }
                    
                    $$ = new RestArgNode(support.union($1, $2), support.arg_var(support.shadowing_lvar($2)));
                }
                | restarg_mark {
                    $$ = new UnnamedRestArgNode($1.getPosition(), "", support.getCurrentScope().addVariable("*"));
                }

// [!null]
blkarg_mark     : tAMPER2 | tAMPER

// f_block_arg - Block argument def for function (foo(&block)) [!null]
f_block_arg     : blkarg_mark tIDENTIFIER {
                    if (!support.is_local_id($2)) {
                        support.yyerror("block argument must be local variable");
                    }
                    
                    $$ = new BlockArgNode(support.union($1, $2), support.arg_var(support.shadowing_lvar($2)));
                }

opt_f_block_arg : ',' f_block_arg {
                    $$ = $2;
                }
                | /* none */ {
                    $$ = null;
                }

singleton       : var_ref {
                    if (!($1 instanceof SelfNode)) {
                        support.checkExpression($1);
                    }
                    $$ = $1;
                }
                | tLPAREN2 {
                    lexer.setState(LexState.EXPR_BEG);
                } expr rparen {
                    if ($3 == null) {
                        support.yyerror("can't define single method for ().");
                    } else if ($3 instanceof ILiteralNode) {
                        support.yyerror("can't define single method for literals.");
                    }
                    support.checkExpression($3);
                    $$ = $3;
                }

// [!null]
assoc_list      : none {
                    $$ = new ArrayNode(lexer.getPosition());
                }
                | assocs trailer {
                    $$ = $1;
                }

// [!null]
assocs          : assoc
                | assocs ',' assoc {
                    $$ = $1.addAll($3);
                }

// [!null]
assoc           : arg_value tASSOC arg_value {
                    SourcePosition pos;
                    if ($1 == null && $3 == null) {
                        pos = $2.getPosition();
                    } else {
                        pos = $1.getPosition();
                    }

                    $$ = support.newArrayNode(pos, $1).add($3);
                }
                | tLABEL arg_value {
                    SourcePosition pos = $1.getPosition();
                    $$ = support.newArrayNode(pos, new SymbolNode(pos, (String) $1.getValue())).add($2);
                }
                | tDSTAR arg_value {
                    $$ = support.newArrayNode($1.getPosition(), $2).add(null);
                }

operation       : tIDENTIFIER | tCONSTANT | tFID
operation2      : tIDENTIFIER | tCONSTANT | tFID | op
operation3      : tIDENTIFIER | tFID | op
dot_or_colon    : tDOT | tCOLON2
opt_terms       : /* none */ | terms
opt_nl          : /* none */ | '\n'
rparen          : opt_nl tRPAREN {
                    $$ = $2;
                }
rbracket        : opt_nl tRBRACK {
                    $$ = $2;
                }
trailer         : /* none */ | '\n' | ','

term            : ';'
                | '\n'

terms           : term
                | terms ';'

none            : /* none */ {
                      $$ = null;
                }

none_block_pass : /* none */ {  
                  $$ = null;
                }

%%

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
