# JRubyParser.jar and jruby-parser.gem

JRuby once had a parser which kept track of all sorts of extra information when it built it's Abstract Syntax Tree (AST).  Stuff like character offsets where a particular element started or ended.  The impact of this extra information was a more than noticeable amount of memory and a bit of a perf impact.  At the time we decided to discontinue having this sort of parser in JRuby we create JRubyParser.

JRubyParser.java is just the Java code which is slowly evolving into everything a Ruby IDE project could want.  Ability to know where source elements are; whether a syntax is correct; source re-writing....

Netbeans and Eclipse are two users of JRubyParser.  We have a vested interest in making parsing Ruby a convenient and simple task for Java programmers.

jruby-parser.gem is a gem which bundles JRubyParser.jar and also provides a thin Ruby layer around the Java API to follow Ruby programming idioms better.  Here is a simple example of parsing and rewriting using jruby-parser.rb:

```ruby
require 'jruby-parser'

root = JRubyParser.parse("b = foo(1)")
fcall = root.find_node(:fcall)
fcall.name = 'bar'
fcall.args[0] = true

# Write out the new source 
root.to_source # b = bar(true)
```

# Building

## Generate new parser code (only needed on hacking .y files):

Assume: [jay 1.0.2](https://github.com/jruby/jay) installed

Do not follow the instructions in the jay INSTALL file about creating a wrapper script - when you run jay you want to run the jay executable in the src directory.

```sh
./bin/generate_parser Ruby19Parser Ruby19
./bin/generate_parser Ruby18Parser Ruby18
```

# Build jruby-parser

'jruby -S rake' or 'jruby -S rake build'

## Maven

'mvn compile'
