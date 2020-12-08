package org.jxls.examples;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.jxls.area.XlsArea;
import org.jxls.command.ImageCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.ImageType;
import org.jxls.entity.Department;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.jxls.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Leonid Vysochyn
 */
public class ImageDemo {
    private static final Logger logger = LogManager.getLogger(ImageDemo.class);
    private static final String template = "image_demo.xlsx";
    private static final String template2 = "image_demo2.xlsx";
    private static final String output = "target/image_output.xlsx";
    private static final String output2 = "target/image_output2.xlsx";
    private static final String output3 = "target/image_output3.xlsx";
    private static final String template4 = "image_demo4.xlsx";
    private static final String output4 = "target/image_output4.xlsx";

    @Test
    public void test() throws IOException {
        logger.info("Running Image demo");
        execute();
        execute2();
        executeWithNullBytes();
        executeResizePicture();
    }

    private void execute() throws IOException {
        logger.info("Opening input stream");
        try (InputStream is = ImageDemo.class.getResourceAsStream(template)) {
            try (OutputStream os = new FileOutputStream(output)) {
                Transformer transformer = TransformerFactory.createTransformer(is, os);
                XlsArea xlsArea = new XlsArea("Sheet1!A1:N30", transformer);
                Context context = new Context();
                InputStream imageInputStream = ImageDemo.class.getResourceAsStream("business.png");
                byte[] imageBytes = Util.toByteArray(imageInputStream);
                context.putVar("image", imageBytes);
                XlsArea imgArea = new XlsArea("Sheet1!A5:D15", transformer);
                xlsArea.addCommand("Sheet1!A4:D15", new ImageCommand("image", ImageType.PNG).addArea(imgArea));
                xlsArea.applyAt(new CellRef("Sheet2!A1"), context);
                transformer.write();
                logger.info("written to file");
            }
        }
    }

    private void execute2() throws IOException {
        try (InputStream is = ImageDemo.class.getResourceAsStream(template2)) {
            try (OutputStream os = new FileOutputStream(output2)) {
                Context context = new Context();
                InputStream imageInputStream = ImageDemo.class.getResourceAsStream("business.png");
                byte[] imageBytes = Util.toByteArray(imageInputStream);
                Department department = new Department("Test Department");
                department.setImage(imageBytes);
                context.putVar("dep", department);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }
    }

    private void executeWithNullBytes() throws IOException {
        try (InputStream is = ImageDemo.class.getResourceAsStream(template2)) {
            try (OutputStream os = new FileOutputStream(output3)) {
                Context context = new Context();
                byte[] imageBytes = null;
                Department department = new Department("Test Department");
                department.setImage(imageBytes);
                context.putVar("dep", department);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }
    }

    private void executeResizePicture() throws IOException {
        try (InputStream is = ImageDemo.class.getResourceAsStream(template4)) {
            try (OutputStream os = new FileOutputStream(output4)) {
                Context context = new Context();
                InputStream imageInputStream = ImageDemo.class.getResourceAsStream("business.png");
                byte[] imageBytes = Util.toByteArray(imageInputStream);
                Department department = new Department("Test Department");
                department.setImage(imageBytes);
                context.putVar("dep", department);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }
    }
}
