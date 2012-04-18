require 'jruby-parser'

# Extra options hash can be passed in to override parser configuration
# opts = {:version => JRubyParser::Compat::RUBY1_8, :filename => 'name.rb'}
# root = JRubyParser.parse("b = foo(1)", opts)
root = JRubyParser.parse("b = foo(1)")

# Enumerable is mixed into AST tree
# fcall = foot.find { |e| e.short_name == "fcall" }
# ...but find_node is pretty common for spec writing:
fcall = root.find_node(:fcall)

# Change the AST.
fcall.name = 'bar'

# Notice this should be a TrueNode, but true knows to coerce into TrueNode
fcall.args[0] = true

# Write out the new source "b = bar(1)"
p root.to_source
