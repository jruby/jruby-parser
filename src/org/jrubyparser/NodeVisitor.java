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
import org.jrubyparser.ast.InstAsgnNode;
import org.jrubyparser.ast.InstVarNode;
import org.jrubyparser.ast.IterNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.LiteralNode;
import org.jrubyparser.ast.LocalAsgnNode;
import org.jrubyparser.ast.LocalVarNode;
import org.jrubyparser.ast.Match2Node;
import org.jrubyparser.ast.Match3Node;
import org.jrubyparser.ast.MatchNode;
import org.jrubyparser.ast.ModuleNode;
import org.jrubyparser.ast.MultipleAsgn19Node;
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
import org.jrubyparser.ast.ToAryNode;
import org.jrubyparser.ast.TrueNode;
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

public interface NodeVisitor {
    public Object visitAliasNode(AliasNode iVisited);
    public Object visitAndNode(AndNode iVisited);
    public Object visitArgsNode(ArgsNode iVisited);
    public Object visitArgsCatNode(ArgsCatNode iVisited);
    public Object visitArgsPushNode(ArgsPushNode iVisited);
    public Object visitArgumentNode(ArgumentNode iVisited);
    public Object visitArrayNode(ArrayNode iVisited);
    public Object visitAttrAssignNode(AttrAssignNode iVisited);
    public Object visitBackRefNode(BackRefNode iVisited);
    public Object visitBeginNode(BeginNode iVisited);
    public Object visitBignumNode(BignumNode iVisited);
    public Object visitBlockArgNode(BlockArgNode iVisited);
    public Object visitBlockArg18Node(BlockArg18Node iVisited);
    public Object visitBlockNode(BlockNode iVisited);
    public Object visitBlockPassNode(BlockPassNode iVisited);
    public Object visitBreakNode(BreakNode iVisited);
    public Object visitConstDeclNode(ConstDeclNode iVisited);
    public Object visitClassVarAsgnNode(ClassVarAsgnNode iVisited);
    public Object visitClassVarDeclNode(ClassVarDeclNode iVisited);
    public Object visitClassVarNode(ClassVarNode iVisited);
    public Object visitCallNode(CallNode iVisited);
    public Object visitCaseNode(CaseNode iVisited);
    public Object visitClassNode(ClassNode iVisited);
    public Object visitColon2Node(Colon2Node iVisited);
    public Object visitColon3Node(Colon3Node iVisited);
    public Object visitConstNode(ConstNode iVisited);
    public Object visitDAsgnNode(DAsgnNode iVisited);
    public Object visitDRegxNode(DRegexpNode iVisited);
    public Object visitDStrNode(DStrNode iVisited);
    public Object visitDSymbolNode(DSymbolNode iVisited);
    public Object visitDVarNode(DVarNode iVisited);
    public Object visitDXStrNode(DXStrNode iVisited);
    public Object visitDefinedNode(DefinedNode iVisited);
    public Object visitDefnNode(DefnNode iVisited);
    public Object visitDefsNode(DefsNode iVisited);
    public Object visitDotNode(DotNode iVisited);
    public Object visitEncodingNode(EncodingNode iVisited);
    public Object visitEnsureNode(EnsureNode iVisited);
    public Object visitEvStrNode(EvStrNode iVisited);
    public Object visitFCallNode(FCallNode iVisited);
    public Object visitFalseNode(FalseNode iVisited);
    public Object visitFixnumNode(FixnumNode iVisited);
    public Object visitFlipNode(FlipNode iVisited);
    public Object visitFloatNode(FloatNode iVisited);
    public Object visitForNode(ForNode iVisited);
    public Object visitGlobalAsgnNode(GlobalAsgnNode iVisited);
    public Object visitGlobalVarNode(GlobalVarNode iVisited);
    public Object visitHashNode(HashNode iVisited);
    public Object visitInstAsgnNode(InstAsgnNode iVisited);
    public Object visitInstVarNode(InstVarNode iVisited);
    public Object visitIfNode(IfNode iVisited);
    public Object visitIterNode(IterNode iVisited);
    public Object visitListNode(ListNode iVisited);
    public Object visitLiteralNode(LiteralNode iVisited);
    public Object visitLocalAsgnNode(LocalAsgnNode iVisited);
    public Object visitLocalVarNode(LocalVarNode iVisited);
    public Object visitMultipleAsgnNode(MultipleAsgnNode iVisited);
    public Object visitMultipleAsgnNode(MultipleAsgn19Node iVisited);
    public Object visitMatch2Node(Match2Node iVisited);
    public Object visitMatch3Node(Match3Node iVisited);
    public Object visitMatchNode(MatchNode iVisited);
    public Object visitModuleNode(ModuleNode iVisited);
    public Object visitNewlineNode(NewlineNode iVisited);
    public Object visitNextNode(NextNode iVisited);
    public Object visitNilNode(NilNode iVisited);
    public Object visitNotNode(NotNode iVisited);
    public Object visitNthRefNode(NthRefNode iVisited);
    public Object visitOpElementAsgnNode(OpElementAsgnNode iVisited);
    public Object visitOpAsgnNode(OpAsgnNode iVisited);
    public Object visitOpAsgnAndNode(OpAsgnAndNode iVisited);
    public Object visitOpAsgnOrNode(OpAsgnOrNode iVisited);
    public Object visitOptArgNode(OptArgNode iVisited);
    public Object visitOrNode(OrNode iVisited);
    public Object visitPreExeNode(PreExeNode iVisited);
    public Object visitPostExeNode(PostExeNode iVisited);
    public Object visitRedoNode(RedoNode iVisited);
    public Object visitRegexpNode(RegexpNode iVisited);
    public Object visitRescueBodyNode(RescueBodyNode iVisited);
    public Object visitRescueNode(RescueNode iVisited);
    public Object visitRestArgNode(RestArgNode iVisited);
    public Object visitRetryNode(RetryNode iVisited);
    public Object visitReturnNode(ReturnNode iVisited);
    public Object visitRootNode(RootNode iVisited);
    public Object visitSClassNode(SClassNode iVisited);
    public Object visitSelfNode(SelfNode iVisited);
    public Object visitSplatNode(SplatNode iVisited);
    public Object visitStrNode(StrNode iVisited);
    public Object visitSuperNode(SuperNode iVisited);
    public Object visitSValueNode(SValueNode iVisited);
    public Object visitSymbolNode(SymbolNode iVisited);
    public Object visitToAryNode(ToAryNode iVisited);
    public Object visitTrueNode(TrueNode iVisited);
    public Object visitUndefNode(UndefNode iVisited);
    public Object visitUntilNode(UntilNode iVisited);
    public Object visitVAliasNode(VAliasNode iVisited);
    public Object visitVCallNode(VCallNode iVisited);
    public Object visitWhenNode(WhenNode iVisited);
    public Object visitWhileNode(WhileNode iVisited);
    public Object visitXStrNode(XStrNode iVisited);
    public Object visitYieldNode(YieldNode iVisited);
    public Object visitZArrayNode(ZArrayNode iVisited);
    public Object visitZSuperNode(ZSuperNode iVisited);
}
