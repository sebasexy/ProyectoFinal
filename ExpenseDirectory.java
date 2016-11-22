package FinalProject;
import java.util.Iterator;

public class ExpenseDirectory<InvoicesNumber, ItemT, Expenses> implements ExpenseDictionaryInterface<InvoicesNumber, ItemT, Expenses> {
	private class AvlTreeExpenses<Tree extends Comparable<? super Tree>>{
		private AvlNode<Tree> root;
		private static final int ALLOWED_IMBALANCE = 1;
	    public AvlTreeExpenses( ){this.root = null;}
	    private class AvlNode<Tree>{
	        Tree element;
	        Expense value;
	        AvlNode<Tree> left;       
	        AvlNode<Tree> right;   
	        int height;
	        AvlNode( Tree theElement, AvlNode<Tree> lt, AvlNode<Tree> rt , Expense value)
	        {
	            element  = theElement;
	            left     = lt;
	            right    = rt;
	            height   = 0;
	        }
	        /**
	         * imprima el elemento y la altura de ese nodo en el siguiente formato [elem – height]
	         */
	        public String toString (){
	        	return ""+"["+element+"-"+(height+1)+"]";
	        }
	    }
	    public void insert( Tree x , Expense value){this.root = insert( x, this.root, value );}
	    private AvlNode<Tree> insert( Tree x, AvlNode<Tree> t, Expense value){
	        if( t == null ){return new AvlNode<>( x, null, null, value );}
	        int compareResult = x.compareTo( t.element );
	        if( compareResult < 0 ){t.left = insert( x, t.left , value);}
	        else if( compareResult > 0 ){t.right = insert( x, t.right , value);}
	        else;
	        return balance( t );
	    }
	    public String remove( Tree x ){
	    	if(contains(x)){
	    		root = remove( x, root );
	    		return "Element errased";
	    	}else{return "Element doesnt exist";}
	    }
	    private AvlNode<Tree> remove( Tree x, AvlNode<Tree> t )
	    {
	        if( t == null ){return t;}
	        int compareResult = x.compareTo( t.element );
	        if( compareResult < 0 ){t.left = remove( x, t.left );}
	        else if( compareResult > 0 ){t.right = remove( x, t.right );}
	        else if( t.left != null && t.right != null ){
	            t.element = findMin( t.right ).element;
	            t.right = remove( t.element, t.right );
	        }
	        else{t = ( t.left != null ) ? t.left : t.right;}
	        return balance( t );
	    }
	    public Tree findMin( ) throws Exception{
	        if( isEmpty( ) ){throw new Exception( );}
	        return findMin( root ).element;
	    }
	    private AvlNode<Tree> findMin( AvlNode<Tree> t ){
	        if( t == null ){ return t;}
	        while( t.left != null ){t = t.left;}
	        return t;
	    }
	    public Tree findMax( ) throws Exception{
	        if( isEmpty( ) ){ throw new Exception( );}
	        return findMax( root ).element;
	    }
	    private AvlNode<Tree> findMax( AvlNode<Tree> t ){
	        if( t == null ){return t;}
	        while( t.right != null ){ t = t.right;}
	        return t;
	    }
	    public boolean contains( Tree x ){return contains( x, root );}
	    private boolean contains( Tree x, AvlNode<Tree> t ){
	        while( t != null ){
	            int compareResult = x.compareTo( t.element );
	            if( compareResult < 0 ){t = t.left;}
	            else if( compareResult > 0 ){t = t.right;}
	            else{return true;}   
	        }
	        return false;   
	    }
	    public void makeEmpty( ){root = null;}
	    public boolean isEmpty( ){return root == null;}
	    public void printTree( ){printTree( root );}
	    private void printTree( AvlNode<Tree> t ){
	    	if( t != null){
	    		printTree(t.left);
	    		System.out.print(t);
	    		printTree(t.right);
	        }
	    }
	    private int height( AvlNode<Tree> t ){
	        return t == null ? -1: t.height;
	    }
	    public Expense getValue(Tree key){
			AvlNode<Tree> node = this.root;
			int result;
			while(node != null){
				result = key.compareTo(node.element);
				if(result == 0){return node.value;}
				else if(result < 0){node = node.left;}
				else if(result > 0){node = node.right;}
			}
			return null;
		}
	    private AvlNode<Tree> balance( AvlNode<Tree> t ){
	        if( t == null ){return t;}
	        if( height( t.left ) - height( t.right ) > ALLOWED_IMBALANCE )
	            if( height( t.left.left ) >= height( t.left.right ) ){t = rotateWithLeftChild( t );}
	            else{ t = doubleWithLeftChild( t );}
	        else if( height( t.right ) - height( t.left ) > ALLOWED_IMBALANCE )
	            if( height( t.right.right ) >= height( t.right.left ) ){t = rotateWithRightChild( t );}
	            else{t = doubleWithRightChild( t );}
	        t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
	        return t;
	    }
	    public void checkBalance( ){checkBalance( root );}
	    private int checkBalance( AvlNode<Tree> t ){
	        if( t == null ){return -1;}
	        if( t != null ){
	            int hl = checkBalance( t.left );
	            int hr = checkBalance( t.right );
	            if( Math.abs( height( t.left ) - height( t.right ) ) > 1 ||
	                    height( t.left ) != hl || height( t.right ) != hr )
	                System.out.println( "OOPS!!" );
	        }
	        return height( t );
	    }
	    private AvlNode<Tree> rotateWithLeftChild( AvlNode<Tree> k2 ){
	        AvlNode<Tree> k1 = k2.left;
	        k2.left = k1.right;
	        k1.right = k2;
	        k2.height = Math.max( height( k2.left ), height( k2.right ) ) + 1;
	        k1.height = Math.max( height( k1.left ), k2.height ) + 1;
	        return k1;
	    }
	    private AvlNode<Tree> rotateWithRightChild( AvlNode<Tree> k1 ){
	        AvlNode<Tree> k2 = k1.right;
	        k1.right = k2.left;
	        k2.left = k1;
	        k1.height = Math.max( height( k1.left ), height( k1.right ) ) + 1;
	        k2.height = Math.max( height( k2.right ), k1.height ) + 1;
	        return k2;
	    }
	    private AvlNode<Tree> doubleWithLeftChild( AvlNode<Tree> k3 ){
	        k3.left = rotateWithRightChild( k3.left );
	        return rotateWithLeftChild( k3 );
	    }
	    private AvlNode<Tree> doubleWithRightChild( AvlNode<Tree> k1 ){
	        k1.right = rotateWithLeftChild( k1.right );
	        return rotateWithRightChild( k1 );
	    }
	}
	@Override
	public ItemT add(InvoicesNumber k, ItemT Item, Expenses Item2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemT remove(InvoicesNumber k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemT getValue(InvoicesNumber k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(InvoicesNumber k) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<InvoicesNumber> getKeyIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<ItemT> getValueIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}
