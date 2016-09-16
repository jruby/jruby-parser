/*
 * © Copyright -  SourceClear Inc
 */

package org.jrubyparser;

import org.jrubyparser.ast.LambdaNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.lexer.ReaderLexerSource;
import org.jrubyparser.parser.ParserConfiguration;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class ParserTest {

  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  @org.junit.Test
  public void testArrowSyntax() throws Exception {
    // https://github.com/jruby/jruby-parser/issues/36
    final List<String> ruby = Arrays.asList("-> (message) { puts message }");

    final StringBuilder stringBuilder = new StringBuilder();
    for (String s : ruby) {
      stringBuilder.append(s);
      stringBuilder.append("\n");
    }
    final String s = stringBuilder.toString();

    final Parser parser = new Parser();
    final Node root = parser.parse("lol", new StringReader(s), new ParserConfiguration(0));
    LambdaNode lambdaNode = root.findFirstChild(LambdaNode.class);
    assertTrue(lambdaNode != null);
  }
}
