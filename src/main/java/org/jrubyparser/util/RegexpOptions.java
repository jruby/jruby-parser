/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jrubyparser.util;

import org.jruby.util.ByteList;
import org.jrubyparser.parser.ReOptions;

public class RegexpOptions implements Cloneable {
    private static ByteList WINDOWS31J = new ByteList(new byte[] {'W', 'i', 'n', 'd', 'o', 'w', 's', '-', '3', '1', 'J'});
    public static final RegexpOptions NULL_OPTIONS = new RegexpOptions(KCode.NONE, true);

    public RegexpOptions() {
        this(KCode.NONE, true);
    }

    public RegexpOptions(KCode kcode, boolean isKCodeDefault) {
        this.kcode = kcode;
        this.kcodeDefault = isKCodeDefault;

        assert kcode != null : "kcode must always be set to something";
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isIgnorecase() {
        return ignorecase;
    }

    public void setIgnorecase(boolean ignorecase) {
        this.ignorecase = ignorecase;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public KCode getKCode() {
        return kcode;
    }

    public String getKCodeName() {
        return isKcodeDefault() ? null : getKCode().name().toLowerCase();
    }

    /**
     * This regexp has an explicit encoding flag or 'nesu' letter associated
     * with it.
     *
     * @param kcode to be set
     */
    public void setExplicitKCode(KCode kcode) {
        this.kcode = kcode;
        kcodeDefault = false;
    }

    private KCode getExplicitKCode() {
        if (kcodeDefault == true) return null;

        return kcode;
    }

    /**
     * Whether the kcode associated with this regexp is implicit (aka
     * default) or is specified explicitly (via 'nesu' syntax postscript or
     * flags to Regexp.new.
     */
    public boolean isKcodeDefault() {
        return kcodeDefault;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public boolean isOnce() {
        return once;
    }

    public void setOnce(boolean once) {
        this.once = once;
    }

    public boolean isJava() {
        return java;
    }

    public void setJava(boolean java) {
        this.java = java;
    }

    public boolean isEncodingNone() {
        return encodingNone;
    }

    public void setEncodingNone(boolean encodingNone) {
        this.encodingNone = encodingNone;
    }

    public boolean isLiteral() {
        return literal;
    }

    public void setLiteral(boolean literal) {
        this.literal = literal;
    }

    public boolean isEmbeddable() {
        return multiline && ignorecase && extended;
    }

    public RegexpOptions withoutOnce() {
        RegexpOptions options = (RegexpOptions)clone();
        options.setOnce(false);
        return options;
    }

    /**
     * This int value is meant to only be used when dealing directly with
     * the joni regular expression library.  It differs from embeddedOptions
     * in that it only contains bit values which Joni cares about.
     */
    public int toJoniOptions() {
        int options = 0;
        // Note: once is not an option that is pertinent to Joni so we exclude it.
        if (multiline) options |= ReOptions.RE_OPTION_MULTILINE;
        if (ignorecase) options |= ReOptions.RE_OPTION_IGNORECASE;
        if (extended) options |= ReOptions.RE_OPTION_EXTENDED;
        return options;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.kcode != null ? this.kcode.hashCode() : 0);
        hash = 11 * hash + (this.fixed ? 1 : 0);
        hash = 11 * hash + (this.once ? 1 : 0);
        hash = 11 * hash + (this.extended ? 1 : 0);
        hash = 11 * hash + (this.multiline ? 1 : 0);
        hash = 11 * hash + (this.ignorecase ? 1 : 0);
        hash = 11 * hash + (this.java ? 1 : 0);
        hash = 11 * hash + (this.encodingNone ? 1 : 0);
        hash = 11 * hash + (this.kcodeDefault ? 1 : 0);
        hash = 11 * hash + (this.literal ? 1 : 0);
        return hash;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnse) {throw new RuntimeException(cnse);}
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof RegexpOptions)) return false;

        // Note: literal and once can be different in this object but for the
        // sake of equality we ignore those two fields since those flags do
        // not affect Ruby equality.
        RegexpOptions o = (RegexpOptions)other;
        boolean equality = o.extended == extended &&
                           o.fixed == fixed &&
                           o.ignorecase == ignorecase &&
                           o.java == java &&
                           o.multiline == multiline;
        if(encodingNone || o.encodingNone) {
            return equality && o.kcode == kcode;
        } else {
            return equality &&
                    o.encodingNone == encodingNone &&
                    o.kcode == kcode &&
                    o.kcodeDefault == kcodeDefault;
        }
    }

    @Override
    public String toString() {
        return "RegexpOptions(kcode: " + kcode +
                (encodingNone == true ? ", encodingNone" : "") +
                (extended == true ? ", extended" : "") +
                (fixed == true ? ", fixed" : "") +
                (ignorecase == true ? ", ignorecase" : "") +
                (java == true ? ", java" : "") +
                (kcodeDefault == true ? ", kcodeDefault" : "") +
                (literal == true ? ", literal" : "") +
                (multiline == true ? ", multiline" : "") +
                (once == true ? ", once" : "") +
                ")";
    }

    private KCode kcode;
    private boolean fixed;
    private boolean once;
    private boolean extended;
    private boolean multiline;
    private boolean ignorecase;
    private boolean java;
    private boolean encodingNone;
    private boolean kcodeDefault;
    private boolean literal;
}
