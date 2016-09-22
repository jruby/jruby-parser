require_relative '../../helpers'

describe org.jrubyparser.ast.VCallNode do
  it "parses a 0-arg method call sans parens +extra line" do
    rparse("\nputs\n").find_node(:vcall).tap do |call|
      expect(call).to have_name('puts')
    end
  end
end
