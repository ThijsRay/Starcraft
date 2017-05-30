package eisbw.percepts.perceivers;

import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.DefensiveMatrixPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.ResourcesPercept;
import eisbw.percepts.SelfPercept;
import eisbw.percepts.SpaceProvidedPercept;
import eisbw.percepts.StatusPercept;
import eisbw.percepts.UnitLoadedPercept;
import eisbw.units.ConditionHandler;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Danny & Harm - The perceiver which handles all the generic percepts.
 *
 */
public class GenericUnitPerceiver extends UnitPerceiver {

	/**
	 * @param api
	 *            The BWAPI.
	 * @param unit
	 *            The perceiving unit.
	 */
	public GenericUnitPerceiver(JNIBWAPI api, Unit unit) {
		super(api, unit);
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {

		defenceMatrixPercept(toReturn);
		resourcesPercept(toReturn);
		selfPercept(toReturn);
		statusPercept(toReturn);
		
		if (unit.getType().getSpaceProvided() > 0) {
			List<Unit> loadedUnits = unit.getLoadedUnits();
			spaceProvidedPercept(toReturn, loadedUnits);
			unitLoadedPercept(toReturn, loadedUnits);
		}

		return toReturn;
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void statusPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> percepts = new HashSet<>();
		ConditionHandler conditionHandler = new ConditionHandler(api, unit);

		percepts.add(new StatusPercept(unit.getHitPoints(), unit.getShields(), unit.getEnergy(),
				conditionHandler.getConditions(), unit.getPosition().getBX(), unit.getPosition().getBY()));

		toReturn.put(new PerceptFilter(Percepts.STATUS, Filter.Type.ON_CHANGE), percepts);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void selfPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> percepts = new HashSet<>();
		UnitType type = unit.getType();

		percepts.add(new SelfPercept(unit.getID(), BwapiUtility.getUnitType(unit), type.getMaxHitPoints(), type.getMaxShields(),
				type.getMaxEnergy()));

		toReturn.put(new PerceptFilter(Percepts.SELF, Filter.Type.ONCE), percepts);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void resourcesPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> percepts = new HashSet<>();

		percepts.add(new ResourcesPercept(api.getSelf().getMinerals(), api.getSelf().getGas(),
				api.getSelf().getSupplyUsed(), api.getSelf().getSupplyTotal()));

		toReturn.put(new PerceptFilter(Percepts.RESOURCES, Filter.Type.ON_CHANGE), percepts);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void defenceMatrixPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> percepts = new HashSet<>();

		if (unit.isDefenseMatrixed()) {
			percepts.add(new DefensiveMatrixPercept(unit.getDefenseMatrixPoints()));
		}

		toReturn.put(new PerceptFilter(Percepts.DEFENSIVEMATRIX, Filter.Type.ALWAYS), percepts);
	}
	
	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 * @param loadedUnits
	 * 			  The loaded units
	 */
	private void unitLoadedPercept(Map<PerceptFilter, Set<Percept>> toReturn, List<Unit> loadedUnits) {
		Set<Percept> percepts = new HashSet<>();
		for (Unit u : loadedUnits) {
			if (u != null) {
				percepts.add(new UnitLoadedPercept(u.getID(), u.getType().getName()));
			}
		}
		toReturn.put(new PerceptFilter(Percepts.UNITLOADED, Filter.Type.ALWAYS), percepts);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 * @param loadedUnits
	 * 			  The loaded units
	 */
	private void spaceProvidedPercept(Map<PerceptFilter, Set<Percept>> toReturn, List<Unit> loadedUnits) {
		Set<Percept> percepts = new HashSet<>();
		percepts.add(new SpaceProvidedPercept(loadedUnits.size(), unit.getType().getSpaceProvided()));
		toReturn.put(new PerceptFilter(Percepts.SPACEPROVIDED, Filter.Type.ON_CHANGE), percepts);
	}


}
