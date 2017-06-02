package org.domeos.framework.api.controller.loadBalancer;

import org.apache.shiro.util.ThreadContext;
import org.domeos.base.BaseTestCase;
import org.domeos.basemodel.ResultStat;
import org.domeos.framework.api.service.deployment.impl.InstanceServiceImpl;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by jackfan on 16/4/2.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class LoadBalancerVersionControllerTest extends BaseTestCase {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(InstanceServiceImpl.class);

    @Before
    public void setup() throws Exception {
        ThreadContext.bind(securityManager);
        this.mockMvc = webAppContextSetup(this.wac).build();
        login("admin", "admin");
    }
    
    @Test
    public void T001CreateLoadBalancerCollection() throws Exception {
        FileInputStream loadBalancerDraftInputStream = new FileInputStream("./src/test/resources/loadBalancer/loadBalancerCollectionNginxDraft.json");
        byte[] loadBalancerDraftBuff = new byte[loadBalancerDraftInputStream.available()];
        loadBalancerDraftInputStream.read(loadBalancerDraftBuff);
        String loadBalancerDraftStr = new String(loadBalancerDraftBuff);
        logger.info("----loadBalancerNginx-collection----" + loadBalancerDraftStr);
        mockMvc.perform(post("/api/loadBalancerCollection").contentType(MediaType.APPLICATION_JSON).content(loadBalancerDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
    
    @Test
    public void T002CreateCluster() throws Exception {
        FileInputStream clusterCreate = new FileInputStream("./src/test/resources/cluster/clusterForLB.json");
        byte[] availableBytes = new byte[clusterCreate.available()];
        clusterCreate.read(availableBytes);
        String clusterCreateStr = new String(availableBytes);
        mockMvc.perform(post("/api/cluster").contentType(MediaType.APPLICATION_JSON).content(clusterCreateStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
    
    @Test
    public void T003CreateLoadBalancer() throws Exception {
        FileInputStream loadBalancerDraftInputStream = new FileInputStream("./src/test/resources/loadBalancer/loadBalancerNginxDraftVersion.json");
        byte[] loadBalancerDraftBuff = new byte[loadBalancerDraftInputStream.available()];
        loadBalancerDraftInputStream.read(loadBalancerDraftBuff);
        String loadBalancerDraftStr = new String(loadBalancerDraftBuff);
        logger.info("----loadBalancerNginxDraftStr----" + loadBalancerDraftStr);
        mockMvc.perform(post("/api/loadBalancer").contentType(MediaType.APPLICATION_JSON).content(loadBalancerDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
    
    @Test
    public void T004CreateLoadBalancerVersion() throws Exception {
        FileInputStream loadBalancerVersionInputStream = new FileInputStream("./src/test/resources/loadBalancer/loadBalancerVersionDraft.json");
        byte[] loadBalancerVersionBuff = new byte[loadBalancerVersionInputStream.available()];
        loadBalancerVersionInputStream.read(loadBalancerVersionBuff);
        String loadBalancerVersionStr = new String(loadBalancerVersionBuff);
        logger.info("----loadBalancerVersion----" + loadBalancerVersionStr);
        mockMvc.perform(post("/api/loadBalancer/version/1").contentType(MediaType.APPLICATION_JSON).content(loadBalancerVersionStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
     
    @Test
    public void T005GetLoadBalancerVersion() throws Exception {
        mockMvc.perform(get("/api/loadBalancer/version/id/1/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
}
