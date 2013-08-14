require_relative '../helpers'

describe Parser do
  VERSIONS.each do |v|
    it "Should parse alias node of symbols" do
      parse("alias :new_name :old_name", v).find_node(:alias).tap do |a|
        a.new_name_string.should == "new_name"
        a.old_name_string.should == "old_name"
        a.new_name.should have_position(0, 0, 6, 15)
        a.old_name.should have_position(0, 0, 16, 25)
      end
    end

    it "Should parse alias node of CONSTANTS" do
      parse("alias NEW OLD", v).find_node(:alias).tap do |a|
        a.new_name_string.should == "NEW"
        a.old_name_string.should == "OLD"
        a.new_name.should have_position(0, 0, 6, 9)
        a.old_name.should have_position(0, 0, 10, 13)
      end
    end
  end
end
