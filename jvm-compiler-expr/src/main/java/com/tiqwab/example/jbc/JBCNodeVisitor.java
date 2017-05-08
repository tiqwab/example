package com.tiqwab.example.jbc;

public interface JBCNodeVisitor {

    public void visit(JBCEval node);
    public void visit(JBCBinaryOperator node);
    public void visit(JBCInteger node);

}
