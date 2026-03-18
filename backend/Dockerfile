# 1단계: 빌드 스테이지 (Build Stage)
# Gradle과 JDK가 설치된 이미지를 사용하여 빌드를 수행합니다.
FROM gradle:8.0-jdk17 AS build
WORKDIR /app

# 소스 코드를 복사합니다.
COPY . .

# Gradle 빌드를 실행하여 JAR 파일을 생성합니다.
# --no-daemon 옵션은 CI 환경에서 메모리 효율을 위해 사용합니다.
RUN ./gradlew bootJar --no-daemon

# 2단계: 실행 스테이지 (Run Stage)
# 실행에는 가벼운 JRE(또는 JDK alpine) 이미지면 충분합니다.
FROM amazoncorretto:17-alpine
WORKDIR /app

# 1단계(build)에서 생성된 JAR 파일만 현재 컨테이너로 복사합니다.
# 빌드 경로가 /app/build/libs/임을 확인하세요.
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]