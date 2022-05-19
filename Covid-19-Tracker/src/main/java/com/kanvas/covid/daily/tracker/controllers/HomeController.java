package com.kanvas.covid.daily.tracker.controllers;

import com.kanvas.covid.daily.tracker.services.CovidDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//RestController methods render rest JSON responses whereas Controller methods renders HTML UI elements
@Controller
public class HomeController {

    @Autowired
    CovidDataService covidDataService;

    @GetMapping("/")
    public String home(Model model){

        //We can create the model here and use that to construct the returned HTML page
        model.addAttribute("locationStats", covidDataService.getAllStats());
        model.addAttribute("totalReportedCases", covidDataService.getTotalNumberOfCases());
        model.addAttribute("lastOneDayChange", covidDataService.getLastOneDayChange());

        //Doing the total via streams
        //Long total = new Long(covidDataService.getAllStats()
        //        .stream()
        //        .mapToInt(stat -> stat.getLatestTotalCases())
        //        .sum());
        //return a UI template file name

        return "home";
    }

}
