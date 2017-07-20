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
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2002-2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
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
package org.jrubyparser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.jrubyparser.IRubyWarnings.ID;
import org.jrubyparser.ast.Node;
import org.jrubyparser.lexer.LexerSource;
import org.jrubyparser.lexer.SyntaxException;
import org.jrubyparser.parser.ParserConfiguration;
import org.jrubyparser.parser.ParserResult;
import org.jrubyparser.parser.Ruby18Parser;
import org.jrubyparser.parser.Ruby19Parser;
import org.jrubyparser.parser.Ruby20Parser;
import org.jrubyparser.parser.Ruby23Parser;
import org.jrubyparser.parser.RubyParser;

/**
 * Serves as a simple facade for all the parsing magic.
 */
public class Parser {
    private volatile long totalTime;
    private volatile int totalBytes;

    public Parser() {}

    public long getTotalTime() {
        return totalTime;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    // TODO: Add rewriter parsing in here.
    
    public Node parse(String file, Reader content, ParserConfiguration configuration)
            throws SyntaxException {
        long startTime = System.nanoTime();

        RubyParser parser;
        if (configuration.getVersion() == CompatVersion.RUBY1_8) {
            parser = new Ruby18Parser();            
        } else if (configuration.getVersion() == CompatVersion.RUBY1_9) {
            parser = new Ruby19Parser();
        } else if (configuration.getVersion() == CompatVersion.RUBY2_0) {
            parser = new Ruby20Parser();
        } else {
            parser = new Ruby23Parser();
        }

        // TODO: Warning interface from configuration?
        parser.setWarnings(new NullWarnings());
        
        LexerSource lexerSource = LexerSource.getSource(file, content, configuration);

        Node ast = null;
        try {
            ParserResult result = parser.parse(configuration, lexerSource);
            
            // We want some amount of extra syntax-only elements properly added to the AST tree
            if (configuration.getSyntax() != ParserConfiguration.SyntaxGathering.NONE) result.weaveInExtraSyntax();
            
            ast = result.getAST();
        } catch(IOException e) {
            // TODO: What should this raise something for IDEs?
        }
        
        totalTime += System.nanoTime() - startTime;
        totalBytes += lexerSource.getOffset();

        return ast;
    }

    public static class NullWarnings implements IRubyWarnings {
        public boolean isVerbose() { return false; }

        public void warn(ID id, String message, Object... data) {}
        public void warning(ID id, String message, Object... data) {}
        public void warn(ID id, SourcePosition position, String message, Object... data) {}
        public void warn(ID id, String fileName, int lineNumber, String message, Object... data) {}
        public void warning(ID id, SourcePosition position, String message, Object... data) {}
        public void warning(ID id, String fileName, int lineNumber, String message, Object...data) {}
    }
}
