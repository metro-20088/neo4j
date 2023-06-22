/*
 * Copyright (c) "Neo4j"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.internal.schema.constraints;

import java.util.Set;
import org.neo4j.graphdb.schema.PropertyType;
import org.neo4j.values.storable.Value;
import org.neo4j.values.storable.ValueRepresentation;

/**
 * Note: ordering and name (user description) is defined by CIP-100.
 */
public enum SchemaValueType implements TypeRepresentation {
    BOOLEAN("BOOLEAN", Ordering.BOOLEAN_ORDER, ValueRepresentation.BOOLEAN),
    STRING("STRING", Ordering.STRING_ORDER, ValueRepresentation.UTF8_TEXT, ValueRepresentation.UTF16_TEXT),
    INTEGER(
            "INTEGER",
            Ordering.INTEGER_ORDER,
            ValueRepresentation.INT64,
            ValueRepresentation.INT32,
            ValueRepresentation.INT16,
            ValueRepresentation.INT8),
    FLOAT("FLOAT", Ordering.FLOAT_ORDER, ValueRepresentation.FLOAT64, ValueRepresentation.FLOAT32),
    DATE("DATE", Ordering.DATE_ORDER, ValueRepresentation.DATE),
    LOCAL_TIME("LOCAL TIME", Ordering.LOCAL_TIME_ORDER, ValueRepresentation.LOCAL_TIME),
    ZONED_TIME("ZONED TIME", Ordering.ZONED_TIME_ORDER, ValueRepresentation.ZONED_TIME),
    LOCAL_DATETIME("LOCAL DATETIME", Ordering.LOCAL_DATETIME_ORDER, ValueRepresentation.LOCAL_DATE_TIME),
    ZONED_DATETIME("ZONED DATETIME", Ordering.ZONED_DATETIME_ORDER, ValueRepresentation.ZONED_DATE_TIME),
    DURATION("DURATION", Ordering.DURATION_ORDER, ValueRepresentation.DURATION),
    POINT("POINT", Ordering.POINT_ORDER, ValueRepresentation.GEOMETRY);

    // Future list types
    // LIST_BOOLEAN("LIST<BOOLEAN>", ValueRepresentation.BOOLEAN_ARRAY),
    // LIST_STRING("LIST<STRING>", ValueRepresentation.TEXT_ARRAY),
    // LIST_INTEGER(
    //         "LIST<INTEGER>",
    //         ValueRepresentation.INT64_ARRAY,
    //         ValueRepresentation.INT32_ARRAY,
    //         ValueRepresentation.INT16_ARRAY,
    //         ValueRepresentation.INT8_ARRAY),
    // LIST_FLOAT("LIST<FLOAT>", ValueRepresentation.FLOAT64_ARRAY, ValueRepresentation.FLOAT32_ARRAY),
    // LIST_DATE("LIST<DATE>", ValueRepresentation.DATE_ARRAY),
    // LIST_LOCAL_TIME("LIST<LOCAL TIME>", ValueRepresentation.LOCAL_TIME_ARRAY),
    // LIST_ZONED_TIME("LIST<ZONED TIME>", ValueRepresentation.ZONED_TIME_ARRAY),
    // LIST_LOCAL_DATETIME("LIST<LOCAL DATETIME>", ValueRepresentation.LOCAL_DATE_TIME_ARRAY),
    // LIST_ZONED_DATETIME("LIST<ZONED DATETIME>", ValueRepresentation.ZONED_DATE_TIME_ARRAY),
    // LIST_DURATION("LIST<DURATION>", ValueRepresentation.DURATION_ARRAY),
    // LIST_POINT("LIST<POINT>", ValueRepresentation.GEOMETRY_ARRAY);

    private final String userDescription;
    private final Set<ValueRepresentation> valueRepresentations;
    private final Ordering order;

    SchemaValueType(String userDescription, Ordering order, ValueRepresentation... valueRepresentations) {
        this.userDescription = userDescription;
        this.order = order;
        this.valueRepresentations = Set.of(valueRepresentations);
    }

    public boolean isAssignable(Value value) {
        return valueRepresentations.contains(value.valueRepresentation());
    }

    public boolean isAssignable(ValueRepresentation valueRepresentation) {
        return valueRepresentations.contains(valueRepresentation);
    }

    public String userDescription() {
        return userDescription;
    }

    @Override
    public Ordering order() {
        return order;
    }

    public String serialize() {
        return this.name();
    }

    @Override
    public String toString() {
        return userDescription;
    }

    public static SchemaValueType fromPublicApi(PropertyType propertyType) {
        return switch (propertyType) {
            case BOOLEAN -> SchemaValueType.BOOLEAN;
            case STRING -> SchemaValueType.STRING;
            case INTEGER -> SchemaValueType.INTEGER;
            case FLOAT -> SchemaValueType.FLOAT;
            case DURATION -> SchemaValueType.DURATION;
            case DATE -> SchemaValueType.DATE;
            case ZONED_DATETIME -> SchemaValueType.ZONED_DATETIME;
            case LOCAL_DATETIME -> SchemaValueType.LOCAL_DATETIME;
            case ZONED_TIME -> SchemaValueType.ZONED_TIME;
            case LOCAL_TIME -> SchemaValueType.LOCAL_TIME;
            case POINT -> SchemaValueType.POINT;
        };
    }

    public PropertyType toPublicApi() {
        return switch (this) {
            case BOOLEAN -> PropertyType.BOOLEAN;
            case STRING -> PropertyType.STRING;
            case INTEGER -> PropertyType.INTEGER;
            case FLOAT -> PropertyType.FLOAT;
            case DURATION -> PropertyType.DURATION;
            case DATE -> PropertyType.DATE;
            case ZONED_DATETIME -> PropertyType.ZONED_DATETIME;
            case LOCAL_DATETIME -> PropertyType.LOCAL_DATETIME;
            case ZONED_TIME -> PropertyType.ZONED_TIME;
            case LOCAL_TIME -> PropertyType.LOCAL_TIME;
            case POINT -> PropertyType.POINT;
                // FIXME PTC remove default when we have all the types in Core API
            default -> throw new IllegalArgumentException(
                    "Property type '" + this.userDescription() + "' is not supported in Core API yet.");
        };
    }
}