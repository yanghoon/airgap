package io.slim.common.template;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public interface TemplateEngine {

    static String render(String template, Map<String, Object> context) {
        StringLookup defaultMapLookup = StringLookupFactory.INSTANCE.mapStringLookup(context);
        Map<String, StringLookup> customLookups = new HashMap<>();

        // 커스텀 매크로 등록: ${format-map:path=..., indent=4}
        customLookups.put("format-map", buildMacro(context, (args, ctx) -> {
            String path = args.get("path");
            if (path == null) throw new IllegalArgumentException("Macro 'format-map' requires 'path' arg");
            
            int indentSize = Integer.parseInt(args.getOrDefault("indent", "4"));
            String indentSpaces = " ".repeat(indentSize);
            String prefix = path + ".";

            String joined = ctx.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .map(e -> String.format("'%s' = '%s'", e.getKey().substring(prefix.length()), e.getValue()))
                .collect(Collectors.joining(",\n" + indentSpaces));

            return joined.isEmpty() ? "" : ",\n" + indentSpaces + joined;
        }));

        StringLookup interpolator = StringLookupFactory.INSTANCE.interpolatorStringLookup(
            customLookups, defaultMapLookup, true
        );

        return new StringSubstitutor(interpolator).replace(template);
    }

    @FunctionalInterface
    interface MacroFunction {
        String execute(Map<String, String> args, Map<String, Object> context);
    }

    static StringLookup buildMacro(Map<String, Object> context, MacroFunction macro) {
        return argString -> {
            Map<String, String> args = new HashMap<>();
            if (argString != null && !argString.isBlank()) {
                for (String pair : argString.split(",")) {
                    String[] kv = pair.split("=", 2);
                    if (kv.length == 2) args.put(kv[0].trim(), kv[1].trim());
                }
            }
            return macro.execute(args, context);
        };
    }
}
