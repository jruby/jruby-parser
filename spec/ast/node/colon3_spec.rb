require 'helpers'

describe org::jrubyparser::ast::Colon3Node do
  describe '#lexical_name' do
    it 'returns the lexical name of the colon 3 node' do
      parse('::Bar').find_node(:colon3).tap do |colon3|
        expect(colon3.lexical_name).to eq 'Bar'
      end
    end
  end
end