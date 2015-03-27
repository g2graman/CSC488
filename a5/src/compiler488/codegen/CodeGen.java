package compiler488.codegen;

import java.io.*;
import java.util.*;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.runtime.ExecutionException ;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.AST;
import compiler488.ast.BasePrettyPrinter;
import compiler488.ast.PrettyPrintable;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.decl.ScalarDeclPart;
import compiler488.ast.expn.AnonFuncExpn;
import compiler488.ast.expn.ArithExpn;
import compiler488.ast.expn.BinaryExpn;
import compiler488.ast.expn.BoolConstExpn;
import compiler488.ast.expn.BoolExpn;
import compiler488.ast.expn.CompareExpn;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.FunctionCallExpn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.stmt.ReturnStmt;
import compiler488.ast.type.IntegerType;
import compiler488.ast.type.Type;
import compiler488.ast.expn.ConstExpn;
import compiler488.ast.expn.EqualsExpn;
import compiler488.ast.expn.IntConstExpn;
import compiler488.ast.expn.NotExpn;
import compiler488.ast.expn.SkipConstExpn;
import compiler488.ast.expn.TextConstExpn;
import compiler488.ast.expn.UnaryExpn;
import compiler488.ast.expn.UnaryMinusExpn;
import compiler488.ast.stmt.AssignStmt;
import compiler488.ast.stmt.ExitStmt;
import compiler488.ast.stmt.GetStmt;
import compiler488.ast.stmt.IfStmt;
import compiler488.ast.stmt.LoopStmt;
import compiler488.ast.stmt.LoopingStmt;
import compiler488.ast.stmt.ProcedureCallStmt;
import compiler488.ast.stmt.Program;
import compiler488.ast.stmt.PutStmt;
import compiler488.ast.stmt.Scope;
import compiler488.ast.stmt.Stmt;
import compiler488.ast.stmt.WhileDoStmt;
import compiler488.ast.type.BooleanType;
import compiler488.symbol.MajorScope.ScopeKind;
import compiler488.symbol.SymbolTableEntry;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTableEntry.SymbolKind;
import compiler488.symbol.MajorScope;
import compiler488.ast.Printable;

/**      CodeGenerator.java 
 *<pre>
 *  Code Generation Conventions
 *
 *  To simplify the course project, this code generator is
 *  designed to compile directly to pseudo machine memory
 *  which is available as the private array memory[]
 *
 *  It is assumed that the code generator places instructions
 *  in memory in locations
 *
 *      memory[ 0 .. startMSP - 1 ]
 *
 *  The code generator may also place instructions and/or
 *  constants in high memory at locations (though this may
 *  not be necessary)
 *      memory[ startMLP .. Machine.memorySize - 1 ]
 *
 *  During program exection the memory area
 *      memory[ startMSP .. startMLP - 1 ]
 *  is used as a dynamic stack for storing activation records
 *  and temporaries used during expression evaluation.
 *  A hardware exception (stack overflow) occurs if the pointer
 *  for this stack reaches the memory limit register (mlp).
 *
 *  The code generator is responsible for setting the global
 *  variables:
 *      startPC         initial value for program counter
 *      startMSP        initial value for msp
 *      startMLP        initial value for mlp
 * </pre>
 * @author  <B> PUT YOUR NAMES HERE </B>
 */

