# Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>

require_relative '../helpers'

describe 'Ruby 3.0 grammar' do
  describe 'endless method definition' do
    it 'parses an endless method with no arguments' do
      expect { parse "def foo = 1", 3.0 }.not_to raise_error
    end

    it 'parses an endless method with parenthesized arguments' do
      expect { parse "def foo(x) = x + 1", 3.0 }.not_to raise_error
    end

    it 'parses an endless singleton method' do
      expect { parse "def obj.bar = 2", 3.0 }.not_to raise_error
    end

    it 'parses an endless singleton method with arguments' do
      expect { parse "def obj.bar(x) = x", 3.0 }.not_to raise_error
    end

    it 'parses an endless method with a rescue modifier' do
      expect { parse "def foo = risky rescue 0", 3.0 }.not_to raise_error
    end

    it 'parses an endless method inside a class' do
      expect { parse "class C\n  def m = 42\n  def self.n = 7\nend", 3.0 }.not_to raise_error
    end

    it 'still parses a normal method definition' do
      expect { parse "def foo\n  1\nend", 3.0 }.not_to raise_error
    end

    it 'builds a DefnNode with a body for an endless method' do
      root = parse "def baz(x) = x + 1", 3.0
      defn = root.find_type(:defn)
      expect(defn).not_to be_nil
      expect(defn.name).to eq("baz")
      expect(defn.body).not_to be_nil
    end

    it 'builds a DefsNode for an endless singleton method' do
      root = parse "def obj.es = 2", 3.0
      defs = root.find_type(:defs)
      expect(defs).not_to be_nil
      expect(defs.name).to eq("es")
      expect(defs.receiver).not_to be_nil
    end

    it 'is rejected under Ruby 2.7' do
      expect { parse "def foo = 1", 2.7 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'one-line pattern matching' do
    it 'parses a rightward assignment with an array pattern' do
      expect { parse "[1, 2, 3] => [a, b, c]", 3.0 }.not_to raise_error
    end

    it 'parses a rightward assignment with a hash pattern' do
      expect { parse "{name: 1, age: 2} => {name:, age:}", 3.0 }.not_to raise_error
    end

    it 'parses a nested rightward assignment' do
      expect { parse "config => {db: {host:}}", 3.0 }.not_to raise_error
    end

    it 'parses a boolean one-line in match' do
      expect { parse "x in Integer", 3.0 }.not_to raise_error
    end

    it 'builds a CaseNode for a rightward assignment' do
      root = parse "data => [a, b]", 3.0
      in_node = root.find_type(:in)
      expect(in_node).not_to be_nil
      expect(in_node.expression).to be_a(org.jrubyparser.ast.ArrayPatternNode)
    end

    it 'is rejected under Ruby 2.7' do
      expect { parse "data => [a, b]", 2.7 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'find pattern' do
    def case_in(body)
      "case x\n#{body}\nend"
    end

    it 'parses a find pattern with two bare splats' do
      expect { parse case_in("in [*, target, *]\n  target"), 3.0 }.not_to raise_error
    end

    it 'parses a find pattern that binds both rest arguments' do
      expect { parse case_in("in [*pre, target, *post]\n  target"), 3.0 }.not_to raise_error
    end

    it 'parses a find pattern with multiple middle elements' do
      expect { parse case_in("in [*, a, b, *]\n  a"), 3.0 }.not_to raise_error
    end

    it 'parses a constant find pattern with brackets' do
      expect { parse case_in("in Array[*, x, *]\n  x"), 3.0 }.not_to raise_error
    end

    it 'parses a constant find pattern with parentheses' do
      expect { parse case_in("in Const(*, y, *)\n  y"), 3.0 }.not_to raise_error
    end

    it 'still parses a regular array pattern with a single splat' do
      expect { parse case_in("in [first, *rest]\n  rest"), 3.0 }.not_to raise_error
    end

    it 'builds a FindPatternNode with both rest arguments and middle args' do
      root = parse case_in("in [*pre, a, b, *post]\n  a"), 3.0
      pattern = root.find_type(:findpattern)
      expect(pattern).not_to be_nil
      expect(pattern.pre_rest_arg).not_to be_nil
      expect(pattern.post_rest_arg).not_to be_nil
      expect(pattern.args.size).to eq(2)
    end

    it 'builds a FindPatternNode with a constant' do
      root = parse case_in("in Array[*, x, *]\n  x"), 3.0
      pattern = root.find_type(:findpattern)
      expect(pattern).not_to be_nil
      expect(pattern.constant).not_to be_nil
    end

    it 'is rejected under Ruby 2.7' do
      expect {
        parse case_in("in [*, x, *]\n  x"), 2.7
      }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end
end
