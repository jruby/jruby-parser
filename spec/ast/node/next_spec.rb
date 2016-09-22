require_relative '../../helpers'

describe org.jrubyparser.ast.NextNode do
  VERSIONS.each do |v|
    it "can accept a single value" do
      rparse("next true").find_node(:next).tap do |b|
        expect(b).to be_a org.jrubyparser.ast.NextNode
        expect(b.value_node).to be_a org.jrubyparser.ast.TrueNode
      end
    end

    it "can accept no value" do
      rparse("next").find_node(:next).tap do |b|
        expect(b).to be_a org.jrubyparser.ast.NextNode
        expect(b.value_node).to be_nil
      end
    end
  end
end
