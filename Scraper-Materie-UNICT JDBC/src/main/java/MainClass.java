import java.sql.SQLException;
import java.util.List;

public class MainClass {
	
	public static void main(String[] args) {
		DBUtility db = DBUtility.getInstance();
		
		String[] links = {
					"http://web.dmi.unict.it/corsi/l-31/programmi", 
					"http://web.dmi.unict.it/corsi/l-35/programmi", 
					"http://web.dmi.unict.it/corsi/lm-18/programmi", 
					"http://web.dmi.unict.it/corsi/lm-40/programmi", 
				};
		
		
		for(String link : links) {
			List<Materia> materie = ScraperMaterie.scrape(link);
			for(Materia materia : materie) {
				try {
					db.query("INSERT INTO Materie(id, nome, anno, semestre) "
							+ "VALUES(" + materia.getId() + ", \"" + materia.getNome() + "\", " + materia.getAnno() + ", \"" + materia.getSemestre() + "\")");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		try {
			db.query("SELECT * FROM Materie");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Terminated");
	
	}

}
