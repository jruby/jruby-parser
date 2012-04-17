$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
require 'jruby-parser'
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  it "should parse alias with quotationmarks" do
    root = parse <<-EOF
def foo(arg)
  puts arg
end
    EOF
    list = root.pathTo(root.find_node(:defn))
    list.size.should == 3
    list.root.node_name.should == "RootNode"
    list.leaf.node_name.should == "DefnNode"
  end
end

