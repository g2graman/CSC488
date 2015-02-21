package compiler488.symbol;

import compiler488.ast.AST;
import compiler488.ast.type.Type;

public class SymbolTableEntry {
  String varname;
  Type type;
  String identifierType; //can be either array, variable, function, procedure
  AST node;

  public SymbolTableEntry(String varname, Type type, String identifierType, AST node){
    this.varname = varname;
    this.type = type;
    this.identifierType = identifierType;
    this.node = node;
  }
}
