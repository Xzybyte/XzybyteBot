package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private String welcomeChannel = "";
    private String logsChannel = "";

    public void loadConfig() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config.ini"));

            setWelcomeChannel(p.getProperty("welcomeChannel"));
            setLogsChannel(p.getProperty("logsChannel"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void setWelcomeChannel(String channel) {
        this.welcomeChannel = channel;
    }

    public String getWelcomeChannel() {
        return welcomeChannel;
    }

    public void setLogsChannel(String channel) {
        this.logsChannel = channel;
    }

    public String getLogsChannel() {
        return logsChannel;
    }

}
