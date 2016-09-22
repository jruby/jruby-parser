require 'java'

class org::jrubyparser::ast::ArgsNode
  def normative_parameter_names(names_only = false)
    names = (pre.child_nodes.to_a +
             opt_args.child_nodes.to_a +
             post.child_nodes.to_a).map { |node| node.name if node }
                                   .select { |a| a != nil }
    if names_only
      rest_arg_node && (names << rest_arg_node.name)
      block && (names << block.name)
    else
      rest_arg_node && (names << "*#{rest_arg_node.name}")
      block && (names << "&#{block.name}")
    end
    names
  end
end
