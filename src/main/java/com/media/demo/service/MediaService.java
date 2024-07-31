package com.media.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.media.demo.model.ChunkingResult;
import com.media.demo.model.HLSGenerationResult;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class MediaService {

	// 15-07-2024 and 17-07-2024

	@Autowired
	private VideoProcessingService videoProcessingService;


	static String uploadDir = "D:\\temp";

	public String addFile(MultipartFile file) throws IOException {
		// Get the original filename
		String fileName = file.getOriginalFilename();
		System.out.println("file name:  " + fileName);

		// Validate file extension
		if (!fileName.toLowerCase().endsWith(".mp4") && !fileName.toLowerCase().endsWith(".avi")
				&& !fileName.toLowerCase().endsWith(".mkv")) {
			throw new IllegalArgumentException("Invalid file format. Only .mp4, .avi, and .mkv files are allowed.");
		}

		// Define the upload directory dynamically (e.g., a temporary directory)

		// Ensure the upload directory exists
		File uploadDirectory = new File(uploadDir);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdirs();
		}

		// Create a unique directory for each media file
//		String uniqueDirName = fileName.substring(0, fileName.lastIndexOf('.'));
//		File mediaDir = new File(uploadDir, uniqueDirName);
//		if (!mediaDir.exists()) {
//			mediaDir.mkdirs();
//		}
		
		String uniqueDirName =System.currentTimeMillis() + "-" + Thread.currentThread().getId();
		//String uniqueDirName = fileName.substring(0, fileName.lastIndexOf('.')) + "-"+ Thread.currentThread().getId();
		File mediaDir = new File(uploadDir, uniqueDirName);
		if (!mediaDir.exists()) {
			mediaDir.mkdirs();
		}

		// Create the destination file in the unique media directory
		File destinationFile = new File(mediaDir, fileName);
		try (InputStream inputStream = file.getInputStream();
				OutputStream outputStream = new FileOutputStream(destinationFile)) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}
		return destinationFile.getAbsolutePath();
	}

// failure recovery without Async mechanism & thread concept:

//	    public HLSGenerationResult generateHLSFiles(String inputFilePath, List<String> resolutions) throws IOException, InterruptedException {
//	        File inputFile = new File(inputFilePath);
//	        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//	        File mediaDir = inputFile.getParentFile();
//	        int exitCode = 0;
//	        StringBuilder ffmpegOutput = new StringBuilder();
//	        List<String> generatedFiles = new ArrayList<>();
//
//	        for (String resolution : resolutions) {
//	            String outputFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + ".m3u8").toString();
//	            String segmentFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + "_%03d.ts").toString();
//
//	            System.out.println(outputFileName);
//	            System.out.println(segmentFileName);
//	            String[] command = {
//	                "ffmpeg", "-loglevel", "error", "-i", inputFilePath,
//	                "-vf", "scale=" + resolution,
//	              //  "-nonexistent_option",
//	                "-hls_time", "10",
//	                "-hls_list_size", "0",
//	                "-hls_segment_filename", segmentFileName,
//	                outputFileName
//	            };
//	            
//	            ProcessBuilder builder = new ProcessBuilder(command);
//	            builder.redirectErrorStream(true);
//	            Process process = builder.start();
//	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//	            String line;
//
//	            while ((line = reader.readLine()) != null) {
//	                ffmpegOutput.append(line).append("\n");
//	            }
//	            exitCode = process.waitFor();
//
//	            if (exitCode != 0) {
//	                System.err.println("Process failed with exit code: " + exitCode);
//	                System.err.println("Error message: " + ffmpegOutput.toString());
//	                break; // Exit the loop on the first error
//	            } else {
//	                System.out.println("Process completed successfully.");
//	                generatedFiles.add(outputFileName);
//	            }
//	        }
//
//	        return new HLSGenerationResult(exitCode, mediaDir.getPath(),inputFile.getPath());
//	    }
//	  

	// latest method with async:

//	  public HLSGenerationResult generateHLSFiles(String inputFilePath, List<String> resolutions) throws InterruptedException {
//	        File inputFile = new File(inputFilePath);
//	        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//	        File mediaDir = inputFile.getParentFile();
//	        int[] finalExitCode = {0}; // Use an array to hold the mutable integer
//
//	        for (String resolution : resolutions) {
//	            videoProcessingService.processVideo(inputFilePath, resolution, finalExitCode);
//	        }
//
//	        videoProcessingService.generateThumbnails(inputFilePath, mediaDir, fileName, finalExitCode);
//          videoProcessingService.generateGIFs(inputFilePath, mediaDir,fileName, finalExitCode);
//          
//	        // Waiting for all async tasks to complete
//	        Thread.sleep(5000); 
//
//	        return new HLSGenerationResult(finalExitCode[0], mediaDir.getPath(), inputFile.getPath()); // Return the final exit code
//	    }
//	  

	
	// 25-07-24 (New Implementation for chuncking to get time taken for processing
	// the video & error)

