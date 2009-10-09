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

    call = ast.find_node(:vcall)
    call.should have_name_and_position("puts", 1, 1, 1, 5)
  end
  
  it "should parse a no-arg method call with parens" do
    ast = parse(<<-EOF)
puts()
    EOF

    call = ast.find_node(:fcall)
    call.should have_name_and_position("puts", 0, 0, 0, 6)
    call.args_node.size.should == 0
  end

  it "should parse a one-arg method call with parens" do
    ast = parse(<<-EOF)
puts(1)
    EOF

    call = ast.find_node(:fcall)
    call.should have_name_and_position("puts", 0, 0, 0, 7)
    call.args_node.size.should == 1
  end

  it "should parse a one-arg method call without parens" do
    ast = parse(<<-EOF)
puts 1
    EOF

    call = ast.find_node(:fcall)
    call.should have_name_and_position("puts", 0, 0, 0, 6)
    call.args_node.size.should == 1
  end

  it "should parse a two-arg method call with parens" do
    ast = parse(<<-EOF)
puts(1,2)
    EOF

    call = ast.find_node(:fcall)
    call.should have_name_and_position("puts", 0, 0, 0, 9)
    call.args_node.size.should == 2
  end

  it "should parse a two-arg method call without parens" do
    ast = parse(<<-EOF)
puts 1, 2
    EOF

    call = ast.find_node(:fcall)
    call.should have_name_and_position("puts", 0, 0, 0, 9)
    call.args_node.size.should == 2
  end

  it "should parse a no-arg object.method call without parens" do
    ast = parse(<<-EOF)
Array.new
    EOF

    call = ast.find_node(:call)
    call.should have_name_and_position("new", 0, 0, 0, 9)
    call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
    call.args_node.size.should == 0
  end

  it "should parse a no-arg object.method call with parens" do
    ast = parse(<<-EOF)
Array.new()
    EOF

    call = ast.find_node(:call)
    call.should have_name_and_position("new", 0, 0, 0, 11)
    call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
    call.args_node.size.should == 0
  end

  it "should parse a one-arg object.method call without parens" do
    ast = parse(<<-EOF)
Array.new 1
    EOF

    call = ast.find_node(:call)
    call.should have_name_and_position("new", 0, 0, 0, 11)
    call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
    call.args_node.size.should == 1
  end

  it "should parse a one-arg object.method call with parens" do
    ast = parse(<<-EOF)
Array.new(1)
    EOF

    call = ast.find_node(:call)
    call.should have_name_and_position("new", 0, 0, 0, 12)
    call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
    call.args_node.size.should == 1
  end
  
  it "should parse a one-arg infix method" do
    ast = parse(<<-EOF)
4 + 5
    EOF

    call = ast.find_node(:call)
    call.should have_name_and_position("+", 0, 0, 0, 5)
    call.receiver_node.should have_position(0, 0, 0, 1)
    call.args_node.size.should == 1
  end

  it "should parse a one-arg object.method call with infix operator as argument" do
    ast = parse(<<-EOF)
Array.new 4 + 5
    EOF

    call = ast.find_node(:call)
    call.should have_name_and_position("new", 0, 0, 0, 15)
    call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
    call.args_node.size.should == 1
  end

  it "should parse an empty method with a block({})" do
    ast = parse(<<-EOF)
foo() {}
    EOF

    call = ast.find_node(:fcall)
    call.should have_name_and_position("foo", 0, 0, 0, 8)
    call.iter_node.should have_position(0, 0, 6, 8)
  end

  it "should parse an empty method with a block(do...end)" do
    ast = parse(<<-EOF)
foo() do
end
    EOF

    call = ast.find_node(:fcall)
    call.should have_name_and_position("foo", 0, 1, 0, 12)
    call.iter_node.should have_position(0, 1, 6, 12)
  end

  it "should parse an empty method with a block({})" do
    ast = parse(<<-EOF)
foo() { |a| }
    EOF

    call = ast.find_node(:fcall)
    call.should have_name_and_position("foo", 0, 0, 0, 13)
    call.iter_node.should have_position(0, 0, 6, 13)

    # FIXME: Need arg tests
  end

  it "should parse an empty method with a block(do...end)" do
    ast = parse(<<-EOF)
foo() do |a|
end
    EOF

    call = ast.find_node(:fcall)
    call.should have_name_and_position("foo", 0, 1, 0, 16)
    call.iter_node.should have_position(0, 1, 6, 16)

    # FIXME: Need arg tests
  end

  it "should parse an nil reciever" do
   ast = parse("().foo")
   call = ast.find_node(:call)
   call.receiver_node.should_not be_nil 
  end

  it "should parse an nil receiver in 1.9 mode" do
   ast = parse("().foo", $config_1_9)
   call = ast.find_node(:call)
   call.receiver_node.should_not be_nil
  end

end
