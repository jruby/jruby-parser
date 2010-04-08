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
 * Copyright (C) 2004 Charles O Nutter <headius@headius.com>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Stefan Matthias Aust <sma@3plus4.de>
 * Copyright (C) 2006-2007 Mirko Stocker <me@misto.ch>
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
package org.jrubyparser.parser;

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
import org.jrubyparser.ast.BlockArgNode;
import org.jrubyparser.ast.BlockNode;
import org.jrubyparser.ast.BlockPassNode;
import org.jrubyparser.ast.BreakNode;
import org.jrubyparser.ast.CallNode;
import org.jrubyparser.ast.CaseNode;
import org.jrubyparser.ast.ClassVarAsgnNode;
import org.jrubyparser.ast.ClassVarDeclNode;
import org.jrubyparser.ast.ClassVarNode;
import org.jrubyparser.ast.Colon2ConstNode;
import org.jrubyparser.ast.Colon2ImplicitNode;
import org.jrubyparser.ast.Colon2MethodNode;
import org.jrubyparser.ast.Colon2Node;
import org.jrubyparser.ast.Colon3Node;
import org.jrubyparser.ast.ConstDeclNode;
import org.jrubyparser.ast.ConstNode;
import org.jrubyparser.ast.DAsgnNode;
import org.jrubyparser.ast.DRegexpNode;
import org.jrubyparser.ast.DStrNode;
import org.jrubyparser.ast.DotNode;
import org.jrubyparser.ast.EvStrNode;
import org.jrubyparser.ast.FCallNode;
import org.jrubyparser.ast.FalseNode;
import org.jrubyparser.ast.FileNode;
import org.jrubyparser.ast.FixnumNode;
import org.jrubyparser.ast.FlipNode;
import org.jrubyparser.ast.FloatNode;
import org.jrubyparser.ast.GlobalAsgnNode;
import org.jrubyparser.ast.GlobalVarNode;
import org.jrubyparser.ast.IArgumentNode;
import org.jrubyparser.ast.IfNode;
import org.jrubyparser.ast.InstAsgnNode;
import org.jrubyparser.ast.InstVarNode;
import org.jrubyparser.ast.IterNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.LocalAsgnNode;
import org.jrubyparser.ast.Match2Node;
import org.jrubyparser.ast.Match3Node;
import org.jrubyparser.ast.MatchNode;
import org.jrubyparser.ast.MultipleAsgnNode;
import org.jrubyparser.ast.NewlineNode;
import org.jrubyparser.ast.NilImplicitNode;
import org.jrubyparser.ast.NilNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.NthRefNode;
import org.jrubyparser.ast.OpElementAsgnNode;
import org.jrubyparser.ast.OrNode;
import org.jrubyparser.ast.RegexpNode;
import org.jrubyparser.ast.RestArgNode;
import org.jrubyparser.ast.RootNode;
import org.jrubyparser.ast.SValueNode;
import org.jrubyparser.ast.SelfNode;
import org.jrubyparser.ast.SplatNode;
import org.jrubyparser.ast.StrNode;
import org.jrubyparser.ast.SuperNode;
import org.jrubyparser.ast.SymbolNode;
import org.jrubyparser.ast.TrueNode;
import org.jrubyparser.ast.WhenNode;
import org.jrubyparser.ast.YieldNode;
import org.jrubyparser.ast.ILiteralNode;
import org.jrubyparser.ast.INameNode;
import org.jrubyparser.IRubyWarnings;
import org.jrubyparser.IRubyWarnings.ID;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.ISourcePositionHolder;
import org.jrubyparser.lexer.SyntaxException;
import org.jrubyparser.lexer.SyntaxException.PID;
import org.jrubyparser.lexer.Token;
import org.jrubyparser.BlockStaticScope;
import org.jrubyparser.LocalStaticScope;
import org.jrubyparser.StaticScope;
import org.jrubyparser.ast.AliasNode;
import org.jrubyparser.ast.LiteralNode;
import org.jrubyparser.ast.UndefNode;

/** 
 *
 */
public class ParserSupport {
    // Parser states:
    protected StaticScope currentScope;
    
    // Is the parser current within a singleton (value is number of nested singletons)
    private int inSingleton;
    
    // Is the parser currently within a method definition
    private boolean inDefinition;

    protected IRubyWarnings warnings;

    private ParserConfiguration configuration;
    private ParserResult result;

    public static boolean isConstant(String id) {
	    return Character.isUpperCase(id.charAt(0));
    }

    public void reset() {
        inSingleton = 0;
        inDefinition = false;
    }
    
    public StaticScope getCurrentScope() {
        return currentScope;
    }
    
    public ParserConfiguration getConfiguration() {
        return configuration;
    }
    
    public void popCurrentScope() {
        currentScope = currentScope.getEnclosingScope();
    }
    
