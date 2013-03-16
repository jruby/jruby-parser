$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require 'jruby-parser'
require 'parser_helpers'

describe JRubyParser do
  [JRubyParser::Compat::RUBY1_8, JRubyParser::Compat::RUBY1_9].each do |v|
    it "finds fcall via simple symbol search" do
      JRubyParser.parse("b = foo(1)").tap do |root|
        fcall = root.find_type(:fcall)
        fcall.should_not == nil
      end
    end

    it "finds specific fcall by block and simple symbol" do
      JRubyParser.parse("foo(1); bar(2)").tap do |root|
        fcall = root.find_type(:fcall) { |n| n.name == "bar" }
        fcall.name.should == "bar"
      end
    end

    it "finds type and method named based on Enumerable find" do
      JRubyParser.parse("foo(1); bar(2)").tap do |root|
        fcall = root.find { |n| n.short_name == "fcall" && n.name == "bar"}
        fcall.name.should == "bar"
        fcalls = root.find_all { |n| n.short_name == "fcall" }
        fcalls.size.should == 2
      end
    end
  end
end
