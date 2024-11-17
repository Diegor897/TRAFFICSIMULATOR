package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {
	
	private String _id;
	
	private int _maxSpeed;
	
	private int _contClass;
	
	private List<String> _itinerary;

	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		
		_id = id;
		_maxSpeed = maxSpeed;
		_contClass = contClass;
		_itinerary = new ArrayList<String>(itinerary);
	}

	@Override
	public void execute(RoadMap map) {
		List<Junction> aux = new ArrayList<Junction>();
		Vehicle v;
		
		for (String j : _itinerary) {
			aux.add(map.getJunction(j));
		}
		
		v = new Vehicle(_id, _maxSpeed, _contClass, aux);
		
		map.addVehicle(v);
		
		v.moveToNextRoad();
	}
	
	@Override
	public String toString() {
		return "New Vehicle '" + _id + "'";
	}
}