    public void pushBlockScope() {
        currentScope = new BlockStaticScope(currentScope);
    }
    
    public void pushLocalScope() {
        currentScope = new LocalStaticScope(currentScope);
    }
    
    public Node arg_concat(SourcePosition position, Node node1, Node node2) {
        return node2 == null ? node1 : new ArgsCatNode(position, node1, node2);
    }

    public Node arg_blk_pass(Node firstNode, BlockPassNode secondNode) {
        if (secondNode != null) {
            secondNode.setArgsNode(firstNode);
            return secondNode;
        }
        return firstNode;
    }

    /**
     * We know for callers of this that it cannot be any of the specials checked in gettable.
     * 
     * @param node to check its variable type
     * @return an AST node representing this new variable
     */
    public Node gettable2(Node node) {
        switch (node.getNodeType()) {
        case DASGNNODE: // LOCALVAR
        case LOCALASGNNODE:
            return currentScope.declare(node.getPosition(), ((INameNode) node).getName());
        case CONSTDECLNODE: // CONSTANT
            return currentScope.declare(node.getPosition(), ((INameNode) node).getName());
        case INSTASGNNODE: // INSTANCE VARIABLE
            return new InstVarNode(node.getPosition(), ((INameNode) node).getName());
        case CLASSVARDECLNODE:
        case CLASSVARASGNNODE:
            return new ClassVarNode(node.getPosition(), ((INameNode) node).getName());
        case GLOBALASGNNODE:
            return new GlobalVarNode(node.getPosition(), ((INameNode) node).getName());
        }
        
        getterIdentifierError(node.getPosition(), ((INameNode) node).getName());
        return null;
    }
    
    /**
     * Create AST node representing variable type it represents.
     * 
     * @param token to check its variable type
     * @return an AST node representing this new variable
     */
    public Node gettable(Token token) {
        switch (token.getType()) {
        case Tokens.kSELF:
            return new SelfNode(token.getPosition());
        case Tokens.kNIL:
            return new NilNode(token.getPosition());
        case Tokens.kTRUE:
            return new TrueNode(token.getPosition());
        case Tokens.kFALSE:
            return new FalseNode(token.getPosition());
        case Tokens.k__FILE__:
            return new FileNode(token.getPosition(), token.getPosition().getFile());
        case Tokens.k__LINE__:
            return new FixnumNode(token.getPosition(), token.getPosition().getEndLine()+1);
        case Tokens.k__ENCODING__:
            // ENEBO: 1.8 will never see this, but 1.9 can
            // TODO: Replace with proper encoding support
            return new FixnumNode(token.getPosition(), token.getPosition().getEndLine()+1);            
        case Tokens.tIDENTIFIER:
            return currentScope.declare(token.getPosition(), (String) token.getValue());
        case Tokens.tCONSTANT:
            return new ConstNode(token.getPosition(), (String) token.getValue());
        case Tokens.tIVAR:
            return new InstVarNode(token.getPosition(), (String) token.getValue());
        case Tokens.tCVAR:
            return new ClassVarNode(token.getPosition(), (String) token.getValue());
        case Tokens.tGVAR:
            return new GlobalVarNode(token.getPosition(), (String) token.getValue());
        }

        getterIdentifierError(token.getPosition(), (String) token.getValue());
        return null;
    }
    
    protected void getterIdentifierError(SourcePosition position, String identifier) {
        throw new SyntaxException(PID.BAD_IDENTIFIER, position, "identifier " +
                identifier + " is not valid", identifier);
    }
    
    public AssignableNode assignable(Token lhs, Node value) {
        checkExpression(value);

        switch (lhs.getType()) {
            case Tokens.kSELF:
                throw new SyntaxException(PID.CANNOT_CHANGE_SELF, lhs.getPosition(), "Can't change the value of self");
            case Tokens.kNIL:
                throw new SyntaxException(PID.INVALID_ASSIGNMENT, lhs.getPosition(), "Can't assign to nil", "nil");
            case Tokens.kTRUE:
                throw new SyntaxException(PID.INVALID_ASSIGNMENT, lhs.getPosition(), "Can't assign to true", "true");
            case Tokens.kFALSE:
                throw new SyntaxException(PID.INVALID_ASSIGNMENT, lhs.getPosition(), "Can't assign to false", "false");
            case Tokens.k__FILE__:
                throw new SyntaxException(PID.INVALID_ASSIGNMENT, lhs.getPosition(), "Can't assign to __FILE__", "__FILE__");
            case Tokens.k__LINE__:
                throw new SyntaxException(PID.INVALID_ASSIGNMENT, lhs.getPosition(), "Can't assign to __LINE__", "__LINE__");
            case Tokens.tIDENTIFIER:
                return currentScope.assign(value != NilImplicitNode.NIL ? union(lhs, value) : lhs.getPosition(), (String) lhs.getValue(), makeNullNil(value));
            case Tokens.tCONSTANT:
                if (isInDef() || isInSingle()) {
                    throw new SyntaxException(PID.DYNAMIC_CONSTANT_ASSIGNMENT, lhs.getPosition(), "dynamic constant assignment");
                }
                return new ConstDeclNode(lhs.getPosition(), (String) lhs.getValue(), null, value);
            case Tokens.tIVAR:
                return new InstAsgnNode(lhs.getPosition(), (String) lhs.getValue(), value);
            case Tokens.tCVAR:
                if (isInDef() || isInSingle()) {
                    return new ClassVarAsgnNode(lhs.getPosition(), (String) lhs.getValue(), value);
                }
                return new ClassVarDeclNode(lhs.getPosition(), (String) lhs.getValue(), value);
            case Tokens.tGVAR:
                return new GlobalAsgnNode(lhs.getPosition(), (String) lhs.getValue(), value);
        }

        throw new SyntaxException(PID.BAD_IDENTIFIER, lhs.getPosition(), "identifier " + 
                (String) lhs.getValue() + " is not valid", lhs.getValue());
    }

