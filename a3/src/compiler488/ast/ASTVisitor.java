package compiler488.ast;

import compiler488.ast.expn.BinaryExpn;
import compiler488.ast.expn.CompareExpn;
import compiler488.ast.expn.FunctionCallExpn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.stmt.ReturnStmt;

public interface ASTVisitor<T> {
	
    public T visit(AST node);
    public T visit(BinaryExpn expn);
    public T visit(CompareExpn expn);
    
    public T visit(ReturnStmt stmt);
    public T visit(FunctionCallExpn expn);
    public T visit(SubsExpn expn);
    public T visit(IdentExpn expn);
}
