package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	
	private Factory<LightSwitchingStrategy> _lssFactory;
	
	private Factory<DequeuingStrategy> _dqsFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		
		_lssFactory = lssFactory;
		_dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		return new NewJunctionEvent(
				data.getInt("time"), 
				data.getString("id"), 
				_lssFactory.createInstance(data.getJSONObject("ls_strategy")), 
				_dqsFactory.createInstance(data.getJSONObject("dq_strategy")), 
				data.getJSONArray("coor").getInt(0), 
				data.getJSONArray("coor").getInt(1));
	}
}