package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private final static String _modeDefaultValue = "gui";
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static Integer _ticks = null;
	private static String _mode = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseMode(line);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicks(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg().desc("Number of cycles to run").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Mode to run").build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && !_mode.equals("gui")) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if (_mode == "console") {
			_outFile = line.getOptionValue("o");
		}
	}

	private static void parseTicks(CommandLine line) {
		if (_mode == "console") {
			String s = line.getOptionValue("t");
			
			if (s == null) {
				_ticks = _timeLimitDefaultValue;
			} else {
				_ticks = Integer.valueOf(s);
			}
		}
	}
	
	private static void parseMode(CommandLine line) {
		String s = line.getOptionValue("m");
		
		if (s == null) {
			_mode = _modeDefaultValue;
		} else {
			_mode = s;
		}
	}
	
	private static void initFactories() {
		ArrayList<Builder<Event>> ebs = new ArrayList<>();
		List<Builder<LightSwitchingStrategy>> lss = new ArrayList<>();
		List<Builder<DequeuingStrategy>> dqs = new ArrayList<>();
		
		lss.add(new RoundRobinStrategyBuilder());
		lss.add(new MostCrowdedStrategyBuilder());
		
		dqs.add(new MoveAllStrategyBuilder());
		dqs.add(new MoveFirstStrategyBuilder());
		
		ebs.add(new NewJunctionEventBuilder(new BuilderBasedFactory<>(lss), new BuilderBasedFactory<>(dqs)));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		
		_eventsFactory = new BuilderBasedFactory<>(ebs);
	}

	private static void startBatchMode() throws JSONException, Exception {		
		TrafficSimulator ts = new TrafficSimulator();
		Controller cont = new Controller(ts, _eventsFactory);
		OutputStream out = null;
		
		cont.loadEvents(new FileInputStream(new File(_inFile)));
		
		if (_outFile == null) {
			out = System.out;
		} else {
			out = new PrintStream(_outFile);
		}
		
		cont.run(_ticks, out);
	}
	
	private static void startGUIMode() throws JSONException, Exception {
		TrafficSimulator ts = new TrafficSimulator();
		Controller cont = new Controller(ts, _eventsFactory);
		OutputStream out = null;
		
		if (_inFile != null) {
			cont.loadEvents(new FileInputStream(new File(_inFile)));
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(cont);
			}
		});
		
		if (_outFile == null) {
			out = System.out;
		} else {
			out = new PrintStream(_outFile);
		}
		
		_ticks = 0;
		cont.run(_ticks, out);
	}

	private static void start(String[] args) throws JSONException, Exception {
		initFactories();
		parseArgs(args);
		
		if (_mode.equals("gui")) {
			startGUIMode();
		} else {
			startBatchMode();
		}
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
