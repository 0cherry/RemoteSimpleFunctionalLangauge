package parser.ast;

public interface ListNode extends Node { 

	Node car();
	ListNode cdr();
	int size();
	Node last();
	boolean isEmpty();
	
	static ListNode cons(Node car, ListNode cdr){
		return new ListNode(){

			@Override
			public Node car() {
				return car;
			}

			@Override
			public ListNode cdr() {
//				if(car != null && cdr == EMPTYLIST)
//					return ENDLIST;
				return cdr;
			}

			@Override
			public int size() {
				if(cdr.isEmpty())
					return 1;
				else
					return 1 + cdr().size();
			}

			@Override
			public Node last() {
				if(cdr.isEmpty())
					return car();
				else
					return cdr().last();
			}

			@Override
			public boolean isEmpty() {
				return car == null && cdr == null;
			}

			@Override
			public boolean equals(Object o) {
				return o == this || o instanceof ListNode && car().equals(((ListNode) o).car()) && cdr().equals(((ListNode) o).cdr());
			}

			@Override
			public String toString() {
				if(car() instanceof QuoteNode)
					return car().toString();
				else
					return "( " + car().toString() + " " + cdr().toString() +" )";
			}
		};
	}
	
	static ListNode EMPTYLIST = new ListNode(){

		@Override
		public Node car() {
			return null;
		}

		@Override
		public ListNode cdr() {
			return null;
		}

		@Override
		public int size() { return 0; }

		@Override
		public Node last() { return null; }

		@Override
		public boolean isEmpty() { return true; }

		@Override
		public String toString() {
			return "(empty)";
		}
	};
	
	
	static ListNode ENDLIST = new ListNode(){ 

		@Override
		public Node car() {
			return null;
		}

		@Override
		public ListNode cdr() {
			return null;
		}

		@Override
		public int size() { return 0; }

		@Override
		public Node last() { return null; }

		@Override
		public boolean isEmpty() { return true; }

		@Override
		public String toString() {
			return "(end)";
		}
	};
	
}
