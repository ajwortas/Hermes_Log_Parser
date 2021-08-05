package compiledLogGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import collectors.Collector;
import dataStorage.SuitesAndTests;
import selectYearMapping.YearSelectFactory;
import tools.LogReader;
import tools.LogWriter;
import tools.TestingData;

public class SemesterLogGenerator {

	private static final String [] nameHeader={	
			"Name",							//0
		  	};	

	 
	private final boolean includeName;
	private Collector [] collectors;
	private String outputFileName;
	
	
	public SemesterLogGenerator(Collector [] collectors, boolean includeName) {
		this(collectors,includeName,"assignment#.csv");
	}
	
	public SemesterLogGenerator(Collector [] collectors, boolean includeName,String outputName) {
		this.includeName=includeName;
		this.collectors=collectors;
		outputFileName=outputName;
	}
	
	/**
	 * Use a # to represent the assignment number
	 */
	public void setAssignmentOutput(String outputName) {
		outputFileName=outputName;
	}
	
	public void setCollectors(Collector [] collectors) {
		this.collectors=collectors;
	}
	
	public void readData(File location, File output) throws IOException, ParseException{
		
		LogReader logReader = new LogReader(location);
		
		for(int assignmentIndex=0;assignmentIndex<logReader.getNumAssignments();assignmentIndex++){
			
			List<List<String>> allAssignmentLines = logReader.getAllStudentsAssignmentLogLines(assignmentIndex);
			if(allAssignmentLines==null)
				continue;
			
			List<String> studentNames = logReader.getStudentNames();
			
			SuitesAndTests assignmentSuitesAndTests = TestingData.findAllSuitesAndTests(allAssignmentLines,assignmentIndex);
			
			
			String [] tests = new String[assignmentSuitesAndTests.getTests().size()];
			assignmentSuitesAndTests.getTests().toArray(tests);
			
			CollectorManager dataCollection = new CollectorManager();
			
			//TODO this should be delegated to the CollectorManager
			for(Collector collector:collectors) {
				if(collector.requiresTestNames()) 
					collector.setTestNames(tests);
				if(collector.requiresSuiteMapping())
					collector.setSuiteMapping(YearSelectFactory.getYearMap().getMapping(assignmentIndex));
//					collector.setSuiteMapping(assignmentSuitesAndTests.getMapping());
				if(collector.requiresAssignmentNum())
					collector.setAssignmentNumber(Integer.toString(assignmentIndex));
				
				dataCollection.addCollector(collector);
			}
			
			
			List<String> dataCategories = new ArrayList<String>();
			
			if(includeName)
				Collections.addAll(dataCategories, nameHeader);
			
			dataCategories.addAll(dataCollection.getOrderedHeaders());

			//File Writing and output
			String path=output.toString()+"/"+outputFileName.replaceAll("#", Integer.toString(assignmentIndex));
			File outputData=new File(path);
			outputData.createNewFile();
			FileWriter dataOut=new FileWriter(outputData);
			
			LogWriter.writeToFile(dataOut,dataCategories);
			
			//Data processing for each student
			for(int i=0;i<allAssignmentLines.size();i++){
				try{
					String studentName=studentNames.get(i);
					
					for(Collector collector:collectors)
						if(collector.requiresStudentName()) 
							collector.setStudentName(studentName);
						
					dataCollection.processLog(allAssignmentLines.get(i),1);
					
					List<String> resultsList;
					
					//TODO make a name collector
					if(includeName) {
						resultsList = new ArrayList<String>();
						resultsList.add(studentName);
						resultsList.addAll(dataCollection.getOrderedData());
					}else 
						resultsList = dataCollection.getOrderedData();
					
					
					dataCollection.reset();
				
					if(dataCollection.specialPrint()) 
						LogWriter.simpleWrite(dataOut,resultsList);
					else
						LogWriter.writeToFile(dataOut,resultsList);
				
				}catch(Exception e){
					e.printStackTrace();
					System.out.println(studentNames.get(i) + "   " + path);
					dataCollection.reset();
					return;
				}
			}
			dataOut.close();
		}
		
	}
	
}
