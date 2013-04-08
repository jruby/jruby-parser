$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require 'jruby-parser'
require 'parser_helpers'

describe JRubyParser do
  [1.8, 1.9].each do |v|
    it "finds all def variable occurences via get_occurences [#{v}]" do
      carets_parse("def foo(^a); ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      carets_parse("def foo(^*a); ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      carets_parse("def foo(^a=1); ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      if v == 1.9
        carets_parse("def foo(a=1, ^b); ^b; end", v).tap do |_, caret_nodes|
          caret_nodes.first.occurences =~ caret_nodes
        end
      end
      carets_parse("def foo(&^a); ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      carets_parse("def foo(^a, b); p ^a; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      carets_parse("def foo(^a); proc { ^a }; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      carets_parse("def foo(^a); proc { |a| a } end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
    end

    it "finds all root variable occurences via get_occurences [#{v}]" do
      carets_parse("^a=1; ^a; ^a if b", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      carets_parse("^a=1; def foo; a=1; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      carets_parse("a=1; def foo; ^a=1; end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
    end

    it "finds all block variable occurences via get_occurences [#{v}]" do
      carets_parse("def foo(a); proc { |^a| ^a } end", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      carets_parse("proc { |^a| ^a }", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      if v == 1.9
        carets_parse("proc { |^a=1| ^a }", v).tap do |_, caret_nodes|
          caret_nodes.first.occurences =~ caret_nodes
        end
        carets_parse("proc { |a=1, ^b| ^b }", v).tap do |_, caret_nodes|
          caret_nodes.first.occurences =~ caret_nodes
        end
      end
      carets_parse("proc { |&^a| ^a }", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
      carets_parse("proc { |*^a| ^a }", v).tap do |_, caret_nodes|
        caret_nodes.first.occurences =~ caret_nodes
      end
    end
  end
end
