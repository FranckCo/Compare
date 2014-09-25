package ps.pcbs.compare.duke.cleaners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.priv.garshol.duke.Cleaner;

/*
- number = 024xxxxx ==> number = 0224xxxxx  (not specified by PCBS)
- number = 029xxxxx ==> number = 0229xxxxx
- number = 24xxxxx ==> number = 0224xxxxx  (not specified by PCBS)
- number = 29xxxxx ==> number = 0229xxxxx
- number = 224xxxxx ==> number = 0224xxxxx  (not specified by PCBS)
- number = 229xxxxx ==> number = 0229xxxxx
- number = 522xxxxxx ==> number = 0522xxxxxx  (not specified by PCBS)
- number = 562xxxxxx ==> number = 0562xxxxxx
- number = 568xxxxxx ==> number = 0568xxxxxx
- number = 569xxxxxx ==> number = 0569xxxxxx
- number = 592xxxxxx ==> number = 0592xxxxxx
- number = 595xxxxxx ==> number = 0595xxxxxx
- number = 597xxxxxx ==> number = 0597xxxxxx
- number = 598xxxxxx ==> number = 0598xxxxxx
- number = 599xxxxxx ==> number = 0599xxxxxx
*/
public class PhoneCleaner implements Cleaner {

	Pattern pattern = Pattern.compile("5(22|62|68|69|92|95|97|98|99)[0-9]{6}|22(4|9)[0-9]{5}");

	@Override
	public String clean(String value) {

		String trimmed = value.trim();

		Matcher matcher = pattern.matcher(trimmed);
		if (matcher.matches()) return "0" + trimmed;

		if (trimmed.length() == 8) {
			if (value.startsWith("024")) return "0224" + trimmed.substring(3);
			if (value.startsWith("029")) return "0229" + trimmed.substring(3);
		}
		else if (trimmed.matches("(24|29)[0-9]{5}")) return "02" + trimmed;

		return trimmed;
	}
}
