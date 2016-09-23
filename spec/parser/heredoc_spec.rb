require 'helpers'

describe Parser do
  it 'parses squiggly heredoc with single quotes' do
    code = "obj = <<~'TEST'
                  hello
            TEST"
    str_node = parse(code).find { |s| s.value == "hello\n" if s.respond_to? :value }
    expect(str_node).to_not be_nil
  end
end