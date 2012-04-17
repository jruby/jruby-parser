require 'java'

class org::jrubyparser::ast::CallNode
  def receiver=(value)
    # FIXME: Find common encapsulation for this logic
    value = value.to_ast_node if value.respond_to? :to_ast_node
    old_value = receiver
    value.position = old_value.position unless value.position    
    setReceiver(value)
  end
end

