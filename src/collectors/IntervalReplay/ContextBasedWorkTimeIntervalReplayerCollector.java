package collectors.IntervalReplay;

import java.io.File;
import java.util.Date;

public class ContextBasedWorkTimeIntervalReplayerCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String headerPhrase;
	
	public ContextBasedWorkTimeIntervalReplayerCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		this("Context Based Time",semesterFolderLocation, pathToStudents);
	}
	
	public ContextBasedWorkTimeIntervalReplayerCollector(String header, File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		headerPhrase=header;
		generateHeaders();
	}
	
	@Override
	public String[] getResults() {
		long result = this.replayer.getWorkTime(this.studentProject,this.startTime,this.endTime)[0];
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
