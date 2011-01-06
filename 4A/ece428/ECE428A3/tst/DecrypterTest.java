import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class DecrypterTest {
	public static final byte[] FILLED_KEY = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1 };
	protected static final byte[] ENCRYPTED_DATA_1 = new byte[] { -128, -122, -62, 36,
			-127, 75, 124, -10, 105, -94, -50, 88, 63, 17, -25, -77, 105, 95, -10, 47,
			13, -96, 26, -126, -21, -32, -41, 3, 116, -103, 23, -56, 109, -43, 123, 71,
			-77, 5, 90, -80, -99, -29, 78, -19, -104, 120, 98, -43, 88, 68, -79, -98, 99,
			124, -29, -88, -23, -94, 52, 25, 104, -6, -101, -123, -119, 125, -128, 64,
			-53, -114, -65, -30, -109, -17, -10, 28, -20, -7, 112, -48, -112, -33, 72,
			18, 35, 106, -124, -22, 94, -4, 62, 87, 118, 17, -50, -121, -106, 55, -32,
			122, -114, 56, 96, 103, 34, -102, -15, 26, 14, 30, -79, 65, 42, 17, -11, 57,
			69, 6, -45, 46, -33, -72, 30, 43, 44, -35, -16, -23, -60, 102, 97, 89, -126,
			-100, 3, 32, -99, -86, 88, 53, -40, -60, -29, 0, -93, -88, -93, 13, 42, 89,
			-115, 93, -26, 29, -23, 28, 27, 34, -16, -86, 42, -41, -60, 34, 126, -54,
			-83, 32, -12, 47, 60, 119, 87, -2, 56, -44, 52, 104, -36, -97, 38, 15, 31,
			-41, -75, 81, 112, -70, -97, -107, 124, -72, -113, 71, 30, -8, -9, -1, -1, 1,
			-126, 14, -38, 103, -103, -73, -30, 109, -53, -90, 5, 67, -72, 70, 105, 30,
			83, 46, 52, -97, -105, -56, -95, 11, -38, -64, 84, 44, 37, 47, 20, -98, 78,
			-8, -11, 124, -39, -55, -4, 99, 42, 95, -19, 110, 63, -81, 17, -31, -46, 23,
			-26, -7, 21, 57, -66, 50 };
	protected static final byte[] ENCRYPTED_DATA_2 = new byte[] { -61, -85, -4, 56, -47,
			7, -74, -45, -83, -8, -120, -118, -20, -122, -9, -125, 116, -127, -38, 21,
			-50, -127, 54, -103, -98, 125, -116, -35, -32, -8, 54, 9, 62, 108, -79, -106,
			-46, -117, 71, -88, -18, 117, 108, 118, 121, 77, -46, 60, -1, -20, -100, 100,
			-70, 82, -78, 92, 17, 11, 42, 63, 116, -91, -64, 36, -103, 97, 63, 104, 1,
			47, -101, 84, -47, 119, 115, 111, 85, 45, -10, 14, -15, -101, 62, -73, -68,
			59, -94, 17, -57, -82, -26, 53, -63, 19, -20, -103, -45, -61, 44, 15, 103,
			-120, -24, 45, -67, 41, -17, 63, -22, -53, 31, 94, 103, 61, 65, -121, 73,
			-109, -35, 106, 95, 105, -110, -12, 69, 30, 78, 102, -10, 45, 80, 68, 46,
			-114, 102, 8, 111, 6, 71, -114, -73, -122, 64, 38, 46, 125, -78, -75, 106, 7,
			8, 60, 63, 106, -104, -112, -6, -79, 61, -96, -11, -80, -17, 125, -22, 5, 44,
			-47, -22, -10, 9, 53, -47, 57, 23, -27, 60, -78, -89, -77, -46, 104, -81, 51,
			16, 120, -122, 75, -54, -70, -33, 47 };
	protected static final byte[] ENCRYPTED_DATA_3 = new byte[] { 31, 112, -75, 43, -94,
			30, -100, 91, 107, 71, 13, -109, 33, 113, -6, -115, -49, 47, 91, -53, 79, 53,
			103, 112, -123, -81, 125, -38, 24, -24, 66, -101, 37, -21, 34, 46, 66, -26,
			-108, 60, 77, -45, 63, -68, 4, -112, -27, 61, -27, -45, 68, 118, -126, -30,
			74, 52, -12, 114, 30, 10, -49, 106, -39, -4, -95, -65, -18, -124, 64, 27,
			-48, 126, 94, -38, 61, 88, 70, -118, -62, 61, -64, 10, -63, 75, 93, -83, 21,
			-56, 112, -8, 44, -73, 11, -112, -122, 112, 31, -69, 89, 114, -89, 44, 66,
			-24, 98, -57, 92, 45, 115, 39, 88, -56, -83, 83, 37, -100, -109, -85, 51, 94,
			-32, 1, -11, -123, -114, -117, -103, -119, 120, -71, 39, -19, -39, 11, 83,
			96, 27, -44, -15, 101, 5, 41, -24, -67, 91, -77, -48, -15, -7, -116, 105, 48,
			-16, 33, 11, 8, -36, -52, 35, 117, 93, -54, 122, -88, 97, 59, 56, -82, 6, 88,
			-32, -52, -27, 15, 97, 35, 54, -22, -99, -71, 92, -4, -78, -7, -110, -86,
			-66, 81, -42, 65, -38, 54, 120, 37, 43, 90, -104, 34, 0, -102, 42, -83, 15,
			45, -24, 120, -77, -101, 6, -117, -88, -39, -58, 73, -38, 48, -86, 75, 59,
			66, -23, 10, -81, 23, -17, -101, 63, -12, -62, 125, 9, 72, 3, -88, -105, 100,
			98, -23, 96, -73, 87, 2, -127, -102, -32, -100, 95, -89, 70, 81, -60, 109,
			22, -61, 4, 5, 6, -49, -75, -1, 57, -31, -7, 13, 5, 38, 28, 14, 124, -88, 23,
			91, -57, -11, -23, -86, -50, 109, -109, 43, -76, -47, 99, -18, 61, -10, 37,
			-67, 12, 39, 112, 20, -54, -13, 63, 79, -17, -22, -19, 97, 89, -56, -65, -93 };

	protected static final String DECRYPTED_DATA = "o more but so?\n\nLAERTES\n\n    Think it no more;\n    For nature, crescent, does not grow alone\n    In thews and bulk, but, as this temple waxes,\n    The inward service of the mind and soul\n    Grows wide withal. Perhaps he loves you now,\n    And now no soil Laertes?\n    You cannot speak of reason to the Dane,\n    And loose your voice: what wouldst thou beg, Laertes,\n    That shall not be my offer, not thy asking?\n    The head is not more nativeonvoy is assistant, do not sleep,\n    But let me hear from you.\n\nOPHELIA\n\n    Do you doubt that?\n\nLAERTES\n\n    For Hamlet and the trifling of his favour,\n    Hold it a fashion and a toy in blood,\n    A violet in the youth of primy nature,\n    Forward, not permanent, sweet, not lasting,\n    The perfume ";

	protected Decrypter decrypter;

	@Before
	public void setUp() throws Exception {
		decrypter = new Decrypter(FILLED_KEY);
	}

	@Test
	public void testBasicDecryption() throws Exception {
		String out = new String(decrypter.decrypt(ENCRYPTED_DATA_1))
				+ new String(decrypter.decrypt(ENCRYPTED_DATA_2))
				+ new String(decrypter.decrypt(ENCRYPTED_DATA_3));

		Assert.assertEquals(out, DECRYPTED_DATA);
	}
}
