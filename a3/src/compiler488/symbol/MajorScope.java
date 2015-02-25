package compiler488.symbol;

import java.util.LinkedList;

/** Symbol Table
 *  This almost empty class is a framework for implementing
 *  a Symbol Table class for the CSC488S compiler
 *
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  <B> PUT YOUR NAMES HERE </B>
 */

public class MajorScope {

	private LinkedList<SymbolTable> scopes;

    public MajorScope() {
        this.scopes = new LinkedList<SymbolTable>();
    }

	/**  Initialize - called once by semantic analysis
	 *                at the start of  compilation
	 *                May be unnecessary if constructor
 	 *                does all required initialization
	 */
	public void Initialize() {

	   

	}

	/**  Finalize - called once by Semantics at the end of compilation
	 *              May be unnecessary
	 */
	public void Finalize(){

	  /**  Additional finalization code for the
	   *  symbol table  class GOES HERE.
	   *
	   */
	}

	/**
	* Called for adding the most local scope
	*/
	public void addScope(SymbolTable tbl) {
		this.scopes.add(tbl); // Add to end for LIFO
	}

	public SymbolTable getMostLocalScope() {
		return this.scopes.getLast();
	}

	/**
	* Called for popping the most local scope
	*/
	public SymbolTable removeScope() {
		if(!this.scopes.isEmpty())
		{
			SymbolTable copyScope = this.getMostLocalScope();
			this.scopes.removeLast(); // Pop from end for LIFO
			return copyScope;
		}
		return null;
	}

	public LinkedList<SymbolTable> getRemainingScopes() {
		return this.scopes;
	}
}