//	    public HLSGenerationResult generateHLSFiles(String inputFilePath, List<String> resolutions) throws InterruptedException, IOException {
//	        File inputFile = new File(inputFilePath);
//	        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//	        File mediaDir = inputFile.getParentFile();
//	        int[] finalExitCode = {0}; // Use an array to hold the mutable integer
//            long endTime = 0;
//	        // Check for errors in the video file before processing
//	        String errorLog = videoProcessingService.checkVideoErrors(inputFilePath);
//	        if (!errorLog.isEmpty()) {
//	            System.err.println("Errors found in the video:\n" + errorLog);
//	            finalExitCode[0] = 1; // Set exit code to indicate error
//	            return new HLSGenerationResult(finalExitCode[0], mediaDir.getPath(), inputFile.getPath());
//	        }
//
//	        long startTime = System.currentTimeMillis();
//
//	        for (String resolution : resolutions) {
//	            videoProcessingService.processVideo(inputFilePath, resolution, finalExitCode);
//	             endTime += System.currentTimeMillis();
//	             System.out.println(endTime);
//	        }
//
//	         
//	        long elapsedTime = (endTime - startTime) / 1000; // Time in seconds
//	        System.out.println("Time taken for chunking: " + elapsedTime + " seconds");
//	        
//	        videoProcessingService.generateThumbnails(inputFilePath, mediaDir, fileName, finalExitCode);
//	        videoProcessingService.generateGIFs(inputFilePath, mediaDir, fileName, finalExitCode);
//
//	        // Waiting for all async tasks to complete
//	        Thread.sleep(5000);
//
//	        return new HLSGenerationResult(finalExitCode[0], mediaDir.getPath(), inputFile.getPath());
//	    }

//	  public HLSGenerationResult generateHLSFiles(String inputFilePath, List<String> resolutions) throws InterruptedException, IOException {
//	        File inputFile = new File(inputFilePath);
//	        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//	        File mediaDir = inputFile.getParentFile();
//	        int[] finalExitCode = {0}; // Use an array to hold the mutable integer
//	        long[] chunkingTime = {0}; // Use an array to hold the mutable long
//
//	        // Check for errors in the video
//	        String errors = videoProcessingService.checkVideoErrors(inputFilePath);
//	        if (!errors.isEmpty()) {
//	            System.err.println("Video has errors:\n" + errors);
//	            finalExitCode[0] = -1;
//	            return new HLSGenerationResult(finalExitCode[0], mediaDir.getPath(), inputFile.getPath());
//	        }
//
//	        long startTime = System.currentTimeMillis();
//
//	        for (String resolution : resolutions) {
//	            videoProcessingService.processVideo(inputFilePath, resolution, finalExitCode, chunkingTime);
//	        }
//
//	        videoProcessingService.generateThumbnails(inputFilePath, mediaDir, fileName, finalExitCode);
//	        videoProcessingService.generateGIFs(inputFilePath, mediaDir, fileName, finalExitCode);
//
//	        // Waiting for all async tasks to complete
//	        Thread.sleep(5000);
//
//	        long endTime = System.currentTimeMillis();
//	        long elapsedTime = (endTime - startTime) / 1000; // Time in seconds
//	        System.out.println("Time taken for chunking: " + elapsedTime + " seconds");
//
//	        System.out.println("Total chunking time measured by 'time' command: " + chunkingTime[0] + " seconds");
//
//	        return new HLSGenerationResult(finalExitCode[0], mediaDir.getPath(), inputFile.getPath());
//	    }

	
//	  public HLSGenerationResult generateHLSFiles(String inputFilePath, List<String> resolutions) throws InterruptedException, IOException {
//	        File inputFile = new File(inputFilePath);
//	        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//	        File mediaDir = inputFile.getParentFile();
//	        int[] finalExitCode = {0}; // Use an array to hold the mutable integer
//	        long[] chunkingTime = {0}; // Use an array to hold the mutable long
//
//	        // Check for errors in the video
//	        String errors = videoProcessingService.checkVideoErrors(inputFilePath);
//	        if (!errors.isEmpty()) {
//	            System.err.println("Video has errors:\n" + errors);
//	            finalExitCode[0] = -1;
//	            return new HLSGenerationResult(finalExitCode[0], mediaDir.getPath(), inputFile.getPath());
//	        }
//
//	        for (String resolution : resolutions) {
//	            videoProcessingService.processVideo(inputFilePath, resolution, finalExitCode, chunkingTime);
//	        }
//
//	        videoProcessingService.generateThumbnails(inputFilePath, mediaDir, fileName, finalExitCode);
//	        videoProcessingService.generateGIFs(inputFilePath, mediaDir, fileName, finalExitCode);
//
//	        // Waiting for all async tasks to complete
//	        Thread.sleep(5000);
//
//	        return new HLSGenerationResult(finalExitCode[0], mediaDir.getPath(), inputFile.getPath());
//	    }
	

