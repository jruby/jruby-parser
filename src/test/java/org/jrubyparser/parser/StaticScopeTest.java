/*
 * © Copyright -  SourceClear Inc
 */

package org.jrubyparser.parser;

import org.jrubyparser.ast.TrueNode;
import org.jrubyparser.lexer.yacc.SimpleSourcePosition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class StaticScopeTest {

  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  @Test
  public void testExists() throws Exception {
    StaticScope scope = StaticScopeFactory.newStaticScope(null, StaticScope.Type.LOCAL, null);
    SimpleSourcePosition pos = new SimpleSourcePosition("<eval>", 0);
    scope.assign(pos, "a", new TrueNode(pos));

    assertEquals(scope.exists("a"), 0);

    String a = new StringBuilder().append('a').toString();
    assertEquals(scope.exists(a), 0);

    assertEquals(scope.exists("unknown"), -1);

    scope.assign(pos, "\uD83D\uDCA3", new TrueNode(pos));

    assertEquals(scope.exists("\uD83D\uDCA3"), 1);
  }
}
