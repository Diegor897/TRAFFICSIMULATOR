package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {
	
	private int _maxSpeed;
	
	private int _currentSpeed;
	
	private int _contClass;
	
	private VehicleStatus _status;
	
	private List<Junction> _itinerary;
	
	private Road _road;
	
	private int _location;
	
	private int _totalCont;
	
	private int _travelledDistance;

	public Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		
		if ((maxSpeed < 1) || ((contClass < 0) || (contClass > 10)) || (itinerary.size() < 2)) {
			
			throw new IllegalArgumentException("illegal armgument in Vehicle");
			
		} else {
			
			_maxSpeed = maxSpeed;
			_contClass = contClass;
			_itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
			
			_currentSpeed = 0;
			_status = VehicleStatus.PENDING;
			_road = null;
			_location = 0;
			_totalCont = 0;
			_travelledDistance = 0;
		}
	}

	public int getLocation() {
		return _location;
	}
	
	public int getSpeed() {
		return _currentSpeed;
	}
	
	public int getMaxSpeed() {
		return _maxSpeed;
	}
	
	public int getContClass() {
		return _contClass;
	}
	
	public VehicleStatus getStatus() {
		return _status;
	}
	
	public int getTotalCO2() {
		return _totalCont;
	}
	
	public List<Junction> getItinerary() {
		return _itinerary;
	}
	
	public Road getRoad() {
		return _road;
	}
	
	public int getTravelledDistance() {
		return _travelledDistance;
	}
	
	public void setSpeed(int s) {
		if (s < 0) {
			throw new IllegalArgumentException("negative speed in Vehicle");
		} else {
			if (_status == VehicleStatus.TRAVELING) {
				_currentSpeed = Math.min(s, _maxSpeed);
			}
		}
	}
	
	public void setContClass(int c) {
		if ((c < 0) || (c > 10)) {
			throw new IllegalArgumentException("illegal contClass in Vehicle");
		} else {
			_contClass = c;
		}
	}
	
	@Override
	public void advance(int time) {
		int newLoc = 0, c = 0;
		int length = _road.getLength();
		int diff = 0;
		
		if (_status == VehicleStatus.TRAVELING) {
			newLoc = Math.min(_location + _currentSpeed, length);
			diff = (newLoc - _location);
			
			_travelledDistance += diff;
			
			c = diff * _contClass;
			
			_location = newLoc;
			_totalCont += c;
			_road.addContamination(c);
			
			if (_location == length) {
				
				_road.getDest().enter(this);
				_status = VehicleStatus.WAITING;
				_currentSpeed = 0;
			}
			
		} else {
			
			_currentSpeed = 0;
		}
	}
	
	public void moveToNextRoad() {
		int lastInd = -1;

		if (_status == VehicleStatus.PENDING) {
			_road = _itinerary.get(0).roadTo(_itinerary.get(1));
			_location = 0;
			
			_road.enter(this);
			_status = VehicleStatus.TRAVELING;
			
		} else if (_status == VehicleStatus.WAITING) {
			lastInd = _itinerary.indexOf(_road.getDest());
			
			_road.exit(this);
			_location = 0;
			
			if (lastInd == (_itinerary.size() - 1)) {
				
				_road = null;
				_status = VehicleStatus.ARRIVED;
				
			} else {
				
				_road = _itinerary.get(lastInd).roadTo(_itinerary.get(lastInd + 1));
				_road.enter(this);
				_status = VehicleStatus.TRAVELING;
			}
			
		} else {
			
			throw new IllegalArgumentException("illegal moveToNextRoad()");
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		jo.put("id", _id);
		jo.put("speed", _currentSpeed);
		jo.put("distance", _travelledDistance);
		jo.put("co2", _totalCont);
		jo.put("class", _contClass);
		jo.put("status", _status.toString());
		
		if ((_status != VehicleStatus.ARRIVED) && (_status != VehicleStatus.PENDING)) {
			jo.put("road", _road.getId());
			jo.put("location", _location);
		}
		
		return jo;
	}
}