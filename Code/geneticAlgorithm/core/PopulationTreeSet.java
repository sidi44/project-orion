package geneticAlgorithm.core;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public class PopulationTreeSet<T extends Individual<?>> extends TreeSet<T> 
																										 implements Population<T> 
{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public boolean insert(T ind) {
		return add(ind);
	}

	@Override
	public T removeMax() {
		return pollLast();
	}

	@Override
	public T removeMin() {
		return pollFirst();
	}

	@Override
	public void removeMaxN(int n) {
		for (int i=0; i<n; ++i) {
			removeMax();
		}		
	}

	@Override
	public void removeMinN(int n) {
		for (int i=0; i<n; ++i) {
			removeMin();
		}
		
	}

	@Override
	public T removeNthMax(int n) {
		Iterator<T> iter = descendingIterator();
		
		for (int i=0; i<n; ++i) {
			if (!iter.hasNext()) {
				throw new IllegalArgumentException("n is larger than size of population.");
			} else {
				iter.next();
			}
		}
		return iter.next();
	}

	@Override
	public T removeNthMin(int n) {
		Iterator<T> iter = iterator();
		
		for (int i=0; i<n; ++i) {
			if (!iter.hasNext()) {
				throw new IllegalArgumentException("n is larger than size of population.");
			} else {
				iter.next();
			}
		}
		return iter.next();
		
	}

	@Override
	public T getMax() {
		return last();
	}

	@Override
	public T getMin() {
		return first();
	}

	@Override
	public T getNthMax(int n) {
		Iterator<T> iter = descendingIterator();
		
		for (int i=0; i<n; ++i) {
			if (!iter.hasNext()) {
				throw new IllegalArgumentException("n is larger than size of population.");
			} else {
				iter.next();
			}
		}
		return iter.next();
	}

	@Override
	public T getNthMin(int n) {
		Iterator<T> iter = iterator();
		
		for (int i=0; i<n; ++i) {
			if (!iter.hasNext()) {
				throw new IllegalArgumentException("n is larger than size of population.");
			} else {
				iter.next();
			}
		}
		return iter.next();
	}

}
