package org.domeos.framework.api.controller.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.shiro.util.ThreadContext;
import org.domeos.base.BaseTestCase;
import org.domeos.basemodel.ResultStat;
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
 * Created by feiliu206363 on 2017/1/20.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class ConfigurationControllerTest extends BaseTestCase {
    private String collectionStr;
    private String configurationStr;

    @Before
    public void setup() throws IOException {
        ThreadContext.bind(securityManager);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        FileInputStream configurationInputStream = new FileInputStream("./src/test/resources/configuration/configuration.json");
        byte[] buffConfiguration = new byte[configurationInputStream.available()];
        configurationInputStream.read(buffConfiguration);
        configurationStr = new String(buffConfiguration);

        FileInputStream collectionInputStream = new FileInputStream("./src/test/resources/configuration/configurationcollection.json");
        byte[] buffCollection = new byte[collectionInputStream.available()];
        collectionInputStream.read(buffCollection);
        collectionStr = new String(buffCollection);

        this.mockMvc = webAppContextSetup(this.wac).build();
        login("admin", "admin");
    }

    @Test
    public void T001CreateConfigurationCollection() throws Exception {
        mockMvc.perform(post("/api/configurationcollection").contentType(MediaType.APPLICATION_JSON).content(collectionStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T002ListConfigurationCollection() throws Exception {
        mockMvc.perform(get("/api/configurationcollection"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T003GetConfigurationCollection() throws Exception {
        mockMvc.perform(get("/api/configurationcollection/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T004ModifyConfigurationCollection() throws Exception {
        mockMvc.perform(put("/api/configurationcollection").contentType(MediaType.APPLICATION_JSON).content(collectionStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T005CreateConfiguration() throws Exception {
        mockMvc.perform(post("/api/configurationcollection/1/configuration").contentType(MediaType.APPLICATION_JSON).content(configurationStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T005ListConfiguration() throws Exception {
        mockMvc.perform(get("/api/configurationcollection/1/configuration"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T006ListAllConfiguration() throws Exception {
        mockMvc.perform(get("/api/configurationcollection/configuration"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T007ListConfigurationByCluster() throws Exception {
        mockMvc.perform(get("/api/configurationcollection/cluster/1/configuration"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T008ListDeployInfoByConfigurationId() throws Exception {
        mockMvc.perform(get("/api/configurationcollection/configuration/1/deployinfo"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T008GetConfigurationById() throws Exception {
        mockMvc.perform(get("/api/configurationcollection/configuration/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T009GetConfigurationByCollectionId() throws Exception {
        mockMvc.perform(get("/api/configurationcollection/1/1/configuration"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T010ModifyConfiguration() throws Exception {
        mockMvc.perform(put("/api/configurationcollection/configuration").contentType(MediaType.APPLICATION_JSON).content(configurationStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T011ModifyConfiguration() throws Exception {
        mockMvc.perform(put("/api/configurationcollection/1/configuration").contentType(MediaType.APPLICATION_JSON).content(configurationStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T012DeleteConfiguration() throws Exception {
        mockMvc.perform(delete("/api/configurationcollection/1/configuration/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T013DeleteConfigurationCollection() throws Exception {
        mockMvc.perform(delete("/api/configurationcollection/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }
}
