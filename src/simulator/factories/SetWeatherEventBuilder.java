package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		List<Pair<String, Weather>> _ws = new ArrayList<Pair<String, Weather>>();
		
		for (int i = 0; i < data.getJSONArray("info").length(); i++) {
			_ws.add(new Pair<String, Weather>(data.getJSONArray("info").getJSONObject(i).getString("road"), 
											  Weather.valueOf(data.getJSONArray("info").getJSONObject(i).getString("weather").toUpperCase())));
		}
		
		return new SetWeatherEvent(data.getInt("time"), _ws);
	}
}