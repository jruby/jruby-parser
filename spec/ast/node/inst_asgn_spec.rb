require 'helpers'

describe org::jrubyparser::ast::InstAsgnNode do
  describe '#lexical_name' do
    it 'returns the lexical name of the instance variable that is being assigned' do
      parse('@foo = 123').find_node(:instasgn).tap do |iasgn|
        expect(iasgn.lexical_name).to eq '@foo'
      end
    end
  end

  describe '#name' do
    it 'returns the name of the instance variable that is being assigned' do
      parse('@foo = 123').find_node(:instasgn).tap do |iasgn|
        expect(iasgn.name).to eq '@foo'
      end
    end
  end
end
