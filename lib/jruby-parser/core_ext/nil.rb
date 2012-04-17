require 'java'

class NilClass
  def to_ast_node(position=nil)
    org.jrubyparser.ast.NilNode.new(position)
  end
end
