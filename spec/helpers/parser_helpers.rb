require 'java'

import java.io.StringReader
import org.jrubyparser.Parser
import org.jrubyparser.parser.ParserConfiguration
import org.jrubyparser.CompatVersion

PARSER = Parser.new
CONFIG_18 = ParserConfiguration.new
CONFIG_19 = ParserConfiguration.new(0, CompatVersion::RUBY1_9)

class Object
  # Wrap the code in what the JRubyParser expects
  def source(code)
    StringReader.new code.to_s
  end

  # Parse the provided code into an AST
  def parse(code, version=1.8)
    config = version == 1.8  ? CONFIG_18 : CONFIG_19
    PARSER.parse "<code>", source(code), config
  end

  ##
  # parse code but record first '^' and return the node which 
  # that offset represents.  If you are testing source which contains
  # a caret already you can substitute the value with a delimeter which
  # will work.
  def caret_parse(code, version=1.8, caret='^')
    caret_index = code.index(caret)
    raise ArgumentError.new("Caret '^' missing: #{code}") unless caret_index

    root = parse(code.sub(caret, ''), version)
    [root, root.node_at(caret_index)]
  end
end
