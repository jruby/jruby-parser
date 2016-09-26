require 'helpers'

describe org::jrubyparser::ast::DAsgnNode do
  describe '#lexical_name' do
    it 'returns the lexical name of the dynamic variable assignment' do
      parse('Proce.new { |a| a = 123 }').find_node(:dasgn).tap do |dasgn|
        expect(dasgn.lexical_name).to eq 'a'
      end
    end
  end
end