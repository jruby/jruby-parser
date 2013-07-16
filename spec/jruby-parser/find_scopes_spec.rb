require_relative '../helpers'

describe JRubyParser do
  VERSIONS.each do |v|
    it "children can ask for the method it is contained in [#{v}]" do
      parse("def foo; true if false; end").find_node(:defn) do |defn|
        defn.find_node(:true).method_for.should == defn
        defn.find_node(:false).method_for.should == defn
        defn.find_node(:if).method_for.should == defn
      end
    end

    it "children can ask for the iter/block it is contained in [#{v}]" do
      parse("proc { |a| proc { |b| true } }").find_node(:iter) do |iter1|
        iter1.find_node(:true).innermost_iter.should_not == iter1
        iter1.find_node(:true).outermost_iter.should == iter1
      end
    end

    it "should not find an innermost block if method is inside block" do
      parse("proc { def foo; true; end }").find_node(:true) do |tru|
        tru.innermost_iter.should == nil
        tru.outermost_iter.should == nil
      end
    end
  end
end
