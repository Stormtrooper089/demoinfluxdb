package influx.Configuration;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean(name = "dbConnection")
    public InfluxDB getInfluxDbConnection(){
        InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086","todd","123456");
        return influxDB;
    }
}
