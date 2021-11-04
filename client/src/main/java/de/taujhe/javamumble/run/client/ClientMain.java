package de.taujhe.javamumble.run.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.taujhe.javamumble.client.MumbleClient;

/**
 * Class containing the main method for the standalone Mumble client application.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public final class ClientMain {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientMain.class);

	public static void main(String[] args) {

		final CommandLineParser commandLineParser = new DefaultParser();
		final Options options = new Options();
		options.addOption(Option.builder("h").longOpt("help").desc("Display this help message").build());
		options.addOption(Option.builder("s")
								.longOpt("server")
								.argName("server-name")
								.desc("DNS name or IP address of the Mumble server")
								.hasArg()
								.required()
								.build());
		options.addOption(Option.builder("p")
								.longOpt("port")
								.argName("port")
								.desc("Port number of the Mumble server")
								.hasArg()
								.type(Short.class)
								.build());
		options.addOption(Option.builder()
								.longOpt("ignore-certs")
								.desc("Ignore TLS certificate validation errors")
								.build());

		CommandLine commandLine = null;
		try {
			commandLine = commandLineParser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		if (commandLine.hasOption('h')) {
			final HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp("java -jar java-mumble-client.jar", options);
		}

		if (!commandLine.hasOption('s')) {
			throw new IllegalStateException("No server name specified.");
		}
		final String server = commandLine.getOptionValue('s');
		final Short port = commandLine.hasOption('p') ? Short.valueOf(commandLine.getOptionValue('p')) : null;

		try (
			final MumbleClient mumbleClient = new MumbleClient(Boolean.parseBoolean(commandLine.getOptionValue(
				"ignore-certs")))
		) {
			mumbleClient.connect(server, port, "Java-Mumble-User", null, null);

			// TODO: Add proper waiting, the client runs async in the background
			System.in.read();
		} catch (final IOException | NoSuchAlgorithmException | KeyManagementException e) {
			LOGGER.error("Exception in client.", e);
		}
	}
}
