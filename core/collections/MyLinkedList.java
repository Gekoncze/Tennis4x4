
package core.collections;

import java.security.InvalidParameterException;
import java.util.LinkedList;

/**
 *
 * @author gekoncze
 */
public class MyLinkedList<T> {
    private class Item {
        private T data = null;
        private boolean locked = false;
        
        public void setData(T data){
            if(locked) return;
            this.data = data;
        }

        public T getData() {
            return data;
        }
        
        public void lock(){
            locked = true;
        }
        
        public boolean isLocked(){
            return locked;
        }
    }
    
    private final LinkedList<Item> itemList = new LinkedList<>();
    
    public synchronized void set(T data, int i){
        if(i < 0) throw new InvalidParameterException();
        if(i >= itemList.size()){
            resize(i);
        }
        
        itemList.get(i).setData(data);
    }
    
    public synchronized T get(int i){
        if(i >= itemList.size() || i < 0){
            return null;
        }
        
        return itemList.get(i).getData();
    }
    
    public synchronized void lock(int i){
        if(i >= itemList.size() || i < 0){
            return;
        }
        
        itemList.get(i).lock();
    }
    
    public synchronized void clear(){
        itemList.clear();
    }
    
    private synchronized void resize(int i){
        while(i >= itemList.size()){
            itemList.add(new Item());
        }
    }
}
