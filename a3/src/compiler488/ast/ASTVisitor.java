package compiler488.ast;

import compiler488.ast.expn.BinaryExpn;
import compiler488.ast.expn.CompareExpn;

public interface ASTVisitor<T> {
	
    public T visit(AST node);
    public T visit(BinaryExpn expn);
    public T visit(CompareExpn expn);
}
