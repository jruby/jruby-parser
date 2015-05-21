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
 * Copyright (C) 2006-2007 Mirko Stocker <me@misto.ch>
 * Copyright (C) 2006-2009 Thomas E Enebo <enebo@acm.org>
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

package org.jrubyparser.rewriter;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jrubyparser.NodeVisitor;
import org.jrubyparser.RegexpOptions;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.StaticScope;
import org.jrubyparser.ast.AliasNode;
import org.jrubyparser.ast.AndNode;
import org.jrubyparser.ast.ArgsCatNode;
import org.jrubyparser.ast.ArgsNode;
import org.jrubyparser.ast.ArgsPushNode;
import org.jrubyparser.ast.ArgumentNode;
import org.jrubyparser.ast.ArrayNode;
import org.jrubyparser.ast.AssignableNode;
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
import org.jrubyparser.ast.INameNode;
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
import org.jrubyparser.rewriter.utils.Operators;
import org.jrubyparser.rewriter.utils.ReWriterContext;

/**
 * Visits each node and outputs the corresponding Ruby sourcecode for the nodes.
 *
 * @author Mirko Stocker
 *
 */
public class ReWriteVisitor implements NodeVisitor {

    protected final ReWriterContext config;
    protected final ReWriterFactory factory;

    public ReWriteVisitor(Writer out, String source) {
        this(new ReWriterContext(new PrintWriter(out), source, new DefaultFormatHelper()));
    }

    public ReWriteVisitor(OutputStream out, String source) {
        this(new ReWriterContext(new PrintWriter(out, true), source, new DefaultFormatHelper()));
    }

    public ReWriteVisitor(ReWriterContext config) {
        this.config = config;
        factory = new ReWriterFactory(config);
    }

    public void flushStream() {
        config.getOutput().flush();
    }

    protected ReWriteVisitor print(String s) {
        config.getOutput().print(s);
        return this;
    }

    protected ReWriteVisitor print(char c) {
        config.getOutput().print(c);
        return this;
    }

    protected ReWriteVisitor print(BigInteger i) {
        config.getOutput().print(i);
        return this;
    }

    protected ReWriteVisitor print(int i) {
        config.getOutput().print(i);
        return this;
    }

    protected ReWriteVisitor print(long l) {
        config.getOutput().print(l);
        return this;
    }

    protected ReWriteVisitor print(double d) {
        config.getOutput().print(d);
        return this;
    }

    private void enterCall() {
        config.getCallDepth().enterCall();
    }

    private void leaveCall() {
        config.getCallDepth().leaveCall();
    }

    private boolean inCall() {
        return config.getCallDepth().inCall();
    }

    protected void printNewlineAndIndentation() {
        if (config.hasHereDocument()) config.fetchHereDocument().print();

        print(config.getFormatHelper().getLineDelimiter());
        config.getIndentor().printIndentation(config.getOutput());
    }

    private static boolean isReceiverACallNode(CallNode n) {
        return n.getReceiver() instanceof CallNode || n.getReceiver() instanceof FCallNode;
    }

    public void visitNode(Node iVisited) {
        if (iVisited == null) return;

        if (iVisited instanceof ArgumentNode) {
            print(((ArgumentNode) iVisited).getLexicalName());
        } else {
            iVisited.accept(this);
        }

        config.setLastPosition(iVisited.getPosition());
    }

    public void visitIter(Iterator iterator) {
        while (iterator.hasNext()) {
            visitNode((Node) iterator.next());
        }
    }

    private void visitIterAndSkipFirst(Iterator iterator) {
        iterator.next();
        visitIter(iterator);
    }

    private static boolean isStartOnNewLine(Node first, Node second) {
        if (first == null || second == null) return false;

        return (getStartLine(first) < getStartLine(second));
    }

    private boolean needsParentheses(Node n) {
        return (n != null && (n.childNodes().size() > 1 || inCall() || firstChild(n) instanceof HashNode)
                || firstChild(n) instanceof NewlineNode || firstChild(n) instanceof IfNode);
    }

    private void printCallArguments(Node argsNode, Node iterNode, boolean hasParens) {
        if (argsNode != null && argsNode.childNodes().size() < 1 && iterNode == null) return;

        if (argsNode != null && argsNode.childNodes().size() == 1
                && firstChild(argsNode) instanceof HashNode && iterNode == null) {
            HashNode hashNode = (HashNode) firstChild(argsNode);
            if (hashNode.getListNode().childNodes().size() < 1) {
                print("({})");
            } else {
                print(' ');
                printHashNodeContent(hashNode);
            }
            return;
        }

        // ENEBO: Unsure whether we need all this logic if we pass in parens accurately from the
        // parser, but I am fairly sure not all cases are handled yet.  specs will need to figure
        // this out better.
        boolean paranthesesPrinted = hasParens || needsParentheses(argsNode)
                || (argsNode == null && iterNode != null && iterNode instanceof BlockPassNode)
                || (argsNode != null && argsNode.childNodes().size() > 0 && iterNode != null);

        if (paranthesesPrinted) {
            print('(');
        } else if (argsNode != null) {
            print(config.getFormatHelper().beforeCallArguments());
        }

        if (firstChild(argsNode) instanceof NewlineNode) {
            config.setSkipNextNewline(true);
        }

        enterCall();

        if (argsNode instanceof SplatNode) {
            visitNode(argsNode);
        } else if (argsNode != null) {
            visitAndPrintWithSeparator(argsNode.childNodes().iterator());
        }

        if (iterNode != null && iterNode instanceof BlockPassNode) {
            if (argsNode != null) print(config.getFormatHelper().getListSeparator());

            print('&');
            visitNode(((BlockPassNode) iterNode).getBody());
        }

        if (paranthesesPrinted) {
            print(')');
        } else {
            print(config.getFormatHelper().afterCallArguments());
        }

        leaveCall();
    }

    public void visitAndPrintWithSeparator(Iterator<Node> it) {
        while (it.hasNext()) {
            Node n = it.next();
            factory.createIgnoreCommentsReWriteVisitor().visitNode(n);
            if (it.hasNext()) print(config.getFormatHelper().getListSeparator());
        }
    }

