package com.bitozen.zencamp.backend.common.type;

public enum FileExtention {
    
    PDF("pdf"),
    XLS("xls"),
    CSV("csv"),
    TXT("txt");

    private String format;

    private FileExtention(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
