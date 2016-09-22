require_relative '../../helpers'

describe org.jrubyparser.ast.OpElementAsgnNode do
  it "can parse simple expr" do
    parse("a[1] += 2").find_node(:opelementasgn).tap do |op|
      expect(op.operator_name).to eq '+'
      expect(op.receiver_node.name).to eq 'a'
      expect(op.args_node.find_node(:fixnum).value).to eq 1
      expect(op.value_node.value).to eq 2
    end
  end

  it "can parse &&= expressions with array subscript as its left hand side" do
    parse("a[1] &&= 2").find_node(:opelementasgn).tap do |op|
      expect(op.operator_name).to eq '&&'
      expect(op.receiver_node.name).to eq 'a'
      expect(op.args_node.find_node(:fixnum).value).to eq 1
      expect(op.value_node.value).to eq 2
    end
  end

  it "can parse ||= expressions with array subscript as its left hand side" do
    parse("a[1] ||= 2").find_node(:opelementasgn).tap do |op|
      expect(op.operator_name).to eq '||'
      expect(op.receiver_node.name).to eq 'a'
      expect(op.args_node.find_node(:fixnum).value).to eq 1
      expect(op.value_node.value).to eq 2
    end
  end
end
