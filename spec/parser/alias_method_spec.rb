require_relative '../helpers'

describe Parser do
  it 'should parse alias with unicode characters' do
    parse('alias_method :💣, :clear').find_node(:fcall).tap do |fcall|
      arg_names = fcall.args_node.child_nodes.map { |n| n.name }
      expect(arg_names).to eq %W(ð\u009F\u0092£ clear)
    end
  end
end
