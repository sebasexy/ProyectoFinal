package ProyectoFinal;
import java.util.Iterator;
public interface DictionaryInterface2<Key, Value, Value2> {

	public Value add(Key k, Value Item, Value2 Item2);
	public Value remove(Key k);
	public Value getValue(Key k);
	public boolean contains(Key k);
	public Iterator<Key> getKeyIterator();
	public Iterator<Value> getValueIterator();
	public boolean  isEmpty();
	public int getSize();
	public void clear();
}
