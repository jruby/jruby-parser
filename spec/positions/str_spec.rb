require_relative '../helpers'

describe Parser do
  [1.8, 1.9].each do |v|
    it "should parse a string value [#{v}]" do
      parse('str = "my str"', v).find_node(:str).value.should == "my str"
    end
  end
end
