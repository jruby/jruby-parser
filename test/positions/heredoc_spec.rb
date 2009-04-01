$LOAD_PATH.unshift File.dirname(__FILE__) + "/../helpers"
require 'java'
require File.dirname(__FILE__) + "/../../dist/JRubyParser.jar"
require 'parser_helpers'
require 'node_helpers'

# Notes:
# 1. All heredocs include end newline right after end marker because that whitespace is part of
#    the markers definition.

describe Parser do
  it "should parse a heredoc with marker at beginning of line" do
    ast = parse(<<-EOF)
<<END
hello
END
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 3, 0, 16)
  end

  it "should parse a heredoc with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-END
hello
 END
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 3, 0, 18)
  end

  it "should parse a heredoc in quotes with marker at beginning of line" do
    ast = parse(<<-EOF)
<<'END'
hello
END
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 3, 0, 18)
  end

  it "should parse a heredoc in quotes with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-'END'
hello
 END
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 3, 0, 20)
  end

  it "should parse a heredoc in double quotes with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-"end;"
hello
 end;
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 3, 0, 22)
  end

  it "should parse a heredoc in method call with other arguments" do
    ast = parse(<<-EOF)
module_eval <<-"end;", "file", 123
 end;
    EOF

    str = ast.find_node(:str)
    # this fails; the end offset doesn't get computed correctly
    str.should have_position(0, 3, 12, 41)
  end
end
