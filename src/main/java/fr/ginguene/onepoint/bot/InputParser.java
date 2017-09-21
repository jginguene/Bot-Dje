package fr.ginguene.onepoint.bot;

public class InputParser {
	
	public Carte parse(String input){
		
		Carte map = new Carte();
		String [] lines = input.split("\n");
		for (String line : lines ){
			line = line.replaceAll(" *", " ");
			String[] attributes = line.split(" ");
			
			String type = attributes[0];
			
			switch(type){
				case "P":
					Planete planete = new Planete (attributes);
					map.addPlanete(planete);
					break;				
				
			
			
			
			}
			
		}
			
		return map;
		
		
		/*
		Format d'une ligne de type Flotte :
		F <Propriétaire:int> <Vaisseaux:int> <IdSource:int> <IdDestination:int> <ToursTotal:int> <ToursRestants:int>

		Format d'une ligne de type Configuration :
		C <tour:int> <toursMaximum:int> <IdentifiantPartie:long> */
	}

}
