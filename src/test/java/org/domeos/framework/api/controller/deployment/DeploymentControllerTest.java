package org.domeos.framework.api.controller.deployment;

import org.apache.shiro.util.ThreadContext;
import org.domeos.base.BaseTestCase;
import org.domeos.basemodel.ResultStat;
import org.domeos.framework.api.consolemodel.deployment.*;
import org.domeos.framework.api.model.deployment.related.*;
import org.domeos.framework.api.model.loadBalancer.related.LoadBalancerPort;
import org.domeos.framework.api.model.loadBalancer.related.LoadBalancerProtocol;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by xxs on 16/2/1.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class DeploymentControllerTest extends BaseTestCase {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(InstanceServiceImpl.class);


    @Before
    public void setup() throws Exception {
        ThreadContext.bind(securityManager);
        this.mockMvc = webAppContextSetup(this.wac).build();
        login("admin", "admin");
    }

    @Test
    public void T0010CreateDeployCollection() throws Exception {
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deploy/deploycollection.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploy-collection----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploycollection").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T020ModifyDeployCollection() throws Exception {
        DeployCollectionDraft deployCollectionDraft = new DeployCollectionDraft();
        deployCollectionDraft.setCreatorId(1);
        deployCollectionDraft.setName("collection-test-2");
        deployCollectionDraft.setId(1);
        String deployCollectionDraftStr = objectMapper.writeValueAsString(deployCollectionDraft);
        mockMvc.perform(put("/api/deploycollection/{deployCollectionId}", 1).contentType(MediaType.APPLICATION_JSON).content(deployCollectionDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T0030CreateDeployment() throws Exception {
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft1.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T031CreateDeployment() throws Exception {
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft2.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T032StartDeployment() throws Exception {
        FileInputStream deploymentCollectionDraftInputStream = new FileInputStream("./src/test/resources/deploy/deploycollection.json");
        byte[] deploymentCollectionDraftBuff = new byte[deploymentCollectionDraftInputStream.available()];
        deploymentCollectionDraftInputStream.read(deploymentCollectionDraftBuff);
        String deploymentCollectionDraftStr = new String(deploymentCollectionDraftBuff);
        logger.info("----deploy-collection----" + deploymentCollectionDraftStr);
        mockMvc.perform(post("/api/deploycollection").contentType(MediaType.APPLICATION_JSON).content(deploymentCollectionDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft2.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/deploy/action/start").param("deployId", "1").param("version", "1")).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T033updateDeployment() throws Exception {
        FileInputStream deploymentCollectionDraftInputStream = new FileInputStream("./src/test/resources/deploy/deploycollection.json");
        byte[] deploymentCollectionDraftBuff = new byte[deploymentCollectionDraftInputStream.available()];
        deploymentCollectionDraftInputStream.read(deploymentCollectionDraftBuff);
        String deploymentCollectionDraftStr = new String(deploymentCollectionDraftBuff);
        logger.info("----deploy-collection----" + deploymentCollectionDraftStr);
        mockMvc.perform(post("/api/deploycollection").contentType(MediaType.APPLICATION_JSON).content(deploymentCollectionDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft2.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        FileInputStream versionInputStream = new FileInputStream("./src/test/resources/deployment/version2.json");
        byte[] versionDraftBuff = new byte[versionInputStream.available()];
        versionInputStream.read(versionDraftBuff);
        String versionDraftStr = new String(versionDraftBuff);
        logger.info("----version----" + versionDraftStr);
        mockMvc.perform(post("/api/version/create").param("deployId", "1").contentType(MediaType.APPLICATION_JSON).content(versionDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/deploy/action/update").param("deployId", "1").param("version", "2")).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T032CreateDeployment() throws Exception {
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft3.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T040GetDeployment() throws Exception {
//        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft3.json");
//        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
//        deploymentDraftInputStream.read(deploymentDraftBuff);
//        String deploymentDraftStr = new String(deploymentDraftBuff);
//        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
//        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
//                .andExpect(status().isOk());
        mockMvc.perform(get("/api/deploy/id/{deployId}", 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T050ListDeployment() throws Exception {
//        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft3.json");
//        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
//        deploymentDraftInputStream.read(deploymentDraftBuff);
//        String deploymentDraftStr = new String(deploymentDraftBuff);
//        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
//        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
//                .andExpect(status().isOk());
        mockMvc.perform(get("/api/deploy/list"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T060ListDeployment() throws Exception {
        mockMvc.perform(get("/api/deploy/list/{collectionId}", 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T070ModifyDeployment() throws Exception {
//        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft3.json");
//        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
//        deploymentDraftInputStream.read(deploymentDraftBuff);
//        String deploymentDraftStr = new String(deploymentDraftBuff);
//        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
//        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
//                .andExpect(status().isOk());
        mockMvc.perform(get("/api/deploy/id/{deployId}", 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());

        DeploymentDraft deploymentDraft = new DeploymentDraft();
        deploymentDraft.setAccessType(DeploymentAccessType.K8S_SERVICE);
        deploymentDraft.setClusterId(1);
        deploymentDraft.setCreateTime(System.currentTimeMillis());
        deploymentDraft.setDeployName("deploy2name");
        deploymentDraft.setExposePortNum(8);
        deploymentDraft.setHostEnv(HostEnv.PROD);
        deploymentDraft.setNamespace("default");
        deploymentDraft.setNetworkMode(NetworkMode.DEFAULT);
        deploymentDraft.setReplicas(33);
        deploymentDraft.setScalable(true);
        deploymentDraft.setStateful(true);

        List<String> volumes = new ArrayList<>();
        volumes.add("/opt/domeos/openxxs/docker:/opt/k8s");
        // deploymentDraft.setVolumes(volumes);

        LogDraft logDraft = new LogDraft();
        logDraft.setKafkaBrokers("domeos");
        List<LogItemDraft> logItemDrafts = new ArrayList<>();
        LogItemDraft logItemDraft = new LogItemDraft();
        logItemDraft.setAutoCollect(true);
        logItemDraft.setAutoDelete(true);
        logItemDraft.setLogExpired(3600);
        logItemDraft.setLogPath("/var/logs/docker");
        logItemDraft.setLogTopic("zookeeper");
        logItemDraft.setProcessCmd("|grep docker");
        logItemDrafts.add(logItemDraft);
        logDraft.setLogItemDrafts(logItemDrafts);
        ContainerDraft flumeContainerDraft = new ContainerDraft();
        flumeContainerDraft.setCpu(0.5);
        List<EnvDraft> envDrafts = new ArrayList<>();
        EnvDraft envDraft = new EnvDraft();
        envDraft.setKey("AN_ENV");
        envDraft.setValue("AN_ENV_VALUE");
        envDraft.setDescription("an env for flume");
        envDrafts.add(envDraft);
        flumeContainerDraft.setEnvs(envDrafts);
        List<EnvDraft> checkEnvDrafts = new ArrayList<>();
        EnvDraft checkEnvDraft = new EnvDraft();
        checkEnvDraft.setKey("AN_CHECK_ENV");
        checkEnvDraft.setValue("AN_CHECK_ENV_VALUE");
        checkEnvDraft.setDescription("an check env for flume");
        checkEnvDrafts.add(checkEnvDraft);
        flumeContainerDraft.setEnvCheckers(checkEnvDrafts);
        flumeContainerDraft.setImage("openxxs/flume");
        flumeContainerDraft.setMem(1024);
        flumeContainerDraft.setRegistry("10.11.150.76:5000");
        flumeContainerDraft.setTag("0.1");
        logDraft.setFlumeDraft(flumeContainerDraft);
        deploymentDraft.setLogDraft(logDraft);

        List<LoadBalancerPort> loadBalancerPorts = new ArrayList<LoadBalancerPort>();
        LoadBalancerForDeployDraft loadBalancerDraft = new LoadBalancerForDeployDraft();
        LoadBalancerPort loadBalancerPort = new LoadBalancerPort();
        loadBalancerPort.setPort(80);
        loadBalancerPort.setTargetPort(80);
        loadBalancerPort.setProtocol(LoadBalancerProtocol.TCP);
        loadBalancerPorts.add(loadBalancerPort);
        loadBalancerDraft.setLoadBalancerPorts(loadBalancerPorts);
        loadBalancerDraft.setSessionAffinity(false);
        deploymentDraft.setLoadBalancerDraft(loadBalancerDraft);

        List<LabelSelector> labelSelectors = new ArrayList<>();
        LabelSelector labelSelector = new LabelSelector();
        labelSelector.setName("alabel");
        labelSelector.setContent("ALABELCONTENT");
        labelSelectors.add(labelSelector);
        deploymentDraft.setLabelSelectors(labelSelectors);

        List<String> hosts = new ArrayList<>();
        hosts.add("10.16.42.198");
        deploymentDraft.setHostList(hosts);

        HealthChecker healthCheckerDraft = new HealthChecker();
        healthCheckerDraft.setType(HealthCheckerType.TCP);
        healthCheckerDraft.setUrl("10.16.42.201");
        healthCheckerDraft.setTimeout(300);
        healthCheckerDraft.setPort(8080);
        deploymentDraft.setHealthCheckerDraft(healthCheckerDraft);

        deploymentDraft.setCreatorId(1);

        List<ContainerConsole> deployContainerDrafts = new ArrayList<>();
        ContainerConsole deployContainerDraft = null;//new ContainerConsole();
        deployContainerDrafts.add(deployContainerDraft);
        deployContainerDraft.setCpu(10);
        List<EnvDraft> deployEnvs = new ArrayList<>();
        EnvDraft deployEnv = new EnvDraft();
        deployEnv.setKey("DEPLOY_ENV");
        deployEnv.setValue("DEPLOY_ENV_VALUE");
        deployEnv.setDescription("deploy env value");
        deployEnvs.add(deployEnv);
        deployContainerDraft.setEnvs(deployEnvs);
        List<EnvDraft> deployCheckEnvs = new ArrayList<>();
        EnvDraft deployCheckEnv = new EnvDraft();
        deployCheckEnv.setKey("DEPLOY_ENV");
        deployCheckEnv.setValue("DEPLOY_ENV_VALUE");
        deployCheckEnv.setDescription("deploy env value");
        deployCheckEnvs.add(deployCheckEnv);
        deployContainerDraft.setEnvCheckers(deployCheckEnvs);
        deployContainerDraft.setImage("10.11.150.76:5000/openxxs/iperf:1.0");
        deployContainerDraft.setMem(10);
        deployContainerDraft.setRegistry("domeos.org");
        deployContainerDraft.setTag("test-tag");
        deploymentDraft.setContainerConsoles(deployContainerDrafts);

        String newDeploymentDraftStr = objectMapper.writeValueAsString(deploymentDraft);
        mockMvc.perform(post("/api/deploy/id/1").contentType(MediaType.APPLICATION_JSON).content(newDeploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/deploy/id/{deployId}", 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T080RemoveDeployment() throws Exception {
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft3.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/deploy/id/{deployId}", 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/deploy/id/{deployId}", 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/deploy/id/{deployId}", 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.DEPLOYMENT_NOT_EXIST.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T090StartDeployment() throws Exception {
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft3.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/deploy/action/start").param("deployId", "1").param("version", "1").param("replicas", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T100StopDeployment() throws Exception {
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft3.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/deploy/action/start").param("deployId", "1").param("version", "1").param("replicas", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        Thread.sleep(20000);
        mockMvc.perform(post("/api/deploy/action/stop").param("deployId", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }


    @Test
    public void T110ListDeploymentEvent() throws Exception {
        FileInputStream deploymentDraftInputStream = new FileInputStream("./src/test/resources/deployment/deploymentDraft3.json");
        byte[] deploymentDraftBuff = new byte[deploymentDraftInputStream.available()];
        deploymentDraftInputStream.read(deploymentDraftBuff);
        String deploymentDraftStr = new String(deploymentDraftBuff);
        logger.info("----deploymentDraftStr----" + deploymentDraftStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(deploymentDraftStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/deploy/action/start").param("deployId", "1").param("version", "1").param("replicas", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        Thread.sleep(20000);
        mockMvc.perform(post("/api/deploy/action/stop").param("deployId", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
        Thread.sleep(20000);
        mockMvc.perform(get("/api/deploy/event/list").param("deployId", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T0120RemoveDeployCollection() throws Exception {
        mockMvc.perform(delete("/api/deploycollection/{collectionId}", 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }



    /*

    @Test
    public void T080Update() throws Exception {
        mockMvc.perform(post("/api/deploy/action/update").param("deployId", "1").param("versionId", "2").param("replicas", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T090Rollback() throws Exception {
        mockMvc.perform(post("/api/deploy/action/rollback").param("deployId", "1").param("versionId", "1").param("replicas", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T100Scaleup() throws Exception {
        mockMvc.perform(post("/api/deploy/action/scaleup").param("deployId", "1").param("versionId", "1").param("replicas", "2"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T110Scaledown() throws Exception {
        mockMvc.perform(post("/api/deploy/action/scaledown").param("deployId", "1").param("versionId", "1").param("replicas", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T120Scaledown() throws Exception {
        mockMvc.perform(post("/api/deploy/action/scaledown").param("deployId", "1").param("versionId", "1").param("replicas", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    */

    @Test
    public void T130crateYamlDeployment() throws Exception {
        FileInputStream podSpecYamlStream = new FileInputStream("./src/test/resources/Deployment/deploymentDraft4.json");
        byte[] podSpecYamlBuff = new byte[podSpecYamlStream.available()];
        podSpecYamlStream.read(podSpecYamlBuff);
        String podSpecYamlStr = new String(podSpecYamlBuff);
        System.out.println(podSpecYamlStr);
        mockMvc.perform(post("/api/deploy/create").contentType(MediaType.APPLICATION_JSON).content(podSpecYamlStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());

        //yamlTest.toPodSPec(podSpecYamlStr);

    }

}
