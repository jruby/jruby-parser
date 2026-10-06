/*
 * Copyright (C) 2026 Piotr Hoppe <piotrhoppe@users.noreply.github.com>
 */
package org.jrubyparser;

public enum CompatVersion {

    RUBY1_8, RUBY1_9, RUBY2_0, RUBY2_3, RUBY2_4, RUBY2_5, RUBY2_6, RUBY2_7, RUBY3_0, RUBY3_1, RUBY3_2, RUBY3_3;
    
    public boolean is1_9() {
        return this == RUBY1_9 || this == RUBY2_0;
    }

    public boolean is2_0() {
        return this == RUBY2_0 || this == RUBY2_3 || this == RUBY2_4 || this == RUBY2_5 || this == RUBY2_6 || this == RUBY2_7 || this == RUBY3_0 || this == RUBY3_1 || this == RUBY3_2 || this == RUBY3_3;
    }
    
    public boolean is2_3() {
        return this == RUBY2_3 || this == RUBY2_4 || this == RUBY2_5 || this == RUBY2_6 || this == RUBY2_7 || this == RUBY3_0 || this == RUBY3_1 || this == RUBY3_2 || this == RUBY3_3;
    }

    public boolean is2_4() {
        return this == RUBY2_4 || this == RUBY2_5 || this == RUBY2_6 || this == RUBY2_7 || this == RUBY3_0 || this == RUBY3_1 || this == RUBY3_2 || this == RUBY3_3;
    }

    public boolean is2_5() {
        return this == RUBY2_5 || this == RUBY2_6 || this == RUBY2_7 || this == RUBY3_0 || this == RUBY3_1 || this == RUBY3_2 || this == RUBY3_3;
    }

    public boolean is2_6() {
        return this == RUBY2_6 || this == RUBY2_7 || this == RUBY3_0 || this == RUBY3_1 || this == RUBY3_2 || this == RUBY3_3;
    }

    public boolean is2_7() {
        return this == RUBY2_7 || this == RUBY3_0 || this == RUBY3_1 || this == RUBY3_2 || this == RUBY3_3;
    }

    public boolean is3_0() {
        return this == RUBY3_0 || this == RUBY3_1 || this == RUBY3_2 || this == RUBY3_3;
    }

    public boolean is3_1() {
        return this == RUBY3_1 || this == RUBY3_2 || this == RUBY3_3;
    }

    public boolean is3_2() {
        return this == RUBY3_2 || this == RUBY3_3;
    }

    public boolean is3_3() {
        return this == RUBY3_3;
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
        } else if (compatString.equalsIgnoreCase("RUBY2_6")) {
            return CompatVersion.RUBY2_6;
        } else if (compatString.equalsIgnoreCase("RUBY2_7")) {
            return CompatVersion.RUBY2_7;
        } else if (compatString.equalsIgnoreCase("RUBY3_0")) {
            return CompatVersion.RUBY3_0;
        } else if (compatString.equalsIgnoreCase("RUBY3_1")) {
            return CompatVersion.RUBY3_1;
        } else if (compatString.equalsIgnoreCase("RUBY3_2")) {
            return CompatVersion.RUBY3_2;
        } else if (compatString.equalsIgnoreCase("RUBY3_3")) {
            return CompatVersion.RUBY3_3;
        } else {
            return null;
        }
    }
}
