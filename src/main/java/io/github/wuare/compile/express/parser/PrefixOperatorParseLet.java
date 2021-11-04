package io.github.wuare.compile.express.parser;

import io.github.wuare.compile.express.ExpParser;
import io.github.wuare.compile.express.ExpToken;
import io.github.wuare.compile.express.ast.Expr;
import io.github.wuare.compile.express.ast.PrefixExpr;

public class PrefixOperatorParseLet implements PrefixParseLet {

    @Override
    public Expr parse(ExpParser parser, ExpToken token) {
        parser.consume();
        Expr operand = parser.parseExp(0);
        return new PrefixExpr(token.getType(), operand);
    }
}
