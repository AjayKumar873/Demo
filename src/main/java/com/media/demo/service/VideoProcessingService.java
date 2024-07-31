package com.media.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.media.demo.model.ChunkingResult;

//@Service
//public class VideoProcessingService {
//		
//	   private static final int NUMBER_OF_THUMBNAILS = 3; 
//	   private static final int NUMBER_OF_GIFS = 3; 
//	   
//	   
//	    @Async
//	    public void processVideo(String inputFilePath, String resolution, int[] finalExitCode) {
//	        File inputFile = new File(inputFilePath);
//	        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//	        File mediaDir = inputFile.getParentFile();
//
//	        try {
//	            String outputFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + ".m3u8").toString();
//	            String segmentFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + "_%03d.ts").toString();
//	      
//	            String[] command = {
//	                    "ffmpeg", "-loglevel", "error", "-i", inputFilePath,
//	                    "-vf", "scale=" + resolution,
//	                    "-hls_time", "10",
//	                    "-hls_list_size", "0",
//	                    "-hls_segment_filename", segmentFileName,
//	                    outputFileName
//	            };
//	            //System.exit(0);
//	            ProcessBuilder builder = new ProcessBuilder(command);
//	            builder.directory(mediaDir);
//	            builder.redirectErrorStream(true);
//	            Process process = builder.start();
//
//	            // Read process output in a separate thread
//	            new Thread(() -> {
//	                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//	                    String line;
//	                    while ((line = reader.readLine()) != null) {
//	                        // System.out.println(line); // Log if needed
//	                    }
//	                } catch (IOException e) {
//	                    e.printStackTrace();
//	                }
//	            }).start();
//
//	            int exitCode = process.waitFor();
//	            if (exitCode != 0) {
//	                System.err.println("Process failed with exit code: " + exitCode);
//	                finalExitCode[0] = exitCode; 
//	               // throw new RuntimeException("FFmpeg process failed for resolution: " + resolution);
//	            }
//
//	        } catch (IOException | InterruptedException e) {
//	            e.printStackTrace();
//	        }
//	    }
//    
//	    //Generating the Thumbnails: 
//	    
//	    @Async
//	    public void generateThumbnails(String inputFilePath, File mediaDir, String fileName, int[] finalExitCode) {
//	        try {
//	            for (int i = 1; i <= NUMBER_OF_THUMBNAILS; i++) {
//	                String thumbnailFileName = Paths.get(mediaDir.getPath(), fileName + "_thumbnail_" + i + ".png").toString();
//	                String timestamp = String.format("00:00:%02d", i); // Example timestamp, you can adjust based on your needs
//
//	                String command = String.format(
//	                    "ffmpeg -loglevel error -i %s -vf scale=420:280 -ss %s -vframes 1 -qscale:v 5 %s",
//	                    inputFilePath, timestamp, thumbnailFileName
//	                );
//
//	                ProcessBuilder builder = new ProcessBuilder(command.split(" "));
//	                builder.directory(mediaDir);
//	                builder.redirectErrorStream(true);
//	                Process process = builder.start();
//
//	                int exitCode = process.waitFor();
//	                if (exitCode != 0) {
//	                    System.err.println("Thumbnail generation failed with exit code: " + exitCode);
//	                    finalExitCode[0] = exitCode;
//	                }
//	            }
//	        } catch (IOException | InterruptedException e) {
//	            e.printStackTrace();
//	        }
//	    }
//
//	  
//	    //generating the gifs: 
//	    
//	    @Async
//	    public void generateGIFs(String inputFilePath, File mediaDir, String fileName, int[] finalExitCode) {
//	        try {
//	            for (int i = 1; i <= NUMBER_OF_GIFS; i++) {
//	                String gifFileName = Paths.get(mediaDir.getPath(), fileName + "_" + i + ".gif").toString();
//	                String timestamp = String.format("00:00:%02d", i * 10);
//
//	                String command = String.format(
//	                    "ffmpeg -loglevel error -i %s -vf scale=320:-1 -ss %s -t 10 %s",
//	                    inputFilePath, timestamp, gifFileName
//	                );
//
//	                ProcessBuilder builder = new ProcessBuilder(command.split(" "));
//	                builder.directory(mediaDir);
//	                builder.redirectErrorStream(true);
//	                Process process = builder.start();
//
//	                int exitCode = process.waitFor();
//	                if (exitCode != 0) {
//	                    System.err.println("GIF generation failed with exit code: " + exitCode);
//	                    finalExitCode[0] = exitCode;
//	                }
//	            }
//	        } catch (IOException | InterruptedException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	    
//	    
//	    public String checkVideoErrors(String inputFilePath) throws IOException, InterruptedException {
//	        ProcessBuilder processBuilder = new ProcessBuilder(
//	            "ffmpeg", "-v", "error", "-i", inputFilePath, "-f", "null", "-"
//	        );
//	        processBuilder.redirectErrorStream(true);
//	        Process process = processBuilder.start();
//
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//	        StringBuilder errorLog = new StringBuilder();
//	        String line;
//	        while ((line = reader.readLine()) != null) {
//	            errorLog.append(line).append("\n");
//	        }
//	        process.waitFor();
//
//	        return errorLog.toString();
//	    }
//	    
//  }

