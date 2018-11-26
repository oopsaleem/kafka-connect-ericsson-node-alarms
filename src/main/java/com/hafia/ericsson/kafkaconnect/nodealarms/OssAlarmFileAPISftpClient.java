package com.hafia.ericsson.kafkaconnect.nodealarms;

import com.jcraft.jsch.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.*;

import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmFileSchemas.*;

public class OssAlarmFileAPISftpClient {
    private static Logger log = LoggerFactory.getLogger(OssAlarmFileAPISftpClient.class);

    private Session session;
    private ChannelSftp channelSftp;
    private Channel channel;
    private List<String> errorMessages;

    AlarmSourceConnectorConfig config;

    public OssAlarmFileAPISftpClient(AlarmSourceConnectorConfig config) {
        this.config = config;
        errorMessages = new ArrayList<>();

        initSftp();
    }

    protected JSONArray getNextFile(Instant since) {
//        if(! channelSftp.isConnected()) throw new Exception("Server is disconnected.");

        JSONArray jsonArray = new JSONArray();
        try {
            for (String filePath : config.filePathsConfig.split("; |;")) {
                Vector<ChannelSftp.LsEntry> list = channelSftp.ls(filePath);
                for(ChannelSftp.LsEntry entry : list) {
                    SftpATTRS sftpATTRS = entry.getAttrs();
                    entry.getAttrs().getPermissions();
                    Instant modifiedAt = Instant.ofEpochMilli(sftpATTRS.getMTime() * 1000L);

                    if( Duration.between(since, modifiedAt).toMinutes() < 0) continue;

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    String fileName = entry.getFilename();
                    if(fileName.replaceAll("[. ]" , "").isEmpty()) continue; //ignore . and .. files
                    String path = filePath.substring(0, filePath.lastIndexOf("/") + 1);
                    channelSftp.get(path + fileName, byteArrayOutputStream);

                    String fileContent = byteArrayOutputStream.toString("UTF-8");
                    JSONObject jo = new JSONObject();

                    Pattern regex = Pattern.compile("\\d{8,}");
                    Matcher regexMatcher = regex.matcher(fileName);
                    if (regexMatcher.find())
                        jo.put(ID_FIELD, Long.parseLong(regexMatcher.group()));
                    else
                        jo.put(ID_FIELD, 100_000_000);

                    jo.put(OSS_GENERATION_FIELD, config.ossGenerationConfig);

                    Pattern rowsAffectedRx = Pattern.compile("(\\d+) rows affected");
                    Matcher rowsAffectedMatcher = rowsAffectedRx.matcher(fileContent);
                    if (rowsAffectedMatcher.find())
                        jo.put(FILE_ROWS_AFFECTED_FIELD, Integer.parseInt(rowsAffectedMatcher.group(1)));
                    else jo.put(FILE_ROWS_AFFECTED_FIELD, -1);

                    jo.put(MODIFIED_AT_FIELD, modifiedAt.toString());
                    jo.put(FILE_CONTENT_FIELD, fileContent);
                    jsonArray.put(jo);
                }
            }
        } catch (SftpException e ) {
            e.printStackTrace();
            disconnect();
            errorMessages.add(e.getMessage());
            log.error("Unable to sftp.get file.", e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            errorMessages.add(e.getMessage());
            log.error("UnsupportedEncoding error", e);
        } finally {
            disconnect();
        }

        return jsonArray;
    }

    private void initSftp(){
        final JSch ssh = new JSch();
        try {
            session = ssh.getSession(config.nodeAuthUsernameConfig, config.nodeHostConfig, config.nodePortConfig);
        } catch (JSchException e) {
            errorMessages.add(e.getMessage());
            log.error("Could not create ssh session", e);
        } catch (Exception e){
            errorMessages.add(e.getMessage());
            e.printStackTrace();
        }
        session.setPassword(config.nodeAuthPasswordConfig);

        try {
            Properties properties = new Properties();
            properties.put( "PreferredAuthentications", "publickey,keyboard-interactive,password");
            properties.put("StrictHostKeyChecking", "no");
            properties.put("UseDNS", "no");
            session.setConfig(properties);
            session.connect(30_000);
        } catch ( JSchException e) {
            session = null;
            errorMessages.add(e.getMessage());
            log.error("Could not establish ssh connection", e);
            e.printStackTrace();
        } catch (Exception e){
            errorMessages.add(e.getMessage());
            e.printStackTrace();
        }

        try {
            channel = session.openChannel("sftp");
            channel.connect(60_000);
            channelSftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            channel = null;
            channelSftp = null;
            session.disconnect();
            session = null;
            log.error("Could not open sftp channel", e);
            errorMessages.add(e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            log.error("Unexpected error !", e);
            errorMessages.add(e.getMessage());
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            if (channel != null)
                channel.disconnect();
        } finally {
            channel = null;
            try {
                if (session != null)
                    session.disconnect();
            } finally {
                session = null;
            }
        }
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
