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
 * Copyright (C) 2004-2006 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2004 Stefan Matthias Aust <sma@3plus4.de>
 * Copyright (C) 2005 Zach Dennis <zdennis@mktec.com>
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
package org.jrubyparser.lexer;

import java.io.IOException;
import java.io.Reader;

import org.jrubyparser.SourcePosition;
import org.jrubyparser.parser.ParserConfiguration;

/**
 * This class is what feeds the lexer.  It is primarily a wrapper around a
 * Reader that can unread() data back onto the source.  Originally, I thought
 * about using the PushBackReader to handle read/unread, but I realized that
 * some extremely pathological case could overflow the pushback buffer.  Better
 * safe than sorry.  I could have combined this implementation with a 
 * PushbackBuffer, but the added complexity did not seem worth it.
 * 
 */
public abstract class LexerSource {
	// Last position we gave out
    private SourcePosition lastPosition;

    // The name of this source (e.g. a filename: foo.rb)
    private final String sourceName;
    
    // Number of newlines read from the reader
    protected int line = 0;
    
    // How many bytes into the source are we?
    protected int offset = 0;

    /**
     * Create our food-source for the lexer
     * 
     * @param sourceName is the file we are reading
     * @param line starting line number for source (used by eval)
     */
    protected LexerSource(String sourceName, int line) {
        this.sourceName = sourceName;
        lastPosition = new SourcePosition("", line, line);
        this.line = line;
    }

    /**
     * What file are we lexing?
     * @return the files name
     */
    public String getFilename() {
    	return sourceName;
    }
    
    /**
     * What line are we at?
     * @return the line number 0...line_size-1
     */
    public int getLine() {
        return line;
    }
    
    /**
     * The location of the last byte we read from the source.
     * 
     * @return current location of source
     */
    public int getOffset() {
        return (offset <= 0 ? 0 : offset);
    }

    /**
     * Where is the reader within the source {filename,row}
     * 
     * @return the current position
     */
    public SourcePosition getPosition(SourcePosition startPosition, boolean inclusive) {
        if (startPosition == null) {
            lastPosition = new SourcePosition(getFilename(), lastPosition.getEndLine(),
                    getLine(), lastPosition.getEndOffset(), getOffset());
        } else if (inclusive) {
            lastPosition = new SourcePosition(getFilename(), startPosition.getStartLine(),
                    getLine(), startPosition.getStartOffset(), getOffset());
        } else {
            lastPosition = new SourcePosition(getFilename(), startPosition.getEndLine(),
                    getLine(), startPosition.getEndOffset(), getOffset());
        }

        return lastPosition;
    }
    
    /**
     * Where is the reader within the source {filename,row}
     * 
     * @return the current position
     */
    public SourcePosition getPosition() {
        return new SourcePosition(getFilename(), lastPosition.getEndLine(),
                    getLine(), lastPosition.getEndOffset(), getOffset());
    }
    

    /**
     * Create a source.
     * 
     * @param name the name of the source (e.g a filename: foo.rb)
     * @param content the data of the source
     * @return the new source
     */
    public static LexerSource getSource(String name, Reader content,
            ParserConfiguration configuration) {
        return new ReaderLexerSource(name, content, configuration.getLineNumber());
    }

    /**
     * Match marker against input consumering lexer source as it goes...Unless it does not match
     * then it reverts lexer source back to point when this method was invoked.
     * 
     * @param marker to match against
     * @param indent eat any leading whitespace
     * @param withNewline includes a check that marker is followed by newline or EOF
     * @return true if marker matches...false otherwise
     * @throws IOException if an error occurred reading from underlying IO source
     */
    public abstract boolean matchMarker(String marker, boolean indent, boolean withNewline) throws IOException;

    public abstract int read() throws IOException;
    public abstract String readUntil(char c) throws IOException;
    public abstract String readLineBytes() throws IOException;
    public abstract int skipUntil(int c) throws IOException;
    public abstract void unread(int c);
    public abstract void unreadMany(CharSequence line);
    public abstract boolean peek(int c) throws IOException;
    public abstract boolean lastWasBeginOfLine();
    public abstract boolean wasBeginOfLine();
    public abstract int chompReadAhead();
    public abstract boolean isANewLine();
    // Various places where we call LexerSource.unread(), the nextCharIsOnANewline value gets inaccurate (column/line too, but I don't care about those)
    public abstract void setIsANewLine(boolean nextCharIsOnANewLine);
    public abstract void setOffset(int offset);
}
