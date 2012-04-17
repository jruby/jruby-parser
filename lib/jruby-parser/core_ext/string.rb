require 'java'

class String
  def to_ast_node(position=nil)
    org.jrubyparser.ast.StrNode.new(position, self.to_java(:string))
  end
end
