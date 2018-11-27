package com.hafia.ericsson.kafkaconnect.nodealarms;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.Result;

import java.io.IOException;
import java.util.Properties;

import static net.sf.expectit.filter.Filters.removeColors;
import static net.sf.expectit.filter.Filters.removeNonPrintable;
import static net.sf.expectit.matcher.Matchers.regexp;
import static net.sf.expectit.matcher.Matchers.sequence;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EricssonNodeAPIsshClient {
    private static final Logger log = LoggerFactory.getLogger(EricssonNodeAPIsshClient.class);

    AlarmSourceConnectorConfig config;

    public EricssonNodeAPIsshClient(AlarmSourceConnectorConfig config) {
        this.config = config;
    }

    protected void initConnection() throws InterruptedException, JSchException, IOException {
        JSch jSch = new JSch();
        if (!config.nodeAuthUsernameConfig.isEmpty() && !config.nodeHostConfig.isEmpty() ){
            Session session = jSch.getSession(config.nodeAuthUsernameConfig, config.nodeHostConfig);
            if(!config.nodeAuthPasswordConfig.isEmpty())
            session.setPassword(config.nodeAuthPasswordConfig);
            Properties properties = new Properties();
            properties.put( "PreferredAuthentications", "publickey,keyboard-interactive,password");
            properties.put("StrictHostKeyChecking", "no");
            properties.put("UseDNS", "no");
            session.setConfig(properties);
            session.connect();

            Channel channel = session.openChannel("shell");
            channel.connect();
            Expect expect = new ExpectBuilder()
                    .withOutput(channel.getOutputStream())
                    .withInputs(channel.getInputStream(), channel.getExtInputStream())
                    .withEchoInput(System.out)
                    .withEchoOutput(System.err)
                    .withInputFilters(removeColors(), removeNonPrintable())
                    .withExceptionOnFailure()
                    .build();
            try {
                expect.expect(regexp("jw2uas1> "));
                expect.sendLine("eaw M1IS1");
                expect.expect(regexp("<"));
                expect.sendLine("erepp:enum=all;");
                Result ssss = expect.expect(sequence(regexp(".+"), regexp("<")));
                log.debug(ssss.getInput());
                //expect.expect(regexp("<"));
                //expect.expect(contains("[RETURN]"));
                //expect.sendLine();
                //String ipAddress = expect.expect(regexp("Trying (.*)\\.\\.\\.")).group(1);
                //System.out.println("Captured IP: " + ipAddress);
                //expect.expect(contains("login:"));
                //expect.sendLine("testuser");
                //expect.expect(contains("(Y/N)"));
                //expect.send("N");
                //expect.expect(regexp(": $"));
            } finally {
                expect.close();
                channel.disconnect();
                session.disconnect();
            }
        }
    }
}
