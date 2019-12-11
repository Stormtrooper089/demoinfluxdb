package model;

import java.util.Date;

public class TradeObject {
	
	private String scriptName;
	private String date;
	private String time;
	private double open;
	private double close;
	private double high;
	private double low;
	private double volume;
	private double deviation;
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
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public double getDeviation() {
		return deviation;
	}
	public void setDeviation(double deviation) {
		this.deviation = deviation;
	}
	@Override
	public String toString() {
		return "TradeObject [scriptName=" + scriptName + ", date=" + date + ", time=" + time + ", open=" + open
				+ ", close=" + close + ", high=" + high + ", low=" + low + ", volume=" + volume + ", deviation="
				+ deviation + "]";
	}
	
	
	
}
