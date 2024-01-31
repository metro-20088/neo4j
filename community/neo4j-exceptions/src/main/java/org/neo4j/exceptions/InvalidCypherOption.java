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
package org.neo4j.exceptions;

import static java.lang.String.format;

public class InvalidCypherOption extends InvalidArgumentException {

    public InvalidCypherOption(String message) {
        super(message);
    }

    public static <T> T failWithInvalidCypherOptionCombination(
            String optionName1, String option1, String optionName2, String option2) {
        throw new InvalidCypherOption(
                format("Cannot combine %s '%s' with %s '%s'", optionName1, option1, optionName2, option2));
    }

    public static <T> T parallelRuntimeIsDisabled() {
        throw new InvalidCypherOption(
                "Parallel runtime has been disabled, please enable it or upgrade to a bigger Aura instance.");
    }

    public static <T> T invalidOption(String input, String name, String... validOptions) {
        throw new InvalidCypherOption(format(
                "%s is not a valid option for %s. Valid options are: %s",
                input, name, String.join(", ", validOptions)));
    }

    public static <T> T conflictingOptionForName(String name) {
        throw new InvalidCypherOption("Can't specify multiple conflicting values for " + name);
    }

    public static <T> T unsupportedOptions(String... keys) {
        throw new InvalidCypherOption(format("Unsupported options: %s", String.join(", ", keys)));
    }

    // NOTE: this is an internal error and should probably not have any GQL code
    public static <T> T sourceGenerationDisabled() {
        throw new InvalidCypherOption("In order to use source generation you need to enable "
                + "`internal.cypher.pipelined.allow_source_generation`");
    }
}
