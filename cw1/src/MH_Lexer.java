
// File:   MH_Lexer.java

// Java template file for lexer component of Informatics 2A Assignment 1.
// Concerns lexical classes and lexer for the language MH (`Micro-Haskell').


import java.io.* ;

class MH_Lexer extends GenLexer implements LEX_TOKEN_STREAM {

static class VarAcceptor extends Acceptor implements DFA {
	public String lexClass() {
		return "VAR";
	}
	public int numberOfStates() {
		return 3;
	}
	public int next (int state, char input) {
		switch(state) {
		case 0: if(CharTypes.isSmall(input)) return 1; else return 2;
		case 1: if(CharTypes.isSmall(input) || CharTypes.isLarge(input) 
				|| CharTypes.isDigit(input) || input == '\'') return 1; else return 2;
			default: return 2;
		}
	}
	public boolean accepting (int state) {
		return (state == 1);
	}
	public int dead () {
		return 2;
	}
}

static class NumAcceptor extends Acceptor implements DFA {
	public String lexClass() {
		return "NUM";
	}
	public int numberOfStates() {
		return 4;
	}
	public int next (int state, char input) {
		switch(state) {
		case 0: if (input == '0') return 1;else if (input != '0' && CharTypes.isDigit(input)) return 2;else return 3;
		case 1: return 3;
		case 2: if (CharTypes.isDigit(input)) return 2;else return 3;
			default: return 3;
		}
	}
	public boolean accepting (int state) {
		return (state == 1 || state == 2);
	}
	public int dead () {
		return 3;
	}
}

static class BooleanAcceptor extends Acceptor implements DFA {
	public String lexClass() {
		return "BOOLEAN";
	}
	public int numberOfStates() {
		return 9;
	}
	public int next (int state, char input) {
		switch(state) {
		case 0: if (input == 'T')return 1;else if (input == 'F') return 5;else return 8;
		case 1: if (input == 'r')return 2;else return 8;
		case 2: if (input == 'u')return 3;else return 8;
		case 3: if (input == 'e')return 4;else return 8;
		case 5: if (input == 'a')return 6;else return 8;
		case 6: if (input == 'l')return 7;else return 8;
		case 7: if (input == 's')return 3;else return 8;
			default: return 8;
		}
	}
	public boolean accepting (int state) {
		return state == 4;
	}
	public int dead () {
		return 8;
	}
}

static class SymAcceptor extends Acceptor implements DFA {
	public String lexClass() {
		return "SYM";
	}
	public int numberOfStates() {
		return 3;
	}
	public int next (int state, char input) {
		switch(state) {
		case 0: if (CharTypes.isSymbolic(input)) return 1;else return 2;
		case 1: if (CharTypes.isSymbolic(input)) return 1;else return 2;
			default: return 2;
		}
	}
	public boolean accepting (int state) {
		return state == 1;
	}
	public int dead () {
		return 2;
	}
}

static class WhitespaceAcceptor extends Acceptor implements DFA {
	public String lexClass() {
		return "";
	}
	public int numberOfStates() {
		return 3;
	}
	public int next (int state, char input) {
		switch(state) {
		case 0: if (CharTypes.isWhitespace(input))return 1;else return 2;
		case 1: if (CharTypes.isWhitespace(input))return 1;else return 2;
			default: return 2;
		}
	}
	public boolean accepting (int state) {
		return state == 1;
	}
	public int dead () {
		return 2;
	}
}

static class CommentAcceptor extends Acceptor implements DFA {
	public String lexClass() {
		return "";
	}
	public int numberOfStates() {
		return 5;
	}
	public int next (int state, char input) {
		switch(state) {
		case 0: if (input == '-')return 1;else return 4;
		case 1: if (input == '-')return 2;else return 4;
		case 2: if (input == '-')return 2;else if(!CharTypes.isSymbolic(input)&&!CharTypes.isNewline(input))return 3;else return 4;
		case 3: if (!CharTypes.isNewline(input))return 3;else return 4;
			default: return 4;
		}
	}
	public boolean accepting (int state) {
		return (state == 2 || state == 3);
	}
	public int dead () {
		return 4;
	}
}

static class TokAcceptor extends Acceptor implements DFA {

