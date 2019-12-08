package influx.Services;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import model.TradeObject;
import translators.TickDataToTradeTranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

@Service
public class DataEntryService {

	@Value( "${dbName:mydb}" )
	private String dbName;
    @Autowired
    private InfluxDB influxDB;
    
    @Autowired
    private LoadTickData loadTickData;

   
    
    private boolean isConnectionALive() {
    	Pong response = influxDB.ping();
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            System.out.println("Error pinging server.");
            return false;
        }
        return true;
    }
    
    public void enterDataToDb() throws InterruptedException{
       
    	List<String> tickDataList = loadTickData.getTickDataList();
    	TickDataToTradeTranslator translator = new TickDataToTradeTranslator();
    	List<TradeObject> tradeObjectList = translator.normalisedTickData(tickDataList);
    	
    	
        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .build();
        
        for (TradeObject tradeObject : tradeObjectList) {
        	
        	
        	 Point point = Point.measurement("Rhovega")
                     .time(System.nanoTime(), TimeUnit.NANOSECONDS)
                     .addField("script", tradeObject.getScriptName())
                     .addField("date", tradeObject.getDate())
                     .addField("time", tradeObject.getTime())
                     .addField("open", tradeObject.getOpen())
                     .addField("high", tradeObject.getHigh())
                     .addField("close", tradeObject.getClose())
                     .addField("low", tradeObject.getLow())
                     .addField("volume", tradeObject.getVolume())
                     .addField("deviation", tradeObject.getDeviation())
                     .build();
        	 
        	 batchPoints.point(point);
        	 
		}
        
        if(isConnectionALive())
        	{
        	System.out.println("The number of entries entered " + batchPoints.getPoints().size());
        		influxDB.write(batchPoints);
        	}
        
    }



}
