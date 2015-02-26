package compiler488.symbol;

import compiler488.ast.AST;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.type.Type;
import compiler488.symbol.SymbolTableEntry.SymbolKind;

/** Symbol Table
 *  This almost empty class is a framework for implementing
 *  a Symbol Table class for the CSC488S compiler
 *
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  <B> 
                Benson Quach (g0quachb)
                Eric Chen (g3chencm)
                Eric Snyder (g4snyder)
                Francesco Gramano (g2graman)
                Nicholas Dujay (c4dujayn)
                Winston Yeung (g2yeungw) 
            </B>
 */

public class MajorScope {

	public static enum ScopeKind {
		PROCEDURE,
		FUNCTION,
		PROGRAM,
		NORMAL
	}

	private SymbolTable symbolTable;
	private ScopeKind kind;
	private RoutineDecl routine;

    public MajorScope(ScopeKind kind) {
    	this(kind, null);
    }

    public MajorScope(ScopeKind kind, RoutineDecl routine) {
        this.symbolTable = new SymbolTable();
        this.kind = kind;
        this.routine = routine;
    }

	public SymbolTableEntry lookup(String varname) {
		return symbolTable.lookup(varname);
	}

	public void addEntry(String varname, Type type, SymbolKind kind, AST node) {
		symbolTable.addEntry(varname, type, kind, node);
	}

	public ScopeKind getKind() {
		return kind;
	}

	public void setKind(ScopeKind kind) {
		this.kind = kind;
	}

	public RoutineDecl getRoutine() {
		return routine;
	}

	public void setRoutine(RoutineDecl routine) {
		this.routine = routine;
	}

	@Override
	public String toString() {
		return String.format("%s: %s\n==============\n%s",
				kind,
				routine != null ? routine.getName() : "",
				symbolTable);
	}
}
