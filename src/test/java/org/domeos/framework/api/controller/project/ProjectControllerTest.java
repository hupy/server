package org.domeos.framework.api.controller.project;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.shiro.util.ThreadContext;
import org.domeos.base.BaseTestCase;
import org.domeos.basemodel.ResultStat;
import org.domeos.framework.api.consolemodel.project.ProjectCollectionConsole;
import org.domeos.framework.api.model.project.GitlabUser;
import org.domeos.framework.api.model.project.Project;
import org.domeos.framework.api.model.project.SubversionUser;
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
 * Created by feiliu206363 on 2016/4/7.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class ProjectControllerTest extends BaseTestCase {
    ProjectCollectionConsole projectCollectionConsole;
    String projectCollectionConsoleStr;


    Project project;
    String projectStr;

    //    ProjectCreate javaProject;
    Project javaProject;
    String javaProjectStr;

    GitlabUser gitlab;
    String gitlabStr;

    SubversionUser subversion;
    String subversionStr;

    @Before
    public void setup() throws IOException {
        ThreadContext.bind(securityManager);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        FileInputStream projectCollectionConsoleInputStream = new FileInputStream("./src/test/resources/project/projectCollection.json");
        byte[] buffProjectCollectionConsole = new byte[projectCollectionConsoleInputStream.available()];
        projectCollectionConsoleInputStream.read(buffProjectCollectionConsole);
        projectCollectionConsole = objectMapper.readValue(buffProjectCollectionConsole, ProjectCollectionConsole.class);
        projectCollectionConsoleStr = new String(buffProjectCollectionConsole);

        FileInputStream projectInputStream = new FileInputStream("./src/test/resources/project/project.json");
        byte[] buffProject = new byte[projectInputStream.available()];
        projectInputStream.read(buffProject);
        project = objectMapper.readValue(buffProject, Project.class);
        projectStr = new String(buffProject);

        FileInputStream gitlabInputStream = new FileInputStream("./src/test/resources/project/gitlab.json");
        byte[] buffGitlab = new byte[gitlabInputStream.available()];
        gitlabInputStream.read(buffGitlab);
        gitlab = objectMapper.readValue(buffGitlab, GitlabUser.class);
        gitlabStr = new String(buffGitlab);

        FileInputStream subversionInputStream = new FileInputStream("./src/test/resources/project/subversion.json");
        byte[] buffSubversion = new byte[subversionInputStream.available()];
        subversionInputStream.read(buffSubversion);
        subversion = objectMapper.readValue(buffSubversion, SubversionUser.class);
        subversionStr = new String(buffSubversion);

        this.mockMvc = webAppContextSetup(this.wac).build();
        login("admin", "admin");
    }

    @Test
    public void T001CreateCollection() throws Exception {
        mockMvc.perform(post("/api/projectcollection").contentType(MediaType.APPLICATION_JSON).content(projectCollectionConsoleStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T002ListCollection() throws Exception {
        mockMvc.perform(get("/api/projectcollection"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T003GetCollection() throws Exception {
        mockMvc.perform(get("/api/projectcollection/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void T004ModifyCollection() throws Exception {
        mockMvc.perform(put("/api/projectcollection").contentType(MediaType.APPLICATION_JSON).content(projectCollectionConsoleStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T010Create() throws Exception {
        mockMvc.perform(post("/api/projectcollection/1/project").contentType(MediaType.APPLICATION_JSON).content(projectStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T020Get() throws Exception {
        mockMvc.perform(get("/api/project/{id}", 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T030List() throws Exception {
        mockMvc.perform(get("/api/projectcollection/1/project"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T040Modify() throws Exception {
        mockMvc.perform(put("/api/project").contentType(MediaType.APPLICATION_JSON).content(projectStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

//    @Test
//    public void T050SetSvnInfo() throws Exception {
//        mockMvc.perform(post("/api/project/git/subversioninfo").contentType(MediaType.APPLICATION_JSON).content(subversionStr))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void T060SvnList() throws Exception {
//        mockMvc.perform(get("/api/project/git/subversioninfo"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
//                .andExpect(status().isOk());
//    }

    @Test
    public void T070GetBranches() throws Exception {
        mockMvc.perform(get("/api/project/branches/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T080GetTags() throws Exception {
        mockMvc.perform(get("/api/project/tags/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T080Delete() throws Exception {
        mockMvc.perform(delete("/api/project/{id}", project.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T090Create() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        FileInputStream javaProjectInputStream = new FileInputStream("./src/test/resources/project/javaProject.json");
        byte[] javaProjectBuff = new byte[javaProjectInputStream.available()];
        javaProjectInputStream.read(javaProjectBuff);
        javaProject = objectMapper.readValue(javaProjectBuff, Project.class);
        javaProjectStr = new String(javaProjectBuff);
        mockMvc.perform(post("/api/projectcollection/1/project").contentType(MediaType.APPLICATION_JSON).content(javaProjectStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T100Dockerfile() throws Exception {
        mockMvc.perform(post("/api/ci/build/dockerfile").contentType(MediaType.APPLICATION_JSON).content(gitlabStr))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T110GetBranches() throws Exception {
        mockMvc.perform(get("/api/project/branches/gitlab/4229/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void T120GetTags() throws Exception {

        mockMvc.perform(get("/api/project/tags/gitlab/4229/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ResultStat.OK.responseCode))
                .andExpect(status().isOk());
    }
}