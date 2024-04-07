package GDG.whatssue.domain.club.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NamePolicy {

    REAL_NAME("실명제"),
    NICK_NAME("닉네임제");

    private final String policy;
}
