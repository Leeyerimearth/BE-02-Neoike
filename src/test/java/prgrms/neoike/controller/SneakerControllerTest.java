package prgrms.neoike.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import prgrms.neoike.controller.dto.sneaker.request.SneakerImageRequest;
import prgrms.neoike.controller.dto.sneaker.request.SneakerRegisterRequest;
import prgrms.neoike.controller.dto.sneaker.request.SneakerRequest;
import prgrms.neoike.controller.dto.sneaker.request.SneakerStockRequest;
import prgrms.neoike.service.SneakerService;
import prgrms.neoike.service.dto.sneaker.SneakerResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static prgrms.neoike.domain.sneaker.MemberCategory.MEN;
import static prgrms.neoike.domain.sneaker.SneakerCategory.JORDAN;

@AutoConfigureRestDocs
@WebMvcTest(SneakerController.class)
class SneakerControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SneakerService sneakerService;

    ObjectMapper objectMapper = new ObjectMapper()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .registerModule(new JavaTimeModule());

    @Test
    @DisplayName("신발 생성이 정상적으로 이루어진다.")
    void testRegisterNewSneaker() throws Exception {
        SneakerRegisterRequest registerRequest = createRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(registerRequest);

        given(sneakerService.registerSneaker(any()))
            .willReturn(
                new SneakerResponse(1L, "test", "DS-1234567")
            );

        mvc.perform(
            post("/api/v1/sneakers")
                .content(requestJson)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(
                document("sneaker-register",
                    requestRegisterSnippet(),
                    responseRegisterSnippet()
                )
            );
    }

    SneakerRegisterRequest createRegisterRequest() {
        return new SneakerRegisterRequest(
            List.of(new SneakerImageRequest("test.PNG", encodedName())),
            new SneakerRequest(MEN, JORDAN, "Jordan 1", 100000, "this is test jordan.", "", of(2022, 10, 10, 10, 0, 0)),
            List.of(new SneakerStockRequest(250, 10), new SneakerStockRequest(260, 10))
        );
    }

    public static String encodedName() {
        File imageFile = new File("src/test/resources/test.PNG");

        try (
            FileInputStream fis = new FileInputStream(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }

            byte[] extractedBytes = Base64.encodeBase64(baos.toByteArray());
            return new String(extractedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    RequestFieldsSnippet requestRegisterSnippet() {
        return requestFields(
            fieldWithPath("sneakerImages").type(ARRAY).description("신발 이미지"),
            fieldWithPath("sneakerImages.[*].originName").type(STRING).description("원본 파일이름"),
            fieldWithPath("sneakerImages.[*].encodedName").type(STRING).description("인코딩 파일이름"),
            fieldWithPath("sneaker").type(OBJECT).description("신발"),
            fieldWithPath("sneaker.memberCategory").type(STRING).description("멤버 카테고리"),
            fieldWithPath("sneaker.sneakerCategory").type(STRING).description("신발 카테고리"),
            fieldWithPath("sneaker.name").type(STRING).description("이름"),
            fieldWithPath("sneaker.price").type(NUMBER).description("가격"),
            fieldWithPath("sneaker.description").type(STRING).description("상품설명"),
            fieldWithPath("sneaker.code").type(STRING).description("코드"),
            fieldWithPath("sneaker.releaseDate").type(STRING).description("출시일자"),
            fieldWithPath("sneakerStocks").type(ARRAY).description("신발재고"),
            fieldWithPath("sneakerStocks.[*].size").type(NUMBER).description("사이즈"),
            fieldWithPath("sneakerStocks.[*].quantity").type(NUMBER).description("수량")
        );
    }

    ResponseFieldsSnippet responseRegisterSnippet() {
        return responseFields(
            fieldWithPath("sneakerId").type(NUMBER).description("신발 아이디"),
            fieldWithPath("name").type(STRING).description("이름"),
            fieldWithPath("code").type(STRING).description("코드")
        );
    }
}