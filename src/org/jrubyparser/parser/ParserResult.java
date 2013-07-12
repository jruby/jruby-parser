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
 * Copyright (C) 2002 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Stefan Matthias Aust <sma@3plus4.de>
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

import java.util.ArrayList;
import java.util.List;

import org.jrubyparser.StaticScope;
import org.jrubyparser.ast.CommentNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.PreExeNode;
import org.jrubyparser.ast.SyntaxNode;

/**
 */
public class ParserResult {
    final public static List<Node> EMPTY_BEGIN_LIST = new ArrayList<Node>();
    final public static List<CommentNode> EMPTY_COMMENT_LIST = new ArrayList<CommentNode>();
    private List<Node> beginNodes;
    private Node ast;
    // __END__ marker offset (-1 means none present)
    private int endOffset = -1;
    private List<SyntaxNode> syntaxNodes;
    private StaticScope scope;
    
    public Node getAST() {
        return ast;
    }

    /**
     * Sets the ast.
     * @param ast The ast to set
     */
    public void setAST(Node ast) {
        this.ast = ast;
    }

    public void addSyntax(SyntaxNode node) {
        if (syntaxNodes == null) syntaxNodes = new ArrayList<SyntaxNode>();
        syntaxNodes.add(node);
    }
        
    public void addBeginNode(PreExeNode node) {
        if (beginNodes == null) beginNodes = new ArrayList<Node>();
    	beginNodes.add(node);
    }
    
    public List<Node> getBeginNodes() {
        return beginNodes == null ? EMPTY_BEGIN_LIST : beginNodes;
    }
    
    public int getEndOffset() {
    	return endOffset;
    }
    
    public List<SyntaxNode> getSyntaxNodes() {
        return syntaxNodes;
    } 
    
    public void setEndOffset(int endOffset) {
    	this.endOffset = endOffset;
    }

    public void setScope(StaticScope scope) {
        this.scope = scope;
    }

    public StaticScope getScope() {
        return scope;
    }
}
