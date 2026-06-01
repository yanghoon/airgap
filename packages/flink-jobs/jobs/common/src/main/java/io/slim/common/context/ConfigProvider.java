package io.slim.common.context;

import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * SmallRye Config를 단독으로 사용하는 환경에서 빌더 설정을 캡슐화하고, 
 * 여러 Mapping 인터페이스를 동적으로 로드할 수 있도록 제공하는 유틸리티 클래스입니다.
 */
public class ConfigProvider {
    
    private static SmallRyeConfig config;
    private static final Set<Class<?>> mappings = new HashSet<>();

    private ConfigProvider() {
        // 유틸리티 클래스 인스턴스화 방지
    }

    /**
     * 지정된 매핑 클래스에 대한 설정을 반환합니다.
     * 호출 시마다 기존에 등록된 매핑 클래스들을 포함하여 SmallRye Config를 구성합니다.
     * 이를 통해 다른 어플리케이션에서 단 1줄의 코드로 설정을 초기화하고 가져올 수 있습니다.
     * 
     * @param mappingClass @ConfigMapping이 선언된 인터페이스
     * @return 매핑된 설정 인스턴스
     */
    public static synchronized <T> T getConfigMapping(Class<T> mappingClass) {
        if (!mappings.contains(mappingClass) || config == null) {
            mappings.add(mappingClass);
            
            SmallRyeConfigBuilder builder = new SmallRyeConfigBuilder()
                .addDefaultInterceptors()
                .addDefaultSources()
                .addDiscoveredSources()
                .addDiscoveredInterceptors();
                
            for (Class<?> clazz : mappings) {
                builder.withMapping(clazz);
            }
            
            config = builder.build();
        }
        
        return config.getConfigMapping(mappingClass);
    }

}
