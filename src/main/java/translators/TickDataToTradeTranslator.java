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
				tradeObject.setOpen(tickArray[3]);
				tradeObject.setHigh(tickArray[4]);
				tradeObject.setLow(tickArray[5]);
				tradeObject.setClose(tickArray[6]);
				tradeObject.setVolume(tickArray[7]);
				tradeObject.setDeviation(tickArray[8]);
			}
			tradeObjectDataList.add(tradeObject);
		}
		return tradeObjectDataList;
	}

}
