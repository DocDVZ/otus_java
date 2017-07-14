package ru.otus.L09.frontend;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author v.chibrikov
 *
 *
 */
public class PageGenerator {
    private static final String HTML_DIR = "webapps";
    private static PageGenerator instance = new PageGenerator();

    private final Configuration cfg = new Configuration();

//    private PageGenerator() {
//        cfg.setClassForTemplateLoading(this.getClass(), "/");
//    }

    public static PageGenerator instance() {
        return instance;
    }

    public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
//            Template template = cfg.getTemplate(filename);
            Template template = cfg.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        System.out.println();
        return stream.toString();
    }
}
