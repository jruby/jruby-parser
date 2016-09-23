require 'helpers'

describe org::jrubyparser::ast::ArgumentNode do
  describe '#lexical_name' do
    it 'returns the name of the argument' do
      parse("def foo(a)\n:foo\nend").find_node(:argument) do |arg|
        expect(arg.lexical_name).to eq 'a'
      end
    end
  end
end