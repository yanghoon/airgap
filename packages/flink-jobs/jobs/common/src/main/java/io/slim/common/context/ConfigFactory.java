package io.slim.common.context;

import java.util.Arrays;

import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;

/**
 * SmallRye Config를 단독으로 사용하는 환경에서 빌더 설정을 캡슐화하고, 
 * 여러 Mapping 인터페이스를 동적으로 로드할 수 있도록 제공하는 유틸리티 클래스입니다.
 */
public class ConfigFactory {
    
    private ConfigFactory() {}

    /**
     * 기본 설정 소스와 사용자가 제공한 ConfigMapping 클래스를 조립하여
     * 독립적인 SmallRyeConfig 인스턴스를 생성합니다.
     */
    public static SmallRyeConfig create(Class<?>... mappingClasses) {
        SmallRyeConfigBuilder builder = new SmallRyeConfigBuilder()
                .addDefaultSources()         // 시스템 프로퍼티, 환경 변수 등
                .addDefaultInterceptors()    // 프로필(dev, prod), 표현식 해석 등
                .addDiscoveredSources()      // application.properties 등
                .addDiscoveredConverters();  // 타입 변환기

        Arrays.stream(mappingClasses).forEach(builder::withMapping);

        return builder.build();
    }

}
