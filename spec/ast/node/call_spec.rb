require_relative '../../helpers'

describe org.jrubyparser.ast.CallNode do
  VERSIONS.each do |v|
    it "parses a 0-arg object.method call without parens [#{v}]" do
      rparse("Array.new", v).find_node(:call).tap do |call|
        call.should have_name_and_position("new", 0, 0, 0, 9)
        call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
        call.args_node.size.should == 0
      end
    end

    it "parses a 0-arg object.method call with parens [#{v}]" do
      rparse("Array.new()", v).find_node(:call).tap do |call|
        call.should have_name_and_position("new", 0, 0, 0, 11)
        call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
        call.args_node.size.should == 0
      end
    end

    it "parses a 1-arg object.method call without parens [#{v}]" do
      rparse("Array.new 1", v).find_node(:call).tap do |call|
        call.should have_name_and_position("new", 0, 0, 0, 11)
        call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
        call.args_node.size.should == 1
      end
    end

    it "parses a 1-arg object.method call with parens [#{v}]" do
      rparse("Array.new(1)", v).find_node(:call).tap do |call|
        call.should have_name_and_position("new", 0, 0, 0, 12)
        call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
        call.args_node.size.should == 1
      end
    end
    
    it "parses a 1-arg infix method [#{v}]" do
      rparse("4 + 5", v).find_node(:call).tap do |call|
        call.should have_name_and_position("+", 0, 0, 0, 5)
        call.receiver_node.should have_position(0, 0, 0, 1)
        call.args_node.size.should == 1
      end
    end

    it "parses a 1-arg object.method call with infix operator as arg [#{v}]" do
      rparse("Array.new 4 + 5", v).find_node(:call).tap do |call|
        call.should have_name_and_position("new", 0, 0, 0, 15)
        call.receiver_node.should have_name_and_position("Array", 0, 0, 0, 5)
        call.args_node.size.should == 1
      end
    end

    if v != 1.8  # In 1.8 this is a NotNode (see not_spec.rb)
      it "parses unary ! call with parenthesis [#{v}]" do
        rparse("!(x < 5)", v).find_node(:call).tap do |call|
          call.should have_name_and_position("!", 0, 0, 0, 8)
        end
      end
    end
  end
end
