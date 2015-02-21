package compiler488.ast;

import compiler488.ast.expn.BinaryExpn;

public interface ASTVisitor {
    public void visit(AST node);

    public void visit(BinaryExpn expn);
}
