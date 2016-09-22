require_relative '../../helpers'

# Notes:
# 1. All heredocs include end newline right after end marker because that whitespace is part of
#    the markers definition.

describe Parser do
  it "parses a heredoc with marker at beginning of line" do
    ast = parse(<<-EOF)
<<END
hello
END
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end

  it "parses an empty heredoc with marker at beginning of line" do
    ast = parse(<<-EOF)
<<END
END
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end

  it "parses a heredoc with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-END
hello
 END
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end

  it "parses an empty heredoc with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-END
 END
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end

  it "parses a heredoc in quotes with marker at beginning of line" do
    ast = parse(<<-EOF)
<<'END'
hello
END
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end

  it "parses a heredoc in quotes with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-'END'
hello
 END
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end

  it "parses a heredoc in double quotes with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-"end;"
hello
 end;
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end

  it "parses an empty heredoc in double quotes with minus(-) and marker not at beginning of line" do
    ast = parse(<<-EOF)
<<-"end;"
 end;
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end

  it "parses a heredoc in method call with other arguments" do
    ast = parse(<<-EOF)
module_eval <<-"end;", "file", 123
  hello world
 end;
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end

  it "parses an empty heredoc in method call with other arguments" do
    ast = parse(<<-EOF)
module_eval <<-"end;", "file", 123
 end;
    EOF

    expect(ast.find_node(:str)).to_not be_nil
  end
end
