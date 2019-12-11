package model;
import org.apache.commons.lang3.StringUtils;



import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;

// This class should only have fields that don't mutate with every snapshot coming in from the venue so that we don't create message context each time we create new Snapshot
public class MessageContext {

	public static final String EMPTY_STRING = "";
	protected int bookSeqNum, exchangeSeqNum;
	protected long exchangeTime, feedHandlerTime, aggregationTime, publishingTime;
	
	protected String exchange = EMPTY_STRING,
			symbol = EMPTY_STRING,
			securityId = EMPTY_STRING,
			stream = EMPTY_STRING;
	
	public void reset() {
		bookSeqNum = 0;
		exchangeSeqNum = 0;
		exchangeTime = 0;
		feedHandlerTime = 0;
		aggregationTime = 0;
		publishingTime = 0;

		exchange = EMPTY_STRING;
		symbol = EMPTY_STRING;
		securityId = EMPTY_STRING;
        stream = EMPTY_STRING;
	}

	public void copyFrom(MessageContext from) {
		this.bookSeqNum = from.bookSeqNum;
		this.exchangeSeqNum = from.exchangeSeqNum;
		this.exchangeTime = from.exchangeTime;
		this.feedHandlerTime = from.feedHandlerTime;
		this.aggregationTime = from.aggregationTime;
		this.publishingTime = from.publishingTime;

		this.exchange = from.exchange;
		this.symbol = from.symbol;
		this.securityId = from.securityId;
        this.stream = from.stream;
	}

	public void read(BytesIn bytes){
		this.bookSeqNum = bytes.readInt();
		this.exchangeSeqNum = bytes.readInt();
		this.exchangeTime = bytes.readLong();
		this.feedHandlerTime = bytes.readLong();
		this.aggregationTime = bytes.readLong();
		this.publishingTime = bytes.readLong();
	
		this.symbol = bytes.read8bit();
		this.securityId = bytes.read8bit();
		this.exchange = bytes.read8bit();
		this.stream = bytes.read8bit();

	}

	public void write(BytesOut bytes){

			bytes.writeInt(bookSeqNum)
				.writeInt(exchangeSeqNum)
				.writeLong(exchangeTime)
				.writeLong(feedHandlerTime)
				.writeLong(aggregationTime)
				.writeLong(publishingTime)
			
				.write8bit(symbol)
				.write8bit(securityId)
				.write8bit(exchange)
				.write8bit(StringUtils.defaultIfEmpty(stream,""));
	
	}

	public int getBookSeqNum() {
		return bookSeqNum;
	}

	public void setBookSeqNum(int bookSeqNum) {
		this.bookSeqNum = bookSeqNum;
	}

	public int getExchangeSeqNum() {
		return exchangeSeqNum;
	}

	public void setExchangeSeqNum(int exchangeSeqNum) {
		this.exchangeSeqNum = exchangeSeqNum;
	}

	public long getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(long exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

	public long getFeedHandlerTime() {
		return feedHandlerTime;
	}

	public void setFeedHandlerTime(long feedHandlerTime) {
		this.feedHandlerTime = feedHandlerTime;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	

	public long getAggregationTime() {
		return aggregationTime;
	}

	public void setAggregationTime(long aggregationTime) {
		this.aggregationTime = aggregationTime;
	}

	public long getPublishingTime() {
		return publishingTime;
	}

	public void setPublishingTime(long publishingTime) {
		this.publishingTime = publishingTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + (int) (bookSeqNum ^ (bookSeqNum >>> 32));
		result = prime * result
				+ ((exchange == null) ? 0 : exchange.hashCode());

		result = prime * result + exchangeSeqNum;
		result = prime * result + (int) (exchangeTime ^ (exchangeTime >>> 32));
		result = prime * result
				+ (int) (feedHandlerTime ^ (feedHandlerTime >>> 32));
		result = prime * result
				+ ((securityId == null) ? 0 : securityId.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + ((stream == null) ? 0 : stream.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageContext other = (MessageContext) obj;
		
		if (bookSeqNum != other.bookSeqNum)
			return false;
		if (exchange == null) {
			if (other.exchange != null)
				return false;
		} else if (!exchange.equals(other.exchange))
			return false;
		
		if (exchangeSeqNum != other.exchangeSeqNum)
			return false;
		if (exchangeTime != other.exchangeTime)
			return false;
		if (feedHandlerTime != other.feedHandlerTime)
			return false;
		if (securityId == null) {
			if (other.securityId != null)
				return false;
		} else if (!securityId.equals(other.securityId))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (stream == null) {
			if (other.stream != null)
				return false;
		} else if (!stream.equals(other.stream))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessageContext [ bookSeqNum="
				+ bookSeqNum + ", exchangeSeqNum=" + exchangeSeqNum
				+ ", exchangeTime=" + exchangeTime 
				+ ", feedHandlerTime=" + feedHandlerTime
				+ ", aggregationTime=" + aggregationTime
				+ ", publishingTime=" + publishingTime
				+ ", exchange=" + exchange + ", symbol=" + symbol
				+ ", securityId=" + securityId
				+ ", stream=" + stream + "]";
	}
}
