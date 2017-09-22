package fr.ginguene.onepoint.hackathon.scoring;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;

public class StarterPlaneteScoring implements IPlaneteScoring {

	private int distanceRatio = 200;
	private int tauxCroissanceRatio = 20;
	private int populationRatio = 150;

	public int getScore(Carte carte, Planete source, Planete destination) {

		int score = 0;

		score += destination.getTauxCroissance() * tauxCroissanceRatio;
		score += source.calcDistance(destination) * distanceRatio;
		score += destination.getPopulation() * populationRatio;

		return score;

	}
}
