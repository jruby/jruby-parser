require_relative '../helpers'

describe Parser do
  it "should parse alias with quotationm arks" do
    parse(%Q{alias :'<==>' :"foo"}).find_node(:alias).tap do |aliasn|
      expect(aliasn.new_name.find_node(:symbol).name).to eq '<==>'
      expect(aliasn.old_name.find_node(:symbol).name).to eq 'foo'
    end
  end
end

