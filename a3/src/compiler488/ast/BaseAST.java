package compiler488.ast;

/**
 * Base class implementation for the AST hierarchy.
 *
 * This is a convenient place to add common behaviours.
 *
 * @author Dave Wortman, Marsha Chechik, Danny House, Peter McCormick
 */
public abstract class BaseAST implements AST {
	
	private int line;
	private int column;
	/**
	 * Default constructor.
	 *
	 * <p>
	 * Add additional information to your AST tree nodes here.
	 * </p>
	 */
	public BaseAST() {
	}

	/**
	 * A default pretty-printer implementation that uses <code>toString</code>.
	 *
	 * @param p
	 *            the printer to use
	 */
	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.print(toString());
	}

	@Override
	public Boolean accept(ASTVisitor<Boolean> visitor) {
		return visitor.visit(this);
	}

	public void setLocation(int line, int column) {
		this.line = line;
		this.column = column;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getCol() {
		return column;
	}
}
