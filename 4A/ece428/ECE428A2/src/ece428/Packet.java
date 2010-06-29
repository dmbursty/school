package ece428;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/**
 * Represents a packet, complete with checksum, header, and data.
 * <p>
 * Has the following fields
 * <ul>
 * <li>0-7: checksum</li>
 * <li>8-11: sequence number</li>
 * <li>12-15: acknowledgment number</li>
 * <li>16-19: flags: 17:syn, 18:fin, 19:ack</li>
 * <li>20+: data</li>
 * </ul>
 */
public class Packet {
    /**
     * All data related to the packet. What is actually sent over the wire.
     */
    final byte[] contents;

    // private static Logger log = Logger.getLogger(Packet.class.getName());
    
    // Location of significant interesting positions in the packet, in bytes 
    static final int CHECKSUM_START = 0;
    static final int POST_CHECKSUM_START = 8;
    static final int SEQUENCE_NUMBER_START = 8;
    static final int ACKNOWLEDGMENT_NUMBER_START = 12;
    static final int FLAGS_START = 16;
    public static final int DATA_START = 20;

    public static final int HEADER_LENGTH = DATA_START;

    /*
     * Masks which outline the bit representing the flags in the flags field
     */
    static final int SYN_FLAG_MASK = 0x04;
    static final int FIN_FLAG_MASK = 0x02;
    static final int ACK_FLAG_MASK = 0x01;

    /**
     * Create a packet with no data
     */
    public Packet() {
        this.contents = new byte[HEADER_LENGTH];
    }

    /**
     * Creates a packet with either the packet contents or using data
     * 
     * @param hasHeader
     *            true if data contains a header, false if data only contains
     *            data
     */
    public Packet(byte[] data, int offset, int length, boolean hasHeader) {
        int arrayLength;
        int copyOffset;
        if (hasHeader) {
            arrayLength = length;
            copyOffset = 0;
        } else {
            arrayLength = HEADER_LENGTH + length;
            copyOffset = DATA_START;
        }

        this.contents = new byte[arrayLength];
        System.arraycopy(data, offset, contents, copyOffset, length);
    }

    /**
     * Header fields in an human readable format
     */
    public String toString() {
        return (isSyn() ? "SYN; " : "") + (isAck() ? "ACK; " : "") + (isFin() ? "FIN" : "")
                + "\n\tSEQ NUM: " + getSequenceNumber() + "; ACK NUM: " + getAckNumber()
                + "\n\tDATA SIZE: " + (contents.length - HEADER_LENGTH) + "\n\tChecksum: "
                + getChecksum();
    }

    // /////////////////
    // Getters and setters

    /**
     * @return a copy of the contents of this packet. An identical packet can be
     * created with the contents returned.
     */
    public byte[] getContents() {
        return Arrays.copyOf(contents, contents.length);
    }

    protected long getChecksum() {
        return getLong(0);
    }

    /**
     * Sets the checksum field correctly so that {@link #isGood()} returns true
     */
    public void setChecksum() {
        setChecksum(calculateChecksum());
    }

    protected void setChecksum(long value) {
        setLong(0, value);
    }

    protected long calculateChecksum() {
        Checksum checksum = new Adler32();
        checksum.update(contents, POST_CHECKSUM_START, contents.length - POST_CHECKSUM_START);
        return checksum.getValue();
    }

    /**
     * @return true if the packet is of a valid length and the checksum is valid
     */
    public boolean isGood() {
        if (contents.length < HEADER_LENGTH) {
            return false;
        }

        long calcd = calculateChecksum();
        long curr = getChecksum();
        // log.info("Calculated: " + calcd + "; Checksum: " + curr);
        return calcd == curr;
    }

    /**
     * @return a copy of the data if it exists or null
     */
    public byte[] getData() {
        if (hasData()) {
            byte[] data = new byte[contents.length - HEADER_LENGTH];
            System.arraycopy(contents, DATA_START, data, 0, data.length);
            return data;

        } else {
            return null;
        }
    }

    public int getSequenceNumber() {
        return getInt(SEQUENCE_NUMBER_START);
    }

    public void setSequenceNumber(int value) {
        setInt(SEQUENCE_NUMBER_START, value);
    }

    public int getAckNumber() {
        return getInt(ACKNOWLEDGMENT_NUMBER_START);
    }

    public void setAcknowledgementNumber(int value) {
        setInt(ACKNOWLEDGMENT_NUMBER_START, value);
    }

    public boolean isSyn() {
        return (getFlags() & SYN_FLAG_MASK) != 0;
    }

    public void setSyn(boolean syn) {
        if (syn) {
            setFlags(getFlags() | SYN_FLAG_MASK);
        } else {
            setFlags(getFlags() & ~SYN_FLAG_MASK);
        }
    }

    public boolean isFin() {
        return (getFlags() & FIN_FLAG_MASK) != 0;
    }

    public void setFin(boolean fin) {
        if (fin) {
            setFlags(getFlags() | FIN_FLAG_MASK);
        } else {
            setFlags(getFlags() & ~FIN_FLAG_MASK);
        }
    }

    public boolean isAck() {
        return (getFlags() & ACK_FLAG_MASK) != 0;
    }

    public void setAck(boolean ack) {
        if (ack) {
            setFlags(getFlags() | ACK_FLAG_MASK);
        } else {
            setFlags(getFlags() & ~ACK_FLAG_MASK);
        }
    }

    protected int getFlags() {
        return getInt(FLAGS_START);
    }

    protected void setFlags(int value) {
        setInt(FLAGS_START, value);
    }

    /**
     * @return true if this packet has a data field
     */
    public boolean hasData() {
        return contents.length > HEADER_LENGTH;
    }

    // /////////////////
    // Getters and setters which directly modify the contents

    /**
     * Formats the 4 bytes starting at offset into an int 
     */
    protected int getInt(int offset) {
        try {
            return getDataInputStream(offset, 4).readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Formats the 8 bytes starting at offset into a long 
     */
    protected long getLong(int offset) {
        try {
            return getDataInputStream(offset, 8).readLong();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Saves value into the 4 bytes starting at offset 
     */
    protected void setInt(int offset, int value) {
        ByteArrayOutputStream array = new ByteArrayOutputStream(4);
        DataOutputStream out = new DataOutputStream(array);

        try {
            out.writeInt(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.arraycopy(array.toByteArray(), 0, contents, offset, 4);
    }

    /**
     * Saves value into the 8 bytes starting at offset 
     */
    protected void setLong(int offset, long value) {
        ByteArrayOutputStream array = new ByteArrayOutputStream(8);
        DataOutputStream out = new DataOutputStream(array);

        try {
            out.writeLong(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.arraycopy(array.toByteArray(), 0, contents, offset, 8);
    }

    protected DataInputStream getDataInputStream(int offset, int length) {
        return new DataInputStream(new ByteArrayInputStream(contents, offset, length));
    }

}
