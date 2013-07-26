require_relative '../helpers'

import org.jrubyparser.lexer.StringTerm
import org.jrubyparser.lexer.SyntaxException

# tests for broken files
describe Parser do
  [1.8, 1.9].each do |v|
    it "should raise an unterminated string exception" do
      lambda {
        parse('str = "', v)
      }.should raise_error StringTerm::UnterminatedStringException
    end

    it "should raise a syntax exception" do
      lambda {
        parse("class Foo\n   def\nend\n", v)
      }.should raise_error SyntaxException
    end
  end
end
