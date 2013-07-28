require_relative '../helpers'

describe JRubyParser do
  VERSIONS.each do |v|
    it "rewrites method name from foo to bar [#{v}]" do
      rparse("b = foo(1)").tap do |root|
        fcall = root.find_node(:fcall)
        fcall.name = 'bar'
      end.to_source.should == "b = bar(1)"

      parse("b = foo 1").tap do |root|
        fcall = root.find_node(:fcall)
        fcall.name = 'bar'
      end.to_source.should == "b = bar 1"
    end

   it "rewrites between different coercible ruby types [#{v}]" do
     [1, 1.0, true, false, nil, "a"].each do |replace|
       parse("foo 1").tap do |root|
         fcall = root.find_node(:fcall)
         fcall.args[0] = replace
       end.to_source.should == "foo #{replace.inspect}"
     end
   end

    it "rewrites receiver of a call [#{v}]" do
      parse("1.to_f(1)").tap do |root|
        call = root.find_node(:call)
        call.receiver = 2
      end.to_source.should == "2.to_f(1)"
    end

    it "rewrites can add to simple args [#{v}]" do
      parse("foo(1)").tap do |root|
        call = root.find_node(:fcall)
        call.args << 2
      end.to_source.should == "foo(1, 2)"
    end

    it "rewrites can add to simple args [#{v}]" do
      parse("foo(1)").tap do |root|
        call = root.find_node(:fcall)
        call.args = [3, 4]
      end.to_source.should == "foo(3, 4)"
    end

    it "rewrites op element assignment [#{v}]" do
      parse("a[3] += 5").tap do |root|
        op = root.find_node(:opelementasgn)
        op.receiver = 1
        op.args[0] = 2
        op.value = 3
      end.to_source.should == "1[2] += 3"
    end

    it "rewrites an alias of barewords [#{v}]" do
      parse("alias foo bar").to_source.should == "alias foo bar"
    end

    if v != 1.8 
      it "rewrites stabby lambda [#{v}]" do
        str = "->(a) {puts a}"
        parse(str, v).to_source.should == str
      end
    end

#    it "rewrites a comment on first line [#{v}]" do
#      code = "# comment 1\nfoo(1)\n"
#      rparse(code).to_source.should == code
#    end
  end
end
