package it.unict.scrapersmaterie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MaterieScraper {

	private List<Materia> materie;
	private String[] links = { "http://web.dmi.unict.it/corsi/l-31/programmi",
			"http://web.dmi.unict.it/corsi/l-35/programmi", "http://web.dmi.unict.it/corsi/lm-18/programmi",
			"http://web.dmi.unict.it/corsi/lm-40/programmi", };

	public List<Materia> getMaterie() {
		return materie;
	}

	MaterieScraper() {
		materie = new ArrayList<Materia>();
		scrape();
	}

	private void scrape() {
		for (String link : links) {
			Document doc = null;
			try {
				doc = Jsoup.connect(link).ignoreHttpErrors(true).get();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Element table = doc.getElementById("tbl_small_font");
			String anno = "";
			for (Element tr : table.getElementsByTag("tr")) {
				if (tr.hasClass("nbr"))
					continue;
				else if (tr.getElementsByTag("td").hasClass("wog"))
					anno = tr.text();
				else {
					Elements tds = tr.children();
					int id = Integer.parseInt((tds.get(0).getElementsByTag("a").attr("href").toString().split("=")[1]));
					String nome = tds.get(0).getElementsByTag("a").text();
					int cfu = Integer.parseInt(tds.get(1).text());
					String semestre = tds.get(2).text();
					// System.out.println("ID: " + id + "\t Materia: " + nome + "\t Anno: " +
					// anno.charAt(0) + "\t Semestre: " + semestre);
					Materia m = new Materia(id, nome, Character.getNumericValue(anno.charAt(0)), semesterConversion(semestre));
					materie.add(m);
				}
			}
		}
	}

	private String semesterConversion(String rawSemester) {
		String convertedSemester = "0";
		switch (rawSemester) {
		case "1°":
		case "A":
			convertedSemester = "I";
			break;
		case "2°":
		case "B":
			convertedSemester = "II";
			break;
		case "1° e 2°":
			convertedSemester = "I-II";
			break;
		}
		return convertedSemester;
	}
}
