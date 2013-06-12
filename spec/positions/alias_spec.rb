$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
require 'jruby-parser'
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  [1.8, 1.9].each do |v|
    it "Should parse alias node of symbols" do
      parse("alias :new_name :old_name", v).find_node(:alias).tap do |a|
        a.new_name_string.should == "new_name"
        a.old_name_string.should == "old_name"
        a.new_name.should have_position(0, 0, 6, 15)
        a.old_name.should have_position(0, 0, 16, 25)
      end
    end
  end
end
