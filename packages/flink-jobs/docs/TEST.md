# JUnit 5 의존성 가이드 및 에러 로그 분석

## 1. 발생한 의존성 에러 원인 및 해결책
* **현상**: `:jobs:common:build` 수행 중 `org.junit.platform:junit-platform-launcher:5.14.4` 아티팩트를 찾을 수 없어 빌드 실패.
* **원인**: JUnit Jupiter의 버전(`5.14.4`)을 JUnit Platform 그룹의 모듈(`junit-platform-launcher`)에 그대로 대입하여 발생 (JUnit Platform에는 `5.x.x` 버전이 없음).
* **해결책**:
  * JUnit Jupiter `5.14.4`에 매칭되는 JUnit Platform 버전은 **`1.14.4`**임.
  * `starters/flink-dependencies/build.gradle.kts`에서 플랫폼 버전을 별도 변수(`1.14.4`)로 선언하고 `junit-platform-launcher`에 적용해야 함.

---

## 2. JUnit 5 구성 모듈 및 역할

JUnit 5는 **Platform(실행 환경)**과 **Jupiter(엔진/API)** 영역이 분리되어 있습니다.

| 모듈명 | 구분 | 핵심 역할 |
| :--- | :--- | :--- |
| **`junit-jupiter-api`** | 컴파일 타임 | 개발자가 테스트 코드를 작성할 때 쓰는 도구 (`@Test`, Assertions 등) |
| **`junit-jupiter-engine`** | 런타임 | 작성된 Jupiter 테스트 코드를 실제로 실행하는 엔진 구현체 |
| **`junit-platform-launcher`** | 런타임 | 빌드 도구(Gradle 등)나 IDE가 테스트 엔진을 스캔하고 구동하는 진입점 API |
| **`junit-jupiter`** | 애그리게이터 | `api` + `engine` + `params` 모듈을 하나로 묶은 패키지 상품 |

---

## 3. JUnit 5 최소 구성 의존성 가이드

### 일반적인 프로젝트 (최소 구성)
IDE와 빌드 도구가 내장 런처를 제공하므로 **애그리게이터 단 한 줄**만 추가하면 됩니다.
```kotlin
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}
tasks.test {
    useJUnitPlatform()
}
```

### 멀티모듈 / 특수 빌드 환경 (현재 프로젝트 포함)
빌드 도구와의 엄격한 버전 호환성 및 런타임 안정성을 위해 **플랫폼 런처를 추가로 요구**합니다.
```kotlin
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.14.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.14.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.4") // 버전 1.x.x 주의
}
```
