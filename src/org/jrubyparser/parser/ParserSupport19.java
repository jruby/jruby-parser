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
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
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

import org.jrubyparser.ast.AssignableNode;
import org.jrubyparser.ast.ClassVarAsgnNode;
import org.jrubyparser.ast.ConstDeclNode;
import org.jrubyparser.ast.GlobalAsgnNode;
import org.jrubyparser.ast.InstAsgnNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.lexer.SyntaxException;
import org.jrubyparser.lexer.SyntaxException.PID;
import org.jrubyparser.lexer.Token;

public class ParserSupport19 extends ParserSupport {
    @Override
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
            case Tokens.k__ENCODING__:
                throw new SyntaxException(PID.INVALID_ASSIGNMENT, lhs.getPosition(), "Can't assign to __ENCODING__", "__ENCODING__");
            case Tokens.tLABEL: // keyword args (only 2.0 grammar can ever call assignable with this token)
            case Tokens.tIDENTIFIER:
                // ENEBO: 1.9 has CURR nodes for local/block variables.  We don't.  I believe we follow proper logic
                return currentScope.assign(value != null ? union(lhs, value) : lhs.getPosition(), (String) lhs.getValue(), value);
            case Tokens.tCONSTANT:
                if (isInDef() || isInSingle()) {
                    throw new SyntaxException(PID.DYNAMIC_CONSTANT_ASSIGNMENT, lhs.getPosition(), "dynamic constant assignment");
                }
                return new ConstDeclNode(lhs.getPosition(), (String) lhs.getValue(), null, value);
            case Tokens.tIVAR:
                return new InstAsgnNode(lhs.getPosition(), (String) lhs.getValue(), value);
            case Tokens.tCVAR:
                return new ClassVarAsgnNode(lhs.getPosition(), (String) lhs.getValue(), value);
            case Tokens.tGVAR:
                return new GlobalAsgnNode(lhs.getPosition(), (String) lhs.getValue(), value);
        }

        throw new SyntaxException(PID.BAD_IDENTIFIER, lhs.getPosition(), "identifier " + 
                (String) lhs.getValue() + " is not valid to set", lhs.getValue());
    }

    @Override
    protected void getterIdentifierError(SourcePosition position, String identifier) {
        throw new SyntaxException(PID.BAD_IDENTIFIER, position, "identifier " +
                identifier + " is not valid to get", identifier);
    }
}
