package collectors.IntervalReplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;

import analyzer.logAnalyzer.AnIntervalReplayer;
import collectors.AbstractCollector;
import grader.basics.interval.IntervalDriver;

public abstract class AbstractIntervalReplayerBasedCollector extends AbstractCollector {

	public static final String numberReplace = "###";

	private final String unfixedPathToStudents;
	private String fixedPathToStudents;
	protected File studentProject;
	
//	private HashMap<String, Date> lastAssignmentFinalCheck = new HashMap<>(); 
	protected Date lastKnown=null, firstKnown=null;
	protected final AnIntervalReplayer replayer;
	
	public AbstractIntervalReplayerBasedCollector(File semesterFolderLocation, String pathToStudents) throws Exception {	
		if(!semesterFolderLocation.exists()) 
			throw new FileNotFoundException();
		if(semesterFolderLocation.isFile())
			throw new Exception("Location must be a directory");
		if(semesterFolderLocation.getAbsolutePath().contains(numberReplace))
			throw new Exception("Directory path must not contain the sequence: "+numberReplace);
		
		replayer = new AnIntervalReplayer(IntervalDriver.MULTIPLIER, IntervalDriver.DEFAULT_THRESHOLD, IntervalDriver.TRACE);
		unfixedPathToStudents=semesterFolderLocation.getAbsolutePath()+"/"+pathToStudents;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException{
		try {
			if(firstKnown==null)
				firstKnown=this.parseDate(data[TIME_TESTED_INDEX]);
			lastKnown=this.parseDate(data[TIME_TESTED_INDEX]);
		}catch(Exception e) {
			throw new IllegalArgumentException("Error when parsing date");
		}
	}
	
	protected Date monthEarlier(Date init) {
		Calendar c = Calendar.getInstance();
		c.setTime(init);
		c.set(Calendar.MONTH, (c.get(Calendar.MONTH)+11)%12);
		return c.getTime();
	}
	
	
	@Override
	public void reset() {
		super.reset();
		lastKnown=null;
		studentProject=null;
	}
	
	@Override
	public void setStudentName(String name) {
		super.setStudentName(name);
		File studentSubmission = new File(fixedPathToStudents+"/"+name+"/Submission attachment(s)");
		try {
			studentProject = findLogsFolder(studentSubmission);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private File findLogsFolder(File studentSubmission) throws Exception {
		File [] subdirs = studentSubmission.listFiles(File::isDirectory);
		if(subdirs.length == 0)
			return null;
		
		//Sees if a subdirectory is the Logs folder
		for(File subdir:subdirs) 
			if(subdir.getName().equals("Logs"))
				return studentSubmission;
		
		//If not looks deeper
		for(File subdir:subdirs) {
			File file = findLogsFolder(subdir);
			if(file!=null)
				return file;
		}
		return null;
	}
	
	@Override
	public void setAssignmentNumber(String assignmentNum) {
		super.setAssignmentNumber(assignmentNum);
		fixedPathToStudents = unfixedPathToStudents.replaceAll(numberReplace, assignmentNum);
	}

	@Override
	public boolean requiresStudentName() {
		return true;
	}
	
	@Override
	public boolean requiresAssignmentNum() {
		return true;
	}
}
