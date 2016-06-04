package org.bubblecloud.zigbee.network.zcl.protocol.command.groups;

import org.bubblecloud.zigbee.network.zcl.ZclCommandMessage;
import org.bubblecloud.zigbee.network.zcl.ZclUtil;
import org.bubblecloud.zigbee.network.zcl.ZclCommand;
import org.bubblecloud.zigbee.network.zcl.protocol.ZclCommandType;
import org.bubblecloud.zigbee.network.zcl.protocol.ZclFieldType;
import org.bubblecloud.zigbee.network.packet.ZToolAddress64;

/**
 * Code generated Get Group Membership Response Command value object class.
 */
public class GetGroupMembershipResponseCommand extends ZclCommand {
    /**
     * Capacity command message field.
     */
    private Byte capacity;
    /**
     * Group count command message field.
     */
    private Byte groupCount;
    /**
     * Group list command message field.
     */
    private Object groupList;

    /**
     * Default constructor setting the command type field.
     */
    public GetGroupMembershipResponseCommand() {
        this.setType(ZclCommandType.GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND);
    }

    /**
     * Constructor copying field values from command message.
     * @param message the command message
     */
    public GetGroupMembershipResponseCommand(final ZclCommandMessage message) {
        super(message);
        this.capacity = (Byte) message.getFields().get(ZclFieldType.GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND_CAPACITY);
        this.groupCount = (Byte) message.getFields().get(ZclFieldType.GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND_GROUP_COUNT);
        this.groupList = (Object) message.getFields().get(ZclFieldType.GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND_GROUP_LIST);
    }

    @Override
    public ZclCommandMessage toCommandMessage() {
        final ZclCommandMessage message = super.toCommandMessage();
        message.getFields().put(ZclFieldType.GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND_CAPACITY,capacity);
        message.getFields().put(ZclFieldType.GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND_GROUP_COUNT,groupCount);
        message.getFields().put(ZclFieldType.GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND_GROUP_LIST,groupList);
        return message;
    }

    /**
     * Gets Capacity.
     * @return the Capacity
     */
    public Byte getCapacity() {
        return capacity;
    }

    /**
     * Sets Capacity.
     * @param capacity the Capacity
     */
    public void setCapacity(final Byte capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets Group count.
     * @return the Group count
     */
    public Byte getGroupCount() {
        return groupCount;
    }

    /**
     * Sets Group count.
     * @param groupCount the Group count
     */
    public void setGroupCount(final Byte groupCount) {
        this.groupCount = groupCount;
    }

    /**
     * Gets Group list.
     * @return the Group list
     */
    public Object getGroupList() {
        return groupList;
    }

    /**
     * Sets Group list.
     * @param groupList the Group list
     */
    public void setGroupList(final Object groupList) {
        this.groupList = groupList;
    }

    static {
        ZclUtil.registerCommandTypeClassMapping(ZclCommandType.GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND,GetGroupMembershipResponseCommand.class);
    }
}
