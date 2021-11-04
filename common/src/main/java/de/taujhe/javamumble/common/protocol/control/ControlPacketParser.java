package de.taujhe.javamumble.common.protocol.control;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import MumbleProto.Mumble;

/**
 * Class to parse a {@link ByteBuffer} into one of the packet classes.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public final class ControlPacketParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControlPacketParser.class);

	private ControlPacketParser() {
	}

	public static Header parseHeader(@Nonnull final ByteBuffer buffer) {
		if (buffer.remaining() < Header.HEADER_LENGTH) {
			throw new IllegalArgumentException("Buffer must contain at least " + Header.HEADER_LENGTH + " more bytes:");
		}
		buffer.order(ByteOrder.BIG_ENDIAN);
		final short id = buffer.getShort();
		final PacketType packetType
			= PacketType.from(id)
						.orElseThrow(() -> new IllegalStateException(
							"Id " + id + " cannot be resolved to a packet type."));

		return new Header(packetType, buffer.getInt());
	}

	public static VersionPacket parseVersionPacket(@Nonnull final ByteBuffer buffer, final int length) {
		if (buffer.remaining() != length) {
			throw new IllegalArgumentException(
				"Buffer's remaining length (" + buffer.remaining() + ") differs from value in packet header: "
					+ length);
		}

		try {
			final Mumble.Version version = Mumble.Version.parseFrom(buffer);
		} catch (final InvalidProtocolBufferException e) {
			LOGGER.error("Exception while parsing version packet.", e);
		}

		return null;
	}
}
