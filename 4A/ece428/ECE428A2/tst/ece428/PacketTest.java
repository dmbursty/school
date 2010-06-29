package ece428;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class PacketTest {
    Packet packet;
    byte[] pureData;

    @Before
    public void setUp() throws Exception {
        pureData = "This is my test string! yayyy".getBytes();
        packet = new Packet(pureData, 0, pureData.length, false);
    }

    @Test
    public void testGetContents() {
        Packet otherPacket = new Packet(packet.getContents(), 0, packet.getContents().length, true);
        Arrays.equals(otherPacket.getData(), packet.getData());
    }

    @Test
    public void testReChecksum() {
        Assert.assertEquals(false, packet.isGood());
        packet.setChecksum();
        Assert.assertEquals(true, packet.isGood());

        Packet clonedPacket = new Packet(packet.getContents(), 0, packet.getContents().length, true);
        Assert.assertEquals(true, clonedPacket.isGood());
    }

    @Test
    public void testNoData() {
        packet = new Packet();

        Assert.assertEquals(false, packet.isGood());
        packet.setSyn(true);
        packet.setSequenceNumber(1);
        packet.setChecksum();
        Assert.assertEquals(true, packet.isGood());

        Packet clonedPacket = new Packet(packet.getContents(), 0, packet.getContents().length, true);
        Assert.assertEquals(true, clonedPacket.isGood());
        Assert.assertEquals(true, clonedPacket.isSyn());
    }

    @Test
    public void testChecksum() {
        Assert.assertEquals(0, packet.getChecksum());
        packet.setChecksum(666);
        Assert.assertEquals(false, packet.isGood());
        packet.setChecksum();
        Assert.assertEquals(true, packet.isGood());
        packet.setAck(true);
        Assert.assertEquals(false, packet.isGood());
    }

    @Test
    public void testGetData() {
        Assert.assertTrue(Arrays.equals(pureData, packet.getData()));
    }

    @Test
    public void testGetSetSequenceNumber() {
        Assert.assertEquals(0, packet.getSequenceNumber());
        packet.setSequenceNumber(123);
        Assert.assertEquals(123, packet.getSequenceNumber());
    }

    @Test
    public void testGetSetAcknowledgementNumber() {
        Assert.assertEquals(0, packet.getAckNumber());
        packet.setAcknowledgementNumber(321);
        Assert.assertEquals(321, packet.getAckNumber());
    }

    @Test
    public void testGetSetSyn() {
        Assert.assertEquals(false, packet.isSyn());
        packet.setSyn(true);
        Assert.assertEquals(true, packet.isSyn());
    }

    @Test
    public void testGetSetFin() {
        Assert.assertEquals(false, packet.isFin());
        packet.setFin(true);
        Assert.assertEquals(true, packet.isFin());
    }

    @Test
    public void testGetSetAck() {
        Assert.assertEquals(false, packet.isAck());
        packet.setAck(true);
        Assert.assertEquals(true, packet.isAck());
    }
    
    @Test
    public void testGetSetFlags() {
        Assert.assertEquals(false, packet.isSyn());
        Assert.assertEquals(false, packet.isFin());
        Assert.assertEquals(false, packet.isAck());
        packet.setSyn(true);
        packet.setFin(false);
        packet.setAck(false);
        Assert.assertEquals(true, packet.isSyn());
        Assert.assertEquals(false, packet.isFin());
        Assert.assertEquals(false, packet.isAck());
        packet.setSyn(true);
        packet.setFin(true);
        packet.setAck(false);
        Assert.assertEquals(true, packet.isSyn());
        Assert.assertEquals(true, packet.isFin());
        Assert.assertEquals(false, packet.isAck());
        packet.setSyn(true);
        packet.setFin(true);
        packet.setAck(true);
        Assert.assertEquals(true, packet.isSyn());
        Assert.assertEquals(true, packet.isFin());
        Assert.assertEquals(true, packet.isAck());
    }
}
