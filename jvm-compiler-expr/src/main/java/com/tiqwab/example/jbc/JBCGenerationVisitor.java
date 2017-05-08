package com.tiqwab.example.jbc;

import com.tiqwab.example.GeneratedCode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JBCGenerationVisitor implements JBCNodeVisitor {

    private ClassWriter classWriter;
    private String generatedClassName;
    private GeneratedCode generatedCode;

    private MethodVisitor mv;

    public JBCGenerationVisitor() {
        this.generatedClassName = "Calculation";
    }

    public GeneratedCode getGeneratedCode() {
        return new GeneratedCode(this.generatedCode);
    }

    private void preVisit() {
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        this.classWriter.visit(
                Opcodes.V1_8,
                Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER,
                generatedClassName,
                null,
                "java/lang/Object",
                null
        );
    }

    /**
     * Create a default constructor.
     */
    private void visitConstructor() {
        MethodVisitor mv = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null
        );
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",
                false
        );
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    /**
     * Create a 'main' method. This is for demonstration.
     */
    private void visitMain() {
        MethodVisitor mv = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null
        );
        mv.visitCode();

        // Print calculation result
        mv.visitFieldInsn(
                Opcodes.GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;"
        );
        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "Calculation",
                "calculate",
                "()I",
                false
        );
        mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(I)V",
                false
        );
        mv.visitInsn(Opcodes.RETURN);

        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    public void generateCode(JBCNode node) {
        this.preVisit();
        this.visitConstructor();
        this.visitMain();

        // Start creation of method to calculate
        mv = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                "calculate",
                "()I",
                null,
                null
        );
        mv.visitCode();

        // Generate java byte code to calculate expression
        node.accept(this);

        // Finish creation
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();

        this.generatedCode = new GeneratedCode(this.classWriter.toByteArray(), this.generatedClassName);
    }

    @Override
    public void visit(JBCEval node) {

    }

    // TODO: Code generation should be performed in JBCNode? e.g. JBCNode#gen(MethodVisitor mv)
    @Override
    public void visit(JBCBinaryOperator node) {
        if (node.getOp().equals("+")) {
            mv.visitInsn(Opcodes.IADD);
        } else if (node.getOp().equals("-")) {
            mv.visitInsn(Opcodes.ISUB);
        } else if (node.getOp().equals("*")) {
            mv.visitInsn(Opcodes.IMUL);
        } else if (node.getOp().equals("/")) {
            mv.visitInsn(Opcodes.IDIV);
        } else {
            throw new IllegalArgumentException("unknown op: " + node.getOp());
        }
    }

    // TODO: Code generation should be performed in JBCNode? e.g. JBCNode#gen(MethodVisitor mv)
    @Override
    public void visit(JBCInteger node) {
        final int value = node.getValue();
        // The generated code is determined by the necessary size (byte) of integer.
        if (-128 <= value && value < 128) {
            mv.visitIntInsn(Opcodes.BIPUSH, node.getValue());
        } else if (-32768 <= value && value < 32768){
            mv.visitIntInsn(Opcodes.SIPUSH, node.getValue());
        } else {
            mv.visitLdcInsn(value);
        }
    }

}
