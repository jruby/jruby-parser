require_relative '../helpers'

describe Parser do
  # Parsing normal forms of Ruby method calls.
  it "should parse a call" do
    parse("def foo; end; self.foo").find_node(:call).tap do |call|
      expect(call.name).to eq 'foo'
    end

    parse("def foo; end; self.foo()").find_node(:call).tap do |call|
      expect(call.name).to eq 'foo'
    end
  end

  it "should parse a vcall" do
    parse("def foo; end; foo").find_node(:vcall).tap do |vcall|
      expect(vcall.name).to eq 'foo'
    end
  end

  it "should parse a fcall" do
    parse("def foo; end; foo()").find_node(:fcall).tap do |fcall|
      expect(fcall.name).to eq 'foo'
    end
  end

  # Parsing - and + as operator calls, which are unique in that they have
  # both binary and unary forms.

  it "should parse unary plus and minus expressed as a call node" do
    ["+", "-"].each do |op|
      parse("#{op}x").find_node(:call).tap do |call|
        expect(call.name).to eq "#{op}@"
      end
    end
  end

  # Parsing the unary form of - and + as an explicit call.

  it "should parse unary plus and minus expressed as an call without parens" do
    ["+", "-"].each do |op|
      parse("x.#{op}@").find_node(:call).tap do |call|
        expect(call.name).to eq "#{op}@"
      end
    end
  end

  it "should parse unary plus and minus expressed as an call with parens" do
    ["+", "-"].each do |op|
      parse("x.#{op}@()").find_node(:call).tap do |call|
        expect(call.name).to eq "#{op}@"
      end
    end
  end

  # Unlike - and +, other unary operators such as ~ are not decorated.

  it "should not decorate ~" do
    parse("~x").find_node(:call).tap do |call|
      expect(call.name).to eq '~'
    end
  end

  # In 1.9 and 2.0 'not' was a call to '!', but we annotate the name of the
  # method as lexically being 'not'. '!' is still just a call.

  it "should parse 'not' as a call to '!' with lexical name 'not'" do
    parse("not x").find_node(:call).tap do |call|
      expect(call.name).to eq '!'
    end
  end

  it "should parse '!' as a call" do
    parse("! x").find_node(:call).tap do |call|
      expect(call.name).to eq "!"
    end
  end
end
