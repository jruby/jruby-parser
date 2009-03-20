require 'java'

import java.io.StringReader
import org.jrubyparser.Parser
import org.jrubyparser.parser.ParserConfiguration

$parser = Parser.new
$config = ParserConfiguration.new

class Object
  # Wrap the code in what the JRubyParser expects
  def source(code)
    StringReader.new code.to_s
  end

  # Parse the provided code into an AST
  #
  # Add optional versioning flag for 1.9 tests
  def parse(code)
    $parser.parse("<code>", source(code), $config)
  end
end
