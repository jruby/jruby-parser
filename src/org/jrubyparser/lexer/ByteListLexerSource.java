/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser.lexer;

import org.jcodings.Encoding;
import org.jruby.util.ByteList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Lexer source for ripper when we have all bytes available to us.
 */
public class ByteListLexerSource extends LexerSource {
  private ByteList completeSource; // The entire source of the file
  private int offset = 0; // Offset into source overall (mri: lex_gets_ptr)

  /**
   * Create our food-source for the lexer.
   *
   * @param sourceName is the file we are reading
   * @param line       starting line number for source (used by eval)
   * @param in         the ByteList backing the source we want to lex
   */
  public ByteListLexerSource(String sourceName, int line, ByteList in) {
    super(sourceName, line);
    this.completeSource = in;
  }

  public static ByteListLexerSource getLexerSource(String sourceName, int line, Reader reader) throws IOException {
    ByteList byteList = new ByteList(80);

    BufferedReader bufferedReader = new BufferedReader(reader);
    String s;
    while ((s = bufferedReader.readLine()) != null) {
      byteList.append(s.getBytes());
    }
    return new ByteListLexerSource(sourceName, 0, byteList);
  }

  @Override
  public Encoding getEncoding() {
    return completeSource.getEncoding();
  }

  @Override
  public void setEncoding(Encoding encoding) {
    completeSource.setEncoding(encoding);
  }


  @Override
  public ByteList gets() {
    int length = completeSource.length();
    if (offset >= length) return null; // At end of source/eof

    int end = completeSource.indexOf('\n', offset) + 1;
    if (end == 0) end = length;

    ByteList line = completeSource.makeShared(offset, end - offset);
    offset = end;

    return line;
  }

  @Override
  public int getOffset() {
    return offset;
  }
}
