package com.media.demo.model;

public class ChunkingResult {
    private int finalExitCode;
    private long chunkingTime;

    public ChunkingResult(int finalExitCode, long chunkingTime) {
        this.finalExitCode = finalExitCode;
        this.chunkingTime = chunkingTime;
    }

    public int getFinalExitCode() {
        return finalExitCode;
    }

    public long getChunkingTime() {
        return chunkingTime;
    }

    public void setFinalExitCode(int finalExitCode) {
        this.finalExitCode = finalExitCode;
    }

    public void setChunkingTime(long chunkingTime) {
        this.chunkingTime = chunkingTime;
    }
}
