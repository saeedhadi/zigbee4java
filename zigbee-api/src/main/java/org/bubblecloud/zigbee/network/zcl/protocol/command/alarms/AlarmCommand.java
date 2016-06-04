package org.bubblecloud.zigbee.network.zcl.protocol.command.alarms;

import org.bubblecloud.zigbee.network.zcl.ZclCommandMessage;
import org.bubblecloud.zigbee.network.zcl.ZclUtil;
import org.bubblecloud.zigbee.network.zcl.ZclCommand;
import org.bubblecloud.zigbee.network.zcl.protocol.ZclCommandType;
import org.bubblecloud.zigbee.network.zcl.protocol.ZclFieldType;
import org.bubblecloud.zigbee.network.packet.ZToolAddress64;

/**
 * Code generated Alarm Command value object class.
 */
public class AlarmCommand extends ZclCommand {
    /**
     * Alarm code command message field.
     */
    private Byte alarmCode;
    /**
     * Cluster identifier command message field.
     */
    private Object clusterIdentifier;

    /**
     * Default constructor setting the command type field.
     */
    public AlarmCommand() {
        this.setType(ZclCommandType.ALARM_COMMAND);
    }

    /**
     * Constructor copying field values from command message.
     * @param message the command message
     */
    public AlarmCommand(final ZclCommandMessage message) {
        super(message);
        this.alarmCode = (Byte) message.getFields().get(ZclFieldType.ALARM_COMMAND_ALARM_CODE);
        this.clusterIdentifier = (Object) message.getFields().get(ZclFieldType.ALARM_COMMAND_CLUSTER_IDENTIFIER);
    }

    @Override
    public ZclCommandMessage toCommandMessage() {
        final ZclCommandMessage message = super.toCommandMessage();
        message.getFields().put(ZclFieldType.ALARM_COMMAND_ALARM_CODE,alarmCode);
        message.getFields().put(ZclFieldType.ALARM_COMMAND_CLUSTER_IDENTIFIER,clusterIdentifier);
        return message;
    }

    /**
     * Gets Alarm code.
     * @return the Alarm code
     */
    public Byte getAlarmCode() {
        return alarmCode;
    }

    /**
     * Sets Alarm code.
     * @param alarmCode the Alarm code
     */
    public void setAlarmCode(final Byte alarmCode) {
        this.alarmCode = alarmCode;
    }

    /**
     * Gets Cluster identifier.
     * @return the Cluster identifier
     */
    public Object getClusterIdentifier() {
        return clusterIdentifier;
    }

    /**
     * Sets Cluster identifier.
     * @param clusterIdentifier the Cluster identifier
     */
    public void setClusterIdentifier(final Object clusterIdentifier) {
        this.clusterIdentifier = clusterIdentifier;
    }

    static {
        ZclUtil.registerCommandTypeClassMapping(ZclCommandType.ALARM_COMMAND,AlarmCommand.class);
    }
}
