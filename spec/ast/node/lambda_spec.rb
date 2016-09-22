require_relative '../../helpers'

describe org.jrubyparser.ast.LambdaNode do
  it 'parses correctly even with a space between the arrow and parens' do
    parse('-> (message) { puts message }').find_node(:lambda) do |l|
      expect(l).to_not be_nil
      expect(l.args_node).to have_arg_counts 1, 0, false, false
      expect(l.args_node).to have_parameters('message')
    end
  end
end
