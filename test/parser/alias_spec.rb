$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  it "should parse alias with quotationmarks" do

    ast = parse(<<-EOF)
alias :'<==>' :"foo"
    EOF

    aliasn = ast.find_node(:alias)
    aliasn.new_name.find_node(:str).value.should == "<==>"
    aliasn.old_name.find_node(:str).value.should == "foo"
  end
end

