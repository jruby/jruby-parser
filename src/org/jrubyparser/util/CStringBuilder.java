package org.jrubyparser.util;

import java.util.Arrays;

/**
 * The sole purpose of this builder is to allow append(int) do a forced cast to a char.
 * Our lexer returns int's for each character and builder till convert ints to base-10
 * decimal strings.  This just makes things less error-prone
 */
public class CStringBuilder implements CharSequence {
    private java.lang.StringBuilder builder;

    public CStringBuilder() {
        builder = new java.lang.StringBuilder();
    }

    public CStringBuilder(int capacity) {
        builder = new java.lang.StringBuilder(capacity);
    }

    public CStringBuilder(String initialValue) {
        builder = new java.lang.StringBuilder(initialValue);
    }

    /**
     * This will assume the passed in int is in fact a char.
     * @param value char to be consumed
     * @return the builder
     */
    public CStringBuilder append(int value) {
        builder.append((char) value);

        return this;
    }

    public CStringBuilder append(byte[] values) {
        builder.append(Arrays.toString(values));

        return this;
    }

    public CStringBuilder append(String value) {
        builder.append(value);

        return this;
    }


    public CStringBuilder append(boolean value) {
        builder.append(value);

        return this;
    }

    public CStringBuilder append(Object value) {
        builder.append(value);

        return this;
    }

    public int length() {
        return builder.length();
    }

    public String substring(int startIndex) {
        return builder.substring(startIndex);
    }

    public void setLength(int length) {
        builder.setLength(length);
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public char charAt(int index) {
        return builder.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }
}
