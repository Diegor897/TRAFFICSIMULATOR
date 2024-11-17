package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {
	
	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int aux;

		if (data.isNull("timeslot")) {
			aux = 1;
		} else {
			aux = data.getInt("timeslot");
		}
		
		return new MostCrowdedStrategy(aux);
	}
}