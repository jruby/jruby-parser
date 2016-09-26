/*
 * © Copyright -  SourceClear Inc
 */

package org.jrubyparser.ast;

import org.jrubyparser.lexer.yacc.SimpleSourcePosition;

import java.util.Collections;
import java.util.List;

/**
 * A null object for call arguments.
 */
public class EmptyArgsNode extends ArgsNode {

  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  //////////////////////////////// Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  /////////////////////////////// Constructors \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  public EmptyArgsNode() {
    super(new SimpleSourcePosition("", 0), null, null, null, null, null, null, null);
  }

  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  //------------------------ Implements:

  //------------------------ Overrides:

  @Override
  public List<Node> childNodes() {
    return Collections.emptyList();
  }


  //---------------------------- Abstract Methods -----------------------------

  //---------------------------- Utility Methods ------------------------------

  //---------------------------- Property Methods -----------------------------
}
