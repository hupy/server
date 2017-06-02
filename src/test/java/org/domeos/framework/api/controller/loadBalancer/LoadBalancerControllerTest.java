package org.domeos.framework.api.controller.loadBalancer;

import org.apache.shiro.util.ThreadContext;
import org.domeos.base.BaseTestCase;
import org.domeos.basemodel.ResultStat;
import org.domeos.framework.api.consolemodel.loadBalancer.LoadBalancerCollectionDraft;
import org.domeos.framework.api.model.loadBalancer.related.LoadBalancerCollectionType;
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
 * Created by jackfan on 16/4/2
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class LoadBalancerControllerTest extends BaseTestCase {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(InstanceServiceImpl.class);

    @Before
    public void setup() throws Exception {
        ThreadContext.bind(securityManager);
        this.mockMvc = webAppContextSetup(this.wac).build();
        login("admin", "admin");
    }

    @Test
    public void T001CreateLoadBalancerCollection() throws Exception {
        FileInputStream loadBalancerDraftInputStream = new FileInputStream("./src/test/resources/loadBalancer/loadBalancerCollectionProxyDraft.json");
        byte[] loadBalancerDraftBuff = new byte[loadBalancerDraftInputStream.available()];
        loadBalancerDraftInputStream.read(loadBalancerDraftBuff);
        String loadBalancerDraftStr = new String(loadBalancerDraftBuff);
        logger.info("----loadBalancerProxy-collection----" + loadBalancerDraftStr);
        mockMvc.perform(post("/api/loadBalancerCollection").contentType(MediaType.APPLICATION_JSON).content(loadBalancerDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
    
    @Test
    public void T002CreateLoadBalancerCollection() throws Exception {
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
    public void T003ModifyLoadBalancerCollection() throws Exception {
        LoadBalancerCollectionDraft loadBalancerCollectionDraft = new LoadBalancerCollectionDraft();
        loadBalancerCollectionDraft.setId(1);
        loadBalancerCollectionDraft.setName("loadBalancerModify");
        loadBalancerCollectionDraft.setDescription("loadBalancerModify");
        loadBalancerCollectionDraft.setType(LoadBalancerCollectionType.KUBE_PROXY);
        String loadBalancerCollectionDraftStr = objectMapper.writeValueAsString(loadBalancerCollectionDraft);
        mockMvc.perform(put("/api/loadBalancerCollection").contentType(MediaType.APPLICATION_JSON).content(loadBalancerCollectionDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
    
    @Test
    public void T004GetLoadBalancerCollection() throws Exception {
        mockMvc.perform(get("/api/loadBalancerCollection/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
    
    @Test
    public void T005CreateCluster() throws Exception {
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
    public void T006CreateAndStartWatcher() throws Exception {
        //create cluster watcher
        FileInputStream watcherDraft = new FileInputStream("./src/test/resources/cluster/clusterwatcher.json");
        byte[] availableBytes = new byte[watcherDraft.available()];
        watcherDraft.read(availableBytes);
        String watcherDraftStr = new String(availableBytes);
        logger.info("----createWatcher----" + watcherDraftStr);
        mockMvc.perform(post("/api/cluster/2/watcher/create").contentType(MediaType.APPLICATION_JSON).content(watcherDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        
        mockMvc.perform(post("/api/deploy/action/start").param("deployId", "1").param("version", "1").param("replicas", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
    
    @Test
    public void T007CreateAndStartDeployment() throws Exception {
        Thread.sleep(60);
        //create deploy-collection
        FileInputStream deploymentCollectionDraft = new FileInputStream("./src/test/resources/deploy/deploycollection.json");
        byte[] availableBytes = new byte[deploymentCollectionDraft.available()];
        deploymentCollectionDraft.read(availableBytes);
        String deploymentCollectionDraftStr = new String(availableBytes);
        logger.info("----createAndStartDeployment----" + deploymentCollectionDraftStr);
        mockMvc.perform(post("/api/deploycollection").contentType(MediaType.APPLICATION_JSON).content(deploymentCollectionDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        
        //create deploy
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deploy/deployForLB.json");
        byte[] deploymentAvailableBytes = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentAvailableBytes);
        String deploymentDraftStr = new String(deploymentAvailableBytes);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/deploy/action/start").param("deployId", "2").param("version", "1").param("replicas", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
    
    @Test
    public void T008CreateLoadBalancer() throws Exception {
        FileInputStream loadBalancerDraftInputStream = new FileInputStream("./src/test/resources/loadBalancer/loadBalancerProxyDraft.json");
        byte[] loadBalancerDraftBuff = new byte[loadBalancerDraftInputStream.available()];
        loadBalancerDraftInputStream.read(loadBalancerDraftBuff);
        String loadBalancerDraftStr = new String(loadBalancerDraftBuff);
        logger.info("----loadBalancerProxyDraftStr----" + loadBalancerDraftStr);
        mockMvc.perform(post("/api/loadBalancer").contentType(MediaType.APPLICATION_JSON).content(loadBalancerDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T009CreateLoadBalancer() throws Exception {
        FileInputStream loadBalancerDraftInputStream = new FileInputStream("./src/test/resources/loadBalancer/loadBalancerNginxDraft.json");
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
    public void T010StartLoadBalancer() throws Exception {
        mockMvc.perform(post("/api/loadBalancer/action/start/3/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T011GetLoadBalancer() throws Exception {
        mockMvc.perform(get("/api/loadBalancer/id/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
}
