package com.tiqwab.example;

import java.util.Arrays;

/**
 * Represents java byte codes generated from AST.
 */
public class GeneratedCode {

    private byte[] byteCodes;
    private String className;

    public GeneratedCode(final byte[] byteCodes, final String className) {
        this.byteCodes = byteCodes;
        this.className = className;
    }

    public GeneratedCode(final GeneratedCode generatedCode) {
        this(generatedCode.getByteCodes(), generatedCode.getClassName());
    }

    public byte[] getByteCodes() {
        return Arrays.copyOf(this.byteCodes, this.byteCodes.length);
    }

    public String getClassName() {
        return className;
    }

}
