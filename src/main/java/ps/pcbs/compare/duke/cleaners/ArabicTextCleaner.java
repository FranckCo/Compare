package ps.pcbs.compare.duke.cleaners;

import no.priv.garshol.duke.Cleaner;

public class ArabicTextCleaner implements Cleaner {

	public ArabicTextCleaner() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String clean(String value) {

		if (value.contains("&") || value.contains("4")) {
			if (value.trim().substring(0, 2).matches("\\p{InArabic}+")) {
				value = value.replace("&", "و");
				if (value.contains("4"))
					value = " فور " + value.replace("4", "");
			} else {
				value = value.replace("&", "and");
				value = value.replace("4", "for");
			}
		}
		
		value=value.replaceAll("ة", "ه");
		
		value = value.replaceAll("أ|إ|آ", "ا");
		value = value.replaceAll("لآ|لأ|لإ", "لا");
		value = value.replace("ابو ", "ابو").replace("أبو ", "أبو").replace("عبد ", "عبد")
				.replace("دير ", "دير").replace("بير ", "بير").replace("كفر ", "كفر")
				.replace("عين ", "عين").replace("نيو ", "نيو").replace("بيت ", "بيت");


			value = value.replace("new look", "newlook")
					.replace("No name", "Noname")
					.replace("play station", "playstation")
					.replace("sea sons", "seasons").replace("too u", "toou")
					.replace("book stores", "bookstores");
			
			value = value.replace("سوبر ماركت", "سوبرماركت")
					.replace("ميني ماركت", "مينيماركت")
					.replace("لتاجير السيارات", "لتاجيرالسيارات")
					.replace("لتأجير السيارات", "لتأجير السيارات")
					.replace("كمبيو نت", "كمبيونت")
					.replace("اند شوب", "اندشوب").replace("بلي ستيشن", "بليستيشن")
					.replace("بانور اما", "بانوراما")
					.replace("جراند فارم", "جراندفارم").replace("كوزمو بولي", "كوزموبولي").replace("جلو بال", "جلوبال")
					.replace("سبيس تون", "سبيستون")
					.replace("ستا لايت", "ستالايت").replace("سي جي", "سيجي").replace("تكنو بال", "تكنوبال")
					.replace("سي دي", "سيدي").replace("انتر بال", "انتربال").replace("انتر ناشونال", "انترناشونال")
					.replace("اي فون", "ايفون").replace("او تو", "اوتو").replace("اوفر سيز", "اوفرسيز")
					.replace("يا هلا", "ياهلا").replace("بلو سكاي", "بلوسكاي").replace("بي تي سي", "بيتيسي").replace("فن تاستك", "فنتاستك").replace("فور يو", "فوريو").replace("دراي كلين", "درايكلين").replace("فودا فون", "فودافون");
			
			value = value.replace(" و ", " و");
			
//			System.out.println("value cleaned "+value.trim()
//					.replaceAll("[^\\p{InArabic}&&[^\\p{L}]&&[^\\s]]", "")
//					.toLowerCase().trim());

			return value.trim()
					.replaceAll("[^\\p{InArabic}&&[^\\p{L}]&&[^\\s]]", "")
					.toLowerCase().trim();
	}

}
