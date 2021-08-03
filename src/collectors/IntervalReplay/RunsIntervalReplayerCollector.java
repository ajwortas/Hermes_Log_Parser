package collectors.IntervalReplay;

import java.io.File;

public class RunsIntervalReplayerCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String headerPhrase;
	
	public RunsIntervalReplayerCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		this("Context Based Time",semesterFolderLocation, pathToStudents);
	}
	
	public RunsIntervalReplayerCollector(String header, File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		headerPhrase=header;
	}
	
	@Override
	public String[] getResults() {
		int result = this.replayer.getRuns(this.studentProject,this.startTime,this.endTime);
		results[0] = Integer.toString(result);
		return results;
	}

	@Override
	protected String getHeaderPhrase() {
		return headerPhrase;
	}

	@Override
	protected void generateHeaders() {
		headers = new String[1];
		headers[0]=getHeaderPhrase();
	}
	
}
