require_relative '../../helpers'

describe org.jrubyparser.ast.CallNode do
  it "parses a 0-arg object.method call without parens " do
    rparse("Array.new").find_node(:call).tap do |call|
      expect(call).to have_name 'new'
      expect(call.receiver_node).to have_name('Array')
      expect(call.args_node).to be_nil
    end
  end

  it "parses a 0-arg object.method call with parens " do
    rparse("Array.new()").find_node(:call).tap do |call|
      expect(call).to have_name 'new'
      expect(call.receiver_node).to have_name('Array')
      expect(call.args_node).to be_nil
    end
  end

  it "parses a 1-arg object.method call without parens" do
    rparse("Array.new 1").find_node(:call).tap do |call|
      expect(call).to have_name 'new'
      expect(call.receiver_node).to have_name('Array')
      expect(call.args_node.size).to eq 1
    end
  end

  it "parses a 1-arg object.method call with parens" do
    rparse("Array.new(1)").find_node(:call).tap do |call|
      expect(call).to have_name 'new'
      expect(call.receiver_node).to have_name('Array')
      expect(call.args_node.size).to eq 1
    end
  end

  it "parses a 1-arg infix method" do
    rparse("4 + 5").find_node(:call).tap do |call|
      expect(call).to have_name '+'
      expect(call.receiver_node.value).to eq 4
      expect(call.args_node.size).to eq 1
    end
  end

  it "parses a 1-arg object.method call with infix operator as arg" do
    rparse("Array.new 4 + 5").find_node(:call).tap do |call|
      expect(call).to have_name 'new'
      expect(call.receiver_node).to have_name('Array')
      expect(call.args_node.size).to eq 1
    end
  end

  it "parses unary ! call with parenthesis" do
    rparse("!(x < 5)").find_node(:call).tap do |call|
      expect(call).to have_name '!'
      expect(call.receiver_node.name).to eq '<'
    end
  end
end
