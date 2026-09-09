package org.sun.racing.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class EntityUnfreezeEvent extends ApplicationEvent {
    @Getter
    private final Long participationEntityId;

    public EntityUnfreezeEvent(Long freezedEntityId) {
        super(freezedEntityId);
        this.participationEntityId = freezedEntityId;
    }
}
