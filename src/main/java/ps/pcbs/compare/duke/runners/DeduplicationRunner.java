package ps.pcbs.compare.duke.runners;

import ps.pcbs.compare.Config;
import no.priv.garshol.duke.ConfigLoader;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.Processor;
import no.priv.garshol.duke.matchers.PrintMatchListener;

public class DeduplicationRunner {


	public DeduplicationRunner() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] argv) throws Exception {

		//String configFilePath = (argv[0] == null ? DEFAULT_CONFIGURATION_FILE_PATH : argv[0]);

		Configuration configuration = ConfigLoader.load(Config.CONFIGDEDUP);
		Processor processor = new Processor(configuration);
		processor.addMatchListener(new PrintMatchListener(true, true, true, false, configuration.getProperties(), false));
		processor.deduplicate();
		processor.close();
	}
}
