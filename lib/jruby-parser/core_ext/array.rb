require 'jruby'

class Array
  def to_ast_node(position = nil)
    inject(org.jrubyparser.ast.ArrayNode.new(position)) do |array, value|
      value = value.to_ast_node if value.respond_to? :to_ast_node
      array << value
    end
  end
end
