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
 * Copyright (C) 2004-2009 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
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

import org.jrubyparser.CompatVersion;
import org.jrubyparser.StaticScope;

public class ParserConfiguration {
    public enum SyntaxGathering { NONE, COMMENTS, ALL };
    
    // What linenumber will the source think it starts from?
    private int lineNumber = 0;

    // Should we display extra debug information while parsing?
    private boolean isDebug = false;

    private CompatVersion version = CompatVersion.RUBY1_8;
    
    // This scope is sent replaces the root scope.  The common scenario for this is
    // having the parser accurately parse in an eval() context where local variables
    // are already defined.  Without this, we would make all vars end getting declared
    // as vcalls instead of localvars.
    private StaticScope scope = null;
    
    // What additional purely syntactical elements should we retain in the AST.
    private SyntaxGathering syntax = SyntaxGathering.NONE;

    public ParserConfiguration() {}
    
    public ParserConfiguration(int lineNumber, CompatVersion version) {
        this(lineNumber, version, null);
    }
    
    public ParserConfiguration(int lineNumber, CompatVersion version, StaticScope scope) {
        this.lineNumber = lineNumber;
        this.version = version;
        this.scope = scope;
        this.syntax = SyntaxGathering.NONE;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public CompatVersion getVersion() {
        return version;
    }
    
    public StaticScope getScope() {
        return scope;
    }
    
    public String getEncoding() {
        return "UTF-8"; // TODO: Replace with charset from IDE?
    }
    
    public SyntaxGathering getSyntax() {
        return syntax;
    }
    
    public void setSyntax(SyntaxGathering syntax) {
        this.syntax = syntax;
    }
}
