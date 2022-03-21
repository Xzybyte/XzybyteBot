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

    private String user;
    private String password;
    private String database;
    private String host;
    private String port;

    public void loadConfig() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config.ini"));

            setWelcomeChannel(p.getProperty("welcomeChannel"));
            setWelcomeMessage(p.getProperty("welcomeMessage"));
            setLogsChannel(p.getProperty("logsChannel"));
            setDefaultRoleID(p.getProperty("defaultRoleID"));
            setPrefix(p.getProperty("prefix"));

            setUser(p.getProperty("user"));
            setPassword(p.getProperty("password"));
            setDatabase(p.getProperty("database"));
            setHost(p.getProperty("host"));
            setPort(p.getProperty("port"));

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

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatabase() {
        return database;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
