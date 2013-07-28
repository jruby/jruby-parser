require_relative '../../helpers'

describe org.jrubyparser.ast.NextNode do
  VERSIONS.each do |v|
    it "can accept a single value [#{v}]" do
      rparse("next true", v).find_node(:next).tap do |b|
        b.should have_position(0, 0, 0, 9)
        b.value_node.class.should == org.jrubyparser.ast.TrueNode
      end
    end

    it "can accept no value [#{v}]" do
      rparse("next", v).find_node(:next).tap do |b|
        b.should have_position(0, 0, 0, 4)
        b.value_node.should == nil
      end
    end
  end
end
