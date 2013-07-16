require_relative '../helpers'

describe JRubyParser do
  VERSIONS.each do |v|
    it "finds method via get_defined_scope [#{v}]" do
      caret_parse("def foo(^a); end", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:defn)
      end
      caret_parse("def foo(); ^a = 1; end", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:defn)
      end
      caret_parse("def foo(a); ^a; end", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:defn)
      end
      caret_parse("def foo(*^a); end", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:defn)
      end
      caret_parse("def foo(&^a); end", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:defn)
      end
      caret_parse("def foo(^a=1); end", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:defn)
      end

      caret_parse("def foo(a); proc { ^a }end", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:defn)
      end
      caret_parse("def foo(a);proc{proc{^a}};end", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:defn)
      end
      caret_parse("def foo(a);proc{proc{^a=1}};end", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:defn)
      end
    end

    it "finds iter via get_defined_scope [#{v}]" do
      caret_parse("proc { |^a| }", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:iter)
      end
      caret_parse("proc { || ^a=1}", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:iter)
      end
      caret_parse("proc { |a| ^a}", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:iter)
      end
      caret_parse("proc { |*^a| }", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:iter)
      end
      caret_parse("proc { |&^a| }", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root.find_node(:iter)
      end
      if v != 1.8
        caret_parse("proc { |^a=1| }", v).tap do |root, caret_node|
          caret_node.defined_scope.should == root.find_node(:iter)
        end
      end
      caret_parse("proc { |a| lambda { ^a }}", v).tap do |root, caret_node|
        caret_node.defined_scope.parent.name.should == root.find_node(:iter).parent.name
      end
    end

    it "finds root via get_defined_scope [#{v}]" do
      caret_parse("^a = 1", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root
      end
      caret_parse("a = 1; ^a", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root
      end
      caret_parse("a = 1; proc { ^a }", v).tap do |root, caret_node|
        caret_node.defined_scope.should == root
      end
    end

    it "finds proper scope per 1.8/1.9 rules for block vars [#{v}]" do
      if v == 1.8 
        caret_parse("a = 1; proc { |a| ^a }", v).tap do |root, caret_node|
          caret_node.defined_scope.should == root
        end
      else # 1.9+
        caret_parse("a = 1; proc { |a| ^a }", v).tap do |root, caret_node|
          caret_node.defined_scope.should == root.find_node(:iter)
        end
        caret_parse("a = 1; proc { |;a| ^a }", v).tap do |root, caret_node|
          caret_node.defined_scope.should == root.find_node(:iter)
        end
      end
    end
  end
end
