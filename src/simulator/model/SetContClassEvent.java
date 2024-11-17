package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {
	
	private List<Pair<String, Integer>> _cs;

	public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		
		if (cs == null) {
			throw new IllegalArgumentException("Null weather list");
		}
		
		_cs = new ArrayList<Pair<String, Integer>>(cs);
	}

	@Override
	public void execute(RoadMap map) {
		for (Pair<String, Integer> c : _cs) {
			Vehicle v = map.getVehicle(c.getFirst());
			
			if (v == null) {
				throw new IllegalArgumentException("Null vehicle");
			}
			
			v.setContClass(c.getSecond());
		}
	}
	
	@Override
	public String toString() {
		String s = "[";
		
		for (Pair<String, Integer> c : _cs) {
			s += "(" + c.getFirst() + ", " + c.getSecond() + "), ";
		}
		
		s = s.substring(0, s.length() - 2) + "]";
		
		return "Set Cont. Class: " + s;
	}
}