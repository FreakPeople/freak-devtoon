package yjh.devtoon.webtoon_viewer.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.webtoon_viewer.domain.MembershipStatus;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import yjh.devtoon.webtoon_viewer.dto.request.WebtoonViewerRegisterRequest;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;
import java.util.stream.Stream;

@DisplayName("통합 테스트 [WebtoonViewer]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebtoonViewerIntegrationTest {

    private static final String NULL = null;

    @Autowired
    private WebtoonViewerRepository webtoonViewerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("웹툰 독자 회원가입 기능 테스트")
    class WebtoonViewerRegisterTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("웹툰 독자 회원가입 성공")
        @Test
        void registerWebtoonViewer_successfully() throws Exception {
            // given
            final WebtoonViewerRegisterRequest request = new WebtoonViewerRegisterRequest(
                    VALID_FILED_TITLE,
                    VALID_FILED_EMAIL,
                    VALID_FILED_PASSWORD
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/webtoon-viewers")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").value(NULL));
        }

        @DisplayName("웹툰 독자 회원가입 실패 - 필드 유효성 검사")
        @ParameterizedTest
        @MethodSource("provideStringsForIsInvalidField")
        void givenInvalidField_whenRegisterWebtoonViewer_thenThrowException(String name, String email, String password) throws Exception {
            // given
            final WebtoonViewerRegisterRequest request = new WebtoonViewerRegisterRequest(
                    name,
                    email,
                    password
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/webtoon-viewers")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        /**
         * webtoon-viewer 도메인의 WebtoonViewerRegisterRequest.class 의 필드 테스트
         */
        private static Stream<Arguments> provideStringsForIsInvalidField() {
            final String SIZE_4 = "0123";
            final String SIZE_21 = "012345678901234567890";
            return Stream.of(
                    // invalid name field
                    Arguments.of(null, "email", "password"),
                    Arguments.of("", "email", "password"),
                    Arguments.of(" ", "email", "password"),
                    Arguments.of(SIZE_21, "email", "password"),
                    // invalid email field
                    Arguments.of("name", null, "password"),
                    Arguments.of("name", "", "password"),
                    Arguments.of("name", " ", "password"),
                    Arguments.of("name", SIZE_21, "password"),
                    // invalid password field
                    Arguments.of("name", "email", null),
                    Arguments.of("name", "email", ""),
                    Arguments.of("name", "email", " "),
                    Arguments.of("name", "email", SIZE_4),
                    Arguments.of("name", "email", SIZE_21)
            );
        }

    }

    @Nested
    @DisplayName("웹툰 독자 회원 조회 기능 테스트")
    class WebtoonViewerRetrieveTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("웹툰 독자 조회 성공")
        @Test
        void retrieveWebtoonViewer_successfully() throws Exception {
            // given
            WebtoonViewerEntity saved = webtoonViewerRepository.save(WebtoonViewerEntity.builder()
                            .name(VALID_FILED_TITLE)
                            .email(VALID_FILED_EMAIL)
                            .password(VALID_FILED_PASSWORD)
                            .membershipStatus(MembershipStatus.GENERAL)
                            .build()
            );
            Long requestId = saved.getId();

            // when
            mockMvc.perform(get("/v1/webtoon-viewers/" + requestId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.name").value(VALID_FILED_TITLE));
        }

        @DisplayName("웹툰 독자 회원 조회 실패 - 해당 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistWebtoonViewerId_whenRetrieveWebtoonViewer_thenThrowException() throws Exception {
            // given
            final Long notExistWebtoonViewerId = 999999999999999L;

            // when
            mockMvc.perform(get("/v1/webtoon-viewers/" + notExistWebtoonViewerId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

}
