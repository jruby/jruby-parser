$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require 'jruby-parser'
require 'parser_helpers'

describe JRubyParser do
  [JRubyParser::Compat::RUBY1_8, JRubyParser::Compat::RUBY1_9].each do |v|
    it "finds fcall via simple getNodeAt search" do
      caret_parse("b = fo^o(1)").tap do |root, caret_node|
        root.find_node(:fcall).should == caret_node
      end
      caret_parse("b = foo(^1)").tap do |root, caret_node|
        root.find_node(:fixnum).should == caret_node
      end
    end
  end
end
