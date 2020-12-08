package org.jxls.template;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.GridCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.JxlsException;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class for grid export
 */
public class SimpleExporter {
    public static final String GRID_TEMPLATE_XLS = "grid_template.xls";
    static Logger logger = LogManager.getLogger(SimpleExporter.class);

    private byte[] templateBytes;

    public SimpleExporter() {
    }

    public void registerGridTemplate(InputStream inputStream) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count;
        while ((count = inputStream.read(data)) != -1) {
            os.write(data, 0, count);
        }
        templateBytes = os.toByteArray();
    }

    public void gridExport(Iterable<?> headers, Iterable<?> dataObjects, String objectProps, OutputStream outputStream) {
        if (templateBytes == null) {
            InputStream is = SimpleExporter.class.getResourceAsStream(GRID_TEMPLATE_XLS);
            try {
                registerGridTemplate(is);
            } catch (IOException e) {
                String message = "Failed to read default template file " + GRID_TEMPLATE_XLS;
                logger.error(message);
                throw new JxlsException(message, e);
            }
    	}    	
    	InputStream is = new ByteArrayInputStream(templateBytes);
        Transformer transformer = TransformerFactory.createTransformer(is, outputStream);
        AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
        List<Area> xlsAreaList = areaBuilder.build();
        Area xlsArea = xlsAreaList.get(0);
        Context context = new Context();
        context.putVar("headers", headers);
        context.putVar("data", dataObjects);
        GridCommand gridCommand = (GridCommand) xlsArea.getCommandDataList().get(0).getCommand();
        gridCommand.setProps(objectProps);
        xlsArea.applyAt(new CellRef("Sheet1!A1"), context);
        try {
            transformer.write();
        } catch (IOException e) {
            final String msg = "Failed to write to output stream";
            logger.error(msg, e);
            throw new JxlsException(msg, e);
        }
    }
}
