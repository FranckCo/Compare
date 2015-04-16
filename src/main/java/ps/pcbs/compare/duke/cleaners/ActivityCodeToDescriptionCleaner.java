package ps.pcbs.compare.duke.cleaners;

import ps.pcbs.compare.utils.ISICMap;
import no.priv.garshol.duke.Cleaner;

public class ActivityCodeToDescriptionCleaner  implements Cleaner{
	private static ISICMap isic= new ISICMap();
	public ActivityCodeToDescriptionCleaner() {
		// TODO Auto-generated constructor stub
		isic.loadMap();
		
	}

	@Override
	public String clean(String value) {
		// TODO Auto-generated method stub
		return isic.getIsicCorrespondance().get(value);
		
	}

}
