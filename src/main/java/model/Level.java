package model;

import java.util.Objects;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.wire.AbstractBytesMarshallable;

public class Level extends AbstractBytesMarshallable implements Comparable<Level> {
	public static final String EMPTY_STRING = "";
	protected boolean isBid;
	protected boolean firm;
	protected boolean synthetic;
	protected long price, aggregatedPrice, referencePrice, mid, incomingMid;
	protected long priceDenom;
	protected int quantity, createdFromQuantity, numOrders;
	protected int spread, ruleSpread,skew,incomingSpread;
	protected short spreadUnit, ruleSpreadUnit, incomingSpreadUnit, skewUnit;
	
	protected String auditLog = EMPTY_STRING;
	protected transient MessageContext context;

	public Level() {
		init();
		this.context = new MessageContext();
	}

	public void reset() {
		init();
		this.context.reset();
	}

	public void init() {
		this.isBid = false;
		this.firm=true;
		this.synthetic = false;
		this.price = 0;
		this.priceDenom = 1;
		this.quantity = 0;
		this.numOrders = 0;
		this.aggregatedPrice = 0;
		this.referencePrice = 0;
		this.createdFromQuantity = 0;
		this.spread=0;
		this.ruleSpread = 0;
		this.mid=0;
		this.skew=0;
		this.incomingSpread=0;
		this.incomingMid=0;
		this.spreadUnit=0;
		this.ruleSpreadUnit = 0;
		this.incomingSpreadUnit=0;
		this.skewUnit=0;
		
		this.auditLog = EMPTY_STRING;
		
		
	}

	public Level(Level level) {
		init();
		this.isBid = level.isBid();
		this.firm = level.isFirm();
		this.synthetic = level.isSynthetic();
		this.price = level.getPrice();
		this.priceDenom = level.getPriceDenom();
		this.quantity = level.getQuantity();
		this.numOrders = level.getNumOrders();
		this.aggregatedPrice = level.getAggregatedPrice();
		this.referencePrice = level.getReferencePrice();
		this.createdFromQuantity = level.getCreatedFromQuantity();
		this.spread = level.getSpread();
		this.ruleSpread = level.getRuleSpread();
		this.mid = level.getMid();
		this.skew = level.getSkew();
		this.incomingSpread = level.getIncomingSpread();
		this.incomingMid = level.getIncomingMid();
		this.spreadUnit = level.getSpreadUnit();
		this.ruleSpreadUnit = level.getRuleSpreadUnit();
		this.incomingSpreadUnit = level.getIncomingSpreadUnit();
		this.skewUnit = level.getSkewUnit();
	
		this.auditLog = level.getAuditLog();
		this.context.copyFrom(level.context);
		
	}

	public Level(boolean isBid, long price, long priceDenom, int quantity, int numOrders, boolean isFirm) {
		init();
		this.isBid = isBid;
		this.firm = isFirm;
		this.price = price;
		this.priceDenom = priceDenom;
		this.quantity = quantity;
		this.numOrders = numOrders;
		
		this.auditLog = String.valueOf(price);		
	}

	public Level(boolean isBid, long price, long priceDenom, int quantity, int numOrders) {
		init();
		this.isBid = isBid;
		this.firm = true;
		this.price = price;
		this.priceDenom = priceDenom;
		this.quantity = quantity;
		this.numOrders = numOrders;
		
		this.auditLog = String.valueOf(price);		
	}
	
	public Level(boolean isBid, long price, int quantity) {
		init();
		this.isBid = isBid;
		this.firm = true;
		this.price = price;
		this.priceDenom = 1;
		this.quantity = quantity;
		this.numOrders = 1;
		
		this.auditLog = String.valueOf(price);	
	}

	public Level(boolean isBid, long price, int quantity, long priceDenom) {
		init();
		this.isBid = isBid;
		this.firm = true;
		this.price = price;
		this.priceDenom = priceDenom;
		this.quantity = quantity;
		this.numOrders = 1;
		
		this.auditLog = String.valueOf(price);	
	}
	
