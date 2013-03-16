$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
require 'jruby-parser'
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  [1.8, 1.9].each do |v|
    it "Should parse attr assign" do
      parse("a[1] = 2", v).find_node(:attrassign).tap do |asgn|
        asgn.should_not == nil
      end
    end
  end
end
