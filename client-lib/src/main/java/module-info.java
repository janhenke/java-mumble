module de.taujhe.javamumble.clientlib {
	exports de.taujhe.javamumble.client;
	requires de.taujhe.javamumble.common;

	requires jsr305;
	requires org.slf4j;
	requires tls.channel;
}
