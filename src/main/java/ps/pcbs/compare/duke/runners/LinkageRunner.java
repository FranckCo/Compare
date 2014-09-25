package ps.pcbs.compare.duke.runners;

import no.priv.garshol.duke.ConfigLoader;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.Processor;
import ps.pcbs.compare.duke.listeners.FileMatchListener;

public class LinkageRunner {

	public static String DEFAULT_CONFIGURATION_FILE_PATH = "src/main/resources/ramallah-census-tel-cfg.xml";
	// TODO Use a Configuration object

	public LinkageRunner() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] argv) throws Exception {

		//String configFilePath = (argv[0] == null ? DEFAULT_CONFIGURATION_FILE_PATH : argv[0]);

		Configuration configuration = ConfigLoader.load(DEFAULT_CONFIGURATION_FILE_PATH);
		Processor processor = new Processor(configuration);
		FileMatchListener listener = new FileMatchListener("src/test/resources/ramallah-census-tel-matches.xml", DEFAULT_CONFIGURATION_FILE_PATH, false, false, true, true);
		processor.addMatchListener(listener);
		processor.link();
		processor.close();
	}
}
