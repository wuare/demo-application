package io.github.wuare.compile.express.parser;

import io.github.wuare.compile.express.ExpParser;
import io.github.wuare.compile.express.ExpToken;
import io.github.wuare.compile.express.ast.Expr;
import io.github.wuare.compile.express.ast.OperatorExpr;

public class BinaryOperatorParseLet implements InfixParseLet {

    private final int precedence;

    public BinaryOperatorParseLet(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expr parse(ExpParser parser, Expr left, ExpToken token) {
        Expr right = parser.parseExp(precedence);
        return new OperatorExpr(token, token.getType(), left, right);
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }
}
