require 'helpers'

describe org::jrubyparser::ast::ConstDeclNode do
  it "returns the lexical name of the constant that's assigned to" do
    parse('FOO = 123').find_node(:constdecl).tap do |const|
      expect(const.lexical_name).to eq 'FOO'
    end
  end
end