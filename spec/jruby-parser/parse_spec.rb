require_relative '../helpers'

describe JRubyParser do
  VERSIONS.each do |v|
    it "passes in a static scope with defined var [#{v}]" do
      parse("b = a", v, scope('a')).tap do |root|
        root.find_type(:localvar).should_not == nil # a
      end
    end
  end
end
