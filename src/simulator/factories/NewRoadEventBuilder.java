package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {
	
	protected String _id;
	
	protected String _srcJun;
	
	protected String _destJun;
	
	protected int _length;
	
	protected int _co2Limit;
	
	protected int _maxSpeed;
	
	protected int _time;
	
	protected Weather _weather;

	public NewRoadEventBuilder(String type) {
		super(type);
	}
	
	protected void getAttributes(JSONObject data) {
		_id = data.getString("id");
		_time = data.getInt("time");
		_srcJun = data.getString("src");
		_destJun = data.getString("dest");
		_length = data.getInt("length");
		_co2Limit = data.getInt("co2limit");
		_maxSpeed = data.getInt("maxspeed");
		_weather = Weather.valueOf(data.getString("weather"));
	}

	@Override
	protected abstract Event createTheInstance(JSONObject data);
}