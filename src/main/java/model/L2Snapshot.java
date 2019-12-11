package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;



import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.wire.AbstractBytesMarshallable;

public class L2Snapshot{
	public static final int MAX_DEPTH = 10;
	public static final String EMPTY_STRING = "";

	private String userText = EMPTY_STRING;
	private boolean firm; // if false tells the book has indicative levels
	private long midPrice; // calculated
	private long midPriceDenom; // calculated

	private int bidDepth, askDepth;
	private final List<Level> bidLevels = new ArrayList<>(MAX_DEPTH);
	private final List<Level> askLevels = new ArrayList<>(MAX_DEPTH);
	protected final MessageContext context = new MessageContext();

    public void init() {
		
		context.reset();
		/*
		 * for (int i = 0; i < MAX_DEPTH; i++) { bidLevels.add(new Level());
		 * askLevels.add(new Level()); }
		 */
	}

	public L2Snapshot() {
		init();
	}

	public L2Snapshot(L2Snapshot l2Snapshot) {
		this.copyFrom(l2Snapshot);
	}

	public void copyFrom(L2Snapshot l2Snapshot) {
		this.userText = l2Snapshot.getUserText();
		this.firm = l2Snapshot.isFirm();
		this.midPrice = l2Snapshot.getMidPrice();
		this.midPriceDenom = l2Snapshot.getMidPriceDenom();

		this.bidDepth = l2Snapshot.getBidDepth();
		this.askDepth = l2Snapshot.getAskDepth();
		for(Level bidLevel: l2Snapshot.getBidLevels()) {
			this.addBidLevel(new Level(bidLevel));
		}
		for(Level askLevel: l2Snapshot.getAskLevels()) {
			this.addAskLevel(new Level(askLevel));
		}
		this.context.copyFrom(l2Snapshot.getContext());
	}

	public void calculateAndSet() {
		setAskDepth(askLevels.size());
		setBidDepth(bidLevels.size());
		setMidPriceAndMidPriceDenom();
	}
	
	public void reset() {
		userText = EMPTY_STRING;
		firm = false;
		midPrice = 0;
		midPriceDenom = 0;
	
		context.reset();
		bidDepth = 0;
		askDepth = 0;
		// clean up levels
		
		 for (int i = 0; i < MAX_DEPTH; i++) { bidLevels.get(i).reset();
		 askLevels.get(i).reset(); }
	}

	public L2Snapshot(Collection<Level> bids, Collection<Level> asks) {
		init();

		if (bids.size() > MAX_DEPTH || asks.size() > MAX_DEPTH) {
			throw new IllegalArgumentException("Bid/Ask level depth cannnot exceed " + MAX_DEPTH);
		}
		bidDepth = bids.size();
		askDepth = asks.size();
		addLevel(bids, bidLevels);
		addLevel(asks, askLevels);
	}

	/*@Override
	public EventType eventType() {
		return EventType.L2SNAPSHOT;
	}*/

	private void addLevel(Collection<Level> newLevels, List<Level> levels) {
		levels.addAll(newLevels);
	}

	/*
	 * private void addLevel(Collection<Level> newLevels, List<Level> levels) { int
	 * i = 0; for (Level newLevel : newLevels) { levels.get(i).copyFrom(newLevel);
	 * i++; } }
	 */

	
	public void readMarshallable(BytesIn bytes) throws IORuntimeException {
		firm = bytes.readBoolean();
		midPrice = bytes.readLong();
		midPriceDenom = bytes.readLong();
	
		userText = bytes.read8bit();
		// MessageContext fields
		context.read(bytes);
		// Depth of the book
		askDepth = bytes.readInt();
		readLevels(bytes, askLevels, askDepth);
		bidDepth = bytes.readInt();
		readLevels(bytes, bidLevels, bidDepth);

	}

	private void readLevels(BytesIn bytes, List<Level> levels, int depth) {
		for (int i = 0; i < depth; i++) {
			Level level = new Level();
			level.readMarshallable(bytes);
			level.setContext(context);
			levels.add(level);
		}
	}

	
    public void writeMarshallable(BytesOut bytes) {
        bytes//.write8bit(id)
                .writeBoolean(firm)
                .writeLong(midPrice)
                .writeLong(midPriceDenom)
                .write8bit(userText);
        context.write(bytes);


        // Depth of the book
        writeLevels(bytes, askLevels, askDepth);
        writeLevels(bytes, bidLevels, bidDepth);
    }

    @SuppressWarnings("rawtypes")
	private void writeLevels(BytesOut bytes, List<Level> levels, int depth) {
		bytes.writeInt(depth);
		for (int i = 0; i < depth; i++) {
			levels.get(i).writeMarshallable(bytes);
		}
	}

	public void addLevel(Level level) {
		level.setContext(context);
		if (level.isBid()) {
			bidLevels.add(level);
			// bidLevels.get(bidDepth++).copyFrom(level);
		} else {
			askLevels.add(level);
			// askLevels.get(askDepth++).copyFrom(level);
		}
	}

	public void addBidLevel(Level level) {
		level.setContext(context);
		int indexOfLevel = findIndexOfMatchingQtyLevel(bidLevels, level.getQuantity());
		if(indexOfLevel != Integer.MAX_VALUE) {
			bidLevels.add(indexOfLevel, level);
			bidLevels.remove(indexOfLevel+1);
		}else {
			bidLevels.add(level);			
		}
		bidDepth = this.bidLevels.size();
		// bidLevels.get(bidDepth++).copyFrom(level);
	}

