require_relative '../../helpers'

describe org.jrubyparser.ast.AliasNode do
  it "Should parse alias node of symbols" do
    rparse("alias :new_name :old_name").find_node(:alias).tap do |a|
      expect(a.new_name.name).to eq "new_name"
      expect(a.old_name.name).to eq "old_name"
    end
  end

  it "Should parse alias node of CONSTANTS" do
    rparse("alias NEW OLD").find_node(:alias).tap do |a|
      expect(a.new_name.name).to eq "NEW"
      expect(a.old_name.name).to eq "OLD"
    end
  end
end
