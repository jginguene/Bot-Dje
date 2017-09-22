package fr.ginguene.onepoint.hackathon.scoring;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;

public interface IPlaneteScoring {

	public int getScore(Carte carte, Planete source, Planete destination);

}
