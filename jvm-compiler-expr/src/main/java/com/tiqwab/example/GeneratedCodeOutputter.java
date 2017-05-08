package com.tiqwab.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Output GeneratedCode.
 */
public class GeneratedCodeOutputter {

    public GeneratedCodeOutputter() {
    }

    private String convertToClassFileName(final String className) {
        return className + ".class";
    }

    /**
     * Output GeneratedCode as a class file.
     * @param generatedCode
     * @return
     * @throws IOException
     */
    public Path output(final GeneratedCode generatedCode) throws IOException {
        final byte[] generateCodes = generatedCode.getByteCodes();
        final Path outputClassFilePath = Paths.get(convertToClassFileName(generatedCode.getClassName()));
        return Files.write(outputClassFilePath, generateCodes);
    }

}
