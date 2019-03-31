/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mqtt.internal.homeassistant;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mqtt.values.NumberValue;
import org.openhab.binding.mqtt.values.TextValue;
import org.openhab.binding.mqtt.values.Value;

/**
 * A MQTT sensor, following the https://www.home-assistant.io/components/sensor.mqtt/ specification.
 *
 * @author David Graeff - Initial contribution
 */
@NonNullByDefault
public class ComponentSensor extends AbstractComponent<ComponentSensor.ChannelConfiguration> {
    public static final String sensorChannelID = "sensor"; // Randomly chosen channel "ID"

    /**
     * Configuration class for MQTT component
     */
    static class ChannelConfiguration extends BaseChannelConfiguration {
        ChannelConfiguration() {
            super("MQTT Sensor");
        }

        protected @Nullable String unit_of_measurement;
        protected @Nullable String device_class;
        protected boolean force_update = false;
        protected int expire_after = 0;

        protected String state_topic = "";

        protected @Nullable String json_attributes_topic;
    };

    public ComponentSensor(CFactory.ComponentConfiguration componentConfiguration) {
        super(componentConfiguration, ChannelConfiguration.class);

        if (channelConfiguration.force_update) {
            throw new UnsupportedOperationException("Component:Sensor does not support forced updates");
        }

        Value value;
        if (channelConfiguration.unit_of_measurement != null) {
            value = new NumberValue(null, null, null);
        } else {
            value = new TextValue();
        }
        buildChannel(sensorChannelID, value, "Sensor").listener(componentConfiguration.getUpdateListener())//
                .stateTopic(channelConfiguration.state_topic, channelConfiguration.value_template)//
                .unit(channelConfiguration.unit_of_measurement)//
                .build();
    }

}
