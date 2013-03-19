$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require 'jruby-parser'
require 'parser_helpers'

describe JRubyParser do
  [1.8, 1.9].each do |v|
    it "finds parameter via is_block_parameter [#{v}]" do
      caret_parse("proc { |^a| }", v).tap do |root, caret_node|
        caret_node.block_parameter?.should == true
      end
      caret_parse("proc { |^a,b| }", v).tap do |root, caret_node|
        caret_node.block_parameter?.should == true
      end
      caret_parse("proc { |*^a| }", v).tap do |root, caret_node|
        caret_node.block_parameter?.should == true
      end
      caret_parse("proc { |a, (b, ^c)| }", v).tap do |root, caret_node|
        caret_node.block_parameter?.should == true
      end
      caret_parse("proc { |&^a| }", v).tap do |root, caret_node|
        caret_node.block_parameter?.should == true
      end

      if v == 1.9
        caret_parse("proc { |a, ^b=1| }", v).tap do |root, caret_node|
          caret_node.block_parameter?.should == true
        end

        caret_parse("proc { |c| proc { |a, b=^c| } }", v).tap do |root, caret_node|
          caret_node.block_parameter?.should == false
        end

      end
    end
  end
end
