package sd.camel.demo.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;

@Component
public class DocProcessor implements Processor {
    private static final Logger LOGGER = LogManager.getLogger(DocProcessor.class);
    private static final String KEY_WORD = "Dev";

    @Override
    public void process(Exchange exchange) throws Exception {
        File file = exchange.getIn().getBody(File.class);
        LOGGER.info("Processing file :: {}", file);
        String fileContent = null;
        if (file.getName().endsWith(".docx")) {
            try (XWPFWordExtractor docx = new XWPFWordExtractor(new XWPFDocument(new FileInputStream(file)))) {
                fileContent = docx.getText();
            }
        } else if (file.getName().endsWith(".doc")) {
            try (WordExtractor doc = new WordExtractor(new HWPFDocument(new FileInputStream(file)))) {
                fileContent = doc.getText();
            }
        }
        LOGGER.info("Content of file :: {}", fileContent);
        if (fileContent != null && fileContent.contains(KEY_WORD)) {
            LOGGER.info("{} matched", KEY_WORD);
            exchange.getIn().setBody(fileContent);
            String destinationFileName = file.getName().split("\\.doc|\\.docx")[0] + ".txt";
            exchange.getIn().setHeader("CamelFileName", destinationFileName);
            LOGGER.info("Updated file name :: {}", destinationFileName);
        }
    }
}