    /**
     *  Wraps node with NEWLINE node.
     *
     *@param node
     *@return a NewlineNode or null if node is null.
     */
    public Node newline_node(Node node, SourcePosition position) {
        if (node == null) return null;
        
        return node instanceof NewlineNode ? node : new NewlineNode(position, node); 
    }
    
    public SourcePosition union(ISourcePositionHolder first, ISourcePositionHolder second) {
        while (first instanceof NewlineNode) {
            first = ((NewlineNode) first).getNextNode();
        }

        while (second instanceof NewlineNode) {
            second = ((NewlineNode) second).getNextNode();
        }
        
        if (second == null) return first.getPosition();
        if (first == null) return second.getPosition();
        
        return first.getPosition().union(second.getPosition());
    }
    
    public SourcePosition union(SourcePosition first, SourcePosition second) {
		if (first.getStartOffset() < second.getStartOffset()) return first.union(second); 

		return second.union(first);
	}
    
    public Node addRootNode(Node topOfAST, SourcePosition position) {
        position = topOfAST != null ? topOfAST.getPosition() : position;

        if (result.getBeginNodes().size() == 0) {
            if (topOfAST == null) topOfAST = NilImplicitNode.NIL;
            
            return new RootNode(position, result.getScope(), topOfAST);
        }
        
        BlockNode newTopOfAST = new BlockNode(position);
        for (Node beginNode: result.getBeginNodes()) {
            appendToBlock(newTopOfAST, beginNode);
        }
        
        // Add real top to new top (unless this top is empty [only begin/end nodes or truly empty])
        if (topOfAST != null) newTopOfAST.add(topOfAST);
        
        return new RootNode(position, result.getScope(), newTopOfAST);
    }
    
    /* MRI: block_append */
    public Node appendToBlock(Node head, Node tail) {
        if (tail == null) return head;
        if (head == null) return tail;

        if (!(head instanceof BlockNode)) {
            head = new BlockNode(head.getPosition()).add(head);
        }

        if (warnings.isVerbose() && isBreakStatement(((ListNode) head).getLast())) {
            warnings.warning(ID.STATEMENT_NOT_REACHED, tail.getPosition(), "Statement not reached.");
        }

        // Assumption: tail is never a list node
        ((ListNode) head).addAll(tail);
        head.setPosition(union(head, tail));
        return head;
    }

    public Node getOperatorCallNode(Node firstNode, String operator) {
        checkExpression(firstNode);

        return new CallNode(firstNode.getPosition(), firstNode, operator, null);
    }
    
    public Node getOperatorCallNode(Node firstNode, String operator, Node secondNode) {
        return getOperatorCallNode(firstNode, operator, secondNode, null);
    }

    public Node getOperatorCallNode(Node firstNode, String operator, Node secondNode, SourcePosition defaultPosition) {
        if (defaultPosition != null) {
            firstNode = checkForNilNode(firstNode, defaultPosition);
            secondNode = checkForNilNode(secondNode, defaultPosition);
        }
        
        checkExpression(firstNode);
        checkExpression(secondNode);
        
        return new CallNode(union(firstNode.getPosition(), secondNode.getPosition()), firstNode, operator, new ArrayNode(secondNode.getPosition(), secondNode));
    }

    public Node getMatchNode(Node firstNode, Node secondNode) {
        if (firstNode instanceof DRegexpNode || firstNode instanceof RegexpNode) {
            return new Match2Node(firstNode.getPosition(), firstNode, secondNode);
        } else if (secondNode instanceof DRegexpNode || secondNode instanceof RegexpNode) {
            return new Match3Node(firstNode.getPosition(), secondNode, firstNode);
        } 

        return getOperatorCallNode(firstNode, "=~", secondNode);
    }