//**********25-07-24*****************//

//    @Async
//    public void processVideo(String inputFilePath, String resolution, int[] finalExitCode, long[] chunkingTime) {
////        File inputFile = new File(inputFilePath);
////        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
////        File mediaDir = inputFile.getParentFile();
////
////        try {
////            String outputFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + ".m3u8").toString();
////            String segmentFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + "_%03d.ts").toString();
////
////            String[] command = {
////                "time", "ffmpeg", "-loglevel", "error", "-i", inputFilePath,
////                "-vf", "scale=" + resolution,
////                "-hls_time", "10",
////                "-hls_list_size", "0",
////                "-hls_segment_filename", segmentFileName,
////                outputFileName
////            };
////
////            ProcessBuilder builder = new ProcessBuilder(command);
////            builder.directory(mediaDir);
////            builder.redirectErrorStream(true);
////            Process process = builder.start();
////
////            // Read process output in a separate thread
////            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
////            String line;
////            while ((line = reader.readLine()) != null) {
////                System.out.println(line); // Log if needed
////            }
////
////            int exitCode = process.waitFor();
////            if (exitCode != 0) {
////                System.err.println("Process failed with exit code: " + exitCode);
////                finalExitCode[0] = exitCode;
////            }
////
////            // Get the time output from the time command
////            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
////            while ((line = errorReader.readLine()) != null) {
////                if (line.startsWith("real")) {
////                    String timeStr = line.split("\t")[1];
////                    long timeInSeconds = convertTimeToSeconds(timeStr);
////                    chunkingTime[0] += timeInSeconds;
////                }
////            }
////
////        } catch (IOException | InterruptedException e) {
////            e.printStackTrace();
////        }
////    }
////
////    private long convertTimeToSeconds(String timeStr) {
////        String[] parts = timeStr.split("m");
////        int minutes = Integer.parseInt(parts[0]);
////        float seconds = Float.parseFloat(parts[1].replace("s", ""));
////        return minutes * 60 + (long) seconds;
//    	
//    	
//    	  File inputFile = new File(inputFilePath);
//          String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//          File mediaDir = inputFile.getParentFile();
//
//          try {
//              String outputFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + ".m3u8").toString();
//              String segmentFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + "_%03d.ts").toString();
//
//              String[] command = {
//                  "ffmpeg", "-loglevel", "error", "-i", inputFilePath,
//                  "-vf", "scale=" + resolution,
//                  "-hls_time", "10",
//                  "-hls_list_size", "0",
//                  "-hls_segment_filename", segmentFileName,
//                  outputFileName
//              };
//
//              ProcessBuilder builder = new ProcessBuilder(command);
//              builder.directory(mediaDir);
//              builder.redirectErrorStream(true);
//              
//              long startTime = System.currentTimeMillis();
//              Process process = builder.start();
//
//              // Read process output in a separate thread
//              new Thread(() -> {
//                  try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                      String line;
//                      while ((line = reader.readLine()) != null) {
//                          // Log if needed
//                      }
//                  } catch (IOException e) {
//                      e.printStackTrace();
//                  }
//              }).start();
//
//              int exitCode = process.waitFor();
//              long endTime = System.currentTimeMillis();
//              long elapsedTime = (endTime - startTime) / 1000; // Time in seconds
//
//              chunkingTime[0] += elapsedTime;
//
//              if (exitCode != 0) {
//                  System.err.println("Process failed with exit code: " + exitCode);
//                  finalExitCode[0] = exitCode;
//              }
//
//          } catch (IOException | InterruptedException e) {
//              e.printStackTrace();
//          }
//    }
//
// 

