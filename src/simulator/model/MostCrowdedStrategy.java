package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int _timeSlot;

	public MostCrowdedStrategy(int timeSlot) {
		_timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		int result = -1;
		int max = 0;
		int k = 0;
		int ind = 0;
		int qsSize = 0;
		
		if (roads != null) {
			if (currGreen == -1) {
				for (List<Vehicle> q : qs) {
					if (q.size() > max) {
						max = q.size();
						ind = k;
					}
					
					k++;
				}
				
				result = ind;
				
			} else if (currTime - lastSwitchingTime < _timeSlot) {
				
				result = currGreen;
				
			} else {
				qsSize = qs.size();
				
				for (int j = 0; j < qsSize; j++) {
					k = (j + currGreen + 1) % qsSize;
					
					if (qs.get(k).size() > max) {
						max = qs.get(k).size();
						ind = k;
					}
				}
				
				result = ind;
			}
		}
		
		return result;
	}
}