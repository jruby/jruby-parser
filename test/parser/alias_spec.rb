$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  [1.8, 1.9].each do |v|
    it "should parse alias with quotationmarks" do
      parse(%Q{alias :'<==>' :"foo"}, v).find(:alias).tap do |aliasn|
        aliasn.new_name.find_node(:symbol).name.should == "<==>"
        aliasn.old_name.find_node(:symbol).name.should == "foo"
        aliasn.should have_position(0,0,0,20)
      end
    end
  end
end

