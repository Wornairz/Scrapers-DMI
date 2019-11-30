package it.unict.scrapersmaterie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MaterieController {
	
	@Autowired
	MaterieScraper materieService;
	
	@GetMapping("/")
	public ResponseEntity<List<Materia>> getMaterie() {
		List<Materia> m = materieService.getMaterie();
		return new ResponseEntity<List<Materia>>(m, HttpStatus.OK);
	}
}
