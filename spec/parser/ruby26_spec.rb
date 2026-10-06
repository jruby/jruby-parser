# Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
require_relative '../helpers'

describe 'Ruby 2.6 grammar' do
  it 'parses an inclusive endless range' do
    expect {
      parse "(1..)", 2.6
    }.not_to raise_error
  end

  it 'parses an exclusive endless range' do
    expect {
      parse "(1...)", 2.6
    }.not_to raise_error
  end

  it 'parses an endless range with a non-literal begin' do
    expect {
      parse "(a..)", 2.6
    }.not_to raise_error
  end

  it 'builds a DotNode with an implicit nil end for an endless range' do
    root = parse "(1..)", 2.6
    dot = root.find_type(:dot)
    expect(dot).not_to be_nil
    expect(dot.begin).to be_a(org.jrubyparser.ast.FixnumNode)
    expect(dot.end).to be_a(org.jrubyparser.ast.ImplicitNilNode)
    expect(dot.exclusive).to be false
  end

  it 'rewrites an endless range without a trailing nil' do
    expect(rparse("1..", 2.6).to_source.strip).to eq("1..")
    expect(rparse("1...", 2.6).to_source.strip).to eq("1...")
  end

  it 'parses rescue in a stabby-lambda do..end block' do
    expect {
      parse <<-RUBY, 2.6
        f = -> do
          risky
        rescue => e
          handle(e)
        end
      RUBY
    }.not_to raise_error
  end

  it 'parses ensure in a stabby-lambda do..end block' do
    expect {
      parse <<-RUBY, 2.6
        f = -> do
          work
        ensure
          cleanup
        end
      RUBY
    }.not_to raise_error
  end
end
