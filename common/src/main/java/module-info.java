module de.taujhe.javamumble.common {
	exports de.taujhe.javamumble.common.protocol.control;

	requires jdk.unsupported;
	requires org.slf4j;
	requires jsr305;

	requires com.google.protobuf;
	requires com.google.protobuf.util;
}
