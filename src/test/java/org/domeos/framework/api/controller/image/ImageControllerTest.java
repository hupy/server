package org.domeos.framework.api.controller.image;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.shiro.util.ThreadContext;
import org.domeos.base.BaseTestCase;
import org.domeos.basemodel.ResultStat;
import org.domeos.framework.api.consolemodel.image.ImageNameDetailRequest;
import org.domeos.framework.api.consolemodel.image.ImageTagDetailRequest;
import org.domeos.framework.api.model.image.BaseImage;
import org.domeos.framework.api.model.image.BuildImage;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by baokangwang on 2016/4/7.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class ImageControllerTest extends BaseTestCase {

    BaseImage baseImage;
    String baseImageStr;

    BuildImage buildImage;
    String buildImageStr;

    ImageNameDetailRequest imageNameDetailRequest;
    String imageNameDetailRequestStr;

    ImageTagDetailRequest imageTagDetailRequest;
    String imageTagDetailRequestStr;

    @Before
    public void setup() throws IOException {

        ThreadContext.bind(securityManager);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        FileInputStream imageTagDetailRequestStream = new FileInputStream("./src/test/resources/image/imageTagDetailRequest.json");
        byte[] imageTagDetailRequestBuff = new byte[imageTagDetailRequestStream.available()];
        imageTagDetailRequestStream.read(imageTagDetailRequestBuff);
        imageTagDetailRequest = objectMapper.readValue(imageTagDetailRequestBuff, ImageTagDetailRequest.class);
        imageTagDetailRequestStr = new String(imageTagDetailRequestBuff);

        FileInputStream fileInputStream = new FileInputStream("./src/test/resources/ci/baseImage.json");
        byte[] buff = new byte[fileInputStream.available()];
        fileInputStream.read(buff);
        baseImage = objectMapper.readValue(buff, BaseImage.class);
        baseImageStr = new String(buff);

        FileInputStream buildImageStream = new FileInputStream("./src/test/resources/ci/buildImage.json");
        byte[] buildImageBuff = new byte[buildImageStream.available()];
        buildImageStream.read(buildImageBuff);
        buildImage = objectMapper.readValue(buildImageBuff, BuildImage.class);
        buildImageStr = new String(buildImageBuff);

        FileInputStream imageNameDetailRequestStream = new FileInputStream("./src/test/resources/image/imageNameDetailRequest.json");
        byte[] imageNameDetailRequestBuff = new byte[imageNameDetailRequestStream.available()];
        imageNameDetailRequestStream.read(imageNameDetailRequestBuff);
        imageNameDetailRequest = objectMapper.readValue(imageNameDetailRequestBuff, ImageNameDetailRequest.class);
        imageNameDetailRequestStr = new String(imageNameDetailRequestBuff);

        this.mockMvc = webAppContextSetup(this.wac).build();
        login("admin", "admin");
    }

    @Test
    public void T010SetBaseImage() throws Exception {
        mockMvc.perform(post("/api/image/base").contentType(MediaType.APPLICATION_JSON).content(baseImageStr))
                .andDo(print())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T020GetBaseImage() throws Exception {
        mockMvc.perform(get("/api/image/base/{id}", baseImage.getId()))
                .andDo(print())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T030ListBaseImage() throws Exception {
        mockMvc.perform(get("/api/image/base"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T040DeleteBaseImage() throws Exception {
        mockMvc.perform(delete("/api/image/base/{id}", baseImage.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T050SetBuildImage() throws Exception {
        mockMvc.perform(post("/api/image/build").contentType(MediaType.APPLICATION_JSON).content(buildImageStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T060GetBuildImage() throws Exception {
        mockMvc.perform(get("/api/image/build"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T070GetDockerImages() throws Exception {
        mockMvc.perform(get("/api/image"))
                .andDo(print())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T080GetDockerImageDetail() throws Exception {
        mockMvc.perform(get("/api/image/detail").param("name", "domeos").param("registry", "10.11.150.76:5000"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T090GetAllDockerImages() throws Exception {
        mockMvc.perform(get("/api/image/all"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T100GetAllDockerImageDetail() throws Exception {
        mockMvc.perform(get("/api/image/all/detail").param("name", "mytest").param("registry", "http://10.11.150.76:5000"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T110GetAllExclusiveImages() throws Exception {
        mockMvc.perform(get("/api/image/exclusive/java"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T120DockerImageNameDetail() throws Exception {
        mockMvc.perform(post("/api/image/all/detail").contentType(MediaType.APPLICATION_JSON).content(imageNameDetailRequestStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T130GetImageTagDetail() throws Exception {
        mockMvc.perform(post("/api/image/all/detail/tag").contentType(MediaType.APPLICATION_JSON).content(imageTagDetailRequestStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
}
