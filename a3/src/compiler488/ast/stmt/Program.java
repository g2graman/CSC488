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

    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
    	// visit program
        if(!visitor.visit(this)) {
        	return false;
        }
        
        // visit scope
        return super.accept(visitor);
    }
}
