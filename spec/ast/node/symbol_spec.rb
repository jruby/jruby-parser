require 'helpers'

describe org::jrubyparser.ast.SymbolNode do
  describe '#lexical_name' do
    it 'returns the lexical name of the symbol' do
      parse(':foo').find_node(:symbol).tap do |sym|
        expect(sym.lexical_name).to eq ':foo'
      end
    end
  end

  describe '#name' do
    it 'returns the name of the symbol' do
      parse(':foo').find_node(:symbol).tap do |sym|
        expect(sym.name).to eq 'foo'
      end
    end
  end
end