    public Object visitAliasNode(AliasNode iVisited) {
        print("alias ");
        visitNode(iVisited.getNewName());
        print(' ');
        visitNode(iVisited.getOldName());
        return null;
    }

    private boolean sourceRangeEquals(int start, int stop, String compare) {
        return (stop <= config.getSource().length() && sourceSubStringEquals(start, stop - start, compare));
    }

    private boolean sourceRangeContains(SourcePosition pos, String searched) {
        return pos.getStartOffset() < config.getSource().length()
                && pos.getEndOffset() < config.getSource().length() + 1
                && config.getSource().substring(pos.getStartOffset(), pos.getEndOffset()).indexOf(searched) > -1;
    }

    public Object visitAndNode(AndNode iVisited) {
        enterCall();
        visitNode(iVisited.getFirst());

        if (sourceRangeContains(iVisited.getPosition(), "&&")) {
            print(" && ");
        } else {
            print(" and ");
        }
        visitNode(iVisited.getSecond());
        leaveCall();
        return null;
    }

    private ArrayList<Node> collectAllArguments(ArgsNode iVisited) {
        ArrayList<Node> arguments = new ArrayList<Node>();

        if (iVisited.getPre() != null) arguments.addAll(iVisited.getPre().childNodes());
        if (iVisited.getOptional() != null) arguments.addAll(iVisited.getOptional().childNodes());
        if (iVisited.getRest() != null) {
            arguments.add(new ConstNode(iVisited.getRest().getPosition(), iVisited.getRest().getLexicalName()));
        }
        if (iVisited.getPost() != null) arguments.addAll(iVisited.getPost().childNodes());
        if (iVisited.getBlock() != null) arguments.add(iVisited.getBlock());

        return arguments;
    }

    public Object visitArgsNode(ArgsNode iVisited) {

        for (Iterator<Node> it = collectAllArguments(iVisited).iterator(); it.hasNext(); ) {
            Node n = it.next();

            if (n instanceof ArgumentNode) {
                print(((ArgumentNode) n).getLexicalName());
            } else {
                visitNode(n);
            }

            if (it.hasNext()) {
                print(config.getFormatHelper().getListSeparator());
            } else {
        	print(config.getFormatHelper().afterMethodArguments());
            }
        }

        return null;
    }

    public Object visitArgsCatNode(ArgsCatNode iVisited) {
        print("[");
        visitAndPrintWithSeparator(iVisited.getFirst().childNodes().iterator());
        print(config.getFormatHelper().getListSeparator());
        print("*");
        visitNode(iVisited.getSecond());
        print("]");
        return null;
    }
    
    public Object visitArgumentNode(ArgumentNode iVisited) {
        return null;
    }

    public Object visitArrayNode(ArrayNode iVisited) {
        print('[');
        enterCall();
        visitAndPrintWithSeparator(iVisited.childNodes().iterator());
        leaveCall();
        print(']');
        return null;
    }

    public Object visitBackRefNode(BackRefNode iVisited) {
        print('$');
        print(iVisited.getType());
        return null;
    }

    public Object visitBeginNode(BeginNode iVisited) {
        print("begin");
        visitNodeInIndentation(iVisited.getBody());
        printNewlineAndIndentation();
        print("end");
        return null;
    }

    public Object visitBignumNode(BignumNode iVisited) {
        print(iVisited.getValue());
        return null;
    }

    public Object visitBlockArgNode(BlockArgNode iVisited) {
        print(iVisited.getLexicalName());
        return null;
    }

    public Object visitBlockArg18Node(BlockArg18Node iVisited) {
        print('&');
        visitNode(iVisited.getBlockArg());
        return null;
    }

    public Object visitBlockNode(BlockNode iVisited) {
        visitIter(iVisited.childNodes().iterator());
        return null;
    }

    public static int getLocalVarIndex(Node n) {
        return n instanceof LocalVarNode ? ((LocalVarNode) n).getIndex() : -1;
    }

    public Object visitBlockPassNode(BlockPassNode iVisited) {
        visitNode(iVisited.getBody());
        return null;
    }

    public Object visitBreakNode(BreakNode iVisited) {
        print("break");
        return null;
    }

    public Object visitConstDeclNode(ConstDeclNode iVisited) {
        printAsgnNode(iVisited);
        return null;
    }

    public Object visitClassVarAsgnNode(ClassVarAsgnNode iVisited) {
        printAsgnNode(iVisited);
        return null;
    }

    public Object visitClassVarDeclNode(ClassVarDeclNode iVisited) {
        printAsgnNode(iVisited);
        return null;
    }

    public Object visitClassVarNode(ClassVarNode iVisited) {
        print(iVisited.getLexicalName());
        return null;
    }

    public Object visitComplexNode(ComplexNode iVisited) {
        iVisited.getNumber().accept(this);
        print("i");
        return null;
    }

    private boolean isNumericNode(Node n) {
        return (n != null && (n instanceof FixnumNode || n instanceof BignumNode));
    }

    private boolean isNameAnOperator(String name) {
        return Operators.contain(name);
    }

    private boolean printSpaceInsteadOfDot(CallNode n) {
        return (isNameAnOperator(n.getName()) && !(n.getArgs().childNodes().size() > 1));
    }

    protected void printAssignmentOperator() {
        print(config.getFormatHelper().beforeAssignment()).print("=").print(config.getFormatHelper().afterAssignment());
    }

    private Object printIndexAssignment(AttrAssignNode iVisited) {
        enterCall();
        visitNode(iVisited.getReceiver());
        leaveCall();
        print('[');
        visitNode(firstChild(iVisited.getArgs()));
        print("]");
        printAssignmentOperator();
        if (iVisited.getArgs().childNodes().size() > 1) {
            visitNode((Node) iVisited.getArgs().childNodes().get(1));
        }
        return null;
    }

    private Object printIndexAccess(CallNode visited) {
        enterCall();
        visitNode(visited.getReceiver());
        leaveCall();
        print('[');
        if (visited.getArgs() != null) {
            visitAndPrintWithSeparator(visited.getArgs().childNodes().iterator());
        }
        print("]");
        return null;
    }

