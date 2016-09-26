require 'helpers'

describe org::jrubyparser::ast::ConstNode do
  it 'returns the lexical name of the constant' do
    parse('FOO').find_node(:const).tap do |const|
      expect(const.lexical_name).to eq 'FOO'
    end
  end
end