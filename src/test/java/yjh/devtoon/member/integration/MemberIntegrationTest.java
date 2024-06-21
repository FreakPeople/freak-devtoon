package yjh.devtoon.member.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;
import yjh.devtoon.bad_words_warning_count.infrastructure.BadWordsWarningCountRepository;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.dto.request.MemberRegisterRequest;
import yjh.devtoon.member.infrastructure.MemberRepository;
import java.util.Optional;
import java.util.stream.Stream;

@DisplayName("통합 테스트 [Member]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberIntegrationTest {

    private static final String NULL = null;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BadWordsWarningCountRepository badWordsWarningCountRepository;

    @Autowired
    private CookieWalletRepository cookieWalletRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("웹툰 독자 회원가입 기능 테스트")
    class MemberRegisterTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("웹툰 독자 회원가입 성공")
        @Test
        void registerMember_successfully() throws Exception {
            // given
            final MemberRegisterRequest request = new MemberRegisterRequest(
                    VALID_FILED_TITLE,
                    VALID_FILED_EMAIL,
                    VALID_FILED_PASSWORD
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/members")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").value(NULL));
        }

        @DisplayName("웹툰 독자 회원가입 성공 시 비속어 카운트 테이블, 쿠키 지갑 테이블도 함께 생성되어야 한다.")
        @Test
        void given_whenRegisterMember_thenCreateBadWordsWarningCount_and_CookieWallet() throws Exception {
            // given
            final MemberRegisterRequest request = new MemberRegisterRequest(
                    VALID_FILED_TITLE,
                    VALID_FILED_EMAIL,
                    VALID_FILED_PASSWORD
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/members")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").value(NULL));

            // then
            Optional<MemberEntity> saved = memberRepository.findByEmail(VALID_FILED_EMAIL);

            assertThat(saved).isNotEmpty();

            Long savedMemberId = saved.get().getId();
            Optional<BadWordsWarningCountEntity> createdBadWordsWarningCount = badWordsWarningCountRepository.findById(savedMemberId);
            Optional<CookieWalletEntity> createdCookieWallet = cookieWalletRepository.findById(savedMemberId);

            assertAll(
                    () -> assertThat(createdBadWordsWarningCount).isNotEmpty(),
                    () -> assertThat(createdCookieWallet).isNotEmpty()
            );
        }

        @DisplayName("웹툰 독자 회원가입 실패 - 필드 유효성 검사")
        @ParameterizedTest
        @MethodSource("provideStringsForIsInvalidField")
        void givenInvalidField_whenRegisterMember_thenThrowException(String name, String email, String password) throws Exception {
            // given
            final MemberRegisterRequest request = new MemberRegisterRequest(
                    name,
                    email,
                    password
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/members")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        /**
         * member 도메인의 MemberRegisterRequest.class 의 필드 테스트
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
    class MemberRetrieveTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("웹툰 독자 조회 성공")
        @Test
        void retrieveMember_successfully() throws Exception {
            // given
            MemberEntity saved = memberRepository.save(MemberEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            Long requestId = saved.getId();

            // when
            mockMvc.perform(get("/v1/members/" + requestId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.name").value(VALID_FILED_TITLE));
        }

        @DisplayName("웹툰 독자 회원 조회 실패 - 해당 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistMemberId_whenRetrieveMember_thenThrowException() throws Exception {
            // given
            final Long notExistMemberId = 999999999999999L;

            // when
            mockMvc.perform(get("/v1/members/" + notExistMemberId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

    @Nested
    @DisplayName("웹툰 독자 회원 등급 변경 테스트")
    class MembershipStatusChangeTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("웹툰 독자 회원 등급 변경 성공")
        @Test
        void changeMembershipStatus_successfully() throws Exception {
            // given
            MemberEntity saved = memberRepository.save(MemberEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            Long requestId = saved.getId();
            String requestBody = """
                    {"membershipStatus" : "premium"}
                    """;

            // when
            mockMvc.perform(patch("/v1/members/" + requestId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").value(NULL));

            assertThat(saved.getMembershipStatus()).isEqualTo(MembershipStatus.PREMIUM);
        }

        @DisplayName("웹툰 독자 회원 등급 변경 실패 - 요청 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistMemberId_whenChangeMembershipStatus_thenThrowException() throws Exception {
            // given
            long notExistMemberId = 999999999999999L;
            String requestBody = """
                    {"membershipStatus" : "premium"}
                    """;

            // when
            mockMvc.perform(patch("/v1/members/" + notExistMemberId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @DisplayName("웹툰 독자 회원 등급 변경 실패 - 요청 status의 회원등급이 존재 하지 않음")
        @Test
        void givenNotExistMembershipStatus_whenChangeMembershipStatus_thenThrowException() throws Exception {
            // given
            MemberEntity saved = memberRepository.save(MemberEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            Long requestId = saved.getId();
            String requestBody = """
                    {"membershipStatus" : "notExist"}
                    """;

            // when
            mockMvc.perform(patch("/v1/members/" + requestId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

}
