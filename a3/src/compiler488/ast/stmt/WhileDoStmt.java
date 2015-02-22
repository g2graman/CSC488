package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.expn.Expn;

/**
 * Represents a loop in which the exit condition is evaluated before each pass.
 */
public class WhileDoStmt extends LoopingStmt {
    public WhileDoStmt(Expn expn, ASTList<Stmt> body) {
        super(expn, body);
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        p.print("while ");
        expn.prettyPrint(p);
        p.println(" do");

        body.prettyPrintBlock(p);

        p.println("end");
    }

    public Boolean parentAccept(ASTVisitor<Boolean> visitor) {
    	return super.accept(visitor);
    }
    
    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
        return visitor.visit(this);
    }
}