    private Object printNegativNumericNode(CallNode visited) {
        print('-');
        visitNode(visited.getReceiver());
        return null;
    }

    private boolean isNegativeNumericNode(CallNode visited) {
        return isNumericNode(visited.getReceiver()) && visited.getName().equals("-@");
    }

    private void printCallReceiverNode(CallNode iVisited) {
        if (iVisited.getReceiver() instanceof HashNode) print('(');

        if (isReceiverACallNode(iVisited) && !printSpaceInsteadOfDot(iVisited)) {
            enterCall();
            visitNewlineInParentheses(iVisited.getReceiver());
            leaveCall();
        } else {
            visitNewlineInParentheses(iVisited.getReceiver());
        }

        if (iVisited.getReceiver() instanceof HashNode) print(')');
    }

    protected boolean inMultipleAssignment() {
        return false;
    }

    public Object visitCallNode(CallNode iVisited) {
        if (isNegativeNumericNode(iVisited)) return printNegativNumericNode(iVisited);

        if (iVisited.getName().equals("[]")) return printIndexAccess(iVisited);

        printCallReceiverNode(iVisited);

        print(printSpaceInsteadOfDot(iVisited) ? ' ' : '.');

        if (inMultipleAssignment() && iVisited.getName().endsWith("=")) {
            print(iVisited.getName().substring(0, iVisited.getName().length() - 1));
        } else {
            print(iVisited.getName());
        }

        if (isNameAnOperator(iVisited.getName())) {
            if (firstChild(iVisited.getArgs()) instanceof NewlineNode) print(' ');

            config.getCallDepth().disableCallDepth();
        }
        printCallArguments(iVisited.getArgs(), iVisited.getIter(), iVisited.hasParens());

        if (isNameAnOperator(iVisited.getName())) config.getCallDepth().enableCallDepth();
        if (!(iVisited.getIter() instanceof BlockPassNode)) visitNode(iVisited.getIter());

        return null;
    }

    public Object visitCaseNode(CaseNode iVisited) {
        print("case ");
        visitNode(iVisited.getCase());
        visitNode(iVisited.getFirstWhen());
        printNewlineAndIndentation();
        print("end");
        return null;
    }

    public Object visitClassNode(ClassNode iVisited) {

        print("class ");
        visitNode(iVisited.getCPath());
        if (iVisited.getSuper() != null) {
            print(" < ");
            visitNode(iVisited.getSuper());
        }

        new ClassBodyWriter(this, iVisited.getBody()).write();

        printNewlineAndIndentation();

        print("end");
        return null;
    }

    public Object visitColon2Node(Colon2Node iVisited) {
        if (iVisited.getLeftNode() != null) {
            visitNode(iVisited.getLeftNode());
            print("::");
        }
        print(iVisited.getName());
        return null;
    }

    public Object visitColon3Node(Colon3Node iVisited) {
        print(iVisited.getLexicalName());
        return null;
    }
    
    public Object visitCommentNode(CommentNode iVisited) {
        print(iVisited.getContent());
        return null;
    }

    public Object visitConstNode(ConstNode iVisited) {
        print(iVisited.getName());
        return null;
    }

    public Object visitDAsgnNode(DAsgnNode iVisited) {
        printAsgnNode(iVisited);
        return null;
    }

    public Object visitDRegxNode(DRegexpNode iVisited) {
        config.getPrintQuotesInString().set(false);
        print(getFirstRegexpEnclosure(iVisited));
        factory.createDRegxReWriteVisitor().visitIter(iVisited.childNodes().iterator());
        print(getSecondRegexpEnclosure(iVisited));
        printRegexpOptions(iVisited.getOptions());
        config.getPrintQuotesInString().revert();
        return null;
    }

    private Object createHereDocument(DStrNode iVisited) {
        config.getPrintQuotesInString().set(false);
        print("<<-EOF");
        StringWriter writer = new StringWriter();
        PrintWriter oldOut = config.getOutput();
        config.setOutput(new PrintWriter(writer));

        for (Iterator it = iVisited.childNodes().iterator(); it.hasNext(); ) {
            factory.createHereDocReWriteVisitor().visitNode((Node) it.next());

            if (it.hasNext()) config.setSkipNextNewline(true);
        }

        config.setOutput(oldOut);
        config.depositHereDocument(writer.getBuffer().toString());
        config.getPrintQuotesInString().revert();

        return null;
    }

    public Object visitDStrNode(DStrNode iVisited) {

        if (firstChild(iVisited) instanceof StrNode && stringIsHereDocument((StrNode) firstChild(iVisited))) {
            return createHereDocument(iVisited);
        }

        if (config.getPrintQuotesInString().isTrue()) print(getSeparatorForStr(iVisited));

        config.getPrintQuotesInString().set(false);
        leaveCall();
        for (Node child: iVisited.childNodes()) {
            visitNode(child);
        }
        enterCall();
        config.getPrintQuotesInString().revert();

        if (config.getPrintQuotesInString().isTrue()) print(getSeparatorForStr(iVisited));

        return null;
    }

    public Object visitDSymbolNode(DSymbolNode iVisited) {
        print(':');
        if (config.getPrintQuotesInString().isTrue()) print(getSeparatorForSym(iVisited));

        config.getPrintQuotesInString().set(false);
        leaveCall();
        for (Node child: iVisited.childNodes()) {
            visitNode(child);
        }
        enterCall();
        config.getPrintQuotesInString().revert();

        if (config.getPrintQuotesInString().isTrue()) print(getSeparatorForSym(iVisited));

        return null;
    }

    public Object visitDVarNode(DVarNode iVisited) {
        print(iVisited.getLexicalName());
        return null;
    }

    public Object visitDXStrNode(DXStrNode iVisited) {
        config.getPrintQuotesInString().set(false);
        print("%x{");
        visitIter(iVisited.childNodes().iterator());
        print('}');
        config.getPrintQuotesInString().revert();
        return null;
    }

