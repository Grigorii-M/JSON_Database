package client;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "-in", description = "file name")
    private String fileName;

    @Parameter(names = "-t", description = "command type")
    private String command;

    @Parameter(names = "-k", description = "key")
    private String key;

    @Parameter(names = "-v", description = "value")
    private String value;

    public String getFileName() {
        return fileName;
    }

    public String getType() {
        return command;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
