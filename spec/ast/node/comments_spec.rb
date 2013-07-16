require_relative '../../helpers'

describe org.jrubyparser.ast.Node do
  VERSIONS.each do |v|
    pending do
    it "finds parameter via is_block_parameter [#{v}]" do
      rparse("# comment 1\ndef foo; end", v).find_node(:defn).tap do |defn|
        defn.previous_comments.to_a.should =~ ["# comment 1"]
      end
    end
    end
  end
end
