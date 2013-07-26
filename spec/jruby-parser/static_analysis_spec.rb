require_relative '../helpers'

describe Parser do
  VERSIONS.each do |v|
    if v == 2.0
      it "test 2.0" do
        parse("def foo(a:1); p a; end", v)
      end
    end
    if v != 1.8
      it "parses a simple multiple assignment [#{v}]" do
        parse("a,b,c = 1,2,3", v).find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, 1], [:b, 2], [:c, 3]])
        end
      end

      it "parses a simple lhs splat multiple assignment [#{v}]" do
        parse("a,*b = 1,2,3", v).find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, 1], [:b, [2, 3]]])
        end
        parse("a,b,*c = 1,2,3,4", v).find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, 1], [:b, 2], [:c, [3,4]]])
        end
      end

      it "parses a simple lhs splat multiple assignment [#{v}]" do
        parse("*a,b = 1,2,3", v).find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, [1, 2]], [:b, 3]])
        end
        parse("*a,b,c = 1,2,3,4", v).find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, [1, 2]], [:b, 3], [:c, 4]])
        end
      end

      it "parses a simple lhs splat multiple assignment [#{v}]" do
        parse("a,*b, c = 1,2,3,4", v).find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, 1], [:b, [2, 3]], [:c, 4]])
        end
        parse("a, b, *c, d = 1,2,3,4,5", v).find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, 1], [:b, 2], [:c, [3, 4]], [:d, 5]])
        end
        parse("a, *b, c, d = 1,2,3,4,5", v).find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, 1], [:b, [2, 3]], [:c, 4], [:d, 5]])
        end
      end

      it "parses a simple lhs splat multiple assignment [#{v}]" do
        parse("a,*b,c,d = 1,2,3", v).find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, 1], [:b, []], [:c, 2], [:d, 3]])
        end
      end

      it "parses a simple rhs splat multiple assignment [#{v}]" do
        ast = parse("a,*b = 1,*foo", v)
        foo = ast.find_node(:vcall)
        ast.find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, 1], [:b, foo]])
        end
      end

      it "parses a simple rhs splat multiple assignment [#{v}]" do
        ast = parse("*a,b = *foo,1", v)
        splatted_foo = ast.find_node(:splat)
        ast.find_node(:multipleasgn19).tap do |masgn|
          masgn.should have_static_assignments([[:a, splatted_foo], [:b, 1]])
        end
      end
    end

    it "method: can parse an empty body method with unused param [#{v}]" do
      parse("def foo(a)\nend").find_node(:defn) do |defn|
        defn.args.get_normative_parameter_name_list(true).each do |parameter|
          defn.is_parameter_used(parameter).should == false
        end
      end
    end

    it "method: Can detect simple parameter is used" do
      parse("def foo(a); a; end").find_node(:defn) do |defn|
        defn.args.get_normative_parameter_name_list(true).each do |parameter|
          defn.is_parameter_used(parameter).should == true
        end
      end

      parse("def foo(a,b); a; b; end").find_node(:defn) do |defn|
        defn.args.get_normative_parameter_name_list(true).each do |parameter|
          defn.is_parameter_used(parameter).should == true
        end
      end

      parse("def foo(a,b); a=1; b; end").find_node(:defn) do |defn|
        defn.is_parameter_used("a").should == false
        defn.is_parameter_used("b").should == true
      end
    end

    it "method: Can detect some simple parameters are used" do
      parse("def foo(a,b); b; end").find_node(:defn) do |defn|
        defn.is_parameter_used("a").should == false
        defn.is_parameter_used("b").should == true
      end

      parse("def foo(a,b); b if true; end").find_node(:defn) do |defn|
        defn.is_parameter_used("a").should == false
        defn.is_parameter_used("b").should == true
      end

      parse("def foo(a,b); proc { b if true }; end").find_node(:defn) do |defn|
        defn.is_parameter_used("a").should == false
        defn.is_parameter_used("b").should == true
      end

      parse("def foo a, b, c\nputs a, b, c\nend").find_node(:defn) do |defn|
        defn.is_parameter_used("a").should == true
        defn.is_parameter_used("b").should == true
        defn.is_parameter_used("c").should == true
      end

      parse("def foo(a, b); b.each_answer {|n| data if n == a }; end") do |defn|
        defn.is_parameter_used("a").should == true
        defn.is_parameter_used("b").should == true
      end
    end

    it "method: Can detect some simple optarg params are used" do
      parse("def foo(a,b = {}); b; end").find_node(:defn) do |defn|
        defn.is_parameter_used("a").should == false
        defn.is_parameter_used("b").should == true
      end
    end

    it "method: Can detect zsuper usage" do
      parse("def foo(a,b = {}); super; end").find_node(:defn) do |defn|
        defn.is_parameter_used("a").should == true
        defn.is_parameter_used("b").should == true
      end
    end

    it "block/iter: can parse an empty proc body with unused param [#{v}]" do
      parse("proc do |a|\nend").find_node(:iter) do |iter|
        iter.is_parameter_used("a").should == false
      end
    end

    it "block/iter: Can detect simple parameter is used" do
      parse("proc { |a, b| a; b }").find_node(:iter) do |iter|
        iter.is_parameter_used("a").should == true
        iter.is_parameter_used("b").should == true
      end
    end

    it "block/iter: Can detect simple parameter is used" do
      parse("proc { |a, b| a=1; b }").find_node(:iter) do |iter|
        iter.is_parameter_used("a").should == false
        iter.is_parameter_used("b").should == true
      end
    end

    it "block/iter: Can detect some simple parameters are used" do
      parse("proc {|a,b| b }").find_node(:iter) do |iter|
        iter.is_parameter_used("a").should == false
        iter.is_parameter_used("b").should == true
      end

      parse("proc {|a,b| b if true }").find_node(:iter) do |iter|
        iter.is_parameter_used("a").should == false
        iter.is_parameter_used("b").should == true
      end

      parse("proc {|a,b| proc { b if true } }").find_node(:iter) do |iter|
        iter.is_parameter_used("a").should == false
        iter.is_parameter_used("b").should == true
      end

      parse("proc {|a, b, c| puts a, b, c }").find_node(:iter) do |iter|
        iter.is_parameter_used("a").should == true
        iter.is_parameter_used("b").should == true
        iter.is_parameter_used("c").should == true
      end

      parse("proc {|a, b| b.each_answer {|n| data if n == a } }") do |iter|
        iter.is_parameter_used("a").should == true
        iter.is_parameter_used("b").should == true
      end
    end
  end
end
