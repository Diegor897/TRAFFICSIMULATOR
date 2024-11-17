package simulator.model;

public class NewJunctionEvent extends Event {
	
	private String _id;
	
	private LightSwitchingStrategy _lsStrategy;
	
	private DequeuingStrategy _dqStrategy;
	
	private int _x;
	
	private int _y;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		
		_id = id;
		_lsStrategy = lsStrategy;
		_dqStrategy = dqStrategy;
		_x = xCoor;
		_y = yCoor;
	}

	@Override
	public void execute(RoadMap map) {
		map.addJunction(new Junction(_id, _lsStrategy, _dqStrategy, _x, _y));
	}
	
	@Override
	public String toString() {
		return "New Junction '" + _id + "'";
	}
}