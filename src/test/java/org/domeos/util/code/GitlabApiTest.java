package org.domeos.util.code;


import org.domeos.base.BaseTestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Created by kairen on 16-1-15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class GitlabApiTest extends BaseTestCase {
//    @Autowired
//    protected RSAKeyPairMapper rsaKeyPairMapper;
//
//    CodeApiInterface testsvnapi;
//
//    @Before
//    public void init(){
//        //login("admin", "admin");
//        testsvnapi = new GitlabApiWrapper("http://code.sohuno.com", "Q6k4quNvfmtmGb2rPcDa");
//        System.out.println("init");
//        for(int i = 900; i < 904; i++  )
//            create(4422, i);
//    }
//
//    public void create(int codeId, int projectId) {
//        //rsaKeyPairMapper.deleteRSAKeyPairByProjectId(project.getId());
//        RSAKeyPair keyPair = RSAKeyPairGenerator.generateKeyPair();
//        int keyId = testsvnapi.setDeployKey(codeId, "DomeOS", keyPair.getPublicKey());
//        if (keyId > 0) {
//            keyPair.setKeyId(keyId);
//            keyPair.setProjectId(projectId);
//            rsaKeyPairMapper.addRSAKeyPair(keyPair);
//        }
//    }
//
//    @After
//    public void clean(){
//        for(int i = 900; i < 904; i++ ) {
//            rsaKeyPairMapper.deleteRSAKeyPairByProjectId(i);
//        }
//        System.out.println("cleanout");
//
//    }
//
//    @Test
//    public void T010getDeployKey(){
//        for(int i = 900; i < 904; i++ ) {
//            RSAKeyPair keyPair = rsaKeyPairMapper.getRSAKeyPairByProjectId(i);
//            System.out.println("DeployId : " + keyPair.getKeyId());
//            System.out.println("Figerprint : " + keyPair.getFingerPrint());
//            System.out.println("ProjectId" + keyPair.getProjectId() );
//        }
//        testsvnapi.getDeployKey(4422);
//        for(int i = 900; i < 904; i++ ) {
//            RSAKeyPair keyPair = rsaKeyPairMapper.getRSAKeyPairByProjectId(i);
//            System.out.println("DeployId : " + keyPair.getKeyId());
//            System.out.println("Figerprint : " + keyPair.getFingerPrint());
//            System.out.println("ProjectId" + keyPair.getProjectId() );
//        }
//    }

}
