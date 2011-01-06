/**
 * Permutes a byte array to all combinations with the given number of ones and
 * zeroes then calls the given function
 * 
 * @author John Barr (j2barr@uwaterloo.ca)
 */
public class ByteArrayPermute {
	public static void permute(byte[] data, int ones,
			RunnableWithParameter<byte[]> runnable) throws Exception {
		permute(data, 0, ones, runnable);
	}

	public static boolean permute(byte[] data, int index, int ones,
			RunnableWithParameter<byte[]> runnable) throws Exception {
		int lengthInBits = data.length * 8;
		if (index >= lengthInBits) {
			return runnable.run(data);
		}

		int zeroes = (lengthInBits - index) - ones;
		int byteIndex = index / 8;
		int byteMod = 7 - (index % 8); // start with most significant bits first
		int byteBit = 1 << (byteMod);

		if (ones > 0) {
			data[byteIndex] |= byteBit; // set to one
			if (!permute(data, index + 1, ones - 1, runnable)) {
				return false;
			}
		}
		if (zeroes > 0) {
			data[byteIndex] &= ~byteBit; // set to zero
			if (!permute(data, index + 1, ones, runnable)) {
				return false;
			}
		}

		return true;
	}

}
