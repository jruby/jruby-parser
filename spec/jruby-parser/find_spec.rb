require_relative '../helpers'

describe JRubyParser do
  it "finds fcall via simple symbol search" do
    parse("b = foo(1)").tap do |root|
      fcall = root.find_type(:fcall)
      expect(fcall).to_not be_nil
    end
  end

  it "finds specific fcall by block and simple symbol" do
    parse("foo(1); bar(2)").tap do |root|
      fcall = root.find_type(:fcall) { |n| n.name == "bar" }
      expect(fcall.name).to eq 'bar'
    end
  end

  it "finds type and method named based on Enumerable find" do
    parse("foo(1); bar(2)").tap do |root|
      fcall = root.find { |n| n.short_name == "fcall" && n.name == "bar" }
      expect(fcall.name).to eq "bar"
      fcalls = root.find_all { |n| n.short_name == "fcall" }
      expect(fcalls.size).to eq 2
    end
  end
end