    public Object visitDefinedNode(DefinedNode iVisited) {
        print("defined? ");
        enterCall();
        visitNode(iVisited.getExpression());
        leaveCall();
        return null;
    }

    private boolean hasArguments(Node n) {
        if (n instanceof ArgsNode) {
            ArgsNode args = (ArgsNode) n;
            return (args.getPre() != null || args.getOptional() != null
                    || args.getBlock() != null || args.getRest() != null);
        } else if (n instanceof ArrayNode && n.childNodes().isEmpty()) {
            return false;
        }
        return true;
    }

    private void printDefNode(Node parent, String name, Node args, StaticScope scope, Node bodyNode) {
        print(name);
        config.getLocalVariables().addLocalVariable(scope);

        if (hasArguments(args)) {
            print(config.getFormatHelper().beforeMethodArguments());
            visitNode(args);
        }

        visitNode(bodyNode);
        config.getIndentor().outdent();
        printNewlineAndIndentation();
        print("end");
    }

    public Object visitDefnNode(DefnNode iVisited) {
        config.getIndentor().indent();
        print("def ");
        printDefNode(iVisited, iVisited.getName(), iVisited.getArgs(), iVisited.getScope(), iVisited.getBody());
        return null;
    }

    public Object visitDefsNode(DefsNode iVisited) {
        config.getIndentor().indent();
        print("def ");
        visitNode(iVisited.getReceiver());
        print('.');
        printDefNode(iVisited, iVisited.getName(), iVisited.getArgs(), iVisited.getScope(), iVisited.getBody());
        return null;
    }

    public Object visitDotNode(DotNode iVisited) {
        enterCall();
        visitNode(iVisited.getBegin());
        print("..");
        if (iVisited.isExclusive()) print('.');
        visitNode(iVisited.getEnd());
        leaveCall();
        return null;
    }

    public Object visitEnsureNode(EnsureNode iVisited) {
        visitNode(iVisited.getBody());
        config.getIndentor().outdent();
        printNewlineAndIndentation();
        print("ensure");
        visitNodeInIndentation(iVisited.getEnsure());
        config.getIndentor().indent();
        return null;
    }

    public Object visitEvStrNode(EvStrNode iVisited) {
        print('#');
        if (!(iVisited.getBody() instanceof NthRefNode)) print('{');
        config.getPrintQuotesInString().set(true);
        visitNode(unwrapNewlineNode(iVisited.getBody()));
        config.getPrintQuotesInString().revert();
        if (!(iVisited.getBody() instanceof NthRefNode)) print('}');
        return null;
    }

    private Node unwrapNewlineNode(Node node) {
        return node instanceof NewlineNode ? ((NewlineNode) node).getNextNode() : node;
    }

    public Object visitFCallNode(FCallNode iVisited) {
        print(iVisited.getName());

        if (iVisited.getIter() != null) config.getCallDepth().enterCall();

        printCallArguments(iVisited.getArgs(), iVisited.getIter(), iVisited.hasParens());

        if (iVisited.getIter() != null) config.getCallDepth().leaveCall();

        if (!(iVisited.getIter() instanceof BlockPassNode)) visitNode(iVisited.getIter());

        return null;
    }

    public Object visitFalseNode(FalseNode iVisited) {
        print("false");
        return null;
    }

    public Object visitFixnumNode(FixnumNode iVisited) {
        print(iVisited.getValue());
        return null;
    }

    public Object visitFlipNode(FlipNode iVisited) {
        enterCall();
        visitNode(iVisited.getBegin());
        print(" ..");
        if (iVisited.isExclusive()) print('.');
        print(' ');
        visitNode(iVisited.getEnd());
        leaveCall();
        return null;
    }

    public Object visitFloatNode(FloatNode iVisited) {
        print(iVisited.getValue());
        return null;
    }

    public Object visitForNode(ForNode iVisited) {
        print("for ");
        visitNode(iVisited.getVar());
        print(" in ");
        visitNode(iVisited.getIter());
        visitNodeInIndentation(iVisited.getBody());
        printNewlineAndIndentation();
        print("end");
        return null;
    }

    public Object visitGlobalAsgnNode(GlobalAsgnNode iVisited) {
        printAsgnNode(iVisited);
        return null;
    }

    public Object visitGlobalVarNode(GlobalVarNode iVisited) {
        print(iVisited.getLexicalName());
        return null;
    }

    private void printHashNodeContent(HashNode iVisited) {
        print(config.getFormatHelper().beforeHashContent());
        if (iVisited.getListNode() != null) {
            for (Iterator it = iVisited.getListNode().childNodes().iterator(); it.hasNext(); ) {
                visitNode((Node) it.next());
                print(config.getFormatHelper().hashAssignment());
                visitNode((Node) it.next());

                if (it.hasNext()) print(config.getFormatHelper().getListSeparator());
            }
        }
        print(config.getFormatHelper().afterHashContent());
    }

    public Object visitHashNode(HashNode iVisited) {
        print('{');
        printHashNodeContent(iVisited);
        print('}');
        return null;
    }

    private void printAsgnNode(AssignableNode n) {
        print(((INameNode) n).getLexicalName());
        if (n.getValue() == null) return;
        printAssignmentOperator();
        visitNewlineInParentheses(n.getValue());
    }

    public Object visitInstAsgnNode(InstAsgnNode iVisited) {
        printAsgnNode(iVisited);
        return null;
    }

    public Object visitInstVarNode(InstVarNode iVisited) {
        print(iVisited.getLexicalName());
        return null;
    }

    /**
     * Elsif-conditions in the AST are represented by multiple nested if / else combinations. This
     * method takes a node and checks if the node is an elsif-statement or a normal else node.
     *
     * @param iVisited
     * @return Returns the last ElseNode or null.
     */
    private Node printElsIfNodes(Node iVisited) {
        if (iVisited != null && iVisited instanceof IfNode) {
            IfNode n = (IfNode) iVisited;
            printNewlineAndIndentation();
            print("elsif ");
            visitNode(n.getCondition());
            visitNodeInIndentation(n.getThenBody());
            return printElsIfNodes(n.getElseBody());
        }

        return iVisited != null ? iVisited : null;
    }

