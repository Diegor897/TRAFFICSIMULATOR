package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	
	private List<Pair<String, Weather>> _ws;

	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
		super(time);
		
		if (ws == null) {
			throw new IllegalArgumentException("Null weather list");
		}
		
		_ws = new ArrayList<Pair<String, Weather>>(ws);
	}

	@Override
	public void execute(RoadMap map) {
		for (Pair<String, Weather> w : _ws) {
			Road r = map.getRoad(w.getFirst());
			
			if (r == null) {
				throw new IllegalArgumentException("Null road");
			}
			
			r.setWeather(w.getSecond());
		}
	}
	
	@Override
	public String toString() {
		String s = "[";
		
		for (Pair<String, Weather> w : _ws) {
			s += "(" + w.getFirst() + ", " + w.getSecond().toString() + "), ";
		}
		
		s = s.substring(0, s.length() - 2) + "]";
		
		return "Set Weather: " + s;
	}
}