    String tok ;
    int tokLen ;
    TokAcceptor (String tok) {this.tok = tok ; tokLen = tok.length() ;}

	public String lexClass() {
		return tok;
	}
	public int numberOfStates() {
		return tokLen + 2;
	}
	public int next (int state, char input) {
		if (tokLen == 7) {
			switch(state) {
			case 0: if (input == 'I')return 1;else return 8;
			case 1: if (input == 'n')return 2;else return 8;
			case 2: if (input == 't')return 3;else return 8;
			case 3: if (input == 'e')return 4;else return 8;
			case 4: if (input == 'g')return 5;else return 8;
			case 5: if (input == 'e')return 6;else return 8;
			case 6: if (input == 'r')return 7;else return 8;
				default: return 8;
			}
			
		}
		if (tokLen == 4) {
			if(tok == "Bool") {
				switch(state) {
				case 0: if (input == 'B')return 1;else return 5; 
				case 1: if (input == 'o')return 2;else return 5; 
				case 2: if (input == 'o')return 3;else return 5; 
				case 3: if (input == 'l')return 4;else return 5; 
					default: return 5;
				}
			}
			if(tok == "then") {
				switch(state) {
				case 0: if (input == 't')return 1;else return 5; 
				case 1: if (input == 'h')return 2;else return 5; 
				case 2: if (input == 'e')return 3;else return 5; 
				case 3: if (input == 'n')return 4;else return 5; 
					default: return 5;
				}
			}
			if(tok == "else") {
				switch(state) {
				case 0: if (input == 'e')return 1;else return 5; 
				case 1: if (input == 'l')return 2;else return 5; 
				case 2: if (input == 's')return 3;else return 5; 
				case 3: if (input == 'e')return 4;else return 5; 
					default: return 5;
				}
			}
		}
		if (tok == "if") {
			switch(state) {
			case 0: if (input == 'i')return 1;else return 3; 
			case 1: if (input == 'f')return 2;else return 3; 
				default: return 3;
			}
		}
		if (tok == "(" ){
			switch(state) {
			case 0: if (input == '(') return 1;else return 2;
				default: return 2;
			}
		}
		if (tok == ")") {
			switch(state) {
			case 0: if ( input == ')') return 1; else return 2;
				default: return 2;
			}
		}
		else {
			switch(state) {
			case 0: if (input == ';') return 1;else return 2;
				default: return 2;
			}
		}
		
			
	}
	public boolean accepting (int state) {
		return (state == tokLen);
	}
	public int dead () {
		return tokLen + 1;
	}
}

    // add definitions of MH_acceptors here
	static DFA varAcc = new VarAcceptor() ;
	static DFA numAcc = new NumAcceptor() ;
	static DFA booleanAcc = new BooleanAcceptor() ;
	static DFA symAcc = new SymAcceptor() ;
	static DFA whitespaceAcc = new WhitespaceAcceptor() ;
	static DFA commentAcc = new CommentAcceptor() ;
	static DFA tokAccInteger = new TokAcceptor("Integer") ;
	static DFA tokAccBool = new TokAcceptor("Bool") ;
	static DFA tokAccIf = new TokAcceptor("if") ;
	static DFA tokAccThen = new TokAcceptor("then") ;
	static DFA tokAccElse = new TokAcceptor("else") ;
	static DFA tokAccLB = new TokAcceptor("(") ;
	static DFA tokAccRB = new TokAcceptor(")") ;
	static DFA tokAccSemicolon = new TokAcceptor(";") ;
	static DFA[] MH_acceptors = 
			new DFA[] {tokAccInteger
					,tokAccBool,tokAccIf,tokAccThen,tokAccElse,tokAccRB,tokAccLB,tokAccSemicolon, booleanAcc ,varAcc,numAcc, commentAcc, symAcc,whitespaceAcc } ;
    MH_Lexer (Reader reader) {
	super(reader,MH_acceptors) ;
    }
    

}

