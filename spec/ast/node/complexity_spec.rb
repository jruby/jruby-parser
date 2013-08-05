require_relative '../../helpers'

describe org.jrubyparser.ast.Node do

  it 'should calculate the complexity of a node' do
    nodeA = parse("def foo(bar)\n bar\n end\n foo('astring')")
    nodeA.complexity.should == 14
  end

end
