/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package org.wso2.carbon.device.mgt.core.search.mgt.impl;

import org.wso2.carbon.device.mgt.common.device.details.DeviceWrapper;
import org.wso2.carbon.device.mgt.common.search.SearchContext;
import org.wso2.carbon.device.mgt.core.dao.DeviceManagementDAOFactory;
import org.wso2.carbon.device.mgt.core.search.mgt.*;
import org.wso2.carbon.device.mgt.core.search.mgt.dao.SearchDAO;
import org.wso2.carbon.device.mgt.core.search.mgt.dao.SearchDAOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessorImpl implements Processor {

    private SearchDAO searchDAO;

    public ProcessorImpl() {
        searchDAO = DeviceManagementDAOFactory.getSearchDAO();
    }

    @Override
    public List<DeviceWrapper> execute(SearchContext searchContext) throws SearchMgtException {

        QueryBuilder queryBuilder = new QueryBuilderImpl();
        List<DeviceWrapper> generalDevices = new ArrayList<>();
        List<List<DeviceWrapper>> allANDDevices = new ArrayList<>();
        List<List<DeviceWrapper>> allORDevices = new ArrayList<>();
        List<DeviceWrapper> locationDevices = new ArrayList<>();
        try {
            Map<String, List<String>> queries = queryBuilder.buildQueries(searchContext.getConditions());
            DeviceManagementDAOFactory.openConnection();

            if (queries.containsKey(Constants.GENERAL)) {
                generalDevices = searchDAO.searchDeviceDetailsTable(queries.get(Constants.GENERAL).get(0));
            }
            if (queries.containsKey(Constants.PROP_AND)) {
                for (String query : queries.get(Constants.PROP_AND)) {
                    List<DeviceWrapper> andDevices = searchDAO.searchDevicePropertyTable(query);
                    allANDDevices.add(andDevices);
                }
            }
            if (queries.containsKey(Constants.PROP_OR)) {
                for (String query : queries.get(Constants.PROP_OR)) {
                    List<DeviceWrapper> orDevices = searchDAO.searchDevicePropertyTable(query);
                    allORDevices.add(orDevices);
                }
            }
            if (queries.containsKey(Constants.LOCATION)) {
                locationDevices = searchDAO.searchDevicePropertyTable(
                        queries.get(Constants.LOCATION).get(0));
            }
        } catch (InvalidOperatorException e) {
            throw new SearchMgtException("Invalid operator was provided, so cannot execute the search.", e);
        } catch (SQLException e) {
            throw new SearchMgtException("Error occurred while managing database transactions.", e);
        } catch (SearchDAOException e) {
            throw new SearchMgtException("Error occurred while running the search operations.", e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }


        ResultSetAggregator aggregator = new ResultSetAggregatorImpl();

        Map<String, List<DeviceWrapper>> deviceWrappers = new HashMap<>();

        deviceWrappers.put(Constants.GENERAL, generalDevices);
        deviceWrappers.put(Constants.PROP_AND, this.processANDSearch(allANDDevices));
        deviceWrappers.put(Constants.PROP_OR, this.processORSearch(allORDevices));
        deviceWrappers.put(Constants.LOCATION, locationDevices);

        return aggregator.aggregate(deviceWrappers);
    }


    private List<DeviceWrapper> processANDSearch(List<List<DeviceWrapper>> deLists) {

        List<DeviceWrapper> devices = new ArrayList<>();
        List<DeviceWrapper> smallestDeviceList = this.findListWithLowestItems(deLists);
        List<Map<Integer, DeviceWrapper>> maps = this.convertDeviceListToMap(deLists);
        boolean valueExist = false;
        for (DeviceWrapper dw : smallestDeviceList) {
            for (Map<Integer, DeviceWrapper> deviceWrapperMap : maps) {
                if (deviceWrapperMap.containsKey(dw.getDevice().getId())) {
                    valueExist = true;
                } else {
                    valueExist = false;
                    break;
                }
            }
            if (valueExist) {
                devices.add(dw);
            }
        }
        return devices;
    }

    private List<DeviceWrapper> processORSearch(List<List<DeviceWrapper>> deLists) {
        List<DeviceWrapper> devices = new ArrayList<>();
        Map<Integer, DeviceWrapper> map = new HashMap<>();

        for (List<DeviceWrapper> list : deLists) {
            for (DeviceWrapper dw : list) {
                if (!map.containsKey(dw.getDevice().getId())) {
                    map.put(dw.getDevice().getId(), dw);
                    devices.add(dw);
                }
            }
        }
        return devices;
    }

    private List<DeviceWrapper> findListWithLowestItems(List<List<DeviceWrapper>> deLists) {

        int size = 0;
        List<DeviceWrapper> deviceWrappers = new ArrayList<>();
        for (List<DeviceWrapper> list : deLists) {
            if (size == 0) {
                size = list.size();
                deviceWrappers = list;
                continue;
            } else {
                if (list.size() < size) {
                    deviceWrappers = list;
                }
            }
        }
        return deviceWrappers;
    }

    private List<Map<Integer, DeviceWrapper>> convertDeviceListToMap(List<List<DeviceWrapper>> deLists) {

        List<Map<Integer, DeviceWrapper>> maps = new ArrayList<>();
        for (List<DeviceWrapper> deviceWrapperList : deLists) {
            Map<Integer, DeviceWrapper> deviceWrapperMap = new HashMap<>();

            for (DeviceWrapper dw : deviceWrapperList) {
                deviceWrapperMap.put(dw.getDevice().getId(), dw);
            }
            maps.add(deviceWrapperMap);
        }
        return maps;
    }

}

