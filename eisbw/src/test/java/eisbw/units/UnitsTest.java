package eisbw.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bwapi.Unit;
import bwapi.UnitType;
import eisbw.StarcraftEnvironmentImpl;
import eisbw.percepts.perceivers.IPerceiver;

public class UnitsTest {
	private Units units;

	@Mock
	private StarcraftEnvironmentImpl env;
	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
	@Mock
	private StarcraftUnitFactory factory;

	/**
	 * Init mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);

		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.toString()).thenReturn("name");
		when(this.unit.getID()).thenReturn(0);

		when(this.factory.create(any(Unit.class))).thenReturn(new StarcraftUnit(new LinkedList<IPerceiver>(), false));

		this.units = new Units(this.env);
	}

	@Test
	public void addDeleteUnit_test() {
		this.units.addUnit(this.unit, this.factory);
		assertEquals("name0", this.units.getUnitNames().get(0));
		assertNotNull(this.units.getUnits().get("name0"));
		verify(this.factory, times(1)).create(any(Unit.class));
		this.units.deleteUnit("name0", 0);
		verify(this.env, times(1)).deleteFromEnvironment("name0");
		this.units.addUnit(this.unit, this.factory);
		this.units.clean();
		verify(this.env, times(2)).deleteFromEnvironment("name0");
	}

	@Test
	public void getStarcraftUnits_test() {
		assertEquals(this.units.getStarcraftUnits(), this.units.starcraftUnits);
	}

	@Test
	public void getUninitializedUnits_test() {
		assertEquals(this.units.getUninitializedUnits(), this.units.uninitializedUnits);
	}
}
