package com.hafia.ericsson.kafkaconnect.nodealarms;

import com.hafia.ericsson.kafkaconnect.nodealarms.models.OssAlarmFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmFileSchemas.FILE_ROWS_AFFECTED_FIELD;
import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmFileSchemas.ID_FIELD;


class OssAlarmFileAPISftpClientTest {

    @Test
    void test(){
        AlarmSourceConnectorConfig config = new AlarmSourceConnectorConfig(Static.initialConfig());
        Instant lastModifiedAt = ZonedDateTime.now().minusMinutes(config.sinceConfig).toInstant();
        Instant now = Instant.now();
        long duration = Duration.between(lastModifiedAt, now).toMillis();
        long waitingMillis =  ((5 * 60 * 1000) - duration);
        System.out.println(lastModifiedAt  + " " + now + " = " + duration + " so " + (waitingMillis + 30_000));
        long yourmilliseconds =waitingMillis + 30_000;
        //1322018752992-Nov 22, 2011 9:25:52 PM


        Instant instant = ZonedDateTime.now().toInstant().plusMillis(yourmilliseconds);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.LONG)
                        .withLocale( Locale.UK )
                        .withZone( ZoneId.systemDefault() );
        String output = formatter.format(instant);
        System.out.printf("formatter: %s with zone: %s and Locale: %s%n",
                formatter,
                formatter.getZone(),
                formatter.getLocale());
        System.out.println("instant: " + instant );
        System.out.println("output: " + output );

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        System.out.println(DATE_TIME_FORMATTER.format(instant));
    }

    @Test
    void getNextFile() {
        AlarmSourceConnectorConfig config = new AlarmSourceConnectorConfig(Static.initialConfig());
        OssAlarmFileAPISftpClient client = new OssAlarmFileAPISftpClient(config);

        JSONArray alarmFiles = client.getNextFile(ZonedDateTime.now().minusMinutes(config.sinceConfig).toInstant(),0, 0 );
        assert  (client.getErrorMessages().size() == 0);
        assert  (alarmFiles.length() > 0 );
        assert  (alarmFiles.getJSONObject(0).get(ID_FIELD) instanceof Long);
        assert  (alarmFiles.getJSONObject(0).get(FILE_ROWS_AFFECTED_FIELD) instanceof Integer);

        for (Object obj : alarmFiles) {
            OssAlarmFile ossAlarmFile = OssAlarmFile.fromJson((JSONObject) obj);
            System.out.println(ossAlarmFile.getRecordSequence());
            break;
        }
    }
}