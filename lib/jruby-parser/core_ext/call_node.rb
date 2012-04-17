require 'java'
require 'jruby-parser/util/coercer'

class org::jrubyparser::ast::CallNode
  include JRubyParser::Receiver, JRubyParser::Value, JRubyParser::Args
end