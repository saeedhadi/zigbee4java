/*
   Copyright 2008-2013 CNR-ISTI, http://isti.cnr.it
   Institute of Information Science and Technologies
   of the Italian National Research Council


   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.bubblecloud.zigbee.proxy;

import org.bubblecloud.zigbee.ZigbeeProxyContext;
import org.bubblecloud.zigbee.network.ClusterListener;
import org.bubblecloud.zigbee.network.ClusterMessage;
import org.bubblecloud.zigbee.network.ZigBeeDevice;
import org.bubblecloud.zigbee.network.ZigBeeNode;
import org.bubblecloud.zigbee.network.impl.ZigBeeBasedriverException;
import org.bubblecloud.zigbee.proxy.cluster.api.ProfileConstants;
import org.bubblecloud.zigbee.proxy.cluster.glue.Cluster;
import org.bubblecloud.zigbee.proxy.cluster.glue.general.Alarms;
import org.bubblecloud.zigbee.proxy.cluster.glue.general.Basic;
import org.bubblecloud.zigbee.proxy.cluster.glue.general.DeviceTemperatureConfiguration;
import org.bubblecloud.zigbee.proxy.cluster.glue.general.Identify;
import org.bubblecloud.zigbee.proxy.cluster.glue.general.PowerConfiguration;
import org.bubblecloud.zigbee.proxy.cluster.api.core.Subscription;
import org.bubblecloud.zigbee.proxy.cluster.api.core.ZCLCluster;

import org.bubblecloud.zigbee.network.model.ProvidedClusterMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/**
 * This class represent a generic <b>Home Automation Device</b> as defined by the document:<br>
 * <i>ZigBee Home Automation</i> public release version 075123r01ZB
 * <br>
 * <b>NOTE</b>: In general the mapping between <b>ClusterId</b> and <b>Cluster</b> is defined<br>
 * by the profile, while the mapping between <b><i>Name</i></b> and <b>Cluster</b> is defined where the<br>
 * <b>Cluster</b> is defined: usually inside a <i>Cluster Library</i>
 *
 * @author <a href="mailto:stefano.lenzi@isti.cnr.it">Stefano "Kismet" Lenzi</a>
 * @author <a href="mailto:francesco.furfari@isti.cnr.it">Francesco Furfari</a>
 * @version $LastChangedRevision: 799 $ ($LastChangedDate: 2013-08-06 19:00:05 +0300 (Tue, 06 Aug 2013) $)
 * @since 0.1.0
 */
public abstract class DeviceProxyBase implements DeviceProxy {

    private final static Logger logger = LoggerFactory.getLogger(DeviceProxyBase.class);

    protected ZigBeeDevice device;
    private ZigbeeProxyContext context;


    private Cluster[] clusters;
    private int index;


    /*
     * Mandatory clusters common to all Home Automation devices
     */
    protected Basic basic;
    protected Identify identify;
    /*
     * Optional clusters common to all Home Automation devices
     */
    protected Alarms alarms;
    protected DeviceTemperatureConfiguration deviceTemperature;
    protected PowerConfiguration powerConfiguration;
    private final ProvidedClusterMode clusterMode;


    public DeviceProxyBase(ZigbeeProxyContext context, ZigBeeDevice device) throws ZigBeeHAException {
        this.device = device;
        this.context = context;

        final int size;
        clusterMode = ProvidedClusterMode.HomeAutomationProfileStrict;
        if (clusterMode == ProvidedClusterMode.HomeAutomationProfileStrict) {
            size = device.getInputClusters().length;
        } else {
            size = device.getInputClusters().length + device.getOutputClusters().length;
        }
        clusters = new Cluster[size];

        for (int i = 0; i < device.getInputClusters().length; i++) {
            addCluster(device.getInputClusters()[i]);
        }
        if (clusterMode != ProvidedClusterMode.HomeAutomationProfileStrict) {
            for (int i = 0; i < device.getOutputClusters().length; i++) {
                addCluster(device.getOutputClusters()[i]);
            }
        }

        basic = (Basic) getCluster(ProfileConstants.CLUSTER_ID_BASIC);
        identify = (Identify) getCluster(ProfileConstants.CLUSTER_ID_IDENTIFY);

        powerConfiguration = (PowerConfiguration) getCluster(ProfileConstants.CLUSTER_ID_POWER_CONFIGURATION);
        deviceTemperature = (DeviceTemperatureConfiguration) getCluster(ProfileConstants.CLUSTER_ID_DEVICE_TEMPERATURE_CONFIGURATION);
        alarms = (Alarms) getCluster(ProfileConstants.CLUSTER_ID_ALARMS);
    }

    public int getDeviceType() {
        return device.getDeviceId();
    }

    public abstract String getName();

    public int getEndPointId() {
        return device.getEndPoint();
    }

    public int getProfileId() {
        return device.getProfileId();
    }


