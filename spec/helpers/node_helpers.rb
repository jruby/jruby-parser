require 'java'

import org.jrubyparser.ast.Node

###########

class AstPositionMatcher
  def initialize(method, *args)
    @method, @position = method, args
  end

  def position
    @actual.__send__(@method).to_a
  end

  def matches?(actual)
    @actual = actual
    position == @position
  end

  def failure_message
    return %[expected #{position.inspect} to have position #{@position.inspect}]
  end

  def failure_message_when_negated
    return %[expected #{position.inspect} to not have position #{@position.inspect}]
  end
end

module HavePosition
  def have_position(*args)
    AstPositionMatcher.new(:position, *args)
  end
end

module HaveNamePosition
  def have_name_position(*args)
    AstPositionMatcher.new(:name_position, *args)
  end
end

class AstNameMatcher
  def initialize(name)
    @name = name
  end

  def matches?(actual)
    @actual = actual
    actual.name == @name
  end

  def failure_message
    return %[expected #{@actual.inspect} to have name #{@name.inspect}]
  end

  def failure_message_when_negated
    return %[expected #{@actual.inspect} to not have name #{@name.inspect}]
  end
end

module HaveName
  def have_name(name)
    AstNameMatcher.new(name)
  end
end

class AstNameAndPositionMatcher
  def initialize(name, *args)
    @name, @position = name, args
  end

  def matches?(actual)
    @actual = actual
    actual.name == @name && actual.position.to_a == @position
  end

  def failure_message
    return %[expected #{@actual.name.inspect}, #{@actual.position.to_a.inspect} to have name and position #{@name.inspect}, #{@position.inspect}]
  end

  def failure_message_when_negated
    return %[expected #{@actual.name.inspect}, #{@actual.position.to_a.inspect} to not have and position #{@name.inspect}, #{@position.inspect}]
  end
end

module HaveNameAndPosition
  def have_name_and_position(*args)
    AstNameAndPositionMatcher.new(*args)
  end
end

class ArgCountsMatcher
  def initialize(pre, optional, rest, block)
    @args = [pre, optional, rest, block]
  end

  def matches?(args)
    @actual = [args.pre_count, args.optional_args_count, args.rest_arg_node != nil, args.block != nil]
    @args == @actual
  end

  def failure_message
    return %[expected #{@actual.inspect} to have name #{@args.inspect}]
  end

  def failure_message_when_negated
    return %[expected #{@actual.inspect} to not have name #{@args.inspect}]
  end
end

module HaveArgCounts
  def have_arg_counts(*args)
    ArgCountsMatcher.new(*args)
  end
end

class HaveParametersMatcher
  def initialize(*list)
    @with_decorations = []
    @without_decorations = []
    list.each do |e1, e2|
      @without_decorations << e1
      @with_decorations << (e2 || e1)
    end
  end

  def matches?(args)
    @actual_with = args.normative_parameter_names(false)
    @actual_without = args.normative_parameter_names(true)
    @with_decorations == @actual_with && @without_decorations == @actual_without
  end

  def failure_message
    error = ""
    if @with_decorations != @actual_with
      error << %[expected #{@actual_with.inspect} to have #{@with_decorations.inspect}.]
    end

    if @without_decorations != @actual_without
      error << %[ expected #{@actual_without.inspect} to have #{@without_decorations.inspect}.]
    end
    error
  end

  def failure_message_when_negated
    error = ""
    if @with_decorations != @actual_with
      error << %[expected #{@actual_with.inspect} to not have #{@with_decorations.inspect}.]
    end

    if @without_decorations != @actual_without
      error << %[ expected #{@actual_without.inspect} to not have #{@without_decorations.inspect}.]
    end
    error
  end
end

module HaveParameters
  def have_parameters(*args)
    HaveParametersMatcher.new(*args)
  end
end

class HaveStaticAssignmentsMatcher
  java_import org.jrubyparser.ast.FixnumNode
  java_import org.jrubyparser.ast.ListNode

  def initialize(list)
    @list = list
  end

  def matches?(args)
    @actual_list = args.calculateStaticAssignments.to_a
    @failures_var_names = []
    @failures_var_values = []

    @list.each_with_index do |arr, i|
      node_pair = @actual_list[i]
      expected_var, expected_value = arr[0].to_s, arr[1]
      actual_var, actual_value = node_pair.first.name, value_for(node_pair.second)

      @failures_var_names << [expected_var, actual_var] if expected_var != actual_var
      @failures_var_values << [expected_value, actual_value] if expected_value != actual_value

    end

    @failures_var_names.size == 0 && @failures_var_values.size == 0
  end

   def value_for(node)
    if node.kind_of? FixnumNode
      node.value
    elsif node.kind_of? ListNode
      node.child_nodes.map {|e| value_for(e)}
    else
      node
    end
  end

  def failure_message
    names = @failures_var_names.inject("names: ") do |s, e|
      s << "#{e[0]} != #{e[1]} "
    end

    @failures_var_values.inject(names + "\nvalues: ") do |s, e|
      s << "#{e[0]} != #{e[1]} "
    end
  end

  def failure_message_when_negated
    names = @failures_var_names.inject("names: ") do |s, e|
      s << "#{e[0]} !!= #{e[1]}"
    end

    @failures_var_values.inject(names + "\nvalues: ") do |s, e|
      s << "#{e[0]} !!= #{e[1]} "
    end
  end
end

module HaveStaticAssignments
  def have_static_assignments(*args)
    HaveStaticAssignmentsMatcher.new(*args)
  end
end

class HaveBlockMatcher
  def matches?(actual)
    block = actual.iter_node
    block && block.is_a?(org.jrubyparser.ast.IterNode)
  end

  def failure_message
    'expected call to have block'
  end

  def failure_message_when_negated
    'expected call to not have block'
  end
end

module HaveBlock
  def have_block
    HaveBlockMatcher.new
  end
end

class HaveKeywordsMatcher
  def initialize(**args)
    @expected_args = args
  end

  def matches?(args_node)
    @actual_args = args_node.keywords.child_nodes.each_with_object({}) do |node, args|
      value_node = node.assignable.value_node
      args[node.assignable.name.to_sym] = value_node.class
    end
    @expected_args == @actual_args
  end

  def failure_message
    %[expected #{@actual_args.inspect} to have #{@expected_args.inspect}.]
  end

  def failure_message_when_negated
    %[expected #{@actual_args.inspect} to not have #{@expected_args.inspect}.]
  end
end

module HaveKeywords
  def have_keywords(**args)
    HaveKeywordsMatcher.new(**args)
  end
end
#module Spec::Example::ExampleMethods
class Object
  include HavePosition
  include HaveNamePosition
  include HaveName
  include HaveNameAndPosition
  include HaveArgCounts
  include HaveParameters
  include HaveStaticAssignments
  include HaveBlock
  include HaveKeywords
end
