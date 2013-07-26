require_relative '../../helpers'

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

