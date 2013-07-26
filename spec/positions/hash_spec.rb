require_relative '../helpers'

describe Parser do
  [1.8, 1.9].each do |v|
    it "should parse a hash literal (a=>b) [#{v}]" do
      parse("{:one => 1, :two => 2}", v).find_node(:hash).tap do |hash|
        hash.should have_position(0, 0, 0, 22)
      end
    end
  end

  it "should parse a hash literal (a,b) [1.8]" do
    parse("{:one, 1, :two, 2}", 1.8).find_node(:hash).tap do |hash|
      hash.should have_position(0, 0, 0, 18)
    end
  end

  it "should parse a hash literal (a: b) [1.9]" do
    parse("{one: 1, two: 2}", 1.9).find_node(:hash).should have_position(0, 0, 0, 16)
  end
end
