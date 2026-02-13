package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IdGenerator {
    private int nextId = 0;

    public int getNextId() {
        return ++nextId;
    }
}
