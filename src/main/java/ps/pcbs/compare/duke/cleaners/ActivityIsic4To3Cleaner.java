package ps.pcbs.compare.duke.cleaners;

import ps.pcbs.compare.utils.ISIC4To3;
import no.priv.garshol.duke.Cleaner;

public class ActivityIsic4To3Cleaner implements Cleaner {
	private static ISIC4To3 isic = new ISIC4To3();

	public ActivityIsic4To3Cleaner() {
		// TODO Auto-generated constructor stub
		isic.loadMap();
	}

	@Override
	public String clean(String value) {
		// TODO Auto-generated method stub
		try {
			return isic.getIsicCorrespondance().get(value).getRight().toString();
		} catch (NullPointerException e) {return "";
		}

	}
}