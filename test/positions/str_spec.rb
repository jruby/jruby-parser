$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  it "should parse a string value" do
    ast = parse(<<-EOF)
str = "my str"
    EOF

    str = ast.find_node(:str)
    str.value.should == "my str"
  end
end
