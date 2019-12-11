package model;

public class CreateDummySnapshot {
	
	
	public static L2Snapshot createLOB() {  
	    L2Snapshot snapshot = new L2Snapshot();
	  //  snapshot.getContext().setExchangeFeedType("LOB");
	    //snapshot.getContext().setBookCharacteristics(bookCharacteristics);
	    //snapshot.setSourceName("360TB");
	    snapshot.getContext().setSymbol("INSTRUMENT_ID_1");
	    snapshot.getContext().setExchange("360TB");

	    snapshot.addBidLevel(new Level(true, 72000000, 3000000,1000000));
	    snapshot.addBidLevel(new Level(true, 71000000, 9000000,1000000));
	    snapshot.addBidLevel(new Level(true, 70000000, 1000000,1000000));
	    snapshot.addBidLevel(new Level(true, 69000000, 12000000,1000000));

	    snapshot.addAskLevel(new Level(false, 73000000, 1000000,1000000));
	    snapshot.addAskLevel(new Level(false, 74000000, 7000000,1000000));
	    snapshot.addAskLevel(new Level(false, 75000000, 5000000,1000000));
	    snapshot.addAskLevel(new Level(false, 76000000, 6000000,1000000));

	    return snapshot;
	} 
}

