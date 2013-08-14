require_relative '../helpers'

describe Parser do
  VERSIONS.each do |v|
    it "Should parse attr assign" do
      parse("a[1] = 2", v).find_node(:attrassign).tap do |asgn|
        asgn.should_not == nil
      end
    end
  end
end
