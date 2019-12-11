package influx.Controller;


import influx.Services.DataEntryService;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataEntryEndPoint {
    @Autowired
    DataEntryService dataEntryService;


    @RequestMapping("/batchTrade")
    public String getBatchTrade() throws InterruptedException, ParseException{
    	dataEntryService.enterDataToDb();
    	return "Success";
    }
}
