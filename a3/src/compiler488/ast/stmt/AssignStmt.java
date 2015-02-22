package compiler488.ast.stmt;

import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.expn.Expn;

/**
 * Holds the assignment of an expression to a variable.
 */
public class AssignStmt extends Stmt {
    /** The location being assigned to. */
    private Expn lval;

    /** The value being assigned. */
    private Expn rval;

    public AssignStmt(Expn lval, Expn rval) {
        super();

        this.lval = lval;
        this.rval = rval;
    }

    public Expn getLval() {
        return lval;
    }

    public Expn getRval() {
        return rval;
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        lval.prettyPrint(p);
        p.print(" <= ");
        rval.prettyPrint(p);
    }

    public Boolean parentAccept(ASTVisitor<Boolean> visitor) {
    	return super.accept(visitor);
    }
    
    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
        return visitor.visit(this);
    }
}
