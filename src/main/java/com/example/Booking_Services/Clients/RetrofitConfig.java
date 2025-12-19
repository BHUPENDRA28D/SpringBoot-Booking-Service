package com.example.Booking_Services.Clients;

import com.example.Booking_Services.API.LocationServiceAPI;
import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Autowired
    private EurekaClient eurekaClient;


    private String getServiceUrl(String serviceName){
        return eurekaClient.getNextServerFromEureka(serviceName,false).getHomePageUrl();
    }



    @Bean
    public LocationServiceAPI locationServiceAPI() {
            return new Retrofit.Builder()
                    .baseUrl(getServiceUrl("SPRINGBOOT_LOCAITONSEVICES"))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder().build())
                    .build()
                    .create(LocationServiceAPI.class);

    }


/*    @Bean
    public Retrofit retrofit()

        //from here communication to multiple microservices is done.!
        return  new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }*/

}
