require 'java'
require 'jruby-parser/util/coercer'

class org::jrubyparser::ast::ListNode
  def <<(value)
    value = value.to_ast_node if value.respond_to? :to_ast_node
    value.position = getPosition unless value.position
    add(value)
  end
end
