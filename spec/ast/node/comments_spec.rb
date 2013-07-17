require_relative '../../helpers'

describe org.jrubyparser.ast.Node do
  VERSIONS.each do |v|
    it "can see a single previous comment [#{v}]" do
      rparse("# c1\ndef foo; end", v).find_node(:defn).tap do |defn|
        defn.previous_comments.to_a.map(&:content).should =~ ["# c1"]
      end

      rparse("# c1\ndef self.[]\nend", v).find_node(:defs).tap do |defs|
        defs.previous_comments.to_a.map(&:content).should =~ ["# c1"]
      end

      rparse("# c1\nclass Foo\nend", v).find_node(:class).tap do |defn|
        defn.previous_comments.to_a.map(&:content).should =~ ["# c1"]
      end

      rparse("# c1\nif 1; end", v).find_node(:if).tap do |ifn|
        ifn.previous_comments.to_a.map(&:content).should =~ ["# c1"]
      end
    end

    it "can see a multiple previous comments [#{v}]" do
      rparse("# c1\n# c2\ndef foo; end", v).find_node(:defn).tap do |defn|
        defn.previous_comments.to_a.map(&:content).should =~ ["# c1", "# c2"]
      end

      rparse("# c1\n# c2\nclass Foo\nend", v).find_node(:class).tap do |defn|
        defn.previous_comments.to_a.map(&:content).should =~ ["# c1", "# c2"]
      end

      rparse("# c1\n# c2\nif 1\nend", v).find_node(:if).tap do |ifn|
        ifn.previous_comments.to_a.map(&:content).should =~ ["# c1", "# c2"]
      end
    end

    it "can see an inline comment [#{v}]" do
      rparse("1 + 2 # inline").find_node(:call).tap do |call|
        call.inline_comment.content.should == "# inline"
      end
    end
  end
end
