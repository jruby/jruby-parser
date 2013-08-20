require_relative '../helpers'

# Notes:
# 1. All heredocs include end newline right after end marker because that whitespace is part of
#    the markers definition.

describe Parser do
  VERSIONS.each do |v|
    it "parses a heredoc with marker at beginning of line[#{v}]" do
    ast = parse(<<-EOF, v)
<<END
hello
END
    EOF

    ast.find_node(:str).should have_position(0, 2, 0, 15)
  end

  it "parses an empty heredoc with marker at beginning of line[#{v}]" do
    ast = parse(<<-EOF, v)
<<END
END
    EOF

    ast.find_node(:str).should have_position(0, 1, 0, 9)
  end

  it "parses a heredoc with minus(-) and marker not at beginning of line[#{v}]" do
    ast = parse(<<-EOF, v)
<<-END
hello
 END
    EOF

    ast.find_node(:str).should have_position(0, 2, 0, 17)
  end

  it "parses an empty heredoc with minus(-) and marker not at beginning of line[#{v}]" do
    ast = parse(<<-EOF, v)
<<-END
 END
    EOF

    ast.find_node(:str).should have_position(0, 1, 0, 11)
  end

  it "parses a heredoc in quotes with marker at beginning of line[#{v}]" do
    ast = parse(<<-EOF, v)
<<'END'
hello
END
    EOF

    ast.find_node(:str).should have_position(0, 2, 0, 17)
  end

  it "parses a heredoc in quotes with minus(-) and marker not at beginning of line[#{v}]" do
    ast = parse(<<-EOF, v)
<<-'END'
hello
 END
    EOF

    ast.find_node(:str).should have_position(0, 2, 0, 19)
  end

  it "parses a heredoc in double quotes with minus(-) and marker not at beginning of line[#{v}]" do
    ast = parse(<<-EOF, v)
<<-"end;"
hello
 end;
    EOF

    ast.find_node(:str).should have_position(0, 2, 0, 21)
  end

  it "parses an empty heredoc in double quotes with minus(-) and marker not at beginning of line[#{v}]" do
    ast = parse(<<-EOF, v)
<<-"end;"
 end;
    EOF

    ast.find_node(:str).should have_position(0, 1, 0, 15)
  end

  it "parses a heredoc in method call with other arguments[#{v}]" do
    ast = parse(<<-EOF, v)
module_eval <<-"end;", "file", 123
  hello world
 end;
    EOF

    ast.find_node(:str).should have_position(0, 2, 12, 41)
  end

  it "parses an empty heredoc in method call with other arguments[#{v}]" do
    ast = parse(<<-EOF, v)
module_eval <<-"end;", "file", 123
 end;
    EOF

    ast.find_node(:str).should have_position(0, 1, 12, 27)
  end
end
end
