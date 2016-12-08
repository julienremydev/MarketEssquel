package agents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class main {

	public static void main(String[] args) {
		String string = "01-01-"+(new Date().getYear()+1900);
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date date = format.parse(string);
			System.out.println(date);
			System.out.println(new Date());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 // Sat Jan 02 00:00:00 GMT 2010

	}

}
