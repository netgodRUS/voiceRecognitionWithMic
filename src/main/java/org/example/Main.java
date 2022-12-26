package org.example;

import org.json.JSONObject;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, LineUnavailableException {

        LibVosk.setLogLevel(LogLevel.DEBUG);
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 60000, 16, 2, 4, 44100, false);

        TargetDataLine microphone;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try (Model model = new Model("C:\\Users\\HOME\\IdeaProjects\\voice recognition\\.idea\\vosk-model-small-ru-0.22");
             Recognizer recognizer = new Recognizer(model, 120000)) {
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] b = new byte[4096];

            while (true) {
                numBytesRead = microphone.read(b, 0, CHUNK_SIZE);
                out.write(b, 0, numBytesRead);
                if (recognizer.acceptWaveForm(b, numBytesRead)) {
                    JSONObject json = new JSONObject(recognizer.getResult());
                    System.out.println(json.getString("text"));
                }
            }
        }
    }
}