//    @Async
//    public void processVideo(String inputFilePath, String resolution, int[] finalExitCode, long[] chunkingTime) {
//        File inputFile = new File(inputFilePath);
//        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//        File mediaDir = inputFile.getParentFile();
//
//        try {
//            String outputFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + ".m3u8").toString();
//            String segmentFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + "_%03d.ts").toString();
//
//            String[] command = {
//                "ffmpeg", "-loglevel", "error", "-i", inputFilePath,
//                "-vf", "scale=" + resolution,
//                "-hls_time", "10",
//                "-hls_list_size", "0",
//                "-hls_segment_filename", segmentFileName,
//                outputFileName
//            };
//
//            ProcessBuilder builder = new ProcessBuilder(command);
//            builder.directory(mediaDir);
//            builder.redirectErrorStream(true);
//            
//            long startTime = System.currentTimeMillis();
//            Process process = builder.start();
//
//            // Read process output in a separate thread
//            new Thread(() -> {
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        // Log if needed
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//            int exitCode = process.waitFor();
//            long endTime = System.currentTimeMillis();
//            long elapsedTime = (endTime - startTime) / 1000; // converting time in seconds
////            synchronized (chunkingTime) {
////                chunkingTime[0] += elapsedTime;
////            }
//            
//                chunkingTime[0] += elapsedTime;
//            
//            System.out.println("chunked process time: "+chunkingTime[0]);
//
//            if (exitCode != 0) {
//                System.err.println("Process failed with exit code: " + exitCode);
//                finalExitCode[0] = exitCode;
//            }
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }



	// **************Old Implementation ends here************//



// 26-07-24 -->solution with System class:

@Service
public class VideoProcessingService {

	private static final int NUMBER_OF_THUMBNAILS = 3;
	private static final int NUMBER_OF_GIFS = 3;
	
	@Async
	public Future<ChunkingResult> processVideo(String inputFilePath, String resolution) {
		File inputFile = new File(inputFilePath);
		String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
		File mediaDir = inputFile.getParentFile();
		int exitCode = 0;
		long elapsedTime = 0;

		try {
			String outputFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + ".m3u8").toString();
			String segmentFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + "_%03d.ts").toString();

			String[] command = { "ffmpeg", "-loglevel", "error", "-i", inputFilePath, "-vf", "scale=" + resolution,
					"-hls_time", "20", "-hls_list_size", "0", "-hls_segment_filename", segmentFileName,
					outputFileName };

			ProcessBuilder builder = new ProcessBuilder(command);
			builder.directory(mediaDir);
			builder.redirectErrorStream(true);

			long startTime = System.currentTimeMillis();
			Process process = builder.start();

