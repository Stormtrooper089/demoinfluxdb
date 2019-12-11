package translators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import model.TradeObject;


@Service
public class TickDataToTradeTranslator {
	
	
	public List<TradeObject> normalisedTickData(List<String> tickDataList){
		
		List<TradeObject> tradeObjectDataList =  new ArrayList<TradeObject>();
		
		for (String tick : tickDataList) {
			String [] tickArray = tick.split(",");
			TradeObject tradeObject = new TradeObject();
			if(tickArray.length == 9) {
				tradeObject.setScriptName(tickArray[0]);
				tradeObject.setDate(tickArray[1]);
				tradeObject.setTime(tickArray[2]);
				tradeObject.setOpen(Double.parseDouble(tickArray[3]));
				tradeObject.setHigh(Double.parseDouble(tickArray[4]));
				tradeObject.setLow(Double.parseDouble(tickArray[5]));
				tradeObject.setClose(Double.parseDouble(tickArray[6]));
				tradeObject.setVolume(Double.parseDouble(tickArray[7]));
				tradeObject.setDeviation(Double.parseDouble(tickArray[8]));
			}
			tradeObjectDataList.add(tradeObject);
		}
		return tradeObjectDataList;
	}

}
