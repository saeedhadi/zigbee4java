package org.bubblecloud.zigbee.network.zcl.field;

import org.bubblecloud.zigbee.api.cluster.impl.api.core.Status;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.ZBDeserializer;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.ZBSerializer;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.ZigBeeType;
import org.bubblecloud.zigbee.network.zcl.ZclField;

/**
 * Write Attribute Status Record field.
 */
public class WriteAttributeStatusRecord implements ZclField {
    /**
     * The status.
     */
    private int status;
    /**
     * The attribute identifier.
     */
    private int attributeIdentifier;

    /**
     * Gets attribute identifier.
     * @return the attribute identifier
     */
    public int getAttributeIdentifier() {
        return attributeIdentifier;
    }

    /**
     * Sets attribute identifier.
     * @param attributeIdentifier the attribute identifier
     */
    public void setAttributeIdentifier(int attributeIdentifier) {
        this.attributeIdentifier = attributeIdentifier;
    }

    /**
     * Gets status.
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets status
     * @param status the status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void serialize(final ZBSerializer serializer) {
        serializer.appendByte((byte) status);
        serializer.appendShort((short) attributeIdentifier);
    }

    @Override
    public void deserialize(final ZBDeserializer deserializer) {
        status = deserializer.read_byte() & (0xFF);
        attributeIdentifier = deserializer.read_short() & (0xFFFF);
    }

    @Override
    public String toString() {
        return "Write Attribute Status Record " +
                "status=" + status +
                ", attributeIdentifier=" + attributeIdentifier;
    }
}
