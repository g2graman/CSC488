package compiler488.symbol;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

import compiler488.ast.AST;
import compiler488.ast.type.Type;

/** Symbol Table
 *  This almost empty class is a framework for implementing
 *  a Symbol Table class for the CSC488S compiler
 *
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  <B> PUT YOUR NAMES HERE </B>
 */

public class SymbolTable {

	/** Symbol Table  constructor
         *  Create and initialize a symbol table
	 */
    private Map<String, SymbolTableEntry> entry;
    public SymbolTable  (){
        this.entry = new HashMap<String, SymbolTableEntry>();
    }

	/**  Initialize - called once by semantic analysis
	 *                at the start of  compilation
	 *                May be unnecessary if constructor
 	 *                does all required initialization
	 */
	public void Initialize() {

	   /**   Initialize the symbol table
	    *	Any additional symbol table initialization
	    *  GOES HERE
	    */

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


	/** The rest of Symbol Table
	 *  Data structures, public and private functions
 	 *  to implement the Symbol Table
	 *  GO HERE.
	 */
/**
     * @param varname The name of the variable to lookup
     * @return The symbol table entry that corresponds to the specific variable name. Returns null
     *         if the variable does not exist in this scope.
     */
    public SymbolTableEntry lookup(String varname){
        return entry.get(varname);
    }

    /**
     * Add the symbol table entry to the symbol table.
     * @param varname The name of the variable to insert into the table
     * @param type The type(boolean or string or integer) of the variable
     * @param kind The kind(procedure, function, array or scalar) of the variable
     * @param node The AST node
     */
    public void addEntry(String varname, Type type, String kind, AST node, String type_of_scope) {
        SymbolTableEntry st_entry = new SymbolTableEntry(varname, type, kind, node);
        entry.put(varname, st_entry);
    }
}
