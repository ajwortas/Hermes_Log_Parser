package collectors.IntervalReplay;

import java.io.File;

public class EditsIntervalReplayerCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String [] header= {
			"Number of Incerts",
			"Characters inserted",
			"Number of Delete",
			"Characters Deleted"
	};
	
	public EditsIntervalReplayerCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		generateHeaders();
	}
	
	@Override
	public String[] getResults() {
		int [] result = this.replayer.getEdits(this.studentProject,this.startTime,this.endTime);
		
		for(int i=0;i<results.length;i++)
			results[i] = Integer.toString(result[i]);
		
		return results;
	}

	@Override
	protected String getHeaderPhrase() {
		return null;
	}
	
	@Override
	protected void generateHeaders() {
		headers = header;
	}

}
