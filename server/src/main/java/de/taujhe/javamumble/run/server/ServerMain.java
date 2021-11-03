package de.taujhe.javamumble.run.server;

import de.taujhe.javamumble.server.MumbleServer;

/**
 * Class containing the main method for the standalone Mumble server application.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public final class ServerMain {

	public static void main(String[] args) {
		final MumbleServer mumbleServer = new MumbleServer();
	}
}
