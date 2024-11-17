package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		List<String> _itinerary = new ArrayList<String>();
		
		for (Object o : data.getJSONArray("itinerary").toList()) {
			_itinerary.add(o.toString());
		}
		
		return new NewVehicleEvent(data.getInt("time"), data.getString("id"), data.getInt("maxspeed"), data.getInt("class"), _itinerary);
	}
}
