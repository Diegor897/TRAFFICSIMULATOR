package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {
	
	protected Junction _src;
	
	protected Junction _dest;
	
	protected int _length;
	
	protected int _maxSpeed;
	
	protected int _speedLimit;
	
	protected int _contLimit;
	
	protected Weather _weather;
	
	protected int _totalCont;
	
	protected List<Vehicle> _vehicles;
	
	protected Comparator<Vehicle> _cmp;

	public Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);

		if ((maxSpeed < 1) || (contLimit < 1) || (length < 1) || (srcJunc == null) || (destJunc == null) || (weather == null)) {
			
			throw new IllegalArgumentException("illegal argument in Road constructor");
			
		} else {
			
			_src = srcJunc;
			_dest = destJunc;
			_maxSpeed = maxSpeed;
			_contLimit = contLimit;
			_length = length;
			_weather = weather;
			
			_src.addOutGoingRoad(this);
			_dest.addIncomingRoad(this);
			
			_vehicles = new ArrayList<Vehicle>();
			
			_cmp = new Comparator<Vehicle>() {
				
				@Override
				public int compare(Vehicle v1, Vehicle v2) {
					
					if (v1.getLocation() > v2.getLocation()) {
						return -1;
					} else if (v1.getLocation() < v2.getLocation()) {
						return 1;
					} else {
						return 0;
					}
				}
			};
		}
	}
	
	public void enter(Vehicle v) {
		if ((v.getSpeed() == 0) && (v.getLocation() == 0)) {
			_vehicles.add(v);
			_vehicles.sort(_cmp);
		} else {
			throw new IllegalArgumentException("Vehicle is not valid in enter");
		}
	}
	
	public void exit(Vehicle v) {
		if (!_vehicles.remove(v)) {
			throw new IllegalArgumentException("Vehicle not in list");
		}
	}
	
	public void setWeather(Weather w) {
		if (w == null) {
			throw new IllegalArgumentException("Null weather in setWeather()");
		} else {
			_weather = w;
		}
	}
	
	public void addContamination(int c) {
		if (c < 0) {
			throw new IllegalArgumentException("Negative contamination");
		} else {
			_totalCont += c;
		}
	}
	
	public abstract void reduceTotalContamination();
	
	public abstract void updateSpeedLimit();
	
	public abstract int calculateVehicleSpeed(Vehicle v);

	@Override
	public void advance(int time) {
		reduceTotalContamination();
		
		updateSpeedLimit();
		
		for (Vehicle v : _vehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			
			v.advance(0);
		}
		
		_vehicles.sort(_cmp);
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		
		jo.put("speedlimit", _speedLimit);
		jo.put("co2", _totalCont);
		jo.put("weather", _weather.toString());
		
		
		for (Vehicle v : _vehicles) {
			ja.put(v.getId());
		}
		
		jo.put("vehicles", ja);
		jo.put("id", _id);
		
		return jo;
	}
	
	public int getLength() {
		return _length;
	}
	
	public Junction getDest() {
		return _dest;
	}
	
	public Junction getSrc() {
		return _src;
	}
	
	public Weather getWeather() {
		return _weather;
	}
	
	public int getContLimit() {
		return _contLimit;
	}
	
	public int getMaxSpeed() {
		return _maxSpeed;
	}
	
	public int getTotalCO2() {
		return _totalCont;
	}
	
	public int getSpeedLimit() {
		return _speedLimit;
	}
	
	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(new ArrayList<>(_vehicles));
	}
}