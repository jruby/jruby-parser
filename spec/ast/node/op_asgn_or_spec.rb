require_relative '../../helpers'

describe Parser do
  it "Should parse attr assign" do
    parse("foo ||= bar").find_node(:opasgnor).tap do |opasgn|
      expect(opasgn).to_not be_nil
      reference, assignment = opasgn.first_node, opasgn.second_node
      expect(reference).to have_name("foo")
      expect(assignment.value_node).to have_name("bar")
    end
  end
end

