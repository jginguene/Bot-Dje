package fr.ginguene.onepoint.hackathon;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import fr.ginguene.onepoint.hackathon.ordre.Ordre;

public class Response {

	private List<Ordre> ordres = new ArrayList<Ordre>();

	public void addOrdre(Ordre ordre) {
		ordres.add(ordre);
	}

	public List<Ordre> getOrdres() {
		return this.ordres;
	}

	public String toString() {

		System.out.println("Nb Ordres:" + ordres.size());
		StringJoiner joiner = new StringJoiner("\n");
		for (Ordre ordre : ordres) {
			joiner.add(ordre.asString());
		}

		return joiner.toString();
	}

}
