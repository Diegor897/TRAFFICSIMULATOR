package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder {

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		getAttributes(data);
		
		return new NewInterCityRoadEvent(_time, _id, _srcJun, _destJun, _length, _co2Limit, _maxSpeed, _weather);
	}
}