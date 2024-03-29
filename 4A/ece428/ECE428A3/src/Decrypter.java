import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Decrypts the contents of an encrypted packet sent from the server
 * 
 * @author John Barr (j2barr@uwaterloo.ca)
 */
public class Decrypter {
	public static final String ALGORITHM = "AES/CBC/NoPadding";
	public static final String BASE_ALGORITHM = "AES";

	public static final byte[] FILLED_KEY = new byte[] { -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
	public static final byte[] iv = new byte[16];

	SecretKeySpec keySpec;
	IvParameterSpec paramSpec;
	Cipher cipher;

	/**
	 * Creates a default decrypter with a key of all 1's
	 */
	public Decrypter() {
		this(FILLED_KEY);
	}

	/**
	 * Creates a decrypter with the given key
	 * 
	 * <p>
	 * Same as:<br>
	 * <code>
	 * 		Decrypter dec = new Decrypter()<br>
	 * 		dec.setKey(key)
	 * </code>
	 */
	public Decrypter(byte[] key) {
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			paramSpec = new IvParameterSpec(iv);
			setKey(key);
			reset();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Changes the current key used for decryption to the given key
	 */
	public void setKey(byte[] key) {
		keySpec = new SecretKeySpec(key, BASE_ALGORITHM);
	}

	/**
	 * Decryptes the given cypher text with the key stored in this.
	 * 
	 * @return Decrypted plaintext. null on failure
	 */
	public byte[] decrypt(byte[] cypherData) throws Exception {
		reset(); // every packet resets the IV to 0

		byte[] decrypted = null;

		for (int block = 0; block < cypherData.length / 16; block++) {
			byte[] decryptedBlock = cipher.update(cypherData, block * 16, 16);

			boolean isFirstBlock = (block == 0);

			if (DecryptedPacketVerification.isBlockGood(decryptedBlock,
					isFirstBlock)) {
				if (isFirstBlock) {
					decrypted = new byte[cypherData.length];
				}

				System.arraycopy(decryptedBlock, 0, decrypted, block * 16, 16);
			} else {
				return null;
			}
		}

		if (DecryptedPacketVerification.isGood(decrypted)) {
			return Arrays.copyOfRange(decrypted, 1, decrypted.length);
		} else {
			return null;
		}
	}

	/**
	 * Resets the initialization vector to the 0 vector
	 */
	protected void reset() throws InvalidKeyException,
			InvalidAlgorithmParameterException {
		cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
	}

}
