package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy {
	
	private int _timeSlot;

	public RoundRobinStrategy(int timeSlot) {
		_timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		int result = -1;
		
		if (roads != null) {
			if (currGreen == -1) {
				
				result = 0;
				
			} else if (currTime - lastSwitchingTime < _timeSlot) {
				
				result = currGreen;
				
			} else {
				
				result = (currGreen + 1) % qs.size();
			}
		}
		
		return result;
	}
}