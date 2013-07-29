/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.util;

import org.jrubyparser.NodeVisitor;
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
import org.jrubyparser.ast.Node;
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
import org.jrubyparser.ast.SyntaxNode;
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

/**
 * A base class visitor where visiting nodes will do nothing (no-op) by default.
 */
public class NoopVisitor implements NodeVisitor {
    
    protected Object visit(Node parent) {
        if (parent == null) return null;
        
        for (Node node: parent.childNodes()) {
            node.accept(this);
        }
        
        return null;
    }
    
    public Object visitAliasNode(AliasNode iVisited) {
        return visit(iVisited);
    }

    public Object visitAndNode(AndNode iVisited) {
        return visit(iVisited);
    }

    public Object visitArgsNode(ArgsNode iVisited) {
        return visit(iVisited);
    }

    public Object visitArgsCatNode(ArgsCatNode iVisited) {
        return visit(iVisited);
    }

    public Object visitArgsPushNode(ArgsPushNode iVisited) {
        return visit(iVisited);
    }

    public Object visitArrayNode(ArrayNode iVisited) {
        return visit(iVisited);
    }
    
    public Object visitArgumentNode(ArgumentNode iVisited) {
        return visit(iVisited);
    }    

    public Object visitAttrAssignNode(AttrAssignNode iVisited) {
        return visit(iVisited);
    }

    public Object visitBackRefNode(BackRefNode iVisited) {
        return visit(iVisited);
    }

    public Object visitBeginNode(BeginNode iVisited) {
        return visit(iVisited);
    }

    public Object visitBignumNode(BignumNode iVisited) {
        return visit(iVisited);
    }

    public Object visitBlockArgNode(BlockArgNode iVisited) {
        return visit(iVisited);
    }

    public Object visitBlockArg18Node(BlockArg18Node iVisited) {
        return visit(iVisited);
    }

    public Object visitBlockNode(BlockNode iVisited) {
        return visit(iVisited);
    }

    public Object visitBlockPassNode(BlockPassNode iVisited) {
        return visit(iVisited);
    }

    public Object visitBreakNode(BreakNode iVisited) {
        return visit(iVisited);
    }

    public Object visitConstDeclNode(ConstDeclNode iVisited) {
        return visit(iVisited);
    }

    public Object visitClassVarAsgnNode(ClassVarAsgnNode iVisited) {
        return visit(iVisited);
    }

    public Object visitClassVarDeclNode(ClassVarDeclNode iVisited) {
        return visit(iVisited);
    }

    public Object visitClassVarNode(ClassVarNode iVisited) {
        return visit(iVisited);
    }

    public Object visitCallNode(CallNode iVisited) {
        return visit(iVisited);
    }

    public Object visitCaseNode(CaseNode iVisited) {
        return visit(iVisited);
    }

    public Object visitClassNode(ClassNode iVisited) {
        return visit(iVisited);
    }

    public Object visitColon2Node(Colon2Node iVisited) {
        return visit(iVisited);
    }

    public Object visitColon3Node(Colon3Node iVisited) {
        return visit(iVisited);
    }
    
    public Object visitCommentNode(CommentNode iVisited) {
        return visit(iVisited);
    }

