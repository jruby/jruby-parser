require_relative '../../helpers'

describe org.jrubyparser.ast.ReturnNode do
  VERSIONS.each do |v|
    it "can accept a single value [#{v}]" do
      rparse("return true", v).find_node(:return).tap do |b|
        b.should have_position(0, 0, 0, 11)
        b.value_node.class.should == org.jrubyparser.ast.TrueNode
      end
    end

    it "can accept no value [#{v}]" do
      rparse("return", v).find_node(:return).tap do |b|
        b.should have_position(0, 0, 0, 6)
        b.value_node.should == nil
      end
    end
  end
end
