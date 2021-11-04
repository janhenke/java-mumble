package de.taujhe.javamumble.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

import static java.util.Objects.nonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.taujhe.javamumble.common.protocol.control.Header;
import tlschannel.ClientTlsChannel;
import tlschannel.TlsChannel;
import tlschannel.async.AsynchronousTlsChannel;
import tlschannel.async.AsynchronousTlsChannelGroup;

/**
 * Main class for an embeddable Mumble client.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public class MumbleClient implements Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MumbleClient.class);

	public static final short DEFAULT_PORT = (short) 64738;

	private final SSLContext sslContext;

	private final SocketChannel socketChannel = SocketChannel.open();
	private final AsynchronousTlsChannelGroup tlsChannelGroup = new AsynchronousTlsChannelGroup();
	private AsynchronousTlsChannel asynchronousTlsChannel;

	private final ByteBuffer readBuffer = ByteBuffer.allocateDirect(Header.MAXIMUM_PACKET_LENGTH);

	public MumbleClient() throws IOException, NoSuchAlgorithmException, KeyManagementException {
		this(false);
	}

	public MumbleClient(@Nullable final Boolean ignoreCerts) throws
		IOException,
		NoSuchAlgorithmException,
		KeyManagementException {

		sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(
			null,
			nonNull(ignoreCerts) && ignoreCerts
				? List.of(new TrustEverythingTrustManager()).toArray(TrustManager[]::new)
				: null,
			new SecureRandom()
		);
	}

	public void connect(
		@Nonnull final String server,
		@Nullable final Short port,
		@Nonnull final String username,
		@Nullable String password,
		@Nullable List<String> userTokens
	) throws IOException {

		final InetSocketAddress socketAddress = new InetSocketAddress(
			server,
			nonNull(port)
				? Short.toUnsignedInt(port)
				: Short.toUnsignedInt(DEFAULT_PORT)
		);
		socketChannel.connect(socketAddress);
		socketChannel.configureBlocking(false);

		final TlsChannel tlsChannel = ClientTlsChannel.newBuilder(socketChannel, sslContext).build();
		asynchronousTlsChannel = new AsynchronousTlsChannel(tlsChannelGroup, tlsChannel, socketChannel);
		asynchronousTlsChannel.read(readBuffer, this, new ReadCompletionHandler());
	}

	@Override
	public void close() throws IOException {
		tlsChannelGroup.shutdown();
		asynchronousTlsChannel.close();
		socketChannel.close();
	}

	private static class ReadCompletionHandler implements CompletionHandler<Integer, MumbleClient> {

		@Override
		public void completed(final Integer result, final MumbleClient attachment) {
			LOGGER.debug("Read {} byte", result);
		}

		@Override
		public void failed(final Throwable exc, final MumbleClient attachment) {

		}
	}

	private static class TrustEverythingTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(final X509Certificate[] chain, final String authType) {

		}

		@Override
		public void checkServerTrusted(final X509Certificate[] chain, final String authType) {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}
}
