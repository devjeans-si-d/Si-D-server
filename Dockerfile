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

# docker 컨테이너 내에서 밖의 전체 host를 지칭하는 도메인 : host.docker.internal
# 하지만 yml 바꾸지는 않겠다. 왜냐면 그러면 빌드부터 다시해야함. 따라서 실행시점에 주입해줄 것임
# 약속되어있는 패턴이다. 예를들면 SPRING_DATASOURCE_USERNAME도 있음
# docker run -d -p 8081:8080 -e SPRING_DATASOURCE_URL=jdbc:mariadb://host.docker.internal:3306/ordersystem ordersystem:latest

# docker 컨테이너 실행 시에 볼륨을 설정할 때는 -v 옵션 사용
# docker run -d -p 8081:8080 -e SPRING_DATASOURCE_URL=jdbc:mariadb://host.docker.internal:3306/ordersystem -v 호스트경로:컨테이너내부경로 ordersystem:latest
# docker run -d -p 8081:8080 -e SPRING_DATASOURCE_URL=jdbc:mariadb://host.docker.internal:3306/ordersystem -v /Users/sejeong/Desktop/tmp_logs:/app/logs ordersystem:latest

# 도커 허브에서 내려받은 이미지로 실행
# docker run -d -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mariadb://host.docker.internal:3306/ordersystem clean01/ordersystem:latest

