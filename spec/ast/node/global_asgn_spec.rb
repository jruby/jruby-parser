require 'helpers'

describe org::jrubyparser::ast::GlobalAsgnNode do
  describe '#lexical_name' do
    it "returns the lexical name of the global variable that's being assigned" do
      parse('$foo = 123').find_node(:globalasgn).tap do |gvar|
        expect(gvar.lexical_name).to eq '$foo'
      end
    end
  end

  describe '#name' do
    it "returns the name of the global variable that's being assigned" do
      parse('$foo = 123').find_node(:globalasgn).tap do |gvar|
        expect(gvar.name).to eq '$foo'
      end
    end
  end
end
