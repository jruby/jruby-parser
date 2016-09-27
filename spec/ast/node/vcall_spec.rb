require_relative '../../helpers'

describe org.jrubyparser.ast.VCallNode do
  VERSIONS.each do |v|
    it "parses a 0-arg method call sans parens +extra line [#{v}]" do
#      rparse("\nputs\n", v).find_node(:vcall).tap do |call|
#        call.should have_name_and_position("puts", 1, 1, 1, 5)
#      end
    end    
  end
end
