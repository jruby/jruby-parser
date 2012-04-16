class Fixnum
  def to_ast_node(position=nil)
    org.jrubyparser.ast.FixnumNode.new(nil, self)
  end
end
