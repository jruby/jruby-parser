class TrueClass
  def to_ast_node(position=nil)
    org.jrubyparser.ast.TrueNode.new(position)
  end
end

class FalseClass
  def to_ast_node(position=nil)
    org.jrubyparser.ast.FalseNode.new(position)
  end
end