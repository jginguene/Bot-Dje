package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class Acharnement extends AbstractStrategie {

	private int destinationId = -1;

	public Acharnement(boolean isDebug) {
		super(isDebug);
	}

	public Acharnement() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		System.out.println("acharnement " + source + " -> destinationId:" + destinationId);

		Planete destination = null;

		if (destinationId > 0) {
			destination = carte.getPlanete(destinationId);
			if (destination.getStatus() == PlaneteStatus.Amie) {
				destination = null;
				destinationId = -1;
			}
		}

		if (destination == null) {
			destination = chooseTarget(source, carte);
			System.out.println("new  destinationId:" + destination.getId());
			destinationId = destination.getId();
		}

		int nbVaisseau = source.getPopulation() - carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source) - 1;

		System.out.println("nbVaisseau:" + nbVaisseau);
		System.out.println(" carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source) :"
				+ carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source));
		System.out.println(" source.getPopulation() :" + source.getPopulation());

		if (nbVaisseau > 3) {
			EnvoiFlotte ordre = new EnvoiFlotte(carte, source, destination, nbVaisseau);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			return true;
		}

		return false;
	}

	private Planete chooseTarget(Planete source, Carte carte) {

		// On choisit la planete ennemie la plus proche
		return carte.getEnnemiLaPlusProche(source);

	}

}