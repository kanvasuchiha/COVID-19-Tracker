package com.kanvas.covid.daily.tracker.services;

import com.kanvas.covid.daily.tracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CovidDataService {

    private static String COVID_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> allStats = new ArrayList<>();


    //Tells spring as to when you construct this service, after it's done, just execute this service
    @PostConstruct
    //Defines scheduled run of the method
    //<second> <minute> <hour> <day-of-month> <month> <day-of-week>
    @Scheduled(cron = "* * 1 * * *")
    //@daily or @midnight – run once a day
    //@Scheduled(cron = "@Daily")
    public void fetchCovidData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(COVID_DATA_URL))
                .build();

        //need to send this request to the client
        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        //we have the response in string format
        //System.out.println(response.body());

        //We need to parse the string data to usable format
        StringReader in = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            System.out.println(locationStat.toString());
            newStats.add(locationStat);
        }
        this.allStats = newStats;
    }

}
