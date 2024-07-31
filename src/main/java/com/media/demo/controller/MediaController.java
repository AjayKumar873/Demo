package com.media.demo.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.media.demo.model.HLSGenerationResult;
import com.media.demo.service.MediaService;

import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/media")
//@CrossOrigin(origins = "http://localhost:4200")
public class MediaController {

	@Autowired
	private MediaService mediaService;

	@CrossOrigin(origins = "*")
	@PostMapping("/add")
    public ResponseEntity<HLSGenerationResult> uploadFile(@RequestParam("file") MultipartFile file) throws ExecutionException {
        try {
            String filePath = mediaService.addFile(file);
            List<String> resolutions = Arrays.asList("1920x1080", "1280x720", "854x480", "640x360", "426x240");  // example resolutions
           // List<String> resolutions = Arrays.asList("1920x1080");
            HLSGenerationResult result = mediaService.generateHLSFiles(filePath, resolutions);
            return ResponseEntity.ok(result);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
	@CrossOrigin(origins = "*")
    @DeleteMapping("/restart")
    public ResponseEntity<HLSGenerationResult> restartChunking(@RequestParam("mediaDir") String mediaDir, @RequestParam("filePath") String filePath) throws ExecutionException {
        try {
        	System.out.println(" For delete: "+mediaDir);
        	System.out.println("for chunking: "+filePath);
            mediaService.deleteGeneratedFiles(mediaDir);
            List<String> resolutions = Arrays.asList("1920x1080", "1280x720", "854x480", "640x360", "426x240"); //Arrays.asList("1920x1080", "1280x720", "640x360"); // example resolutions
            HLSGenerationResult result = mediaService.generateHLSFiles(filePath, resolutions);
            return ResponseEntity.ok(result);
        } catch (IOException | InterruptedException e) {
        	return ResponseEntity.status(500).body(null);
        }
    }
	
}
