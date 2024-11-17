package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator _trafficSimulator;
	
	private Factory<Event> _eventFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		
		if (sim == null || eventsFactory == null) {
			throw new IllegalArgumentException("Null argument in Controller()");
		}
		
		_trafficSimulator = sim;
		_eventFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray ja = jo.optJSONArray("events");
		
		if (!jo.has("events") || (ja == null)) {
			throw new IllegalArgumentException("Wrong JSON format");
		}
		
		for (int i = 0; i < ja.length(); i++) {
			_trafficSimulator.addEvent(_eventFactory.createInstance(ja.getJSONObject(i)));
		}
	}
	
	public void run(int n, OutputStream out) {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		
		for (int i = 0; i < n; i++) {
			_trafficSimulator.advance();
			ja.put(_trafficSimulator.report());
		}
		
		jo.put("states", ja);

		try {
			out.write(jo.toString(3).getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void run(int n) { // TODO
		for (int i = 0; i < n; i++) {
			_trafficSimulator.advance();
		}
	}
	
	public void reset() {
		_trafficSimulator.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		_trafficSimulator.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		_trafficSimulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		_trafficSimulator.addEvent(e);
	}
	
	public int getTime() {
		return _trafficSimulator.getTime();
	}
	
	public RoadMap getRoadMap() {
		return _trafficSimulator.getRoadMap();
	}
} 