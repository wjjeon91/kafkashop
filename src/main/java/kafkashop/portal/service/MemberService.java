package kafkashop.portal.service;

import kafkashop.portal.domain.Member;
import kafkashop.portal.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long signup(Member member){

        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        memberRepository.findByName(member.getName()).ifPresent(m->{throw new IllegalStateException("이미 존재하는 회원입니다.");});
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

    public Member findByName(String name){
        Member member = memberRepository.findByName(name).orElseThrow(()->{throw new IllegalStateException("회원 정보 없습니다.");});
        return member;
    }
}