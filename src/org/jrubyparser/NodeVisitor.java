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
 * Copyright (C) 2009 Thomas E Enebo <tom.enebo@gmail.com>
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
package org.jrubyparser;

import org.jrubyparser.ast.AliasNode;
import org.jrubyparser.ast.AndNode;
import org.jrubyparser.ast.ArgsCatNode;
import org.jrubyparser.ast.ArgsNode;
import org.jrubyparser.ast.ArgsPushNode;
import org.jrubyparser.ast.ArgumentNode;
import org.jrubyparser.ast.ArrayNode;
import org.jrubyparser.ast.AttrAssignNode;
import org.jrubyparser.ast.BackRefNode;
import org.jrubyparser.ast.BeginNode;
import org.jrubyparser.ast.BignumNode;
import org.jrubyparser.ast.BlockArg18Node;
import org.jrubyparser.ast.BlockArgNode;
import org.jrubyparser.ast.BlockNode;
import org.jrubyparser.ast.BlockPassNode;
import org.jrubyparser.ast.BreakNode;
import org.jrubyparser.ast.CallNode;
import org.jrubyparser.ast.CaseNode;
import org.jrubyparser.ast.ClassNode;
import org.jrubyparser.ast.ClassVarAsgnNode;
import org.jrubyparser.ast.ClassVarDeclNode;
import org.jrubyparser.ast.ClassVarNode;
import org.jrubyparser.ast.Colon2Node;
import org.jrubyparser.ast.Colon3Node;
import org.jrubyparser.ast.CommentNode;
import org.jrubyparser.ast.ComplexNode;
import org.jrubyparser.ast.ConstDeclNode;
import org.jrubyparser.ast.ConstNode;
import org.jrubyparser.ast.DAsgnNode;
import org.jrubyparser.ast.DRegexpNode;
import org.jrubyparser.ast.DStrNode;
import org.jrubyparser.ast.DSymbolNode;
import org.jrubyparser.ast.DVarNode;
import org.jrubyparser.ast.DXStrNode;
import org.jrubyparser.ast.DefinedNode;
import org.jrubyparser.ast.DefnNode;
import org.jrubyparser.ast.DefsNode;
import org.jrubyparser.ast.DotNode;
import org.jrubyparser.ast.EncodingNode;
import org.jrubyparser.ast.EnsureNode;
import org.jrubyparser.ast.EvStrNode;
import org.jrubyparser.ast.FCallNode;
import org.jrubyparser.ast.FalseNode;
import org.jrubyparser.ast.FixnumNode;
import org.jrubyparser.ast.FlipNode;
import org.jrubyparser.ast.FloatNode;
import org.jrubyparser.ast.ForNode;
import org.jrubyparser.ast.GlobalAsgnNode;
import org.jrubyparser.ast.GlobalVarNode;
import org.jrubyparser.ast.HashNode;
import org.jrubyparser.ast.IfNode;
import org.jrubyparser.ast.ImplicitNilNode;
import org.jrubyparser.ast.InstAsgnNode;
import org.jrubyparser.ast.InstVarNode;
import org.jrubyparser.ast.IterNode;
import org.jrubyparser.ast.KeywordArgNode;
import org.jrubyparser.ast.KeywordRestArgNode;
import org.jrubyparser.ast.LambdaNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.LiteralNode;
import org.jrubyparser.ast.LocalAsgnNode;
import org.jrubyparser.ast.LocalVarNode;
import org.jrubyparser.ast.Match2Node;
import org.jrubyparser.ast.Match3Node;
import org.jrubyparser.ast.MatchNode;
import org.jrubyparser.ast.MethodNameNode;
import org.jrubyparser.ast.ModuleNode;
import org.jrubyparser.ast.MultipleAsgnNode;
import org.jrubyparser.ast.NewlineNode;
import org.jrubyparser.ast.NextNode;
import org.jrubyparser.ast.NilNode;
import org.jrubyparser.ast.NotNode;
import org.jrubyparser.ast.NthRefNode;
import org.jrubyparser.ast.OpAsgnAndNode;
import org.jrubyparser.ast.OpAsgnNode;
import org.jrubyparser.ast.OpAsgnOrNode;
import org.jrubyparser.ast.OpElementAsgnNode;
import org.jrubyparser.ast.OptArgNode;
import org.jrubyparser.ast.OrNode;
import org.jrubyparser.ast.PostExeNode;
import org.jrubyparser.ast.PreExeNode;
import org.jrubyparser.ast.RationalNode;
import org.jrubyparser.ast.RedoNode;
import org.jrubyparser.ast.RegexpNode;
import org.jrubyparser.ast.RescueBodyNode;
import org.jrubyparser.ast.RescueNode;
import org.jrubyparser.ast.RestArgNode;
import org.jrubyparser.ast.RetryNode;
import org.jrubyparser.ast.ReturnNode;
import org.jrubyparser.ast.RootNode;
import org.jrubyparser.ast.SClassNode;
import org.jrubyparser.ast.SValueNode;
import org.jrubyparser.ast.SelfNode;
import org.jrubyparser.ast.SplatNode;
import org.jrubyparser.ast.StrNode;
import org.jrubyparser.ast.SuperNode;
import org.jrubyparser.ast.SymbolNode;
import org.jrubyparser.ast.SyntaxNode;
import org.jrubyparser.ast.ToAryNode;
import org.jrubyparser.ast.TrueNode;
import org.jrubyparser.ast.UnaryCallNode;
import org.jrubyparser.ast.UndefNode;
import org.jrubyparser.ast.UntilNode;
import org.jrubyparser.ast.VAliasNode;
import org.jrubyparser.ast.VCallNode;
import org.jrubyparser.ast.WhenNode;
import org.jrubyparser.ast.WhileNode;
import org.jrubyparser.ast.XStrNode;
import org.jrubyparser.ast.YieldNode;
import org.jrubyparser.ast.ZArrayNode;
import org.jrubyparser.ast.ZSuperNode;

