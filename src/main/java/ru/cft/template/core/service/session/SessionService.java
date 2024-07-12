package ru.cft.template.core.service.session;

import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.session.SessionDto;
import ru.cft.template.api.dto.session.SessionRequest;
import ru.cft.template.core.entity.Session;

import java.util.List;

@Service
public interface SessionService {

    Session getValidSession(String token);

    List<SessionDto> getAllActive();

    SessionDto getCurrent();

    SessionDto create(SessionRequest sessionRequest);

    void removeCurrent();

    void invalidateExpiredSessions();
}
