package ps.pcbs.compare.duke.cleaners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ps.pcbs.compare.Config;
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
		String compositeNumber;
		
		if(value.startsWith("02/")) value=value.replace("/", "");
		
		if(value.matches("([0-9]{7}|[0-9]{9}|[0-9]{10})\\s([0-9]{9}|[0-9]{10})")){
			if(value.matches("([0-9]{10})\\s([0-9]{9}|[0-9]{10})")){
			return value.substring(0,10)+Config.DEFAULT_TOKEN_SEPARATOR+value.substring(11);
			}
			if(value.matches("([0-9]{9})\\s([0-9]{9}|[0-9]{10})")){
				return value.substring(0,9)+Config.DEFAULT_TOKEN_SEPARATOR+value.substring(10);
				}
			if(value.matches("([0-9]{7})\\s([0-9]{9}|[0-9]{10})")){
				return "02"+value.substring(0,7)+Config.DEFAULT_TOKEN_SEPARATOR+value.substring(8);
				}
		}

		String trimmed = value.trim().replaceAll(" ", "")
				.replaceAll("\\p{InArabic}+", "");
		
		Matcher matcher = pattern.matcher(trimmed);
		
		
		
		if (matcher.matches())
			return "0" + trimmed.substring(0);

		if (trimmed.matches("(22|23|24|26|27|28|29)[0-9]{5}")) {
			if (value.startsWith("23") | value.startsWith("27")
					| value.startsWith("29") | value.startsWith("22")) {
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
					+ trimmed.replaceAll("[^0-9]","");

		if (trimmed.matches("(23|24|27|29)[0-9]{5}[^0-9][0-9]")) {

			if (value.startsWith("24")) {
				compositeNumber = "0" + trimmed.substring(0, 7)+ trimmed.charAt(8);
//				"04"+  compositeNumber = compositeNumber
//						+ Config.DEFAULT_TOKEN_SEPARATOR
//						+ compositeNumber.substring(0, 8) + trimmed.charAt(8);

				return compositeNumber;
			}

			else {
				compositeNumber = "02" + trimmed.substring(0, 7);
				compositeNumber = compositeNumber
						+ Config.DEFAULT_TOKEN_SEPARATOR
						+ compositeNumber.substring(0, 8) + trimmed.charAt(8);

				return compositeNumber;
			}

		}

		if (trimmed.matches("29[0-9]{5}[^0-9][0-9][^0-9][0-9]")) {
			compositeNumber = "02" + trimmed.substring(0, 7);
			compositeNumber = compositeNumber + Config.DEFAULT_TOKEN_SEPARATOR
					+ compositeNumber.substring(0, 8) + trimmed.charAt(8);
			compositeNumber = compositeNumber + Config.DEFAULT_TOKEN_SEPARATOR
					+ compositeNumber.substring(0, 8) + trimmed.charAt(10);
			return compositeNumber;
		}

		if (trimmed.matches("(22|25|27)[0-9]{5}[^0-9](24|27)[0-9]{5}")) {
			return "02" + trimmed.substring(0, 7)+Config.DEFAULT_TOKEN_SEPARATOR+"02"+trimmed.substring(8);
		}
		if (trimmed.matches("29[0-9]{5}[^0-9](059)[0-9]{7}")) {
			return "02" + trimmed.substring(0, 7)+Config.DEFAULT_TOKEN_SEPARATOR+trimmed.substring(8);
		}

		if (trimmed.matches("29[0-9]{5}[^0-9]29[0-9]{5}"))
			return "02" + trimmed.substring(0, 7)+Config.DEFAULT_TOKEN_SEPARATOR+"02"+trimmed.substring(8);

		if (trimmed.matches("[^0-9]29[0-9]{5}29[0-9]{5}"))
			return "02" + trimmed.substring(1, 8)+Config.DEFAULT_TOKEN_SEPARATOR+"02"+trimmed.substring(8);

		if (trimmed.matches("(1599|1544)[0-9]{6}"))
			return "0" + trimmed.substring(1);

		if (trimmed.matches("(02)[^0-9][0-9]{7}"))
			return "02" + trimmed.substring(2).replaceAll("[^0-9]","");

		if (trimmed.matches("(0599)[^0-9][0-9]{6}"))
			return trimmed.replaceAll("[^0-9]","");

		if (trimmed.matches("(05990599)[0-9]{6}"))
			return trimmed.substring(4);

		if (trimmed.matches("[0-9]{10}[^0-9]"))
			return trimmed.replaceAll("[^0-9]","");
		;

		if (trimmed.length() == 11) {
			if (value.startsWith("05*"))
				return "05" + trimmed.substring(3);
		}

		return trimmed;

	}
}
