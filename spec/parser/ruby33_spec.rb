# Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>

require_relative '../helpers'

describe 'Ruby 3.3 grammar' do
  describe 'brace block on a constant-path method call' do
    it 'parses a brace block attached to a constant path call' do
      expect { parse "Foo::Bar { 1 }", 3.3 }.not_to raise_error
    end

    it 'parses a brace block with block parameters' do
      expect { parse "Foo::Bar { |a| a + 1 }", 3.3 }.not_to raise_error
    end

    it 'parses a nested constant path with a brace block' do
      expect { parse "A::B::C { x }", 3.3 }.not_to raise_error
    end

    it 'parses a constant path brace block in an assignment' do
      expect { parse "result = Foo::Bar { 2 }", 3.3 }.not_to raise_error
    end

    it 'still parses a plain constant path reference' do
      expect { parse "Foo::Bar", 3.3 }.not_to raise_error
    end

    it 'rejects a do/end block on a constant path (only braces allowed)' do
      expect { parse "Foo::Bar do |a| a end", 3.3 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end

    it 'builds a CallNode with a receiver and an iter block' do
      root = parse "Foo::Bar { |a| a }", 3.3
      call = root.find_type(:call)
      expect(call).not_to be_nil
      expect(call.name).to eq("Bar")
      expect(call.receiver).to be_a(org.jrubyparser.ast.ConstNode)
      expect(call.iter).to be_a(org.jrubyparser.ast.IterNode)
    end

    it 'is rejected under Ruby 3.2' do
      expect { parse "Foo::Bar { 1 }", 3.2 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'endless method definition with not' do
    it 'parses an endless method whose body is a negated expression' do
      expect { parse "def foo = not true", 3.3 }.not_to raise_error
    end

    it 'parses an endless method whose body negates a command call' do
      expect { parse "def foo = not bar(1)", 3.3 }.not_to raise_error
    end

    it 'parses an endless singleton method with a negated body' do
      expect { parse "def obj.foo = not x", 3.3 }.not_to raise_error
    end

    it 'parses a doubly negated endless body' do
      expect { parse "def foo = not not true", 3.3 }.not_to raise_error
    end

    it 'still parses an endless method with an arg body' do
      expect { parse "def foo = 1", 3.3 }.not_to raise_error
    end

    it 'still parses an endless method with a command body' do
      expect { parse 'def foo = puts "x"', 3.3 }.not_to raise_error
    end

    it 'still parses an endless method with a rescue modifier' do
      expect { parse "def foo = risky rescue 0", 3.3 }.not_to raise_error
    end

    it 'builds a DefnNode whose body is a negation call' do
      root = parse "def foo = not true", 3.3
      defn = root.find_type(:defn)
      expect(defn).not_to be_nil
      expect(defn.name).to eq("foo")
      expect(defn.body).to be_a(org.jrubyparser.ast.CallNode)
      expect(defn.body.name).to eq("!")
    end

    it 'is rejected under Ruby 3.2' do
      expect { parse "def foo = not true", 3.2 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end
end
