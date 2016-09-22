require_relative '../../helpers'

describe Parser do
  VERSIONS.each do |v|
    it "parses a 0-arg method sans parens [#{v}]" do
      parse("def foo\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name 'foo'
        expect(defn.args_node).to have_arg_counts(0, 0, false, false)
        expect(defn.args_node).to have_parameters()
      end
    end

    it "parses a 0-arg method with parens [#{v}]" do
      parse("def foo()\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(0, 0, false, false)
        expect(defn.args_node).to have_parameters()
      end
    end

    it "parses a 1-arg method sans parens [#{v}]" do
      parse("def foo a\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(1, 0, false, false)
        expect(defn.args_node).to have_parameters('a')
        defn.args_node.pre.tap do |pre|
          expect(pre[0]).to have_name("a")
        end
      end
    end

    it "parses a 1-arg method with parens [#{v}]" do
      parse("def foo(a)\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(1, 0, false, false)
        expect(defn.args_node).to have_parameters('a')
        defn.args_node.pre.tap do |pre|
          expect(pre[0]).to have_name("a")
        end
      end
    end

    it "parses a 2-arg method sans parens [#{v}]" do
      parse("def foo a, b\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(2, 0, false, false)
        expect(defn.args_node).to have_parameters('a', 'b')
        defn.args_node.pre.tap do |pre|
          expect(pre[0]).to have_name("a")
          expect(pre[1]).to have_name("b")
        end
      end
    end

    it "parses a 2-arg method with parens [#{v}]" do
      parse("def foo(a, b)\nend\n").find_node(:defn) do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(2, 0, false, false)
        expect(defn.args_node).to have_parameters('a', 'b')
        defn.args_node.pre.tap do |pre|
          expect(pre[0]).to have_name("a")
          expect(pre[1]).to have_name("b")
        end
      end
    end

    it "parses a 1-optarg method sans parens [#{v}]" do
      parse("def foo a=1\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(0, 1, false, false)
        expect(defn.args_node).to have_parameters('a')
        lasgn = defn.args_node.opt_args.find_node(:localasgn)
        expect(lasgn).to have_name("a")
      end
    end

    it "parses a 1-optarg method with parens [#{v}]" do
      parse("def foo(a=1)\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(0, 1, false, false)
        expect(defn.args_node).to have_parameters('a')
        lasgn = defn.args_node.opt_args.find_node(:localasgn)
        expect(lasgn).to have_name("a")
      end
    end

    it "parses a rest-arg method sans parens [#{v}]" do
      parse("def foo *a\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(0, 0, true, false)
        expect(defn.args_node).to have_parameters(['a', '*a'])
      end
    end

    it "parses a rest-arg method with parens [#{v}]" do
      parse("def foo(*a)\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(0, 0, true, false)
        expect(defn.args_node).to have_parameters(['a', '*a'])
      end
    end

    it "parses a block-arg method sans parens [#{v}]" do
      parse("def foo &a\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(0, 0, false, true)
        expect(defn.args_node).to have_parameters(['a', '&a'])
      end
    end

    it "parses a block-arg method with parens [#{v}]" do
      parse("def foo(&a)\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(0, 0, false, true)
        expect(defn.args_node).to have_parameters(['a', '&a'])
      end
    end

    it "parses a mixed-arg method sans parens [#{v}]" do
      parse("def foo a, b, c = 1, *d\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        args = defn.args_node
        expect(args).to have_arg_counts(2, 1, true, false)
        expect(defn.args_node).to have_parameters('a', 'b', 'c', ['d', '*d'])
      end
    end

    it "parses a mixed-arg method with parens [#{v}]" do
      parse("def foo(a, b, c = 1, *d)\nend\n").find_node(:defn).tap do |defn|
        expect(defn).to have_name("foo")
        expect(defn.args_node).to have_arg_counts(2, 1, true, false)
        expect(defn.args_node).to have_parameters('a', 'b', 'c', ['d', '*d'])
      end
    end
  end
end
