package io.github.wuare.compile.express.parser;

import io.github.wuare.compile.express.ExpParser;
import io.github.wuare.compile.express.ExpToken;
import io.github.wuare.compile.express.ast.Expr;
import io.github.wuare.compile.express.ast.PrefixExpr;

public class ParenParseLet implements PrefixParseLet {

    @Override
    public Expr parse(ExpParser parser, ExpToken token) {
        parser.consume();
        Expr expr = parser.parseExp(0);
        parser.consume();
        return new PrefixExpr(token.getType(), expr);
    }
}
