package ece428.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.junit.Test;

import ece428.udp.UDPClient;
import ece428.udp.UDPServer;

public class UDPTest {
	@Test
	public void testSinglePacket() throws IOException, InterruptedException {
		UDPServer server = new UDPServer();
		UDPClient client = new UDPClient();

		int port = server.getLocalPort();
		
		DatagramPacket sentPacket = createTestPacket(port);
		client.sendPacket(sentPacket);
		
		DatagramPacket receivedPacket = server.receive();
		assertDataEquals(receivedPacket, sentPacket);
	}
	
	protected DatagramPacket createTestPacket(int port) throws UnknownHostException {
		byte[] buffer = "I am DATA!".getBytes();
		return new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), port);
	}
	
	protected void assertDataEquals(DatagramPacket expected, DatagramPacket actual){
		Assert.assertEquals(expected.getLength(), actual.getLength());
		
		for(int i = 0; i < expected.getLength(); i++){
			Assert.assertEquals(expected.getData()[i], actual.getData()[i]);
		}
	}
}
