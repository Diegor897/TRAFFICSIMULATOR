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

import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {
	
	private static final long serialVersionUID = -6375230819112055509L;

	private int _status;
	
	private JComboBox<Vehicle> _vehicles;
	private DefaultComboBoxModel<Vehicle> _vehiclesModel;
	
	private JComboBox<Integer> _contClass;
	private DefaultComboBoxModel<Integer> _contClassModel;
	
	private JSpinner _ticks;
	private SpinnerNumberModel _ticksModel;
	
	public ChangeCO2ClassDialog(Frame parent) {
		super(parent, true);
		this.initGUI();
	}
	
	private void initGUI() {
		_status = 0;
		
		setTitle("Change Contamination Class");
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		setContentPane(main);
		
		JLabel helpMsg = new JLabel("Set Contamination Class");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);
		main.add(helpMsg);
		
		main.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		main.add(viewsPanel);

		main.add(Box.createRigidArea(new Dimension(0, 20)));

		_vehiclesModel = new DefaultComboBoxModel<>();
		_vehicles = new JComboBox<>(_vehiclesModel);
		_vehicles.setMaximumSize(new Dimension(80, 40));
		_vehicles.setMinimumSize(new Dimension(80, 40));
		_vehicles.setPreferredSize(new Dimension(80, 40));

		viewsPanel.add(new JLabel("Vehicles"), _vehicles);
		viewsPanel.add(_vehicles);
		
		_contClassModel = new DefaultComboBoxModel<>();
		_contClass = new JComboBox<>(_contClassModel);
		_contClass.setMaximumSize(new Dimension(80, 40));
		_contClass.setMinimumSize(new Dimension(80, 40));
		_contClass.setPreferredSize(new Dimension(80, 40));
		
		viewsPanel.add(new JLabel("Contamination Class"), _contClass);
		viewsPanel.add(_contClass);
		
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
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_vehiclesModel.getSelectedItem() != null) {
					_status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
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
		for (Vehicle v : roadMap.getVehicles()) {
			_vehiclesModel.addElement(v);
		}
		
		for (int i = 0; i < 10; i++) {
			_contClassModel.addElement(i);
		}
		
		this.setVisible(true);
		return _status;
	}
	
	public int getTicks() {
		return (int)_ticksModel.getValue();
	}
	
	public Vehicle getVehicle() {
		return (Vehicle)_vehiclesModel.getSelectedItem();
	}
	
	public int getCO2Class() {
		return (int)_contClassModel.getSelectedItem();
	}
}