//26-7-24:
// Approach  with System class :
	public HLSGenerationResult generateHLSFiles(String inputFilePath, List<String> resolutions)
			throws InterruptedException, IOException, ExecutionException {
		File inputFile = new File(inputFilePath);
		String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
		File mediaDir = inputFile.getParentFile();
		int finalExitCode = 0;
		long totalChunkingTime = 0;
		boolean flag = true;
		// Check for errors in the video
		String errors = videoProcessingService.checkVideoErrors(inputFilePath);
		if (!errors.isEmpty()) {
			System.err.println("Video has errors:\n" + errors);
			return new HLSGenerationResult(-1, mediaDir.getPath(), inputFile.getPath(), 0,flag);
		}

		for (String resolution : resolutions) {
			 Future<ChunkingResult> futureResult = videoProcessingService.processVideo(inputFilePath, resolution);
		        ChunkingResult result = futureResult.get();
			//ChunkingResult result = videoProcessingService.processVideo(inputFilePath, resolution);
			if(result!=null)
			{
				finalExitCode = result.getFinalExitCode();
				  System.out.println("chunking time for " + resolution + " : " + result.getChunkingTime());
				  totalChunkingTime += result.getChunkingTime();
				if (finalExitCode != 0) {
					flag=true;
					break;
				}
				else
				{
					flag=false;
				}
			}
		}
		System.out.println("total Chunking Time: " + totalChunkingTime);
		if (finalExitCode == 0) {
			videoProcessingService.generateThumbnails(inputFilePath, mediaDir, fileName, finalExitCode);
			videoProcessingService.generateGIFs(inputFilePath, mediaDir, fileName, finalExitCode);
		}

		// Waiting for all async tasks to complete
		//Thread.sleep(5000);

		return new HLSGenerationResult(finalExitCode, mediaDir.getPath(), inputFile.getPath(), totalChunkingTime, flag);
	}
	
//Solution with progress  option: 
	
//	  public HLSGenerationResult generateHLSFiles(String inputFilePath, List<String> resolutions) throws InterruptedException, IOException {
//    File inputFile = new File(inputFilePath);
//    String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//    File mediaDir = inputFile.getParentFile();
//    int[] finalExitCode = {0}; // Use an array to hold the mutable integer
//    long[] chunkingTime = {0}; // Use an array to hold the mutable long
//
//    // Check for errors in the video
//    String errors = videoProcessingService.checkVideoErrors(inputFilePath);
//    if (!errors.isEmpty()) {
//        System.err.println("Video has errors:\n" + errors);
//        finalExitCode[0] = -1;
//        return new HLSGenerationResult(finalExitCode[0], mediaDir.getPath(), inputFile.getPath());
//    }
//
//    for (String resolution : resolutions) {
//        videoProcessingService.processVideo(inputFilePath, resolution, finalExitCode);
//    }
//
//    videoProcessingService.generateThumbnails(inputFilePath, mediaDir, fileName, finalExitCode);
//    videoProcessingService.generateGIFs(inputFilePath, mediaDir, fileName, finalExitCode);
//
//    // Waiting for all async tasks to complete
//    Thread.sleep(5000);
//
//    return new HLSGenerationResult(finalExitCode[0], mediaDir.getPath(), inputFile.getPath());
//}	

	
// To delete the previous file in failure scenarios:
	
	public void deleteGeneratedFiles(String mediaDirPath) throws IOException {
		File mediaDir = new File(mediaDirPath);
		if (mediaDir.exists() && mediaDir.isDirectory()) {
			for (File file : mediaDir.listFiles()) {
				if (file.isFile() && (file.getName().endsWith(".ts") || file.getName().endsWith(".m3u8"))) {
					file.delete();
				}
			}
		}
	}

}
