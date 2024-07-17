package de.muenchen.mobidam.security;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.junit.jupiter.api.Test;
import org.owasp.encoder.Encode;
import org.owasp.esapi.errors.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EsapiTest
{

//    @Test
//    public void testIsValidEmail()
//    {
//        Validator instance = ESAPI.validator();
//        assertTrue(instance.isValidInput("test", "jeff.williams@aspectsecurity.com", "Email",
//                100,false));
//    }
//
//    @Test
//    public void testIsInvalidEmail()
//    {
//        Validator instance = ESAPI.validator();
//        assertFalse(instance.isValidInput("test", "jeff.williamsaspectsecurity.com", "Email",
//                100,false));
//    }

//    @Test
//    public void testIsInvalidXml() throws IOException, ValidationException {
//        String unsafe = Files.readString(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml"));
//        assertFalse(ESAPI.validator().isValidInput("input validation context", unsafe, "XML", 25500, false));
//    }
//
//    @Test
//    public void testIsValidXml() throws IOException, ValidationException {
//        String safe = Files.readString(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test0.xml"));
//        Validator instance = ESAPI.validator();
//        assertTrue(ESAPI.validator().isValidInput("input validation context", safe, "XML", 25500, false));
//    }
//
    @Test
    public void testIsInvalidMimetypeTika() throws IOException, ValidationException, TikaException {
//        String safe = Files.readString(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test0.xml"));
//        String unsafe = Files.readString(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml"));
        TikaConfig tika = new TikaConfig();

            Metadata metadata = new Metadata();
            //TikaInputStream sets the TikaCoreProperties.RESOURCE_NAME_KEY
            //when initialized with a file or path
            MediaType mimetype = tika.getDetector().detect(TikaInputStream.get(Path.of("C:\\Users\\martin.dietrich\\tmp\\jfr.exe.xml")), metadata);
            System.out.println("File is " + mimetype);
            assertNotEquals("application/xml", mimetype.toString());
    }

    @Test
    public void testIsValidMimetypeTika() throws IOException, ValidationException, TikaException {
//        String safe = Files.readString(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test0.xml"));
//        String unsafe = Files.readString(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml"));
        TikaConfig tika = new TikaConfig();

            Metadata metadata = new Metadata();
            //TikaInputStream sets the TikaCoreProperties.RESOURCE_NAME_KEY
            //when initialized with a file or path
            MediaType mimetype = tika.getDetector().detect(TikaInputStream.get(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml")), metadata);
            System.out.println("File is " + mimetype);
            assertEquals("application/xml", mimetype.toString());
    }

    @Test
    public void testIsValidXmlTika() throws IOException, ValidationException, TikaException {
//        String safe = Files.readString(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test0.xml"));
//        String unsafe = Files.readString(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml"));
        TikaConfig tika = new TikaConfig();

            Metadata metadata = new Metadata();
            //TikaInputStream sets the TikaCoreProperties.RESOURCE_NAME_KEY
            //when initialized with a file or path
            MediaType mimetype = tika.getDetector().detect(TikaInputStream.get(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test0.xml")), metadata);
            System.out.println("File is " + mimetype);
            assertEquals("application/xml", mimetype.toString());
    }

    // wapiti: py-script to crawl websites

//    @Test
//    public void testIsInvalidAntiSamy() throws PolicyException, IOException {
//        String unsafe = Files.readString(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test-xss.xml"));
//        Policy policy = Policy.getInstance(this.getClass().getResourceAsStream("/antisamy.xml"));
//
//        AntiSamy as = new AntiSamy();
//        try {
//            CleanResults cr = as.scan(unsafe, policy);
//            System.out.println("errors: "+cr.getNumberOfErrors());
//            DocumentFragment fr = cr.getCleanXMLDocumentFragment();
//            System.out.println(fr.getTextContent());
//        } catch (ScanException e) {
//            System.err.println(e);
//            throw new RuntimeException(e);
//        }
//
////        MyUserDAO.storeUserProfile(cr.getCleanHTML()); // some custom function
//    }

//    @Test
//    public void testIsInvalidBinDetector() throws IOException {
//        assertTrue(BinaryContentDetector.containsBinaryContent(new File("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml")));
//    }
//
//    @Test
//    public void testIsValidBinDetector() throws IOException {
//        assertFalse(BinaryContentDetector.containsBinaryContent(new File("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test0.xml")));
//    }

//    @Test
//    public void testIsInvalidInvalidByteSequenceDetector() throws IOException {
//        assertFalse(InvalidByteSequenceDetector.isValid("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml"));
//    }
//
//    @Test
//    public void testIsInvalidInvalidByteSequenceDetectorExe() throws IOException {
//        assertFalse(InvalidByteSequenceDetector.isValid("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\jfr.exe.xml"));
//    }
//
//    @Test
//    public void testIsValidInvalidByteSequenceDetector() throws IOException {
//        assertTrue(InvalidByteSequenceDetector.isValid("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test0.xml"));
//    }

//    @Test
//    public void testIsValidInvalidByteSequenceDetector3() throws IOException {
//        assertFalse(InvalidByteSequenceDetector.test2("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml"));
//    }
//
//    @Test
//    public void testIsValidInvalidByteSequenceDetector4() throws IOException {
//        assertTrue(InvalidByteSequenceDetector.test2("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test0.xml"));
//    }
//
//    @Test
//    public void testIsValidInvalidByteSequenceDetector5() throws IOException {
//        assertTrue(InvalidByteSequenceDetector.test2("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\output-statisch-2.xml"));
//    }

    @Test
    public void testIsInvalidJSoup() throws IOException {
//        assertTrue(InvalidByteSequenceDetector.test2("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\output-statisch-2.xml"));
//        Jsoup.isValid(unsafe, safeList); // usable just with a safelist
    }

//    @Test
//    public void testIsInvalidTidy() throws IOException {
////        assertTrue(InvalidByteSequenceDetector.test2("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\output-statisch-2.xml"));
//        Tidy tidy = new Tidy();
//        tidy.setXmlTags(true);
//        tidy.setQuiet(true);
//        tidy.setCharEncoding(org.w3c.tidy.Configuration.UTF8);
//        Document doc = tidy.parseDOM(new FileInputStream("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test-xss.xml"), null);
////        traverseNode(tidy, doc.getDocumentElement()); // tidy does not support node.getTextContent
//    }

    @Test
    public void testIsInvalidDOMParser() throws IOException, SAXException, ParserConfigurationException {
//        assertTrue(InvalidByteSequenceDetector.test2("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\output-statisch-2.xml"));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        // parse the XML document

//        File file = new File("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test-xss.xml"); // OK
//        File file = new File("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\output-statisch-xss.xml"); // OK
//        File file = new File("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml"); // OK
        File file = new File("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\output-statisch-exe.xml");

        Document doc = builder.parse(file);

        // traverse the document tree and extract textContent nodes

        ArrayList<String> textNodes = traverseNode(doc.getDocumentElement());

        // print the textContent nodes to the console

        for (String textNode : textNodes) {

            System.out.println(textNode);
            String clean = Encode.forHtml(textNode);
            if (!textNode.equals(clean)){
                System.out.println("XSS");
            }

        }
    }

    public static ArrayList<String> traverseNode(Node node) {

        ArrayList<String> textNodes = new ArrayList<String>();

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
