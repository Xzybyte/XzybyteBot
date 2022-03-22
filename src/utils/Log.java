package utils;

import main.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class Log {

    private List<ChannelLogs> chs = new ArrayList<>();

    public void addChannel(String chId, Message msg) {
        ChannelLogs cl = new ChannelLogs(chId);
        chs.add(cl);
        cl.addMessage(msg);
    }

    public List<ChannelLogs> getAllChannels() {
        return chs;
    }

    public ChannelLogs getChannel(String chId) {
        return getAllChannels().stream().filter(cl -> cl.getId().equals(chId)).findFirst().orElse(null);
    }

    /*
        Load up to 100 messages from existing channels
     */
    public void LoadLogs() {
        for (TextChannel ch : Main.getInstance().getGuild().getTextChannels()) {
            if (ch != null) {
                ch.getHistory().retrievePast(100).queue((List<Message> mess) -> mess.forEach((messages) -> {
                    if (getChannel(ch.getId()) != null) {
                        getChannel(ch.getId()).addMessage(messages);
                    } else {
                        addChannel(ch.getId(), messages);
                    }
                }));
            }
        }
    }

    public class ChannelLogs {

        private String id;
        private List<Message> msg = new ArrayList<>();

        public ChannelLogs(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void addMessage(Message m) {
            msg.add(m);
        }

        public List<Message> getMessages() {
            return msg;
        }

        public Message getMessage(String messageId) {
            return getMessages().stream().filter(message -> message.getId().equals(messageId)).findFirst().orElse(null);
        }

    }

}
