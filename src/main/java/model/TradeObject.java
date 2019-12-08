package model;

public class TradeObject {
	
	private String scriptName;
	private String date;
	private String time;
	private String open;
	private String close;
	private String high;
	private String low;
	private String volume;
	private String deviation;
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getClose() {
		return close;
	}
	public void setClose(String close) {
		this.close = close;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getDeviation() {
		return deviation;
	}
	public void setDeviation(String deviation) {
		this.deviation = deviation;
	}
	@Override
	public String toString() {
		return "TradeObject [scriptName=" + scriptName + ", date=" + date + ", time=" + time + ", open=" + open
				+ ", close=" + close + ", high=" + high + ", low=" + low + ", volume=" + volume + ", deviation="
				+ deviation + "]";
	}
	
	
}
