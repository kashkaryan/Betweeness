import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @param <V>   {@inheritDoc}
 * @param <Key> {@inheritDoc}
 */

public class BinaryMinHeapImpl<Key extends Comparable<Key>, V> implements BinaryMinHeap<Key, V> {
    
    private ArrayList<Entry<Key, V>> heap = new ArrayList<Entry<Key, V>>();
    private Map<V, Integer> pairs = new HashMap<V, Integer>(); //Value is key, index is value
    
    /**
     * {@inheritDoc}
     * Runtime - O(1)
     */
    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public boolean isEmpty() {
        return (heap.size() == 0);
    }

    /**
     * {@inheritDoc}
     * Runtime - O(1)
     */
    @Override
    public boolean containsValue(V value) {
        if (size() == 0) {
            return false;
        } else {
            return pairs.containsKey(value);
        }
    }
    
    //for testing pairs
    Map<V, Integer> getPairs() {
        return pairs;
    }
    
    //if the head, then parent is itself, otherwise get from heap
    Entry<Key, V> parent(int index) {
        if (index == 0) {
            return heap.get(index);
        } else {
            return heap.get((index - 1)  / 2);
        }
    }
    
    //if index is out of range of being able to have a left child, return null
    Entry<Key, V> leftChildIndex(int ind) {
        if (ind > this.size() - 1 || ind < 0 || (1 + (2 * ind)) > this.size() - 1) {
            return null;
        }
        return heap.get(ind * 2 + 1);
    }
    
    //if index is out of range of having right child, return null
    Entry<Key, V> rightChildIndex(int ind) {
        if (ind > this.size() - 1 || ind < 0 || (2 + (2 * ind)) > this.size() - 1) {
            return null;
        }
        return heap.get((ind + 1) * 2);
    }
    
    int getIndex(Entry <Key, V> curr) {
        return pairs.get(curr.value);
    }
    
    Entry<Key, V> getEntry(int index) {
        return heap.get(index);
    }
    

    /**
     * {@inheritDoc}
     * Runtime - O(lgn)
     */
    @Override
    public void add(Key key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (this.containsValue(value)) {
            throw new IllegalArgumentException();
        }
        Entry<Key, V> newEnt = new Entry<Key, V>(key, value);
        if (this.size() == 0) {
            heap.add(0, newEnt);
            pairs.put(value, 0);
            return;
        }
        
        //put at the end of the heap and hashMap
        heap.add(newEnt);
        pairs.put(value, this.size() - 1);
            
        Entry<Key, V> parent = parent(this.size() - 1);
        //this part takes log(n) time for switching in each level if needed to get to top
        while (parent != newEnt && parent.key.compareTo(newEnt.key) > 0) {
            switchPair(parent, newEnt);
            parent = parent(getIndex(newEnt)); 
        }
    }
    
    //swap parent child entry when child is less than parent
    void switchPair(Entry<Key, V> parent, Entry<Key, V> child) {
        int indexP = getIndex(parent);
        int indexC = getIndex(child);
        
        //switch the two in the hashMap
        pairs.put(parent.value, indexC);
        pairs.put(child.value, indexP);
        
        //switch the two in the heap
        heap.set(indexC, parent);
        heap.set(indexP, child);
    }

    /**
     * {@inheritDoc}
     * 
     * Runtime - O(logn)
     */
    @Override
    public void decreaseKey(V value, Key newKey) {
        if (!pairs.containsKey(value)) {
            throw new NoSuchElementException();
        }
        if (newKey == null) {
            throw new IllegalArgumentException();
        }
        //if newKey is greater than old key
        int index = pairs.get(value);
        if (newKey.compareTo(heap.get(index).key) > 0) {
            throw new IllegalArgumentException();
        }
        
        //change the value in heap, hashMap does not matter as it does not contain key
        Entry<Key, V> newEnt = new Entry<Key, V>(newKey, value);
        heap.set(index, newEnt);
        
        Entry<Key, V> parent = parent(index);
        //only need to check above it, as the key can only decrease and the entry can only move up
        //this part takes logn time to possibly check up each level
        while (pairs.get(value) >= 1 && parent != newEnt && parent.key.compareTo(newEnt.key) > 0) {
            switchPair(parent, newEnt);
            parent = parent(getIndex(newEnt));
        }  
        
    }

    /**
     * {@inheritDoc}
     * 
     * Runtime - O(1)
     * Gets the top min element in heap
     */
    @Override
    public Entry<Key, V> peek() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        return heap.get(0);
    }
    
    //takes log(n) time as recurrence is T(n/2) + O(1)
    void minHeapify(int index) {
        
        Entry<Key, V> smallest = heap.get(index);
        Entry<Key, V> oldSmall = heap.get(index);
        
        if (!(index >= 0 && index <= heap.size() - 1)) {
            return;
        }
        
        Entry<Key, V> leftChild = leftChildIndex(index);
        
        Entry<Key, V> rightChild = rightChildIndex(index);
        
        if (rightChild != null) {
            if (rightChild.key.compareTo(smallest.key) < 0) {
                smallest = rightChild; 
            }
        }
        if (leftChild != null) {
            if (leftChild.key.compareTo(smallest.key) < 0) {
                smallest = leftChild;
            }
        } 
        if (smallest != heap.get(index)) {
            switchPair(smallest, heap.get(index));
            minHeapify(heap.indexOf(oldSmall));
        }
    }

    /**
     * {@inheritDoc}
     * 
     *  Runtime - O(logn)
     */
    @Override
    public Entry<Key, V> extractMin() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        if (heap.size() == 1) {
            return heap.remove(0);
        }
        Entry<Key, V> min = heap.get(0);
        pairs.remove(min.value);
          
        //move the last element to the top in heap and hashMap, and then minHeapify
        Entry<Key, V> last = heap.remove(this.size() - 1);
        heap.set(0, last);
        pairs.put(last.value, 0);
        minHeapify(0); //takes log(n) time
            
        return min;
    }

    /**
     * {@inheritDoc}
     * 
     * Runtime - O(n) for going through each element and adding to set of values
     * 
     */
    @Override
    public Set<V> values() {
        Set<V> values = new HashSet<V>();
        for (Entry<Key,V> each: heap) {
            values.add(each.value);
        }
        return values;

    }
}