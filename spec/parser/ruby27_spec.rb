# Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
require_relative '../helpers'

describe 'Ruby 2.7 grammar' do
  describe 'beginless range' do
    it 'parses an inclusive beginless range' do
      expect { parse "(..10)", 2.7 }.not_to raise_error
    end

    it 'parses an exclusive beginless range' do
      expect { parse "(...10)", 2.7 }.not_to raise_error
    end

    it 'builds a DotNode with an implicit nil begin' do
      root = parse "(..10)", 2.7
      dot = root.find_type(:dot)
      expect(dot).not_to be_nil
      expect(dot.begin).to be_a(org.jrubyparser.ast.ImplicitNilNode)
      expect(dot.end).to be_a(org.jrubyparser.ast.FixnumNode)
      expect(dot.exclusive).to be false
    end

    it 'is rejected under Ruby 2.6' do
      expect { parse "(..10)", 2.6 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'argument forwarding' do
    it 'parses a method definition that forwards all arguments' do
      expect { parse "def foo(...); bar(...); end", 2.7 }.not_to raise_error
    end

    it 'parses leading arguments before the forward marker' do
      expect { parse "def foo(a, ...); bar(a, ...); end", 2.7 }.not_to raise_error
    end

    it 'is rejected under Ruby 2.6' do
      expect { parse "def foo(...); end", 2.6 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'no-keyword marker (**nil)' do
    it 'parses **nil in a method definition' do
      expect { parse "def foo(**nil); end", 2.7 }.not_to raise_error
    end

    it 'parses **nil alongside positional args' do
      expect { parse "def foo(a, **nil); end", 2.7 }.not_to raise_error
    end

    it 'is rejected under Ruby 2.6' do
      expect { parse "def foo(**nil); end", 2.6 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'rescue modifier on multiple assignment' do
    it 'parses a, b = expr rescue fallback' do
      expect { parse "a, b = risky rescue [1, 2]", 2.7 }.not_to raise_error
    end
  end

  describe 'pattern matching' do
    def case_in(body)
      "case x\n#{body}\nend"
    end

    it 'parses a literal pattern' do
      expect { parse case_in("in 1\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses an array pattern' do
      expect { parse case_in("in [1, 2]\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses an array pattern that binds variables' do
      expect { parse case_in("in [a, b]\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses an array pattern with a splat' do
      expect { parse case_in("in [a, *rest]\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses an array pattern with pre and post around a splat' do
      expect { parse case_in("in [a, *, b]\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a hash pattern with explicit values' do
      expect { parse case_in("in {a: 1}\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a hash pattern with bare keys' do
      expect { parse case_in("in {a:, b:}\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a hash pattern with a double splat rest' do
      expect { parse case_in("in {a: 1, **rest}\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a hash pattern with **nil' do
      expect { parse case_in("in {a: 1, **nil}\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a binding pattern (pattern => var)' do
      expect { parse case_in("in Integer => n\n  n"), 2.7 }.not_to raise_error
    end

    it 'parses an alternative pattern (a | b)' do
      expect { parse case_in("in 1 | 2 | 3\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a pin pattern (^var)' do
      expect { parse case_in("in ^y\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a range pattern' do
      expect { parse case_in("in 1..10\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a beginless range pattern' do
      expect { parse case_in("in ..10\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a constant array pattern with parentheses' do
      expect { parse case_in("in Point(x:, y:)\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses a constant array pattern with brackets' do
      expect { parse case_in("in Point[a, b]\n  a"), 2.7 }.not_to raise_error
    end

    it 'parses multiple in clauses with an else' do
      expect {
        parse "case x\nin [1]\n  a\nin [2]\n  b\nelse\n  c\nend", 2.7
      }.not_to raise_error
    end

    it 'parses a one-line pattern match' do
      expect { parse "x in [a, b]", 2.7 }.not_to raise_error
    end

    it 'builds an InNode for a case/in expression' do
      root = parse case_in("in 1\n  a"), 2.7
      in_node = root.find_type(:in)
      expect(in_node).not_to be_nil
      expect(in_node.expression).to be_a(org.jrubyparser.ast.FixnumNode)
    end

    it 'builds an ArrayPatternNode for an array pattern' do
      root = parse case_in("in [a, *rest]\n  a"), 2.7
      pattern = root.find_type(:arraypattern)
      expect(pattern).not_to be_nil
      expect(pattern.has_rest_arg?).to be true
      expect(pattern.pre_args.size).to eq(1)
    end

    it 'builds a HashPatternNode with no-rest for **nil' do
      root = parse case_in("in {a: 1, **nil}\n  a"), 2.7
      pattern = root.find_type(:hashpattern)
      expect(pattern).not_to be_nil
      expect(pattern.no_rest?).to be true
    end

    it 'builds a PatternBindNode for a binding pattern' do
      root = parse case_in("in Integer => n\n  n"), 2.7
      bind = root.find_type(:patternbind)
      expect(bind).not_to be_nil
      expect(bind.target).not_to be_nil
    end

    it 'rejects case/in under Ruby 2.6' do
      expect {
        parse case_in("in [1, 2]\n  a"), 2.6
      }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end
end
