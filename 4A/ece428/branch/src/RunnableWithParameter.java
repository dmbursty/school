
/**
 * Used by {@link ByteArrayPermute}
 * 
 * TODO: Rename me!
 * @author John Barr (j2barr@uwaterloo.ca)
 */
public interface RunnableWithParameter<T> {
	public boolean run(T value) throws Exception;
}
