package ps.pcbs.compare.duke.runners;

import java.util.Map;

import org.apache.log4j.Logger;

import no.priv.garshol.duke.ConfigLoader;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.Processor;
import no.priv.garshol.duke.matchers.PrintMatchListener;
import ps.pcbs.compare.Config;
import ps.pcbs.compare.duke.listeners.FileMatchListener;
import ps.pcbs.compare.duke.listeners.FileProcessListener;
import ps.pcbs.compare.utils.ExtractMultipleMatches;

public class LinkageRunner {

	public LinkageRunner() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] argv) throws Exception {

		// String configFilePath = (argv[0] == null ?
		// DEFAULT_CONFIGURATION_FILE_PATH : argv[0]);

		Configuration configuration = ConfigLoader.load(Config.CONFIGLINK);
		Processor processor = new Processor(configuration);
		// FileMatchListener listener = new FileMatchListener(Config.BILAN,
		// Config.CONFIGLINK, true, false, true, true);
		FileProcessListener listener = new FileProcessListener(Config.BILAN,
				Config.CONFIGLINK, true, false, true, true);
		PrintMatchListener listenerConsole = new PrintMatchListener(false,
				false, true, true, configuration.getProperties(), true);
		processor.addMatchListener(listener);
		processor.addMatchListener(listenerConsole);
		
		if (Config.MATCHALL.equals("true")) {
			logger.info("matching multiple records from file 2 per record from file 1: ");
			processor.link(configuration.getDataSources(1),
					configuration.getDataSources(2), true, 49999);
		} else {
			logger.info("matching only one record from file 2 per record from file 1: ");
			processor.link(configuration.getDataSources(1),
					configuration.getDataSources(2), false, 100000);
		}
		// processor.link();
		processor.close();
		
		// need to add here the filter multiple matches
		ExtractMultipleMatches obj = new ExtractMultipleMatches();
		String csvFile = Config.BILAN;
		// Map <String,Integer> linestokeep=obj.run(csvFile,18);
		// obj.keep(csvFile,linestokeep,18);
		// Map <String,Integer> linestokeep=obj.run(csvFile,11);
		// obj.keep(csvFile,linestokeep,11);
		Map<String, Integer> linestokeep = obj.run(csvFile, 2);
		obj.keep(csvFile, linestokeep, 2);
	}
	
	
	private static final Logger logger = Logger.getLogger(LinkageRunner.class);
}
