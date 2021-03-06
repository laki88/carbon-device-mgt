/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var groupModule = {};
(function (groupModule) {
    var log = new Log("/app/modules/group.js");

    var constants = require('/app/modules/constants.js');
    var devicemgtProps = require('/app/conf/devicemgt-props.js').config();
    var utility = require("/app/modules/utility.js").utility;
    var serviceInvokers = require("/app/modules/backend-service-invoker.js").backendServiceInvoker;

    var groupServiceEndpoint = devicemgtProps["httpsURL"] + constants.ADMIN_SERVICE_CONTEXT + "/groups";

    var user = session.get(constants.USER_SESSION_KEY);

    var endPoint;

    groupModule.getGroupCount = function () {
        endPoint = groupServiceEndpoint + "/user/" + user.username + "/count";
        return serviceInvokers.XMLHttp.get(
                endPoint, function (responsePayload) {
                    return responsePayload;
                }
                ,
                function (responsePayload) {
                    log.error(responsePayload);
                    return -1;
                }
        );
    };

    groupModule.getGroupDeviceCount = function (groupName, owner) {
        endPoint = groupServiceEndpoint + "/" + owner + "/" + groupName + "/devices/count";
        return serviceInvokers.XMLHttp.get(
                endPoint, function (responsePayload) {
                    return responsePayload;
                }
                ,
                function (responsePayload) {
                    log.error(responsePayload);
                    return -1;
                }
        );
    };

}(groupModule));
