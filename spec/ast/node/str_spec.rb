require_relative '../../helpers'

describe Parser do
  it "should parse a string value" do
    parse('str = "my str"').find_node(:str).tap do |s|
      expect(s.value).to eq 'my str'
    end
  end
end
