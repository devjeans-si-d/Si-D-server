## 팀원
<div align="center">
	<table>
	<tr>
	<td><img width="300" alt="세호" src="https://avatars.githubusercontent.com/u/96410921?v=4"></td>
	<td><img width="300" alt="슬기" src="https://avatars.githubusercontent.com/u/135789383?v=)"> </td>
	    <td><img width="300" alt="우진" src="https://avatars.githubusercontent.com/u/126751594?v=4">
	    <td><img width="300" alt="세정" src="https://avatars.githubusercontent.com/u/64718002?v=4"></td>
	</tr>
		<tr>
   <th><a href="https://github.com/sseho"> 최세호</a> </th>
	<th><a href="https://github.com/wisdom0405"> 정슬기 </a></th>
  <th><a href="https://github.com/getsetgo1"> 박우진</a> </th>
  <th><a href="https://github.com/clean2001"> 김세정</a> </th>
	  </tr>
<th> BE </th>
<th> BE </th>
<th> BE </th>
<th> BE, 팀장 </th>
	</table>
</div>

## Commit Convention
유다시티 컨벤션
```
feat: 새로운 기능 구현
fix: 버그, 오류 해결
docs: README나 WIKI 등의 문서 작업
style: 코드가 아닌 스타일 변경을 하는 경우
refactor: 리팩토링 작업
test: 테스트 코드 추가, 테스트 코드 리팩토링
chore: 코드 수정, 내부 파일 수정
```


## 목차

