package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.Printable;

/**
 * Represents the special literal constant associated with writing a new-line
 * character on the output device.
 */
public class SkipConstExpn extends ConstExpn implements Printable {
    public SkipConstExpn() {
        super();
    }

    @Override
    public String toString() {
        return "skip";
    }

    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
    	if(!super.accept(visitor)) {
    		return false;
    	}
        return visitor.visit(this);
    }
}
