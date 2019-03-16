package sd.camel.demo.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd.camel.demo.processor.DocReader;

import static org.apache.camel.Exchange.FILE_NAME;

@Component
public class DocProcessorRoute extends RouteBuilder {
    private static final Logger LOGGER = LogManager.getLogger(DocProcessorRoute.class);
    private static final String KEY_WORD = "Dev";

    @Autowired
    private DocReader docReader;

    @Override
    public void configure() throws Exception {
        LOGGER.info("Route configuration started");
        from("file:data?noop=true&include=.*.docx|.*.doc")
                .log("Starting to process file ${header.CamelFilenameOnly}")
                .process(docReader)
                .filter(bodyAs(String.class).contains(KEY_WORD))
                .log("'" + KEY_WORD + "' is present in ${header.CamelFilenameOnly}")
                .setHeader(FILE_NAME, header(FILE_NAME).regexReplaceAll("\\.docx|\\.doc", ".txt"))
                .log("New file ${header.CamelFilename} is to be written in destination")
                .to("file:destination")
                .log("File processing is completed")
                .end();

        LOGGER.info("Route configuration ended");
    }
}
