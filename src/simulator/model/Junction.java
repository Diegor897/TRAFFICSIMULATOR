package simulator.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	private List<Road> _incoming;
	
	private Map<Junction, Road> _outgoing;
	
	private List<List<Vehicle>> _queue;
	
	private Map<Road, List<Vehicle>> _queueRoads;
	
	private int _green;
	
	private int _time;
	
	private LightSwitchingStrategy _lsStrategy;
	
	private DequeuingStrategy _dqStrategy;
	
	private int _x;
	
	private int _y;

	public Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		
		if ((lsStrategy == null) || (dqStrategy == null) || (xCoor < 0) || (yCoor < 0)) {
			throw new IllegalArgumentException("Illegal argument in Junction constructor");
		}
		
		_lsStrategy = lsStrategy;
		_dqStrategy = dqStrategy;
		_x = xCoor;
		_y = yCoor;
		
		_time = 0;
		_green = -1;
		
		_incoming = new LinkedList<Road>();
		_outgoing = new HashMap<Junction, Road>();
		_queue = new ArrayList<List<Vehicle>>();
		_queueRoads = new HashMap<Road, List<Vehicle>>();
	}
	
	public void addIncomingRoad(Road r) {
		LinkedList<Vehicle> l = new LinkedList<Vehicle>();
		
		if (r.getDest() != this) {
			throw new IllegalArgumentException("Road destination is different from Junction");
		}
		
		_incoming.add(r);
		
		_queue.add(l);
		
		_queueRoads.put(r, l);
	}
	
	public void addOutGoingRoad(Road r) {
		
		if ((r.getSrc() != this) || _outgoing.containsKey(r.getDest())) {
			throw new IllegalArgumentException("Illegal argument in Junction.addOutGoingRoad()");
		}
		
		_outgoing.put(r.getDest(), r);
	}
	
	public void enter(Vehicle v) {
		_queueRoads.get(v.getRoad()).add(v);
	}
	
	public Road roadTo(Junction j) {
		return _outgoing.get(j);
	}
	
	public int getGreenLightIndex() {
		return _green;
	}
	
	public List<Road> getInRoads() {
		return _incoming;
	}
	
	public List<Vehicle> getQueue(Road r) {
		return _queueRoads.get(r);
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}

	@Override
	public void advance(int time) {
		int index = -1;
		
		if ((_green != -1) && (_queue.size() > 0) && (_queue.get(_green).size() > 0)) {
			for (Vehicle v : _dqStrategy.dequeue(_queue.get(_green))) {
				v.moveToNextRoad();
				
				_queue.get(_green).remove(v);
			}	
		}
		
		if (_queue.size() > 0) {
			index = _lsStrategy.chooseNextGreen(_incoming, _queue, _green, _time, time);
		}
		
		if (index != _green) {
			_green = index;
			_time = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja0 = new JSONArray();
		JSONArray ja1 = new JSONArray();
		JSONObject aux = new JSONObject();
		
		jo.put("id", _id);
		jo.put("green", (_green == -1 ? "none" : (_incoming.size() == 0 ? "none" : _incoming.get(_green))));

		for (Road r : _incoming) {
			aux.put("road", r.getId());
			
			for (Vehicle v : _queueRoads.get(r)) {
				ja1.put(v.getId());
			}
			
			aux.put("vehicles", ja1);
			
			ja0.put(aux);
			
			aux = new JSONObject();
			ja1 = new JSONArray();
		}
		
		jo.put("queues", ja0);
		
		return jo;
	}
}