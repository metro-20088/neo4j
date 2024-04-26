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
package org.neo4j.bolt.testing.extension.initializer;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.neo4j.bolt.fsm.StateMachine;
import org.neo4j.bolt.fsm.error.StateMachineException;
import org.neo4j.bolt.protocol.common.fsm.States;
import org.neo4j.bolt.testing.annotation.fsm.initializer.Streaming;
import org.neo4j.bolt.testing.assertions.ResponseRecorderAssertions;
import org.neo4j.bolt.testing.assertions.StateMachineAssertions;
import org.neo4j.bolt.testing.extension.dependency.StateMachineDependencyProvider;
import org.neo4j.bolt.testing.fsm.StateMachineProvider;
import org.neo4j.bolt.testing.response.ResponseRecorder;

public class StreamingStateMachineInitializer implements StateMachineInitializer {

    @Override
    public void initialize(
            ExtensionContext extensionContext,
            ParameterContext parameterContext,
            StateMachineDependencyProvider dependencyProvider,
            StateMachineProvider provider,
            StateMachine fsm)
            throws StateMachineException {
        var recorder = new ResponseRecorder();

        var annotation = parameterContext
                .findAnnotation(Streaming.class)
                .map(Streaming::value)
                .filter(it -> !it.isBlank())
                .orElse("CREATE (n {k:'k'}) RETURN n.k");

        fsm.process(provider.messages().run(annotation), recorder);

        ResponseRecorderAssertions.assertThat(recorder).hasSuccessResponse();

        StateMachineAssertions.assertThat(fsm).isInState(States.IN_TRANSACTION);
    }
}
