require 'helpers'

describe org::jrubyparser::ast::InstVarNode do
  describe '#lexical_name' do
    it 'returns the lexical name of the instance variable' do
      parse('@foo').find_node(:instvar).tap do |ivar|
        expect(ivar.lexical_name).to eq '@foo'
      end
    end
  end

  describe '#name' do
    it 'returns the name of the instance variable' do
      parse('@foo').find_node(:instvar).tap do |ivar|
        expect(ivar.name).to eq '@foo'
      end
    end
  end
end
