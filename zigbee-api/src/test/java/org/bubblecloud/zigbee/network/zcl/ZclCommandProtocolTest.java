package org.bubblecloud.zigbee.network.zcl;

import org.bubblecloud.zigbee.api.cluster.impl.api.core.ZigBeeType;
import org.bubblecloud.zigbee.network.zcl.field.*;
import org.bubblecloud.zigbee.network.zcl.protocol.command.general.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlaukkan on 6/5/2016.
 */
public class ZclCommandProtocolTest {

    @Test
    public void testDiscoverAttributesCommand() throws Exception {
        final DiscoverAttributesCommand command = new DiscoverAttributesCommand();
        command.setStartAttributeIdentifier(0);
        command.setMaximumAttributeIdentifiers(100);
        testSerialization(command);
    }

    @Test
    public void testDiscoverAttributesResponseCommand() throws Exception {
        final DiscoverAttributesResponseCommand command = new DiscoverAttributesResponseCommand();
        command.setCommandIdentifier(true);
        final List<AttributeInformation> list = new ArrayList<AttributeInformation>();
        final AttributeInformation data = new AttributeInformation();
        data.setAttributeIdentifier(1);
        data.setAttributeDataType(2);
        list.add(data);
        command.setInformation(list);
        testSerialization(command);
    }

    @Test
    public void testReadAttributesCommand() throws Exception {
        final ReadAttributesCommand command = new ReadAttributesCommand();
        final List<AttributeIdentifier> list = new ArrayList<AttributeIdentifier>();
        final AttributeIdentifier data = new AttributeIdentifier();
        data.setAttributeIdentifier(1);
        list.add(data);
        command.setIdentifiers(list);
        testSerialization(command);
    }

    @Test
    public void testReadAttributesResponseCommand() throws Exception {
        final ReadAttributesResponseCommand command = new ReadAttributesResponseCommand();
        final List<ReadAttributeStatusRecord> list = new ArrayList<ReadAttributeStatusRecord>();
        final ReadAttributeStatusRecord data = new ReadAttributeStatusRecord();
        data.setAttributeIdentifier(1);
        data.setStatus(0);
        data.setAttributeDataType(ZigBeeType.UnsignedInteger8bit.getId());
        data.setAttributeValue(1);
        list.add(data);
        command.setRecords(list);
        testSerialization(command);
    }

    @Test
    public void testWriteAttributesCommand() throws Exception {
        final WriteAttributesCommand command = new WriteAttributesCommand();
        final List<WriteAttributeRecord> list = new ArrayList<WriteAttributeRecord>();
        final WriteAttributeRecord data = new WriteAttributeRecord();
        data.setAttributeIdentifier(1);
        data.setAttributeDataType(ZigBeeType.UnsignedInteger8bit.getId());
        data.setAttributeValue(1);
        list.add(data);
        command.setRecords(list);
        testSerialization(command);
    }

    @Test
    public void testWriteAttributesUndividedCommand() throws Exception {
        final WriteAttributesUndividedCommand command = new WriteAttributesUndividedCommand();
        final List<WriteAttributeRecord> list = new ArrayList<WriteAttributeRecord>();
        final WriteAttributeRecord data = new WriteAttributeRecord();
        data.setAttributeIdentifier(1);
        data.setAttributeDataType(ZigBeeType.UnsignedInteger8bit.getId());
        data.setAttributeValue(1);
        list.add(data);
        command.setRecords(list);
        testSerialization(command);
    }

    @Test
    public void testWriteAttributesNoResponseCommand() throws Exception {
        final WriteAttributesNoResponseCommand command = new WriteAttributesNoResponseCommand();
        final List<WriteAttributeRecord> list = new ArrayList<WriteAttributeRecord>();
        final WriteAttributeRecord data = new WriteAttributeRecord();
        data.setAttributeIdentifier(1);
        data.setAttributeDataType(ZigBeeType.UnsignedInteger8bit.getId());
        data.setAttributeValue(1);
        list.add(data);
        command.setRecords(list);
        testSerialization(command);
    }

