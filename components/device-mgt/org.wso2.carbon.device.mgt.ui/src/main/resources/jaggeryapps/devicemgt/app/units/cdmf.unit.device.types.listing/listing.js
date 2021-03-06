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

function onRequest(context) {
    var log = new Log("listing.js");
    var DTYPE_CONF_DEVICE_TYPE_KEY = "deviceType";
    var DTYPE_CONF_DEVICE_CATEGORY = "category";
    var DTYPE_CONF_DEVICE_TYPE_LABEL_KEY = "label";

    var viewModel = {};
    var deviceModule = require("/app/modules/device.js").deviceModule;
    var utility = require("/app/modules/utility.js").utility;
    var data = deviceModule.getDeviceTypes();

    if (data.data) {
        var deviceTypes = data.data;
        var deviceTypesList = [], virtualDeviceTypesList = [];
        for (var i = 0; i < deviceTypes.length; i++) {

            var deviceTypeLabel = deviceTypes[i].name;
            var configs = utility.getDeviceTypeConfig(deviceTypeLabel);
            var deviceCategory = "device";
            if (configs) {
                if (configs[DTYPE_CONF_DEVICE_TYPE_KEY][DTYPE_CONF_DEVICE_CATEGORY]) {
                    deviceCategory = configs[DTYPE_CONF_DEVICE_TYPE_KEY][DTYPE_CONF_DEVICE_CATEGORY];
                }
                if (configs[DTYPE_CONF_DEVICE_TYPE_KEY][DTYPE_CONF_DEVICE_TYPE_LABEL_KEY]) {
                    deviceTypeLabel = configs[DTYPE_CONF_DEVICE_TYPE_KEY][DTYPE_CONF_DEVICE_TYPE_LABEL_KEY];
                }
            }
            //deviceTypesList.push({
            //    "hasCustTemplate": false,
            //    "deviceTypeLabel": deviceTypeLabel,
            //    "deviceTypeName": deviceTypes[i].name,
            //    "deviceCategory": deviceCategory,
            //    "deviceTypeId": deviceTypes[i].id
            //});
            if (deviceCategory == 'virtual'){
                virtualDeviceTypesList.push({
                    "hasCustTemplate": false,
                    "deviceTypeLabel": deviceTypeLabel,
                    "deviceTypeName": deviceTypes[i].name,
                    "deviceCategory": deviceCategory,
                    "deviceTypeId": deviceTypes[i].id
                });
            }else{
                deviceTypesList.push({
                    "hasCustTemplate": false,
                    "deviceTypeLabel": deviceTypeLabel,
                    "deviceTypeName": deviceTypes[i].name,
                    "deviceCategory": deviceCategory,
                    "deviceTypeId": deviceTypes[i].id
                });
            }
        }
        if (virtualDeviceTypesList.length > 0) {
            viewModel.virtualDeviceTypesList = virtualDeviceTypesList;
        }
        viewModel.deviceTypesList = stringify(deviceTypesList);
    } else {
        log.error("Unable to fetch device types data");
        throw new Error("Unable to fetch device types!");
    }
    return viewModel;
}