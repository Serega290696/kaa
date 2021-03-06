/*
 * Copyright 2014 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaaproject.kaa.server.common.dao.model.sql;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.kaaproject.kaa.common.dto.ConfigurationSchemaDto;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;

import static org.kaaproject.kaa.server.common.dao.DaoConstants.CONFIGURATION_SCHEMA_BASE_SCHEMA;
import static org.kaaproject.kaa.server.common.dao.DaoConstants.CONFIGURATION_SCHEMA_OVERRIDE_SCHEMA;
import static org.kaaproject.kaa.server.common.dao.DaoConstants.CONFIGURATION_SCHEMA_PROTOCOL_SCHEMA;
import static org.kaaproject.kaa.server.common.dao.DaoConstants.CONFIGURATION_SCHEMA_TABLE_NAME;

@Entity
@Table(name = CONFIGURATION_SCHEMA_TABLE_NAME)
@OnDelete(action = OnDeleteAction.CASCADE)
public final class ConfigurationSchema extends Schema<ConfigurationSchemaDto> implements Serializable {

    private static final long serialVersionUID = -8854035430683210037L;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = CONFIGURATION_SCHEMA_BASE_SCHEMA)
    private String baseSchema;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = CONFIGURATION_SCHEMA_PROTOCOL_SCHEMA)
    private String protocolSchema;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = CONFIGURATION_SCHEMA_OVERRIDE_SCHEMA)
    private String overrideSchema;

    public ConfigurationSchema() {
    }

    public ConfigurationSchema(Long id) {
        this.id = id;
    }

    public ConfigurationSchema(ConfigurationSchemaDto dto) {
        super(dto);
        if (dto != null) {
            this.baseSchema = dto.getBaseSchema();
            this.protocolSchema = dto.getProtocolSchema();
            this.overrideSchema = dto.getOverrideSchema();
        }
    }

    public String getBaseSchema() {
        return baseSchema;
    }

    public void setBaseSchema(String baseSchema) {
        this.baseSchema = baseSchema;
    }

    public String getProtocolSchema() {
        return protocolSchema;
    }

    public void setProtocolSchema(String protocolSchema) {
        this.protocolSchema = protocolSchema;
    }

    public String getOverrideSchema() {
        return overrideSchema;
    }

    public void setOverrideSchema(String overrideSchema) {
        this.overrideSchema = overrideSchema;
    }

    public String getApplicationId() {
        return application != null ? application.getStringId() : null;
    }

    @Override
    public ConfigurationSchemaDto toDto() {
        ConfigurationSchemaDto dto = super.toDto();
        dto.setProtocolSchema(protocolSchema);
        dto.setBaseSchema(baseSchema);
        dto.setOverrideSchema(overrideSchema);
        return dto;
    }

    @Override
    protected ConfigurationSchemaDto createDto() {
        return new ConfigurationSchemaDto();
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * result + super.hashCode();
        result = prime * result + ((baseSchema == null) ? 0 : baseSchema.hashCode());
        result = prime * result + ((protocolSchema == null) ? 0 : protocolSchema.hashCode());
        result = prime * result + ((overrideSchema == null) ? 0 : overrideSchema.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConfigurationSchema other = (ConfigurationSchema) obj;
        if (baseSchema == null) {
            if (other.baseSchema != null) {
                return false;
            }
        } else if (!baseSchema.equals(other.baseSchema)) {
            return false;
        }
        if (protocolSchema == null) {
            if (other.protocolSchema != null) {
                return false;
            }
        } else if (!protocolSchema.equals(other.protocolSchema)) {
            return false;
        }
        if (overrideSchema == null) {
            if (other.overrideSchema != null) {
                return false;
            }
        } else if (!overrideSchema.equals(other.overrideSchema)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConfigurationSchema [majorVersion=" + majorVersion + ", minorVersion=" + minorVersion + ", name=" + name + ", description="
                + description + ", createdUsername=" + createdUsername + ", createdTime=" + createdTime + ", endpointCount=" + endpointCount + ", id=" + id
                + "]";
    }

}
