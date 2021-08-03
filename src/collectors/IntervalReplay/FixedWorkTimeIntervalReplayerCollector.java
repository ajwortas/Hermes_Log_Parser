package collectors.IntervalReplay;

import java.io.File;
import java.util.Date;

public class FixedWorkTimeIntervalReplayerCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String headerPhrase;
	
	public FixedWorkTimeIntervalReplayerCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		this("Fixed Work Time",semesterFolderLocation, pathToStudents);
	}
	
	public FixedWorkTimeIntervalReplayerCollector(String header, File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		headerPhrase=header;
	}
	
	@Override
	public String[] getResults() {
		long result = this.replayer.getWorkTime(this.studentProject,this.startTime,this.endTime)[1];
		results[0] = (new Date(result)).toString();
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
