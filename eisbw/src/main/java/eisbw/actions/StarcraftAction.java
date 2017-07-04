package eisbw.actions;

import eis.iilang.Action;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - Abstract class for all the actions.
 *
 */
public abstract class StarcraftAction {
	protected final JNIBWAPI api;

	/**
	 * The StarcraftAction constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public StarcraftAction(JNIBWAPI api) {
		this.api = api;
	}

	/**
	 * @param action
	 *            The evaluated action.
	 * @return A boolean which indicates whether the parameters of the action are
	 *         valid.
	 */
	public abstract boolean isValid(Action action);

	/**
	 * @param unit
	 *            The unit performing the action.
	 * @param action
	 *            The evaluated action.
	 * @return A boolean which indicated wheter the specified unit can execute the
	 *         action.
	 */
	public abstract boolean canExecute(Unit unit, Action action);

	/**
	 * @param unit
	 *            The unit performing the action.
	 * @param action
	 *            The evaluated action.
	 */
	public abstract void execute(Unit unit, Action action);

	@Override
	public abstract String toString();

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof StarcraftAction)) {
			return false;
		} else {
			return toString().equals(obj.toString());
		}
	}
}