    /**
     * Define an array set condition so we can return lhs
     * 
     * @param receiver array being set
     * @param index node which should evalute to index of array set
     * @return an AttrAssignNode
     */
    public Node aryset(Node receiver, Node index) {
        checkExpression(receiver);

        return new_attrassign(receiver.getPosition(), receiver, "[]=", index);
    }

    /**
     * Define an attribute set condition so we can return lhs
     * 
     * @param receiver object which contains attribute
     * @param name of the attribute being set
     * @return an AttrAssignNode
     */
    public Node attrset(Node receiver, String name) {
        checkExpression(receiver);

        return new_attrassign(receiver.getPosition(), receiver, name + "=", null);
    }

    public void backrefAssignError(Node node) {
        if (node instanceof NthRefNode) {
            String varName = "$" + ((NthRefNode) node).getMatchNumber();
            throw new SyntaxException(PID.INVALID_ASSIGNMENT, node.getPosition(), 
                    "Can't set variable " + varName + '.', varName);
        } else if (node instanceof BackRefNode) {
            String varName = "$" + ((BackRefNode) node).getType();
            throw new SyntaxException(PID.INVALID_ASSIGNMENT, node.getPosition(), "Can't set variable " + varName + '.', varName);
        }
    }

    public Node arg_add(SourcePosition position, Node node1, Node node2) {
        if (node1 == null) {
            if (node2 == null) {
                return new ArrayNode(position, NilImplicitNode.NIL);
            } else {
                return new ArrayNode(node2.getPosition(), node2);
            }
        }
        if (node1 instanceof ArrayNode) return ((ArrayNode) node1).add(node2);
        
        return new ArgsPushNode(position, node1, node2);
    }
    
	/**
	 * @fixme position
	 **/
    public Node node_assign(Node lhs, Node rhs) {
        if (lhs == null) return null;

        Node newNode = lhs;

        checkExpression(rhs);
        if (lhs instanceof AssignableNode) {
    	    ((AssignableNode) lhs).setValueNode(rhs);
    	    lhs.setPosition(union(lhs, rhs));
        } else if (lhs instanceof IArgumentNode) {
            IArgumentNode invokableNode = (IArgumentNode) lhs;
            
            return invokableNode.setArgsNode(arg_add(lhs.getPosition(), invokableNode.getArgsNode(), rhs));
        }
        
        return newNode;
    }
    
    public Node ret_args(Node node, SourcePosition position) {
        if (node != null) {
            if (node instanceof BlockPassNode) {
                throw new SyntaxException(PID.DYNAMIC_CONSTANT_ASSIGNMENT, position, "Dynamic constant assignment.");
            } else if (node instanceof ArrayNode && ((ArrayNode)node).size() == 1) {
                node = ((ArrayNode)node).get(0);
            } else if (node instanceof SplatNode) {
                node = new SValueNode(position, node);
            }
        }
        
        return node;
    }

    /**
     * Is the supplied node a break/control statement?
     * 
     * @param node to be checked
     * @return true if a control node, false otherwise
     */
    public boolean isBreakStatement(Node node) {
        breakLoop: do {
            if (node == null) return false;

            switch (node.getNodeType()) {
            case NEWLINENODE:
                node = ((NewlineNode) node).getNextNode();
                continue breakLoop;
            case BREAKNODE: case NEXTNODE: case REDONODE:
            case RETRYNODE: case RETURNNODE:
                return true;
            default:
                return false;
            }
        } while (true);                    
    }

    // TODO: rename or something since -e not important here
    public void warnUnlessEOption(ID id, Node node, String message) {
        warnings.warn(id, node.getPosition(), message);
    }

    public void warningUnlessEOption(ID id, Node node, String message) {
        warnings.warning(id, node.getPosition(), message);
    }

    /**
     * Does this node represent an expression?
     * @param node to be checked
     * @return true if an expression, false otherwise
     */
    public void checkExpression(Node node) {
        if (warnings.isVerbose() && !isExpression(node)) {
            warnings.warning(ID.VOID_VALUE_EXPRESSION, node.getPosition(), "void value expression");
        }
    }
    
    private boolean isExpression(Node node) {
        do {
            if (node == null) return true;
            
            switch (node.getNodeType()) {
            case BEGINNODE:
                node = ((BeginNode) node).getBodyNode();
                break;
            case BLOCKNODE:
                node = ((BlockNode) node).getLast();
                break;
            case BREAKNODE:
                node = ((BreakNode) node).getValueNode();
                break;
            case CLASSNODE: case DEFNNODE: case DEFSNODE: case MODULENODE: case NEXTNODE: 
            case REDONODE: case RETRYNODE: case RETURNNODE: case UNTILNODE: case WHILENODE:
                return false;
            case IFNODE:
                return isExpression(((IfNode) node).getThenBody()) &&
                  isExpression(((IfNode) node).getElseBody());
            case NEWLINENODE:
                node = ((NewlineNode) node).getNextNode();
                break;
            default: // Node
                return true;
            }
        } while (true);
    }
    
