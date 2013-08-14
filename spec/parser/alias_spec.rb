require_relative '../helpers'

describe Parser do
  VERSIONS.each do |v|
    it "should parse alias with quotationmarks" do
      parse(%Q{alias :'<==>' :"foo"}, v).find_node(:alias).tap do |aliasn|
        aliasn.new_name.find_node(:symbol).name.should == "<==>"
        aliasn.old_name.find_node(:symbol).name.should == "foo"
        aliasn.should have_position(0,0,0,20)
      end
    end
  end
end

