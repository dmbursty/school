import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.stream.events.Characters;

public class EncryptionStuff {
	public static final String ALGORITHM = "AES/CBC/NoPadding";
	public static final String ALGORITHM_SIMPLE = "AES";
	public static final int KEY_SIZE = 128;
	public static final int BLOCK_SIZE = 128;
	public static final int KEY_SIZE_BYTES = KEY_SIZE / 8;
	public static final int BLOCK_SIZE_BYTES = BLOCK_SIZE / 8;

	public static final byte[] BLANK_KEY = new byte[16];
	public static final byte[] FILLED_KEY = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1 };
	public static final byte[] iv = new byte[16];

	SecretKeySpec keySpec;
	SecretKey secretKey;
	IvParameterSpec paramSpec;
	Cipher cipher;

	public EncryptionStuff() throws Exception {
		this(FILLED_KEY);
	}

	public EncryptionStuff(byte[] key) throws Exception {

		keySpec = new SecretKeySpec(key, ALGORITHM_SIMPLE);
		cipher = Cipher.getInstance(ALGORITHM);
		paramSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
	}
	
	public void reset() throws InvalidKeyException, InvalidAlgorithmParameterException {
		cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
		
		//System.out.println("decrypted : " + cipher.doFinal());
	}

	public void decrypt(byte[] cyperData) throws Exception {
		reset();
		try {
			// System.out.println(cyperData.length);
			byte[] toDecrypt = Arrays.copyOf(cyperData, BLOCK_SIZE_BYTES);
			// System.out.println("decrypted : "
			// + new String(cipher.doFinal(/* toDecrypt */new byte[] { 96,
			// -29, -3, -41,
			// 73, -26, 65, -64, 20, -36, 41, -120, 11, 96, -79, 53 })));
			System.out.println("decrypted : " + new String(cipher.update(cyperData)));//toDecrypt)));
		} catch (Exception e) {
			System.out.println("Decryption fail");
		}
		/*
		 * for (int i = 0; i +1< cyperData.length / BLOCK_SIZE_BYTES; i++) {
		 * System.out.println(new String(cipher.doFinal(cyperData, i *
		 * BLOCK_SIZE_BYTES, BLOCK_SIZE_BYTES))); }
		 */

	}

	public static void main(String[] args) throws Exception {
		byte[] encrpyted = new byte[] { -71, -36, -67, -92, 121, 87, -59, -32, -124, 25, -113,
				-102, 122, 106, 4, 87, 53, 20, 123, 13, 105, -90, -128, -103, -7, 52, -28, 77, -10,
				41, -16, 22, -106, 29, -110, 119, 120, -70, -37, -116, 21, -34, 57, -45, -19, 65,
				-69, 29, 57, 99, 28, -108, -29, 77, -60, -8, 83, 60, 90, 25, 69, -126, 85, 126,
				-65, 125, -96, 35, 2, -17, -46, -83, 127, 25, 57, -97, 2, 111, 114, 101, 62, -82,
				52, -69, -44, 91, -17, 101, -13, -104, 79, -96, -35, -97, -47, -63, -37, 95, 25,
				-95, -45, 117, -126, 80, -20, 68, -25, 87, -34, 83, -118, -54 };
		byte[] encryptedN0 = new byte[] { 41, 99, -65, 78, -30, -14, 113, 1, 19, 114, -1, 31, -24,
				72, -54, 88, -112, -33, -116, 18, 17, -74, -68, -97, 112, -10, -90, 100, 29, -27,
				67, -103, 107, -6, 91, -80, -112, 89, -12, -104, 103, 42, -35, 9, 95, -62, 65, 70,
				93, -106, -5, 50, -43, -49, 100, 6, 43, -16, -26, 80, 58, -92, 16, 79 };

		EncryptionStuff encr = new EncryptionStuff();

		byte[] decrypted = encr.cipher.doFinal(encryptedN0);

		System.out.println(new String(decrypted));
		System.out.println(new String(decrypted, "US-ASCII"));
		System.out.println(new String(decrypted, "ISO-8859-1"));

	}
}
