import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ByteArrayPermuteTest {
	int count;

	@Before
	public void setUp() throws Exception {
		count = 0;
	}

	@Test
	public void testPermutationZero() throws Exception {
		count(new byte[16], 0);
		Assert.assertEquals(1, count);
	}

	@Test
	public void testPermutationSingle() throws Exception {
		count(new byte[16], 1);
		Assert.assertEquals(16 * 8, count);
	}

	@Test
	public void testPermutationDouble() throws Exception {
		count(new byte[16], 2);
		Assert.assertEquals(8128, count);
	}

	@Test
	public void testPermutationTriple() throws Exception {
		count(new byte[16], 3);
		Assert.assertEquals(341376, count);
	}

	@Test
	public void testPermutationQuad() throws Exception {
		count(new byte[16], 4);
		Assert.assertEquals(10668000, count);
	}

	protected void count(byte[] data, int ones) throws Exception {
		ByteArrayPermute.permute(new byte[16], ones, new RunnableWithParameter<byte[]>() {
			@Override
			public boolean run(byte[] value) throws Exception {
				count++;
				return true;
			}
		});
	}

}
