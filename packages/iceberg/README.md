# [Tech Spec] Zarf 기반 Iceberg REST Catalog 스택 자동화 설계

**Status:** Draft
**Author:** AI Assistant & User
**Last Updated:** 2026-04-18

## 1. Context (배경)
로컬 개발 환경(Kind) 및 폐쇄망(Air-gapped) 가능성을 고려한 데이터 레이크하우스 카탈로그 환경 구축이 필요합니다. 특히, 인프라 구성의 유연성을 위해 스토리지(MinIO) 설치 여부를 선택할 수 있어야 하며, 패키지 관리 도구인 **Zarf**를 통해 일관된 배포 경험을 제공하고자 합니다.

## 2. Goals (목표)
* **Kubernetes 1.34.4(Kind)** 환경에서 동작하는 데이터 플랫폼 스택 구축.
* **오프라인 배포 지원:** PostgreSQL 및 Iceberg REST 이미지를 Zarf 패키지에 내장.
* **유연한 스토리지 연결:** 내장 MinIO 또는 외부 S3(AWS 등)를 배포 시점에 선택 및 설정.
* **IaC 기반 관리:** Helm 및 K8s Manifest를 Zarf로 오케스트레이션.

## 3. Technical Decisions (기술적 결정)

### 3.1 Zarf를 활용한 하이브리드 패키징
* **Core(PG, Iceberg):** 폐쇄망 환경을 고려하여 `images` 섹션에 명시하여 패키지에 포함.
* **Optional(MinIO):** 패키지 용량 최적화를 위해 `images`를 제외하고, 인터넷이 연결된 환경에서 실시간으로 Pull 하도록 구성.

### 3.2 변수 주입 전략 (`###ZARF_VAR_...###`)
* S3 접속 정보 및 DB 계정 정보를 Zarf Variables로 관리하여, `zarf package deploy` 시점에 프롬프트를 통해 사용자 입력을 수용합니다.

### 3.3 Iceberg Catalog 연동
* JDBC(PostgreSQL) 기반의 Catalog 구현체를 사용하며, S3 FileIO를 통해 데이터 실제 경로(Warehouse)를 관리합니다.

## 4. 최종 산출물 파일 목록 (Artifacts)

경로,파일명,설명
/,zarf.yaml,메인 패키지 정의 및 외부 설정 파일 참조
./values/,values-postgresql.yaml,PostgreSQL 전용 Helm Values 설정
./values/,values-minio.yaml,MinIO 전용 Helm Values 설정
./manifests/,iceberg-rest.yaml,Iceberg REST Server K8s Manifest (기존 유지)

## 5. Operation (운영 가이드)

### 5.1 패키지 생성 (Create)
```bash
# 인터넷이 연결된 곳에서 실행
zarf package create . --confirm
```

### 5.2 패키지 배포 (Deploy)
```bash
# 클러스터 접근이 가능한 곳에서 실행
zarf package deploy zarf-package-iceberg-stack-*.tar.zst
```
* 배포 과정에서 `minio` 컴포넌트 설치 여부를 묻습니다. 
* 외부 S3를 사용한다면 `minio`를 `N`으로 선택하고, `ICEBERG_S3_ENDPOINT` 등에 외부 주소를 입력하십시오.

https://gemini.google.com/share/53c37f94dc28
