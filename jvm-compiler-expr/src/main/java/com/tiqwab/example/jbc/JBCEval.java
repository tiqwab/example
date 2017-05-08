package com.tiqwab.example.jbc;

public class JBCEval implements JBCStmt {

    private final JBCExpr expr;

    public JBCEval(final JBCExpr expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return String.format("JBCEval{expr=%s}", this.expr.toString());
    }

    @Override
    public void accept(JBCNodeVisitor visitor) {
        expr.accept(visitor);
        visitor.visit(this);
    }

}
