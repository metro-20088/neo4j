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
package org.neo4j.bolt.protocol.common.fsm.transition.transaction;

import org.neo4j.bolt.fsm.Context;
import org.neo4j.bolt.fsm.error.StateMachineException;
import org.neo4j.bolt.fsm.state.StateReference;
import org.neo4j.bolt.fsm.state.transition.AbstractStateTransition;
import org.neo4j.bolt.protocol.common.fsm.response.ResponseHandler;
import org.neo4j.bolt.protocol.common.message.request.RequestMessage;
import org.neo4j.bolt.tx.Transaction;

public abstract class TransactionalStateTransition<R extends RequestMessage> extends AbstractStateTransition<R> {

    protected TransactionalStateTransition(Class<R> requestType) {
        super(requestType);
    }

    @Override
    public final StateReference process(Context ctx, R message, ResponseHandler handler) throws StateMachineException {
        var tx = ctx.connection()
                .transaction()
                .orElseThrow(() -> new IllegalStateException("No active transaction within connection"));

        return this.process(ctx, tx, message, handler);
    }

    protected abstract StateReference process(Context ctx, Transaction tx, R message, ResponseHandler handler)
            throws StateMachineException;
}
