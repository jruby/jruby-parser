require_relative '../../helpers'

describe org::jrubyparser::ast::AttrAssignNode do
  it "is parseable by the Parser" do
    parse("a[1] = 2").find_node(:attrassign).tap do |asgn|
      expect(asgn).to_not be_nil
    end
  end

  describe '#lexical_name' do
    it 'returns the lexical name' do
      parse("a[1] = 2").find_node(:attrassign).tap do |asgn|
        expect(asgn.lexical_name).to eq '[]='
      end
    end
  end
end
