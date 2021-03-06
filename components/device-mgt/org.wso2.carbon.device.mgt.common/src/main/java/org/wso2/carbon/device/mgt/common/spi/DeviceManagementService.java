/*
 *   Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.wso2.carbon.device.mgt.common.spi;

import org.wso2.carbon.device.mgt.common.DeviceIdentifier;
import org.wso2.carbon.device.mgt.common.DeviceManagementException;
import org.wso2.carbon.device.mgt.common.DeviceManager;
import org.wso2.carbon.device.mgt.common.app.mgt.ApplicationManager;
import org.wso2.carbon.device.mgt.common.operation.mgt.Operation;

import java.util.List;

/**
 * Composite interface that acts as the SPI exposing all device management as well as application management
 * functionalities.
 */
public interface DeviceManagementService extends ApplicationManager {

    /**
     * Method to retrieve the provider type that implements DeviceManager interface.
     *
     * @return Returns provider type
     */
    String getType();

    /**
     * This returns the tenant domain of the provider.
     * @return
     */
    String getProviderTenantDomain();

    /**
     * returns true if the device type is shared between all tenants and false if its not.
     *
     * @return
     */
    boolean isSharedWithAllTenants();

    void init() throws DeviceManagementException;

    DeviceManager getDeviceManager();

    ApplicationManager getApplicationManager();

    void notifyOperationToDevices(Operation operation, List<DeviceIdentifier> deviceIds) throws DeviceManagementException;

}
