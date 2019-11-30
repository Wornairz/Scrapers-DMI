package it.unict.scrapersmaterie;

public class Materia {
	private int id;
	private String nome;
	private int anno;
	private String semestre;
	
	public Materia(int id, String nome, int anno, String semestre) {
		this.id = id;
		this.nome = nome;
		this.anno = anno;
		this.semestre = semestre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	
}
