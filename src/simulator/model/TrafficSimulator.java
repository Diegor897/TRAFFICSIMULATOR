package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {

	private RoadMap _roadMap;
	
	private List<Event> _events;
	
	private int _time;
	
	private List<TrafficSimObserver> _observers;
	
	public TrafficSimulator() {
		_roadMap = new RoadMap();
		_events = new SortedArrayList<Event>();
		_time = 0;
		
		_observers = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		_events.add(e);
		
		for (TrafficSimObserver o : _observers) {
			//o.onEventAdded(_roadMap, _events, e, _time);
		}
	}
	
	public void advance() {
		try {
			_time++;
			
			for (TrafficSimObserver o : _observers) {
				o.onAdvanceStart(_roadMap, _events, _time);
			}
			
			for (int i = 0; i < _events.size(); i++) {
				if (_time == _events.get(i).getTime()) {
					_events.get(i).execute(_roadMap);
				}
			}
			
			for (int i = 0; i < _events.size(); i++) {
				if (_time == _events.get(i).getTime()) {
					_events.remove(_events.get(i));
				}
			}
			
			for (Junction j : _roadMap.getJunctions()) {
				j.advance(_time);
			}
			
			for (Road r : _roadMap.getRoads()) {
				r.advance(_time);
			}
			
			for (TrafficSimObserver o : _observers) {
				o.onAdvanceEnd(_roadMap, _events, _time);
			}
			
		} catch (Exception ex) {
			for (TrafficSimObserver o : _observers) {
				o.onError(ex.getMessage());
			}
			
			throw ex;
		}
	}
	
	public void reset() {
		_roadMap.reset();
		_events.clear();
		_time = 0;
		
		for (TrafficSimObserver o : _observers) {
			o.onReset(_roadMap, _events, _time);
		}
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		jo.put("time", _time);
		jo.put("state", _roadMap.report());
		
		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		_observers.add(o);
		
		for (TrafficSimObserver obs : _observers) {
			obs.onRegister(_roadMap, _events, _time);
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		_observers.remove(o);
	}
	
	public int getTime() {
		return _time;
	}
	
	public RoadMap getRoadMap() {
		return _roadMap;
	}
}