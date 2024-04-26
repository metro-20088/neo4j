/*
 * Copyright (c) "Neo4j"
 * Neo4j Sweden AB [https://neo4j.com]
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.neo4j.bolt.protocol.common.message.decoder.authentication;

import org.neo4j.bolt.protocol.common.message.decoder.AbstractSingletonMessageDecoder;
import org.neo4j.bolt.protocol.common.message.request.authentication.LogoffMessage;

public final class DefaultLogoffMessageDecoder extends AbstractSingletonMessageDecoder<LogoffMessage> {

    private static final DefaultLogoffMessageDecoder INSTANCE = new DefaultLogoffMessageDecoder();

    private DefaultLogoffMessageDecoder() {}

    public static DefaultLogoffMessageDecoder getInstance() {
        return INSTANCE;
    }

    @Override
    public short getTag() {
        return LogoffMessage.SIGNATURE;
    }

    @Override
    protected LogoffMessage getMessageInstance() {
        return LogoffMessage.getInstance();
    }
}
