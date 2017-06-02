CREATE TABLE IF NOT EXISTS `endpoint` (
  `id` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `endpoint` VARCHAR(255) NOT NULL DEFAULT '',
  `ts` INT(11) DEFAULT NULL,
  `t_create` DATETIME NOT NULL COMMENT 'create time',
  `t_modify` TIMESTAMP NOT NULL COMMENT 'last modify time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `idx_endpoint` ON endpoint(`endpoint`);

CREATE TABLE IF NOT EXISTS `endpoint_counter` (
  `id` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `endpoint_id` int(10) unsigned NOT NULL,
  `counter` varchar(255) NOT NULL DEFAULT '',
  `step` INT(11) not null default 60 comment 'in second',
  `type` VARCHAR(16) not null comment 'GAUGE|COUNTER|DERIVE',
  `ts` INT(11) DEFAULT NULL,
  `t_create` DATETIME NOT NULL COMMENT 'create time',
  `t_modify` TIMESTAMP NOT NULL COMMENT 'last modify time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `idx_endpoint_id_counter` ON endpoint_counter(`endpoint_id`, `counter`);

CREATE TABLE tag_endpoint (
  `id` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `tag` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'srv=tv',
  `endpoint_id` INT(10) NOT NULL,
  `ts` INT(11) DEFAULT NULL,
  `t_create` DATETIME NOT NULL COMMENT 'create time',
  `t_modify` TIMESTAMP NOT NULL COMMENT 'last modify time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `idx_tag_endpoint_id` ON tag_endpoint(`tag`, `endpoint_id`);

CREATE TABLE IF NOT EXISTS `action` (
  `id`                   INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `uic`                  VARCHAR(255)     NOT NULL DEFAULT '',
  `url`                  VARCHAR(255)     NOT NULL DEFAULT '',
  `callback`             TINYINT(4)       NOT NULL DEFAULT '0',
  `before_callback_sms`  TINYINT(4)       NOT NULL DEFAULT '0',
  `before_callback_mail` TINYINT(4)       NOT NULL DEFAULT '0',
  `after_callback_sms`   TINYINT(4)       NOT NULL DEFAULT '0',
  `after_callback_mail`  TINYINT(4)       NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `expression` (
  `id`          INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `expression`  VARCHAR(1024)    NOT NULL,
  `func`        VARCHAR(16)      NOT NULL DEFAULT 'all(#1)',
  `op`          VARCHAR(8)       NOT NULL DEFAULT '',
  `right_value` VARCHAR(16)      NOT NULL DEFAULT '',
  `max_step`    INT(11)          NOT NULL DEFAULT '1',
  `priority`    TINYINT(4)       NOT NULL DEFAULT '0',
  `note`        VARCHAR(1024)    NOT NULL DEFAULT '',
  `action_id`   INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `create_user` VARCHAR(64)      NOT NULL DEFAULT '',
  `pause`       TINYINT(1)       NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `grp` (
  id          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  grp_name    VARCHAR(255)     NOT NULL DEFAULT '',
  create_user VARCHAR(64)      NOT NULL DEFAULT '',
  create_at   TIMESTAMP        NOT NULL,
  come_from   TINYINT(4)       NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `idx_host_grp_grp_name` ON grp(`grp_name`);

CREATE TABLE IF NOT EXISTS `grp_host` (
  grp_id  INT UNSIGNED NOT NULL,
  host_id INT UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `idx_grp_host_grp_id` ON grp_host(`grp_id`);
CREATE INDEX `idx_grp_host_host_id` ON grp_host(`host_id`);

CREATE TABLE IF NOT EXISTS `grp_tpl` (
  `grp_id`    INT(10) UNSIGNED NOT NULL,
  `tpl_id`    INT(10) UNSIGNED NOT NULL,
  `bind_user` VARCHAR(64)      NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `idx_grp_tpl_grp_id` ON grp_tpl(`grp_id`);
CREATE INDEX `idx_grp_tpl_tpl_id` ON grp_tpl(`tpl_id`);

CREATE TABLE IF NOT EXISTS `host` (
  id             INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  hostname       VARCHAR(255) NOT NULL DEFAULT '',
  ip             VARCHAR(16)  NOT NULL DEFAULT '',
  agent_version  VARCHAR(16)  NOT NULL DEFAULT '',
  plugin_version VARCHAR(128) NOT NULL DEFAULT '',
  maintain_begin INT UNSIGNED NOT NULL DEFAULT 0,
  maintain_end   INT UNSIGNED NOT NULL DEFAULT 0,
  update_at      TIMESTAMP    NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `idx_host_hostname` ON host(`hostname`);

CREATE TABLE IF NOT EXISTS `mockcfg` (
  `id`       BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name`     VARCHAR(255)        NOT NULL DEFAULT ''
  COMMENT 'name of mockcfg, used for uuid',
  `obj`      VARCHAR(10240)      NOT NULL DEFAULT ''
  COMMENT 'desc of object',
  `obj_type` VARCHAR(255)        NOT NULL DEFAULT ''
  COMMENT 'type of object, host or group or other',
  `metric`   VARCHAR(128)        NOT NULL DEFAULT '',
  `tags`     VARCHAR(1024)       NOT NULL DEFAULT '',
  `dstype`   VARCHAR(32)         NOT NULL DEFAULT 'GAUGE',
  `step`     INT(11) UNSIGNED    NOT NULL DEFAULT 60,
  `mock`     DOUBLE              NOT NULL DEFAULT 0
  COMMENT 'mocked value when nodata occurs',
  `creator`  VARCHAR(64)         NOT NULL DEFAULT '',
  `t_create` DATETIME            NOT NULL
  COMMENT 'create time',
  `t_modify` TIMESTAMP           NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `uniq_name` ON mockcfg(`name`);

CREATE TABLE IF NOT EXISTS `plugin_dir` (
  `id`          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `grp_id`      INT(10) UNSIGNED NOT NULL,
  `dir`         VARCHAR(255)     NOT NULL,
  `create_user` VARCHAR(64)      NOT NULL DEFAULT '',
  `create_at`   TIMESTAMP        NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `idx_plugin_dir_grp_id` ON plugin_dir(`grp_id`);

CREATE TABLE `strategy` (
  `id`          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `metric`      VARCHAR(128)     NOT NULL DEFAULT '',
  `tags`        VARCHAR(256)     NOT NULL DEFAULT '',
  `max_step`    INT(11)          NOT NULL DEFAULT '1',
  `priority`    TINYINT(4)       NOT NULL DEFAULT '0',
  `func`        VARCHAR(16)      NOT NULL DEFAULT 'all(#1)',
  `op`          VARCHAR(8)       NOT NULL DEFAULT '',
  `right_value` VARCHAR(64)      NOT NULL,
  `note`        VARCHAR(128)     NOT NULL DEFAULT '',
  `run_begin`   VARCHAR(16)      NOT NULL DEFAULT '',
  `run_end`     VARCHAR(16)      NOT NULL DEFAULT '',
  `tpl_id`      INT(10) UNSIGNED NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `idx_strategy_tpl_id` ON strategy(`tpl_id`);

CREATE TABLE `tpl` (
  id          INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  tpl_name    VARCHAR(255) NOT NULL DEFAULT '',
  parent_id   INT UNSIGNED NOT NULL DEFAULT 0,
  action_id   INT UNSIGNED NOT NULL DEFAULT 0,
  create_user VARCHAR(64)  NOT NULL DEFAULT '',
  create_at   TIMESTAMP    NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `idx_tpl_create_user` ON tpl(`create_user`);
CREATE UNIQUE INDEX `idx_tpl_name` ON tpl(`tpl_name`);

-- domeos tables
CREATE TABLE IF NOT EXISTS `admin_roles` (
  `userId` INT(11) NOT NULL PRIMARY KEY,
  `role` VARCHAR(255) NOT NULL DEFAULT '0'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `build_history` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0',
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0',
  `data` MEDIUMTEXT NULL,
  `projectId` INT(11) NOT NULL,
  `secret` VARCHAR(255) NOT NULL,
  `log` LONGBLOB NULL,
  `taskName` VARCHAR(255) NULL DEFAULT NULL,
  `dockerfileContent` MEDIUMTEXT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `build_history_name` ON build_history(`name`);
CREATE INDEX `build_history_projectId` ON build_history(`projectId`);

CREATE TABLE IF NOT EXISTS `gitlab_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `userId` INT(11) NOT NULL DEFAULT '0',
  `name` VARCHAR(255) NOT NULL COMMENT 'username in gitlab',
  `token` VARCHAR(255) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0',
  `gitlabId` INT(11) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `gitlab_user_userId` ON gitlab_user(`userId`);

CREATE TABLE IF NOT EXISTS `operation_history` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `resourceId` INT(11) NOT NULL,
  `resourceType` VARCHAR(255) NOT NULL,
  `operation` VARCHAR(255) NOT NULL,
  `userId` INT(11) NOT NULL,
  `userName` VARCHAR(255) NOT NULL,
  `status` VARCHAR(255) NOT NULL,
  `message` MEDIUMTEXT NOT NULL,
  `operateTime` BIGINT(20) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `project` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL,
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0',
  `data` MEDIUMTEXT NULL,
  `authority` TINYINT(4) NOT NULL DEFAULT '0'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `project_name` ON project(`name`);
CREATE INDEX `project_authority` ON project(`authority`);

CREATE TABLE IF NOT EXISTS `project_rsakey_map` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `projectId` INT(11) NOT NULL,
  `rsaKeypairId` INT(11) NOT NULL,
  `keyId` INT(11) NOT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `project_rsakey_map_projectId` ON project_rsakey_map(`projectId`);
CREATE INDEX `project_rsakey_map_rsaKeypairId` ON project_rsakey_map(`rsaKeypairId`);

CREATE TABLE IF NOT EXISTS `rsa_keypair` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0',
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0',
  `data` MEDIUMTEXT NULL,
  `authority` INT(11) NOT NULL DEFAULT '0'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `rsa_keypair_name` ON rsa_keypair(`name`);

CREATE TABLE IF NOT EXISTS `subversion_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `userId` INT(11) NOT NULL DEFAULT '0',
  `name` VARCHAR(255) NOT NULL COMMENT 'username in subversion',
  `password` VARCHAR(255) NOT NULL,
  `svnPath` VARCHAR(255) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `subversion_user_userId` ON subversion_user(`userId`);

CREATE TABLE IF NOT EXISTS `users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `phone` VARCHAR(255) NULL DEFAULT NULL,
  `loginType` VARCHAR(255) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0',
  `updateTime` BIGINT(20) NOT NULL DEFAULT '0',
  `state` VARCHAR(128) NULL DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `users_state` ON users(`state`);
CREATE INDEX `users_username` ON users(`username`);

CREATE TABLE IF NOT EXISTS `deployment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0',
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0',
  `data` MEDIUMTEXT NULL,
  `clusterId` INT(11) DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `version` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) DEFAULT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) DEFAULT NULL,
  `removeTime` BIGINT(20) DEFAULT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0',
  `data` MEDIUMTEXT NULL,
  `deployId` INT(11) DEFAULT NULL,
  `version` BIGINT(20) DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `cluster` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) DEFAULT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) DEFAULT NULL,
  `removeTime` BIGINT(20) DEFAULT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0',
  `data` MEDIUMTEXT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `loadbalancer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `state` varchar(128) NOT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `removeTime` bigint(20) DEFAULT NULL,
  `removed` tinyint(4) NOT NULL DEFAULT '0',
  `data` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `loadbalancer_collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `state` varchar(128) NOT NULL,
  `createTime` bigint(20) NOT NULL,
  `removeTime` bigint(20) DEFAULT NULL,
  `removed` tinyint(4) NOT NULL DEFAULT '0',
  `data` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `loadbalancer_deploy_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createTime` bigint(20) DEFAULT NULL,
  `removeTime` bigint(20) DEFAULT NULL,
  `removed` tinyint(4) NOT NULL DEFAULT '0',
  `deployId` int(11) DEFAULT NULL,
  `loadBalancerId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `uniq_port_index` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loadBalancerId` int(11) NOT NULL,
  `port` int(11) NOT NULL,
  `clusterId` int(11) NOT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `removeTime` bigint(20) DEFAULT NULL,
  `removed` tinyint(4) DEFAULT '0',
  `ip` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `port_clusterId_index` (`port`,`clusterId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `base_images` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `imageName` VARCHAR(255) NOT NULL DEFAULT '0',
  `imageTag` VARCHAR(255) NULL DEFAULT '0',
  `registry` VARCHAR(255) NULL DEFAULT '0',
  `description` TEXT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `global` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `type` VARCHAR(255) NOT NULL,
  `value` VARCHAR(4096) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0',
  `lastUpdate` BIGINT(20) NOT NULL DEFAULT '0',
  `description` VARCHAR(1024) NULL DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `monitor_targets` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `target` VARCHAR(10240) NULL DEFAULT NULL,
  `create_time` DATETIME NULL DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

 CREATE TABLE IF NOT EXISTS `k8s_events` (
   `id` INT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `version` VARCHAR(255) NOT NULL,
   `clusterId` INT(11) NOT NULL,
   `deployId` INT(11) NOT NULL DEFAULT -1,
   `namespace` VARCHAR(255) NOT NULL,
   `eventKind` VARCHAR(255) NOT NULL,
   `name` VARCHAR(255) NOT NULL,
   `host` VARCHAR(255),
   `content` TEXT
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `k8s_events_kind_index` ON k8s_events(`clusterId`, `namespace`, `eventKind`);
CREATE INDEX `k8s_events_name_index` ON k8s_events(`clusterId`, `namespace`, `name`);
CREATE INDEX `k8s_events_host_index` ON k8s_events(`host`);
CREATE INDEX `k8s_events_deploy_index` ON k8s_events(`clusterId`, `namespace`, `deployId`);

 CREATE TABLE IF NOT EXISTS `deploy_event` (
  `eid` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `deployId` INT(11) DEFAULT NULL,
  `operation` VARCHAR(255) DEFAULT NULL,
  `eventStatus` VARCHAR(255) DEFAULT NULL,
  `statusExpire` BIGINT(20) DEFAULT NULL,
  `content` MEDIUMTEXT NULL,
   `startTime` BIGINT(20) DEFAULT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `deploy_event_start_time_index` ON deploy_event(`startTime`);


CREATE TABLE IF NOT EXISTS `project_collection` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0',
  `removeTime` BIGINT(20) NULL DEFAULT '0',
  `removed` TINYINT(4) NOT NULL DEFAULT '0',
  `data` MEDIUMTEXT NULL,
  `projectCollectionState` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `deploy_collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `state` varchar(128) NOT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `removeTime` bigint(20) DEFAULT NULL,
  `removed` tinyint(4) NOT NULL DEFAULT '0',
  `data` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm related tables: 11 in total
-- 2016.04.14

-- alarm event info
CREATE TABLE IF NOT EXISTS `alarm_event_info_draft` (
  `id` VARCHAR(64) NOT NULL PRIMARY KEY,
  `endpoint` VARCHAR(128) NULL DEFAULT NULL,
  `metric` VARCHAR(128) NULL DEFAULT NULL,
  `counter` VARCHAR(128) NULL DEFAULT NULL,
  `func` VARCHAR(128) NULL DEFAULT NULL,
  `left_value` VARCHAR(128) NULL DEFAULT NULL,
  `operator` VARCHAR(128) NULL DEFAULT NULL,
  `right_value` VARCHAR(128) NULL DEFAULT NULL,
  `note` VARCHAR(4096) NULL DEFAULT NULL,
  `max_step` INT(20) NULL DEFAULT NULL,
  `current_step` INT(20) NULL DEFAULT NULL,
  `priority` INT(20) NULL DEFAULT NULL,
  `status` VARCHAR(128) NULL DEFAULT NULL,
  `timestamp` INT(20) NULL DEFAULT NULL,
  `expression_id` INT(20) NULL DEFAULT NULL,
  `strategy_id` INT(20) NULL DEFAULT NULL,
  `template_id` INT(20) NULL DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm callback
CREATE TABLE IF NOT EXISTS `alarm_callback_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `url` VARCHAR(256) NULL DEFAULT NULL,
  `beforeCallbackSms` TINYINT(1) NULL DEFAULT NULL,
  `beforeCallbackMail` TINYINT(1) NULL DEFAULT NULL,
  `afterCallbackSms` TINYINT(1) NULL DEFAULT NULL,
  `afterCallbackMail` TINYINT(1) NULL DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm host info
CREATE TABLE IF NOT EXISTS `alarm_host_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `hostname` VARCHAR(128) NULL DEFAULT NULL,
  `ip` VARCHAR(128) NULL DEFAULT NULL,
  `cluster` VARCHAR(128) NULL DEFAULT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm host group info
CREATE TABLE IF NOT EXISTS `alarm_host_group_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `hostGroupName` VARCHAR(128) NULL DEFAULT NULL,
  `creatorId` INT(11) NULL DEFAULT NULL,
  `creatorName` VARCHAR(128) NULL DEFAULT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL,
  `updateTime` BIGINT(20) NULL DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm strategy info
CREATE TABLE IF NOT EXISTS `alarm_strategy_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `metric` VARCHAR(64) NULL DEFAULT NULL,
  `tag` VARCHAR(128) NULL DEFAULT NULL,
  `pointNum` INT(11) NULL DEFAULT NULL,
  `aggregateType` VARCHAR(64) NULL DEFAULT NULL,
  `operator` VARCHAR(64) NULL DEFAULT NULL,
  `rightValue` DOUBLE NULL DEFAULT NULL,
  `note` VARCHAR(1024) NULL DEFAULT NULL,
  `maxStep` INT(11) NULL DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm template info
CREATE TABLE IF NOT EXISTS `alarm_template_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `templateName` VARCHAR(64) NULL DEFAULT NULL,
  `templateType` VARCHAR(64) NULL DEFAULT NULL,
  `creatorId` INT(11) NULL DEFAULT NULL,
  `creatorName` VARCHAR(128) NULL DEFAULT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL,
  `updateTime` BIGINT(20) NULL DEFAULT NULL,
  `callbackId` INT(11) NULL DEFAULT NULL,
  `deployId` INT(11) NULL DEFAULT NULL,
  `isRemoved` TINYINT(4) NOT NULL DEFAULT '0'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm host group & host bind
CREATE TABLE IF NOT EXISTS `alarm_host_group_host_bind` (
  `hostGroupId` INT(11) NOT NULL,
  `hostId` INT(11) NOT NULL,
  `bindTime` BIGINT(20) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm template & host group bind
CREATE TABLE IF NOT EXISTS `alarm_template_host_group_bind` (
  `templateId` INT(11) NOT NULL,
  `hostGroupId` INT(11) NOT NULL,
  `bindTime` BIGINT(20) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm template & user group bind
CREATE TABLE IF NOT EXISTS `alarm_template_user_group_bind` (
  `templateId` INT(11) NOT NULL,
  `userGroupId` INT(11) NOT NULL,
  `bindTime` BIGINT(20) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm template & strategy bind
CREATE TABLE IF NOT EXISTS `alarm_template_strategy_bind` (
  `templateId` INT(11) NOT NULL,
  `strategyId` INT(11) NOT NULL,
  `bindTime` BIGINT(20) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm link info
CREATE TABLE IF NOT EXISTS `alarm_link_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `content` MEDIUMTEXT NULL DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm user group info & user group bind
CREATE TABLE IF NOT EXISTS `alarm_user_group_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `userGroupName` varchar(128) DEFAULT NULL,
  `creatorId` int(11) DEFAULT NULL,
  `creatorName` varchar(128) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `updateTime` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `alarm_user_group_info_userGroupName_pk_index` ON alarm_user_group_info(`userGroupName`);

CREATE TABLE IF NOT EXISTS `alarm_user_group_user_bind` (
  `userGroupId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `bindTime` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- add collection map
CREATE TABLE IF NOT EXISTS `collection_authority_map` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `collectionId` INT(11) NOT NULL COMMENT 'projectCollectionId or deployCollectionId or clusterId',
  `resourceType` VARCHAR(255) NOT NULL COMMENT 'PROJECT or DEPLOY or CLUSTER',
  `userId` INT(11) NOT NULL COMMENT 'userId',
  `role` VARCHAR(255) NOT NULL COMMENT 'role name',
  `updateTime` BIGINT(20) NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `collection_authority_map_resourceType_index` ON collection_authority_map(`resourceType`);
CREATE INDEX `ollection_authority_map_collectionId_index` ON collection_authority_map(`collectionId`);
CREATE INDEX `collection_authority_map_userId_index` ON collection_authority_map(`userId`);
CREATE UNIQUE INDEX `collection_authority_map_uniq` ON collection_authority_map(`collectionId`, `resourceType`, `userId`);

CREATE TABLE IF NOT EXISTS `collection_resource_map` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `resourceId` INT(11) NOT NULL COMMENT 'projectId or deployId',
  `creatorId` INT(11) NOT NULL COMMENT 'userId',
  `resourceType` VARCHAR(255) NOT NULL COMMENT 'PROJECT or DEPLOY or CLUSTER',
  `collectionId` INT(11) NOT NULL COMMENT 'projectCollectionId or deployCollectionId',
  `updateTime` BIGINT(20) NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX `collection_resource_map_resourceType_resourceId_index` ON collection_resource_map(`resourceType`, `resourceId`);
CREATE INDEX `collection_resource_map_collection_index` ON collection_resource_map(`collectionId`, `resourceType`, `resourceId`);

CREATE TABLE `storage_cluster` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NULL DEFAULT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL,
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `data` MEDIUMTEXT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `storage_volume` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NULL DEFAULT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL,
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `data` MEDIUMTEXT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `volume_deploy_map` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `volumeId` INT(11) NOT NULL,
  `deployId` INT(11) NOT NULL,
  `versionId` INT(11) NOT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `volume_deploy_map_uniq_index` ON volume_deploy_map(`volumeId`, `deployId`, `versionId`);

CREATE TABLE IF NOT EXISTS `clusterwatcher_deploy_map` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `clusterId` INT(11) NOT NULL,
  `deployId` INT(11) NOT NULL,
  `updateTime` BIGINT(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `clusterwatcher_deploy_map_uniq_index` ON clusterwatcher_deploy_map(`clusterId`);

CREATE TABLE IF NOT EXISTS `configuration_collection` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NULL DEFAULT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL,
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `data` MEDIUMTEXT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `configuration` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NULL DEFAULT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL,
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `data` MEDIUMTEXT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `configuration_deploy_map` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `configurationId` INT(11) NOT NULL,
  `deployId` INT(11) NOT NULL,
  `versionId` INT(11) NOT NULL,
  `createTime` BIGINT(20) NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX `configuration_deploy_map_uniq_index` ON configuration_deploy_map(`configurationId`, `deployId`, `versionId`);

-- test, test
insert into users(username, password, salt, loginType, state) VALUES ('admin','5fdf2372d4f23bdecfd2b8e8d7aacce1','0ea3abcf42700bb1bbcca6c27c92a821','USER','NORMAL');
insert into users(username, password, salt, loginType, state) VALUES ('test','060b125a2e2de865ccc0f06367bc3491','831d1215e1bcbe99419a3e8f88edb869','USER','NORMAL');
insert into users(username, password, salt, loginType, state) VALUES ('csf','a190c9f28a28a1d7667f50830d8bf6d5','db5474a6e19613a7081506e2910f5dc4','USER','NORMAL');
insert into global(type, value) VALUES ('LDAP_SERVER','ldap://ldap.sohu-inc.com:389');
insert into global(type, value) VALUES ('LDAP_PREFIX','@sohu-inc.com');
insert into global(type, value) VALUES ('SERVER','localhost:8080');
insert into global(type, value) VALUES ('GITLAB','http://code.sohuno.com');
insert into global(type, value) VALUES ('REGISTRY_URL','http://registry-in.beta.sohucs.com');
insert into global(type, value) VALUES ('BUILD_IMAGE','registry-in.beta.sohucs.com/domeos/build:0.3.2');
insert into global(type, value) VALUES ('PUBLIC_REGISTRY_URL','http://pub.domeos.org');
insert into global(type, value) VALUES ('CI_CLUSTER_ID','1');
insert into global(type, value) VALUES ('CI_CLUSTER_NAMESPACE','default');
insert into gitlab_user(userId, name, token) VALUES ('1','admin', 'E5nHzziCYs7G9Q8TjDSK');
INSERT INTO admin_roles(userId, role) VALUES ('1', 'admin');
INSERT INTO cluster(name, state, removed, data) VALUES ('mycluster', 'RUNNING', '0', '{"api":"10.16.42.205:8080", "ver":1, "fqcn": "org.domeos.framework.api.model.cluster.Cluster"}');

CREATE TABLE IF NOT EXISTS `testmodel1` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0',
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0',
  `data` MEDIUMTEXT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `testmodel2` (
  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `state` VARCHAR(128) NOT NULL,
  `createTime` BIGINT(20) NOT NULL DEFAULT '0',
  `removeTime` BIGINT(20) NULL DEFAULT NULL,
  `removed` TINYINT(4) NOT NULL DEFAULT '0',
  `data` MEDIUMTEXT NULL,
  `column1` INT(11)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;