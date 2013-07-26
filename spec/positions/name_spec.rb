require_relative '../helpers'

describe Parser do
  [1.8, 1.9].each do |v|
    it "should get name position for lvars [#{v}]" do
      carets_parse("^foo = 1; ^foo", v).tap do |_, (asgn, var)|
        asgn.should have_name_position(0, 0, 0, 3)
        var.should have_name_position(0, 0, 9, 12)
      end
    end
    it "should get name position for ivars [#{v}]" do
      carets_parse("^@foo = 1; ^@foo", v).tap do |_, (asgn, var)|
        asgn.should have_name_position(0, 0, 1, 4)
        var.should have_name_position(0, 0, 11, 14)
      end
    end
    it "should get name position for cvars [#{v}]" do
      carets_parse("^@@foo = 1; ^@@foo", v).tap do |_, (asgn, var)|
        asgn.should have_name_position(0, 0, 2, 5)
        var.should have_name_position(0, 0, 13, 16)
      end
    end
    it "should get name position for gvars [#{v}]" do
      carets_parse("^$foo = 1; ^$foo", v).tap do |_, (asgn, var)|
        asgn.should have_name_position(0, 0, 1, 4)
        var.should have_name_position(0, 0, 11, 14)
      end
    end
    it "should get name position for constants [#{v}]" do
      carets_parse("^FOO = 1; ^FOO", v).tap do |_, (asgn, var)|
        asgn.should have_name_position(0, 0, 0, 3)
        var.should have_name_position(0, 0, 9, 12)
      end
    end
    it "should get name position for method parameters [#{v}]" do
      carets_parse("def foo(^a, ^b=1, *^c, &^d);end", v).tap do |_, (a, b, c, d)|
        a.should have_name_position(0, 0, 8, 9)
        b.should have_name_position(0, 0, 11, 12)
        c.should have_name_position(0, 0, 17, 18)
        d.should have_name_position(0, 0, 21, 22)
      end
      if v != 1.8
        carets_parse("def foo(^a, ^b=1, ^c);end", v).tap do |_, (a, b, c)|
          c.should have_name_position(0, 0, 16,17)
        end
      end
    end
    it "should get name position for block parameters [#{v}]" do
      carets_parse("proc {|^a, *^b, &^c|}", v).tap do |_, (a, b, c)|
        a.should have_name_position(0, 0, 7, 8)
        b.should have_name_position(0, 0, 11, 12)
        c.should have_name_position(0, 0, 15, 16)
      end
    end
  end
end
