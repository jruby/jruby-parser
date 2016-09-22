require_relative '../../helpers'

tnode, fnode = org.jrubyparser.ast.TrueNode, org.jrubyparser.ast.FalseNode
inil = org.jrubyparser.ast.NilNode

describe org.jrubyparser.ast.ArrayNode do
  it "can have multiple elements" do
    parse("[true, false]").find_node(:array).tap do |list|
      expect(list.size).to eq 2
      expect(list.child_nodes.to_a.map(&:class)).to eq [tnode, fnode]
    end
  end

  it "can have nasty implcit nil elements" do
    parse("[()]").find_node(:array).tap do |list|
      expect(list.size).to eq 1
      expect(list.child_nodes.to_a.map(&:class)).to eq [inil]
    end
  end
end
