/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tinkerpop.machine.beam;

import org.apache.tinkerpop.machine.function.FilterFunction;
import org.apache.tinkerpop.machine.traverser.Traverser;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FilterFn<C, S> extends AbstractFn<C, S, S> {

    private FilterFunction<C, S> filterFunction;

    public FilterFn(final FilterFunction<C, S> filterFunction) {
        super(filterFunction);
        this.filterFunction = filterFunction;
    }

    @ProcessElement
    public void processElement(final @Element Traverser<C, S> traverser, final OutputReceiver<Traverser<C, S>> output) {
        final Traverser<C, S> clone = traverser.split(this.filterFunction, traverser.object());
        clone.coefficient().set(traverser.coefficient().clone().value()); // TODO: get rid of this
        if (clone.filter(this.filterFunction))
            output.output(clone);
    }
}