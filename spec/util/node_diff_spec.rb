require_relative '../helpers'

describe org.jrubyparser.util.diff.SequenceMatcher do
  before(:all) do
    SeqMatch = org.jrubyparser.util.diff.SequenceMatcher
  end

  it 'should take two nodes at creation' do
    nodeA = parse('a = "a"')
    nodeB = parse('b = "b"')
    sm = SeqMatch.new(nodeA, nodeB)
    sm.sequence_one.should == nodeA
    sm.sequence_two.should == nodeB
  end

  it 'should call a callback passed in' do
    nodeA = parse('a = "a"')
    nodeB = parse('b = "b"')
    check = false
    sm = SeqMatch.new(nodeA, nodeB) do |node|
      check = true
    end
    check.should == true
  end


end


