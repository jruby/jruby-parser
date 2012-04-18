if !defined? RUBY_ENGINE || RUBY_ENGINE != "jruby"
  puts "Rake must be run from JRuby for Ant integration"
  exit 1
end

require 'rspec/core/rake_task'
require 'ant'
require 'bundler'
Bundler::GemHelper.install_tasks

task :default => [:jar, :frobnicate, :spec]

task :frobnicate do
  root = File.dirname(__FILE__)
  cp File.join(root, 'dist', 'JRubyParser.jar'), File.join(root, 'lib', 'jruby-parser.jar')
end

ant_import # load all ant targets as rake tasks

desc "Run specs"
RSpec::Core::RakeTask.new
