/*
 * © Copyright -  SourceClear Inc
 */

package org.jrubyparser;

import org.jruby.util.ByteList;
import org.jrubyparser.lexer.ByteListLexerSource;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ByteListLexerSourceTest {

  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  @Test
  public void testGetSource() throws Exception {
    String s = "a\nb\nc";
    ByteListLexerSource source = ByteListLexerSource.getLexerSource("source", 0, new StringReader(s));
    assertEquals("a\n", source.gets().toString());
    assertEquals("b\n", source.gets().toString());
    assertEquals("c", source.gets().toString());
  }
}
