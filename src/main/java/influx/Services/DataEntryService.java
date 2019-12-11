package influx.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import model.CreateDummySnapshot;
import model.L2Snapshot;
import model.TradeObject;
import translators.TickDataToTradeTranslator;

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
    
    public void enterDataToDb() throws InterruptedException, ParseException{
       
    	List<String> tickDataList = loadTickData.getTickDataList();
    	TickDataToTradeTranslator translator = new TickDataToTradeTranslator();
    	List<TradeObject> tradeObjectList = translator.normalisedTickData(tickDataList);
    	
    	
    	L2Snapshot l2SnapShot = CreateDummySnapshot.createLOB();
    	influxDB.enableBatch(100, 200, TimeUnit.NANOSECONDS);
        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .build();
        
//        List<Level> allLevels = l2SnapShot.getAllLevels();
//        
//        for (Level level : allLevels) {
//        
//        	Point point = Point.measurement("L2SNAPSHOT")
//        			.time(System.nanoTime(), TimeUnit.NANOSECONDS)
//        			.addField("symbol", l2SnapShot.getContext().getSymbol())
//        			.addField("exchange", l2SnapShot.getContext().getExchange())
//        			.addField("isBid", level.isBid())
//        			.addField("price", level.getPrice())
//        			.addField("quantity", level.getQuantity())
//        			.addField("priceDaemon", level.getPriceDenom())
//        			.build();
//
//
//        	
//        	batchPoints.point(point);
//        	
//		}
        
        Instant stamp = Instant.now();
        long initialEpochSecInstantInNano = stamp.getEpochSecond() * 1000000000;
        long initialEpochSecInNano = System.nanoTime();
        for (TradeObject tradeObject : tradeObjectList) {
        	long elapsedNanos = System.nanoTime()-initialEpochSecInNano;
    		long nanos = initialEpochSecInstantInNano+elapsedNanos;
    		
    		//String time = "2009-07-20 05-33"; 
    		String time = tradeObject.getDate()+ " " + tradeObject.getTime();                              
    		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd hh:mm");
    		Date dt = df.parse(time);                                      
    		Long l = dt.getTime(); //Equal or Greater than JDK 1.5  
    		System.out.println("the long time of the date is "  + l);
        	Point point = Point.measurement("HDFC")
                    .time(nanos, TimeUnit.NANOSECONDS)
                    .addField("script", tradeObject.getScriptName())
                    .addField("date", tradeObject.getDate())
                    .addField("timeCol", tradeObject.getTime())
                    .addField("open", tradeObject.getOpen())
                    .addField("high", tradeObject.getHigh())
                    .addField("close", tradeObject.getClose())
                    .addField("low", tradeObject.getLow())
                    .addField("volume", tradeObject.getVolume())
                    .addField("timeT",l)
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
