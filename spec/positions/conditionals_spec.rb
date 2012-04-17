$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
$LOAD_PATH.unshift File.dirname(__FILE__) + "/../../lib"
require 'jruby-parser'
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  [1.8, 1.9].each do |v|
    it "should parse an unless on alias [#{v}]" do
      ast = parse("alias p ** unless method_defined? :p", v)
      ast.find_node(:alias).tap { |a| a.should have_position(0, 0, 0, 10) }
      ast.find_node(:if).tap { |i| i.should have_position(0, 0, 0, 36) }
    end
  end
end
