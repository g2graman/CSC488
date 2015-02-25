package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {
    public Program(ASTList<Stmt> body) {
        super(body);
    }

    public Program(Scope scope) {
        super(scope.getBody());
    }
    
    public Boolean parentAccept(ASTVisitor<Boolean> visitor) {
    	return super.accept(visitor);
    }

    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
        return visitor.visit(this);
    }
}