    private Object printShortIfStatement(IfNode n) {
        if (n.getThenBody() == null) {
            visitNode(n.getElseBody());
            print(" unless ");
            visitNode(n.getCondition());
        } else {
            enterCall();
            factory.createShortIfNodeReWriteVisitor().visitNode(n.getCondition());
            print(" ? ");
            factory.createShortIfNodeReWriteVisitor().visitNode(n.getThenBody());
            print(" : ");
            factory.createShortIfNodeReWriteVisitor().visitNewlineInParentheses(n.getElseBody());
            leaveCall();
        }
        return null;
    }

    private boolean isAssignment(Node n) {
        return (n instanceof DAsgnNode || n instanceof GlobalAsgnNode
                || n instanceof InstAsgnNode || n instanceof LocalAsgnNode || n instanceof ClassVarAsgnNode);
    }

    private boolean sourceSubStringEquals(int offset, int length, String str) {
        return config.getSource().length() >= offset + length
                && config.getSource().substring(offset, offset + length).equals(str);
    }

    private boolean isShortIfStatement(IfNode iVisited) {
        return (isOnSingleLine(iVisited.getCondition(), iVisited.getElseBody())
                && !(iVisited.getElseBody() instanceof IfNode)
                && !sourceSubStringEquals(getStartOffset(iVisited), 2, "if"));
    }

    public Object visitIfNode(IfNode iVisited) {

        if (isShortIfStatement(iVisited)) return printShortIfStatement(iVisited);

        print("if ");

        if (isAssignment(iVisited.getCondition())) enterCall();

        // We have to skip a possible Newline here:
        visitNewlineInParentheses(iVisited.getCondition());

        if (isAssignment(iVisited.getCondition())) leaveCall();

        config.getIndentor().indent();
        // we have to check this to generate valid code for this style: "return
        // if true", because there is no newline
        if (!isStartOnNewLine(iVisited.getCondition(), iVisited.getThenBody()) && iVisited.getThenBody() != null) {
            printNewlineAndIndentation();
        }

        visitNode(iVisited.getThenBody());
        config.getIndentor().outdent();
        Node elseNode = printElsIfNodes(iVisited.getElseBody());

        if (elseNode != null) {
            printNewlineAndIndentation();
            print("else");
            config.getIndentor().indent();
            visitNode(elseNode);
            config.getIndentor().outdent();
        }
        printNewlineAndIndentation();
        print("end");
        return null;
    }

    private boolean isOnSingleLine(Node n) {
        return isOnSingleLine(n, n);
    }

    private boolean isOnSingleLine(Node n1, Node n2) {
        if (n1 == null || n2 == null) return false;

        return (getStartLine(n1) == getEndLine(n2));
    }

    private boolean printIterVarNode(IterNode n) {
        if (n.getVar() == null) return false;

        print('|');
        visitNode(n.getVar());
        print('|');

        return true;
    }

    public Object visitImplicitNilNode(ImplicitNilNode visited) {
        return null;
    }
    
    public Object visitIterNode(IterNode iVisited) {
        if (isOnSingleLine(iVisited)) {
            print(config.getFormatHelper().beforeIterBrackets());
            print("{");
            print(config.getFormatHelper().beforeIterVars());
            if (printIterVarNode(iVisited)) print(config.getFormatHelper().afterIterVars());
            config.setSkipNextNewline(true);
            visitNode(iVisited.getBody());
            print(config.getFormatHelper().beforeClosingIterBrackets());
            print('}');
        } else {
            print(" do ");
            printIterVarNode(iVisited);
            visitNodeInIndentation(iVisited.getBody());
            printNewlineAndIndentation();
            print("end");
        }
        return null;
    }
    
    public Object visitLambdaNode(LambdaNode visited) {
        print("->(");
        visitArgsNode(visited.getArgs());
        print(")");
        
        if (isOnSingleLine(visited)) {
            print(config.getFormatHelper().beforeIterBrackets());
            print("{");
            print(config.getFormatHelper().beforeIterVars());
            config.setSkipNextNewline(true);
            visitNode(visited.getBody());
            print(config.getFormatHelper().beforeClosingIterBrackets());
            print('}');
        } else {
            print(" do ");
            visitNodeInIndentation(visited.getBody());
            printNewlineAndIndentation();
            print("end");            
        }

        return null;
    }
    
    public Object visitListNode(ListNode iVisited) {
        return null;
    }

    public Object visitLocalAsgnNode(LocalAsgnNode iVisited) {
        config.getLocalVariables().addLocalVariable(iVisited.getIndex(), iVisited.getLexicalName());
        printAsgnNode(iVisited);
        return null;
    }

    public Object visitLocalVarNode(LocalVarNode iVisited) {
        print(iVisited.getLexicalName());
        return null;
    }
    public Object visitMethodNameNode(MethodNameNode iVisited) {
        return null;
    }

    public Object visitMultipleAsgnNode(MultipleAsgnNode iVisited) {
        if (iVisited.getPre() != null) {
            factory.createMultipleAssignmentReWriteVisitor().visitAndPrintWithSeparator(iVisited.getPre().childNodes().iterator());
        }
        if (iVisited.getValue() == null) {
            visitNode(iVisited.getRest());
            return null;
        }
        print(config.getFormatHelper().beforeAssignment());
        print("=");
        print(config.getFormatHelper().afterAssignment());
        enterCall();
        if (iVisited.getValue() instanceof ArrayNode) {
            visitAndPrintWithSeparator(iVisited.getValue().childNodes().iterator());
        } else {
            visitNode(iVisited.getValue());
        }
        leaveCall();
        return null;
    }

    public Object visitMatch2Node(Match2Node iVisited) {
        visitNode(iVisited.getReceiver());
        print(config.getFormatHelper().matchOperator());
        enterCall();
        visitNode(iVisited.getValue());
        leaveCall();
        return null;
    }

