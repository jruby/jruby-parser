require_relative '../helpers'

describe JRubyParser do
  VERSIONS.each do |v|
    it "finds  parameter via is_method_parameter [#{v}]" do
      caret_parse("def foo(^a); end", v).tap do |root, caret_node|
        caret_node.method_parameter?.should == true
      end
      caret_parse("def foo(^a,b); end", v).tap do |root, caret_node|
        caret_node.method_parameter?.should == true
      end
      caret_parse("def foo(*^a); end", v).tap do |root, caret_node|
        caret_node.method_parameter?.should == true
      end
      caret_parse("def foo(&^a); end", v).tap do |root, caret_node|
        caret_node.method_parameter?.should == true
      end
      caret_parse("def foo(a, ^b=1); end", v).tap do |root, caret_node|
        caret_node.method_parameter?.should == true
      end
      if v != 1.8
        caret_parse("def foo(a, (b, ^c)); end", v).tap do |root, caret_node|
          caret_node.method_parameter?.should == true
        end
        caret_parse("def foo(a, b=1, ^c); end", v).tap do |root, caret_node|
          caret_node.method_parameter?.should == true
        end
      end
    end
  end
end
