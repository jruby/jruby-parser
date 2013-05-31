require 'java'

import java.io.StringReader
import org.jrubyparser.Parser
import org.jrubyparser.parser.ParserConfiguration
import org.jrubyparser.CompatVersion

PARSER = Parser.new
CONFIG_18 = ParserConfiguration.new
CONFIG_19 = ParserConfiguration.new(0, CompatVersion::RUBY1_9)
CONFIG_20 = ParserConfiguration.new(0, CompatVersion::RUBY2_0)
VERSIONS_MAP = { 1.8 => CONFIG_18,  1.9 => CONFIG_19,  2.0 => CONFIG_20 }
VERSIONS = [1.8, 1.9, 2.0]

class Object
  # Wrap the code in what the JRubyParser expects
  def source(code)
    StringReader.new code.to_s
  end

  # Parse the provided code into an AST
  def parse(code, version=1.8)
    PARSER.parse "<code>", source(code), VERSIONS_MAP[version]
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

  ##
  # parse code but record all '^' found in source and return a list of all
  # nodes found at those carets.
  def carets_parse(code, version=1.8, caret='^')
    deloused_code, caret_indices = remove_carets(code, caret)
    root = parse(deloused_code, version)
    [root, caret_indices.map { |e| root.node_at(e)}]
  end

  # Pretty naive impl :)
  def remove_carets(code, caret)
    indices = []
    index = code.index(caret)

    while index
      indices << index
      code.sub!(caret, '')
      index = code.index(caret)
    end

    [code, indices]
  end
end