    @Test
    public void testWriteAttributesResponseCommand() throws Exception {
        final WriteAttributesResponseCommand command = new WriteAttributesResponseCommand();
        final List<WriteAttributeStatusRecord> list = new ArrayList<WriteAttributeStatusRecord>();
        final WriteAttributeStatusRecord data = new WriteAttributeStatusRecord();
        data.setStatus(0);
        data.setAttributeIdentifier(1);
        list.add(data);
        command.setRecords(list);
        testSerialization(command);
    }

    @Test
    public void testConfigureReportingCommand() throws Exception {
        final ConfigureReportingCommand command = new ConfigureReportingCommand();
        final List<AttributeReportingConfigurationRecord> list = new ArrayList<AttributeReportingConfigurationRecord>();
        final AttributeReportingConfigurationRecord data = new AttributeReportingConfigurationRecord();
        data.setAttributeIdentifier(1);
        data.setMinimumReportingInterval(2);
        data.setMaximumReportingInterval(3);
        data.setAttributeDataType(ZigBeeType.UnsignedInteger8bit.getId());
        data.setReportableChange(1);
        data.setTimeoutPeriod(4);
        list.add(data);
        command.setRecords(list);
        testSerialization(command);
    }

    @Test
    public void testConfigureReportingResponseCommand() throws Exception {
        final ConfigureReportingResponseCommand command = new ConfigureReportingResponseCommand();
        final List<AttributeStatusRecord> list = new ArrayList<AttributeStatusRecord>();
        final AttributeStatusRecord data = new AttributeStatusRecord();
        data.setStatus(0);
        data.setDirection(true);
        data.setAttributeIdentifier(1);
        list.add(data);
        command.setRecords(list);
        testSerialization(command);
    }

    @Test
    public void testReadReportingConfigurationCommand() throws Exception {
        final ReadReportingConfigurationCommand command = new ReadReportingConfigurationCommand();
        final List<AttributeRecord> list = new ArrayList<AttributeRecord>();
        final AttributeRecord data = new AttributeRecord();
        data.setDirection(true);
        data.setAttributeIdentifier(1);
        list.add(data);
        command.setRecords(list);
        testSerialization(command);
    }

    @Test
    public void testReadReportingConfigurationResponseCommand() throws Exception {
        final ReadReportingConfigurationResponseCommand command = new ReadReportingConfigurationResponseCommand();
        final List<AttributeReportingConfigurationRecord> list = new ArrayList<AttributeReportingConfigurationRecord>();
        final AttributeReportingConfigurationRecord data = new AttributeReportingConfigurationRecord();
        data.setAttributeIdentifier(1);
        data.setMinimumReportingInterval(2);
        data.setMaximumReportingInterval(3);
        data.setAttributeDataType(ZigBeeType.UnsignedInteger8bit.getId());
        data.setReportableChange(1);
        data.setTimeoutPeriod(4);
        list.add(data);
        command.setRecords(list);
        testSerialization(command);
    }

    @Test
    public void testReportAttributesCommand() throws Exception {
        final ReportAttributesCommand command = new ReportAttributesCommand();
        final List<AttributeReport> list = new ArrayList<AttributeReport>();
        final AttributeReport data = new AttributeReport();
        data.setAttributeIdentifier(1);
        data.setAttributeDataType(ZigBeeType.UnsignedInteger8bit.getId());
        data.setAttributeValue(1);
        list.add(data);
        command.setReports(list);
        testSerialization(command);
    }

    /**
     * Tests command serialization.
     * @param command the command
     * @throws IOException if IO exception occurs.
     */
    private void testSerialization(final ZclCommand command) throws IOException {
        System.out.println(command);

        final ZclCommandMessage message1 = command.toCommandMessage();

        final byte[] payload = ZclCommandProtocol.serializePayload(message1);

        final ZclCommandMessage message2 = new ZclCommandMessage();
        message2.setType(message1.getType());

        ZclCommandProtocol.deserializePayload(payload, message2);

        final ZclCommand command2 = (ZclCommand) ZclUtil.toCommand(message2);

        Assert.assertEquals("Command equality after payload ZigBee serialization", command.toString(), command2.toString());

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        final String json = objectMapper.writeValueAsString(message1);
        System.out.println(json);
        final ZclCommandMessage message3 = objectMapper.readValue(json, ZclCommandMessage.class);

        Assert.assertEquals("Command equality after JSON serialization", message1.toString(), message3.toString());
    }

}
