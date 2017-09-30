package fr.ginguene.onepoint.hackathon.action;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class BombardeStrategie extends AbstractStrategie {

	public BombardeStrategie(boolean isDebug) {
		super(isDebug);
	}

	public BombardeStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (source.getPopulation() > 140 || source.getPopulation() > source.getPopulationMax() - 20) {

			int nbEnnemie = 0;
			for (Planete aPlanete : carte.getVoisines(source, 6)) {
				if (aPlanete.getStatus() != PlaneteStatus.Amie) {
					nbEnnemie++;
				}
			}

			int nbVaisseau = source.getPopulation() - 60;

			if (nbVaisseau < 0 || (nbEnnemie == 0 && carte.getFlottesEnnemie(source.getId()) == 0)) {
				nbVaisseau = source.getPopulation() - 10;
			}

			trace("Lancement de la bombe: " + source + " => nbVaisseau:" + nbVaisseau);

			Planete destination = null;
			if (isOptimizingScore) {
				List<Planete> neutres = carte.getPlanetes(Constantes.NEUTRE);
				if (neutres.size() != 0) {
					destination = neutres.get(0);
				}
			} else {
				destination = getDestinationForBomb(carte, source, nbVaisseau);
			}

			if (destination != null) {
				EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
				source.remPopulation(nbVaisseau);
				response.addOrdre(ordre);
				carte.addFlotte(ordre.getFlotte());
				return true;
			}
		}

		return false;
	}

	private Planete getDestinationForBomb(Carte carte, Planete source, int bombSize) {

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getProprietaire() != Constantes.AMI
					&& carte.getMesFlottes(aPlanete.getId()) < aPlanete.getPopulationMax()) {
				return aPlanete;
			}
		}
		return null;

	}

}
