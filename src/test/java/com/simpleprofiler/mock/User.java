package com.simpleprofiler.mock;

import com.simpleprofiler.annotation.Monitor;
import com.simpleprofiler.annotation.MonitorPolicy;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 20.12.2019
 **/

public class User
{
    @Monitor(MonitorPolicy.SHORT)
    private String getShortUserInfo(){
        return "anonymous";
    }

    @Monitor(MonitorPolicy.DETAILED)
    private String getDetailUserInfo(){
        return "anonymous";
    }

    @Monitor(MonitorPolicy.DETAILED)
    private String getUserInfoWithId(final int id){
        return "The User ID is " + id;
    }
}
