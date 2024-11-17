package simulator.model;

public class CityRoad extends Road {

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		updateSpeedLimit();
	}

	@Override
	public void reduceTotalContamination() {
		int x = 0;
		
		switch(_weather) {
		case WINDY:
		case STORM:
			x = 10;
			break;
		default:
			x = 2;
			break;
		}
		
		_totalCont = Math.max(0, (_totalCont - x));
	}

	@Override
	public void updateSpeedLimit() {
		_speedLimit = _maxSpeed;
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		
		v.setSpeed((int)(((11.0 - v.getContClass()) / 11.0) * _speedLimit));
		
		return v.getSpeed();
	}
}