    /**
     * Is this a literal in the sense that MRI has a NODE_LIT for.  This is different than
     * ILiteralNode.  We should pick a different name since ILiteralNode is something we created
     * which is similiar but used for a slightly different condition (can I do singleton things).
     * 
     * @param node to be tested
     * @return true if it is a literal
     */
    public boolean isLiteral(Node node) {
        return node != null && (node instanceof FixnumNode || node instanceof BignumNode || 
                node instanceof FloatNode || node instanceof SymbolNode || 
                (node instanceof RegexpNode && 
                        (((RegexpNode) node).getOptions() & ~ReOptions.RE_OPTION_ONCE) == 0));
    }

    private void handleUselessWarn(Node node, String useless) {
        warnings.warn(ID.USELESS_EXPRESSION, node.getPosition(), "Useless use of " + useless + " in void context.", useless);
    }

    /**
     * Check to see if current node is an useless statement.  If useless a warning if printed.
     * 
     * @param node to be checked.
     */
    public void checkUselessStatement(Node node) {
        if (!warnings.isVerbose()) return;
        
        uselessLoop: do {
            if (node == null) return;
            
            switch (node.getNodeType()) {
            case NEWLINENODE:
                node = ((NewlineNode) node).getNextNode();
                continue uselessLoop;
            case CALLNODE: {
                String name = ((CallNode) node).getName();
                
                if (name == "+" || name == "-" || name == "*" || name == "/" || name == "%" || 
                    name == "**" || name == "+@" || name == "-@" || name == "|" || name == "^" || 
                    name == "&" || name == "<=>" || name == ">" || name == ">=" || name == "<" || 
                    name == "<=" || name == "==" || name == "!=") {
                    handleUselessWarn(node, name);
                }
                return;
            }
            case BACKREFNODE: case DVARNODE: case GLOBALVARNODE:
            case LOCALVARNODE: case NTHREFNODE: case CLASSVARNODE:
            case INSTVARNODE:
                handleUselessWarn(node, "a variable"); return;
            // FIXME: Temporarily disabling because this fires way too much running Rails tests. JRUBY-518
            /*case CONSTNODE:
                handleUselessWarn(node, "a constant"); return;*/
            case BIGNUMNODE: case DREGEXPNODE: case DSTRNODE: case DSYMBOLNODE:
            case FIXNUMNODE: case FLOATNODE: case REGEXPNODE:
            case STRNODE: case SYMBOLNODE:
                handleUselessWarn(node, "a literal"); return;
            // FIXME: Temporarily disabling because this fires way too much running Rails tests. JRUBY-518
            /*case CLASSNODE: case COLON2NODE:
                handleUselessWarn(node, "::"); return;*/
            case DOTNODE:
                handleUselessWarn(node, ((DotNode) node).isExclusive() ? "..." : ".."); return;
            case DEFINEDNODE:
                handleUselessWarn(node, "defined?"); return;
            case FALSENODE:
                handleUselessWarn(node, "false"); return;
            case NILNODE: 
                handleUselessWarn(node, "nil"); return;
            // FIXME: Temporarily disabling because this fires way too much running Rails tests. JRUBY-518
            /*case SELFNODE:
                handleUselessWarn(node, "self"); return;*/
            case TRUENODE:
                handleUselessWarn(node, "true"); return;
            default: return;
            }
        } while (true);
    }

    /**
     * Check all nodes but the last one in a BlockNode for useless (void context) statements.
     * 
     * @param blockNode to be checked.
     */
    public void checkUselessStatements(BlockNode blockNode) {
        if (warnings.isVerbose()) {
            Node lastNode = blockNode.getLast();

            for (int i = 0; i < blockNode.size(); i++) {
                Node currentNode = blockNode.get(i);
        		
                if (lastNode != currentNode ) {
                    checkUselessStatement(currentNode);
                }
            }
        }
    }

	/**
     * assign_in_cond
	 **/
    private boolean checkAssignmentInCondition(Node node) {
        if (node instanceof MultipleAsgnNode) {
            throw new SyntaxException(PID.MULTIPLE_ASSIGNMENT_IN_CONDITIONAL, node.getPosition(), "Multiple assignment in conditional.");
        } else if (node instanceof LocalAsgnNode || node instanceof DAsgnNode || node instanceof GlobalAsgnNode || node instanceof InstAsgnNode) {
            Node valueNode = ((AssignableNode) node).getValueNode();
            if (valueNode instanceof ILiteralNode || valueNode instanceof NilNode || valueNode instanceof TrueNode || valueNode instanceof FalseNode) {
                warnings.warn(ID.ASSIGNMENT_IN_CONDITIONAL, node.getPosition(), "Found '=' in conditional, should be '=='.");
            }
            return true;
        } 

        return false;
    }
    
