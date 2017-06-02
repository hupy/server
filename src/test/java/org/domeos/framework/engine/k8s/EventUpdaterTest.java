package org.domeos.framework.engine.k8s;

import org.domeos.base.BaseTestCase;
import org.domeos.framework.api.biz.cluster.ClusterBiz;
import org.domeos.framework.api.model.cluster.Cluster;
import org.domeos.framework.engine.exception.DaoException;
import org.domeos.framework.engine.k8s.updater.EventUpdater;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * Created by xupeng on 16-4-6.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class EventUpdaterTest extends BaseTestCase {

    @Autowired
    EventUpdater eventUpdater;

    @Autowired
    ClusterBiz clusterBiz;

//    ClusterBasicMapper clusterBasicMapper;

    private Cluster buildClusterBasic(String clusterName, String api) {
        Cluster clusterBasic = new Cluster();
        clusterBasic.setApi(api);
        clusterBasic.setCreateTime(System.currentTimeMillis());
        clusterBasic.setName(clusterName);
        clusterBasic.setEtcd("10.16.42.200:4001");
        return clusterBasic;
    }

    @Test
    public void T010CheckCluster() throws InterruptedException, DaoException {
        Cluster cluster = buildClusterBasic("mycluster", "10.16.42.200:8080");
        clusterBiz.insertCluster(cluster);
//        eventUpdater.checkUpdateTask();
        TimeUnit.SECONDS.sleep(10);
        cluster = buildClusterBasic("mytest", "10.16.54.12:8080");
        clusterBiz.insertCluster(cluster);
        TimeUnit.SECONDS.sleep(10);
        clusterBiz.insertCluster(cluster);
        TimeUnit.SECONDS.sleep(10);

    }

//    @Test
//    public void T020init() {
//        System.out.println(SpringContextManager.getBean(AuthUtil.class));
//    }
}
