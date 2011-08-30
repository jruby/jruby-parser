/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jrubyparser;

/**
 *
 * @author enebo
 */
public class RegexpOptions implements Cloneable {
    public static final RegexpOptions NULL_OPTIONS = new RegexpOptions(true);
    
    public RegexpOptions() {
        this(true);
    }
    
    public RegexpOptions(boolean isKCodeDefault) {
        this.kcodeDefault = isKCodeDefault;
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
        return multiline || ignorecase || extended || !isKcodeDefault();
    }    

    public boolean isEmbeddable() {
        return multiline && ignorecase && extended;
    }
    
    public void setKCodeChar(char value) {
        this.kcodeChar = value;
    }
    
    public char getKCodeChar() {
        return kcodeChar;
    }

    public RegexpOptions withoutOnce() {
        RegexpOptions options = (RegexpOptions)clone();
        options.setOnce(false);
        return options;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.fixed ? 1 : 0);
        hash = 11 * hash + (this.once ? 1 : 0);
        hash = 11 * hash + (this.extended ? 1 : 0);
        hash = 11 * hash + (this.multiline ? 1 : 0);
        hash = 11 * hash + (this.ignorecase ? 1 : 0);
        hash = 11 * hash + (this.java ? 1 : 0);
        hash = 11 * hash + (this.encodingNone ? 1 : 0);
        hash = 11 * hash + (this.kcodeDefault ? 1 : 0);
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
        return o.encodingNone == encodingNone &&
               o.extended == extended &&
               o.fixed == fixed &&
               o.ignorecase == ignorecase &&
               o.java == java &&
               o.kcodeDefault == kcodeDefault &&
               o.multiline == multiline; 
    }
    
    @Override
    public String toString() {
        return "RegexpOptions(" +
                (encodingNone == true ? ", encodingNone" : "") +
                (extended == true ? ", extended" : "") +
                (fixed == true ? ", fixed" : "") +
                (ignorecase == true ? ", ignorecase" : "") +
                (java == true ? ", java" : "") +
                (kcodeDefault == true ? ", kcodeDefault" : "") +
                (multiline == true ? ", multiline" : "") +
                (once == true ? ", once" : "") +                
                ")";
    }
    
    private boolean fixed;
    private boolean once;
    private boolean extended;
    private boolean multiline;
    private boolean ignorecase;
    private boolean java;
    private boolean encodingNone;
    private boolean kcodeDefault;
    private char kcodeChar;
}
