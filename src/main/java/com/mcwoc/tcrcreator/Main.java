package com.mcwoc.tcrcreator;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tcr.v20190924.TcrClient;
import com.tencentcloudapi.tcr.v20190924.models.CreateRepositoryPersonalRequest;
import com.tencentcloudapi.tcr.v20190924.models.CreateRepositoryPersonalResponse;

public class Main {
    public static void main(String[] args) {
        String id = System.getenv("TCR_SECRET_ID");
        String key = System.getenv("TCR_SECRET_KEY");
        String region = System.getenv("TCR_REGION");
        String namespace = System.getenv("TCR_NAMESPACE");
        String repoName = System.getenv("TCR_REPONAME");

        if(id == null || id.isEmpty())
            throw new IllegalArgumentException("TCR_SECRET_ID cannot be null/empty");
        if(key == null || key.isEmpty())
            throw new IllegalArgumentException("TCR_SECRET_KEY cannot be null/empty");
        if(repoName == null || repoName.isEmpty())
            throw new IllegalArgumentException("This util can only be run in Gitlab CI env.");
        if(region == null || region.isEmpty())
            region = "ap-nanjing";
        if(namespace == null || namespace.isEmpty())
            namespace = "mcxk";
        try{
            Credential cred = new Credential(id, key);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tcr.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            TcrClient client = new TcrClient(cred, region, clientProfile);
            CreateRepositoryPersonalRequest req = new CreateRepositoryPersonalRequest();
            req.setRepoName(namespace+"/"+repoName);
            req.setPublic(0L);
            CreateRepositoryPersonalResponse resp = client.CreateRepositoryPersonal(req);
            System.out.println(CreateRepositoryPersonalResponse.toJsonString(resp));
            System.exit(0);
        } catch (TencentCloudSDKException ignored) {
        }
    }
}
