import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainClass {
	
	public static void main(String[] args) {
		String[] dmiLinks = {
					"http://web.dmi.unict.it/corsi/l-31/programmi", 
					"http://web.dmi.unict.it/corsi/l-35/programmi", 
					"http://web.dmi.unict.it/corsi/lm-18/programmi", 
					"http://web.dmi.unict.it/corsi/lm-40/programmi", 
				};
		
		for(String cds : dmiLinks) {
			Document doc = null;
			try {
				doc = Jsoup.connect(cds).ignoreHttpErrors(true).get();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
			Element table = doc.getElementById("tbl_small_font");
			String anno = "";
			for(Element tr : table.getElementsByTag("tr")) {
				if(tr.hasClass("nbr"))
					continue;
				else if(tr.getElementsByTag("td").hasClass("wog"))
					anno = tr.text();
				else {
					Elements tds = tr.children();
					String nome = tds.get(0).getElementsByTag("a").text();
					String cfu = tds.get(1).text();
					String semestre = tds.get(2).text();
					System.out.println("Materia: " + nome + "\t Anno: " + anno + "\t Semestre: " + semestre);
				}
			}
		}
		System.out.println("Terminated");
	
	}

}
