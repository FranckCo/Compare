package ps.pcbs.compare.duke.runners;

import no.priv.garshol.duke.ConfigLoader;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.Processor;
import no.priv.garshol.duke.matchers.PrintMatchListener;

public class DeduplicationRunner {

	public static String DEFAULT_CONFIGURATION_FILE_PATH = "src/main/resources/census-cfg.xml";
	// TODO Use a Configuration object

	public DeduplicationRunner() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] argv) throws Exception {

		//String configFilePath = (argv[0] == null ? DEFAULT_CONFIGURATION_FILE_PATH : argv[0]);

		Configuration configuration = ConfigLoader.load(DEFAULT_CONFIGURATION_FILE_PATH);
		Processor processor = new Processor(configuration);
		processor.addMatchListener(new PrintMatchListener(true, true, true, false, configuration.getProperties(), false));
		processor.deduplicate();
		processor.close();
	}
}
