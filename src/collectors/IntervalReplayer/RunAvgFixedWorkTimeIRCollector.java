package collectors.IntervalReplayer;

import java.io.File;

import collectors.Collector;

public class RunAvgFixedWorkTimeIRCollector extends AbstractIntervalReplayerBasedCollector{

	public RunAvgFixedWorkTimeIRCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		generateHeaders();
		reset();
	}
	
	private int numRuns;
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException{
		super.logData(data);
		
		int runs = Integer.parseInt(data[Collector.SESSION_NUMBER_INDEX])+1;
		if(runs!=numRuns)
			numRuns=runs;
		
	}
	
	@Override
	public String[] getResults() {
		double workingResults = replayer.getWorkTime(this.studentProject,  this.startTime, this.currentTestTime)[1];
		results[0]=Double.toString(workingResults/numRuns);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		numRuns=0;
		super.reset();
	}
	
	@Override
	protected String getHeaderPhrase() {
		return "Avg Test Time Fixed Work Time";
	}

	@Override
	protected void generateHeaders() {
		headers = new String[1];
		headers[0]=getHeaderPhrase();
	}
	
	@Override
	public boolean requiresTestNames() {
		return false;
	}
	
}
