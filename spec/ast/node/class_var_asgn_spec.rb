require 'helpers'

describe org::jrubyparser::ast::ClassVarAsgnNode do
  describe '#lexical_name' do
    it 'returns the lexical name of the class variable that' do
      parse('@@foo = 123').find_node(:classvarasgn).tap do |class_var|
        expect(class_var.lexical_name).to eq '@@foo'
      end
    end
  end
end