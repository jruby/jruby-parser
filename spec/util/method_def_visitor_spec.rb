require_relative '../helpers'

java_import org.jrubyparser.util.MethodDefVisitor

def modules_in(code)
  find_modules(parse(code))
end

def find_modules(node)
  node.child_nodes.inject([]) do |modules, n|
    modules << n if n.kind_of? org.jrubyparser.ast.IModuleScope
    modules.concat find_modules(n)
  end
end

describe MethodDefVisitor do
  it 'should find no methods defined in a class' do
    modules_in("class Example\nend").first.method_defs.should == []
  end

  it 'should find a method defined in a class' do
    modz = modules_in("class Example\ndef hello\nend\nend")
    modz.first.method_defs.map(&:name).should =~ %w{hello}
  end

  it 'should find methods defined in a class' do
    modz = modules_in("class Example\ndef hello\nend;def world; end\nend")
    modz.first.method_defs.map(&:name).should =~ %w{hello world}
  end

  it 'should not find a method defined on an inner class' do
    modz = modules_in("module Ex\nclass Example\ndef hello\nend\nend\nend")
    modz.first.method_defs.size.should == 0
  end

  it 'should not find a method defined inside a method' do
    modz = modules_in("class Example\ndef hello\ndef hello_too\nend\nend\nend")
    modz.first.method_defs.size.should == 1
  end

  it 'should find a method defined in block scope in module body' do
    modz = modules_in("module Ex\nloop do\ndef hello\nend\nend\nend")
    modz.first.method_defs.size.should == 1
  end
end

