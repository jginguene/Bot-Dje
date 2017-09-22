package fr.ginguene.onepoint.hackathon;

public class InputParser {

	public Carte parse(String input) {

		Carte carte = new Carte();

		String[] lines = input.split("\n");
		for (String line : lines) {

			line = line.replaceAll("  ", " ");
			String[] attributes = line.split(" ");

			String type = attributes[0];

			switch (type) {
			case "P":
				Planete planete = new Planete(attributes);
				carte.addPlanete(planete);
				break;

			case "F":
				Flotte flotte = new Flotte(attributes);
				carte.addFlotte(flotte);
				break;

			case "C":
				Configuration conf = new Configuration(attributes);
				carte.setConfiguration(conf);
				break;

			}

		}

		return carte;

		/*
		 * Format d'une ligne de type Flotte : F <Propriétaire:int>
		 * <Vaisseaux:int> <IdSource:int> <IdDestination:int> <ToursTotal:int>
		 * <ToursRestants:int>
		 * 
		 * Format d'une ligne de type Configuration : C <tour:int>
		 * <toursMaximum:int> <IdentifiantPartie:long>
		 */
	}

}
