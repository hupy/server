"use strict";!function(e,t){void 0!==e&&e.controller("CreateProjectCtr1",["$scope","$state","$domeData","$modal","$domeProject","dialog","$sce","$domeGlobal",function(e,t,o,r,n,a,c,i){e.$emit("pageTitle",{title:"新建工程",descrition:"在这里把您的代码仓库和DomeOS对接即可创建新项目。此外，您还可以对现有项目进行查询和管理。",mod:"projectManage"}),e.pageNo=1,e.pageSize=8,e.projectList=[],e.creator={},e.codeManager="gitlab",e.projectCollectionId=t.params.projectCollectionId,e.projectCollectionId||t.go("projectCollectionManage"),n.projectService.getProjectCollectionNameById(e.projectCollectionId).then(function(t){e.projectCollectionName=t.data.result||""}),e.autoBuildInfo={tag:0,master:!1,other:!1,branches:""},e.role="user",e.projectType="commonconfig",e.projectTypeLanguage=["java"],e.projectTypeNotAllowedWOCodeManager=["dockerfileincode"].concat(e.projectTypeLanguage),e.currentProject={},e.isFromLastStep=!1;var l=angular.copy(o.getData("createProjectInfo1"));l&&(o.delData("createProjectInfo1"),l.info.codeInfo&&(e.currentProject=function(){var e=l.info.codeInfo;return{nameWithNamespace:e.nameWithNamespace,sshUrl:e.codeSshUrl,httpUrl:e.codeHttpUrl,projectId:e.codeId}}(),e.currentUserId=l.info.codeInfo.codeManagerUserId,e.runnersToken=l.info.codeInfo.runnersToken),e.codeManager=l.codeManager,e.currentGitLab=l.currentGitLab,e.gitLabInfo=l.gitLabInfo,e.gitLabList=l.gitLabList,e.isFromLastStep=l.isFromLastStep,e.projectName=l.info.name,l.info.autoBuildInfo?e.autoBuildInfo=l.info.autoBuildInfo:e.autoBuildInfo={tag:0,master:!1,other:!1,branches:""},e.projectType=l.projectType),e.setProjectList=function(t){e.pageNo=1,e.currentUserId=t.id,e.projectList=t.projectInfos},e.setCurrentProject=function(t){e.currentProject=t};!function(){if(e.isFromLastStep){if(e.currentUserId&&e.currentProject&&e.isFromLastStep)for(var t=0,o=e.gitLabInfo.length;t<o;t++)if(e.gitLabInfo[t].id===e.currentUserId){e.setProjectList(e.gitLabInfo[t]);for(var r=0,n=e.projectList.length;r<n;r++)if(e.projectList[r].projectId===e.currentProject.projectId){e.setCurrentProject(e.projectList[r]);break}break}}else i.getGloabalInstance("gitUser").getData().then(function(t){var o=t||{};e.gitLabList=o.gitConfigList;var r=null;e.gitLabList.length>0&&o.defaultGitlab?(e.gitLabList.forEach(function(e){if(e.id===o.defaultGitlab)return void(r=e)}),null!=r?e.toggleCodeManager(r,"gitlab"):e.toggleCodeManager(r,null)):e.toggleCodeManager(r,null)},function(t){a.error("拉取 GitLab 失败",t),e.toggleCodeManager(null,null)})}(),e.getGitLabInfo=function(t){e.isGitLabInfoLoading=!0,n.projectService.getGitLabInfo(t).then(function(t){e.gitLabInfo=t.data.result||[],e.gitLabInfo.length>0?e.setProjectList(e.gitLabInfo[0]):(e.pageNo=1,e.currentUserId=null,e.projectList=[])}).finally(function(){e.isGitLabInfoLoading=!1})},e.toggleCodeManager=function(t,o){null!==t?(e.currentGitLab=t,e.codeManager=o,e.isFromLastStep=!1,e.getGitLabInfo(e.currentGitLab.id)):(e.currentGitLab=t,e.codeManager=o),e.codeManager||e.projectTypeNotAllowedWOCodeManager.indexOf(e.projectType)===-1||(e.projectType="common"),e.$broadcast("changeScrollList",new Date)},e.toRelated=function(){r.open({templateUrl:"loginModal.html",controller:"LoginModalCtr",size:"md",resolve:{gitLab:function(){return e.currentGitLab}}}).result.then(function(){a.alert("提示","关联成功！"),e.isFromLastStep=!1,e.getGitLabInfo(e.currentGitLab.id)})},e.toNext=function(){if(e.codeManager&&!e.currentProject.projectId)return void a.error("警告","请选择一个项目！");var r={name:e.projectName,projectCollectionId:e.projectCollectionId,projectCollectionName:e.projectCollectionName,projectBelong:e.creator.name};e.codeManager&&(r.autoBuildInfo=e.autoBuildInfo,r.codeInfo={codeManager:e.codeManager,nameWithNamespace:e.currentProject.nameWithNamespace,codeSshUrl:e.currentProject.sshUrl,codeHttpUrl:e.currentProject.httpUrl,codeId:e.currentProject.projectId,codeManagerUserId:e.currentUserId,runnersToken:e.runnersToken}),o.setData("projectInfo",{codeManager:e.codeManager,info:r,projectType:e.projectType,gitLabInfo:e.gitLabInfo,isFromLastStep:!0,gitLabList:e.gitLabList,currentGitLab:e.currentGitLab}),t.go("createProject2",{projectCollectionId:e.projectCollectionId})},e.isDescriptionNull=function(e){var t=e;return e||(t="无描述信息"),t},e.modifyTooltip=function(t){if(t){var o=[];o.push('<div class="table-detail-wrap"><table class="table-detail" style="text-align:left">'),o.push("<tbody>"),o.push("<tr><td>httpUrl: </td><td>"+t.httpUrl+"</td>"),o.push("<tr><td>sshUrl: </td><td>"+t.sshUrl+"</td>"),o.push("</tbody>"),o.push("</table></div>"),e.toolTipText=c.trustAsHtml(o.join(""))}},e.modifyCodeInfo=function(t){r.open({templateUrl:"/index/tpl/modal/codeInfoModal/codeInfoModal.html",controller:"CodeInfoModalCtr as vmPro",size:"md",resolve:{project:function(){return t},showForm:function(){return"createProject"}}}).result.then(function(t){for(var o=0,r=e.projectList.length;o<r;o++)if(e.projectList[o].projectId===t.projectId){e.projectList[o]=t;break}e.setCurrentProject(t)})}}]).controller("LoginModalCtr",["$scope","$http","$modalInstance","$domeUser","gitLab",function(e,t,o,r,n){e.gitLab=n,e.toLogin=function(){e.errorTxt="",e.isWaiting=!0;var t=e.username.indexOf("@"),a=e.username;t!==-1&&(a=a.substring(0,t));var c={gitlabId:n.id,login:a,password:e.password};r.relatedGitLab(c).then(function(){o.close()},function(){e.errorTxt="关联失败，请重试！",e.isWaiting=!1})},e.close=function(){o.dismiss("cancel")}}])}(angular.module("domeApp"));