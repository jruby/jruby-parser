$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

import org.jrubyparser.lexer.StringTerm
import org.jrubyparser.lexer.SyntaxException

# tests for broken files
describe Parser do
  it "should raise an unterminated string exception" do
    lambda {parse(<<-EOF)
str = "
    EOF
    }.should raise_error StringTerm::UnterminatedStringException
  end

  it "should raise a syntax exception" do
    lambda {parse(<<-EOF)
class Foo
   def
end
    EOF
    }.should raise_error SyntaxException
  end


end