    protected Node makeNullNil(Node node) {
        return node == null ? NilImplicitNode.NIL : node;
    }

    private Node cond0(Node node) {
        checkAssignmentInCondition(node);
        
        Node leftNode = null;
        Node rightNode = null;

        // FIXME: DSTR,EVSTR,STR: warning "string literal in condition"
        switch(node.getNodeType()) {
        case DREGEXPNODE: {
            SourcePosition position = node.getPosition();

            return new Match2Node(position, node, new GlobalVarNode(position, "$_"));
        }
        case ANDNODE:
            leftNode = cond0(((AndNode) node).getFirstNode());
            rightNode = cond0(((AndNode) node).getSecondNode());
            
            return new AndNode(node.getPosition(), makeNullNil(leftNode), makeNullNil(rightNode));
        case ORNODE:
            leftNode = cond0(((OrNode) node).getFirstNode());
            rightNode = cond0(((OrNode) node).getSecondNode());
            
            return new OrNode(node.getPosition(), makeNullNil(leftNode), makeNullNil(rightNode));
        case DOTNODE: {
            DotNode dotNode = (DotNode) node;
            if (dotNode.isLiteral()) return node; 
            
            String label = String.valueOf("FLIP" + node.hashCode());
            currentScope.getLocalScope().addVariable(label);
            int slot = currentScope.isDefined(label);
            
            return new FlipNode(node.getPosition(),
                    getFlipConditionNode(((DotNode) node).getBeginNode()),
                    getFlipConditionNode(((DotNode) node).getEndNode()),
                    dotNode.isExclusive(), slot);
        }
        case REGEXPNODE:
            warningUnlessEOption(ID.REGEXP_LITERAL_IN_CONDITION, node, "regex literal in condition");
            
            return new MatchNode(node.getPosition(), node);
        } 

        return node;
    }

    public Node getConditionNode(Node node) {
        if (node == null) return NilImplicitNode.NIL;

        if (node instanceof NewlineNode) {
            return new NewlineNode(node.getPosition(), cond0(((NewlineNode) node).getNextNode()));
        } 

        return cond0(node);
    }

    /* MRI: range_op */
    // FIXME: Not sure this is needed anymore
    private Node getFlipConditionNode(Node node) {
        return node;
    }
    
    public SplatNode newSplatNode(SourcePosition position, Node node) {
        return new SplatNode(position, makeNullNil(node));
    }
    
    public ArrayNode newArrayNode(SourcePosition position, Node firstNode) {
        return new ArrayNode(position, makeNullNil(firstNode));
    }

    public AndNode newAndNode(SourcePosition position, Node left, Node right) {
        checkExpression(left);
        
        if (left == null && right == null) return new AndNode(position, makeNullNil(left), makeNullNil(right));
        
        return new AndNode(union(left, right), makeNullNil(left), makeNullNil(right));
    }

    public OrNode newOrNode(SourcePosition position, Node left, Node right) {
        checkExpression(left);

        if (left == null && right == null) return new OrNode(position, makeNullNil(left), makeNullNil(right));
        
        return new OrNode(union(left, right), makeNullNil(left), makeNullNil(right));
    }

    /**
     * Ok I admit that this is somewhat ugly.  We post-process a chain of when nodes and analyze
     * them to re-insert them back into our new CaseNode the way we want.  The grammar is being
     * difficult and until I go back into the depths of that this is where things are.
     *
     * @param expression of the case node (e.g. case foo)
     * @param firstWhenNode first when (which could also be the else)
     * @return a new case node
     */
    public CaseNode newCaseNode(SourcePosition position, Node expression, Node firstWhenNode) {
        ArrayNode cases = new ArrayNode(firstWhenNode != null ? firstWhenNode.getPosition() : position);
        CaseNode caseNode = new CaseNode(position, expression, cases);

        for (Node current = firstWhenNode; current != null;) {
            if (current instanceof WhenNode) {
                cases.add(current);
            } else {
                caseNode.setElseNode(current);
                break;
            }

            current = ((WhenNode) current).getNextCase();
        }

        return caseNode;
    }

    
    public WhenNode newWhenNode(SourcePosition position, Node expressionNodes, Node bodyNode, Node nextCase) {
        if (bodyNode == null) bodyNode = NilImplicitNode.NIL;

        if (expressionNodes instanceof SplatNode || expressionNodes instanceof ArgsCatNode) {
            return new WhenNode(position, expressionNodes, bodyNode, nextCase);
        }

        ListNode list = (ListNode) expressionNodes;

        if (list.size() == 1) {
            Node element = list.get(0);
            
            if (!(element instanceof SplatNode)) {
                return new WhenNode(position, element, bodyNode, nextCase);
            }
        }

        return new WhenNode(position, expressionNodes, bodyNode, nextCase);
    }

