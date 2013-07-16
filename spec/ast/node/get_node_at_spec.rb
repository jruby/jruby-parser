require_relative '../../helpers'

describe org.jrubyparser.ast.Node do
  VERSIONS.each do |v|
    it "finds fcall via simple getNodeAt search [#{v}]" do
      caret_parse("b = fo^o(1)", v).tap do |root, caret_node|
        root.find_node(:fcall).should == caret_node
      end
      caret_parse("b = foo(^1)", v).tap do |root, caret_node|
        root.find_node(:fixnum).should == caret_node
      end
    end
  end
end
