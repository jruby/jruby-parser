require 'java'

import java.io.StringReader
import org.jrubyparser.Parser
import org.jrubyparser.parser.ParserConfiguration
import org.jrubyparser.parser.StaticScopeFactory

PARSER = Parser.new
VERSIONS = [2.3]

class Object
  # Wrap the code in what the JRubyParser expects
  def source(code)
    StringReader.new code.to_s
  end

  def config(scope=nil)
    ParserConfiguration.new(0, scope, false, false, false)
  end

  # Create a static scope that we can pass to the parser that has
  # defined all strings local var names defined within it.
  def scope(*vars)
    position = org.jrubyparser.lexer.yacc.SimpleSourcePosition.new('<eval>', 0)
    node = org.jrubyparser.ast.TrueNode.new position
    StaticScopeFactory.new_local_scope(nil).tap do |scope|
      vars.each { |e| scope.assign(position, e, node) }
    end
  end

  # Parse the provided code into an AST
  def parse(code, scope=nil)
    PARSER.parse "<code>", source(code), config(scope)
  end

  # Parse the provided code into an AST with full syntax collection.
  # The 'r' is for rewriting...
  def rparse(code, version=1.8, scope=nil)
    root = PARSER.parse "<code>", source(code), config(scope)
#    display(root)
    root
  end

  ##
  # parse code but record first '^' and return the node which
  # that offset represents.  If you are testing source which contains
  # a caret already you can substitute the value with a delimeter which
  # will work.
  def caret_parse(code, version=1.8, caret='^', scope=nil)
    caret_index = code.index(caret)
    raise ArgumentError.new("Caret '^' missing: #{code}") unless caret_index

    root = parse code.sub(caret, ''), version, scope
    [root, root.node_at(caret_index)]
  end

  ##
  # parse code but record all '^' found in source and return a list of all
  # nodes found at those carets.
  def carets_parse(code, version=1.8, caret='^', scope=nil)
    deloused_code, caret_indices = remove_carets(code, caret)
    root = parse deloused_code, version, scope
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
