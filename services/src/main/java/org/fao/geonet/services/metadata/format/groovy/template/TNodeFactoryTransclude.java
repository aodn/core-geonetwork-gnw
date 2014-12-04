package org.fao.geonet.services.metadata.format.groovy.template;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.fao.geonet.SystemInfo;
import org.fao.geonet.services.metadata.format.groovy.Handlers;
import org.fao.geonet.services.metadata.format.groovy.TransformationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Creates nodes that import other templates into the current template.
 *
 * @author Jesse on 12/3/2014.
 */
@Component
public class TNodeFactoryTransclude extends TNodeFactoryByAttName {
    private static final String TRANSCLUDE = "transclude";
    private static final String MODEL_KEY = TRANSCLUDE + "-model";
    private static final String EXTRA_MODEL = TRANSCLUDE + "-extra-model";
    private static final String REPLACE = TRANSCLUDE + "-replace";

    private static final TextContentParser TEXT_PARSER = new TextContentParser();

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private SystemInfo info;

    protected TNodeFactoryTransclude() {
        super(TRANSCLUDE);
    }

    public TNodeFactoryTransclude(SystemInfo info) {
        super(TRANSCLUDE);
        this.info = info;
    }

    @Override
    public TNode create(String localName, String qName, Attributes attributes) throws IOException {
        Attributes filteredAtts = new FilteredAttributes(attributes, TRANSCLUDE, REPLACE, MODEL_KEY, EXTRA_MODEL);
        String templatePath = getValue(attributes, TRANSCLUDE);
        String model = getValue(attributes, MODEL_KEY);
        String extraModelAtts = getValue(attributes, EXTRA_MODEL);

        Map<String, Object> extraModel = Maps.newHashMap();
        if (extraModelAtts != null) {

            for (String att : extraModelAtts.split("\\|")) {
                final String[] parts = att.split("=");
                String key = parts[0];
                String value = parts.length > 1 ? parts[1] : null;

                extraModel.put(key, value);
            }
        }

        String replaceAtt = getValue(attributes, REPLACE);
        boolean replace = false;
        if (replaceAtt != null) {
            replace = replaceAtt.isEmpty() || replaceAtt.equalsIgnoreCase("y") || replaceAtt.equalsIgnoreCase("true")
                      || replaceAtt.equalsIgnoreCase("yes");
        }

        return new TNodeTransclude(info, qName, filteredAtts, templatePath, replace, model, extraModel);
    }

    private class TNodeTransclude extends TNode {

        private final String templatePath;
        private final boolean replace;
        private final String model;
        private final Map<String, Object> extraModel;

        public TNodeTransclude(SystemInfo info, String qName, Attributes attributes, String templatePath, boolean replace,
                               String model, Map<String, Object> extraModel) throws IOException {
            super(info, qName, attributes);
            this.replace = replace;
            this.templatePath = templatePath;
            this.model = model;
            this.extraModel = extraModel;
        }

        @Override
        protected Optional<String> canRender(TRenderContext context) {
            return Optional.absent();
        }

        @Override
        public void render(TRenderContext context) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final TRenderContext sepContext = new TRenderContext(outputStream, context.getModel(true));
            if (replace) {
                for (TNode childNode : getChildren()) {
                    childNode.render(sepContext);
                }
            } else {
                super.render(sepContext);
            }

            Map<String, Object> fullModel = Maps.newHashMap();
            fullModel.putAll(context.getModel(true));
            for (Map.Entry<String, Object> entry : this.extraModel.entrySet()) {
                OutputStream o = new ByteArrayOutputStream();
                TEXT_PARSER.parse(entry.getValue().toString()).render(new TRenderContext(o, context.getModel(true)));
                fullModel.put(entry.getKey(), o.toString());
            }
            fullModel.put(this.model, outputStream.toString());
            final Handlers handlers = TransformationContext.getContext().getHandlers();
            final FileResult fileResult = handlers.fileResult(this.templatePath, fullModel);
            context.append(fileResult.toString());

        }
    }
}
