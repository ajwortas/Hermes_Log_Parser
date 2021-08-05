package collectors.IntervalReplayer;

import java.io.File;
import java.util.List;

public class TestFocusedContextBasedWorkTimeIRCollector extends AbstractIntervalReplayerBasedCollector{

	public TestFocusedContextBasedWorkTimeIRCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
	}

	private long time=0;
	private double [] workingResults;
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException{
		updateTests(data);
		super.logData(data);
		
		
		try {
			time += replayer.getWorkTime(this.studentProject, this.lastTestTime, this.currentTestTime)[0];
		}catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		
		List<String> movedTests = getChangedTests();
		String [] finishedTests = new String[movedTests.size()];
		movedTests.toArray(finishedTests);
		
		if(finishedTests.length>0){
			double amount = (double)time/finishedTests.length;
			for(int i=0;i<testNames.length;i++)
				for(int j=0;j<finishedTests.length;j++)
					if(testNames[i].equals(finishedTests[j].replace("+", "").replace("-", "")))
						workingResults[i]+=amount;
			time=0;
		}
	}
	
	@Override
	public String[] getResults() {
		int numNeverAttempted=0;
		for(int i=0;i<workingResults.length;i++)
			if(workingResults[i]==0)
				numNeverAttempted++;
		double amount=-1.0*((double)time/numNeverAttempted);
		for(int i=0;i<workingResults.length;i++)
			results[i]=Double.toString(workingResults[i]==0?amount:workingResults[i]);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		workingResults=new double[testNames.length];
		time=0;
		super.reset();
	}
	
	@Override
	protected String getHeaderPhrase() {
		return " Context Based Work Time";
	}

	@Override
	public boolean requiresTestNames() {
		return true;
	}
	
}
