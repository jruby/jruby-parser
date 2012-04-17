require 'java'

class org::jrubyparser::ast::Node
  ##
  # Find nth child element of this node
  #
  def [](value)
    child_nodes[value]
  end

  ##
  # Replace the nth child element of this node with the specified value.
  # if the value is an actual Ruby value it will attempt to call to_ast_node
  # on it to do automatic coercion to an AST node.  If the node does not
  # contain positioning information then it will just use the
  # old nodes information
  def []=(index, value)
    value = value.to_ast_node if value.respond_to? :to_ast_node

    old_value = child_nodes[index]
    value.position = old_value.position unless value.position
    child_nodes[index] = value
  end

  ##
  # Find first node by name (which is short_name of actual node)
  # === parameters
  # * _name_ is the name of the class you want to find
  # === examples
  # 
  # root.find_node(:fcall) # Find first child node of type fcall (depth-first)
  #
  def find_node(name)
    name = name.to_s
    return self if name == short_name

    child_nodes.each do |child|
      value = child.find_node(name)

      return value if value
    end
    nil
  end

  ##
  # Convert this node back to human-readable source code.
  #
  def to_source(opts = {})
    filename = opts[:filename] ? opts[:filename] : '(string)'
    java.io.StringWriter.new.tap do |writer|
      accept org.jrubyparser.rewriter.ReWriteVisitor.new(writer, filename)
    end.to_s
  end

  def short_name
    java_class.name.gsub(/(^.*\.|Node$)/, '').downcase
  end
  private :short_name
end