    public Node getReturnArgsNode(Node node) {
        if (node instanceof ArrayNode && ((ArrayNode) node).size() == 1) { 
            return ((ListNode) node).get(0);
        } else if (node instanceof BlockPassNode) {
            throw new SyntaxException(PID.BLOCK_ARG_UNEXPECTED, node.getPosition(), "Block argument should not be given.");
        }
        return node;
    }
    
    public Node new_opElementAsgnNode(SourcePosition position, Node receiverNode, String operatorName, Node argsNode, Node valueNode) {
        return new OpElementAsgnNode(position, receiverNode, operatorName, argsNode, valueNode);
    }
    
    public Node new_attrassign(SourcePosition position, Node receiver, String name, Node args) {
        return new AttrAssignNode(position, receiver, name, args);
    }
    
    public Node new_call(Node receiver, Token name, Node args, Node iter) {
        if (args instanceof BlockPassNode) {
            // Block and block pass passed in at same time....uh oh
            if (iter != null) {
                throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, iter.getPosition(), "Both block arg and actual block given.");
            }
        }

        // We need to union with rightmost existing element.
        ISourcePositionHolder holder = getRightmostHolderForCall(name, args, iter);

        SourcePosition position;
        if (receiver == null) {
            receiver = NilImplicitNode.NIL;
            position = holder.getPosition();
        } else if (receiver instanceof NilImplicitNode) {
            position = holder.getPosition();
        } else {
            position = union(receiver, holder);
        }

        // If we have no arguments we will construct an empty list to avoid null checking.
        // Notes: 1) We cannot share empty lists because of rewriting 2) Position of an empty
        // list is undefined so we use any position just to satisfy node requirements.
        if (args == null) args = new ListNode(name.getPosition());

