import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This program generates a AES key, retrieves its raw bytes, and then
 * reinstantiates a AES key from the key bytes. The reinstantiated key is used
 * to initialize a AES cipher for encryption and decryption.
 */

public class AES {

	/**
	 * Turns array of bytes into string
	 * 
	 * @param buf
	 *            Array of bytes to convert to hex string
	 * @return Generated hex string
	 */
	public static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");

			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	public static void main(String[] args) throws Exception {
		// Get the KeyGenerator

		javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator.getInstance("AES");
		kgen.init(128); // 192 and 256 bits may not be available

		// Generate the secret key specs.
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();

		SecretKeySpec skeySpec = new SecretKeySpec(EncryptionStuff.BLANK_KEY, "AES");

		// Instantiate the cipher

		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec,  new IvParameterSpec(new byte[16]));

		byte[] p = (args.length == 0 ? "12345678901234561234567890123456" : args[0]).getBytes();
		byte[] encrypted = cipher.doFinal(p);
		System.out.println("encrypted string: " + asHex(encrypted));

		Cipher cipher2 = Cipher.getInstance("AES/CBC/NoPadding");
		cipher2.init(Cipher.DECRYPT_MODE, skeySpec,  new IvParameterSpec(new byte[16]));
		
		byte[] original = cipher2.doFinal(encrypted);
		String originalString = new String(original);
		System.out.println("Original string: " + originalString + " " + asHex(original));
	}
}
