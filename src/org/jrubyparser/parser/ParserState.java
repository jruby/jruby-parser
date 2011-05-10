package org.jrubyparser.parser;

import org.jrubyparser.lexer.Lexer;

public interface ParserState {
    public Object execute(ParserSupport support, Lexer lexer, Object yyVal, Object[] yyVals, int yyTop);
}
