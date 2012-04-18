# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)
require "jruby-parser/version"

files = `git ls-files -- lib/* spec/* sample/*`.split("\n")
files << 'lib/jruby-parser.jar'

Gem::Specification.new do |s|
  s.name        = 'jruby-parser'
  s.version     = JRubyParser::VERSION
  s.platform    = Gem::Platform::RUBY
  s.authors     = 'Thomas E. Enebo'
  s.email       = 'tom.enebo@gmail.com'
  s.homepage    = 'http://github.com/jruby/jruby-parser'
  s.summary     = %q{A Gem for syntactically correct parse trees of Ruby source}
  s.description = %q{A Gem for syntactically correct parse trees of Ruby source}

  s.rubyforge_project = "jruby-parser"

  s.files         = files
  s.test_files    = `git ls-files -- spec/*`.split("\n")
  s.executables   = `git ls-files -- bin/*`.split("\n").map{ |f| File.basename(f) }
  s.require_paths = ["lib"]
  s.has_rdoc      = true
end
