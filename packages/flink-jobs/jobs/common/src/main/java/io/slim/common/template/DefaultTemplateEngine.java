package io.slim.common.template;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;

public class DefaultTemplateEngine implements TemplateEngine {

    @Override
    public String render(String template, Map<String, Object> context) {
        var defaultMapLookup = StringLookupFactory.INSTANCE.mapStringLookup(context);
        var customLookups = new HashMap<String, StringLookup>(); // 다이아몬드 연산자 대신 명시적 타입 지정

        // 커스텀 매크로 등록 (메서드 레퍼런스 적용)
        customLookups.put("format-map", argumentedLookup(context, DefaultTemplateEngine::formatMap));

        var interpolator = StringLookupFactory.INSTANCE.interpolatorStringLookup(
            customLookups, defaultMapLookup, true
        );

        return new StringSubstitutor(interpolator).replace(template);
    }

    // ---------------------------------------------------------
    // [공통 인프라] 파라미터 파싱 및 매크로 생성기
    // ---------------------------------------------------------
    private static StringLookup argumentedLookup(
            Map<String, Object> context, 
            BiFunction<Map<String, String>, Map<String, Object>, String> lookup) {
        return argString -> {
            var args = parseArgs(argString);
            return lookup.apply(args, context);
        };
    }

    private static Map<String, String> parseArgs(String argString) {
        if (argString == null || argString.isBlank()) {
            return Map.of();
        }

        var args = new HashMap<String, String>();
        for (var pair : argString.split(",")) {
            var kv = pair.split("=", 2);
            if (kv.length == 2) {
                args.put(kv[0].trim(), kv[1].trim());
            }
        }
        return args;
    }

    // ---------------------------------------------------------
    // [매크로 구현부] 포맷 매크로
    // ---------------------------------------------------------
    private static String formatMap(Map<String, String> args, Map<String, Object> context) {
        var path = args.get("path");
        if (path == null) {
            throw new IllegalArgumentException("StringLookup 'format-map' requires 'path' arg");
        }
        
        var indentSize = Integer.parseInt(args.getOrDefault("indent", "4"));
        var indentSpaces = " ".repeat(indentSize);
        var prefix = path + ".";

        var joined = context.entrySet().stream()
            .filter(e -> e.getKey().startsWith(prefix))
            .map(e -> String.format("'%s' = '%s'", e.getKey().substring(prefix.length()), e.getValue()))
            .collect(Collectors.joining(",\n" + indentSpaces));

        return joined.isEmpty() ? "" : ",\n" + indentSpaces + joined;
    }

}