    public Object visitMatch3Node(Match3Node iVisited) {
        visitNode(iVisited.getValue());
        print(config.getFormatHelper().matchOperator());
        visitNode(iVisited.getReceiver());
        return null;
    }

    public Object visitMatchNode(MatchNode iVisited) {
        visitNode(iVisited.getRegexp());
        return null;
    }

    public Object visitModuleNode(ModuleNode iVisited) {
        print("module ");
        config.getIndentor().indent();
        visitNode(iVisited.getCPath());
        visitNode(iVisited.getBody());
        config.getIndentor().outdent();
        printNewlineAndIndentation();
        print("end");
        return null;
    }

    public Object visitNewlineNode(NewlineNode iVisited) {
        if (config.isSkipNextNewline()) {
            config.setSkipNextNewline(false);
        } else {
            printNewlineAndIndentation();
        }
        visitNode(iVisited.getNextNode());
        return null;
    }

    public Object visitNextNode(NextNode iVisited) {
        print("next");
        return null;
    }

    public Object visitNilNode(NilNode iVisited) {
        print("nil");
        return null;
    }

    public Object visitNotNode(NotNode iVisited) {
        if (iVisited.getCondition() instanceof CallNode) enterCall();

        print(sourceRangeContains(iVisited.getPosition(), "not") ? "not " : "!");
        visitNewlineInParentheses(iVisited.getCondition());

        if (iVisited.getCondition() instanceof CallNode) leaveCall();

        return null;
    }

    public Object visitNthRefNode(NthRefNode iVisited) {
        print('$');
        print(iVisited.getMatchNumber());
        return null;
    }

    private boolean isSimpleNode(Node n) {
        return (n instanceof LocalVarNode || n instanceof AssignableNode
                || n instanceof InstVarNode || n instanceof ClassVarNode
                || n instanceof GlobalVarNode || n instanceof ConstDeclNode
                || n instanceof VCallNode || isNumericNode(n));
    }

    public Object visitOpElementAsgnNode(OpElementAsgnNode iVisited) {

        if (!isSimpleNode(iVisited.getReceiver())) {
            visitNewlineInParentheses(iVisited.getReceiver());
        } else {
            visitNode(iVisited.getReceiver());
        }

        visitNode(iVisited.getArgs());
        print(' ').print(iVisited.getOperatorName()).print("=").print(config.getFormatHelper().afterAssignment());
        visitNode(iVisited.getValue());
        return null;
    }

    public Object visitOpAsgnNode(OpAsgnNode op) {
        visitNode(op.getReceiver());
        print('.').print(op.getVariableName()).print(' ').print(op.getOperatorName()).print("=").print(config.getFormatHelper().afterAssignment());
        visitNode(op.getValue());
        return null;
    }

    private void printOpAsgnNode(Node n, String operator) {
        enterCall();

        print(((INameNode) n).getName()).print(config.getFormatHelper().beforeAssignment()).print(operator).print(config.getFormatHelper().afterAssignment());
        visitNode(((AssignableNode) n).getValue());

        leaveCall();
    }

    public Object visitOpAsgnAndNode(OpAsgnAndNode iVisited) {
        printOpAsgnNode(iVisited.getSecond(), "&&=");
        return null;
    }

    public Object visitOpAsgnOrNode(OpAsgnOrNode iVisited) {
        printOpAsgnNode(iVisited.getSecond(), "||=");
        return null;
    }
    
    public Object visitOptArgNode(OptArgNode iVisited) {
        return null;
    }    

    public Object visitOrNode(OrNode iVisited) {
        enterCall();
        visitNode(iVisited.getFirst());
        leaveCall();

        print(sourceRangeContains(iVisited.getPosition(), "||") ? " || " : " or ");

        enterCall();
        visitNewlineInParentheses(iVisited.getSecond());
        leaveCall();

        return null;
    }

    public Object visitPostExeNode(PostExeNode iVisited) {
        // this node contains nothing but an empty list, so we don't have to
        // process anything
        return null;
    }

    public Object visitPreExeNode(PreExeNode iVisited) {
        // this node contains nothing but an empty list, so we don't have to
        // process anything
        return null;
    }

    public Object visitRationalNode(RationalNode iVisited) {
        print(iVisited.getNumerator());
        print("/");
        print(iVisited.getDenominator());

        return null;
    }

    public Object visitRedoNode(RedoNode iVisited) {
        print("redo");
        return null;
    }

    private String getFirstRegexpEnclosure(Node n) {
        return isSpecialRegexNotation(n) ? "%r(" : "/";
    }

    private String getSecondRegexpEnclosure(Node n) {
        return isSpecialRegexNotation(n) ? ")" : "/";
    }

    private boolean isSpecialRegexNotation(Node n) {
        return getStartOffset(n) >= 2
                && !(config.getSource().length() < getStartOffset(n))
                && config.getSource().charAt(getStartOffset(n) - 3) == '%';
    }

    private void printRegexpOptions(RegexpOptions option) {
        if (option.isIgnorecase()) print('i');
        if (option.isExtended()) print('x');
        if (option.isMultiline()) print('m');
    }

    public Object visitRegexpNode(RegexpNode re) {
        print(getFirstRegexpEnclosure(re)).print(re.getValue().toString()).print(getSecondRegexpEnclosure(re));
        printRegexpOptions(re.getOptions());
        return null;
    }

    public static Node firstChild(Node n) {
        if (n == null || n.childNodes().size() <= 0) return null;

        return (Node) n.childNodes().get(0);
    }

    public Object visitRescueBodyNode(RescueBodyNode iVisited) {
        if (iVisited.getBody() != null && config.getLastPosition().getStartLine() == getEndLine(iVisited.getBody())) {
            print(" rescue ");
        } else {
            print("rescue");
        }

        if (iVisited.getExceptions() != null) {
            printExceptionNode(iVisited);
        } else {
            visitNodeInIndentation(iVisited.getBody());
        }

        if (iVisited.getOptRescue() != null) printNewlineAndIndentation();

        visitNode(iVisited.getOptRescue());
        return null;
    }

