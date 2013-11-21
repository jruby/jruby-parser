require_relative '../../helpers'

describe org.jrubyparser.ast.NotNode do
  VERSIONS.each do |v|
    if v == 1.8
      it "parses unary ! call with parenthesis [#{v}]" do
        rparse("!(x < 5)", v).find_node(:not).tap do |call|
          call.should have_position(0, 0, 0, 8)
        end
      end
    end
  end
end
