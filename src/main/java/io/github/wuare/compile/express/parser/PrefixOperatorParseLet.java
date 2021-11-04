package io.github.wuare.compile.express.parser;

import io.github.wuare.compile.express.ExpParser;
import io.github.wuare.compile.express.ExpToken;
import io.github.wuare.compile.express.ast.Expr;
import io.github.wuare.compile.express.ast.PrefixExpr;

public class PrefixOperatorParseLet implements PrefixParseLet {

    private final int precedence;

    public PrefixOperatorParseLet(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expr parse(ExpParser parser, ExpToken token) {
        parser.consume();
        Expr operand = parser.parseExp(precedence);
        return new PrefixExpr(token.getType(), operand);
    }
}