			// Read process output in a separate thread
			new Thread(() -> {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line;
					while ((line = reader.readLine()) != null) {
						// Log if needed
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();

			exitCode = process.waitFor();
			long endTime = System.currentTimeMillis();
			elapsedTime = (endTime - startTime) / 1000; // converting time in seconds
			if (exitCode != 0) {
				System.err.println("Process failed with exit code: " + exitCode);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			exitCode = -1;
		}
		
		 return new AsyncResult<>(new ChunkingResult(exitCode, elapsedTime));
	}

	
	
	// Solution with progress option:

//	@Async
//	public void processVideo(String inputFilePath, String resolution, int[] finalExitCode) {
//	    File inputFile = new File(inputFilePath);
//	    String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//	    File mediaDir = inputFile.getParentFile();
//
//	    String outputFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + ".m3u8").toString();
//	    String segmentFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + "_%03d.ts").toString();
//	    String progressFilePath = Paths.get(mediaDir.getPath(), fileName + "_progress_" + resolution + ".txt").toString();
//
//	    String[] command = { 
//	        "ffmpeg", "-loglevel", "error", "-i", inputFilePath, 
//	        "-vf", "scale=" + resolution, 
//	        "-hls_time", "20", 
//	        "-hls_list_size", "0", 
//	        "-hls_segment_filename", segmentFileName, 
//	        "-progress", progressFilePath, 
//	        outputFileName 
//	    };
//
//	    ProcessBuilder builder = new ProcessBuilder(command);
//	    builder.directory(mediaDir);
//	    builder.redirectErrorStream(true);
//
//	    try {
//	        Process process = builder.start();
//
//	        // Read process output in a separate thread
//	        new Thread(() -> {
//	            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//	                String line;
//	                while ((line = reader.readLine()) != null) {
//	                    System.out.println("FFmpeg output: " + line); // Log FFmpeg output for debugging
//	                }
//	            } catch (IOException e) {
//	                e.printStackTrace();
//	            }
//	        }).start();
//
//	        // Allow some time for FFmpeg to create the progress file
//	        Thread.sleep(5000); // Adjust the sleep time as needed
//
//	        // Check progress file existence and readability
//	        new Thread(() -> {
//	            Path filePath = Paths.get(progressFilePath);
//
//	            if (Files.exists(filePath) && Files.isReadable(filePath)) {
//	                System.out.println("File reading: " + progressFilePath);
//
//	                try (BufferedReader progressReader = new BufferedReader(new InputStreamReader(new FileInputStream(progressFilePath)))) {
//	                    String line;
//	                    long lastOutTimeMs = 0;
//	                    double lastSpeed = 0;
//	                    double speed = 1.0;
//	                    long totalDurationMs = 0; // Keep track of total duration based on progress updates
//
//	                    while ((line = progressReader.readLine()) != null) {
//	                        if (line.startsWith("out_time_ms=")) {
//	                            String value = line.split("=")[1];
//	                            if (!value.equals("N/A")) {
//	                                lastOutTimeMs = Long.parseLong(value);
//	                                System.out.println("Parsed out_time_ms: " + lastOutTimeMs);
//	                                totalDurationMs = lastOutTimeMs; // Update totalDurationMs based on the last parsed value
//	                            }
//	                        }
//	                        if (line.startsWith("speed=")) {
//	                            String value = line.split("=")[1].replace("x", "");
//	                            if (!value.equals("N/A")) {
//	                            	lastSpeed=Double.parseDouble(value);
//	                                System.out.println("Parsed speed: " + lastSpeed);
//	                                speed = lastSpeed;
//	                            }
//	                        }
//
//	                    }
//	                    
//	                    if (totalDurationMs > 0 && speed > 0) {
//                        	System.out.println("totalDurationMs : "+totalDurationMs);
//                        	System.out.println("speed : "+speed);
//                            long estimatedTotalTime = (long) (totalDurationMs / speed);
//                            System.out.println("estimatedTotalTime: "+estimatedTotalTime);
//                            System.out.println("lastOutTimeMs: "+lastOutTimeMs);
//                            // long estimatedTotalTime = (long) (outTimeMs / speed); // Estimate total time in real-time
//                            long remainingTime = estimatedTotalTime - (totalDurationMs / 1000000); // Subtract processed time
//
//                          //  long remainingTime = lastOutTimeMs-estimatedTotalTime;
//
//                            // Ensure remaining time is non-negative
//                          //  remainingTime = Math.max(0, remainingTime);
//
//                            System.out.println("Estimated remaining time for " + resolution + ": " + remainingTime / 1000 + " seconds"); // Convert to seconds
//                        }
//	                } catch (IOException e) {
//	                    e.printStackTrace();
//	                }
//	            } else {
//	                System.err.println("File does not exist or is not readable: " + progressFilePath);
//	            }
//	        }).start();
//
//	        int exitCode = process.waitFor();
//	        if (exitCode != 0) {
//	            System.err.println("Process failed with exit code: " + exitCode);
//	            finalExitCode[0] = exitCode;
//	        }
//
//	    } catch (IOException | InterruptedException e) {
//	        e.printStackTrace();
//	    }
//	}
	
	
	// getting process time with benchmark option :

//    @Async
//    public void processVideo(String inputFilePath, String resolution, int[] finalExitCode, long[] chunkingTime) {
//        File inputFile = new File(inputFilePath);
//        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
//        File mediaDir = inputFile.getParentFile();
//
//        try {
//            String outputFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + ".m3u8").toString();
//            String segmentFileName = Paths.get(mediaDir.getPath(), fileName + "_" + resolution + "_%03d.ts").toString();
//
//            String[] command = {
//            	    "ffmpeg", "-benchmark", "-loglevel", "info", "-i", inputFilePath,
//            	    "-vf", "scale=" + resolution,
//            	    "-hls_time", "20",
//            	    "-hls_list_size", "0",
//            	    "-hls_segment_filename", segmentFileName,
//            	    outputFileName
//            	};
//
//
//            ProcessBuilder builder = new ProcessBuilder(command);
//            builder.directory(mediaDir);
//            builder.redirectErrorStream(true);
//
//            long startTime = System.currentTimeMillis();
//            Process process = builder.start();
//
//            // Read process output in the current thread
//            StringBuilder ffmpegOutput = new StringBuilder();
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                	if(line.contains("bench:") )
//                	{
//                		ffmpegOutput.append(line).append("\n");
//                        //System.out.println(ffmpegOutput.toString());
//                		System.out.println(line);
//                	}
//                    
//                }
//            }
//
//            int exitCode = process.waitFor();
//            long endTime = System.currentTimeMillis();
//            long elapsedTime = (endTime - startTime) / 1000; // converting time in seconds
//
//            chunkingTime[0] += elapsedTime;
//            System.out.println("Chunking process time: " + chunkingTime[0] + " seconds");
//
//            if (exitCode != 0) {
//                System.err.println("Process failed with exit code: " + exitCode);
//                finalExitCode[0] = exitCode;
//            }
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//    

	
	@Async
	public void generateThumbnails(String inputFilePath, File mediaDir, String fileName, int finalExitCode) {
		try {
			for (int i = 1; i <= NUMBER_OF_THUMBNAILS; i++) {
				String thumbnailFileName = Paths.get(mediaDir.getPath(), fileName + "_thumbnail_" + i + ".png")
						.toString();
				String timestamp = String.format("00:00:%02d", i); // Example timestamp, you can adjust based on your
																	// needs

				String command = String.format(
						"ffmpeg -loglevel error -i %s -vf scale=420:280 -ss %s -vframes 1 -qscale:v 5 %s",
						inputFilePath, timestamp, thumbnailFileName);

				ProcessBuilder builder = new ProcessBuilder(command.split(" "));
				builder.directory(mediaDir);
				builder.redirectErrorStream(true);
				Process process = builder.start();

				int exitCode = process.waitFor();
				if (exitCode != 0) {
					System.err.println("Thumbnail generation failed with exit code: " + exitCode);
					finalExitCode= exitCode;
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Async
	public void generateGIFs(String inputFilePath, File mediaDir, String fileName, int finalExitCode) {
		try {
			for (int i = 1; i <= NUMBER_OF_GIFS; i++) {
				String gifFileName = Paths.get(mediaDir.getPath(), fileName + "_" + i + ".gif").toString();
				String timestamp = String.format("00:00:%02d", i * 10);

				String command = String.format("ffmpeg -loglevel error -i %s -vf scale=320:-1 -ss %s -t 10 %s",
						inputFilePath, timestamp, gifFileName);

				ProcessBuilder builder = new ProcessBuilder(command.split(" "));
				builder.directory(mediaDir);
				builder.redirectErrorStream(true);
				Process process = builder.start();

				int exitCode = process.waitFor();
				if (exitCode != 0) {
					System.err.println("GIF generation failed with exit code: " + exitCode);
					finalExitCode= exitCode;
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String checkVideoErrors(String inputFilePath) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-v", "error", "-i", inputFilePath, "-f", "null",
				"-");
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuilder errorLog = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			errorLog.append(line).append("\n");
		}
		process.waitFor();

		return errorLog.toString();
	}

}
