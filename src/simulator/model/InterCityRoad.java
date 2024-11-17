package simulator.model;

public class InterCityRoad extends Road {

	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		updateSpeedLimit();
	}

	@Override
	public void reduceTotalContamination() {
		int x = 0;
		
		switch(_weather) {
		case SUNNY:
			x = 2;
			break;
		case CLOUDY:
			x = 3;
			break;
		case RAINY:
			x = 10;
			break;
		case WINDY:
			x = 15;
			break;
		case STORM:
			x = 20;
			break;
		}
		
		_totalCont = (int)(((100.0 - x) / 100.0) * _totalCont);
	}

	@Override
	public void updateSpeedLimit() {
		double x = 1;
		
		if (_totalCont > _contLimit) {
			x = 0.5;
		}
		
		_speedLimit = (int)(_maxSpeed * x);
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		double x = 1;
		
		if (_weather == Weather.STORM) {
			x = 0.8;
		}
		
		v.setSpeed((int)(_speedLimit * x));
		
		return v.getSpeed();
	}
}