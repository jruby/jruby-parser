require 'java'

import java.io.StringReader
import org.jrubyparser.Parser
import org.jrubyparser.parser.ParserConfiguration
import org.jrubyparser.CompatVersion

$parser = Parser.new
$config = ParserConfiguration.new
$config_1_9 = ParserConfiguration.new(0, CompatVersion::RUBY1_9)

class Object
  # Wrap the code in what the JRubyParser expects
  def source(code)
    StringReader.new code.to_s
  end

  # Parse the provided code into an AST
  #
  # Add optional versioning flag for 1.9 tests
  def parse(code, parser_config=$config)
    $parser.parse("<code>", source(code), parser_config)
  end
end
