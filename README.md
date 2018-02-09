# JRubyParser.jar and jruby-parser.gem

JRuby once had a parser which kept track of all sorts of extra information when it built it's Abstract Syntax Tree (AST).  Stuff like character offsets where a particular element started or ended.  The impact of this extra information was a more than noticeable amount of memory and a bit of a perf impact.  At the time we decided to discontinue having this sort of parser in JRuby we created JRubyParser.

JRubyParser.java is just the Java code which is slowly evolving into everything a Ruby IDE project could want.  Ability to know where source elements are; whether a syntax is correct; source re-writing....

Netbeans and Eclipse are two users of JRubyParser.  We have a vested interest in making parsing Ruby a convenient and simple task for Java programmers.

# Basic Usage

## Java

```java
import org.jrubyparser.CompatVersion;
import org.jrubyparser.Parser;
import org.jrubyparser.ast.*;
import org.jrubyparser.parser.ParserConfiguration;
import java.io.StringReader;

public class ParseSomething {

    public static void main(String[] args) {
        String codeString = "def foo(bar)\n bar\n end\n foo('astring')";

        Node node = parseContents(codeString);
        System.out.println(node);
    }

    public static Node parseContents(String string) {
        Parser rubyParser = new Parser();
        StringReader in = new StringReader(string);
        CompatVersion version = CompatVersion.RUBY1_8;
        ParserConfiguration config = new ParserConfiguration(0, version);
        return rubyParser.parse("<code>", in, config);
    }
}

```
In the above code, `CompatVersion` is an enum which can be `RUBY1_8`,`RUBY1_9`, `RUBY2_0`, or `RUBY2_3`. ParserConfiguration takes
several options, including linenumber, version, and optionally a static scope and can be configured with additional information about syntax gathering if necessary. 

## Ruby

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

```sh
./bin/generate_parser Ruby18Parser Ruby18
./bin/generate_parser Ruby19Parser Ruby19
./bin/generate_parser Ruby20Parser Ruby20
```

# Build and test using Rake

Compiles .java files, builds jar, copies jar to lib directory and runs the specs:

`jruby -S rake`

Builds gemfile:

`jruby -S rake build`

Runs specs:

`jruby -S rake spec`

## Build and package using Maven to get a JAR

```sh
mvn compile
mvn package
```

