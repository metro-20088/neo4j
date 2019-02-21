/*
 * Copyright (c) 2002-2019 "Neo4j,"
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
package org.neo4j.bolt.v1.messaging;

import java.io.IOException;

import org.neo4j.bolt.messaging.BoltResponseMessageWriter;
import org.neo4j.bolt.runtime.BoltConnection;
import org.neo4j.bolt.runtime.BoltResult;
import org.neo4j.bolt.v1.messaging.response.RecordMessage;
import org.neo4j.logging.Log;
import org.neo4j.values.AnyValue;
import org.neo4j.values.storable.BooleanValue;

public class ResultHandler extends MessageProcessingHandler
{
    public ResultHandler( BoltResponseMessageWriter handler, BoltConnection connection, Log log )
    {
        super( handler, connection, log );
    }

    @Override
    public boolean onPullRecords( final BoltResult result, final long size ) throws Exception
    {
        return markHasMore(
                result.handleRecords( new RecordWritingBoltResultSubscriber( ), size ) );
    }

    @Override
    public boolean onDiscardRecords( BoltResult result, long size ) throws Exception
    {
        return markHasMore( result.handleRecords( new RecordDiscardingBoltResultSubscriber(), size ) );
    }

    private class RecordWritingBoltResultSubscriber implements BoltResult.Subscriber
    {
        private AnyValue[] values;

        @Override
        public void onRecordCompleted() throws IOException
        {
            messageWriter.write( new RecordMessage( values ) );
        }

        @Override
        public void onResult( int numberOfFields )
        {
            values = new AnyValue[numberOfFields];
        }

        @Override
        public void onRecord()
        {
            //do nothing
        }

        @Override
        public void onField( int offset, AnyValue value )
        {
            values[offset] = value;
        }

        @Override
        public void addMetadata( String key, AnyValue value )
        {
            onMetadata( key, value );
        }
    }

    private class RecordDiscardingBoltResultSubscriber extends BoltResult.BaseSubscriber
    {
        @Override
        public void addMetadata( String key, AnyValue value )
        {
            onMetadata( key, value );
        }
    }

    private boolean markHasMore( boolean hasMore )
    {
        if ( hasMore )
        {
            onMetadata( "has_more", BooleanValue.TRUE );
        }
        return hasMore;
    }
}
