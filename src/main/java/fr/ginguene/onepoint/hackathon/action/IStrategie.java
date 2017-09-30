package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;

public interface IStrategie {

	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore, boolean debug);

}
