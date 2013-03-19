$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require 'jruby-parser'
require 'parser_helpers'

describe JRubyParser do
  [1.8, 1.9].each do |v|
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
