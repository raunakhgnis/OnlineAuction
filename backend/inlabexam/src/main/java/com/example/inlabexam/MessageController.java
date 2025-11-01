package com.example.inlabexam;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MessageController {

    @GetMapping("/message")
    public ResponseEntity<?> getMessage(@RequestParam String format) {
        switch (format.toLowerCase()) {
            case "json":
                Map<String, String> jsonResponse = Map.of("status", "success", "message", "Hello!");
                return ResponseEntity.ok().body(jsonResponse);

            case "string":
                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Hello!");

            case "integer":
                return ResponseEntity.ok().body(100);

            case "double":
                return ResponseEntity.ok().body(99.99);

            case "html":
                String errorHtml = "<h1>Error: Invalid Format</h1>";
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.TEXT_HTML)
                        .body(errorHtml);

            default:
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Unsupported format. Use json, string, integer, double, or html.");
        }
    }
}
