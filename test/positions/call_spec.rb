$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  it "should parse a no-arg method call without parens with extra line" do
    ast = parse(<<-EOF)

puts
    EOF

    defn = ast.find_node(:vcall)
    defn.should have_name_and_position("puts", 1, 1, 1, 5)
  end
  
  it "should parse a no-arg method call with parens" do
    ast = parse(<<-EOF)
puts()
    EOF

    defn = ast.find_node(:fcall)
    defn.should have_name_and_position("puts", 0, 0, 0, 6)
    defn.args_node.size.should == 0
  end

  it "should parse a one-arg method call with parens" do
    ast = parse(<<-EOF)
puts(1)
    EOF

    defn = ast.find_node(:fcall)
    defn.should have_name_and_position("puts", 0, 0, 0, 7)
    defn.args_node.size.should == 1
  end

  it "should parse a one-arg method call without parens" do
    ast = parse(<<-EOF)
puts 1
    EOF

    defn = ast.find_node(:fcall)
    defn.should have_name_and_position("puts", 0, 0, 0, 6)
    defn.args_node.size.should == 1
  end

  it "should parse a two-arg method call with parens" do
    ast = parse(<<-EOF)
puts(1,2)
    EOF

    defn = ast.find_node(:fcall)
    defn.should have_name_and_position("puts", 0, 0, 0, 9)
    defn.args_node.size.should == 2
  end

  it "should parse a two-arg method call without parens" do
    ast = parse(<<-EOF)
puts 1, 2
    EOF

    defn = ast.find_node(:fcall)
    defn.should have_name_and_position("puts", 0, 0, 0, 9)
    defn.args_node.size.should == 2
  end

  it "should parse a no-arg object.method call without parens" do
    ast = parse(<<-EOF)
Array.new
    EOF

    defn = ast.find_node(:call)
    defn.should have_name_and_position("new", 0, 0, 0, 9)
    defn.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
    defn.args_node.size.should == 0
  end
end
