package de.muenchen.mobidam.security;

import lombok.extern.slf4j.Slf4j;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Service
@Slf4j
public class MaliciousXmlCodeDetector implements MaliciousCodeDetector {

    public boolean isValidData(final InputStream stream) throws ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc;
        try {
            doc = builder.parse(stream);
        } catch (SAXException e) {
            return false;
        }
        ArrayList<String> textNodes = traverseNode(doc.getDocumentElement());
        for (String textNode : textNodes) {
            log.trace(textNode);
            String clean = Encode.forHtml(textNode);
            if (!textNode.equals(clean)) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<String> traverseNode(Node node) {
        ArrayList<String> textNodes = new ArrayList<>();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                String textContent = child.getTextContent().trim();
                if (!textContent.isEmpty()) {
                    textNodes.add(textContent);
                }
            } else {
                textNodes.addAll(traverseNode(child));
            }
        }
        return textNodes;
    }

}
