require 'helpers'

describe org::jrubyparser::ast::BackRefNode do
  describe '#lexical_name' do
    it 'returns the lexical name' do
      parse("$&").find_node(:backref).tap do |back_ref|
        expect(back_ref.lexical_name).to eq '$&'
      end
    end
  end
end