require 'helpers'

describe org::jrubyparser::ast::InstVarNode do
  describe '#lexical_name' do
    it 'returns the lexical name fo the instance variable' do
      parse('@foo').find_node(:instvar).tap do |ivar|
        expect(ivar.lexical_name).to eq '@foo'
      end
    end
  end
end