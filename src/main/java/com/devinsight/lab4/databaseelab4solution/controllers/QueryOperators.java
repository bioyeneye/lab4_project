package com.devinsight.lab4.databaseelab4solution.controllers;

import com.devinsight.lab4.databaseelab4solution.services.QueryFunctionsOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/query")
public class QueryOperators {
    @Autowired
    private QueryFunctionsOperations functionsOperations;

    @GetMapping("")
    public String queryFunctions(@RequestParam String action, Model model) {
        String result = null;

        int actionSelected = 0;
        try {
            actionSelected = Integer.parseInt(action);
        }catch (Exception ex) {

        }

        result = functionsOperations.getResultForActions(actionSelected);
        model.addAttribute("response", result);
        model.addAttribute("queries", QueryFunctionsOperations.getQueries(actionSelected));
        return "query";
    }
}
