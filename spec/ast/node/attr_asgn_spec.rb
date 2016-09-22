require_relative '../../helpers'

describe Parser do
  it "Should parse attr assign" do
    parse("a[1] = 2").find_node(:attrassign).tap do |asgn|
      expect(asgn).to_not be_nil
    end
  end
end