    protected boolean isClusterValid(int clusterId, ProvidedClusterMode complainanceMode) {
        if (device.providesInputCluster(clusterId)) {
            return true;
        }
        return false;
    }

    /**
     * This method create a cluster for the DeviceProxy if and only if the cluster is actually supported by the device.
     * Depending on the current value of cluster mode it add or not the cluster
     * to the set of cluster which will be returned by executing the method {@link #getAvailableCluster()}
     *
     * @param clusterId the id of the cluster
     * @return {@link Cluster} if it is already present or if it has been added to the {@link DeviceProxyBase}
     * @throws ZigBeeHAException
     */
    protected Cluster addCluster(int clusterId) throws ZigBeeHAException {
        /*
         * Verify if the cluster has already been added. For example, when the HA Driver is working with
         * ProvidedClusterMode.EitherInputAndOutput mode the HA Driver adds either inputs and outputs cluster
         * to the DeviceProxyBase
         */
        final Cluster duplicated = getCluster(clusterId);
        if (duplicated != null) {
            /*logger.warn(
                    "Cluster {}/{} already added to this device. " +
                    "It may identifies an error in the definition of the device description",
                    duplicated.getName(), Integer.toHexString(clusterId)
            );*/
            return duplicated;
        }


        //TODO We should move this code in isClusterValid() method
        /*
         * We are trying to add a cluster which is not defined as input cluster and is optional then we are not going to add it
         */
        if (!device.providesInputCluster(clusterId) && getDescription().isOptional(clusterId)) {
            logger.warn(
                    "ZigBeeDevice with DeviceId={} of Home Automation profile " +
                            "implements the OPTINAL cluster {} ONLY AS OUTPUT instead of input " +
                            "it may identify an error either on the Driver description or in " +
                            "in the implementation of firmware of the physical device",
                    device.getDeviceId(), clusterId
            );
            return null;
        }

        /*
         * We are trying to add a cluster which is not defined as input cluster and is optional then we are not going to add it
         */
        if (!device.providesInputCluster(clusterId) && getDescription().isCustom(clusterId)) {
            //TODO check if exists custom add-on by using ProfileModule interface
            logger.warn(
                    "ZigBeeDevice with DeviceId={} of Home Automation profile " +
                            "implements a CUSTOM cluster {} but HA Driver does not support them yet",
                    device.getDeviceId(), clusterId
            );
            return null;
        }

        /*
         * This is the last case, when a Cluster is not defined as input but it is among the mandotory cluster of the device
         * so if ProvidedClusterMode.EitherInputAndOutput is set we will consider it as a firmware issue so we will add the cluster anyway
         */
        if (!device.providesInputCluster(clusterId) && getDescription().isMandatory(clusterId)) {
            logger.warn(
                    "ZigBeeDevice with DeviceId={} of Home Automation profile " +
                            "doesn't implement mandatory cluster {}", device.getDeviceId(), clusterId
            );
            if (device.providesOutputCluster(clusterId)) {
                logger.error(
                        "ZigBeeDevice with DeviceId={} of Home Automation profile " +
                                "implements the mandatory cluster {} as output instead of as input " +
                                "it may identify an error either on the Driver description or in " +
                                "in the implementation of firmware of the physical device",
                        device.getDeviceId(), clusterId
                );
            } else {
                logger.error(
                        "ZigBeeDevice with DeviceId={} of Home Automation profile " +
                                "doesn't implements the mandatory cluster {} neither as output " +
                                "nor as input it may identify an error either on the Driver " +
                                "description or in in the implementation of firmware of the " +
                                "physical device",
                        device.getDeviceId(), clusterId
                );
                return null;
            }
            if (clusterMode == ProvidedClusterMode.HomeAutomationProfileStrict) {
                logger.warn(
                        "The cluster {} of the device {} is PROVIDED AS OUTPUT instead of AS INPUT, " +
                                "if you want to add it anyway please change the value of the property {} " +
                                " from {} to {}", new Object[]{
                        clusterId,
                        device.getDeviceId(),
                        "undefined",
                        ProvidedClusterMode.HomeAutomationProfileStrict,
                        ProvidedClusterMode.EitherInputAndOutput
                }
                );
                return null;
            } else {
                logger.warn(
                        "The cluster {} of the device {} is PROVIDED AS OUTPUT instead of AS INPUT, " +
                                "it will be added anyway to the device due to value of the property {} " +
                                ", if you want to disable this change the value from {} to {}",
                        new Object[]{
                                clusterId,
                                device.getDeviceId(),
                                "undefined",
                                ProvidedClusterMode.EitherInputAndOutput,
                                ProvidedClusterMode.HomeAutomationProfileStrict
                        }
                );
            }
        }

        String key = ProfileConstants.PROFILE_ID_HOME_AUTOMATION + ":" + String.valueOf(clusterId);
        ClusterFactory factory = (ClusterFactory) context.getClusterFactory();
        Cluster cluster = factory.getInstance(key, device);
        if (index >= clusters.length) {
            logger.error(
                    "Device {} cluster {}. More than expected number of clusters. Skipping.",
                    device.getDeviceId(), clusterId
            );
            return null;
        }
        if (cluster == null) {
            logger.error(
                    "Cluster {} for device {} not constructed by factory.",
                    Integer.toHexString(clusterId), device.getDeviceId()
            );
            return null;
        }
        logger.info(
                "Cluster {} - {} added to {} device proxy.",
                Integer.toHexString(clusterId), cluster.getName(), device.getDeviceId()
        );

        clusters[index++] = cluster;
        return cluster;
    }

