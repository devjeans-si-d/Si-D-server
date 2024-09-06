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

- [Devops Trouble Shooting](#️-devops-trouble-shooting)

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
<summary> 프론트엔드 CI/CD : deploy-with-s3.yml </summary>

```

name: deploy to aws s3
# main 브랜치에 push 될 때 현재 스크립트 실행 트리거 발동
on:
  push:
    branches:
      - main
jobs:
# workflow는 하나 이상의 작업(job)으로 구성. 여기서는 하나의 작업만을 정의
  build-and-deploy:
    runs-on: ubuntu-latest # 우분투 최신 판에서 작업(빌드, 배포 작업 어디서 할건지 지정)
    steps:
    # actions는 깃헙에서 제공되는 공식 워크플로이다.
    # checkout은 현재 repo의 main 브랜치 소스코드를 copy
      - name: source code checkout
        uses: actions/checkout@v2
        # node js 세팅
      - name: setup node.js
        uses: actions/setup-node@v2
        with:
          node-version: '20'
      - name: pnpm install
        working-directory: .
        # run은 직접 사용하고자 하는 명령어이다.
        run: |
          npm install -g pnpm
          pnpm install
      - name: npm build
        env:
          VUE_APP_REST_API_KEY: ${{ secrets.KAKAO_API_KEY }}
          VUE_APP_API_BASE_URL: ${{secrets.SERVER_URL}}
          VUE_APP_MY_URL: ${{secrets.CLIENT_URL}}
        working-directory: .
        run: pnpm run build
      - name: setup aws cli
      # aws 관련한 aws actions가 제공된다. 지금 이게 configure 세팅하는 거임
        uses: aws-actions/configure-aws-credentials@v2
        with:
          aws-access-key-id: ${{secrets.AWS_ACCESS_KEY}}
          aws-secret-access-key: ${{secrets.AWS_SECRET_KEY}}
          aws-region: "ap-northeast-2"
        # 버킷에 소스코드 붓기
      - name: clear s3 bucket
        # 기존 s3 버킷을 비워주기
        run: aws s3 rm s3://www.si-d.site/ --recursive
        
        # S3에 넣기
      - name: deploy to s3
        run: aws s3 cp ./dist s3://www.si-d.site/ --recursive

      # cloud front의 캐시를 지워주는 작업이다.
      - name: invalidate cloudfront caches
        run: aws cloudfront create-invalidation --distribution-id E1O6AN1E7XTVYQ --paths "/*"

```
</details>


<details>
<summary>백엔드 CI/CD : be-cicd.yml</summary>

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
<summary>데브옵스 CI/CD : k8s-cicd.yml</summary>

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
<img src="https://img.shields.io/badge/Spring-green?style=for-the-badge&logo=Spring&logoColor=white"><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"><img src="https://img.shields.io/badge/Springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"><img src="https://img.shields.io/badge/Spring Data JPA -13C100?style=for-the-badge&logo=Spring Boot&logoColor=white"><img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"><img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/mariadb-003545?style=for-the-badge&logo=mariadb&logoColor=white"><img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

### ✔️ Devops
<img src="https://img.shields.io/badge/amazonwebservices-232F3E?style=for-the-badge&logo=amazonwebservices&logoColor=white"><img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"><img src="https://img.shields.io/badge/kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white"><img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"><img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"><img src="https://img.shields.io/badge/amazonroute53-8C4FFF?style=for-the-badge&logo=amazonroute53&logoColor=white"><img src="https://img.shields.io/badge/amazonelasticache-C925D1?style=for-the-badge&logo=amazonelasticache&logoColor=white"><img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"><img src="https://img.shields.io/badge/awselasticloadbalancing-ED1965?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white"><img src="https://img.shields.io/badge/amazoneks-FF9900?style=for-the-badge&logo=amazoneks&logoColor=white"><img src="https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white"><img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"><img src="https://img.shields.io/badge/cloudfront-FF4F8B?style=for-the-badge&logo=amazoncloudwatch&logoColor=white">

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

    <details> <summary><b>프로젝트 지원자 승인하기 시연 영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/52e74684-6ec4-4dac-b918-517dda2bd8fc"/>
        </div>
    </details>

    <details> <summary><b>프로젝트 마감 시 알람수신 시연영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/08e1221b-fe2a-4206-940c-5aa41b4a9b9c"/>
        </div>
    </details>

### 📍 완성된 프로젝트 등록 (Launched Project) 

- 완성된 프로젝트(Launched Project)
    - 프로젝트가 완료되면 PM은 Launched Project글을 작성할 수 있다.
    - 기술스택, 프로젝트 URL, 글쓰기 등록 가능하다.
    - Launched Project 글에는 좋아요(사이다)를 누를 수 있다.
  

    <details> <summary><b>런칭 프로젝트 등록 시연영상</b></summary>
        <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/4709aee2-1c3f-4eba-b8f1-02131d2009ea"/>
        </div>
    </details>

## ☄️ Devops Trouble Shooting
<details> 
<summary><h3> SSE(Server-Sent Events) 미작동 문제 </h3> </summary> 

### 📌 이슈

개발 서버 배포 시 Nginx를 사용한 환경에서 Server-Sent Events(SSE)가 작동하지 않는 문제가 발생했습니다. SSE는 HTTP/1.1 이상에서 지원되지만 Nginx가 HTTP/1.0으로 요청을 프록시 처리하면서 발생한 이슈였습니다.

### 📌 원인

Nginx의 기본 설정에서 HTTP/1.0을 사용하고 있었으며 SSE는 HTTP/1.1 이상의 프로토콜에서만 지원됩니다. 따라서 Nginx에서 HTTP/1.1로의 전환이 필요했습니다.

### 📌 해결 방법

문제를 해결하기 위해 Nginx 대신 **AWS의 Application Load Balancer(ALB)**를 사용하여 로드 밸런싱을 처리했습니다. AWS ALB는 HTTP/1.1을 기본적으로 지원하므로, 별도의 Nginx 설정 없이도 SSE가 정상적으로 동작하였습니다.
</details>

<details> 
<summary><h3> 쿠버네티스 멀티서버 배포 시 SSE 구독 실패 문제 해결 </h3> </summary> 

### 오류 확인
![sse구독실패 문제](https://github.com/user-attachments/assets/7228d6ab-bf7d-42dc-bae6-4172a5469359)

### 해결 시연영상
![redis구독은 방이랑 상관이없다 편집](https://github.com/user-attachments/assets/6a0c1a02-7547-4c01-b1d8-a9b05c55bec9)


### 📌 이슈

쿠버네티스를 사용하여 멀티서버 배포 중 알림이 간헐적으로 전달되지 않는 문제가 발생했습니다. 수천 개의 채팅 메시지를 보내는 상황에서도 알림이 일부는 도착하고, 일부는 도착하지 않는 불안정한 현상이 있었습니다.

### 📌 원인

문제의 원인을 파악하기 위해 Redis에 접속하여 데이터가 제대로 저장되는지 확인한 결과, redis에는 데이터가 제대로 들어왔으나 알림이 발생되지 않는 문제를 발견하였고 코드를 확인해보니 채팅과 알림 RedisMessageListenerContainer가 두 개가 존재하여 중복 구독이 발생할 가능성이 있다고 판단했습니다. 이로 인해 메시지 처리의 일관성이 깨졌을 수 있습니다. 

또한, SSE 구독 요청이 실패할 때 알림이 전송되지 않는 문제도 추가적으로 발견되었습니다. 이는 프론트엔드의 구독 실패로 인한 이슈였습니다.

### 📌 해결 방법

RedisMessageListenerContainer 중복 문제 해결: 두 개의 RedisMessageListenerContainer가 구동 중인 것을 확인한 후, 하나를 삭제하고 관련된 qualifier를 제거하여 재배포했습니다. 이로 인해 중복 문제는 해결되었습니다.

프론트엔드 구독 실패 문제 해결: SSE/subscribe 요청이 실패할 때 알림이 전송되지 않는 문제를 해결하기 위해, try-catch 문을 사용하여 실패 시 재연결 요청을 하도록 프론트엔드 코드를 수정했습니다.
</details>

<details> 
<summary><h3> Spring Scheduler 동시성 문제 해결  </h3> </summary> 

### 📌 이슈

Spring Scheduler 환경에서 여러 Pod 간에 스케줄된 작업이 동시에 실행되어 동시성 문제가 발생했습니다. 이를 방지하기 위해 Redis에 락 키를 저장해봤지만, 동시성 문제는 여전히 해결되지 않았습니다.

### 📌 원인

Redis 락을 사용해도 스케줄 작업 간의 동시성 제어가 제대로 되지 않았던 이유는, Redis가 락을 충분히 빠르게 관리하지 못하거나, 여러 노드 간 동시성 제어에 한계가 있었기 때문입니다. Redis만으로는 여러 Pod 간 잠금을 효율적으로 관리하는 데 어려움이 있었습니다.

### 📌 해결 방법

ShedLock 도입: ShedLock은 여러 노드 또는 Pod에서 동일한 작업이 중복 실행되지 않도록 잠금을 제공합니다. 한 노드에서 잠금을 획득하면, 다른 노드는 동일한 작업을 실행하지 않으며 대기하지 않고 건너뜁니다.

**ShedLock의 특징:**

휘발성 관리: 잠금이 필요한 시간 동안만 유지되며, 작업이 완료되면 잠금이 자동 해제됩니다.
클러스터 환경 지원: 한 노드가 잠금을 획득하면 다른 노드는 해당 잠금이 해제될 때까지 작업을 실행하지 않습니다.
시간 기반 잠금: 노드 시간이 동기화된 환경에서만 제대로 작동합니다.
적용 방법:

@SchedulerLock 어노테이션을 사용하여 스케줄된 메서드에 잠금 로직을 적용.
Lock Provider로 Redis를 사용하여 빠른 실시간 잠금 처리가 가능하게 설정.
적용 환경:

JDK 17 이상 및 Spring 6 이상 환경에서는 ShedLock 5.1.0 버전을 권장.
JDK 17 미만 환경에서는 ShedLock 4.44.0 버전 사용.

</details>

<details> 
<summary><h3> 배포 후 화면 깨짐 현상 </h3> </summary> 
	
### 배포 후 화면 깨짐
<img width="1002" alt="배포 후 화면깨짐" src="https://github.com/user-attachments/assets/d58a81be-0f20-447b-9e12-fc2d6e303f2a">

### 배포 후 화면 수정
<img width="957" alt="배포 후 화면수정" src="https://github.com/user-attachments/assets/bfa809ee-fa77-4581-8bf7-35bfa25fbfc3">

### 📌 이슈

배포 후 프론트엔드 화면이 깨지는 문제가 발생하였습니다. 로컬 환경에서는 정상적으로 표시되었지만, 배포 환경에서는 CSS 우선순위가 달라져 화면이 제대로 렌더링되지 않았습니다.

### 📌 원인

로컬 개발 환경과 배포 환경 간의 차이로 인해 CSS 우선순위가 변경된 것이 문제의 원인이었습니다. 특히, 웹팩(Webpack) 빌드 과정에서 CSS가 예상과 다르게 처리되어, 스타일이 올바르게 적용되지 않았습니다.

### 📌 해결 방법

1. npm run build 명령어로 프로젝트를 빌드한 후, 배포 환경과 동일한 방식으로 로컬 서버에서 애플리케이션을 확인하기 위해 serve -s dist 명령어를 실행하여 배포환경과 동일한 환경을 로컬에서 재현할 수 있었습니다.

2. 로컬 서버에서 배포 환경과 동일하게 확인하며 CSS 스타일 우선순위를 다시 맞추었습니다. 스타일 우선순위 충돌을 해결하고, 화면이 정상적으로 표시되는 것을 확인했습니다.

3. 이후 수정된 CSS를 포함한 코드를 다시 빌드 및 배포하여 문제를 해결했습니다.

</details>






