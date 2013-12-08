require_relative '../../helpers'

describe org.jrubyparser.ast.Node do
  VERSIONS.each do |v|
    it "finds all def variable occurrences via get_occurrences [#{v}]" do
      carets_parse("def foo(^a); ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      carets_parse("def foo(*^a); ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      carets_parse("def foo(^a=1); ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      if v != 1.8
        carets_parse("def foo(a=1, ^b); ^b; end", v).tap do |_, caret_nodes|
          caret_nodes.first.occurrences.to_a.should =~ caret_nodes
        end
      end
      carets_parse("def foo(&^a); ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      carets_parse("def foo(^a, b); p ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      carets_parse("def foo(^a); proc { ^a }; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      if v == 1.8
        carets_parse("def foo(^a); proc { |^a| ^a } end", v).tap do |_, caret_nodes|
          caret_nodes.first.occurrences.to_a.should =~ caret_nodes
        end
      else
        carets_parse("def foo(^a); proc { |a| a } end", v).tap do |_, caret_nodes|
          caret_nodes.first.occurrences.to_a.should =~ caret_nodes
        end
      end
      carets_parse("def foo(^a); ^a[:b] = 1; ^a[:b]; end", v).tap do |_, nodes|
        nodes.first.occurrences.to_a.should =~ nodes
      end
      carets_parse("def foo(^a); b = ^a ? {} : []; end", v).tap do |_, nodes|
        nodes.first.occurrences.to_a.should =~ nodes
      end
    end

    it "finds all root variable occurrences via get_occurrences [#{v}]" do
      carets_parse("^a=1; ^a; ^a if b", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      carets_parse("^a=1; def foo; a=1; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      carets_parse("a=1; def foo; ^a=1; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
    end

    it "finds all block variable occurrences via get_occurrences [#{v}]" do
      if v == 1.8
        carets_parse("def foo(^a); proc { |^a| ^a } end", v).tap do |_, caret_nodes|
          caret_nodes.first.occurrences.to_a.should =~ caret_nodes
        end
      else
        carets_parse("def foo(a); proc { |^a| ^a } end", v).tap do |_, caret_nodes|
          caret_nodes.first.occurrences.to_a.should =~ caret_nodes
        end
      end

      if v == 1.8
        carets_parse("proc { |^a| proc { |^a| ^a } }", v).tap do |_, caret_nodes|
          caret_nodes.last.occurrences.to_a.should =~ caret_nodes
        end
      else
        carets_parse("proc { |a| proc { |^a| ^a } }", v).tap do |_, caret_nodes|
          caret_nodes.last.occurrences.to_a.should =~ caret_nodes
        end
      end

      carets_parse("proc { |^a| ^a }", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      if v != 1.8
        carets_parse("proc { |^a=1| ^a }", v).tap do |_, caret_nodes|
          caret_nodes.first.occurrences.to_a.should =~ caret_nodes
        end
        carets_parse("proc { |a=1, ^b| ^b }", v).tap do |_, caret_nodes|
          caret_nodes.first.occurrences.to_a.should =~ caret_nodes
        end
      end
      carets_parse("proc { |&^a| ^a }", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
      carets_parse("proc { |*^a| ^a }", v).tap do |_, caret_nodes|
        caret_nodes.first.occurrences.to_a.should =~ caret_nodes
      end
    end
  end
end