public interface NodeVisitor<T> {
  public T visitAliasNode(AliasNode iVisited);

  public T visitAndNode(AndNode iVisited);

  public T visitArgsNode(ArgsNode iVisited);

  public T visitArgsCatNode(ArgsCatNode iVisited);

  public T visitArgsPushNode(ArgsPushNode iVisited);

  public T visitArgumentNode(ArgumentNode iVisited);

  public T visitArrayNode(ArrayNode iVisited);

  public T visitAttrAssignNode(AttrAssignNode iVisited);

  public T visitBackRefNode(BackRefNode iVisited);

  public T visitBeginNode(BeginNode iVisited);

  public T visitBignumNode(BignumNode iVisited);

  public T visitBlockArgNode(BlockArgNode iVisited);

  public T visitBlockArg18Node(BlockArg18Node iVisited);

  public T visitBlockNode(BlockNode iVisited);

  public T visitBlockPassNode(BlockPassNode iVisited);

  public T visitBreakNode(BreakNode iVisited);

  public T visitConstDeclNode(ConstDeclNode iVisited);

  public T visitClassVarAsgnNode(ClassVarAsgnNode iVisited);

  public T visitClassVarDeclNode(ClassVarDeclNode iVisited);

  public T visitClassVarNode(ClassVarNode iVisited);

  public T visitCallNode(CallNode iVisited);

  public T visitCaseNode(CaseNode iVisited);

  public T visitClassNode(ClassNode iVisited);

  public T visitCommentNode(CommentNode iVisited);

  public T visitColon2Node(Colon2Node iVisited);

  public T visitColon3Node(Colon3Node iVisited);

  public T visitConstNode(ConstNode iVisited);

  public T visitDAsgnNode(DAsgnNode iVisited);

  public T visitDRegxNode(DRegexpNode iVisited);

  public T visitDStrNode(DStrNode iVisited);

  public T visitDSymbolNode(DSymbolNode iVisited);

  public T visitDVarNode(DVarNode iVisited);

  public T visitDXStrNode(DXStrNode iVisited);

  public T visitDefinedNode(DefinedNode iVisited);

  public T visitDefnNode(DefnNode iVisited);

  public T visitDefsNode(DefsNode iVisited);

  public T visitDotNode(DotNode iVisited);

  public T visitEncodingNode(EncodingNode iVisited);

  public T visitEnsureNode(EnsureNode iVisited);

