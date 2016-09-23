require 'helpers'

describe org::jrubyparser::ast::ClassVarNode do

  describe '#lexical_name' do
    it 'returns the lexical name fo the class variable' do
      parse('@@foo').find_node(:classvar).tap do |cvar|
        expect(cvar.lexical_name).to eq '@@foo'
      end
    end
  end
end