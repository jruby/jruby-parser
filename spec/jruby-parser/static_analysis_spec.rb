$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
require 'jruby-parser'
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  [1.9].each do |v|
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
end
