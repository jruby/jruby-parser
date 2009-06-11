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
    str.should have_position(0, 2, 0, 15)
  end

  it "should parse an empty heredoc with marker at beginning of line" do
    ast = parse(<<-EOF)
<<END
END
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 1, 0, 9)
  end

  it "should parse a heredoc with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-END
hello
 END
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 2, 0, 17)
  end

  it "should parse an empty heredoc with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-END
 END
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 1, 0, 11)
  end

  it "should parse a heredoc in quotes with marker at beginning of line" do
    ast = parse(<<-EOF)
<<'END'
hello
END
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 2, 0, 17)
  end

  it "should parse a heredoc in quotes with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-'END'
hello
 END
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 2, 0, 19)
  end

  it "should parse a heredoc in double quotes with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-"end;"
hello
 end;
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 2, 0, 21)
  end

  it "should parse an empty heredoc in double quotes with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-"end;"
 end;
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 1, 0, 15)
  end

  it "should parse a heredoc in method call with other arguments" do
    ast = parse(<<-EOF)
module_eval <<-"end;", "file", 123
  hello world
 end;
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 2, 12, 41)
  end

  it "should parse an empty heredoc in method call with other arguments" do
    ast = parse(<<-EOF)
module_eval <<-"end;", "file", 123
 end;
    EOF

    str = ast.find_node(:str)
    str.should have_position(0, 1, 12, 27)
  end

end
