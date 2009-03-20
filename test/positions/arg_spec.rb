$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  it "should parse a no-arg method without parens" do
    ast = parse(<<-EOF)
def foo
end
    EOF

    defn = ast.find_node(:defn)
    defn.should have_name_and_position("foo", 0, 1, 0, 11)
    defn.args_node.max_arguments_count.should == 0
  end

  it "should parse a no-arg method with parens" do
    ast = parse(<<-EOF)
def foo()
end
    EOF

    defn = ast.find_node(:defn)
    defn.should have_name_and_position("foo", 0, 1, 0, 13)
    defn.args_node.max_arguments_count.should == 0
  end

  it "should parse a one-arg method without parens" do
    ast = parse(<<-EOF)
def foo a
end
    EOF

    defn = ast.find_node(:defn)
    defn.should have_name_and_position("foo", 0, 1, 0, 13)
    defn.args_node.max_arguments_count.should == 1
    pre = defn.args_node.pre
    pre.should have_position(0, 0, 8, 9)
    pre[0].should have_name_and_position("a", 0, 0, 8, 9)
  end

  it "should parse a one-arg method with parens" do
    ast = parse(<<-EOF)
def foo(a)
end
    EOF

    defn = ast.find_node(:defn)
    defn.should have_name_and_position("foo", 0, 1, 0, 14)
    defn.args_node.max_arguments_count.should == 1
    pre = defn.args_node.pre
    pre.should have_position(0, 0, 8, 9)
    pre[0].should have_name_and_position("a", 0, 0, 8, 9)
  end

  it "should parse a two-arg method without parens" do
    ast = parse(<<-EOF)
def foo a, b
end
    EOF

    defn = ast.find_node(:defn)
    defn.should have_name_and_position("foo", 0, 1, 0, 16)
    defn.args_node.max_arguments_count.should == 2
    pre = defn.args_node.pre
    pre.should have_position(0, 0, 8, 12)
    pre[0].should have_name_and_position("a", 0, 0, 8, 9)
    pre[1].should have_name_and_position("b", 0, 0, 11, 12)
  end

  it "should parse a two-arg method with parens" do
    ast = parse(<<-EOF)
def foo(a, b)
end
    EOF

    defn = ast.find_node(:defn)
    defn.should have_name_and_position("foo", 0, 1, 0, 17)
    defn.args_node.max_arguments_count.should == 2
    pre = defn.args_node.pre
    pre.should have_position(0, 0, 8, 12)
    pre[0].should have_name_and_position("a", 0, 0, 8, 9)
    pre[1].should have_name_and_position("b", 0, 0, 11, 12)
  end

  it "should parse a 1-optional_arg method without parens" do
    ast = parse(<<-EOF)
def foo a=1
end
    EOF

    defn = ast.find_node(:defn)
    defn.should have_name_and_position("foo", 0, 1, 0, 15)
    defn.args_node.required_args_count.should == 0
    defn.args_node.optional_args_count.should == 1
    defn.args_node.max_arguments_count.should == 1
    opt = defn.args_node.opt_args
    lasgn = opt.find_node(:localasgn)
    lasgn.should have_name_and_position("a", 0, 0, 8, 11)
  end

  it "should parse a 1-optional-arg method with parens" do
    ast = parse(<<-EOF)
def foo(a=1)
end
    EOF

    defn = ast.find_node(:defn)
    defn.should have_name_and_position("foo", 0, 1, 0, 16)
    defn.args_node.required_args_count.should == 0
    defn.args_node.optional_args_count.should == 1
    defn.args_node.max_arguments_count.should == 1
    opt = defn.args_node.opt_args
    lasgn = opt.find_node(:localasgn)
    lasgn.should have_name_and_position("a", 0, 0, 8, 11)
  end
end
