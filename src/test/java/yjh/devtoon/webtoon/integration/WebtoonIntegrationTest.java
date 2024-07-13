package yjh.devtoon.webtoon.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.member.domain.Authority;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.domain.Role;
import yjh.devtoon.member.infrastructure.MemberRepository;
import yjh.devtoon.webtoon.domain.Genre;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon.dto.request.WebtoonCreateRequest;
import yjh.devtoon.webtoon.infrastructure.LocalImageRepository;
import yjh.devtoon.webtoon.infrastructure.WebtoonRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@DisplayName("통합 테스트 [Webtoon]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebtoonIntegrationTest {

    private static final String NULL = null;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private LocalImageRepository localImageRepository;

    @Value("${file.local.upload.path}")
    private String uploadPath;

    @AfterEach
    public void tearDown() throws IOException {
        Path path = Paths.get(uploadPath);
        localImageRepository.deleteDirectory(path);
    }

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"MEMBER"})
    @Nested
    @DisplayName("웹툰 등록 테스트")
    class WebtoonRegisterTests {

        @DisplayName("웹툰 등록 성공")
        @Test
        void registerWebtoon_successfully() throws Exception {
            // given
            final WebtoonCreateRequest request = new WebtoonCreateRequest(
                    "쿠베라",
                    "카레곰",
                    "horror"
            );
            String requestBody = objectMapper.writeValueAsString(request);

            MockMultipartFile imageFile = new MockMultipartFile(
                    "image",
                    "test-image.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "Test Image Content".getBytes()
            );

            MockMultipartFile jsonFile = new MockMultipartFile(
                    "request",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    requestBody.getBytes()
            );

            // when
            mockMvc.perform(multipart("/v1/webtoons")
                            .file(imageFile)
                            .file(jsonFile))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").value(NULL));
        }

        @DisplayName("웹툰 등록 실패 - 필드가 null인 경우")
        @ParameterizedTest
        @CsvSource(value = {", 카레곰, horror", "쿠베라, , horror"}, delimiter = ',')
        void givenNullField_whenRegisterWebtoon_thenThrowException(String title,
                                                                   String writerName,
                                                                   String genre) throws Exception {
            // given
            final WebtoonCreateRequest request = new WebtoonCreateRequest(
                    title,
                    writerName,
                    genre
            );
            String requestBody = objectMapper.writeValueAsString(request);

            MockMultipartFile imageFile = new MockMultipartFile(
                    "image",
                    "test-image.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "Test Image Content".getBytes()
            );

            MockMultipartFile jsonFile = new MockMultipartFile(
                    "request",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    requestBody.getBytes()
            );

            // when
            mockMvc.perform(multipart("/v1/webtoons")
                            .file(imageFile)
                            .file(jsonFile))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @DisplayName("웹툰 등록 실패 - 필드가 공백인 경우")
        @ParameterizedTest
        @CsvSource(value = {"' ', 카레곰, horror", "쿠베라, ' ', horror"}, delimiter = ',')
        void givenEmptyField_whenRegisterWebtoon_thenThrowException(String title,
                                                                    String writerName,
                                                                    String genre) throws Exception {
            // given
            final WebtoonCreateRequest request = new WebtoonCreateRequest(
                    title,
                    writerName,
                    genre
            );
            String requestBody = objectMapper.writeValueAsString(request);

            MockMultipartFile imageFile = new MockMultipartFile(
                    "image",
                    "test-image.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "Test Image Content".getBytes()
            );

            MockMultipartFile jsonFile = new MockMultipartFile(
                    "request",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    requestBody.getBytes()
            );

            // when
            mockMvc.perform(multipart("/v1/webtoons")
                            .file(imageFile)
                            .file(jsonFile))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @DisplayName("웹툰 등록 실패 - 필드 사이즈 범위가 [1~20]이 아닌경우")
        @ParameterizedTest
        @CsvSource(value = {"123456789012345678901, 카레곰, horror", "쿠베라, 123456789012345678901, " +
                "horror"}, delimiter = ',')
        void givenNotRangeFiled_whenRegisterWebtoon_thenThrowException(String title,
                                                                       String writerName,
                                                                       String genre) throws Exception {
            // given
            final WebtoonCreateRequest request = new WebtoonCreateRequest(
                    title,
                    writerName,
                    genre
            );
            String requestBody = objectMapper.writeValueAsString(request);

            MockMultipartFile imageFile = new MockMultipartFile(
                    "image",
                    "test-image.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "Test Image Content".getBytes()
            );

            MockMultipartFile jsonFile = new MockMultipartFile(
                    "request",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    requestBody.getBytes()
            );

            // when
            mockMvc.perform(multipart("/v1/webtoons")
                            .file(imageFile)
                            .file(jsonFile))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @DisplayName("웹툰 등록 실패 - 중복된 웹툰 이름 존재")
        @TestFactory
        Stream<DynamicTest> givenDuplicatedTitleField_whenRegisterWebtoon_thenThrowException() {
            return Stream.of(
                    DynamicTest.dynamicTest("1. 웹툰 저장", () -> {
                        // given
                        WebtoonEntity webtoonEntity = WebtoonEntity.builder()
                                .title("쿠베라")
                                .writerName("카레곰")
                                .genre(Genre.HORROR)
                                .build();

                        // when
                        webtoonRepository.save(webtoonEntity);

                        // then
                        Optional<WebtoonEntity> saved = webtoonRepository.findByTitle("쿠베라");
                        assertAll(
                                () -> assertThat(saved.isPresent()).isTrue(),
                                () -> assertThat(saved.get().getTitle()).isEqualTo("쿠베라")
                        );

                    }), DynamicTest.dynamicTest("2. 중복된 웹툰 저장 시도", () -> {
                        // given
                        final WebtoonCreateRequest request = new WebtoonCreateRequest(
                                "쿠베라",
                                "짜장곰",
                                "horror"
                        );
                        String requestBody = objectMapper.writeValueAsString(request);

                        MockMultipartFile imageFile = new MockMultipartFile(
                                "image",
                                "test-image.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "Test Image Content".getBytes()
                        );

                        MockMultipartFile jsonFile = new MockMultipartFile(
                                "request",
                                "",
                                MediaType.APPLICATION_JSON_VALUE,
                                requestBody.getBytes()
                        );

                        // when
                        mockMvc.perform(multipart("/v1/webtoons")
                                        .file(imageFile)
                                        .file(jsonFile))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.statusMessage").value("실패"))
                                .andExpect(jsonPath("$.data.status").value(HttpStatus.CONFLICT.value()));
                    })
            );
        }

    }

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"MEMBER"})
    @Nested
    @DisplayName("웹툰 조회 테스트")
    class WebtoonRetrieveTests {

        @DisplayName("웹툰 조회 성공")
        @Test
        void retrieveWebtoon_successfully() throws Exception {
            // given
            WebtoonEntity webtoonEntity = WebtoonEntity.builder()
                    .title("쿠베라")
                    .writerName("카레곰")
                    .genre(Genre.HORROR)
                    .build();
            WebtoonEntity saved = webtoonRepository.save(webtoonEntity);

            // when
            mockMvc.perform(get("/v1/webtoons/" + saved.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.webtoonId").value(saved.getId()))
                    .andExpect(jsonPath("$.data.title").value(saved.getTitle()))
                    .andExpect(jsonPath("$.data.writerName").value(saved.getWriterName()));
        }

        @DisplayName("웹툰 조회 실패 - 존재 하지 않는 id로 조회한 경우")
        @Test
        void givenNotExistWebtoonId_whenRetrieveWebtoon_thenThrowException() throws Exception {
            // when
            mockMvc.perform(get("/v1/webtoons/99999999999999")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"MEMBER"})
    @Nested
    @DisplayName("웹툰 전체 조회 테스트")
    class WebtoonRetrieveAllTests {

        @DisplayName("웹툰 전체 조회 성공")
        @Test
        void retrieveAllWebtoon_successfully() throws Exception {
            // given
            WebtoonEntity webtoonEntity1 = WebtoonEntity.builder()
                    .title("쿠베라")
                    .writerName("카레곰")
                    .genre(Genre.HORROR)
                    .build();
            webtoonRepository.save(webtoonEntity1);

            WebtoonEntity webtoonEntity2 = WebtoonEntity.builder()
                    .title("기기괴괴")
                    .writerName("오성대")
                    .genre(Genre.HORROR)
                    .build();
            webtoonRepository.save(webtoonEntity2);

            // when
            mockMvc.perform(get("/v1/webtoons")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content[0].title").value("쿠베라"))
                    .andExpect(jsonPath("$.data.content[1].title").value("기기괴괴"));
        }
    }

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"MEMBER"})
    @Nested
    @DisplayName("나의 웹툰 전체 조회 테스트")
    class WebtoonRetrieveAllMyTests {

        @DisplayName("나의 웹툰 전체 조회 성공")
        @Test
        void retrieveAllWebtoon_successfully() throws Exception {
            // given
            MemberEntity member = MemberEntity.builder()
                    .name("카레곰")
                    .email("email@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .membershipStatus(MembershipStatus.GENERAL)
                    .authorities(Set.of(new Authority(Role.MEMBER)))
                    .build();
            memberRepository.save(member);

            WebtoonEntity webtoonEntity1 = WebtoonEntity.builder()
                    .title("쿠베라")
                    .writerName("카레곰")
                    .genre(Genre.HORROR)
                    .build();
            webtoonRepository.save(webtoonEntity1);

            WebtoonEntity webtoonEntity2 = WebtoonEntity.builder()
                    .title("기기괴괴")
                    .writerName("오성대")
                    .genre(Genre.HORROR)
                    .build();
            webtoonRepository.save(webtoonEntity2);

            // when
            mockMvc.perform(get("/v1/webtoons/my")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content[0].title").value("쿠베라"));
        }
    }

}