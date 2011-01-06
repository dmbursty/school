import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Makes use of {@link Worker} to decrypt an AES packet for a given number of
 * ones.
 * 
 * @author John Barr (j2barr@uwaterloo.ca)
 */
public abstract class Administrator {

	protected static final int NUM_THREADS = 4;
	protected static final int[] MAGIC_NUMBER_1 = { 0, 1, 2, 3, 4 };
	protected static final int[] MAGIC_NUMBER_2 = { 7, 9, 25, 77, 111 };
	protected static final int[] MAGIC_NUMBER_3 = { 16, 26, 67, 75, 105 };
	protected static final int[] MAGIC_NUMBER_4 = { 30, 46, 69, 83, 93 };
	protected static final int[] MAGIC_NUMBER_5 = { 123, 124, 125, 126, 127 };

	/**
	 * Makes use of {@link Worker} to decrypt an AES packet for a given number
	 * of ones. Spawns multiple workers in the numZeros=5 case.
	 * <p>
	 * The reason multiple workers are only used for n=5 is because it is hard
	 * to partition the keys evenly using the implemented algorithm
	 */
	public static byte[] decrypt(int numZeros, byte[] data)
			throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

		List<KeyBreakingWorker> workers = new ArrayList<KeyBreakingWorker>();
		if (numZeros == 5) {
			workers.add(new KeyBreakingWorker(numZeros, MAGIC_NUMBER_1,
					MAGIC_NUMBER_2, data));
			workers.add(new KeyBreakingWorker(numZeros, MAGIC_NUMBER_2,
					MAGIC_NUMBER_3, data));
			workers.add(new KeyBreakingWorker(numZeros, MAGIC_NUMBER_3,
					MAGIC_NUMBER_4, data));
			workers.add(new KeyBreakingWorker(numZeros, MAGIC_NUMBER_4,
					MAGIC_NUMBER_5, data));
		} else {
			workers.add(new KeyBreakingWorker(numZeros, data));
		}

		byte[] key;
		try {
			key = executor.invokeAny(workers);
		} catch (ExecutionException e) {
			// no worker was successful
			return null;
		}

		// kill all remaining workers
		executor.shutdownNow();

		return key;
	}
}
