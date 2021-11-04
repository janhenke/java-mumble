package de.taujhe.javamumble.common.protocol.control;

import java.util.Arrays;
import java.util.Optional;

/**
 * Packet types defined for the TCP/Control channel of the Mumble protocol.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public enum PacketType {
	VERSION(0),
	UDP_TUNNEL(1),
	AUTHENTICATE(2),
	PING(3),
	REJECT(4),
	SERVER_SYNC(5),
	CHANNEL_REMOVE(6),
	CHANNEL_STATE(7),
	USER_REMOVE(8),
	USER_STATE(9),
	BAN_LIST(10),
	TEXT_MESSAGE(11),
	PERMISSION_DENIED(12),
	ACL(13),
	QUERY_USERS(14),
	CRYPT_SETUP(15),
	CONTEXT_ACTION_MODIFY(16),
	CONTEXT_ACTION(17),
	USER_LIST(18),
	VOICE_TARGET(19),
	PERMISSION_QUERY(20),
	CODEC_VERSION(21),
	USER_STATS(22),
	REQUEST_BLOB(23),
	SERVER_CONFIG(24),
	SUGGEST_CONFIG(25);

	public static Optional<PacketType> from(final short id) {
		return Arrays.stream(values()).filter(packetType -> id == packetType.id).findFirst();
	}

	private final short id;

	PacketType(final int id) {
		this.id = (short) id;
	}

	public short getId() {
		return id;
	}
}