    private void printExceptionNode(RescueBodyNode n) {
        if (n.getExceptions() == null) return;

        print(' ');
        visitNode(firstChild(n.getExceptions()));

        Node firstBodyNode = n.getBody();
        if (n.getBody() instanceof BlockNode) firstBodyNode = firstChild(n.getBody());

        // if the exception is assigned to a variable, we have to skip the first
        // node in the body
        if (firstBodyNode instanceof AssignableNode) {
            print(config.getFormatHelper().beforeAssignment());
            print("=>");
            print(config.getFormatHelper().afterAssignment());
            print(((INameNode) firstBodyNode).getName());
            if (firstBodyNode instanceof LocalAsgnNode) {
                config.getLocalVariables().addLocalVariable(((LocalAsgnNode) firstBodyNode).getIndex(),
                        ((LocalAsgnNode) firstBodyNode).getName());
            }

            config.getIndentor().indent();
            visitIterAndSkipFirst(n.getBody().childNodes().iterator());
            config.getIndentor().outdent();
        } else {
            visitNodeInIndentation(n.getBody());
        }
    }

    public Object visitRescueNode(RescueNode iVisited) {
        visitNode(iVisited.getBody());
        config.getIndentor().outdent();
        
        Node body = iVisited.getRescue().getBody();

        if (body != null && getStartLine(iVisited) != getEndLine(body)) printNewlineAndIndentation();

        if (body == null) {
            printNewlineAndIndentation();
            print("rescue");
            printExceptionNode(iVisited.getRescue());
        } else {
            visitNode(iVisited.getRescue());
        }

        if (iVisited.getElse() != null) {
            printNewlineAndIndentation();
            print("else");
            visitNodeInIndentation(iVisited.getElse());
        }

        config.getIndentor().indent();
        return null;
    }

