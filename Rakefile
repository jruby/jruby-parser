if !defined? RUBY_ENGINE || RUBY_ENGINE != "jruby"
  puts 'jruby is required to run tests'
  exit 1
end

require 'rspec/core/rake_task'
require 'bundler'
Bundler::GemHelper.install_tasks

task :default => [:package, :frobnicate, :spec]

task :package do
  `mvn clean package`
end

task :frobnicate do
  root = File.dirname(__FILE__)
  jar = Dir.glob(File.join(root, 'target', 'jrubyparser-*-SNAPSHOT.jar')).first
  cp jar, File.join(root, 'lib', 'jruby-parser.jar')
end

desc "Run specs"
RSpec::Core::RakeTask.new do |r|
  r.ruby_opts = "-J-ea -Ilib"
end
