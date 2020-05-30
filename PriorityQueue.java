import java.util.*;
import java.util.stream.*;
import java.util.Collections.*;
import java.io.Serializable;
/**
 * Generic PriorityQueu<E>, ~ data structure
 * that has a list<E> "queue" and a
 * a Comparator<E>, i.e., of the same type E, 
 * that will determine the ordering of its 
 * elements. 
 */
public class PriorityQueue<E> implements Iterable<E>, Serializable {
    private Comparator<E> comparator;
    private List<E> queue;
    private Integer size;
    
    public PriorityQueue(int size, Comparator<E> comparator) {
	this.comparator = comparator;
	queue = (ArrayList<E>)(new ArrayList<Object>(size));
	size = queue.size();
    }
    /**
     * get the head of the queue
     */
    public E getMin() {
        if (queue.size() == 0 || queue == null)
	    //throw new NoSuchElementException();
	    return null;
	else
	    return queue.get(0);
    }
    /**
     * get the tail of the queue
     */
    public E getMax() {
	if (queue.size() == 0 || queue == null)
	    //throw new NoSuchElementException();
	    return null;
	else
	    return queue.get(queue.size()-1);
    }
    /**
     * returns true if a given element is present 
     * in the queue.
     */
    public boolean contains(E element) {
	return queue.stream().filter(e -> e.equals(element))
	    .findFirst()
	    .isPresent();
    }
    /**
     * returns the index of a given element in 
     * that is present in the queue.
     */
    public Integer indexOf(E element) {
        if (contains(element))
	    return queue.indexOf(element);
	return -1;
    }
    /**
     * inserts a new element in the queue and reorders it
     * according to its comparator compare method implementation.
     */
    public void insert(E element) {
	if (!queue.contains(element)) {
	    queue.add(element);
	    Collections.sort(queue, comparator);
	}
    }
    /**
     * removes the head of the queue.
     */
    public void remove() {
	if ((queue.size() != 0)) {
	    queue.remove(0);
	    Collections.sort(queue, comparator);
	} else
	    throw new NoSuchElementException();
    }
    /**
     * removes a given element from the queue and reorders it
     * according to its comparator compare method implementation.
     */
    public void remove(E element) {
	if (indexOf(element) <= queue.size() - 1) {
	    queue.remove(element);
	    Collections.sort(queue, comparator);
	}
    }
    /**
     * returns the head of the queue
     * if the queue hasnt been initialised
     * throws a NoSuchElementException
     */
    public E peek() {
	if (queue == null)
	    throw new NoSuchElementException();
	else
	    return getMin();
    }
    /**
     * returns true if the queue is empty
     */
    public boolean isEmpty() {
	if (queue.size() == 0)
	    return true;
	else
	    return false;
    }
    /**
     * removes all elements from the queue
     */
    public void clear() {
	queue.clear();
    }
    /**
     * returns the size of the queue
     */
    public Integer getSize() {
	return queue.size();
    }
    /**
     * returns the element index at a given position.
     */
    public E get(int index) {
	return queue.get(index);
    }
    /**
     * returns a reference to the actual queue
     */
    public List<E> getPriorityQueue() {
	return queue;
    }
    /* Implementation of the iterator() method as 
     * present in the Iterable<E> interface. 
     * A reference to an iterator is returned.  
     */
    @Override
    public Iterator<E> iterator() {
	/**
	 * Anonymous implementation of the Iterator<E>
	 * interface to define the actual object of
	 * type Iterator<E>.
	 *
	 *
	 * This iterator has a field, "currentPosition"
	 * that stores where in the queue it is at. 
	 *
	 * hasNext() and next() are overriden so as to
	 * adjust them to the intended behaviour of the
	 * priority queue.	 
	 */
	Iterator<E> pqIterator = new Iterator<E>() {
		private int currentPosition = 0;
		/**
		 * returns true if the currentposition is
		 * less than the last index of the queue.
		 */
		public boolean hasNext() {
		    if(currentPosition < queue.size() - 1) 
			return true;
		    else
			return false;
		}
		/**
		 * returns the next immediate value if there are still
		 * values to iterate over. 
		 */
		public E next() {
		    if (hasNext()) {
			E elem = queue.get(currentPosition);
			currentPosition++;
			return elem;
		    } else {
			throw new NoSuchElementException();
		    }
		}
		public void remove() {
		    throw new UnsupportedOperationException();
		}
	    };
	return pqIterator;
    }
}
