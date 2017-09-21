package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Response {
	
	private List<Ordre> ordres = new ArrayList<Ordre>();
	
	public void addOrdre(Ordre ordre){
		ordres.add(ordre);
	}
	
	
	public String toString(){
		 StringJoiner joiner = new StringJoiner("\n");
		 for (Ordre ordre: ordres){
			 joiner.add(ordre.toString());
		 }
	
		 
		return joiner.toString();
	}

}
