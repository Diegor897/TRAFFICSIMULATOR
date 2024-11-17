package simulator.view;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

import simulator.model.SetWeatherEvent;
import simulator.model.SetContClassEvent;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = -4423199850333010661L;
	
	private Controller _ctrl;
	
	private RoadMap _roadMap;
	
	private JToolBar _toolBar;
	
	private JFileChooser _fileChooser;
	
	private JButton _fileChooserButton;
	
	private JButton _setContClassButton;
	
	private JButton _setWeatherButton;
	
	private JButton _runButton;
	
	private JButton _stopButton;
	
	private JButton _exitButton;
	
	private JButton _exam1Button;
	
	private JButton _exam2Button;
	
	private JButton _exam3Button;
	
	private JSpinner _ticks;
	
	private SpinnerNumberModel _ticksModel;
	
	private boolean _stopped;
	
	private WeatherHistoryDialog _HistoryWeather;
	
	private ContaminationHistoryDialog _CHistory;
	private SpeedHistoryDialog _SHistory;
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_ctrl.addObserver(this);
		_stopped = true;
		
		_roadMap = ctrl.getRoadMap();
		_HistoryWeather = new WeatherHistoryDialog(null, ctrl);
		_CHistory = new ContaminationHistoryDialog(null, ctrl);
		_SHistory = new SpeedHistoryDialog(null, ctrl);
		
		initGUI();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// EMPTY
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_roadMap = map;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// EMPTY
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_roadMap = map;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_roadMap = map;
	}

	@Override
	public void onError(String msg) {
		// EMPTY		
	}

	private void initGUI() {
		_toolBar = new JToolBar();
		this.setLayout(new BorderLayout());
		this.add(_toolBar, BorderLayout.PAGE_START);
		
		// File Chooser Button
		_fileChooser = new JFileChooser();
		_fileChooser.setDialogTitle("Load File");
		
		_fileChooserButton = new JButton();
		
		_fileChooserButton.setToolTipText("Load File");
		_fileChooserButton.setIcon(loadImage("resources/icons/open.png"));
		_fileChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				loadFile();
			}
		});
		
		_toolBar.add(_fileChooserButton);
		_toolBar.addSeparator();
		
		// Set Contamination Class Button
		_setContClassButton = new JButton();
		
		_setContClassButton.setToolTipText("Set Contamination Class of a Vehicle");
		_setContClassButton.setIcon(loadImage("resources/icons/co2class.png"));
		_setContClassButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				setContClass();
			}
		});
		
		_toolBar.add(_setContClassButton);
		_toolBar.addSeparator();
		
		// Set Weather Button
		_setWeatherButton = new JButton();
		
		_setWeatherButton.setToolTipText("Change a Road's Weather Conditions");
		_setWeatherButton.setIcon(loadImage("resources/icons/weather.png"));
		_setWeatherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				changeWeather();
			}
		});
		
		_toolBar.add(_setWeatherButton);
		_toolBar.addSeparator();
		
		// Run Button
		_runButton = new JButton();
		
		_runButton.setToolTipText("Run Simulation");
		_runButton.setIcon(loadImage("resources/icons/run.png"));
		_runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					start();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		_toolBar.add(_runButton);
		_toolBar.addSeparator();
		
		// Stop Button
		_stopButton = new JButton();
		
		_stopButton.setToolTipText("Stop Simulation");
		_stopButton.setIcon(loadImage("resources/icons/stop.png"));
		_stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop_sim();
			}
		});
		
		_toolBar.add(_stopButton);
		_toolBar.addSeparator();
		
		
		// Test Button
		
		_exam1Button = new JButton();
		
		_exam1Button.setToolTipText("Change a Road's Weather Conditions");
		_exam1Button.setIcon(loadImage("resources/icons/weather.png"));
		_exam1Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				exam1();
			}
		});
		
		_toolBar.add(_exam1Button);
		_toolBar.addSeparator();
		
		// Test Button
		
				_exam2Button = new JButton();
				
				_exam2Button.setToolTipText("Change a Road's Weather Conditions");
				_exam2Button.setIcon(loadImage("resources/icons/weather.png"));
				_exam2Button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {				
						exam2();
					}
				});
				
				_toolBar.add(_exam2Button);
				_toolBar.addSeparator();
				
				// Test Button
				
				_exam3Button = new JButton();
				
				_exam3Button.setToolTipText("Change a Road's Weather Conditions");
				_exam3Button.setIcon(loadImage("resources/icons/weather.png"));
				_exam3Button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {				
						exam3();
					}
				});
				
				_toolBar.add(_exam3Button);
				_toolBar.addSeparator();
				
				
		
		
		// Ticks spinner
		_toolBar.add(new JLabel("Ticks:"), _ticks);
		_ticksModel = new SpinnerNumberModel(10, 1, 10000, 1);
		_ticks = new JSpinner(_ticksModel);
		
		_ticks.setToolTipText("Number of Ticks to Run");
		_ticks.setMaximumSize(new Dimension(80, 40));
		_ticks.setMinimumSize(new Dimension(80, 40));
		_ticks.setPreferredSize(new Dimension(80, 40));
		
		_toolBar.add(_ticks);
		_toolBar.addSeparator();
		
		//
		_toolBar.add(Box.createGlue());
		
		// Exit button
		_exitButton = new JButton();
		
		_exitButton.setToolTipText("Exit");
		_exitButton.setIcon(loadImage("resources/icons/exit.png"));
		_exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, 
						JOptionPane.WARNING_MESSAGE, null, null, null) == 0) {
					System.exit(0);
				}
			}
		});
		
		_toolBar.add(_exitButton);
		
	}
	
	private void loadFile() {
		if (JFileChooser.APPROVE_OPTION == _fileChooser.showOpenDialog(this)) {
			try {
				File file = _fileChooser.getSelectedFile();
				_ctrl.reset();
				_ctrl.loadEvents(new FileInputStream(file));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.toString());
			}
		}
	}
	
	private void setContClass() {
		ChangeCO2ClassDialog changeCO2 = new ChangeCO2ClassDialog(null);
		List<Pair<String, Integer>> aux = new ArrayList<>();
		
		if (changeCO2.open(_roadMap) == 1) {
			aux.add(new Pair<>(changeCO2.getVehicle().getId(), changeCO2.getCO2Class()));
			
			try {
				_ctrl.addEvent(new SetContClassEvent(_ctrl.getTime() + changeCO2.getTicks(), aux));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.toString());
			}
		}
	}
	
	private void changeWeather() {
		ChangeWeatherDialog changeWeather = new ChangeWeatherDialog(null);
		List<Pair<String, Weather>> aux = new ArrayList<>();
		
		if (changeWeather.open(_roadMap) == 1) {
			aux.add(new Pair<>(changeWeather.getRoad().getId(), changeWeather.getWeather()));
			
			try {
				_ctrl.addEvent(new SetWeatherEvent(_ctrl.getTime() + changeWeather.getTicks(), aux));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.toString());
			}
		}
	}
	
	private void start() {
		_stopped = false;
		
		enableToolBar(false);
		
		run_sim((int)_ticksModel.getValue());
	}
	
	private void enableToolBar(boolean b) {
		_fileChooserButton.setEnabled(b);
		_setContClassButton.setEnabled(b);
		_setWeatherButton.setEnabled(b);
		_runButton.setEnabled(b);
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.toString());
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}
	
	private void stop_sim() {
		_stopped = true;
	}
	
	private void exam1() {
		_HistoryWeather.open();
		
	
	}
	
	private void exam2() {
		_CHistory.open();
		
	
	}
	
	private void exam3() {
		_SHistory.open();
		
	
	}
	
	private ImageIcon loadImage(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(path));
	}
}