package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;

public class BombeStrategie2 extends AbstractStrategie {

	public BombeStrategie2(boolean isDebug) {
		super(isDebug);
	}

	public BombeStrategie2() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {
		return false;

	}

}
