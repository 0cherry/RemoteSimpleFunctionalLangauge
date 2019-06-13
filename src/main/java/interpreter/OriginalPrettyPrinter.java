package interpreter;
import parser.ast.ListNode;
import parser.ast.Node;
import parser.ast.QuoteNode;

import java.io.PrintStream;
public class OriginalPrettyPrinter {
    PrintStream ps;
    
    public static OriginalPrettyPrinter getPrinter(PrintStream ps) {
        return new OriginalPrettyPrinter(ps);
    }

    private OriginalPrettyPrinter(PrintStream ps) {
        this.ps = ps;
    }
    
    private void printNode(ListNode listNode) {     
        if (listNode.isEmpty()) {
            return;
        }
        ps.print(" ");
        printNode(listNode.car());
        ps.print(" ");
        printNode(listNode.cdr());
    }
    
    private void printNode(QuoteNode quoteNode) {
        ps.print("'");
        printNode(quoteNode.nodeInside());
    }
    
    private void printNode(Node node) {
        if (node instanceof ListNode) {
            if(((ListNode) node).car() instanceof QuoteNode)
                printNode((QuoteNode) ((ListNode) node).car());
            else {
                ps.print("(");
                printNode((ListNode) node);
                ps.print(")");
            }
        } else if (node instanceof QuoteNode) {
            printNode((QuoteNode)node);
        } else {
            ps.print(node);
        }
    }
    
    public void prettyPrint(Node node){
        printNode(node);
        ps.print("\n");
    }
}