public class CodeGen implements ASTVisitor<Boolean>
    {

    /** initial value for memory stack pointer */
    private short startMSP;
    /** initial value for program counter */
    private short startPC;
    /** initial value for memory limit pointer */
    private short startMLP;

    /** flag for tracing code generation */
    private boolean traceCodeGen = Main.traceCodeGen ;

    private ArrayList<String> instructionCounter;
    private int programCounter;
    private LinkedList<MajorScope> scopes;
    private boolean isProgramScope;
    private int lexicalLevel;
    private HashMap<String, Integer> hash;
    private int num_var;

    /**  
     *  Constructor to initialize code generation
     */
    public CodeGen()
    {
    // YOUR CONSTRUCTOR GOES HERE.
    }

    // Utility procedures used for code generation GO HERE.

    /** 
     *  Additional intialization for gode generation.
     *  Called once at the start of code generation.
     *  May be unnecesary if constructor does everything.
     */

   /** Additional initialization for Code Generation (if required) */
   public void Initialize()
    {
    /********************************************************/
    /* Initialization code for the code generator GOES HERE */
    /* This procedure is called once before codeGeneration  */      
    /*                                                      */
    /********************************************************/
    instructionCounter = new ArrayList<String>();
    programCounter = 0;
    scopes = new LinkedList<MajorScope>();
    isProgramScope = true;
    hash = new HashMap<>();
    num_var = 0;
    lexicalLevel = 0;

    return;
    }

    
    /**  
     *  Perform any requred cleanup at the end of code generation.
     *  Called once at the end of code generation.
     *  @throws MemoryAddressException  from Machine.writeMemory
     *  @throws ExecutionException      from Machine.writeMemory
     */
    public void Finalize()
        throws MemoryAddressException, ExecutionException     // from Machine.writeMemory 
    {
    /********************************************************/
    /* Finalization code for the code generator GOES HERE.  */      
    /*                                                      */
    /* This procedure is called once at the end of code     */
    /* generation                                           */
    /********************************************************/

    //  REPLACE THIS CODE WITH YOUR OWN CODE
    //  THIS CODE generates a single HALT instruction 
        //  as an example.
    Machine.setPC( (short) 0 ) ;        /* where code to be executed begins */
    Machine.setMSP((short)  programCounter );       /* where memory stack begins */
    Machine.setMLP((short) ( Machine.memorySize -1 ) ); 
                    /* limit of stack */
    return;
    }

    /** Procedure to implement code generation based on code generation
     *  action number
     * @param actionNumber  code generation action to perform
     */
    void generateCode( int actionNumber )
    {
    if( traceCodeGen )
        {
        //output the standard trace stream
        Main.traceStream.println("CodeGen: C" +  actionNumber );
        }

    /****************************************************************/
    /*  Code to implement the code generation actions GOES HERE     */
    /*  This dummy code generator just prints the actionNumber      */
    /*  In Assignment 5, you'll implement something more interesting */
        /*                                                               */
        /*  FEEL FREE TO ignore or replace this procedure                */
    /****************************************************************/

        System.out.println("Codegen: C" + actionNumber ); 
    return;
    }

     //  ADDITIONAL FUNCTIONS TO IMPLEMENT CODE GENERATION GO HERE
    public void emitInstructions( String instruction ) {
        String splitstring[] = instruction.split("\\s+");
        for (String token : splitstring) {
            try {
                switch(token) {
                    case "ADDR":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.ADDR );
                        break;
                    case "LOAD":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.LOAD );
                        break;
                    case "STORE":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.STORE );
                        break;
                    case "PUSH":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.PUSH );
                        break;
                    case "PUSHMT":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.PUSHMT );
                        break;
                    case "SETD":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.SETD );
                        break;
                    case "POPN":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.POPN );
                        break;
                    case "POP":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.POP );
                        break;
                    case "DUPN":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.DUPN );
                        break;
                    case "DUP":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.DUP );
                        break;
                    case "BR":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.BR );
                        break;
                    case "BF":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.BF );
                        break;
                    case "NEG":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.NEG );
                        break;
                    case "ADD":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.ADD );
                        break;
                    case "SUB":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.SUB );
                        break;
                    case "MUL":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.MUL);
                        break;
                    case "DIV":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.DIV );
                        break;
                    case "EQ":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.EQ );
                        break;
                    case "LT":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.LT );
                        break;
                    case "OR":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.OR );
                        break;
                    case "SWAP":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.SWAP );
                        break;
                    case "READC":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.READC );
                        break;
                    case "PRINTC":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.PRINTC );
                        break;
                    case "READI":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.READI );
                        break;
                    case "PRINTI":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.PRINTI );
                        break;
                    case "HALT":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.HALT );
                        break;
                    case "TRON":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.TRON );
                        break;
                    case "TROFF":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.TROFF );
                        break;
                    case "ILIMIT":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.ILIMIT );
                        break;
                    case "UNDEFINED":
                        Machine.writeMemory( (short)instructionCounter.size(), Machine.UNDEFINED );
                        break;
                    case "true":
                        Machine.writeMemory( (short)instructionCounter.size(), Machine.MACHINE_TRUE );
                        break;
                    case "false":
                        Machine.writeMemory( (short)instructionCounter.size(), Machine.MACHINE_FALSE );
                    default:
                        Machine.writeMemory( (short)instructionCounter.size() , Short.parseShort(token) );
                        break;

                }
                instructionCounter.add(token);
            } catch (MemoryAddressException e) {
                System.out.println(e);
            } catch (ExecutionException e) {
                System.out.println(e);
            }
        }
    }

    public SymbolTableEntry lookup(String varname, boolean local) {
        if(local) {
            return scopes.getFirst().lookup(varname);
        }

        for(MajorScope scope : scopes) {
            SymbolTableEntry entry = scope.lookup(varname);
            if(entry != null) {
                return entry;
            }
        }
        return null;
    }

    public int lookup_offset(String varname, boolean local) {
        if(local) {
            return scopes.getFirst().lookup(varname).getOff();
        }

        for(MajorScope scope : scopes) {
            SymbolTableEntry entry = scope.lookup(varname);
            if(entry != null) {
                return entry.getOff();
            }
        }
        return 0;
    }

    public void addEntry(String varname, Type type, SymbolKind kind, AST node, int off) {
        if(scopes.isEmpty()) {
            return;
        }
        scopes.getFirst().addEntry(varname, type, kind, node, off);
    }

    public void enterScope(ScopeKind kind, RoutineDecl routine) {
        scopes.addFirst(new MajorScope(kind, routine));
    }

    public void exitScope() {
        num_var = scopes.getFirst().getSize();
        scopes.removeFirst();
    }
    
    public MajorScope getFirstRoutine() {
        for(MajorScope scope : scopes) {
            if(scope.getKind() == ScopeKind.FUNCTION ||
                scope.getKind() == ScopeKind.PROCEDURE) {
                return scope;
            }
        }
        
        return null;
    }

    public Boolean visit(Program stmt) {
        emitInstructions("PUSHMT");
        emitInstructions("SETD 0");

        isProgramScope = false;
        enterScope(ScopeKind.PROGRAM, null);
        this.visit( (Scope) stmt );
        exitScope();
        emitInstructions("PUSH "+num_var);
        emitInstructions("POPN");
        emitInstructions("HALT");
        return true;
    }
    public Boolean visit(AST node) {return true;};
    public <A extends AST> Boolean visit(ASTList<A> list) {
        Boolean bools = true;
        for(A node : list) {
            Boolean z = node.accept(this);
            bools = bools && z;
        }
        return bools;
    }
 
    public Boolean visit(ArrayDeclPart decl) {
        System.out.println("ArrayDeclPart");

        if ( lookup(decl.getName(), true) == null) {
            addEntry(decl.getName(),decl.getType(), SymbolKind.SCALAR, decl, scopes.getFirst().getSize());
        }
        for (int i=0; i<decl.getSize(); i++) {
            emitInstructions("PUSH UNDEFINED");
        }

        return true;
    }
    public Boolean visit(Declaration decl) {
        System.out.println("Declaration");
        return true;
    }
    public Boolean visit(MultiDeclarations decl) {
        System.out.println("MultiDeclarations");
        ASTList<DeclarationPart> parts = decl.getParts();

        for(DeclarationPart part : parts) {
            if(part instanceof ScalarDeclPart) {
                ScalarDecl scalarDecl = new ScalarDecl(part.getName(), decl.getType());
                scalarDecl.setLocation(part.getLine() - 1, part.getCol());

                Boolean scalar = this.visit(scalarDecl);
            } else {
                ArrayDeclPart arrayDecl = (ArrayDeclPart) part;
                arrayDecl.setType(decl.getType());

                Boolean array = this.visit(arrayDecl);
            }
        }

        return true;
    }
    public Boolean visit(RoutineDecl decl) {
        System.out.println("RoutineDecl");
        int pc = instructionCounter.size()+1;
        emitInstructions("PUSH 0");
        emitInstructions("BR");


        // if ( lookup(decl.getName(), true) == null) {
        //     addEntry(decl.getName(),decl.getType(), SymbolKind.PROCEDURE, decl);
        // }
        // emitInstructions("PUSH 0");

        hash.put(decl.getName(), instructionCounter.size());
        //emitInstructions("PUSH "+(pc+5));

        ScopeKind routineKind = decl.getType() == null ? ScopeKind.PROCEDURE : ScopeKind.FUNCTION;
        enterScope(routineKind, decl);

        emitInstructions("PUSHMT");
        lexicalLevel++;
        emitInstructions("SETD "+lexicalLevel);

        ASTList<ScalarDecl> parameters = decl.getParameters();
        int i = 0;
        while (!parameters.isEmpty()) {
            ScalarDecl s = parameters.pollFirst();
            if ( lookup(s.getName(), true) == null) {
                i++;
                addEntry(s.getName(), s.getType(), SymbolKind.SCALAR, s, (-i));
            }
        }

        this.visit(decl.getBody());
        // // pop all local variables
        // emitInstructions("PUSH "+num_var);
        // emitInstructions("POPN");

        // pop all parameters
        emitInstructions("PUSH "+i);
        emitInstructions("POPN");
        exitScope();
        lexicalLevel--;

        emitInstructions("BR");

        try {
            Machine.writeMemory( (short)pc, (short)instructionCounter.size());
        } catch (MemoryAddressException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            System.out.println(e);
        }

        return true;
    }
    public Boolean visit(ScalarDecl decl) {
        System.out.println("ScalarDecl");
        if ( lookup(decl.getName(), true) == null) {
            addEntry(decl.getName(),decl.getType(), SymbolKind.SCALAR, decl, scopes.getFirst().getSize());
        }
        emitInstructions("PUSH UNDEFINED");
        return true;
    }

    public Boolean visit(AnonFuncExpn expn) {
        System.out.println("AnonFuncExpn");
        return true;
    }
    public Boolean visit(ArithExpn expn) {
        System.out.println("ArithExpn");
        return true;
    }
    public Boolean visit(BinaryExpn expn) {
        System.out.println("BinaryExpn");
        return true;
    }
    public Boolean visit(BoolConstExpn expn) {
        System.out.println("BoolConstExpn");
        emitInstructions("PUSH "+expn.toString());
        return true;
    }
    public Boolean visit(BoolExpn expn) {
        System.out.println("BoolExpn");
        return true;
    }
    public Boolean visit(CompareExpn expn) {
        System.out.println("CompareExpn");
        return true;
    }
    public Boolean visit(ConstExpn expn) {
        System.out.println("ConstExpn");
        return true;
    }
    public Boolean visit(EqualsExpn expn) {
        System.out.println("EqualsExpn");
        return true;
    }
    public Boolean visit(FunctionCallExpn expn) {
        System.out.println("FunctionCallExpn");
        return true;
    }
    public Boolean visit(IdentExpn expn) {
        System.out.println("IdentExpn");
        emitInstructions("ADDR "+lexicalLevel+" "+lookup_offset(expn.toString(), false));
        return true;
    }
    public Boolean visit(IntConstExpn expn) {
        System.out.println("IntConstExpn");
        emitInstructions("PUSH "+expn.toString());

        return true;
    }
    public Boolean visit(NotExpn expn) {
        System.out.println("NotExpn");
        return true;
    }

    public Boolean visit(SkipConstExpn expn){
        System.out.println("SkipConstExpn");
        return true;
    }
    public Boolean visit(SubsExpn expn){
        System.out.println("SubsExpn");

        SymbolTableEntry array = lookup(expn.getVariable(), true);
        if (array.getNode() instanceof ArrayDeclPart) {
            int lb1 = ((ArrayDeclPart)array.getNode()).getLowerBoundary1();
            int ub1 = ((ArrayDeclPart)array.getNode()).getUpperBoundary1();
            int sub1 = Integer.parseInt(expn.getSubscript1().toString());
            if (((ArrayDeclPart)array.getNode()).isTwoDimensional() == true) {
                int lb2 = ((ArrayDeclPart)array.getNode()).getLowerBoundary2();
                int sub2 = Integer.parseInt(expn.getSubscript2().toString());
                int off = (ub1-lb1+1)*(sub1-lb1)+(sub2-lb2);
                emitInstructions("ADDR "+lexicalLevel+" "+off);
            } else {
                int off = sub1 - lb1;
                emitInstructions("ADDR "+lexicalLevel+" "+off);
            }
        }
        return true;
    }
    public Boolean visit(TextConstExpn expn){
        System.out.println("TextConstExpn");
        return true;
    }
    public Boolean visit(UnaryExpn expn){
        System.out.println("UnaryExpn");
        return true;
    }
    public Boolean visit(UnaryMinusExpn expn){
        System.out.println("UnaryMinusExpn");
        return true;
    }

    public Boolean visit(AssignStmt stmt){

        System.out.println("AssignStmt");
        stmt.getLval().accept(this);
        stmt.getRval().accept(this);
        emitInstructions("STORE");

        return true;
    }
    public Boolean visit(ExitStmt stmt){
        System.out.println("ExitStmt");
        return true;
    }
    public Boolean visit(GetStmt stmt){
        System.out.println("GetStmt");
        return true;
    }
    public Boolean visit(IfStmt stmt){
        System.out.println("IfStmt");
        return true;
    }
    public Boolean visit(LoopingStmt stmt){
        System.out.println("LoopingStmt");
        return true;
    }
    public Boolean visit(LoopStmt stmt){
        System.out.println("LoopStmt");
        return true;
    }
    public Boolean visit(ProcedureCallStmt stmt){
        System.out.println("ProcedureCallStmt");
        emitInstructions("PUSH "+(instructionCounter.size()+5+stmt.getArguments().size()*4));
        while (!stmt.getArguments().isEmpty()) {
            Expn e = stmt.getArguments().pollLast();
            this.visit(e);
            emitInstructions("ADDR "+lexicalLevel+" "+lookup_offset(e.toString(), true));
            emitInstructions("LOAD");
        }
        emitInstructions("PUSH "+hash.get(stmt.getName()));
        emitInstructions("BR");

        return true;
    }
    public Boolean visit(PutStmt stmt){

        System.out.println("PutStmt");

        String out = stmt.getOutputs().toString();
        if (out.charAt(0) == '\"') {
            for (char c : out.toCharArray()) {
                if (c != '\"') {
                    emitInstructions("PUSH "+(int)c);
                    emitInstructions("PRINTC");
                }
            }
        } else {
            for (Printable p : stmt.getOutputs()) {
                p.accept(this);
                emitInstructions("LOAD");
                emitInstructions("PRINTI");
            }
        }
        return true;
    }
    public Boolean visit(ReturnStmt stmt){
        System.out.println("ReturnStmt");
        return true;
    }
    public Boolean visit(Scope stmt){
        System.out.println("Scope");
        
        ASTList<Stmt> body = stmt.getBody();
        boolean result = true;
        if(body != null) {
            result = body.accept(this);
        }

        return true;
    }
    public Boolean visit(Stmt stmt){
        System.out.println("Stmt");
        return true;
    }
    public Boolean visit(WhileDoStmt stmt){
        System.out.println("WhileDoStmt");
        return true;
    }

    public Boolean visit(BooleanType type){
        System.out.println("BooleanType");
        return true;
    }
    public Boolean visit(IntegerType type){
        System.out.println("IntegerType");
        return true;
    }


}
