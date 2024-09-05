# 멀티 스테이지 빌드 방법 사용

### 첫번째 스테이지 => 결과물: .jar 파일 ###
FROM openjdk:11 as stage1
# WORKDIR로 필요한 것들만 카피해주면된다!
WORKDIR /app
# /app/gradlew 파일로 생성
COPY gradlew .
# /app/gradle 폴더로 생성
COPY gradle gradle
COPY src src
COPY build.gradle .
COPY settings.gradle .

# RUN은 도커 컨테이너 안에서 명령어를 날리는 것이다.
# 보통은 그냥 이렇게 실행하면 권한 없다 에러날 수 있음. 따라서 777 권한 주자
RUN chmod 777 ./gradlew
RUN ./gradlew bootJar


### 두번째 스테이지 ###
FROM openjdk:11
WORKDIR /app
# stage 1에서 만든 jar를 stage 2의 app.jar라는 이름으로 copy하겠다.
COPY --from=stage1 /app/build/libs/*.jar app.jar

# CMD 또는 ENTRYPOINT를 통해 컨테이너를 실행한다.
ENTRYPOINT ["java", "-jar", "app.jar"]
