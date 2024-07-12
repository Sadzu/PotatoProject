package ru.cft.template.core.service.session;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.session.SessionDto;
import ru.cft.template.api.dto.session.SessionRequest;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Session;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.mapper.SessionMapper;
import ru.cft.template.core.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final AuthenticationManager authenticationManager;

    @Nullable
    @Override
    public Session getValidSession(String token) {
        Session session = sessionRepository.findByValue(token);
        if (session != null && session.getExpirationTime().isBefore(LocalDateTime.now())) {
            invalidate(session);
            return null;
        }
        return session;
    }

    @Override
    public List<SessionDto> getAllActive() {
        User user = Context.get().getUser();
        List<Session> sessions = sessionRepository
                .findAllByUserAndActiveTrueAndExpirationTimeAfter(user, LocalDateTime.now());
        return sessionMapper.map(sessions);
    }

    @Override
    public SessionDto getCurrent() {
        return sessionMapper.map(Context.get().getSession());
    }

    @Override
    public SessionDto create(SessionRequest sessionRequest) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(sessionRequest.email(), sessionRequest.password()));

        Session session = buildNewSession((User) authentication.getPrincipal());
        sessionRepository.save(session);
        return sessionMapper.map(session);
    }

    @Override
    public void removeCurrent() {
        Session session = Context.get().getSession();
        invalidate(session);
    }

    @Override
    public void invalidateExpiredSessions() {
        sessionRepository.updateAllByExpirationTimeAfter(LocalDateTime.now());
    }

    private void invalidate(Session session) {
        session.setActive(false);
    }

    private Session buildNewSession(User user) {
        return Session.builder()
                .active(true)
                .expirationTime(LocalDateTime.now().plusHours(1))
                .user(user)
                .build();
    }
}
