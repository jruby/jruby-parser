require_relative '../helpers'

describe Parser do
  VERSIONS.each do |v|
    it "Should parse attr assign" do
      parse("foo ||= bar", v).find_node(:opasgnor).tap do |opasgn|
        opasgn.should have_position(0, 0, 0, 11)
        reference, assignment = opasgn.first, opasgn.second
        reference.should have_name_and_position("foo", 0, 0, 0, 3)
#        assignment.should have_name_and_position("foo", 0, 0, 0, 11)
        assignment.value.should have_name_and_position("bar", 0, 0, 8, 11)
      end
    end
  end
end
