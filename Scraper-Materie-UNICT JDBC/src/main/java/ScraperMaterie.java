import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScraperMaterie {
	private String[] links;
	
	ScraperMaterie(String[] links){
		this.links = links;
	}
	
	public void scrape() {
		for(String link : links) {
			scrape(link);
		}
	}

	public static List<Materia> scrape(String link) {
		List<Materia> materie = new ArrayList<>();
		Document doc = null;
		try {
			doc = Jsoup.connect(link).ignoreHttpErrors(true).get();
		} catch (IOException e) {
			e.printStackTrace();
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
				int id = Integer.parseInt((tds.get(0).getElementsByTag("a").attr("href").toString().split("=")[1]));
				String nome = tds.get(0).getElementsByTag("a").text();
				int cfu = Integer.parseInt(tds.get(1).text());
				String semestre = tds.get(2).text();
				//System.out.println("ID: " + id + "\t Materia: " + nome + "\t Anno: " + anno.charAt(0) + "\t Semestre: " + semestre);
				materie.add(new Materia(id, nome, Character.getNumericValue(anno.charAt(0)), semestre));
			}
		}
		return materie;
	}
	
	private static void semesterConversion(String semester) {
		//TODO: convert dirty semester
		//example 1° should be I
	}
}
