public class ListArray
{ private static final int INITIAL_SIZE = 50;
  private Object[] items = new Object[INITIAL_SIZE];
  private int numItems = 0;
  
  public ListArray()
  // post: creates an empty ListArray object
  {
  }
  
  public void add(int index, Object item)
  //pre: 1 <= index <= size()+1
  //post: Items from index to size have their index increased by one,
  //      item is inserted at index.
  { assert (1 <= index && index <= numItems+1);
    ensureCapacity();
    for (int i=numItems; i >= index; i--)
    { items[translate(i+1)] = items[translate(i)];
    }
    items[translate(index)] = item;
    numItems++;
  }
  
  private int translate(int listPosition) 
  // pre: 1 <= listPostion <= size()+1
  // post: returns listPosition-1 (corresponding array position)
  { assert(1<=listPosition && listPosition <= numItems+1);
    return listPosition-1; 
  }
  
  private void ensureCapacity()
  // post: items is not full
  { // if items is full, create a larger array, copy all entries into it, and 
    // update items to reference the larger array
    if (numItems==items.length)
    { Object[] bigger = new Object[2*numItems+1];
      for (int i=0; i < numItems; i++)
      { bigger[i] = items[i]; 
      }
      items = bigger;
    }
    
  }
  
  public Object get(int index)
  //pre: 1 <= index <= size()
  //post: returns item at position index in the list
  { assert(1<=index && index <= numItems);
    return items[translate(index)];
  }
  
  public boolean isEmpty()
  //post: returns true iff this is empty
  { return numItems==0;
  }
  
  public Object remove(int index)
  //pre: 1 <= index <= size()
  //post: removes and returns the item at position index in the list
  //      and items from index+1 to the end have their position 
  //      decreased by one.
  { assert(1<=index && index <= numItems);
    Object returnVal = items[translate(index)];
    for (int i=index+1; i <= numItems; i++)
    { items[translate(i-1)] = items[translate(i)];
    }
    numItems--;
    return returnVal;
  }
  
  public void removeAll()
  //post: this is empty
  { numItems = 0;
  }
  
  public int size()
  //post: returns the number of items currently in this
  { return numItems;
  }
  
  public boolean contains(Object item)
  //post: returns true iff item is the same as one of the entries
  //      in this.
  { boolean isThere = false;
    int position = 0;
    while (position < numItems && !isThere)
    { if (items[position].equals(item)) { isThere = true; }
      else {position++; }
    }
    return isThere;
  }
}