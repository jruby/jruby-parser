require_relative '../../helpers'

describe org.jrubyparser.ast.BreakNode do
  it "can accept a single value" do
    rparse("break true").find_node(:break).tap do |b|
      expect(b.value_node.class).to eq org.jrubyparser.ast.TrueNode
    end
  end

  it "can accept no value" do
    rparse("break").find_node(:break).tap do |b|
      expect(b.value_node.class).to eq org.jrubyparser.ast.NilImplicitNode
    end
  end
end
