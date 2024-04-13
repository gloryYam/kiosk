package sample.cafekiosk.spring.api.service.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import sample.cafekiosk.spring.aplication.event.MemberLeaveEvent;
import sample.cafekiosk.spring.domain.jwt.TokenRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenEventListener {

    private final TokenRepository tokenRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void deleteToken(final MemberLeaveEvent memberLeaveEvent) {
        Long memberId = memberLeaveEvent.memberId();
        tokenRepository.deleteByMemberId(memberId);

        log.info("토큰 삭제 By 사용자 회원 탈퇴 이벤트 Listen - 회원 ID : {}", memberId);
    }
}