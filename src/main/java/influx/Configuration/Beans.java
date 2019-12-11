package influx.Configuration;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean(name = "dbConnection")
    public InfluxDB getInfluxDbConnection(){
        InfluxDB influxDB = InfluxDBFactory.connect("http://159.89.171.118:8086","test","password");
        return influxDB;
    }
}
