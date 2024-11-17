package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent {

	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJun, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time, id, srcJun, destJun, length, co2Limit, maxSpeed, weather);
	}

	@Override
	public void execute(RoadMap map) {
		map.addRoad(new InterCityRoad(_id, map.getJunction(_srcJun), map.getJunction(_destJun), _maxSpeed, _co2Limit, _length, _weather));
	}
	
	@Override
	public String toString() {
		return "New InterCityRoad '" + _id + "'";
	}
}