package org.devjeans.sid.domain.launchedProject.entity;

import lombok.*;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.BasicInfoLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.ListLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO.LaunchedProjectScrapResponse;
import org.devjeans.sid.domain.mainPage.dto.TopListLaunchedProjectResponse;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LaunchedProject extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "launched_project_id")
    private Long id; // 프로젝트 전시(Launched-Project) id

    @Column(nullable = false, length = 2083)
    private String launchedProjectImage; // 프로젝트 사진(기본사진 url)

    @Column(nullable = false, length = 5000)
    private String launchedProjectContents; // Launched-Project 글 내용

    private String imageUrl;

    @Column(length = 2083)
    private String siteUrl; // 프로젝트 사이트 링크

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project; // 프로젝트 (project테이블 id FK)

    @Column(columnDefinition = "bigint default 0")
    private Long views; // Launched-Project 조회수

//    orphanRemoval = true : 부모엔티티의 컬렉션에서 자식 엔티티가 제거될 때 자식 엔티티를 삭제해야 하는 경우에 사용

    @OneToMany(mappedBy = "launchedProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LaunchedProjectTechStack> launchedProjectTechStacks; // Launched-Project에 사용된 기술스택 리스트

    @OneToMany(mappedBy = "launchedProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LaunchedProjectScrap> launchedProjectScraps; // Launched-Project 스크랩(사이다) 리스트

    public static TopListLaunchedProjectResponse topListResfromEntity(LaunchedProject launchedProject){
        // 완성된 프로젝트 글 내용 30자까지만 잘라서 출력
        String contents = launchedProject.getLaunchedProjectContents();
        String truncatedContent = contents != null && contents.length() > 30 ? contents.substring(0, 30) : contents;

        // 기술스택명 5개까지만 잘라서 출력
        List<String> techStackNameList = new ArrayList<>();
        String techStackName = "";
        List<LaunchedProjectTechStack> techStackList = launchedProject.getLaunchedProjectTechStacks();

        for(LaunchedProjectTechStack techStack : techStackList){
            techStackName = techStack.getTechStack().getTechStackName();
            techStackNameList.add(techStackName);
        }

        if(techStackNameList.size()>5) techStackNameList = techStackNameList.subList(0,5);

        return TopListLaunchedProjectResponse.builder()
                .id(launchedProject.getId())
                .launchedProjectImage(launchedProject.getLaunchedProjectImage())
                .projectName(launchedProject.getProject().getProjectName())
                .launchedProjectContents(truncatedContent)
                .techStacks(techStackNameList)
                .build();

    }

    // 완성된 프로젝트 이미지 업로드
    public void updateLaunchedProjectImage(String imagePath){
        this.launchedProjectImage = imagePath;
    }

    public void updateLaunchedProjectContents(String launchedProjectContents){
        this.launchedProjectContents = launchedProjectContents;
    }

    public void updateSiteUrl(String siteUrl){
        this.siteUrl = siteUrl;
    }

    // LaunchProject -> DetailBasicLaunchedProjectResponse(DTO)로 build (완성된 프로젝트 기본정보 조회)
    public static BasicInfoLaunchedProjectResponse BasicInfoResfromEntity(LaunchedProject launchedProject){
        return BasicInfoLaunchedProjectResponse.builder()
                .id(launchedProject.getId()) // 완성된 프로젝트 글id
                .launchedProjectImage(launchedProject.getLaunchedProjectImage()) // 프로젝트 사진 url(String)
                .launchedProjectContents(launchedProject.getLaunchedProjectContents()) // 프로젝트 글 내용
                .siteUrl(launchedProject.getSiteUrl()) // 프로젝트 출시 사이트 링크
                .projectId(launchedProject.getProject().getId()) // FK걸린 프로젝트
//                .views(launchedProject.getViews()) // 조회수
                .build();
    }

    // LaunchProject -> ListLaunchedProjectResponse(DTO)로 build (완성된 프로젝트 리스트 response)
    public static ListLaunchedProjectResponse listResFromEntity(LaunchedProject launchedProject){
        // LaunchedProject 글내용 30자까지만 잘라서 list에 출력
        String contents = launchedProject.getLaunchedProjectContents();
        String truncatedContent = contents != null && contents.length() > 30 ? contents.substring(0, 30) : contents;

        return ListLaunchedProjectResponse.builder()
                .id(launchedProject.getId()) // 프로젝트 전시글(Launched-Project) id
                .launchedProjectImage(launchedProject.getLaunchedProjectImage()) // 프로젝트 사진(기본사진 url)
                .projectName(launchedProject.getProject().getProjectName()) // 프로젝트 이름 (LaunchedProject -> Project -> projectName)
                .launchedProjectContents(truncatedContent) // Launched-Project 글 내용 (30자 까지만 잘라서 출력)
                .views(launchedProject.getViews())
                .scrapCount(launchedProject.getLaunchedProjectScraps().size()) // Launched-Project 스크랩 수
                .build();
    }

    // LaunchedProject의 기존TechStack 리스트를 새로운 TechStack 리스트로 업데이트하는 메서드
    public void updateLaunchedProjectTechStacks(List<LaunchedProjectTechStack> newTechStacks) {
        this.launchedProjectTechStacks.clear(); // 기존 기술스택 삭제
        if (newTechStacks != null) {
            this.launchedProjectTechStacks.addAll(newTechStacks); // 새 기술스택 추가z
        }
    }

}