    public Object visitConstNode(ConstNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDAsgnNode(DAsgnNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDRegxNode(DRegexpNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDStrNode(DStrNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDSymbolNode(DSymbolNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDVarNode(DVarNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDXStrNode(DXStrNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDefinedNode(DefinedNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDefnNode(DefnNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDefsNode(DefsNode iVisited) {
        return visit(iVisited);
    }

    public Object visitDotNode(DotNode iVisited) {
        return visit(iVisited);
    }

    public Object visitEncodingNode(EncodingNode iVisited) {
        return visit(iVisited);
    }

    public Object visitEnsureNode(EnsureNode iVisited) {
        return visit(iVisited);
    }

    public Object visitEvStrNode(EvStrNode iVisited) {
        return visit(iVisited);
    }

    public Object visitFCallNode(FCallNode iVisited) {
        return visit(iVisited);
    }

    public Object visitFalseNode(FalseNode iVisited) {
        return visit(iVisited);
    }

    public Object visitFixnumNode(FixnumNode iVisited) {
        return visit(iVisited);
    }

    public Object visitFlipNode(FlipNode iVisited) {
        return visit(iVisited);
    }

    public Object visitFloatNode(FloatNode iVisited) {
        return visit(iVisited);
    }

    public Object visitForNode(ForNode iVisited) {
        return visit(iVisited);
    }

    public Object visitGlobalAsgnNode(GlobalAsgnNode iVisited) {
        return visit(iVisited);
    }

    public Object visitGlobalVarNode(GlobalVarNode iVisited) {
        return visit(iVisited);
    }

    public Object visitHashNode(HashNode iVisited) {
        return visit(iVisited);
    }
    
    public Object visitImplicitNilNode(ImplicitNilNode visited) {
        return visit(visited);
    }
    
    public Object visitInstAsgnNode(InstAsgnNode iVisited) {
        return visit(iVisited);
    }

    public Object visitInstVarNode(InstVarNode iVisited) {
        return visit(iVisited);
    }

    public Object visitIfNode(IfNode iVisited) {
        return visit(iVisited);
    }

    public Object visitIterNode(IterNode iVisited) {
        return visit(iVisited);
    }
    
    public Object visitKeywordArgNode(KeywordArgNode iVisited) {
        return visit(iVisited);
    }

    public Object visitKeywordRestArgNode(KeywordRestArgNode iVisited) {
        return visit(iVisited);
    }
    
    public Object visitLambdaNode(LambdaNode visited) {
        return visit(visited);
    }
    
    public Object visitListNode(ListNode iVisited) {
        return visit(iVisited);
    }
    
    public Object visitLiteralNode(LiteralNode iVisited) {
        return visit(iVisited);
    }

    public Object visitLocalAsgnNode(LocalAsgnNode iVisited) {
        return visit(iVisited);
    }

    public Object visitLocalVarNode(LocalVarNode iVisited) {
        return visit(iVisited);
    }

    public Object visitMultipleAsgnNode(MultipleAsgnNode iVisited) {
        return visit(iVisited);
    }

    public Object visitMatch2Node(Match2Node iVisited) {
        return visit(iVisited);
    }

    public Object visitMatch3Node(Match3Node iVisited) {
        return visit(iVisited);
    }

    public Object visitMatchNode(MatchNode iVisited) {
        return visit(iVisited);
    }

    public Object visitModuleNode(ModuleNode iVisited) {
        return visit(iVisited);
    }
    
    public Object visitMethodNameNode(MethodNameNode iVisited) {
        return visit(iVisited);
    }
    
    public Object visitNewlineNode(NewlineNode iVisited) {
        return visit(iVisited);
    }

    public Object visitNextNode(NextNode iVisited) {
        return visit(iVisited);
    }

    public Object visitNilNode(NilNode iVisited) {
        return visit(iVisited);
    }

    public Object visitNotNode(NotNode iVisited) {
        return visit(iVisited);
    }

    public Object visitNthRefNode(NthRefNode iVisited) {
        return visit(iVisited);
    }

    public Object visitOpElementAsgnNode(OpElementAsgnNode iVisited) {
        return visit(iVisited);
    }
    
    public Object visitOptArgNode(OptArgNode iVisited) {
        return visit(iVisited);
    }

    public Object visitOpAsgnNode(OpAsgnNode iVisited) {
        return visit(iVisited);
    }

    public Object visitOpAsgnAndNode(OpAsgnAndNode iVisited) {
        return visit(iVisited);
    }

    public Object visitOpAsgnOrNode(OpAsgnOrNode iVisited) {
        return visit(iVisited);
    }

    public Object visitOrNode(OrNode iVisited) {
        return visit(iVisited);
    }

    public Object visitPreExeNode(PreExeNode iVisited) {
        return visit(iVisited);
    }

    public Object visitPostExeNode(PostExeNode iVisited) {
        return visit(iVisited);
    }

    public Object visitRedoNode(RedoNode iVisited) {
        return visit(iVisited);
    }

    public Object visitRegexpNode(RegexpNode iVisited) {
        return visit(iVisited);
    }

    public Object visitRescueBodyNode(RescueBodyNode iVisited) {
        return visit(iVisited);
    }

    public Object visitRescueNode(RescueNode iVisited) {
        return visit(iVisited);
    }

    public Object visitRestArgNode(RestArgNode iVisited) {
        return visit(iVisited);
    }

    public Object visitRetryNode(RetryNode iVisited) {
        return visit(iVisited);
    }

    public Object visitReturnNode(ReturnNode iVisited) {
        return visit(iVisited);
    }

    public Object visitRootNode(RootNode iVisited) {
        return visit(iVisited);
    }

    public Object visitSClassNode(SClassNode iVisited) {
        return visit(iVisited);
    }

    public Object visitSelfNode(SelfNode iVisited) {
        return visit(iVisited);
    }

    public Object visitSplatNode(SplatNode iVisited) {
        return visit(iVisited);
    }

    public Object visitStrNode(StrNode iVisited) {
        return visit(iVisited);
    }

    public Object visitSuperNode(SuperNode iVisited) {
        return visit(iVisited);
    }

    public Object visitSValueNode(SValueNode iVisited) {
        return visit(iVisited);
    }

    public Object visitSymbolNode(SymbolNode iVisited) {
        return visit(iVisited);
    }
    
    public Object visitSyntaxNode(SyntaxNode iVisited) {
        return visit(iVisited);
    }

    public Object visitToAryNode(ToAryNode iVisited) {
        return visit(iVisited);
    }

    public Object visitTrueNode(TrueNode iVisited) {
        return visit(iVisited);
    }

    public Object visitUndefNode(UndefNode iVisited) {
        return visit(iVisited);
    }

    public Object visitUntilNode(UntilNode iVisited) {
        return visit(iVisited);
    }

    public Object visitVAliasNode(VAliasNode iVisited) {
        return visit(iVisited);
    }

    public Object visitVCallNode(VCallNode iVisited) {
        return visit(iVisited);
    }

    public Object visitWhenNode(WhenNode iVisited) {
        return visit(iVisited);
    }

    public Object visitWhileNode(WhileNode iVisited) {
        return visit(iVisited);
    }

    public Object visitXStrNode(XStrNode iVisited) {
        return visit(iVisited);
    }

    public Object visitYieldNode(YieldNode iVisited) {
        return visit(iVisited);
    }

    public Object visitZArrayNode(ZArrayNode iVisited) {
        return visit(iVisited);
    }

    public Object visitZSuperNode(ZSuperNode iVisited) {
        return visit(iVisited);
    }
}
