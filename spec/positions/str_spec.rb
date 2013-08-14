require_relative '../helpers'

describe Parser do
  VERSIONS.each do |v|
    it "should parse a string value [#{v}]" do
      parse('str = "my str"', v).find_node(:str).value.should == "my str"
    end
  end
end
