require_relative '../../helpers'

describe org.jrubyparser.ast.AliasNode do
  VERSIONS.each do |v|
    it "should parse a hash literal (a=>b) [#{v}]" do
      rparse("{:one => 1, :two => 2}", v).find_node(:hash).tap do |hash|
        hash.should have_position(0, 0, 0, 22)
      end
    end

    if v == 1.8
      it "should parse a hash literal (a,b) [#{v}]" do
        rparse("{:one, 1, :two, 2}", v).find_node(:hash).tap do |hash|
          hash.should have_position(0, 0, 0, 18)
        end
      end
    else
      it "should parse a hash literal (a: b) [#{v}]" do
        rparse("{one: 1, two: 2}", v).find_node(:hash).tap do |hash|
          hash.should have_position(0, 0, 0, 16)
        end
      end
    end
  end
end
