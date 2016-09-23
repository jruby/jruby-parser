require 'helpers'

describe org::jrubyparser::ast::BlockArgNode do
  describe '#lexical_name' do
    it 'returns the lexical name of the block argument' do
      parse("def foo(&block)\n:foo\nend").find_node(:blockarg).tap do |s|
        expect(s.lexical_name).to eq '&block'
      end
    end
  end
end