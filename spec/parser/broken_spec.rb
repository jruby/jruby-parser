require_relative '../helpers'

import org.jrubyparser.lexer.yacc.SyntaxException

# tests for broken files
describe Parser do
  VERSIONS.each do |v|
    it "should raise an unterminated string exception" do
      expect { parse('str = "') }.to raise_error SyntaxException
    end

    it "should raise a syntax exception" do
      expect { parse("class Foo\n   def\nend\n") }.to raise_error SyntaxException
    end
  end
end
