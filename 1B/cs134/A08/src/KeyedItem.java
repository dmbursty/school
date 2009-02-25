public class KeyedItem
{ 
   private Comparable key;
   private Object value;
  
   public KeyedItem(Comparable theKey)
   // post: this contains the key theKey and no associated value
   {  this(theKey, null);
   }
  
   public KeyedItem(Comparable theKey, Object theValue) 
   // post: this contains key theKey and associated value theValue
   {  this.key = theKey;
      this.value = theValue;
   }
  
   public Comparable getKey()
   // post: returns the key value
   {  return this.key;
   }
  
   public Object getValue()
   // post: returns the Object value associated with the key
   {  return this.value;
   }
  
   public void setValue(Object theValue)
   // post: this contains the associated value theValue
   {  this.value = theValue;
   }
   
   public String toString()
   {
    return this.key.toString() + ":" + this.value.toString();
   }
}
