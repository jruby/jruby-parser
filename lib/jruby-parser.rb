require 'java'
require 'jruby-parser.jar'
require 'jruby-parser/core_ext/array'
require 'jruby-parser/core_ext/boolean'
require 'jruby-parser/core_ext/nil'
require 'jruby-parser/core_ext/call_node'
require 'jruby-parser/core_ext/fcall_node'
require 'jruby-parser/core_ext/list_node'
require 'jruby-parser/core_ext/node'
require 'jruby-parser/core_ext/numeric' # float,fixnum
require 'jruby-parser/core_ext/op_element_asgn_node'
require 'jruby-parser/core_ext/string'
require 'jruby-parser/core_ext/args_node'

module JRubyParser
  ##
  # Parse source string and return a Abstract Syntax Tree (AST) of the source.
  # You may also pass in additional options to affect the reported filename
  # and which version of Ruby you want to use:
  #
  # === Parameters
  # * _source_string_ source you want to parse
  # * _opts_ customize how your source is parsed (:filename, and :version [defaults to 1.9])
  # === Example
  # JRubyParser.parse(%q{puts "hello world"}, :version => JRubyParser::Compat::RUBY1_8)
  #
  def parse(source_string, opts={})
    filename = opts[:filename] ?  opts[:filename] : '(string)'
    config = org.jrubyparser.parser.ParserConfiguration.new(0)
    reader = java.io.StringReader.new(source_string)
    org.jrubyparser.Parser.new.parse(filename, reader, config)
  end
  module_function :parse
end
