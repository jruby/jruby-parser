require 'helpers'

describe org::jrubyparser::ast::LocalAsgnNode do
  describe '#lexical_name' do
    it 'returns the lexical name of the local variable that is being assigned' do
      parse('foo = 123').find_node(:localasgn).tap do |lasgn|
        expect(lasgn.lexical_name).to eq 'foo'
      end
    end
  end
end
