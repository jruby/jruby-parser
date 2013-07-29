require_relative '../../helpers'

describe org.jrubyparser.ast.BreakNode do
  VERSIONS.each do |v|
    it "can accept a single value [#{v}]" do
      rparse("break true", v).find_node(:break).tap do |b|
        b.should have_position(0, 0, 0, 10)
        b.value_node.class.should == org.jrubyparser.ast.TrueNode
      end
    end

    it "can accept no value [#{v}]" do
      rparse("break", v).find_node(:break).tap do |b|
        b.should have_position(0, 0, 0, 5)
        b.value_node.should == nil
      end
    end
  end
end
