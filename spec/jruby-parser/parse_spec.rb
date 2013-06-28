$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require 'jruby-parser'
require 'parser_helpers'

describe JRubyParser do
  VERSIONS.each do |v|
    it "passes in a static scope with defined var [#{v}]" do
      parse("b = a", v, scope('a')).tap do |root|
        root.find_type(:localvar).should_not == nil # a
      end
    end
  end
end
