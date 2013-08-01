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

  it 'should create a flat list of children' do
    nodeA = parse("def foo(bar)\n bar\n end\n foo('astring')")
    nodeB = parse('b = "b"')
    sm = SeqMatch.new(nodeA, nodeB)
    sm.flattenChildren(nodeA)
    fc = sm.flat_children
    fc.to_a[-1].node_type.should == org.jrubyparser.ast.NodeType::STRNODE
  end

end

