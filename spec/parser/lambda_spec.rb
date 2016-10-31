require_relative '../helpers'

describe Parser do
  it "parses stabby lambdas with whitespace between () and -> in 2.0 mode" do
    parse("hello_world = -> (message) { puts message }", 2.0).find_node(:lambda).tap do |l|
      expect(l).to have_position(0, 0, 17, 43)
    end
  end
end

