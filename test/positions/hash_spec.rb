$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  [1.8, 1.9].each do |v|
    it "should parse a hash literal (a=>b) [#{v}]" do
      parse("{:one => 1, :two => 2}", v).find(:hash).tap do |hash|
        hash.should have_position(0, 0, 0, 22)
      end
    end
  end

  it "should parse a hash literal (a,b) [1.8]" do
    parse("{:one, 1, :two, 2}", 1.8).find(:hash).tap do |hash|
      hash.should have_position(0, 0, 0, 18)
    end
  end

  it "should parse a hash literal (a: b) [1.9]" do
    parse("{one: 1, two: 2}", 1.9).find(:hash).should have_position(0, 0, 0, 16)
  end
end