	public void set(boolean isBid, long price, int quantity) {
		this.isBid = isBid;
		this.firm = true;
		this.price = price;
		this.priceDenom = 1;
		this.quantity = quantity;
		this.numOrders = 1;
		
		//this.sourceName = EMPTY_STRING;
		this.auditLog = String.valueOf(price);
		this.context = new MessageContext();	
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void readMarshallable(BytesIn bytes) throws IORuntimeException {
		isBid = bytes.readBoolean();
		firm = bytes.readBoolean();
		synthetic = bytes.readBoolean();
		price = bytes.readLong();
		priceDenom = bytes.readLong();
		quantity = bytes.readInt();
		numOrders = bytes.readInt();
		aggregatedPrice = bytes.readLong();
		referencePrice = bytes.readLong();
		createdFromQuantity = bytes.readInt();
		spread = bytes.readInt();
		ruleSpread = bytes.readInt();
		mid = bytes.readLong();
		skew = bytes.readInt();
		incomingSpread = bytes.readInt();
		incomingMid = bytes.readLong();
		spreadUnit = bytes.readShort();
		ruleSpreadUnit = bytes.readShort();
		incomingSpreadUnit = bytes.readShort();
		skewUnit = bytes.readShort();
		
		auditLog = bytes.read8bit();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void writeMarshallable(BytesOut bytes) {
		bytes// .writeInt(id)
				.writeBoolean(isBid).writeBoolean(firm).writeBoolean(synthetic)
				.writeLong(price).writeLong(priceDenom)
				.writeInt(quantity).writeInt(numOrders)
				.writeLong(aggregatedPrice).writeLong(referencePrice)
				.writeInt(createdFromQuantity).writeInt(spread).writeInt(ruleSpread)
				.writeLong(mid)
				.writeInt(skew).writeInt(incomingSpread)
				.writeLong(incomingMid)
				.writeShort(spreadUnit).writeShort(ruleSpreadUnit).writeShort(incomingSpreadUnit).writeShort(skewUnit)
				.write8bit(auditLog);
	}
	
	public boolean isFirm() {
		return firm;
	}

	public void setFirm(boolean firm) {
		this.firm = firm;
	}

	public boolean isBid() {
		return isBid;
	}

	public void setBid(boolean isBid) {
		this.isBid = isBid;
	}

	public long getPrice() {
		return price;
	}
 
	public void setPrice(long price) {
		this.price = price;
	}

	public long getPriceDenom() {
		return priceDenom;
	}

	public void setPriceDenom(long priceDenom) {
		this.priceDenom = priceDenom;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getNumOrders() {
		return numOrders;
	}

	public void setNumOrders(int numOrders) {
		this.numOrders = numOrders;
	}

	
	public String getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(String auditLog) {
		this.auditLog = auditLog;
	}

	public void setContext(MessageContext context) {
		if (this.context == null)
			this.context = context;
		else
			this.context.copyFrom(context);
	}

	public MessageContext getContext() {
		return context;
	}

	public long getAggregatedPrice() {
		return aggregatedPrice;
	}

	public void setAggregatedPrice(long aggregatedPrice) {
		this.aggregatedPrice = aggregatedPrice;
	}

	public long getReferencePrice() {
		return referencePrice;
	}

	public void setReferencePrice(long referencePrice) {
		this.referencePrice = referencePrice;
	}
	
	public int getSpread() {
		return spread;
	}

	public void setSpread(int spread) {
		this.spread = spread;
	}

	public int getSkew() {
		return skew;
	}

	public void setSkew(int skew) {
		this.skew = skew;
	}
	
	public int getIncomingSpread() {
		return incomingSpread;
	}

	public void setIncomingSpread(int incomingSpread) {
		this.incomingSpread = incomingSpread;
	}
	
	public long getIncomingMid() {
		return incomingMid;
	}

	public void setIncomingMid(long incomingMid) {
		this.incomingMid = incomingMid;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}
	
	public short getSpreadUnit() {
		return spreadUnit;
	}

	public void setSpreadUnit(short spreadUnit) {
		this.spreadUnit = spreadUnit;
	}

	public short getIncomingSpreadUnit() {
		return incomingSpreadUnit;
	}

	public void setIncomingSpreadUnit(short incomingSpreadUnit) {
		this.incomingSpreadUnit = incomingSpreadUnit;
	}

	public short getSkewUnit() {
		return skewUnit;
	}

	public void setSkewUnit(short skewUnit) {
		this.skewUnit = skewUnit;
	}
	
	public int getRuleSpread() {
		return ruleSpread;
	}

	public void setRuleSpread(int ruleSpread) {
		this.ruleSpread = ruleSpread;
	}
	
	public short getRuleSpreadUnit() {
		return ruleSpreadUnit;
	}

	public void setRuleSpreadUnit(short ruleSpreadUnit) {
		this.ruleSpreadUnit = ruleSpreadUnit;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isBid, price, priceDenom, quantity, numOrders);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Level other = (Level) obj;
		
		if (isBid != other.isBid) {
			return false;
		}
		if (numOrders != other.numOrders) {
			return false;
		}
		if (price != other.price) {
			return false;
		}
		if (priceDenom != other.priceDenom) {
			return false;
		}
		if (quantity != other.quantity) {
			return false;
		}
		return true;
	}
	
	public int getCreatedFromQuantity() {
		return createdFromQuantity;
	}

	public void setCreatedFromQuantity(int createdFromQuantity) {
		this.createdFromQuantity = createdFromQuantity;
	}
	
	public boolean isSynthetic() {
		return synthetic;
	}

	public void setSynthetic(boolean synthetic) {
		this.synthetic = synthetic;
	}

	@Override
	public String toString() {
		return "Level [isBid=" + isBid + ", firm=" + firm + ", synthetic=" + synthetic + ", price=" + price
				+ ", priceDenom=" + priceDenom + ", quantity=" + quantity + ", numOrders=" + numOrders
				+ ", aggregatedPrice=" + aggregatedPrice + ", referencePrice=" + referencePrice
				+ ", createdFromQuantity=" + createdFromQuantity + ", spread=" + spread + ", ruleSpread="
				+ ruleSpread + ", ruleSpreadUnit=" + ruleSpreadUnit + ", mid=" + mid + ", skew=" + skew + ", incomingSpread=" + incomingSpread
				+ ", incomingMid=" + incomingMid + ", spreadUnit=" + spreadUnit + ", incomingSpreadUnit="
				+ incomingSpreadUnit + ", skewUnit=" + skewUnit + ""
				//+ ", sourceName=" + sourceName
				+ ", auditLog=" + auditLog
				+ ", context=" + context + "]";
	}

	public int compareTo(Level o) {
		// TODO : include demom once the meaning is clear
		if (isBid == true && o.isBid() == true || isBid == false && o.isBid() == false) {
			if (isBid) {
				return (int) (o.getPrice() - price);
			} else {
				return (int) (price - o.getPrice());
			}
		} else if (isBid == true && o.isBid() == false) {
			return (int) (price - o.getPrice());
		} else if (isBid == false && o.isBid() == true) {
			return (int) (o.getPrice() - price);
		}
		return 0;
	}
}
