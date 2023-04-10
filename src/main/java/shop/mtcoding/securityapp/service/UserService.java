package shop.mtcoding.securityapp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.securityapp.dto.UserRequest;
import shop.mtcoding.securityapp.model.User;
import shop.mtcoding.securityapp.model.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /*
     * 서비스의 책임
     * 1. 트랜잭션 관리
     * 2. 영속성 객체 변경 감지
     * 3. RequestDTO 요청 받기
     * 4. 비지니스 로직 처리하기
     * 5. ResponseDTO 응답하기
     */
    @Transactional
    public User 회원가입(UserRequest.JoinDTO joinDTO) {
        String rawPassword = joinDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword); // 60Byte로 암호화
        joinDTO.setPassword(encPassword);
        return userRepository.save(joinDTO.toEntity());
    }
}
