package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder {

	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		getAttributes(data);
		
		return new NewCityRoadEvent(_time, _id, _srcJun, _destJun, _length, _co2Limit, _maxSpeed, _weather);
	}
}