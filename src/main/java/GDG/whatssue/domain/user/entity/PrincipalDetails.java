//package GDG.whatssue.domain.user.entity;
//
//import GDG.whatssue.domain.member.entity.ClubMember;
//import GDG.whatssue.domain.user.entity.User;
//import GDG.whatssue.domain.member.entity.Role;
//import jakarta.transaction.Transactional;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
///*
//* 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//* 로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다. (Security ContextHolder)
//* 오브젝트 => Authentication 타입 객체
//* Authentication 안에 User 정보가 있어야 됨.
//* User 오브젝트 타입 => UserDetails 타입 객체
//*
//* Security Session => Authentication => UserDetails(PrincipalDetails)
// */
//@Getter
//@Transactional
//public class PrincipalDetails implements UserDetails {
//
//    private User user;
//
//    public PrincipalDetails(User user) {
//        this.user = user;
//        // 추가: 세션을 열고 연관된 엔티티를 즉시 로딩
//        this.user.getClubMemberList().size();
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<ClubMember> clubMemberList = user.getClubMemberList();
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        for (ClubMember clubMember : clubMemberList) {
//            authorities.add((GrantedAuthority) () -> {
//                Long clubId = clubMember.getClub().getId();
//                Role role = clubMember.getRole();
//                System.out.println("ROLE_" + clubId + role);
//                return "ROLE_" + clubId + role;
//            });
//        }
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUserName();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        // 우리 사이트 1년동안 회원이 로그인을 안하면 휴먼 계정으로 하기로 함.
//        // 현재시간 - 로긴시간 => 1년을 초과하면 return false;
//        return true;
//    }
//}
