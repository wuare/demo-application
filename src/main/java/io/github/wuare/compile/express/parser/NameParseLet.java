package io.github.wuare.compile.express.parser;

import io.github.wuare.compile.express.ExpParser;
import io.github.wuare.compile.express.ExpToken;
import io.github.wuare.compile.express.ast.Expr;
import io.github.wuare.compile.express.ast.NameExpr;

public class NameParseLet implements PrefixParseLet {

    @Override
    public Expr parse(ExpParser parser, ExpToken token) {
        NameExpr nameExpr = new NameExpr(token.getText());
        parser.consume();
        return nameExpr;
    }
}
