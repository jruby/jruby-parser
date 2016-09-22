require_relative '../../helpers'

describe org.jrubyparser.ast.AliasNode do
  it "should parse a hash literal (a=>b)" do
    rparse("{:one => 1, :two => 2}").find_node(:hash).tap do |hash|
      expect(hash.pairs.size).to eq 2
      expect(hash.pairs.map { |pair| pair.key.name }).to eq ['one', 'two']
      expect(hash.pairs.map { |pair| pair.value.value }).to eq [1, 2]
    end
  end

  it "should parse a hash literal (a: b)" do
    rparse("{one: 1, two: 2}").find_node(:hash).tap do |hash|
      expect(hash.pairs.size).to eq 2
      expect(hash.pairs.map { |pair| pair.key.name }).to eq ['one', 'two']
      expect(hash.pairs.map { |pair| pair.value.value }).to eq [1, 2]
    end

    rparse("call one: 2").find_node(:hash).tap do |hash|
      expect(hash.pairs.size).to eq 1
      expect(hash.pairs.map { |pair| [pair.key.name, pair.value.value] }).to eq [['one', 2]]
    end
  end
end
