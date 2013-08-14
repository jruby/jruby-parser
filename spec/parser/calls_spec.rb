require_relative '../helpers'

describe Parser do
  [1.8, 1.9, 2.0].each do |v|
    # Parsing normal forms of Ruby method calls.

    it "should parse a call" do
      parse("def foo; end; self.foo", v).find_node(:call).tap do |fcall|
        fcall.name.should == "foo"
      end

      parse("def foo; end; self.foo()", v).find_node(:call).tap do |fcall|
        fcall.name.should == "foo"
      end
    end

    it "should parse a vcall" do
      parse("def foo; end; foo", v).find_node(:vcall).tap do |vcall|
        vcall.name.should == "foo"
      end
    end

    it "should parse a fcall" do
      parse("def foo; end; foo()", v).find_node(:fcall).tap do |fcall|
        fcall.name.should == "foo"
      end
    end

    # Parsing - and + as operator calls, which are unique in that they have
    # both binary and unary forms.

    it "should parse unary plus and minus expressed as an operator" do
      ["+", "-"].each do |op|
        parse("#{op}x", v).find_node(:unarycall).tap do |unarycall|
          unarycall.name.should == "#{op}@"
          unarycall.lexical_name.should == op
        end
      end
    end

    # Parsing the unary form of - and + as an explicit call.

    it "should parse unary plus and minus expressed as an call without parens" do
      ["+", "-"].each do |op|
        parse("x.#{op}@", v).find_node(:call).tap do |call|
          call.name.should == "#{op}@"
        end
      end
    end

    it "should parse unary plus and minus expressed as an call with parens" do
      ["+", "-"].each do |op|
        parse("x.#{op}@()", v).find_node(:call).tap do |call|
          call.name.should == "#{op}@"
        end
      end
    end

    # The not operator is always a node and never a call.

    it "should parse not as a node" do
      parse("not x", v).find_node(:notn).tap do |notn|
        notn.should_not == nil
      end
    end

    # Unlike - and +, other unary operators such as  are not decorated.

    it "should not decorate ~" do
      parse("~x", v).find_node(:call).tap do |call|
        call.name.should == "~"
      end
    end
  end

  # The ! operator used to be a node and is now a call.

  it "should parse 1.8 ! as a node" do
    parse("!x", 1.8).find_node(:not).tap do |notn|
      notn.should_not == nil
    end
  end

  [1.9, 2.0].each do |v|
    it "should parse 1.9 and 2.0 ! as a call" do
      parse("!x", v).find_node(:call).tap do |call|
        call.name.should == "!"
      end
    end
  end
end
