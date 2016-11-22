package FinalProject;
import java.util.Iterator;

public interface InvoicesDictionaryInterface<Key, Value1, Value2> {
	public Value1 add(Key k, Value1 Item, Value2 Item2);
	public Value1 remove(Key k);
	public Value1 getValue(Key k);
	public boolean contains(Key k);
	public Iterator<Key> getKeyIterator();
	public Iterator<Value1> getValueIterator();
	public boolean  isEmpty();
	public int getSize();
	public void clear();
}
