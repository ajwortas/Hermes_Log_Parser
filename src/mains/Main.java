package mains;

import java.io.File;
import java.io.FileWriter;

import collectors.*;
import collectors.EventCollectors.*;
import collectors.EventCollectors.FineGrained.*;
import collectors.FineGrained.*;
import collectors.IntervalReplayer.*;
import collectors.IntervalReplayer.FineGrained.*;
import collectors.IntervalReplayer.Runs.*;
import collectors.IntervalReplayer.Timing.*;
import collectors.StandardCollectors.*;
import compiledLogGenerator.CollectorManager;
import compiledLogGenerator.LocalChecksLogData;
import compiledLogGenerator.SemesterLogGenerator;
import selectYearMapping.Comp524Fall2020;
import selectYearMapping.YearSelectFactory;
import tools.files.CSVParser;
import tools.files.LogWriter;
public class Main {

	private static final int breakTime= (int)(10*60);
	
	public static void main(String[] args) {
		try {
//			runEventsAnalysis();
//			runAnalysis();
//			eventLogs();
//			soloTesting();
//			replayData();
			attemptsAndTimings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void attemptsAndTimings() throws Exception {
		final File rawFolder = new File("I:\\Research\\Log_Parsing\\ClassFolders\\Comp301\\Summer20");
		final String pathing = "Assignment "+AbstractIntervalReplayerBasedCollector.numberReplace+"_named/Assignment "+AbstractIntervalReplayerBasedCollector.numberReplace;
		Collector [] collectors = {
//				new AttemptsCollectorV2(),
//				new AvgTimeToSolveIRCollector(rawFolder,pathing),
//				new AvgTestFocusedTimeToSolveIRCollector(rawFolder,pathing),
//				new TotalSessionsCollector(),
//				new TestPassPercentCollector(),
//				new TestScorePercentCollector(),
				new DateFirstTestedCollector(),
				new TestFocusedFixedWorkTimeIRCollector(rawFolder,pathing),
				new TestFocusedContextBasedWorkTimeIRCollector(rawFolder,pathing),
		};
		
		File [] inputs = {
				new File("InputFolders/Comp301/Summer20"),
		};
		File [] outputs = {
				new File("FourthPaper"),
		};

		
		for(int i=0;i<inputs.length;i++) {
			try {
				SemesterLogGenerator smg = new SemesterLogGenerator(collectors,true,"assignment#_TestTimes.csv");
				smg.readData(inputs[i], outputs[i]);
				smg.tm.end();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	private static void replayData() throws Exception {
		final File rawFolder = new File("I:\\Research\\Log_Parsing\\ClassFolders\\Comp301\\Summer20");
		final String pathing = "Assignment "+AbstractIntervalReplayerBasedCollector.numberReplace+"_named/Assignment "+AbstractIntervalReplayerBasedCollector.numberReplace;
	
		Collector [] collectors = {
			new ContextBasedWorkTimeIRCollector(rawFolder,pathing),
			new FixedWorkTimeIRCollector(rawFolder,pathing),
			new RunAvgFixedWorkTimeIRCollector(rawFolder,pathing),
			new RunAvgContextBasedWorkTimeIRCollector(rawFolder,pathing),
//			new EditsIRCollector(rawFolder,pathing),
//			new RunsIRCollector(rawFolder,pathing),
			new TestFocusedFixedWorkTimeIRCollector(rawFolder,pathing),
			new TestFocusedContextBasedWorkTimeIRCollector(rawFolder,pathing),
		};
	
	
		File [] inputs = {
				new File("InputFolders/Comp301/Summer20"),
		};
		File [] outputs = {
				new File("ThirdPaper"),
		};

		
		for(int i=0;i<inputs.length;i++) {
			try {
				
				SemesterLogGenerator smg = new SemesterLogGenerator(collectors,true,"assignment#_IntervalReplayer.csv");
				smg.readData(inputs[i], outputs[i]);
				smg.tm.end();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void runAnalysis() {
		File [] inputs = {
//				new File("InputFolders/Comp533/Spring20"),
//				new File("InputFolders/Comp533/Spring19"),
//				new File("InputFolders/Comp533/Spring18"),
//				new File("InputFolders/Comp524/Fall2020_hash"),
//				new File("InputFolders/Comp524/Fall2019"),
				new File("InputFolders/Comp301/Summer20"),
				
		};
		File [] outputs = {
//				new File("SecondPaperWork/Comp533Spring2020"),
//				new File("SecondPaperWork/Comp533Spring2019"),
//				new File("SecondPaperWork/Comp533Spring2018"),
//				new File("SecondPaperWork/Comp524_Newer"),
//				new File("SecondPaperWork/Comp524Fall2019"),
				new File("ThirdPaper"),
		};
		
		Collector [] collectors = {
				new TotalAttemptsCollector(),
//				new WorkingSetStatisicsCollector(),
//				new TestingSetStatisicsCollector(),
//				new NaiveAttemptsCollector(),
				
				new PercentPassedCollector(4),
				new TotalTimeCollector(),
				new DateFirstTestedCollector(),
				new DateLastTestedCollector(),
				
				new AttemptsCollectorV2(),
				new WorkTimeCollector(breakTime, false),
				new IncreasingAttemptsCollector(),
				new DecreasingAttemptsCollectorV2(),
				new FinalStatusCollector(),
		};
		
		YearSelectFactory.setYearMap(new Comp524Fall2020());
		
		for(int i=0;i<inputs.length;i++) {
			try {
				new SemesterLogGenerator(collectors,true,"assignment#.csv").readData(inputs[i], outputs[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void soloTesting() {
		File directory = new File("C:\\Users\\Andrew\\OneDrive\\Comp 524\\Assignment Three");
		Collector [] collectors = {
				new TotalAttemptsCollector(),
				new AttemptsCollectorV2(),
		};
		LocalChecksLogData.getData(directory,3,collectors);
	}
	
	private static void eventLogs() {
		
		File [] inputs = {
//				new File("InputFolders/Comp533/Spring20"),
//				new File("InputFolders/Comp533/Spring19"),
//				new File("InputFolders/Comp533/Spring18"),
				new File("InputFolders/Comp524/Fall2020_hash"),
//				new File("InputFolders/Comp524/Fall2019"),
				
		};
		File [] outputs = {
//				new File("SecondPaperWork/Comp533Spring2020_events"),
//				new File("SecondPaperWork/Comp533Spring2019_events"),
//				new File("SecondPaperWork/Comp533Spring2018_events"),
				new File("SecondPaperWork/Comp524Fall2020_events"),
//				new File("SecondPaperWork/Comp524Fall2019_events"),
		};
		
		YearSelectFactory.setYearMap(new Comp524Fall2020());
		
		Collector [] test = {new TestWorkingEvent()};
		Collector [] suite = {new SuiteWorkingEvent()};
		Collector [] tts = {new TestToSuiteWorkingEvent()};
		Collector [] btc = {new BreakEvent(breakTime)};
		
		for(int i=0;i<inputs.length;i++) {
			try {
				new SemesterLogGenerator(suite,false,"assignment#_suiteEvents.csv").readData(inputs[i], outputs[i]);
				new SemesterLogGenerator(test,false,"assignment#_testEvents.csv").readData(inputs[i], outputs[i]);
				new SemesterLogGenerator(tts,false,"assignment#_testToSuiteEvents.csv").readData(inputs[i], outputs[i]);
				new SemesterLogGenerator(btc,false,"assignment#_breakEvents.csv").readData(inputs[i], outputs[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void runEventsAnalysis() {
		File [] inputs = {
//				new File("InputFolders/Comp533/Spring20"),
//				new File("InputFolders/Comp533/Spring19"),
//				new File("InputFolders/Comp533/Spring18"),
//				new File("InputFolders/Comp524/Fall2020_hash"),
//				new File("InputFolders/Comp524/Fall2019"),
				new File("InputFolders/Comp301/Summer20"),
		};
		File [] outputs = {
//				new File("SecondPaperWork/Comp533Spring2020"),
//				new File("SecondPaperWork/Comp533Spring2019"),
//				new File("SecondPaperWork/Comp533Spring2018"),
//				new File("SecondPaperWork/Comp524_Newer_events"),
//				new File("SecondPaperWork/Comp524Fall2019"),
				new File("FourthPaper"),
		};
		
		Collector [][] recursives = {
//				{new TestWorkingSetEvent("ListToStringDeepChecker")},
//				{new TestWorkingSetEvent("ListToStringChecker")},
//				{new TestWorkingSetEvent("BaseCaseSExpressionToStringChecker")},
//				{new TestWorkingSetEvent("BaseCaseListToStringChecker")}
				{new TestRunsCollector()},
//				{new TestWorkingEvent()},
//				{new TestStatusEvent()},
				
		};
		
		
		
//		YearSelectFactory.setYearMap(new Comp524Fall2020());
		CollectorManager.enableConcurrency=false;
		for(int i=0;i<recursives.length;i++) {
			try {
				new SemesterLogGenerator(recursives[i],false,i+"assignment#_events.csv").readData(inputs[0], outputs[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * This was just used to correct data displaying in the WorkTimeStats Collector
	 */
	private static void fixCSVData() throws Exception {
		for(int i=0;i<7;i++) {
			File csvLoc = new File("./NewVersionTest/Assignment"+i+"_TestWorkTimes.csv");
			String [][] readFile = (new CSVParser(csvLoc)).readCSV();
			
			for(int j=0;j<readFile[0].length;j++) {
				int lastOpen=0;
				for(int k=0;k<readFile.length;k++) {
					System.out.println(readFile[k][j]);
					if(!readFile[k][j].equals("")) {
						if(lastOpen!=k) {
							readFile[lastOpen][j] = readFile[k][j];
							readFile[k][j]="";
						}
						lastOpen++;
					}else {
						System.out.println(readFile[k][j]);
					}
				}
			}
			
			FileWriter writeTo = new FileWriter(csvLoc);
			LogWriter.writeToFileMultipleLines(writeTo, readFile);
			writeTo.close();
		}
	}
	
}