- [주요기능](#주요-기능)

- [프로젝트 소개: Si-D란?](#si-d-소개합니다)

- [요구사항정의](#요구사항정의)

- [ERD](#erd)

- [기술스택](#기술스택)

- [CI/CD 아키텍처 설계서](#CI/CD-아키텍처-설계서)

- [CI/CD를 위한 구성 스크립트](#CI/CD를-위한-구성-스크립트)

- [테스트 결과서](#테스트결과서)

- [협업관리](#️-협업-관리)

- [기능 및 시연영상](#기능)

## 주요 기능

- [소셜로그인, 회원정보 입력](#-회원가입--카카오-소셜로그인-si-der-card-등록)

- [Team-Building: 프로젝트 모집](#-프로젝트-모집team-building)

    -[프로젝트 지원](#-프로젝트-모집공고-지원-팀원)

    -[프로젝트 관리](#-프로젝트-관리-pm)

- [Launched-Project: 완성된 프로젝트 자랑하기](#-완성된-프로젝트-등록-launched-project)



## Si-D🍾 소개합니다 
<p align="center">
  <img src="https://github.com/user-attachments/assets/17ea70cd-77cb-4eb4-a6b0-16b2f4853ed0" alt="로고" width="400" />
</p>


Designer와 Developer를 이어주는 사이드(Side) 프로젝트 플랫폼

사이드 프로젝트를 구하기 힘들었던 사람들을 위하여 만들어진 사이트입니다. 

자신이 원하는 주제를 선택하여 프로젝트를 모집하거나 지원하여 다양한 프로젝트로 포트폴리오를 채워나갈 수 있는 서비스를 제공하고자 합니다.


<table>
  <thead>
    <tr>
      <th>기능</th>
      <th>설명</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>프로젝트 모집</td>
      <td>프로젝트 모집글을 등록 및 수정할 수 있습니다. 등록된 프로젝트 모집글을 통해 지원 및 채팅을 할 수 있습니다. 조회수 및 스크랩 순으로 정렬 및 각 분야별로 필터링하여 전체 조회할 수 있습니다.</td>
    </tr>
    <tr>
      <td>프로젝트 관리</td>
      <td>PM이 등록한 모집 프로젝트 또는 내가 참여한 모집 프로젝트를 확인하고 관리할 수 있습니다. 지원을 확인하고 채팅으로 연락하며 관리합니다.</td>
    </tr>
    <tr>
      <td>완성된 프로젝트</td>
      <td>모집 프로젝트를 기반으로 완성된 프로젝트를 등록 및 수정할 수 있습니다. 완성된 프로젝트는 사이트 URL로 결과물을 보여줄 수 있습니다.</td>
    </tr>
    <tr>
      <td>사이더카드</td>
      <td>각 회원의 사이더카드를 통해 관심 직무 분야, 프로젝트 경험, 커리어를 등록 및 수정합니다. 프로젝트 모집글 및 완성된 프로젝트에서 해당되는 회원의 사이더카드를 조회할 수 있습니다.</td>
    </tr>
  </tbody>
</table>

## 요구사항정의
[요구사항 정의 보기](https://www.notion.so/af1a856a1abc4164a9efece3bf72cc57?v=8e30938bc7664893bed667c194fde673&pvs=4)

## ERD
<img width="542" alt="스크린샷 2024-08-20 오후 9 14 53" src="https://github.com/user-attachments/assets/d4e3365b-ac70-4ab8-93aa-5515a9954248">

## CI/CD 아키텍처 설계서

### 초기 배포 아키텍처
<img width="1113" alt="데브서버1" src="https://github.com/user-attachments/assets/de56bf4a-c20b-4d9f-9b64-b6ef759d3179">

### 최종 배포 아키텍처
<img width="756" alt="아키텍처3" src="https://github.com/user-attachments/assets/4cd6c8b6-8f35-4169-a328-969c9d7b0c6b">

## CI/CD를 위한 구성 스크립트
<details>
<summary>be-cicd.yml</summary>

```

name: deploy to ec2 with docker
on:
  push:
    branches:
      - dev
jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - name: checkout-branch
        uses: actions/checkout@v2

      - name: build image
        working-directory: .
        run: docker build -t clean01/sid:latest .

      - name: docker hub login
        uses: docker/login-action@v1
        with:
          username: ${{secrets.DOCKER_EMAIL}}
          password: ${{secrets.DOCKER_PASSWORD}}

      - name: push to dockerhub
        run: docker push clean01/sid:latest

      - name: ec2 ssh login and docker compose update
        uses: appleboy/ssh-action@master
        with:
          host: ec2-3-36-130-110.ap-northeast-2.compute.amazonaws.com
          username: ubuntu
          key: ${{secrets.EC2_PEMKEY}}
          script: |
            if ! type docker > /dev/null ; then
              sudo snap install docker || echo "docker install failed!"
            fi
            sudo docker login --username ${{secrets.DOCKER_EMAIL}} --password ${{secrets.DOCKER_PASSWORD}}
            sudo docker-compose pull && sudo docker-compose up -d
            
            # Remove old and unused Docker images
            sudo docker image prune -f

```
</details>

<details>
<summary>k8s-cicd.yml</summary>

```

# docker build 후 ecr 업로드 및 kubectl apply 시켜주기
name: deploy sid with k8s
on:
  push:
    branches:
      - main
jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - name: checkout github
        uses: actions/checkout@v2

      - name: install kubectl # 가상 컴퓨터에 kubectl 설치
        uses: azure/setup-kubectl@v3
        with:
          version: "v1.25.9"
        id: install

      - name: configure aws # aws configure 해서 key값 세팅하는 부분
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{secrets.AWS_KEY}}
          aws-secret-access-key: ${{secrets.AWS_SECRET}}
          aws-region: ap-northeast-2

      - name: update cluster information
        run: aws eks update-kubeconfig --name 6team-cluster --region ap-northeast-2 # 원래는 6team-cluster

      - name: login ECR
        id: login-ecr
        uses: aws-actions/amazon-ecr-login@v1

      # 이곳에서 가장 빈번한 변경이 일어남(이미지)
      - name: build and push docker images to ecr
        env: # 변수를 지정하는 부분
          REGISTRY: 346903264902.dkr.ecr.ap-northeast-2.amazonaws.com
          REPOSITORY: devjeans-sid # AWS ecr의 프라이빗 리포지토리 이름을 의미
        run: |
          docker build -t $REGISTRY/$REPOSITORY:latest -f ./Dockerfile .
          docker push $REGISTRY/$REPOSITORY:latest
      # deployment가 변경되면 반영하는 부분
      - name: eks kubectl apply
        run: |
          kubectl apply -f ./k8s/sid_depl.yml
          kubectl rollout restart deployment sid-deployment

```
</details>

<details>
<summary>Dockerfile</summary>

```

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


```
</details>

## 테스트 결과서

### 📍카카오 소셜 로그인 및 나의 사이더카드 조회

  <details>
      <summary><b>카카오 소셜 로그인 및 나의 사이더카드 조회</b></summary>
      <img src="https://github.com/user-attachments/assets/a6ecb38f-2378-4731-a22a-77aaf5d91e0b" alt="카카오 소셜 로그인 및 나의 사이더카드 조회"/>
  </details>
  <br/>

### 📍프로젝트 모집 공고 등록
  <details>
      <summary><b>프로젝트 모집 공고 등록</b></summary>
      <img src="https://github.com/user-attachments/assets/213bc96c-e95f-4489-9a81-2bedb4aeb905" alt="프로젝트 모집 공고 등록"/>
  </details>
  <br/>

### 📍프로젝트 모집 공고 지원
  <details>
      <summary><b>PM에게 문의채팅</b></summary>
      <img src="https://github.com/user-attachments/assets/29d242b0-6328-4881-afcb-1c4c4b71dc92" alt="PM에게 문의채팅"/>
  </details>
  <br/>
  <details>
      <summary><b>PM 답변 채팅</b></summary>
      <img src="https://github.com/user-attachments/assets/069e19a4-9e84-4fb3-960f-3ab8d3116132" alt="PM 답변 채팅"/>
  </details>
  <br/>
  <details>
      <summary><b>프로젝트 지원</b></summary>
      <img src="https://github.com/user-attachments/assets/0431b94f-30cb-4617-89e1-36acad830b6d" alt="프로젝트 지원"/>
  </details>
  <br/>

### 📍프로젝트 관리 (PM)
  <details>
      <summary><b>프로젝트 지원자 승인</b></summary>
      <img src="https://github.com/user-attachments/assets/eb6fb10e-cef6-4f63-948c-03b2e7a625e0" alt="프로젝트 지원자 승인"/>
  </details>
  <br/>
    <details>
      <summary><b>프로젝트 마감 시 알람</b></summary>
      <img src="https://github.com/user-attachments/assets/d9d955a2-b493-4541-b93d-1903c91dd396" alt="프로젝트 마감 시 알람"/>
  </details>
  <br/>

### 📍완성된 프로젝트 등록
  <details>
      <summary><b>완성된 프로젝트 등록</b></summary>
      <img src="https://github.com/user-attachments/assets/e6f8601e-ad6d-4e65-af52-d559a107562a" alt="완성된 프로젝트 등록"/>
  </details>
  <br/>

## 📍이슈 관리
[이슈 관리 보기](https://quark-smile-890.notion.site/01f6e9a772864d789a2aa5f35798e92b?v=8288992a047b499f853c24bfc5f2c1cd&pvs=4)


## 기술스택
### ✔️Frond-end
<img src="https://img.shields.io/badge/Vue.js-4FC08D?style=for-the-badge&logo=Vue.js&logoColor=white" ><img src="https://img.shields.io/badge/Vuetify-1867C0?style=for-the-badge&logo=vuetify&logoColor=#1867C0" ><img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" >

### ✔️Back-end
<img src="https://img.shields.io/badge/Spring-green?style=for-the-badge&logo=Spring&logoColor=white"><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"><img src="https://img.shields.io/badge/Sspringsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"><img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"><img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/mariadb-003545?style=for-the-badge&logo=mariadb&logoColor=white"><img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

### ✔️ 협업 관리
<img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white"><img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

## 기능

 ### 📍 회원가입 : 카카오 소셜로그인, Si-der Card 등록

- OAuth 카카오 소셜로그인으로 회원가입 구현
- 회원가입 후 SiderCard(프로필) 업데이트 가능
    - 프로필 사진, 직무, 자기소개, 재직정보, 사용가능 기술스택 선택가능
    - 이후 프로젝트 참여 시 참여했던 프로젝트 정보(Launched Project) 까지 자동으로 추가됨 
- 다른 회원들의 SiderCard 조회 가능하다
- 해당 회원이 진행한 프로젝트도 조회 가능하다.


    <details> <summary><b>카카오 로그인 시연영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/c0825004-48f7-43b0-929d-c921ef0c342a"/>
        </div>
    </details>

    <details> <summary><b>사이더 카드 등록 시연영상1</b></summary
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/e2346dad-dfbb-4d81-a5b4-39bfe162bc9c"/>
        </div>
    </details>

    <details> <summary><b>사이더 카드 등록 시연영상2</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/0430e96e-5651-4323-8cc0-d78b01741aef"/>
        </div>
    </details>

    <details> <summary><b>사이더 카드 목록 조회 시연영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/9db28996-a023-4638-ad3a-68e4dcf6e91a"/>
        </div>
    </details>

### 📍 프로젝트 모집(Team-Building)

- 프로젝트 등록 (PM)
    - 프로젝트를 등록한 사람이 자동으로 PM이 된다.
    - 프로젝트 사진, 프로젝트 글, 모집마감 기한, 모집정보(직무, 필요인원) 등록 가능
    - 모집기한이 만료되면 스케쥴러에 의해 자동으로 마감처리된다.
      

    <details> <summary><b>프로젝트 모집글 등록 시연영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/7cf6db13-dc39-40e9-886d-c336d538c61e"/>
        </div>
    </details>

### 📍 프로젝트 모집공고 지원 (팀원)

- 프로젝트 모집공고 지원 (팀원)
    - 지원자는 'PM과의 채팅'을 통해 문의채팅이 가능하다.
    - 지원자는 '프로젝트 지원'을 통해 공고에 지원이 가능하다
    - Sider Card에 등록한 직무와 상관없이 직무는 자유롭게 선택이 가능하다
    - 프로젝트 지원 내역은 '마이페이지 > 신청내역'에서 확인 가능하다
      

    <details> <summary><b>프로젝트 지원하기 시연 영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/2fc988d1-19f8-44de-bbb2-6dafa98c73e5"/>
        </div>
    </details>

    <details> <summary><b>PM에게 문의채팅 시연영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/a13f56d2-e755-445c-9e23-7f9283d78ce9"/>
        </div>
    </details>

    <details> <summary><b>채팅알림</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/043207e3-0486-491c-9dd6-940ce1a3f13a"/>
        </div>
    </details>

    <details> <summary><b>PM이 답변채팅 시연영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/a37e732e-17f7-45b9-b6b3-7b2b0e92e6f2"/>
        </div>
    </details>


### 📍 프로젝트 관리 (PM)

- 프로젝트 관리
    - 프로젝트는 수동으로 마감이 가능하다. 
    - '프로젝트 관리'에서 지원자 조회가 가능하다.
    - 지원자를 승인하고 프로젝트에 초대하면 지원자에게 승인안내 메일이 전송된다.
    - 프로젝트가 마감되면 프로젝트 참여자에게 프로젝트 모집이 종료되었다는 알림이 간다.
    - 

    <details> <summary><b>프로젝트 지원자 승인하기 시연 영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/52e74684-6ec4-4dac-b918-517dda2bd8fc"/>
        </div>
    </details>

    <details> <summary><b>>프로젝트 마감 시 알람수신 시연영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/08e1221b-fe2a-4206-940c-5aa41b4a9b9c"/>
        </div>
    </details>

### 📍 완성된 프로젝트 등록 (Launched Project) 

- 완성된 프로젝트(Launched Project)
    - 프로젝트가 완료되면 PM은 Launched Project글을 작성할 수 있다.
    - 기술스택, 프로젝트 URL, 글쓰기 등록 가능하다.
    - Launched Project 글에는 좋아요(사이다)를 누를 수 있다.
  

    <details> <summary><b>>런칭 프로젝트 등록 시연영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/4709aee2-1c3f-4eba-b8f1-02131d2009ea"/>
        </div>
    </details>








