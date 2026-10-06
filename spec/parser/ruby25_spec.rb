# Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
require_relative '../helpers'

describe 'Ruby 2.5 grammar' do
  it 'parses rescue in do..end blocks' do
    expect {
      parse <<-RUBY, 2.5
        [1, 2, 3].each do |i|
          puts i
        rescue
          puts "error"
        end
      RUBY
    }.not_to raise_error
  end

  it 'parses ensure in do..end blocks' do
    expect {
      parse <<-RUBY, 2.5
        [1, 2, 3].each do |i|
          puts i
        ensure
          puts "done"
        end
      RUBY
    }.not_to raise_error
  end

  it 'parses rescue and ensure in do..end blocks' do
    expect {
      parse <<-RUBY, 2.5
        [1, 2, 3].each do |i|
          risky(i)
        rescue RuntimeError => e
          puts e.message
        ensure
          cleanup
        end
      RUBY
    }.not_to raise_error
  end
end
