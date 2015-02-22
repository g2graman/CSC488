package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.Readable;

/**
 *  References to a scalar variable or function call without parameters.
 */
public class IdentExpn extends Expn implements Readable {
    /** Name of the identifier. */
    private String ident;

    public IdentExpn(String ident) {
        super();

        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    /**
     * Returns the name of the variable or function.
     */
    @Override
    public String toString() {
        return ident;
    }

    public Boolean parentAccept(ASTVisitor<Boolean> visitor) {
    	return super.accept(visitor);
    }
    
	@Override
	public Boolean accept(ASTVisitor<Boolean> visitor) {
		return visitor.visit(this);
	}
}
