package eisbw.percepts.perceivers;

import eis.iilang.Percept;
import eisbw.percepts.LiftPercept;
import eisbw.percepts.StimPercept;

import java.util.*;

import jnibwapi.*;

public class LiftUnitPerceiver extends UnitPerceiver {

    public LiftUnitPerceiver(JNIBWAPI api, Unit unit) {
        super(api, unit);
    }

    @Override
    public List<Percept> perceive() {
        List<Percept> result = new ArrayList<>();
        if (unit.isLifted()) {
            result.add(new LiftPercept());
        }
        return result;
    }
}