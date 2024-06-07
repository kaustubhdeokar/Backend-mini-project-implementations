package com.bookacab.rider.service;

import com.bookacab.rider.config.AppConfig;
import com.bookacab.rider.dao.GeoData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

@Service
public class GeoDataService {

    private final RestTemplate restTemplate;
    private final KafkaTemplate kafkaTemplate;

    public GeoDataService(RestTemplate restTemplate, KafkaTemplate kafkaTemplate) {
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void getRandomGeoData() throws Exception {
        String url = "https://api.3geonames.org/?randomland=yes";
        for(int i=0;i<10;i++) {
            String response = restTemplate.getForObject(url, String.class);
            parseXML(response);
        }
    }

    private void parseXML(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        NodeList nodeList = document.getElementsByTagName("nearest");

        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String latt = element.getElementsByTagName("latt").item(0).getTextContent();
                String longt = element.getElementsByTagName("longt").item(0).getTextContent();
                kafkaTemplate.send(AppConfig.topic, new GeoData(latt, longt).toString());
            }
        }
    }


}
