package parser.parse;

import lexer.Token;
import lexer.TokenType;
import parser.ast.*;

import java.util.ArrayList;

public class CuteParser {
	private ArrayList<Token> token_list;
	private static Node END_OF_LIST = new Node(){};

	public CuteParser() {
	}

	public void setTokenList(ArrayList<Token> token_list) {
		this.token_list = token_list;
	}

	private Token getNextToken() {
		if(token_list.isEmpty())
			return null;
		Token t = token_list.get(0);
		token_list.remove(0);
		return t;
	}

	public Node parseExpr() {
		Token t = getNextToken();

		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type();
		String tLexeme = t.lexme();

		switch (tType) {
		case ID:
			return new IdNode(tLexeme);
		case INT:
			return new IntNode(tLexeme);
		case STRING:
			return new StringNode(tLexeme);
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			return new BinaryOpNode(tType);
		case ATOM_Q:
		case CAR:
		case CDR:
		case COND:
		case CONS:
		case DEFINE:
		case EQ_Q:
		case LAMBDA:
		case NOT:
		case NULL_Q:
		case IMPORT:
        case EXIT:
			return new FunctionNode(tType);
		case FALSE:
			return BooleanNode.FALSE_NODE;
		case TRUE:
			return BooleanNode.TRUE_NODE;
		case L_PAREN:
			return parseExprList();
		case R_PAREN:
			return END_OF_LIST;
		case APOSTROPHE:
			return ListNode.cons(new QuoteNode(parseQuotedExpr()), ListNode.EMPTYLIST);
		case QUOTE:
			return new QuoteNode(parseQuotedExpr());
		default:
			System.out.println("parseExpr Error!");
			return null;
		}
	}

	private ListNode parseExprList() {
		Node head = parseExpr();
		if(head == null) {
			System.out.println("parseList Error!");
			return null;
		}
		if(head == END_OF_LIST) // if next token is RPAREN
			return ListNode.EMPTYLIST;

		ListNode tail = parseExprList();
		if(tail == null) {
			System.out.println("parseList Error!");
			return null;
		}

		return ListNode.cons(head, tail);
	}

	private Node parseQuotedExpr() {
		Token t = getNextToken();

		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type();
		String tLexeme = t.lexme();

		switch (tType) {
			case ID:
			case DIV:
			case EQ:
			case MINUS:
			case GT:
			case PLUS:
			case TIMES:
			case LT:
			case ATOM_Q:
			case CAR:
			case CDR:
			case COND:
			case CONS:
			case DEFINE:
			case EQ_Q:
			case LAMBDA:
			case NOT:
			case NULL_Q:
            case IMPORT:
            case EXIT:
				return new IdNode(tLexeme);
			case INT:
				return new IntNode(tLexeme);
			case FALSE:
				return BooleanNode.FALSE_NODE;
			case TRUE:
				return BooleanNode.TRUE_NODE;
			case L_PAREN:
				return parseQuotedList();
			case R_PAREN:
				return END_OF_LIST;
			case APOSTROPHE:
				return ListNode.cons(new QuoteNode(parseQuotedExpr()), ListNode.EMPTYLIST);
			case QUOTE:
				return new QuoteNode(parseQuotedExpr());
			default:
				System.out.println("parseQuotedExpr Error!");
				return null;
		}
	}

	private ListNode parseQuotedList() {
		Node head = parseQuotedExpr();
		if(head == null) {
			System.out.println("parseQuotedList Error!");
			return null;
		}
		if(head == END_OF_LIST) // if next token is RPAREN
			return ListNode.EMPTYLIST;

		ListNode tail = parseQuotedList();
		if(tail == null) {
			System.out.println("parseList Error!");
			return null;
		}

		return ListNode.cons(head, tail);
	}
}
