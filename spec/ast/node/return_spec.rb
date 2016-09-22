require_relative '../../helpers'

describe org.jrubyparser.ast.ReturnNode do
  it "can accept a single value" do
    rparse("return true").find_node(:return).tap do |b|
      expect(b.value_node).to be_a org.jrubyparser.ast.TrueNode
    end
  end

  it "can accept no value" do
    rparse("return").find_node(:return).tap do |b|
      expect(b.value_node).to be_nil
    end
  end
end
