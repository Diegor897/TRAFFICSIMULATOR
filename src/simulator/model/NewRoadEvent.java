package simulator.model;

public abstract class NewRoadEvent extends Event {
	
	protected String _id;
	
	protected String _srcJun;
	
	protected String _destJun;
	
	protected int _length;
	
	protected int _co2Limit;
	
	protected int _maxSpeed;
	
	protected Weather _weather;

	NewRoadEvent(int time, String id, String srcJun, String destJun, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		
		_id = id;
		_srcJun = srcJun;
		_destJun = destJun;
		_length = length;
		_co2Limit = co2Limit;
		_maxSpeed = maxSpeed;
		_weather = weather;
	}
}