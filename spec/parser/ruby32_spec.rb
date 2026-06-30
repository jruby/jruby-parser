# Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>

require_relative '../helpers'

describe 'Ruby 3.2 grammar' do
  describe 'anonymous keyword rest argument in a method definition' do
    it 'parses an endless method with a bare double splat' do
      expect { parse "def foo(**) = 1", 3.2 }.not_to raise_error
    end

    it 'parses a method with a bare double splat' do
      expect { parse "def foo(**)\n  1\nend", 3.2 }.not_to raise_error
    end

    it 'parses a bare double splat after a keyword argument' do
      expect { parse "def foo(k:, **)\n  1\nend", 3.2 }.not_to raise_error
    end

    it 'parses a bare double splat alongside a bare splat' do
      expect { parse "def foo(*, **)\n  1\nend", 3.2 }.not_to raise_error
    end

    it 'still parses a named keyword rest argument' do
      expect { parse "def foo(**opts)\n  opts\nend", 3.2 }.not_to raise_error
    end

    it 'also parses under Ruby 3.1 (jruby-parser accepts it in all versions)' do
      expect { parse "def foo(**)\n  1\nend", 3.1 }.not_to raise_error
    end
  end

  describe 'anonymous rest argument forwarding' do
    it 'parses forwarding of an anonymous rest argument' do
      expect { parse "def foo(*)\n  bar(*)\nend", 3.2 }.not_to raise_error
    end

    it 'parses forwarding with leading positional arguments' do
      expect { parse "def foo(a, *)\n  bar(a, *)\nend", 3.2 }.not_to raise_error
    end

    it 'builds a SplatNode whose value references the anonymous rest variable' do
      root = parse "def foo(*)\n  bar(*)\nend", 3.2
      splat = root.find_type(:splat)
      expect(splat).not_to be_nil
      expect(splat.value).to be_a(org.jrubyparser.ast.LocalVarNode)
      expect(splat.value.name).to eq("*")
    end

    it 'is rejected under Ruby 3.1' do
      expect { parse "def foo(*)\n  bar(*)\nend", 3.1 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'anonymous keyword rest argument forwarding' do
    it 'parses forwarding of an anonymous keyword rest argument' do
      expect { parse "def foo(**)\n  bar(**)\nend", 3.2 }.not_to raise_error
    end

    it 'parses forwarding with leading positional arguments' do
      expect { parse "def foo(a, **)\n  bar(a, **)\nend", 3.2 }.not_to raise_error
    end

    it 'parses forwarding of both anonymous rest and keyword rest' do
      expect { parse "def foo(*, **)\n  bar(*, **)\nend", 3.2 }.not_to raise_error
    end

    it 'builds a HashNode whose value references the anonymous keyword rest variable' do
      root = parse "def foo(**)\n  bar(**)\nend", 3.2
      hash = root.find_type(:hash)
      expect(hash).not_to be_nil
      array = hash.child_nodes.to_a.first
      value = array.child_nodes.to_a.first
      expect(value).to be_a(org.jrubyparser.ast.LocalVarNode)
      expect(value.name).to eq("**")
    end

    it 'is rejected under Ruby 3.1' do
      expect { parse "def foo(**)\n  bar(**)\nend", 3.1 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end
end
