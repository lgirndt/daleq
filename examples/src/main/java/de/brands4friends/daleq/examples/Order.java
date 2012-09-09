/*
 * Copyright 2012 brands4friends, Private Sale GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.brands4friends.daleq.examples;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

public class Order {

    private final long id;
    private final long customerId;
    private final DateTime creation;

    public Order(final long id, final long customerId, final DateTime creation) {
        this.id = id;
        this.customerId = customerId;
        this.creation = creation;
    }

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public DateTime getCreation() {
        return creation;
    }

    @Override
    public final String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("customerId", customerId)
                .add("creation", creation)
                .toString();
    }
}
