package yjh.devtoon.comment.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.comment.domain.CommentEntity;
import yjh.devtoon.comment.dto.request.CommentCreateRequest;
import yjh.devtoon.comment.infrastructure.CommentRepository;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.webtoon.domain.Genre;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon.infrastructure.WebtoonRepository;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.infrastructure.MemberRepository;

@DisplayName("통합 테스트 [Comment]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommentIntegrationTest {

    private static final String NULL = null;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private MemberRepository memberRepository;

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"MEMBER"})
    @Nested
    @DisplayName("댓글 등록 테스트")
    class CommentRegisterTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("댓글 등록 성공")
        @Test
        void registerComment_successfully() throws Exception {
            // given
            // 웹툰 저장
            WebtoonEntity savedWebtoon = webtoonRepository.save(WebtoonEntity.builder()
                    .title("쿠베라")
                    .writerName("카레곰")
                    .genre(Genre.HORROR)
                    .build()
            );
            // 웹툰 구독자 저장
            MemberEntity savedMember =
                    memberRepository.save(MemberEntity.builder()
                            .name(VALID_FILED_TITLE)
                            .email(VALID_FILED_EMAIL)
                            .password(VALID_FILED_PASSWORD)
                            .membershipStatus(MembershipStatus.GENERAL)
                            .build()
                    );
            long detailId = 1L;

            final CommentCreateRequest request = new CommentCreateRequest(
                    savedWebtoon.getId(),
                    detailId,
                    savedMember.getId(),
                    "정말 레전드 작화 네요!"
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/comments")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").value(NULL));
        }

        @DisplayName("댓글 등록 실패 - 필드가 null인 경우")
        @Test
        void givenNullField_whenRegisterComment_thenThrowException() throws Exception {
            // given
            // 웹툰 저장
            WebtoonEntity savedWebtoon = webtoonRepository.save(WebtoonEntity.builder()
                    .title("쿠베라")
                    .writerName("카레곰")
                    .genre(Genre.HORROR)
                    .build()
            );
            // 웹툰 구독자 저장
            MemberEntity savedMember =
                    memberRepository.save(MemberEntity.builder()
                            .name(VALID_FILED_TITLE)
                            .email(VALID_FILED_EMAIL)
                            .password(VALID_FILED_PASSWORD)
                            .membershipStatus(MembershipStatus.GENERAL)
                            .build()
                    );
            long detailId = 1L;

            final CommentCreateRequest request = new CommentCreateRequest(
                    savedWebtoon.getId(),
                    detailId,
                    savedMember.getId(),
                    null
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/comments")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @DisplayName("댓글 등록 실패 - 필드가 공백인 경우")
        @Test
        void givenEmptyField_whenRegisterComment_thenThrowException() throws Exception {
            // given
            // given
            // 웹툰 저장
            WebtoonEntity savedWebtoon = webtoonRepository.save(WebtoonEntity.builder()
                    .title("쿠베라")
                    .writerName("카레곰")
                    .genre(Genre.HORROR)
                    .build()
            );
            // 웹툰 구독자 저장
            MemberEntity savedMember =
                    memberRepository.save(MemberEntity.builder()
                            .name(VALID_FILED_TITLE)
                            .email(VALID_FILED_EMAIL)
                            .password(VALID_FILED_PASSWORD)
                            .membershipStatus(MembershipStatus.GENERAL)
                            .build()
                    );
            long detailId = 1L;

            final CommentCreateRequest request = new CommentCreateRequest(
                    savedWebtoon.getId(),
                    detailId,
                    savedMember.getId(),
                    ""
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/comments")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @DisplayName("댓글 등록 실패 - 필드 사이즈 범위가 [1~100]이 아닌경우")
        @Test
        void givenNotRangeFiled_whenRegisterComment_thenThrowException() throws Exception {
            // given
            // 웹툰 저장
            WebtoonEntity savedWebtoon = webtoonRepository.save(WebtoonEntity.builder()
                    .title("쿠베라")
                    .writerName("카레곰")
                    .genre(Genre.HORROR)
                    .build()
            );
            // 웹툰 구독자 저장
            MemberEntity savedMember =
                    memberRepository.save(MemberEntity.builder()
                            .name(VALID_FILED_TITLE)
                            .email(VALID_FILED_EMAIL)
                            .password(VALID_FILED_PASSWORD)
                            .membershipStatus(MembershipStatus.GENERAL)
                            .build()
                    );
            long detailId = 1L;

            final CommentCreateRequest request = new CommentCreateRequest(
                    savedWebtoon.getId(),
                    detailId,
                    savedMember.getId(),
                    "문" .repeat(101)
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/comments")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @DisplayName("댓글 등록 실패 - 해당 웹툰이 없는 경우")
        @Test
        void givenNotExistWebtoonId_whenRegisterComment_thenThrowException() throws Exception {
            // given
            long NotExistWebtoonId = 99999999999L;

            // 웹툰 구독자 저장
            MemberEntity savedMember =
                    memberRepository.save(MemberEntity.builder()
                            .name(VALID_FILED_TITLE)
                            .email(VALID_FILED_EMAIL)
                            .password(VALID_FILED_PASSWORD)
                            .membershipStatus(MembershipStatus.GENERAL)
                            .build()
                    );
            long detailId = 1L;

            final CommentCreateRequest request = new CommentCreateRequest(
                    NotExistWebtoonId,
                    detailId,
                    savedMember.getId(),
                    "정말 레전드 작화 네요!"
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/comments")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"MEMBER"})
    @Nested
    @DisplayName("댓글 조회 테스트")
    class CommentRetrieveTests {

        @DisplayName("댓글 조회 성공")
        @Test
        void retrieveComment_successfully() throws Exception {
            // given
            CommentEntity comment = CommentEntity.builder()
                    .webtoonId(2L)
                    .detailId(2L)
                    .memberId(2L)
                    .content("재밌어요!")
                    .build();
            CommentEntity saved = commentRepository.save(comment);

            // when
            mockMvc.perform(get("/v1/comments/" + saved.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.webtoonNo").value(saved.getWebtoonId()))
                    .andExpect(jsonPath("$.data.webtoonDetailNo").value(saved.getDetailId()))
                    .andExpect(jsonPath("$.data.writerId").value(saved.getMemberId()))
                    .andExpect(jsonPath("$.data.content").value(saved.getContent()));
        }

        @DisplayName("댓글 조회 실패 - 존재 하지 않는 id로 조회한 경우")
        @Test
        void givenNotExistWebtoonId_whenRetrieveWebtoon_thenThrowException() throws Exception {
            // given
            long NotExistCommentId = 99999999999L;

            // when
            mockMvc.perform(get("/v1/webtoons/" + NotExistCommentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @DisplayName("특정 웹툰에 대한 모든 댓글 조회 성공")
        @Test
        void retrieveAllCommentByWebtoonId_successfully() throws Exception {
            // given
            long webtoonId = 2L;
            CommentEntity comment1 = CommentEntity.builder()
                    .webtoonId(webtoonId)
                    .detailId(2L)
                    .memberId(2L)
                    .content("재밌어요!")
                    .build();
            commentRepository.save(comment1);

            CommentEntity comment2 = CommentEntity.builder()
                    .webtoonId(webtoonId)
                    .detailId(2L)
                    .memberId(2L)
                    .content("최고에요!")
                    .build();
            commentRepository.save(comment2);

            // when
            mockMvc.perform(get("/v1/comments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("webtoonId", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content[0].content").value("재밌어요!"))
                    .andExpect(jsonPath("$.data.content[1].content").value("최고에요!"));
        }

    }

}