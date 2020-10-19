package br.unesp.rc.mqttexample;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Main {
    public static void main(String[] args) throws IOException {
        final String topic        = "gHome/FEC00C9B4130/#";
        final int qos             = 2; // Garantia de entrega: deve receber uma e única vez
        final String broker       = "tcp://g.gficher.com:1883";
        final String username = "unesp";
        final String password = "unesp";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            // Conecta ao Broker
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId(), persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());

            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");
            
            
            // Increve em um tópico
            MqttCallback callback;
            callback = new MqttCallbackExample();
            
            System.out.println("Subscribing to: " + topic);
            client.subscribe(topic);
            client.setCallback(callback);

            System.in.read();
            
            
            // Desconecta do Broker
//            client.disconnect();
//            System.out.println("Disconnected");
//            client.close();
        } catch(MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
