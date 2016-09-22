require_relative '../../helpers'

describe org.jrubyparser.ast.WhileNode do
  it "can have an empty body" do
    rparse("while true\n;end\n").find_node(:while).tap do |b|
      expect(b.condition_node).to be_a org.jrubyparser.ast.TrueNode
    end
  end
end
