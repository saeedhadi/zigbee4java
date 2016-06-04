package org.bubblecloud.zigbee.network.zcl.protocol.command.identify;

import org.bubblecloud.zigbee.network.zcl.ZclCommandMessage;
import org.bubblecloud.zigbee.network.zcl.ZclUtil;
import org.bubblecloud.zigbee.network.zcl.ZclCommand;
import org.bubblecloud.zigbee.network.zcl.protocol.ZclCommandType;
import org.bubblecloud.zigbee.network.zcl.protocol.ZclFieldType;
import org.bubblecloud.zigbee.network.packet.ZToolAddress64;

/**
 * Code generated Identify Query Response Command value object class.
 */
public class IdentifyQueryResponseCommand extends ZclCommand {
    /**
     * Identify Time command message field.
     */
    private Short identifyTime;

    /**
     * Default constructor setting the command type field.
     */
    public IdentifyQueryResponseCommand() {
        this.setType(ZclCommandType.IDENTIFY_QUERY_RESPONSE_COMMAND);
    }

    /**
     * Constructor copying field values from command message.
     * @param message the command message
     */
    public IdentifyQueryResponseCommand(final ZclCommandMessage message) {
        super(message);
        this.identifyTime = (Short) message.getFields().get(ZclFieldType.IDENTIFY_QUERY_RESPONSE_COMMAND_IDENTIFY_TIME);
    }

    @Override
    public ZclCommandMessage toCommandMessage() {
        final ZclCommandMessage message = super.toCommandMessage();
        message.getFields().put(ZclFieldType.IDENTIFY_QUERY_RESPONSE_COMMAND_IDENTIFY_TIME,identifyTime);
        return message;
    }

    /**
     * Gets Identify Time.
     * @return the Identify Time
     */
    public Short getIdentifyTime() {
        return identifyTime;
    }

    /**
     * Sets Identify Time.
     * @param identifyTime the Identify Time
     */
    public void setIdentifyTime(final Short identifyTime) {
        this.identifyTime = identifyTime;
    }

    static {
        ZclUtil.registerCommandTypeClassMapping(ZclCommandType.IDENTIFY_QUERY_RESPONSE_COMMAND,IdentifyQueryResponseCommand.class);
    }
}
