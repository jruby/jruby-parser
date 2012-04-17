require 'java'

class Fixnum
  def to_ast_node(position=nil)
    org.jrubyparser.ast.FixnumNode.new(position, self)
  end
end

class Float
  def to_ast_node(position=nil)
    org.jrubyparser.ast.FloatNode.new(position, self)
  end
end