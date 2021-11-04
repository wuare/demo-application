package io.github.wuare.compile.express.ast;

import io.github.wuare.compile.express.ExpTokenType;
import io.github.wuare.compile.express.visitor.ExpVisitor;

public class PrefixExpr implements Expr {

    private ExpTokenType tokenType;
    private Expr operand;

    public PrefixExpr(ExpTokenType tokenType, Expr operand) {
        this.tokenType = tokenType;
        this.operand = operand;
    }

    public ExpTokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(ExpTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Expr getOperand() {
        return operand;
    }

    public void setOperand(Expr operand) {
        this.operand = operand;
    }

    @Override
    public void accept(ExpVisitor visitor) {
        visitor.visitPrefixExpr(this);
    }
}
