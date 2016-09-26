require 'helpers'

describe org::jrubyparser::ast::LocalVarNode do
  describe '#lexical_name' do
    it 'returns the lexical name of the local variable' do
      code = <<-CODE
              a = 1
              b = a
             CODE
      parse(code).find_node(:localvar).tap do |lvar|
        expect(lvar.lexical_name).to eq 'a'
      end
    end
  end
end