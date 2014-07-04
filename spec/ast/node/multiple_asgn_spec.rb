require_relative '../../helpers'

describe org.jrubyparser.ast.MultipleAsgnNode do
  VERSIONS.each do |v|
    it "can tell two nodes with a different rhs apart [#{v}]" do
      root = JRubyParser.parse("x, y, z = [1, 2, 3]")
      root2 = JRubyParser.parse("x, y, z = [1, 2, 4]")
      multi = root.find_node(:multipleasgn)
      multi2 = root2.find_node(:multipleasgn)
      expect(multi.is_same(multi2)).not_to be true
    end

    it "can tell two nodes with a different lhs apart [#{v}]" do
      root = JRubyParser.parse("x, y, z = [1, 2, 3]")
      root2 = JRubyParser.parse("a, b, c = [1, 2, 3]")
      multi = root.find_node(:multipleasgn)
      multi2 = root2.find_node(:multipleasgn)
      expect(multi.is_same(multi2)).not_to be true
    end

    it "can tell two nodes are the same [#{v}]" do
      root = JRubyParser.parse("x, y, z = [1, 2, 3]")
      root2 = JRubyParser.parse("x, y, z = [1, 2, 3]")
      multi = root.find_node(:multipleasgn)
      multi2 = root2.find_node(:multipleasgn)
      expect(multi.is_same(multi2)).to be true
    end
  end
end
