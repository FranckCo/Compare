package ps.pcbs.compare.duke.cleaners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.priv.garshol.duke.Cleaner;

/**
 * Constructs a Phone Number <code>Cleaner</code>. Implements the specification
 * supplied by CT.
 * 
 * @author Java/CNIO
 *
 */
public class PhoneCleaner implements Cleaner {

	Pattern pattern = Pattern
			.compile("5(22|62|68|69|92|95|97|98|99)[0-9]{6}|22(4|9)[0-9]{5}");

	@Override
	public String clean(String value) {

		String trimmed = value.trim().replaceAll(" ", "")
				.replaceAll("\\p{InArabic}+", "");
		Matcher matcher = pattern.matcher(trimmed);

		if (matcher.matches())
			return "0" + trimmed.substring(0);

		if (trimmed.matches("(23|24|26|27|28|29)[0-9]{5}")) {
			if (value.startsWith("23") | value.startsWith("27")
					| value.startsWith("29")) {
				return "02" + trimmed;
			}
			if (value.startsWith("24"))
				return "04" + trimmed;
			if (value.startsWith("26"))
				return "09" + trimmed;
			if (value.startsWith("28"))
				return "08" + trimmed;

		}
		if (trimmed.matches("29[0-9]{5}[^0-9]"))
			return "02"
					+ trimmed.replace("/", "").replace(".", "")
							.replace("*", "").replace("-", "");

		if (trimmed.matches("(23|24|27|29)[0-9]{5}[^0-9][0-9]")) {

			if (value.startsWith("24")) {
				return "04" + trimmed.substring(0, 7);
			}

			else {
				return "02" + trimmed.substring(0, 7);
			}

		}

		if (trimmed.matches("29[0-9]{5}[^0-9][0-9][^0-9][0-9]"))
			return "02" + trimmed.substring(0, 7);

		if (trimmed.matches("(22|25|27)[0-9]{5}[^0-9](24|27)[0-9]{5}")) {
			return "02" + trimmed.substring(0, 7);
		}
		if (trimmed.matches("29[0-9]{5}[^0-9](059)[0-9]{7}")) {
			return "02" + trimmed.substring(0, 7);
		}

		if (trimmed.matches("29[0-9]{5}[^0-9]29[0-9]{5}"))
			return "02" + trimmed.substring(0, 7);

		if (trimmed.matches("[^0-9]29[0-9]{5}29[0-9]{5}"))
			return "02" + trimmed.substring(1, 8);

		if (trimmed.matches("(1599|1544)[0-9]{6}"))
			return "0" + trimmed.substring(1);

		if (trimmed.matches("(02)[^0-9][0-9]{7}"))
			return "02" + trimmed.substring(2).replace("-", "");

		if (trimmed.matches("(0599)[^0-9][0-9]{6}"))
			return "05" + trimmed.substring(2).replace("-", "");

		if (trimmed.matches("(05990599)[0-9]{6}"))
			return trimmed.substring(4);

		if (trimmed.matches("[0-9]{10}[^0-9]"))
			return trimmed.replace("/", "").replace(".", "").replace("*", "")
					.replace("-", "");
		;

		if (trimmed.length() == 11) {
			if (value.startsWith("05*"))
				return "05" + trimmed.substring(3);
		}

		return trimmed;

	}
}
