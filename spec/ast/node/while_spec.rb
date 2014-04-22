require_relative '../../helpers'

describe org.jrubyparser.ast.WhileNode do
  VERSIONS.each do |v|
    it "can have an empty body [#{v}]" do
      rparse("while true\n;end\n", v).find_node(:while).tap do |b|
        b.should have_position(0, 1, 0, 15)
      end
    end
  end
end
