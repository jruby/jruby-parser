$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
require 'jruby-parser'

describe JRubyParser do
  [JRubyParser::Compat::RUBY1_8, JRubyParser::Compat::RUBY1_9].each do |v|
    it "rewrites method name from foo to bar" do
      JRubyParser.parse("b = foo(1)").tap do |root|
        fcall = root.find_node(:fcall)
        fcall.name = 'bar'
      end.to_source.should == "b = bar(1)"

      JRubyParser.parse("b = foo 1").tap do |root|
        fcall = root.find_node(:fcall)
        fcall.name = 'bar'
      end.to_source.should == "b = bar 1"
    end

    it "rewrites between different coercible ruby types" do
      [1, 1.0, true, false, nil, "a"].each do |replace|
        JRubyParser.parse("foo 1").tap do |root|
          fcall = root.find_node(:fcall)
          fcall.args[0] = replace
        end.to_source.should == "foo #{replace.inspect}"
      end
    end

    it "rewrites receiver of a call" do
      JRubyParser.parse("1.to_f(1)").tap do |root|
        call = root.find_node(:call)
        call.receiver = 2
      end.to_source.should == "2.to_f(1)"
    end

    it "rewrites can add to simple args" do
      JRubyParser.parse("foo(1)").tap do |root|
        call = root.find_node(:fcall)
        call.args << 2
      end.to_source.should == "foo(1, 2)"
    end

    it "rewrites can add to simple args" do
      JRubyParser.parse("foo(1)").tap do |root|
        call = root.find_node(:fcall)
        call.args = [3, 4]
      end.to_source.should == "foo(3, 4)"
    end

    it "rewrites op element assignment" do
      JRubyParser.parse("a[3] += 5").tap do |root|
        op = root.find_node(:opelementasgn)
        op.receiver = 1
        op.args[0] = 2
        op.value = 3
      end.to_source.should == "1[2] += 3"
    end
  end
end
