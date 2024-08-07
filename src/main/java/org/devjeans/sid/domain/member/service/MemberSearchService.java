package org.devjeans.sid.domain.member.service;

import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.dto.SearchMemberRequest;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
@Service
public class MemberSearchService {
    private final MemberSearchRepository memberSearchRepository;
    @Autowired
    public MemberSearchService(MemberSearchRepository memberSearchRepository) {
        this.memberSearchRepository = memberSearchRepository;
    }

    public Page<MemberInfoResponse> memberList(SearchMemberRequest searchMemberRequest, Pageable pageable){
        Specification<Member> specification = new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(searchMemberRequest.getEmail()!=null){
                    predicates.add(criteriaBuilder.like(root.get("email"), "%"+searchMemberRequest.getEmail()+"%"));
                }
                if(searchMemberRequest.getName()!=null){
                    predicates.add(criteriaBuilder.like(root.get("name"), "%"+searchMemberRequest.getName()+"%"));
                }
                if(searchMemberRequest.getNickname()!=null){
                    predicates.add(criteriaBuilder.like(root.get("nickname"), "%"+searchMemberRequest.getNickname()+"%"));
                }
                Predicate[] predicateArr = new Predicate[predicates.size()];
                for(int i=0;i<predicateArr.length;i++){
                    predicateArr[i]=predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
        Page<Member> members = memberSearchRepository.findAll(specification, pageable);
        return members.map(MemberInfoResponse::fromEntity);
    }
}
