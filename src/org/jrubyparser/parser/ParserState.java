package org.jrubyparser.parser;

import org.jrubyparser.lexer.yacc.RubyLexer;

public interface ParserState {
    public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop);
}
