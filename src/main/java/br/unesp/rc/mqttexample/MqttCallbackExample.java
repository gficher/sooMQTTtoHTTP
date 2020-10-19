package br.unesp.rc.mqttexample;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class MqttCallbackExample implements MqttCallback {
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Pega hora atual
        LocalDateTime now = LocalDateTime.now();

        // Pega informações do sensor pelo tópico
        String[] topic_split = topic.split("\\/");

        System.out.println(topic_split[1] + " (" + topic_split[2] + "): " + message);



        // Prepara requisição HTTP
        URL url = new URL("http://localhost:8080/api/sensores/leitura");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        // Formata payload
        byte[] out = ("{\"" +
                "tipoSensor\":\"" + topic_split[2] + "\"," +
                "\"data\":\"" + now.toString() + "\"," +
                "\"valor\":\"" + message + "\"" +
        "}").getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        // Envia requisição POST
        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // Se algum erro acontecer, podemos ver o que causou mais facilmente
        cause.printStackTrace();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        // Nós não precisamos disso neste projeto
    }
}
