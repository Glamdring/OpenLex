package org.openlex.experiments.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigParser implements GATEPathBundle {
	private String pathToGATE;
	private String pathToGATEPlugins;
	private String pathToResources;
	private String pathToFileResources;
	private String pathToResultOutput;
	private String pathToOriginalOutput;

	public boolean parseConfig() {
		Properties configPaths = new Properties();
		try {
			FileInputStream configIn = new FileInputStream("config.properties");
			configPaths.load(configIn);
			configIn.close();
			pathToGATE = configPaths.getProperty("gatepath");
			pathToResources = configPaths.getProperty("resourcespath");
			pathToGATEPlugins = pathToGATE + "/plugins";
			pathToFileResources = "file:///" + pathToResources;
			pathToResultOutput = pathToResources + "results/";
			pathToOriginalOutput = pathToResources + "original/";
		} catch (FileNotFoundException e) {
			System.out.println("Config file not found in " + System.getProperty("user.dir"));
			return false;
		} catch (IOException e) {
			System.out.println(
					"The config file must contain a correct path for 'gatepath' and 'resourcespath' in a JAVA properties format.");
			e.printStackTrace();
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
}
