package eisbw.actions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import eis.iilang.Action;
import eis.iilang.Parameter;
import eis.iilang.Identifier;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import org.apache.commons.lang3.concurrent.TimedSemaphore;

/**
 * @author Thijs Molendijk & Thijs Raymakers - Send a chat message to all players in the game.
 */
public class SendText extends StarcraftAction {
	private static List<String> ALLOWED_PHRASES = Arrays.asList(
			"gg",
			"gg wp",
			"Error: null",
			"I'm using tilt controls!",
			"It's free real estate!",
			"Hello world!",
			"bg",
			"We are going to destroy you!",
			"I give up",
			"I surrender",
			"Wololo!",
			"Well played!",
			"I don't feel so good Protoss",
			"I don't feel so good Terran",
			"I don't feel so good Zerg",
			"no u",
			"Nice",
			"Yes",
			"No",
			"PogChamp",
			"Ahh!",
			"Ah! Being rushed",
			"Ooh!",
			"Enemy sighted!",
			"Medic!",
			"Is this easy mode?",
			"VAC",
			"nice save bro",
			"SAVED",
			"RUINED",
			"Who called in the fleet?",
			"It's your turn!",
			"ggeznore",
			"rekt",
			"You must construct additional pylons",
			"plz don't cheese me",
			"zerg rush"
	);

	// Use a timed semaphore to limit the amount of messages sent per second.
	private int TIME_UNIT = 1;
	private int MAX_MESSAGES_PER_TIME_UNIT = 1;
	private TimedSemaphore rateLimit = new TimedSemaphore(TIME_UNIT, TimeUnit.SECONDS, MAX_MESSAGES_PER_TIME_UNIT);

	/**
	 * The SendText constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public SendText(JNIBWAPI api) {
		super(api);
	}

	/**
	 * Checks if the invoked sendText action has a syntactically correct parameter.
	 * @param action The action that is invoked.
	 * @return If the action contains an Identifier as a parameter.
	 */ 
	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier;
	}

	/**
	 * Checks if this unit type can execute this action. Since SendText is not type specific, this should always
	 * return true.
	 * @param type
	 *            The type of the unit performing the action.
	 * @param action
	 *            The evaluated action.
	 * @return true, since sendText is not type specific.
	 */
	@Override
	public boolean canExecute(UnitType type, Action action) {
		return true;
	}

	/**
	 * Execute the sendText command
	 * @param unit
	 *            The unit performing the action.
	 * @param action The action that is being performed.
	 */
	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String text = ((Identifier) parameters.get(0)).getValue().trim();

		if(isValidChatString(text)) {
			// Everything is okay, send the message.
			this.api.sendText(text);
		}
	}

	/**
	 * Checks if the string is valid and meets the required conditions. It checks for rate limiting (max. 1 message per
	 * second), the length of the text and if it is on the whitelist of phrases.
	 * @param text The string that wants to be send
	 * @return Whether it is a valid string
	 */
	protected boolean isValidChatString(String text) {
		// Deny chats that are too short or long.
		if (text.length() < 1 || text.length() > 140) return false;

		// Only allow up to 1 message a second.
		if (!this.rateLimit.tryAcquire()) return false;

		// Check if it is in the ALLOWED_PHRASES list
		if(!ALLOWED_PHRASES.contains(text)) return false;

		// If everything succeeds, return true.
		return true;
	}

	/**
	 * @return The String notation of this method
	 */
	@Override
	public String toString() {
		return "sendText";
	}
}
