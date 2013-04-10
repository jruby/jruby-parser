$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require 'jruby-parser'
require 'parser_helpers'

describe JRubyParser do
  [1.8, 1.9].each do |v|
    it "finds a variable's parameter declaration via get_declaration [#{v}]" do
      carets_parse("def foo(^a); ^a; end", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
      carets_parse("def foo(^a); a = 1; ^a; end", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
      carets_parse("def foo(*^a); a = 1; ^a; end", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
      carets_parse("def foo(&^a); a = 1; ^a; end", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
      carets_parse("def foo(^a=1); a = 1; ^a; end", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
    end
    it "finds a variable's lvar declaration via get_declaration [#{v}]" do
      carets_parse("def foo; ^a = 1; ^a; end", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
      carets_parse("def foo; ^a = 1; a = 2; ^a; end", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
    end
    it "finds a variable's parameter declaration via get_declaration [#{v}]" do
      carets_parse("proc {|^a| ^a}", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
      carets_parse("proc {|^a| a = 1; ^a}", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
      carets_parse("proc {|*^a| a = 1; ^a}", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
      carets_parse("proc {|&^a| a = 1; ^a}", v).tap do |_, caret_nodes|
        caret_nodes[1].declaration.should == caret_nodes[0]
      end
      if v != 1.8
        carets_parse("proc {|^a=1| a = 1; ^a}", v).tap do |_, caret_nodes|
          caret_nodes[1].declaration.should == caret_nodes[0]
        end
      end
    end
  end
end
