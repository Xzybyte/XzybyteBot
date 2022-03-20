package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private String welcomeChannel = "";
    private String welcomeMessage = "";
    private String logsChannel = "";
    private String defaultRoleID = "";
    private String prefix = "!";

    public void loadConfig() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config.ini"));

            setWelcomeChannel(p.getProperty("welcomeChannel"));
            setWelcomeChannel(p.getProperty("welcomeMessage"));
            setLogsChannel(p.getProperty("logsChannel"));
            setDefaultRoleID(p.getProperty("defaultRoleID"));
            setPrefix(p.getProperty("prefix"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void setWelcomeChannel(String channel) {
        this.welcomeChannel = channel;
    }

    public String getWelcomeChannel() {
        return welcomeChannel;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setLogsChannel(String channel) {
        this.logsChannel = channel;
    }

    public String getLogsChannel() {
        return logsChannel;
    }

    public void setDefaultRoleID(String roleId) {
        this.defaultRoleID = roleId;
    }

    public String getDefaultRoleID() {
        return defaultRoleID;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
