package compiler488.symbol;

import compiler488.ast.AST;
import compiler488.ast.type.Type;

public class SymbolTableEntry {
	
	public static enum SymbolKind {
		SCALAR,
		ARRAY,
		FUNCTION,
		PROCEDURE
	}
	
	private String varname;
	private Type type;
	private SymbolKind kind; // can be either array, variable, function, procedure
	private AST node;
	private int off;

	public SymbolTableEntry(String varname, Type type, SymbolKind identifierType,
			AST node, int off) {
		this.varname = varname;
		this.type = type;
		this.kind = identifierType;
		this.node = node;
		this.off = off;
	}

	public String getVarname() {
		return varname;
	}

	public Type getType() {
		return type;
	}

	public SymbolKind getKind() {
		return kind;
	}

	public AST getNode() {
		return node;
	}

	public int getOff() {
		return off;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", this.kind, this.type, this.varname);
	}
}
