package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {
	
	private static final long serialVersionUID = -2069268137903573849L;

	private int _status;
	
	private JComboBox<Road> _roads;
	private DefaultComboBoxModel<Road> _roadsModel;
	
	private JComboBox<Weather> _weathers;
	private DefaultComboBoxModel<Weather> _weathersModel;
	
	private JSpinner _ticks;
	private SpinnerNumberModel _ticksModel;
	
	public ChangeWeatherDialog(Frame parent) {
		super(parent, true);
		this.initGUI();
	}
	
	private void initGUI() {
		_status = 0;
		
		setTitle("Change Weather Conditions");
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		setContentPane(main);
		
		JLabel helpMsg = new JLabel("Change a Road's Weather Conditions");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);
		main.add(helpMsg);
		
		main.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		main.add(viewsPanel);

		main.add(Box.createRigidArea(new Dimension(0, 20)));

		_roadsModel = new DefaultComboBoxModel<>();
		_roads = new JComboBox<>(_roadsModel);
		_roads.setMaximumSize(new Dimension(80, 40));
		_roads.setMinimumSize(new Dimension(80, 40));
		_roads.setPreferredSize(new Dimension(80, 40));

		viewsPanel.add(new JLabel("Roads"), _roads);
		viewsPanel.add(_roads);
		
		_weathersModel = new DefaultComboBoxModel<>();
		_weathers = new JComboBox<>(_weathersModel);
		_weathers.setMaximumSize(new Dimension(80, 40));
		_weathers.setMinimumSize(new Dimension(80, 40));
		_weathers.setPreferredSize(new Dimension(80, 40));
		
		viewsPanel.add(new JLabel("Weathers"), _weathers);
		viewsPanel.add(_weathers);
		
		_ticksModel = new SpinnerNumberModel();
		_ticks = new JSpinner(_ticksModel);
		_ticks.setMaximumSize(new Dimension(80, 40));
		_ticks.setMinimumSize(new Dimension(80, 40));
		_ticks.setPreferredSize(new Dimension(80, 40));
		
		viewsPanel.add(new JLabel("Ticks"), _ticks);
		viewsPanel.add(_ticks);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		main.add(buttonsPanel);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
		
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_roadsModel.getSelectedItem() != null) {
					_status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public int open(RoadMap roadMap) {
		for (Road r : roadMap.getRoads()) {
			_roadsModel.addElement(r);
		}
		
		for (Weather w : Weather.values()) {
			_weathersModel.addElement(w);
		}
		
		this.setVisible(true);
		return _status;
	}
	
	public int getTicks() {
		return (int)_ticksModel.getValue();
	}
	
	public Road getRoad() {
		return (Road)_roadsModel.getSelectedItem();
	}
	
	public Weather getWeather() {
		return (Weather)_weathersModel.getSelectedItem();
	}
}