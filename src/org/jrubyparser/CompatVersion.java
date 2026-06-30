package org.jrubyparser;

public enum CompatVersion {

    RUBY1_8, RUBY1_9, RUBY2_0, RUBY2_3, RUBY2_4, RUBY2_5;
    
    public boolean is1_9() {
        return this == RUBY1_9 || this == RUBY2_0;
    }

    public boolean is2_0() {
        return this == RUBY2_0 || this == RUBY2_3 || this == RUBY2_4 || this == RUBY2_5;
    }
    
    public boolean is2_3() {
        return this == RUBY2_3 || this == RUBY2_4 || this == RUBY2_5;
    }

    public boolean is2_4() {
        return this == RUBY2_4 || this == RUBY2_5;
    }

    public boolean is2_5() {
        return this == RUBY2_5;
    }

    public static CompatVersion getVersionFromString(String compatString) {
        if (compatString.equalsIgnoreCase("RUBY1_8")) {
            return CompatVersion.RUBY1_8;
        } else if (compatString.equalsIgnoreCase("RUBY1_9")) {
            return CompatVersion.RUBY1_9;
        } else if (compatString.equalsIgnoreCase("RUBY2_0")) {
            return CompatVersion.RUBY2_0;
        } else if (compatString.equalsIgnoreCase("RUBY2_3")) {
            return CompatVersion.RUBY2_3;
        } else if (compatString.equalsIgnoreCase("RUBY2_4")) {
            return CompatVersion.RUBY2_4;
        } else if (compatString.equalsIgnoreCase("RUBY2_5")) {
            return CompatVersion.RUBY2_5;
        } else {
            return null;
        }
    }
}
