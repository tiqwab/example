package com.tiqwab.example.jbc;

public class JBCBinaryOperator implements JBCExpr {

    private final String op;
    private final JBCNode lhs;
    private final JBCNode rhs;

    public JBCBinaryOperator(final String op, final JBCNode lhs, final JBCNode rhs) {
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String getOp() {
        return this.op;
    }

    @Override
    public String toString() {
        return String.format("JBCBinaryOperator{op=%s, lhs=%s, rhs=%s}", this.op, this.lhs, this.rhs);
    }

    @Override
    public void accept(JBCNodeVisitor visitor) {
        lhs.accept(visitor);
        rhs.accept(visitor);
        visitor.visit(this);
    }

}