        return new CallNode(position, receiver,(String) name.getValue(), args, (IterNode) iter);
    }

    private ISourcePositionHolder getRightmostHolderForCall(Token name, Node args, Node iter) {
        if (iter != null) {
            return iter;
        } else if (args != null) {
            return args;
        }

        return name;
    }

    public Colon2Node new_colon2(SourcePosition position, Node leftNode, String name) {
        if (isConstant(name)) {
            if (leftNode == null) return new Colon2ImplicitNode(position, name);

            return new Colon2ConstNode(position, leftNode, name);
        }

        return new Colon2MethodNode(position, leftNode, name);
    }

    public Colon3Node new_colon3(SourcePosition position, String name) {
        return new Colon3Node(position, name);
    }
    
    public Node new_fcall(Token operation, Node args, Node iter) {
        SourcePosition position = union(operation, (iter != null ? iter : args));

        // If we have no arguments we will construct an empty list to avoid null checking.
        // Notes: 1) We cannot share empty lists because of rewriting 2) Position of an empty
        // list is undefined so we use any position just to satisfy node requirements.
        if (args == null) args = new ListNode(operation.getPosition());
        
        return new FCallNode(position, (String) operation.getValue(), args, (IterNode) iter);
    }

    public Node new_super(Node args, Token operation) {
        if (args != null && args instanceof BlockPassNode) {
            return new SuperNode(union(operation, args), ((BlockPassNode) args).getArgsNode(), args);
        }
        return new SuperNode(operation.getPosition(), args);
    }

    /**
    *  Description of the RubyMethod
    */
    public void initTopLocalVariables() {
        currentScope = new LocalStaticScope(null);
        
        result.setScope(currentScope);
    }

    /** Getter for property inSingle.
     * @return Value of property inSingle.
     */
    public boolean isInSingle() {
        return inSingleton != 0;
    }

    /** Setter for property inSingle.
     * @param inSingle New value of property inSingle.
     */
    public void setInSingle(int inSingle) {
        this.inSingleton = inSingle;
    }

    public boolean isInDef() {
        return inDefinition;
    }

    public void setInDef(boolean inDef) {
        this.inDefinition = inDef;
    }

    /** Getter for property inSingle.
     * @return Value of property inSingle.
     */
    public int getInSingle() {
        return inSingleton;
    }

    /**
     * Gets the result.
     * @return Returns a RubyParserResult
     */
    public ParserResult getResult() {
        return result;
    }

    /**
     * Sets the result.
     * @param result The result to set
     */
    public void setResult(ParserResult result) {
        this.result = result;
    }

    /**
     * Sets the configuration.
     * @param configuration The configuration to set
     */
    public void setConfiguration(ParserConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setWarnings(IRubyWarnings warnings) {
        this.warnings = warnings;
    }
    
    public Node literal_concat(SourcePosition position, Node head, Node tail) {
        if (head == null) return tail;
        if (tail == null) return head;
        
        if (head instanceof EvStrNode) {
            head = new DStrNode(union(head.getPosition(), position)).add(head);
        } 

        if (tail instanceof StrNode) {
            if (head instanceof StrNode) {
        	    return new StrNode(union(head, tail), (StrNode) head, (StrNode) tail);
            } 
            head.setPosition(union(head, tail));
            return ((ListNode) head).add(tail);
        	
        } else if (tail instanceof DStrNode) {
            if (head instanceof StrNode){
                ((DStrNode)tail).prepend(head);
                return tail;
            } 

            return ((ListNode) head).addAll(tail);
        } 

        // tail must be EvStrNode at this point 
        if (head instanceof StrNode) {
        	
            //Do not add an empty string node
            if(((StrNode) head).getValue().length() == 0) {
                head = new DStrNode(head.getPosition());
            } else {
                // All first element StrNode's do not include syntacical sugar.
                head.getPosition().adjustStartOffset(-1);
                head = new DStrNode(head.getPosition()).add(head);
            }
        }
        return ((DStrNode) head).add(tail);
    }
    
    public Node newEvStrNode(SourcePosition position, Node node) {
        Node head = node;
        while (true) {
            if (node == null) break;
            
            if (node instanceof StrNode || node instanceof DStrNode || node instanceof EvStrNode) {
                return node;
            }
                
            if (!(node instanceof NewlineNode)) break;
                
            node = ((NewlineNode) node).getNextNode();
        }
        
        return new EvStrNode(position, head);
    }
    
    public Node new_yield(SourcePosition position, Node node) {
        boolean state = true;
        
        if (node != null) {
            if (node instanceof BlockPassNode) {
                throw new SyntaxException(PID.BLOCK_ARG_UNEXPECTED, node.getPosition(), "Block argument should not be given.");
            }

            // FIXME: This does not seem to be in 1.9
            if (node instanceof ArrayNode && ((ArrayNode)node).size() == 1) {
                node = ((ArrayNode)node).get(0);
                state = false;
            }
            
            if (node != null && node instanceof SplatNode) {
                state = true;
            }
        } else {
            state = false;
        }
        
        return new YieldNode(position, node, state);
    }
    
    public Node negateInteger(Node integerNode) {
        if (integerNode instanceof FixnumNode) {
            FixnumNode fixnumNode = (FixnumNode) integerNode;
            
            fixnumNode.setValue(-fixnumNode.getValue());
            return fixnumNode;
        } else if (integerNode instanceof BignumNode) {
            BignumNode bignumNode = (BignumNode) integerNode;
            
            bignumNode.setValue(bignumNode.getValue().negate());
        }
        
        return integerNode;
    }
    
    public FloatNode negateFloat(FloatNode floatNode) {
        floatNode.setValue(-floatNode.getValue());
        
        return floatNode;
    }
    
    public SourcePosition createEmptyArgsNodePosition(SourcePosition pos) {
        return new SourcePosition(pos.getFile(), pos.getStartLine(), pos.getEndLine(), pos.getEndOffset() - 1, pos.getEndOffset() - 1);
    }
    
    public Node unwrapNewlineNode(Node node) {
    	if(node instanceof NewlineNode) {
    		return ((NewlineNode) node).getNextNode();
    	}
    	return node;
    }
    
    private Node checkForNilNode(Node node, SourcePosition defaultPosition) {
        return (node == null) ? new NilNode(defaultPosition) : node; 
    }

    public ArgumentNode getRestArgNode(Token token) {
        int index = ((Integer) token.getValue()).intValue();
        if(index < 0) {
            return null;
        }
        String name = getCurrentScope().getLocalScope().getVariables()[index];
        // FIXME: We should not need to specify IDESourcePosition here...
        SourcePosition position = new SourcePosition(token.getPosition().getFile(), token.getPosition().getStartLine(), token.getPosition().getEndLine(), token.getPosition().getStartOffset(), token.getPosition().getEndOffset() + name.length());
        return new ArgumentNode(position, name);
    }

    public Node new_args(SourcePosition position, ListNode pre, ListNode optional, RestArgNode rest,
            ListNode post, BlockArgNode block) {
        return new ArgsNode(position, pre, optional, rest, post, block);
    }

    public Node newAlias(SourcePosition position, Node newNode, Node oldNode) {
        return new AliasNode(position, newNode, oldNode);
    }

    public Node newUndef(SourcePosition position, Node nameNode) {
        return new UndefNode(position, nameNode);
    }
}
