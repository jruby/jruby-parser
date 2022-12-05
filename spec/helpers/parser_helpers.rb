require 'java'

java_import java.io.StringReader
java_import org.jrubyparser.CompatVersion
java_import org.jrubyparser.Parser
java_import org.jrubyparser.LocalStaticScope
java_import org.jrubyparser.parser.ParserConfiguration

PARSER = Parser.new
VERSIONS_MAP = {
  1.8 => CompatVersion::RUBY1_8,  
  1.9 => CompatVersion::RUBY1_9,  
  2.0 => CompatVersion::RUBY2_0,
  2.3 => CompatVersion::RUBY2_3,
}
VERSIONS = [1.8, 1.9, 2.0, 2.3]

SYNTAX_MAP = {
  nil => ParserConfiguration::SyntaxGathering::NONE,
  :all => ParserConfiguration::SyntaxGathering::ALL
}

class Object
  # Wrap the code in what the JRubyParser expects
  def source(code)
    StringReader.new code.to_s
  end

  def config(version, scope=nil, syntax=nil)
    ParserConfiguration.new(0, VERSIONS_MAP[version], scope).tap do |c|
      c.syntax = SYNTAX_MAP[syntax]
    end
  end

  # Create a static scope that we can pass to the parser that has
  # defined all strings local var names defined within it.
  def scope(*vars)
    position = org.jrubyparser.SourcePosition.new("(eval)", 0, 0)
    LocalStaticScope.new(nil).tap do |scope|
      vars.each { |e| scope.assign(position, e, nil) }
    end
  end

  # Parse the provided code into an AST
  def parse(code, version=1.8, scope=nil)
    PARSER.parse "<code>", source(code), config(version, scope)
  end

# def node_value(node)
#   name = node.getClass.name

#   value = node.value if node.respond_to?(:value)
#   value = node.name if node.respond_to?(:name)

#   name + (value ? ("(" + value.to_s + ")" ) : "")
# end

# def node_position(node)
#   node.position 
# end

# def display(node, indent="")
#   puts "#{indent}#{node_value(node)}: #{node_position(node)}"
#   node.child_nodes.each do |child|
#     display(child, indent + "  ")
#   end
# end


  # Parse the provided code into an AST with full syntax collection.
  # The 'r' is for rewriting...
  def rparse(code, version=1.8, scope=nil)
    root = PARSER.parse "<code>", source(code), config(version, scope, :all)
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
    [root, root.get_node_at(caret_index)]
  end

  ##
  # parse code but record all '^' found in source and return a list of all
  # nodes found at those carets.
  def carets_parse(code, version=1.8, caret='^', scope=nil)
    deloused_code, caret_indices = remove_carets(code, caret)
    root = parse deloused_code, version, scope
    [root, caret_indices.map { |e| root.get_node_at(e)}]
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
