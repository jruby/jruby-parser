if !defined? RUBY_ENGINE || RUBY_ENGINE != "jruby"
  puts "Rake must be run from JRuby for Ant integration"
  exit 1
end

require 'rspec/core/rake_task'
require 'ant'

task :default => [:jar, :spec]

ant_import # load all ant targets as rake tasks

desc "Run specs"
RSpec::Core::RakeTask.new
