package de.taujhe.javamumble.common.protocol.control;

import javax.annotation.Nonnull;

/**
 * Represents the header element of a TCP/control channel network packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public class Header {

	/**
	 * Length of the header in the network packet (in bytes).
	 */
	public static int HEADER_LENGTH = 6;
	/**
	 * Maximum payload length in bytes.
	 */
	public static int MAXIMUM_PAYLOAD_LENGTH = 8 * 1024 * 1024 - 1;
	/**
	 * Maximum packet length in byte (header + payload).
	 */
	public static int MAXIMUM_PACKET_LENGTH = HEADER_LENGTH + MAXIMUM_PAYLOAD_LENGTH;

	@Nonnull
	private final PacketType packetType;
	private final int packetLength;

	public Header(@Nonnull final PacketType packetType, final int packetLength) {
		this.packetType = packetType;
		this.packetLength = packetLength;
	}

	@Nonnull
	public PacketType getPacketType() {
		return packetType;
	}

	public int getPacketLength() {
		return packetLength;
	}
}
