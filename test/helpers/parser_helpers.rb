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
end
