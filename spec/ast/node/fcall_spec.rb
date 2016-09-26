require_relative '../../helpers'

describe org.jrubyparser.ast.FCallNode do
  it 'parses a 0-arg method call sans parens +extra line' do
    rparse("\nblock_given?\n").find_node(:fcall).tap do |call|
      expect(call).to have_name 'block_given?'
    end
  end

  it "parses a 0-arg method call with parens" do
    rparse("puts()").find_node(:fcall).tap do |call|
      expect(call).to have_name 'puts'
      expect(call.args_node).to be_instance_of(org.jrubyparser.ast.EmptyArgsNode)
      expect(call.args_node.child_nodes).to eq []
    end
  end

  it "parses a 1-arg method call with parens" do
    rparse("puts(1)").find_node(:fcall).tap do |call|
      expect(call).to have_name 'puts'
      expect(call.args_node.size).to eq 1
    end
  end

  it "parses a 1-arg method call without parens" do
    rparse("puts 1").find_node(:fcall).tap do |call|
      expect(call).to have_name 'puts'
      expect(call.args_node.size).to eq 1
    end
  end

  it "parses a 2-arg method call with parens" do
    rparse("puts(1,2)").find_node(:fcall).tap do |call|
      expect(call).to have_name 'puts'
      expect(call.args_node.size).to eq 2
    end
  end

  it "parses a 2-arg method call without parens" do
    rparse("puts 1, 2").find_node(:fcall).tap do |call|
      expect(call).to have_name 'puts'
      expect(call.args_node.size).to eq 2
    end
  end

  it "parses an empty method with a block({})" do
    parse("foo() {}").find_node(:fcall).tap do |call|
      expect(call).to have_name 'foo'
      expect(call).to have_block
    end
  end

  it "parses an empty method with a block(do...end)" do
    parse("foo() do\nend\n").find_node(:fcall).tap do |call|
      expect(call).to have_name 'foo'
      expect(call).to have_block
    end
  end

  it "parses an empty method with a block({}) +1 arg" do
    parse("foo() { |a| }").find_node(:fcall).tap do |call|
      expect(call).to have_name 'foo'
      expect(call).to have_block
      expect(call.find_node(:argument).name).to eq 'a'
    end
  end

  it "parses an empty method with a block(do...end)" do
    parse("foo() do |a|\nend\n").find_node(:fcall).tap do |call|
      expect(call).to have_name 'foo'
      expect(call).to have_block
      expect(call.find_node(:argument).name).to eq 'a'
    end
  end

  describe '#lexical_name' do
    it 'returns the lexical name of the fcall' do
      parse("foo 123, 456").find_node(:fcall).tap do |call|
        expect(call.lexical_name).to eq 'foo'
      end
    end
  end
end
