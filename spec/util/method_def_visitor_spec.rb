require_relative '../helpers'

import org.jrubyparser.util.MethodDefVisitor

def find_modules(node)
  ret_nodes = []
  node.child_nodes.each do |n|
    if n.kind_of? org.jrubyparser.ast.IModuleScope
      ret_nodes << n
    end
    ret_nodes = ret_nodes.concat find_modules(n)
  end
  ret_nodes if ret_nodes
end

describe MethodDefVisitor do
  it 'should find a method defined on a class' do
    root = parse("class Example\ndef hello\nend\nend")
    klasses = find_modules(root)
    klasses[0].method_defs[0].name.should =~ /hello/
  end

  it 'should not find a method defined on an inner class' do
    root = parse("module Ex\nclass Example\ndef hello\nend\nend\nend")
    modz = find_modules(root)
    modz[0].method_defs.size.should == 0
  end

  it 'should not find a method defined inside a method' do
    root = parse("class Example\ndef hello\ndef hello_too\nend\nend\nend")
    modz = find_modules(root)
    modz[0].method_defs.size.should == 1
  end
end

