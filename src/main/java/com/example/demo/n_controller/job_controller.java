package com.example.demo.n_controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/")
public class job_controller {

    @GetMapping(path = "getJobList")
    public String getJobList() {
        log.info("getJobList");
        
        String url = "http://dev3.dansmultipro.co.id/api/recruitment/positions.json";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity( url, String.class);
            return response.getBody();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "getJobDetail/{ID}")
    public String getJobDetail(@PathVariable(value="ID") String id) {
        log.info("getJobDetail/{ID}");
        
        String url = "http://dev3.dansmultipro.co.id/api/recruitment/positions/" + id;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity( url, String.class);
            return response.getBody();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "getJobList/csv", produces = "text/csv")
    public ResponseEntity<String> getJobListCSV() {
        log.info("getJobList/csv");
        
        String url = "http://dev3.dansmultipro.co.id/api/recruitment/positions.json";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity( url, String.class);
            JsonNode jsonTree = new ObjectMapper().readTree(response.getBody());

            Builder csvSchemaBuilder = CsvSchema.builder();
            JsonNode firstObject = jsonTree.elements().next();
            firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
            CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

            CsvMapper csvMapper = new CsvMapper();
            String data = csvMapper.writerFor(JsonNode.class)
                        .with(csvSchema)
                        .writeValueAsString(jsonTree);
            
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Job List.csv");
            responseHeaders.add("Content-Type", "text/csv");
            return new ResponseEntity<>(data, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}