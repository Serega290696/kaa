/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.kaaproject.kaa.server.common.log.shared.avro.gen;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class RecordHeader extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"RecordHeader\",\"namespace\":\"org.kaaproject.kaa.server.common.log.shared.avro.gen\",\"fields\":[{\"name\":\"endpointKeyHash\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"]},{\"name\":\"applicationToken\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"]},{\"name\":\"headerVersion\",\"type\":[\"int\",\"null\"]},{\"name\":\"timestamp\",\"type\":[\"long\",\"null\"]},{\"name\":\"logSchemaVersion\",\"type\":[\"int\",\"null\"]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
   private java.lang.String endpointKeyHash;
   private java.lang.String applicationToken;
   private java.lang.Integer headerVersion;
   private java.lang.Long timestamp;
   private java.lang.Integer logSchemaVersion;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use {@link \#newBuilder()}. 
   */
  public RecordHeader() {}

  /**
   * All-args constructor.
   */
  public RecordHeader(java.lang.String endpointKeyHash, java.lang.String applicationToken, java.lang.Integer headerVersion, java.lang.Long timestamp, java.lang.Integer logSchemaVersion) {
    this.endpointKeyHash = endpointKeyHash;
    this.applicationToken = applicationToken;
    this.headerVersion = headerVersion;
    this.timestamp = timestamp;
    this.logSchemaVersion = logSchemaVersion;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return endpointKeyHash;
    case 1: return applicationToken;
    case 2: return headerVersion;
    case 3: return timestamp;
    case 4: return logSchemaVersion;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: endpointKeyHash = (java.lang.String)value$; break;
    case 1: applicationToken = (java.lang.String)value$; break;
    case 2: headerVersion = (java.lang.Integer)value$; break;
    case 3: timestamp = (java.lang.Long)value$; break;
    case 4: logSchemaVersion = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'endpointKeyHash' field.
   */
  public java.lang.String getEndpointKeyHash() {
    return endpointKeyHash;
  }

  /**
   * Sets the value of the 'endpointKeyHash' field.
   * @param value the value to set.
   */
  public void setEndpointKeyHash(java.lang.String value) {
    this.endpointKeyHash = value;
  }

  /**
   * Gets the value of the 'applicationToken' field.
   */
  public java.lang.String getApplicationToken() {
    return applicationToken;
  }

  /**
   * Sets the value of the 'applicationToken' field.
   * @param value the value to set.
   */
  public void setApplicationToken(java.lang.String value) {
    this.applicationToken = value;
  }

  /**
   * Gets the value of the 'headerVersion' field.
   */
  public java.lang.Integer getHeaderVersion() {
    return headerVersion;
  }

  /**
   * Sets the value of the 'headerVersion' field.
   * @param value the value to set.
   */
  public void setHeaderVersion(java.lang.Integer value) {
    this.headerVersion = value;
  }

  /**
   * Gets the value of the 'timestamp' field.
   */
  public java.lang.Long getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the 'timestamp' field.
   * @param value the value to set.
   */
  public void setTimestamp(java.lang.Long value) {
    this.timestamp = value;
  }

  /**
   * Gets the value of the 'logSchemaVersion' field.
   */
  public java.lang.Integer getLogSchemaVersion() {
    return logSchemaVersion;
  }

  /**
   * Sets the value of the 'logSchemaVersion' field.
   * @param value the value to set.
   */
  public void setLogSchemaVersion(java.lang.Integer value) {
    this.logSchemaVersion = value;
  }

  /** Creates a new RecordHeader RecordBuilder */
  public static org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder newBuilder() {
    return new org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder();
  }
  
  /** Creates a new RecordHeader RecordBuilder by copying an existing Builder */
  public static org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder newBuilder(org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder other) {
    return new org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder(other);
  }
  
  /** Creates a new RecordHeader RecordBuilder by copying an existing RecordHeader instance */
  public static org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder newBuilder(org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader other) {
    return new org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder(other);
  }
  
  /**
   * RecordBuilder for RecordHeader instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<RecordHeader>
    implements org.apache.avro.data.RecordBuilder<RecordHeader> {

    private java.lang.String endpointKeyHash;
    private java.lang.String applicationToken;
    private java.lang.Integer headerVersion;
    private java.lang.Long timestamp;
    private java.lang.Integer logSchemaVersion;

    /** Creates a new Builder */
    private Builder() {
      super(org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.endpointKeyHash)) {
        this.endpointKeyHash = data().deepCopy(fields()[0].schema(), other.endpointKeyHash);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.applicationToken)) {
        this.applicationToken = data().deepCopy(fields()[1].schema(), other.applicationToken);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.headerVersion)) {
        this.headerVersion = data().deepCopy(fields()[2].schema(), other.headerVersion);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[3].schema(), other.timestamp);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.logSchemaVersion)) {
        this.logSchemaVersion = data().deepCopy(fields()[4].schema(), other.logSchemaVersion);
        fieldSetFlags()[4] = true;
      }
    }
    
    /** Creates a Builder by copying an existing RecordHeader instance */
    private Builder(org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader other) {
            super(org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.SCHEMA$);
      if (isValidValue(fields()[0], other.endpointKeyHash)) {
        this.endpointKeyHash = data().deepCopy(fields()[0].schema(), other.endpointKeyHash);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.applicationToken)) {
        this.applicationToken = data().deepCopy(fields()[1].schema(), other.applicationToken);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.headerVersion)) {
        this.headerVersion = data().deepCopy(fields()[2].schema(), other.headerVersion);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[3].schema(), other.timestamp);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.logSchemaVersion)) {
        this.logSchemaVersion = data().deepCopy(fields()[4].schema(), other.logSchemaVersion);
        fieldSetFlags()[4] = true;
      }
    }

    /** Gets the value of the 'endpointKeyHash' field */
    public java.lang.String getEndpointKeyHash() {
      return endpointKeyHash;
    }
    
    /** Sets the value of the 'endpointKeyHash' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder setEndpointKeyHash(java.lang.String value) {
      validate(fields()[0], value);
      this.endpointKeyHash = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'endpointKeyHash' field has been set */
    public boolean hasEndpointKeyHash() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'endpointKeyHash' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder clearEndpointKeyHash() {
      endpointKeyHash = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'applicationToken' field */
    public java.lang.String getApplicationToken() {
      return applicationToken;
    }
    
    /** Sets the value of the 'applicationToken' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder setApplicationToken(java.lang.String value) {
      validate(fields()[1], value);
      this.applicationToken = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'applicationToken' field has been set */
    public boolean hasApplicationToken() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'applicationToken' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder clearApplicationToken() {
      applicationToken = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'headerVersion' field */
    public java.lang.Integer getHeaderVersion() {
      return headerVersion;
    }
    
    /** Sets the value of the 'headerVersion' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder setHeaderVersion(java.lang.Integer value) {
      validate(fields()[2], value);
      this.headerVersion = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'headerVersion' field has been set */
    public boolean hasHeaderVersion() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'headerVersion' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder clearHeaderVersion() {
      headerVersion = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'timestamp' field */
    public java.lang.Long getTimestamp() {
      return timestamp;
    }
    
    /** Sets the value of the 'timestamp' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder setTimestamp(java.lang.Long value) {
      validate(fields()[3], value);
      this.timestamp = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'timestamp' field has been set */
    public boolean hasTimestamp() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'timestamp' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder clearTimestamp() {
      timestamp = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'logSchemaVersion' field */
    public java.lang.Integer getLogSchemaVersion() {
      return logSchemaVersion;
    }
    
    /** Sets the value of the 'logSchemaVersion' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder setLogSchemaVersion(java.lang.Integer value) {
      validate(fields()[4], value);
      this.logSchemaVersion = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'logSchemaVersion' field has been set */
    public boolean hasLogSchemaVersion() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'logSchemaVersion' field */
    public org.kaaproject.kaa.server.common.log.shared.avro.gen.RecordHeader.Builder clearLogSchemaVersion() {
      logSchemaVersion = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    public RecordHeader build() {
      try {
        RecordHeader record = new RecordHeader();
        record.endpointKeyHash = fieldSetFlags()[0] ? this.endpointKeyHash : (java.lang.String) defaultValue(fields()[0]);
        record.applicationToken = fieldSetFlags()[1] ? this.applicationToken : (java.lang.String) defaultValue(fields()[1]);
        record.headerVersion = fieldSetFlags()[2] ? this.headerVersion : (java.lang.Integer) defaultValue(fields()[2]);
        record.timestamp = fieldSetFlags()[3] ? this.timestamp : (java.lang.Long) defaultValue(fields()[3]);
        record.logSchemaVersion = fieldSetFlags()[4] ? this.logSchemaVersion : (java.lang.Integer) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
