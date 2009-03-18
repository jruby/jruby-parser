require 'java'

$LOAD_PATH.unshift File.dirname(__FILE__) + "/../dist"

require 'JRubyParser.jar'

import org.jrubyparser.Parser
import org.jrubyparser.parser.ParserConfiguration
JFile = java.io.File
import java.io.FileReader

config = ParserConfiguration.new
parser = Parser.new

while (filename = ARGV.shift)
  p parser.parse(filename, FileReader.new(JFile.new(filename)), config).toString
end
