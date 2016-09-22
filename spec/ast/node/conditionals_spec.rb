require_relative '../../helpers'

describe Parser do
  it 'should parse an unless on alias' do
    ast = parse('alias p ** unless method_defined? :p')
    ast.find_node(:alias).tap { |a| expect(a).to_not be_nil }
    ast.find_node(:if).tap { |i| expect(i).to_not be_nil }
  end
end
