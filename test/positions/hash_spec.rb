$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

describe Parser do
  it "should parse a hash literal" do
    ast = parse(<<-EOF)
{:one => 1, :two => 2}
    EOF

    hsh = ast.find_node(:hash)
    hsh.should have_position(0, 0, 0, 22)
  end

  it "should parse a 1.8 hash literal" do
    ast = parse(<<-EOF)
{:one, 1, :two, 2}
    EOF

    hsh = ast.find_node(:hash)
    hsh.should have_position(0, 0, 0, 18)
  end

  it "should parse a 1.9 hash literal" do
    ast = parse(<<-EOF, $config_1_9)
{one: 1, two: 2}
    EOF

    hsh = ast.find_node(:hash)
    hsh.should have_position(0, 0, 0, 16)
  end
end