    public Basic getBasic() {
        return basic;
    }


    public Identify getIdentify() {
        return identify;
    }

    public PowerConfiguration getPowerConfiguration() {
        return powerConfiguration;
    }

    public DeviceTemperatureConfiguration getDeviceTemperatureConfiguration() {
        return deviceTemperature;
    }

    public Alarms getAlarms() {
        return alarms;
    }


    public abstract DeviceDescription getDescription();

    public Cluster getCluster(int id) {
        for (int i = 0; i < clusters.length; i++) {
            if (clusters[i] != null && clusters[i].getId() == id)
                return clusters[i];
        }
        return null;
    }


    public <T extends Cluster> T getCluster(Class<T> clusterIntercace) {
        for (int i = 0; i < clusters.length; i++) {
            if (clusters[i] != null && Arrays.asList(clusters[i].getClass().getInterfaces()).contains(clusterIntercace))
                return (T) clusters[i];
        }
        return null;
    }

    /**
     * @param name the {@link String} representing the name associated to <b>ClusterId</b>
     * @return the {@link ZCLCluster} identified by the given name if this device implements<br>
     *         otherwise <code>null</code>
     */
    public Cluster getCluster(String name) {
        for (int i = 0; i < clusters.length; i++) {
            if (clusters[i] != null && clusters[i].getName().equals(name))
                return clusters[i];
        }
        return null;
    }

    public Cluster[] getAvailableCluster() {
        return clusters;
    }

    public void stop() {
        for (int i = 0; i < clusters.length; i++) {
            if (clusters[i] == null) continue;

            Subscription[] subscriptions = clusters[i].getActiveSubscriptions();
            if (subscriptions == null) continue;

            for (int j = 0; j < subscriptions.length; j++) {
                if (subscriptions[j] == null) continue;
                subscriptions[j].clear();
            }
        }
    }

    public ZigBeeDevice getDevice() {
        return device;
    }

    @Override
    public boolean bindTo(DeviceProxy deviceProxy, int clusterId) throws ZigBeeBasedriverException {
        return device.bindTo(deviceProxy.getDevice(), clusterId);
    }

    @Override
    public boolean unbindFrom(DeviceProxy deviceProxy, int clusterId) throws ZigBeeBasedriverException {
        return device.unbindFrom(deviceProxy.getDevice(), clusterId);
    }

    @Override
    public short getEndPoint() {
        return device.getEndPoint();
    }

    @Override
    public int getDeviceId() {
        return device.getDeviceId();
    }

    @Override
    public ZigBeeNode getPhysicalNode() {
        return device.getPhysicalNode();
    }

    @Override
    public String getUniqueIdenfier() {
        return device.getUniqueIdenfier();
    }

    @Override
    public short getDeviceVersion() {
        return device.getDeviceVersion();
    }

    @Override
    public int[] getInputClusters() {
        return device.getInputClusters();
    }

    @Override
    public boolean providesInputCluster(int id) {
        return device.providesInputCluster(id);
    }

    @Override
    public int[] getOutputClusters() {
        return device.getOutputClusters();
    }

    @Override
    public boolean providesOutputCluster(int id) {
        return device.providesOutputCluster(id);
    }

    @Override
    public ClusterMessage invoke(ClusterMessage input) throws ZigBeeBasedriverException {
        return device.invoke(input);
    }

    @Override
    public void send(ClusterMessage input) throws ZigBeeBasedriverException {
        device.send(input);
    }

    @Override
    public boolean bindTo(ZigBeeDevice device, int clusterId) throws ZigBeeBasedriverException {
        return device.bindTo(device, clusterId);
    }

    @Override
    public boolean unbindFrom(ZigBeeDevice device, int clusterId) throws ZigBeeBasedriverException {
        return device.unbindFrom(device, clusterId);
    }

    @Override
    public boolean bind(int clusterId) throws ZigBeeBasedriverException {
        return device.bind(clusterId);
    }

    @Override
    public boolean unbind(int clusterId) throws ZigBeeBasedriverException {
        return device.unbind(clusterId);
    }

    @Override
    public boolean addClusterListener(ClusterListener listener) {
        return device.addClusterListener(listener);
    }

    @Override
    public boolean removeClusterListener(ClusterListener listener) {
        return device.removeClusterListener(listener);
    }
}
