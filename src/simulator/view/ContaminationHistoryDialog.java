package simulator.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Event;
import simulator.model.Road;
import simulator.control.Controller;
import simulator.misc.Pair;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class ContaminationHistoryDialog extends JDialog implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private static final String _HELPMSG = "<html><p>Information about roads contamination.</p></html>";
	List<Pair<Integer, List<Pair<String, Integer>>>> _history;
	private JSpinner _contLimit;

	private HistoryTableModel _historyTableModel;

	class HistoryTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		String[] _header = { "Tick", "Roads" };

		@Override
		public int getRowCount() {
			return _history.size();
		}

		@Override
		public int getColumnCount() {
			return _header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Pair<Integer, List<Pair<String, Integer>>> e = _history.get(rowIndex);
			String v = "";

			switch (columnIndex) {
			case 0:
				v = e.getFirst() + "";
				break;
			case 1:
				v = filter(e.getSecond());
				break;
			}
			return v;
		}

		private String filter(List<Pair<String, Integer>> l) {
			List<String> roads = new ArrayList<>();
			Integer c = (Integer) _contLimit.getValue();

			for (Pair<String, Integer> e : l) {
				if (e.getSecond() >= c) {
					roads.add(e.getFirst());
				}
			}
			return roads.toString();
		}

		private void update() {
			fireTableDataChanged();
		}

	}

	public ContaminationHistoryDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {

		_history = new ArrayList<>();
		_historyTableModel = new HistoryTableModel();

		setTitle("Roads Contamination History");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel help = new JLabel(_HELPMSG);
		help.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(help);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_contLimit = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		_contLimit.setToolTipText("The contamination limit to be used in the table below");
		_contLimit.setPreferredSize(new Dimension(80, 25));

		viewsPanel.add(new JLabel("Contamination Limit:"));
		viewsPanel.add(_contLimit);

		JButton cancelButton = new JButton("Close");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ContaminationHistoryDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("Update");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_historyTableModel.update();
			}
		});
		buttonsPanel.add(okButton);

		mainPanel.add(new JScrollPane(new JTable(_historyTableModel)));

		setPreferredSize(new Dimension(500, 500));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open() {
		_historyTableModel.update();
		setVisible(true);
	}

	private void updateHistory(RoadMap map, int time) {
		List<Pair<String, Integer>> l = new ArrayList<>();
		for (Road r : map.getRoads()) {
			l.add(new Pair<>(r.getId(), r.getTotalCO2()));
		}
		_history.add(new Pair<>(time, l));
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(() -> updateHistory(map, time));
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(() -> _history.clear());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
	}

}