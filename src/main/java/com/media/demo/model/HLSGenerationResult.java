package com.media.demo.model;

public class HLSGenerationResult {
	
	    private int exitCode;
	    private String mediaDirPath;
	    private String inputFilePath;
	    private long chunkingTime; // Add this field
	    private boolean flag ;

	    public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		public HLSGenerationResult(int exitCode, String mediaDirPath, String inputFilePath, long chunkingTime, boolean flag) {
	        this.exitCode = exitCode;
	        this.mediaDirPath = mediaDirPath;
	        this.inputFilePath = inputFilePath;
	        this.chunkingTime = chunkingTime; // Initialize this field
	        this.flag= flag;
	    }

	    public HLSGenerationResult(int finalExitCode, String mediaDirPath, String inputFilePath) {
		
	    	   this.exitCode = finalExitCode;
		        this.mediaDirPath = mediaDirPath;
		        this.inputFilePath = inputFilePath;
		}

	    
	    //for other approach:
		public HLSGenerationResult(String mediaDirPath, String inputFilePath, long totalChunkingTime) {
		
			  this.mediaDirPath = mediaDirPath;
		        this.inputFilePath = inputFilePath;
		        this.chunkingTime = totalChunkingTime;
		}

		// Getters and setters
	    public int getExitCode() {
	        return exitCode;
	    }

	    public void setExitCode(int exitCode) {
	        this.exitCode = exitCode;
	    }

	    public String getMediaDirPath() {
	        return mediaDirPath;
	    }

	    public void setMediaDirPath(String mediaDirPath) {
	        this.mediaDirPath = mediaDirPath;
	    }

	    public String getInputFilePath() {
	        return inputFilePath;
	    }

	    public void setInputFilePath(String inputFilePath) {
	        this.inputFilePath = inputFilePath;
	    }

	    public long getChunkingTime() {
	        return chunkingTime;
	    }

	    public void setChunkingTime(long chunkingTime) {
	        this.chunkingTime = chunkingTime;
	    }
	}

