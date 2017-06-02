package org.domeos.util.code;


import org.domeos.framework.api.consolemodel.project.CodeSourceInfo;
import org.domeos.framework.api.mapper.domeos.project.SubversionUserMapper;
import org.domeos.framework.api.model.ci.CodeType;
import org.domeos.framework.api.model.project.SubversionUser;
import org.domeos.framework.api.service.project.impl.ProjectServiceImpl;
import org.domeos.framework.engine.coderepo.CodeApiInterface;
import org.domeos.framework.engine.coderepo.ReflectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.List;

/**
 * Created by kairen on 16-1-15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class SubversionApiTest {
    @Autowired
    protected SubversionUserMapper subversionUserMapper;
    @Autowired
    protected ProjectServiceImpl projectService;

    CodeApiInterface testsvnapi;

    @Before
    public void init() {
        SubversionUser svn = null;//new SubversionUser(20, "rkasdf", "123456", System.currentTimeMillis(), "svn://10.2.86.82/test123/trunk/ttttt");
        //subversionMapper.addSubversionInfo(svn);
        testsvnapi = ReflectFactory.createCodeApiInterface(CodeType.subversion.getCodeType(), 0);
        System.out.println("init");


    }

    @After
    public void clean() {
        subversionUserMapper.deleteSubversionInfoById(40);
        System.out.println("test");

    }

    @Test
    public void T010listEntires() {
        //testsvnapi.listEntries("");
    }

    @Test
    public void T020getFile() {
        // byte[]  dockerfile = testsvnapi.getFile("tomcat_docker/Dockerfile");

        //System.out.println(dockerfile);
    }

    @Test
    public void T030getNoFile() {
        //byte[] dockerfile = testsvnapi.getFile("tomcat_docker/dddd");
        //System.out.println(dockerfile);
    }

    @Test
    public void T040listCodeInfo() {
        List<CodeSourceInfo> testlist = testsvnapi.listCodeInfo(20);
        System.out.println(testlist);

    }

    @Test
    public void T050getCommitinfo() {
        testsvnapi.getCommitInfo(1, "");

    }
}
