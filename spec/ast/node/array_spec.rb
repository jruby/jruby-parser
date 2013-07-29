require_relative '../../helpers'

tnode, fnode = org.jrubyparser.ast.TrueNode, org.jrubyparser.ast.FalseNode
inil = org.jrubyparser.ast.ImplicitNilNode

describe org.jrubyparser.ast.ArrayNode do
  VERSIONS.each do |v|
    it "can have multiple elements [#{v}]" do
      parse("[true, false]", v).find_node(:array).tap do |list|
        list.should have_position(0, 0, 0, 13)
        list.size.should == 2
        list.child_nodes.to_a.map(&:class).should == [tnode, fnode]
      end
    end

    it "can have nasty implcit nil elements [#{v}]" do
      parse("[()]", v).find_node(:array).tap do |list|
        list.should have_position(0, 0, 0, 4)
        list.size.should == 1
        list.child_nodes.to_a.map(&:class).should == [inil]
      end
    end
  end
end
