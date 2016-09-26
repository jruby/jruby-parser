require 'helpers'

describe org::jrubyparser::ast::EmptyArgsNode do
  describe '#child_nodes' do
    it 'returns an empty array' do
      parse('foo()').find_node(:fcall).tap do |call|
        expect(call.args_node.child_nodes).to eq []
      end
    end
  end
end

