require 'java'

import org.jrubyparser.SourcePosition
import org.jrubyparser.ast.Node

class Node
  def short_name
    java_class.name.gsub(/(^.*\.|Node$)/, '').downcase
  end

  # Find first node by name (which is short_name)
  def find_node(name)
    name = name.to_s
    return self if name == short_name

    child_nodes.each do |child|
      value = child.find_node(name)

      return value if value
    end
    nil
  end

  alias :find :find_node

  def [](value)
    child_nodes[value]
  end
end

class SourcePosition
  def to_a
    [start_line, end_line, start_offset, end_offset]
  end
end

###########

class AstPositionMatcher
  def initialize(*args)
    @position = args
  end
 
  def matches?(actual)
    @actual = actual
    actual.position.to_a == @position
  end
 
  def failure_message
    return %[expected #{@actual.position.to_a.inspect} to have position #{@position.inspect}]
  end
 
  def negative_failure_message
    return %[expected #{@actual.position.to_a.inspect} to not have position #{@position.inspect}]
  end
end
 
module HavePosition
  def have_position(*args)
    AstPositionMatcher.new(*args)
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
 
  def negative_failure_message
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
 
  def negative_failure_message
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
    @actual = [args.pre_count, args.optional_count, args.rest != nil, args.block != nil]
    @args == @actual
  end

  def failure_message
    return %[expected #{@actual.inspect} to have name #{@args.inspect}]
  end

  def negative_failure_message
    return %[expected #{@actual.inspect} to not have name #{@args.inspect}]
  end
end

module HaveArgCounts
  def have_arg_counts(*args)
    ArgCountsMatcher.new(*args)
  end
end
 
#module Spec::Example::ExampleMethods
class Object
  include HavePosition
  include HaveName
  include HaveNameAndPosition
  include HaveArgCounts
end
