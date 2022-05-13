package edu.cuz.mamv2;

import com.baidu.aip.speech.AipSpeech;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;

/**
 * @author VM
 * @date 2022/5/8 1:12
 */
@Slf4j
@SpringBootTest
public class AipSpeechTest {
    private static final String videoPath = "D:/Code/Java/mamv2/src/test/java/edu/cuz/mamv2/assert/test.mp4";
    private static final String audioPath = "D:/Code/Java/mamv2/src/test/java/edu/cuz/mamv2/assert/test.wav";
    
    public void getAudio() {
        File video = new File(videoPath);
        File target = new File(audioPath);
        MultimediaObject multimediaObject = new MultimediaObject(video);
        AudioAttributes audioAttributes = new AudioAttributes();
        audioAttributes.setCodec("pcm_s16le");
        audioAttributes.setBitRate(8000);
        EncodingAttributes encodingAttributes = new EncodingAttributes();
        encodingAttributes.setAudioAttributes(audioAttributes);
        encodingAttributes.setOutputFormat("wav");
        Encoder encoder = new Encoder();
        try {
            encoder.encode(multimediaObject, target, encodingAttributes);
        } catch (EncoderException e) {
            log.info("提取失败：{}", e.getMessage());
        }
    }
}
