package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;

public abstract class AbstractStrategie {

	private final boolean isDebug;

	protected AbstractStrategie(boolean isDebug) {
		this.isDebug = isDebug;
	}

	protected AbstractStrategie() {
		this.isDebug = false;
	}

	public abstract boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore);

	protected void trace(String message) {
		if (isDebug) {
			System.out.println(message);
		}
	}

}
