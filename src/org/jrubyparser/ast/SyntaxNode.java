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
 * Copyright (C) 2013 The JRuby Team
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
package org.jrubyparser.ast;

import org.jrubyparser.NodeVisitor;
import org.jrubyparser.SourcePosition;

/**
 * This represents extra syntax which has no value to a runtime but is necessary to preserve
 * syntax in the case of rewriting or any other syntactical analysis.  Comments and potentially
 * other pieces of syntax may subclass this so it is easier to process those in a visitor.
 */
public class SyntaxNode extends Node {
    // text for this region of syntax
    private String content;

    public SyntaxNode(SourcePosition position, String content) {
        super(position);

        this.content = content;
    }

    //TODO : Should I have an #isSame() method?

    @Override
    public NodeType getNodeType() {
        return NodeType.SYNTAXNODE;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitSyntaxNode(this);
    }

    public String getContent() {
        return content;
    }
}
