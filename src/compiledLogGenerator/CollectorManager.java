package compiledLogGenerator;

import java.util.ArrayList;
import java.util.List;

import collectors.Collector;
import tools.concurrency.*;

public class CollectorManager {

	public static boolean enableConcurrency = true;
	private final ThreadManager manager;
	private List<Collector> collectors;
	private int maxReqPass=1;
	private int numNormalPasses=0;
	
	public CollectorManager(){
		this(new ArrayList<Collector>(),null);
	}
	
	public CollectorManager(ThreadManager tm){
		this(new ArrayList<Collector>(),tm);
	}
	
	public CollectorManager(List<Collector> collectors, ThreadManager tm){
		this.collectors=collectors;
		
		for(Collector c:collectors)
			if(c.getRequiredPass()!=Integer.MAX_VALUE) {
				numNormalPasses++;
				if(c.getRequiredPass()>maxReqPass)
					maxReqPass = c.getRequiredPass();
			}
		
		manager = tm;
	}
	
	public CollectorManager(Collector ... collectors) {
		this();
		for(Collector c:collectors)
			addCollector(c);
	}
	
	public void addCollector(Collector c){
		if(c.getRequiredPass()!=Integer.MAX_VALUE) {
			numNormalPasses++;
			if(c.getRequiredPass()>maxReqPass)
				maxReqPass = c.getRequiredPass();
		}
		collectors.add(c);
	}
	
	public List<Collector> getCollectors(){
		return collectors;
	}
	
	public void processLog(List<String> dataLines) throws Exception {

		for(int i=1;i<=maxReqPass;i++)
			for(String dataLine:dataLines){
				String [] splitLine=dataLine.split(",");
				
				if(enableConcurrency&&maxReqPass==1) {
					Joiner j = new Joiner(this.numNormalPasses);
					
					for(Collector collector:collectors)
						if(collector.getRequiredPass()==i)
							manager.addTask(new CollectorTask(collector,j,splitLine));
					j.finish();
				}else {
					for(Collector collector:collectors)
						if(collector.getRequiredPass()==i)
							collector.logData(splitLine);
				}
			}
		
		String [] lastLine = dataLines.get(dataLines.size()-1).split(",");
		for(Collector collector:collectors)
			if(collector.getRequiredPass()==Integer.MAX_VALUE)
				collector.logData(lastLine);
	}
	
	public List<String> getOrderedHeaders(){
		List<String> headers = new ArrayList<String>();
		for(Collector collector:collectors)
			for(String header:collector.getHeaders())
				headers.add(header);
		return headers;
	}
	
	public List<String> getOrderedData(){
		List<String> dataEntries = new ArrayList<String>();
		for(Collector collector:collectors)
			for(String dataEntry:collector.getResults())
				dataEntries.add(dataEntry);
		return dataEntries;
	}
	
	public List<String []> getCertainHeadersAndData(String [] desiredTests) {

		List<String []> retval = new ArrayList<String[]>();
		for(Collector collector:collectors) {
			String [] headers = collector.getHeaders();
			String [] data = collector.getResults();
			boolean requiresTestName = collector.requiresTestNames();
			
			for(int i=0;i<headers.length;i++) {
				if(requiresTestName && !headerContainsTests(headers[i],desiredTests)) continue;
				String [] addToReturn = {headers[i],data[i]};
				retval.add(addToReturn);
			}
		}
		
		return retval;
	}
	
	private boolean headerContainsTests(String header, String [] desiredTests) {
		for(String test:desiredTests)
			if(header.contains(test))
				return true;
		return false;
	}
	
	
	public void reset(){
		for(Collector collector:collectors)
			collector.reset();
	}
	
	public boolean specialPrint() {
		for(Collector collector:collectors)
			if(!collector.otherCollectorCompatable())
				return true;
		return false;
	}
	
	public List<String> getCollectorNames(){
		List<String> retval = new ArrayList<>();
		for(Collector collector:collectors)
			retval.add(collector.getClass().getSimpleName());
		return retval;
	}
}

	
