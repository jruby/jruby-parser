# Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>

require_relative '../helpers'

describe 'Ruby 3.1 grammar' do
  describe 'hash value omission' do
    it 'parses a hash literal with omitted values' do
      expect { parse "x = 1\ny = 2\n{x:, y:}", 3.1 }.not_to raise_error
    end

    it 'parses a mix of omitted and explicit values' do
      expect { parse "x = 1\n{x:, y: 2}", 3.1 }.not_to raise_error
    end

    it 'parses omitted values in a method call' do
      expect { parse "x = 1\ny = 2\nfoo(x:, y:)", 3.1 }.not_to raise_error
    end

    it 'builds a HashNode whose omitted value references a local variable' do
      root = parse "x = 1\n{x:}", 3.1
      hash = root.find_type(:hash)
      expect(hash).not_to be_nil
      array = hash.child_nodes.to_a.first
      key, value = array.child_nodes.to_a
      expect(key).to be_a(org.jrubyparser.ast.SymbolNode)
      expect(key.name).to eq("x")
      expect(value).to be_a(org.jrubyparser.ast.LocalVarNode)
    end

    it 'builds a VCallNode for an omitted value that is not a local variable' do
      root = parse "{y:}", 3.1
      hash = root.find_type(:hash)
      array = hash.child_nodes.to_a.first
      _key, value = array.child_nodes.to_a
      expect(value).to be_a(org.jrubyparser.ast.VCallNode)
    end

    it 'is rejected under Ruby 3.0' do
      expect { parse "x = 1\n{x:}", 3.0 }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'pin operator with expression' do
    def case_in(body)
      "case x\n#{body}\nend"
    end

    it 'parses a pin with a parenthesized expression' do
      expect { parse case_in("in ^(1 + 2)\n  :ok"), 3.1 }.not_to raise_error
    end

    it 'parses a pin with an instance variable' do
      expect { parse case_in("in ^@expected\n  :ok"), 3.1 }.not_to raise_error
    end

    it 'parses a pin with a global variable' do
      expect { parse case_in("in ^$global\n  :ok"), 3.1 }.not_to raise_error
    end

    it 'parses a pin with a class variable' do
      expect { parse case_in("in ^@@cvar\n  :ok"), 3.1 }.not_to raise_error
    end

    it 'parses a pin expression inside a hash pattern' do
      expect { parse case_in("in {value: ^(@a + 1)}\n  :ok"), 3.1 }.not_to raise_error
    end

    it 'still parses a pin with a local variable' do
      expect { parse "y = 1\n#{case_in("in ^y\n  :ok")}", 3.1 }.not_to raise_error
    end

    it 'is rejected under Ruby 3.0' do
      expect {
        parse case_in("in ^(1 + 2)\n  :ok"), 3.0
      }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'anonymous block forwarding' do
    it 'parses an anonymous block parameter in a method definition' do
      expect { parse "def foo(&)\n  bar(&)\nend", 3.1 }.not_to raise_error
    end

    it 'parses anonymous block forwarding with other arguments' do
      expect { parse "def foo(a, &)\n  bar(a, &)\nend", 3.1 }.not_to raise_error
    end

    it 'builds a BlockPassNode for the anonymous forward in the call' do
      root = parse "def foo(&)\n  bar(&)\nend", 3.1
      block_pass = root.find_type(:blockpass)
      expect(block_pass).not_to be_nil
    end

    it 'is rejected under Ruby 3.0' do
      expect {
        parse "def foo(&)\n  bar(&)\nend", 3.0
      }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end

  describe 'endless method with command body' do
    it 'parses an endless method whose body is a command call' do
      expect { parse 'def foo = puts "hello"', 3.1 }.not_to raise_error
    end

    it 'parses an endless method with arguments and a command body' do
      expect { parse 'def foo(x) = print x', 3.1 }.not_to raise_error
    end

    it 'parses an endless singleton method with a command body' do
      expect { parse 'def obj.greet = puts "hi"', 3.1 }.not_to raise_error
    end

    it 'parses an endless command body with a rescue modifier' do
      expect { parse 'def foo = raise "e" rescue nil', 3.1 }.not_to raise_error
    end

    it 'still parses an endless method with an arg body' do
      expect { parse "def foo = 1", 3.1 }.not_to raise_error
    end

    it 'builds a DefnNode with a body for an endless command method' do
      root = parse 'def foo = puts "hi"', 3.1
      defn = root.find_type(:defn)
      expect(defn).not_to be_nil
      expect(defn.name).to eq("foo")
      expect(defn.body).not_to be_nil
    end

    it 'is rejected under Ruby 3.0' do
      expect {
        parse 'def foo = puts "hello"', 3.0
      }.to raise_error(org.jrubyparser.lexer.SyntaxException)
    end
  end
end
