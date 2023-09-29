package com.minsproject.board.service;

import com.minsproject.board.domain.entity.HashtagEntity;
import com.minsproject.board.repository.HashtagEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@RequiredArgsConstructor
@Service
public class HashtagService {

    private final HashtagEntityRepository hashtagEntityRepository;

    public Set<String> parseHashtagNames(String body) {
        if (body == null) {
            return Set.of();
        }

        Pattern pattern = Pattern.compile("#[\\w가-힣]+");
        Matcher matcher = pattern.matcher(body.strip());
        Set<String> result = new HashSet<>();

        while (matcher.find()) {
            result.add(matcher.group().replace("#", ""));
        }

        return Set.copyOf(result);
    }

    public Set<HashtagEntity> findHashtagsByNames(Set<String> hashtagNames) {
        return new HashSet<>(hashtagEntityRepository.findByHashtagNameIn(hashtagNames));
    }
}
