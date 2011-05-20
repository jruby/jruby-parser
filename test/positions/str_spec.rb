$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  [1.8, 1.9].each do |v|
    it "should parse a string value [#{v}]" do
      parse('str = "my str"', v).find(:str).value.should == "my str"
    end
  end
end