  public T visitEvStrNode(EvStrNode iVisited);

  public T visitFCallNode(FCallNode iVisited);

  public T visitFalseNode(FalseNode iVisited);

  public T visitFixnumNode(FixnumNode iVisited);

  public T visitFlipNode(FlipNode iVisited);

  public T visitFloatNode(FloatNode iVisited);

  public T visitForNode(ForNode iVisited);

  public T visitGlobalAsgnNode(GlobalAsgnNode iVisited);

  public T visitGlobalVarNode(GlobalVarNode iVisited);

  public T visitHashNode(HashNode iVisited);

  public T visitInstAsgnNode(InstAsgnNode iVisited);

  public T visitInstVarNode(InstVarNode iVisited);

  public T visitIfNode(IfNode iVisited);

  public T visitImplicitNilNode(ImplicitNilNode visited);

  public T visitIterNode(IterNode iVisited);

  public T visitKeywordArgNode(KeywordArgNode iVisited);

  public T visitKeywordRestArgNode(KeywordRestArgNode iVisited);

  public T visitLambdaNode(LambdaNode visited);

  public T visitListNode(ListNode iVisited);

  public T visitLiteralNode(LiteralNode iVisited);

  public T visitLocalAsgnNode(LocalAsgnNode iVisited);

  public T visitLocalVarNode(LocalVarNode iVisited);

  public T visitMultipleAsgnNode(MultipleAsgnNode iVisited);

  public T visitMatch2Node(Match2Node iVisited);

  public T visitMatch3Node(Match3Node iVisited);

  public T visitMatchNode(MatchNode iVisited);

  public T visitMethodNameNode(MethodNameNode iVisited);

  public T visitModuleNode(ModuleNode iVisited);

  public T visitNewlineNode(NewlineNode iVisited);

  public T visitNextNode(NextNode iVisited);

  public T visitNilNode(NilNode iVisited);

  public T visitNotNode(NotNode iVisited);

  public T visitNthRefNode(NthRefNode iVisited);

  public T visitOpElementAsgnNode(OpElementAsgnNode iVisited);

  public T visitOpAsgnNode(OpAsgnNode iVisited);

  public T visitOpAsgnAndNode(OpAsgnAndNode iVisited);

  public T visitOpAsgnOrNode(OpAsgnOrNode iVisited);

  public T visitOptArgNode(OptArgNode iVisited);

  public T visitOrNode(OrNode iVisited);

  public T visitPreExeNode(PreExeNode iVisited);

  public T visitPostExeNode(PostExeNode iVisited);

  public T visitRedoNode(RedoNode iVisited);

  public T visitRegexpNode(RegexpNode iVisited);

  public T visitRescueBodyNode(RescueBodyNode iVisited);

  public T visitRescueNode(RescueNode iVisited);

  public T visitRestArgNode(RestArgNode iVisited);

  public T visitRetryNode(RetryNode iVisited);

  public T visitReturnNode(ReturnNode iVisited);

  public T visitRootNode(RootNode iVisited);

  public T visitSClassNode(SClassNode iVisited);

  public T visitSelfNode(SelfNode iVisited);

  public T visitSplatNode(SplatNode iVisited);

  public T visitStrNode(StrNode iVisited);

  public T visitSuperNode(SuperNode iVisited);

  public T visitSValueNode(SValueNode iVisited);

  public T visitSymbolNode(SymbolNode iVisited);

  public T visitSyntaxNode(SyntaxNode iVisited);

  public T visitToAryNode(ToAryNode iVisited);

  public T visitTrueNode(TrueNode iVisited);

  public T visitUndefNode(UndefNode iVisited);

  public T visitUnaryCallNode(UnaryCallNode iVisited);

  public T visitUntilNode(UntilNode iVisited);

  public T visitVAliasNode(VAliasNode iVisited);

  public T visitVCallNode(VCallNode iVisited);

  public T visitWhenNode(WhenNode iVisited);

  public T visitWhileNode(WhileNode iVisited);

  public T visitXStrNode(XStrNode iVisited);

  public T visitYieldNode(YieldNode iVisited);

  public T visitZArrayNode(ZArrayNode iVisited);

  public T visitZSuperNode(ZSuperNode iVisited);
}
