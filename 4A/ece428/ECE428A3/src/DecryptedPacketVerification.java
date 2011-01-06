import java.util.HashSet;

/**
 * Ensures that a packet has actually been decrypted
 * 
 * @author John Barr (j2barr@uwaterloo.ca)
 */
public abstract class DecryptedPacketVerification {
	public static final byte[] HAMLET_CHARACTERS = new byte[] { 10, 32, 33, 39,
			44, 45, 46, 49, 50, 51, 52, 53, 58, 59, 63, 65, 66, 67, 68, 69, 70,
			71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87,
			89, 91, 93, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107,
			108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
			121, 122 };
	public static final HashSet<Byte> HAMLET_CHARACTER_SET = new HashSet<Byte>(
			HAMLET_CHARACTERS.length);
	static {
		for (byte b : HAMLET_CHARACTERS) {
			HAMLET_CHARACTER_SET.add(b);
		}
	}

	/**
	 * Uses all the contained tests to ensure that the packet is correct
	 */
	public static boolean isGood(byte[] data) {
		return checkByteParity(data) && checkHamletCharacterset(data);
	}

	/**
	 * Uses all the contained tests to ensure that the block is correct
	 */
	public static boolean isBlockGood(byte[] data, boolean isFirstBlock) {
		if (isFirstBlock) {
			return checkHamletCharacterset(data, 1, data.length - 1);
		} else {
			return checkHamletCharacterset(data);
		}
	}

	/**
	 * Parity byte is at the beginning of every packet
	 * 
	 * @return true if all bytes in data xor'd together produce the 0 byte
	 */
	protected static boolean checkByteParity(byte[] data) {
		byte parity = 0;
		for (byte b : data) {
			parity ^= b;
		}

		return parity == 0;
	}

	/**
	 * @return true iff all bytes in data are characters of Hamlet Act 1
	 */
	protected static boolean checkHamletCharacterset(byte[] data) {
		return checkHamletCharacterset(data, 0, data.length);
	}

	/**
	 * @return true iff all of the characters in data are characters of Hamlet
	 *         Act 1
	 */
	protected static boolean checkHamletCharacterset(byte[] data, int offset,
			int length) {
		for (int i = 1; i < data.length; i++) {
			if (!HAMLET_CHARACTER_SET.contains(data[i])) {
				return false;
			}
		}
		return true;
	}
}