    public Object visitKeywordArgNode(KeywordArgNode iVisited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object visitKeywordRestArgNode(KeywordRestArgNode iVisited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Object visitRetryNode(RetryNode iVisited) {
        print("retry");
        return null;
    }

    public static Node unwrapSingleArrayNode(Node n) {
        if (!(n instanceof ArrayNode)) return n;
        if (((ArrayNode) n).childNodes().size() > 1) return n;

        return firstChild((ArrayNode) n);
    }

    public Object visitReturnNode(ReturnNode iVisited) {
        print("return");
        enterCall();
        if (iVisited.getValue() != null) {
            print(' ');
            visitNode(unwrapSingleArrayNode(iVisited.getValue()));
        }
        leaveCall();
        return null;
    }

    public Object visitSClassNode(SClassNode iVisited) {
        print("class << ");
        config.getIndentor().indent();
        visitNode(iVisited.getReceiver());
        visitNode(iVisited.getBody());
        config.getIndentor().outdent();
        printNewlineAndIndentation();
        print("end");
        return null;
    }

    public Object visitSelfNode(SelfNode iVisited) {
        print("self");
        return null;
    }

    public Object visitSplatNode(SplatNode iVisited) {
        print("*");
        visitNode(iVisited.getValue());
        return null;
    }

    private boolean stringIsHereDocument(StrNode n) {
        return sourceRangeEquals(getStartOffset(n) + 1, getStartOffset(n) + 3, "<<")
                || sourceRangeEquals(getStartOffset(n), getStartOffset(n) + 3, "<<-");
    }

    protected char getSeparatorForSym(Node n) {
        // ENEBO: I added one since a sym will start with ':'...This seems like an incomplete assumption
        if (config.getSource().length() >= (getStartOffset(n) + 1)
                && config.getSource().charAt(getStartOffset(n) + 1) == '\'') {
            return '\'';
        }
        return '"';
    }

    protected char getSeparatorForStr(Node n) {
        if (config.getSource().length() >= getStartOffset(n)
                && config.getSource().charAt(getStartOffset(n)) == '\'') {
            return '\'';
        }
        return '"';
    }

    protected boolean inDRegxNode() {
        return false;
    }

    public Object visitStrNode(StrNode iVisited) {
        // look for a here-document:
        if (stringIsHereDocument(iVisited)) {
            print("<<-EOF");
            config.depositHereDocument(iVisited.getValue().toString());
            return null;
        }

        if (iVisited.getValue().equals("")) {
            if (config.getPrintQuotesInString().isTrue()) print("\"\"");

            return null;
        }

        // don't print quotes if we are a subpart of an other here-document
        if (config.getPrintQuotesInString().isTrue()) print(getSeparatorForStr(iVisited));

        if (inDRegxNode()) {
            print(iVisited.getValue().toString());
        } else {
            Matcher matcher = Pattern.compile("([\\\\\\n\\f\\r\\t\\\"\\\'])").matcher(iVisited.getValue().toString());

            if (matcher.find()) {
                String unescChar = unescapeChar(matcher.group(1).charAt(0));
                print(matcher.replaceAll("\\\\" + unescChar));
            } else {
                print(iVisited.getValue().toString());
            }
        }
        if (config.getPrintQuotesInString().isTrue()) print(getSeparatorForStr(iVisited));

        return null;
    }

    public static String unescapeChar(char escapedChar) {
        switch (escapedChar) {
            case '\n':
                return "n";
            case '\f':
                return "f";
            case '\r':
                return "r";
            case '\t':
                return "t";
            case '\"':
                return "\"";
            case '\'':
                return "'";
            case '\\':
                return "\\\\";
            default:
                return null;
        }
    }

    private boolean needsSuperNodeParentheses(SuperNode n) {
        return n.getArgs().childNodes().isEmpty() && 
                config.getSource().charAt(getEndOffset(n)) == '(';
    }

    public Object visitSuperNode(SuperNode iVisited) {
        print("super");
        printCallArguments(iVisited.getArgs(), iVisited.getIter(), iVisited.hasParens());

        return null;
    }

    public Object visitSValueNode(SValueNode iVisited) {
        visitNode(iVisited.getValue());
        return null;
    }

    public Object visitSymbolNode(SymbolNode symbol) {
        print(symbol.getLexicalName());
        return null;
    }
    
    public Object visitSyntaxNode(SyntaxNode iVisited) {
        print(iVisited.getContent());
        return null;
    }

    public Object visitToAryNode(ToAryNode iVisited) {
        visitNode(iVisited.getValue());
        return null;
    }

    public Object visitTrueNode(TrueNode iVisited) {
        print("true");
        return null;
    }

    public Object visitUnaryCallNode(UnaryCallNode iVisited) {
        print(iVisited.getLexicalName());
        visitNode(iVisited.getReceiver());
        return null;
    }

    public Object visitUndefNode(UndefNode iVisited) {
        print("undef ");
        visitNode(iVisited.getName());
        return null;
    }

    public Object visitUntilNode(UntilNode iVisited) {
        print("until ");
        visitNode(iVisited.getCondition());
        visitNodeInIndentation(iVisited.getBody());
        printNewlineAndIndentation();
        print("end");
        return null;
    }

    public Object visitVAliasNode(VAliasNode iVisited) {
        print("alias ").print(iVisited.getNewName()).print(' ').print(iVisited.getOldName());
        return null;
    }

    public Object visitVCallNode(VCallNode iVisited) {
        print(iVisited.getName());
        return null;
    }

    public void visitNodeInIndentation(Node n) {
        config.getIndentor().indent();
        visitNode(n);
        config.getIndentor().outdent();
    }

    public Object visitWhenNode(WhenNode iVisited) {
        printNewlineAndIndentation();
        print("when ");
        enterCall();
        visitAndPrintWithSeparator(iVisited.getExpression().childNodes().iterator());
        leaveCall();
        visitNodeInIndentation(iVisited.getBody());
        if ((iVisited.getNextCase() instanceof WhenNode || iVisited.getNextCase() == null)) {
            visitNode(iVisited.getNextCase());
        } else {
            printNewlineAndIndentation();
            print("else");
            visitNodeInIndentation(iVisited.getNextCase());
        }
        return null;
    }

    protected void visitNewlineInParentheses(Node n) {
        if (n instanceof NewlineNode) {
            if (((NewlineNode) n).getNextNode() instanceof SplatNode) {
                print('[');
                visitNode(((NewlineNode) n).getNextNode());
                print(']');
            } else {
                print('(');
                visitNode(((NewlineNode) n).getNextNode());
                print(')');
            }
        } else {
            visitNode(n);
        }
    }

    private void printWhileStatement(WhileNode iVisited) {
        print("while ");

        if (isAssignment(iVisited.getCondition())) enterCall();

        visitNewlineInParentheses(iVisited.getCondition());

        if (isAssignment(iVisited.getCondition())) leaveCall();

        visitNodeInIndentation(iVisited.getBody());

        printNewlineAndIndentation();
        print("end");
    }

    private void printDoWhileStatement(WhileNode iVisited) {
        print("begin");
        visitNodeInIndentation(iVisited.getBody());
        printNewlineAndIndentation();
        print("end while ");
        visitNode(iVisited.getCondition());
    }

    public Object visitWhileNode(WhileNode iVisited) {
        if (iVisited.evaluateAtStart()) {
            printWhileStatement(iVisited);
        } else {
            printDoWhileStatement(iVisited);
        }
        return null;
    }

    public Object visitXStrNode(XStrNode iVisited) {
        print('`');
        print(iVisited.getValue().toString());
        print('`');
        return null;
    }

    public Object visitYieldNode(YieldNode iVisited) {
        print("yield");
        printCallArguments(iVisited.getArgs(), null, iVisited.hasParens());

        return null;
    }

    public Object visitZArrayNode(ZArrayNode iVisited) {
        print("[]");
        return null;
    }

    public Object visitZSuperNode(ZSuperNode iVisited) {
        print("super");
        return null;
    }

    private static int getStartLine(Node n) {
        return n.getPosition().getStartLine();
    }

    private static int getStartOffset(Node n) {
        return n.getPosition().getStartOffset();
    }

    private static int getEndLine(Node n) {
        return n.getPosition().getEndLine();
    }

    protected static int getEndOffset(Node n) {
        return n.getPosition().getEndOffset();
    }

    public ReWriterContext getConfig() {
        return config;
    }

    public static String createCodeFromNode(Node node, String document) {
        return createCodeFromNode(node, document, new DefaultFormatHelper());
    }

    public static String createCodeFromNode(Node node, String document, FormatHelper helper) {
        StringWriter writer = new StringWriter();
        ReWriterContext ctx = new ReWriterContext(writer, document, helper);
        ReWriteVisitor rewriter = new ReWriteVisitor(ctx);
        rewriter.visitNode(node);
        return writer.toString();
    }

    public Object visitArgsPushNode(ArgsPushNode node) {
        assert false : "Unhandled node";
        return null;
    }

    public Object visitAttrAssignNode(AttrAssignNode iVisited) {
        if (iVisited.getName().equals("[]=")) return printIndexAssignment(iVisited);

        if (iVisited.getName().endsWith("=")) {
            visitNode(iVisited.getReceiver());
            print('.');

            printNameWithoutEqualSign(iVisited);
            printAssignmentOperator();
            if (iVisited.getArgs() != null) {
                visitAndPrintWithSeparator(iVisited.getArgs().childNodes().iterator());
            }
        } else {
            assert false : "Unhandled AttrAssignNode";
        }

        return null;
    }

    private void printNameWithoutEqualSign(INameNode iVisited) {
        print(iVisited.getName().substring(0, iVisited.getName().length() - 1));
    }

    public Object visitRootNode(RootNode root) {
        config.getLocalVariables().addLocalVariable(root.getStaticScope());
        for (Node child: root.childNodes()) {
            visitNode(child);
        }
        if (config.hasHereDocument()) config.fetchHereDocument().print();

        return null;
    }

    public Object visitRestArgNode(RestArgNode iVisited) {
        print(iVisited.getLexicalName());
        return null;
    }

    public Object visitEncodingNode(EncodingNode iVisited) {
        print("__ENCODING__");
        return null;
    }

    public Object visitLiteralNode(LiteralNode iVisited) {
        print(iVisited.getName());
        return null;
    }
}
