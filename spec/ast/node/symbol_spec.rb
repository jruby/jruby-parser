require 'helpers'

describe org::jrubyparser.ast.SymbolNode do

  it 'returns the lexical name of the symbol' do
    parse(':foo').find_node(:symbol).tap do |sym|
      expect(sym.lexical_name).to eq ':foo'
    end
  end
end