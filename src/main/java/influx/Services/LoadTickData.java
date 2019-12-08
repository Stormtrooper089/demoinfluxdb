package influx.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;


@Service
public class LoadTickData {
	
	List<String> tickDataList = new ArrayList<String>();
	
	@PostConstruct
	public void loadData() throws IOException {
		loadDataFromFile();
	}

	private void loadDataFromFile() throws IOException {
		String fileName = "C:\\Users\\ashis\\IdeaProjects\\demoinfluxdb\\src\\main\\resources\\tickData.txt";
		File file = new File(fileName);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String line;
		while((line = br.readLine()) != null){
		    tickDataList.add(line);
		}
		
		
	}

	public List<String> getTickDataList() {
		return tickDataList;
	}

	public void setTickDataList(List<String> tickDataList) {
		this.tickDataList = tickDataList;
	}
	
	

}
