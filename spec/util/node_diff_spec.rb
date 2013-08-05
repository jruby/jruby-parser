require_relative '../helpers'
import org.jrubyparser.util.diff.SequenceMatcher
import org.jrubyparser.util.diff.Change

describe org.jrubyparser.util.diff.SequenceMatcher do

  it 'should take two nodes at creation' do
    nodeA = parse('a = "a"')
    nodeB = parse('b = "b"')
    sm = SequenceMatcher.new(nodeA, nodeB)
    sm.sequence_one.should == nodeA
    sm.sequence_two.should == nodeB
  end

  it 'should call a callback passed in' do
    nodeA = parse('a = "a"')
    nodeB = parse('b = "b"')
    check = false
    SequenceMatcher.new(nodeA, nodeB) do |node|
      check = true
    end
    check.should == true
  end

end

describe "Change" do
  nodeA = parse("b = 'b'")
  nodeB = parse("b = 'a'")
  change = Change.new(nodeA, 4, nodeB, 4)

  it 'should hold the old node' do
    change.old_node.should == nodeB
  end

  it 'should hold the new node' do
    change.new_node.should == nodeA
  end

  it 'should hold the complexity of the nodes' do
    change.old_cost.should == 4
    change.new_cost.should == 4
    change.total_cost.should == 8
  end
end
