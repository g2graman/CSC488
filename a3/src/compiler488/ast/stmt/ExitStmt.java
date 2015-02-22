package compiler488.ast.stmt;

import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.expn.*;

/**
 * Represents the command to exit from a loop.
 */
public class ExitStmt extends Stmt {
    /** Condition expression for <code>exit when</code> variation. */
    private Expn expn = null;

    public ExitStmt(Expn expn) {
        super();

        this.expn = expn;
    }

    public ExitStmt() {
        this(null);
    }

    public Expn getExpn() {
        return expn;
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        p.print("exit");

        if (expn != null) {
            p.print(" when ");
            expn.prettyPrint(p);
        }
    }
    
    public Boolean parentAccept(ASTVisitor<Boolean> visitor) {
    	return super.accept(visitor);
    }

    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
        return visitor.visit(this);
    }
}
