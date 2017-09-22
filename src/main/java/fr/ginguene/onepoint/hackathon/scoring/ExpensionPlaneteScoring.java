package fr.ginguene.onepoint.hackathon.scoring;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.Planete;

public class ExpensionPlaneteScoring implements IPlaneteScoring {

	private int nbTourRatio = -200;
	private int tauxCroissanceRatio = 20;
	private int populationRatio = -150;

	private int[] ennemiFlotteRatio = new int[] { 512, 256, 128, 64, 32, 16, 8, 4, 2, 1 };
	private int[] amiFlotteRatio = new int[] { -512, -256, -128, -64, -32, -16, -8, -4, -2, -1 };

	public int getScore(Carte carte, Planete source, Planete destination) {

		int score = 0;

		if (destination.getProprietaire() == Constantes.MOI) {
			score -= populationRatio * destination.getPopulation();
		} else {
			score += populationRatio * destination.getPopulation();
		}

		/*
		 * for (Flotte flotte : carte.getFlotte(Constantes.MOI)) { if
		 * (flotte.getToursRestants() - 1 < amiFlotteRatio.length) { score +=
		 * amiFlotteRatio[flotte.getToursRestants() - 1] *
		 * flotte.getVaisseaux(); } }
		 * 
		 * for (Flotte flotte : carte.getFlotte(Constantes.Ennemi)) { if
		 * (flotte.getToursRestants() - 1 < ennemiFlotteRatio.length) { score +=
		 * ennemiFlotteRatio[flotte.getToursRestants() - 1] *
		 * flotte.getVaisseaux(); } }
		 */

		score += destination.getTauxCroissance() * tauxCroissanceRatio;
		score += carte.getTrajetNbTour(source, destination) * nbTourRatio;

		System.out.println("ExpensionPlaneteScoring " + source.getId() + " to " + destination.getId() + " => " + score);

		return score;

	}

}
