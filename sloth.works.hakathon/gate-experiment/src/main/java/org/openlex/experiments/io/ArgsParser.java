package org.openlex.experiments.io;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgsParser implements GATEPathBundle {
	private String pathToGATE;
	private String pathToGATEPlugins;
	private String pathToResources;
	private String pathToFileResources;
	private String pathToResultOutput;
	private String pathToOriginalOutput;
	private String[] args;

	public ArgsParser(String[] args) {
		this.args = args;
	}

	public boolean handleArgs() {
		Options options = new Options();
		Option gatePath = new Option("p", "gatepath", true, "GATE path");
		gatePath.setRequired(true);
		options.addOption(gatePath);
		Option resourcePath = new Option("r", "resorcespath", true, "Resources path");
		resourcePath.setRequired(true);
		options.addOption(resourcePath);

		CommandLineParser clp = new DefaultParser();
		HelpFormatter hlpForm = new HelpFormatter();

		CommandLine cmd;

		try {
			cmd = clp.parse(options, args);
			pathToGATE = cmd.getOptionValue("gatepath");
			pathToGATEPlugins = pathToGATE + "/plugins";

			pathToResources = cmd.getOptionValue("resorcespath");
			pathToFileResources = "file:///" + pathToResources;
			pathToResultOutput = pathToResources + "results/";
			pathToOriginalOutput = pathToResources + "original/";
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			hlpForm.printHelp("Annotated Corpus Reader", options);

			return false;
		}
		return true;
	}

	@Override
	public String getPathToGATE() {
		return pathToGATE;
	}

	@Override
	public String getPathToGATEPlugins() {
		return pathToGATEPlugins;
	}

	@Override
	public String getPathToResources() {
		return pathToResources;
	}

	@Override
	public String getPathToFileResources() {
		return pathToFileResources;
	}

	@Override
	public String getPathToResultOutput() {
		return pathToResultOutput;
	}

	@Override
	public String getPathToOriginalOutput() {
		return pathToOriginalOutput;
	}

	public String[] getArgs() {
		return args;
	}

}
