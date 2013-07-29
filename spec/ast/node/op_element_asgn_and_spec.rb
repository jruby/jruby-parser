require_relative '../../helpers'

describe org.jrubyparser.ast.OpElementAsgnAndNode do
  VERSIONS.each do |v|
    it "can parse simple expr [#{v}]" do
      parse("a[1] &&= 2", v).find_node(:opelementasgnand).tap do |op|
        op.should have_position(0, 0, 0, 10)
      end
    end
  end
end
