require_relative '../helpers'

describe JRubyParser do
  VERSIONS.each do |v|
    it "passes in a static scope with defined var [#{v}]" do
      parse("b = a", scope('a')).tap do |root|
        expect(root.find_type(:localvar)).to_not be_nil
      end
    end

    it "parses hash literal with trailing = at end of key name" do
      parse("{:a==>1}").tap do |root|
        expect(root.find_type(:symbol).name).to eq 'a='
      end
    end
  end
end
