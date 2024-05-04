package yjh.devtoon.bad_words_warning_count.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;
import yjh.devtoon.bad_words_warning_count.infrastructure.BadWordsWarningCountRepository;
import yjh.devtoon.webtoon_viewer.domain.MembershipStatus;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;

@DisplayName("통합 테스트 [WebtoonViewer]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BadWordsWarningIntegrationTest {

    @Autowired
    private BadWordsWarningCountRepository badWordsWarningCountRepository;

    @Autowired
    private WebtoonViewerRepository webtoonViewerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("비속어 카운트 조회 기능 테스트")
    class BadWordsWarningRetrieveTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("비속어 카운트 조회 성공")
        @Test
        void retrieveBadWordsWarningCount_successfully() throws Exception {
            // given
            // 회원 저장
            WebtoonViewerEntity savedViewer = webtoonViewerRepository.save(WebtoonViewerEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            // 회원 비속어 카운팅 정보 저장
            BadWordsWarningCountEntity badWordsWarningCount = new BadWordsWarningCountEntity(
                    savedViewer.getId(),
                    0,
                    null
            );
            badWordsWarningCountRepository.save(badWordsWarningCount);

            // when
            mockMvc.perform(get("/v1/bad-words-warning-count")
                            .param("webtoonViewerNo", String.valueOf(savedViewer.getId()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.webtoonViewerNo").value(savedViewer.getId()))
                    .andExpect(jsonPath("$.data.count").value("0"));
        }

        @DisplayName("비속어 카운트 조회 실패 - 해당 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistWebtoonViewerId_whenRetrieveWebtoonViewer_thenThrowException() throws Exception {
            // given
            final long notExistWebtoonViewerId = 999999999999999L;

            // when
            mockMvc.perform(get("/v1/bad-words-warning-count")
                            .param("webtoonViewerNo", String.valueOf(notExistWebtoonViewerId))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

    @Nested
    @DisplayName("비속어 카운트 증가 기능 테스트")
    class BadWordsWarningIncreaseTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("비속어 카운트 증가 성공")
        @Test
        void IncreaseBadWordsWarningCount_successfully() throws Exception {
            // given
            WebtoonViewerEntity savedViewer = webtoonViewerRepository.save(WebtoonViewerEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            BadWordsWarningCountEntity badWordsWarningCount = new BadWordsWarningCountEntity(
                    savedViewer.getId(),
                    0,
                    null
            );
            badWordsWarningCountRepository.save(badWordsWarningCount);

            // when
            mockMvc.perform(put("/v1/bad-words-warning-count/increase")
                            .param("webtoonViewerNo", String.valueOf(savedViewer.getId()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.webtoonViewerNo").value(savedViewer.getId()))
                    .andExpect(jsonPath("$.data.count").value("1"));
        }

        @DisplayName("비속어 카운트 증가 실패 - 증가 하려는 대상 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistWebtoonViewerId_whenIncreaseBadWordsWarningCount_thenThrowException() throws Exception {
            // given
            final long notExistWebtoonViewerId = 999999999999999L;

            // when
            mockMvc.perform(put("/v1/bad-words-warning-count/increase")
                            .param("webtoonViewerNo", String.valueOf(notExistWebtoonViewerId))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

}
