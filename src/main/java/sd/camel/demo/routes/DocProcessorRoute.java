package sd.camel.demo.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd.camel.demo.processor.DocProcessor;

@Component
public class DocProcessorRoute extends RouteBuilder {
    private static final Logger LOGGER = LogManager.getLogger(DocProcessorRoute.class);

    @Autowired
    private DocProcessor docProcessor;

    @Override
    public void configure() throws Exception {
        LOGGER.info("Route configuration started");
        from("file:data?noop=true&include=.*.doc|.*.docx")
                .log("File ${header.CamelFileNameOnly} is being copied..")
                .process(docProcessor)
                .to("file:destination")
                .end();

        LOGGER.info("Route configuration ended");
    }
}
