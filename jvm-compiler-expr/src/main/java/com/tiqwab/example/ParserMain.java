package com.tiqwab.example;

import com.tiqwab.example.jbc.JBCGenerationVisitor;
import com.tiqwab.example.jbc.JBCNode;

public class ParserMain {

    public static void main(String[] args) throws Exception {
        System.out.println();
        Parser parser = new Parser(System.in);
        JBCNode node = parser.Start();

        System.out.println("Generate java bytecodes...");
        System.out.println(node.toString());
        JBCGenerationVisitor jbcGenerationVisitor = new JBCGenerationVisitor();
        jbcGenerationVisitor.generateCode(node);
        new GeneratedCodeOutputter().output(jbcGenerationVisitor.getGeneratedCode());
    }

}
