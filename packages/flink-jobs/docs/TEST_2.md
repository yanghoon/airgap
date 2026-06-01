# 멀티 모듈 테스트 의존성 트러블슈팅 및 Gradle 전파 규칙 요약

## 1. 빌드 에러 원인 및 해결 (flink-test 의존성)
* **컴파일 에러 (`package does not exist`)**
  * **원인**: `flink-test` 모듈이 JUnit을 `implementation`으로 선언하여 Consumer(`jobs:common`)의 컴파일 클래스패스에 노출되지 않음.
  * **해결**: `implementation`을 `api`로 변경하여 트랜지티브하게 노출.
* **테스트 실행 에러 (`OutputDirectoryCreator not available`)**
  * **원인**: 명시된 JUnit 버전(5.14.4)과 Gradle 8.5 내부 테스트 엔진 간의 호환성 문제(버전 불일치).
  * **해결**: JUnit 버전을 `5.10.2`로 낮추고, `junit-platform-launcher` 버전을 `1.10.2`로 맞추어 호환성 정렬.

## 2. 테스트 스코프 격리 및 전파 규칙
* **`testImplementation`의 격리성**
  * Consumer가 의존성을 `testImplementation`으로 선언하면, Provider의 의존성들은 Consumer의 메인 프로덕션 환경(`main`)에 섞이지 않고 **테스트 환경(`test`)에만 격리**되어 추가됨.
* **`test...` 스코프 전파 불가**
  * Provider(`flink-test`) 내부에서 `testImplementation`, `testRuntimeOnly` 등으로 선언한 의존성은 **외부 모듈로 절대 전파되지 않음**.
* **테스트 스타터의 런타임 의존성 전파 방법**
  * `flink-test`처럼 외부 프로젝트의 테스트 환경 구성을 돕는 모듈은 Consumer의 런타임에 필요한 파일(`junit-platform-launcher` 등)을 `testRuntimeOnly`가 아닌 **`runtimeOnly`**로 선언해야 정상적으로 전파됨.

## 3. 클래스패스 자동 매핑 규칙
Consumer가 Provider를 **`testImplementation(project(":provider"))`** 로 가져올 때의 자동 매핑 결과:
* Provider의 **`api`** ➡️ Consumer의 **`testCompileClasspath`** & **`testRuntimeClasspath`**
* Provider의 **`implementation`** ➡️ Consumer의 **`testRuntimeClasspath`**
* Provider의 **`runtimeOnly`** ➡️ Consumer의 **`testRuntimeClasspath`**

> **참고**: 의존성이 Consumer의 선언용 스코프(예: `testRuntimeOnly`)에 문자 그대로 복사되는 것이 아니라, 최종적으로 컴파일/실행 시 해소되는 **'결과물 클래스패스(Resolvable Configuration)'**로 자동 매핑됨.