	public void addAskLevel(Level level) {
		level.setContext(context);
		int indexOfLevel = findIndexOfMatchingQtyLevel(askLevels, level.getQuantity());
		if(indexOfLevel != Integer.MAX_VALUE) {
			askLevels.add(indexOfLevel, level);
			askLevels.remove(indexOfLevel+1);
		}else {
			askLevels.add(level);			
		}
		askDepth = this.askLevels.size();
		// askLevels.get(askDepth++).copyFrom(level);
	}
	
	private int findIndexOfMatchingQtyLevel(List<Level> levels, long qty) {
		int index = Integer.MAX_VALUE;
		for(int i = 0; i<levels.size();i++) {
			if(qty == levels.get(i).getQuantity()) {
				index = i;
			}
		}
		
		return index;
	}

	/*
	 * public void addBidLevel(int id, boolean isBid, long price, long quantity) {
	 * bidLevels.get(bidDepth++).set(id, isBid, price, quantity); }
	 */

	/*
	 * public void addAskLevel(int id, boolean isBid, long price, long quantity) {
	 * askLevels.get(askDepth++).set(id, isBid, price, quantity); }
	 */




	public List<Level> getBidLevels() {
		return Collections.unmodifiableList(bidLevels);
	}

	public List<Level> getAskLevels() {
		return Collections.unmodifiableList(askLevels);
	}
	
	public void removeLevel(Level level) {
		if (level.isBid()) {
			bidLevels.remove(level);
		} else {
			askLevels.remove(level);
		}
	}
	
	public int getBidSize() {
		return bidDepth;
	}

	public int getAskSize() {
		return askDepth;
	}

	public boolean isFirm() {
		return firm;
	}

	public long getMidPrice() {
		return midPrice;
	}

	public long getMidPriceDenom() {
		return midPriceDenom;
	}

	public String getUserText() {
		return userText;
	}

	public void setUserText(String userText) {
		this.userText = userText;
	}

	public MessageContext getContext() {
		return context;
	}

	public void setContext(MessageContext context) {
		this.context.copyFrom(context);
	}

	public void setFirm(boolean firm) {
		this.firm = firm;
	}

	public void setMidPriceAndMidPriceDenom() {
//		List<Level> bids = sortLevels(bidLevels);
//		List<Level> asks = sortLevels(askLevels);
//		this.midPrice = calculateMidPriceFromSortedLists(bids, asks);
//		this.midPriceDenom = calculateMidPriceDenomFromSortedLists(bids, asks);
	}



	public int getBidDepth() {
		return bidDepth;
	}

	public void setBidDepth(int bidDepth) {
		this.bidDepth = bidDepth;
	}

	public int getAskDepth() {
		return askDepth;
	}

	public void setAskDepth(int askDepth) {
		this.askDepth = askDepth;
	}

	public void setMidPrice(long midPrice) {
		this.midPrice = midPrice;
	}

	public void setMidPriceDenom(long midPriceDenom) {
		this.midPriceDenom = midPriceDenom;
	}

	public void setFeedHandlerTimeMicros(long timeInMicros) {
    	context.setFeedHandlerTime(timeInMicros);
	}

	public long getFeedHandlerTimeMicros() {
		return context.getFeedHandlerTime();
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((askLevels == null) ? 0 : askLevels.hashCode());
        result = prime * result
                + ((bidLevels == null) ? 0 : bidLevels.hashCode());
        result = prime * result + ((context == null) ? 0 : context.hashCode());
        result = prime * result + (firm ? 1231 : 1237);
        long temp;
        temp = Double.doubleToLongBits(midPrice);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(midPriceDenom);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        result = prime * result
                + ((userText == null) ? 0 : userText.hashCode());
        return result;
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
        L2Snapshot other = (L2Snapshot) obj;
        if (askDepth != other.askDepth) {
            return false;
        }
        if (bidDepth != other.bidDepth) {
            return false;
        }
        if (askLevels == null) {
            if (other.askLevels != null) {
                return false;
            }
        } else if (!askLevels.equals(other.askLevels)) {
            return false;
        }
        if (bidLevels == null) {
            if (other.bidLevels != null) {
                return false;
            }
        } else if (!bidLevels.equals(other.bidLevels)) {
            return false;
        }
        if (context == null) {
            if (other.context != null) {
                return false;
            }
        } else if (!context.equals(other.context)) {
            return false;
        }
        if (firm != other.firm) {
            return false;
        }
        if (midPrice != other.midPrice) {
            return false;
        }
        if (midPriceDenom != other.midPriceDenom) {
            return false;
        }

        if (userText == null) {
            if (other.userText != null) {
                return false;
            }
        } else if (!userText.equals(other.userText)) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "L2Snapshot [" + "userText=" + userText + ", firm=" + firm + ", midPrice=" + midPrice
				+ ", midPriceDenom=" + midPriceDenom + ", snapshotType=" 
				+ ", bidLevels=" + bidLevels + ", askLevels=" + askLevels + ", context=" + context + "]";
	}

	public List<Level> getAllLevels() {
		List<Level> allLevels = new ArrayList<>(bidLevels.size() + askLevels.size());
		allLevels.addAll(bidLevels);
		allLevels.addAll(askLevels);

		return allLevels;
	}
}
